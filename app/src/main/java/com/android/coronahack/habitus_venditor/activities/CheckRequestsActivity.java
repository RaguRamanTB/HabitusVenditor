package com.android.coronahack.habitus_venditor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
    Boolean pres = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_requests);

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
                            if (uploadRequest.prescriptionLink.length() != 0) {
                                pres = true;
                            }
                            getRequestsPending.add(new GetRequest(uploadRequest.customerName,uploadRequest.phNum,uploadRequest.customerAddress, uploadRequest.mList, pres));
                        } else {
                            if (uploadRequest.prescriptionLink.length() != 0) {
                                pres = true;
                            }
                            getRequestsAccepted.add(new GetRequest(uploadRequest.customerName,uploadRequest.phNum,uploadRequest.customerAddress, uploadRequest.mList, pres));
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
}
