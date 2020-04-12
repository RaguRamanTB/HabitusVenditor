package com.android.coronahack.habitus_venditor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.coronahack.habitus_venditor.R;
import com.android.coronahack.habitus_venditor.helpers.GetRequest;
import com.android.coronahack.habitus_venditor.helpers.GetRequestAdapter;
import com.android.coronahack.habitus_venditor.helpers.GlobalData;
import com.android.coronahack.habitus_venditor.helpers.UploadRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CheckRequestsActivity extends AppCompatActivity {

    RecyclerView pendingRecycler, approvedRecycler;
    RecyclerView.Adapter pAdapter, aAdapter;
    List<GetRequest> getRequestsAccepted, getRequestsPending;
    DatabaseReference reference;
    ValueEventListener valueEventListener;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_requests);

        handler  = new Handler();
        startRepeatingTask();

        Toolbar toolbar = findViewById(R.id.checkRequests);
        toolbar.setTitle("Your Requests");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pendingRecycler = findViewById(R.id.pendingRecycler);
        approvedRecycler = findViewById(R.id.approvedRecycler);

        getRequestsAccepted = new ArrayList<>();
        getRequestsPending = new ArrayList<>();

        pendingRecycler.setHasFixedSize(true);
        approvedRecycler.setHasFixedSize(true);

        pendingRecycler.setLayoutManager(new LinearLayoutManager(this));
        approvedRecycler.setLayoutManager(new LinearLayoutManager(this));

        pAdapter = new GetRequestAdapter(CheckRequestsActivity.this, getRequestsPending);
        aAdapter = new GetRequestAdapter(CheckRequestsActivity.this, getRequestsAccepted);

        pendingRecycler.setAdapter(pAdapter);
        approvedRecycler.setAdapter(aAdapter);

        reference = FirebaseDatabase.getInstance().getReference().child(GlobalData.type);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getRequestsAccepted.clear();
                getRequestsPending.clear();
                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                    UploadRequest uploadRequest = dss.getValue(UploadRequest.class);
                    assert uploadRequest != null;
                    if (uploadRequest.shopName.equals(GlobalData.name)) {
                        if (uploadRequest.mKey == 0) {
                            if (uploadRequest.prescriptionLink.length() > 0) {
                                getRequestsPending.add(new GetRequest(uploadRequest.customerName, uploadRequest.phNum, uploadRequest.customerAddress, uploadRequest.mList, true, uploadRequest.prescriptionLink));
                            } else {
                                getRequestsPending.add(new GetRequest(uploadRequest.customerName, uploadRequest.phNum, uploadRequest.customerAddress, uploadRequest.mList, false));
                            }
                        } else {
                            if (uploadRequest.prescriptionLink.length() > 0) {
                                getRequestsAccepted.add(new GetRequest(uploadRequest.customerName, uploadRequest.phNum, uploadRequest.customerAddress, uploadRequest.mList, true, uploadRequest.prescriptionLink));
                            } else {
                                getRequestsAccepted.add(new GetRequest(uploadRequest.customerName, uploadRequest.phNum, uploadRequest.customerAddress, uploadRequest.mList, false));
                            }
                        }
                    }
                }
                pAdapter.notifyDataSetChanged();
                aAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        reference.addValueEventListener(valueEventListener);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                bluetoothAdapter.startDiscovery();
            } finally {
                handler.postDelayed(runnable, 1000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    private void stopRepeatingTask() {
        handler.removeCallbacks(runnable);
    }

    private void startRepeatingTask() {
        runnable.run();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                Log.d("Bluetooth", name + " => "+ rssi);
                if (rssi > -68) {
                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 2000);
                    Toast.makeText(CheckRequestsActivity.this, "Please maintain distance from others!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
