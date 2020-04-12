package com.android.coronahack.habitus_venditor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.coronahack.habitus_venditor.R;
import com.android.coronahack.habitus_venditor.helpers.GlobalData;

public class MainActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    AlertDialog alertDialog = null;
    RadioButton type;
    public static EditText name;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    TextView welcome, shop_type;
    ImageView shop_image;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Handler handler;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler  = new Handler();
        startRepeatingTask();

        welcome = findViewById(R.id.welcome_text);
        shop_type = findViewById(R.id.shop_type);
        shop_image = findViewById(R.id.shopButton);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean("firstStart", true);
        GlobalData.name = sharedPreferences.getString("name", "GUEST");
        GlobalData.phNum = sharedPreferences.getString("phNum", "NULL");
        GlobalData.type = sharedPreferences.getString("type", "prescriptions");

        if (firstStart) {
            startRegistration();
        } else {
            welcome.setText(GlobalData.name.toUpperCase());
            if (GlobalData.type.equals("prescriptions")) {
                shop_type.setText("Medical shop");
                shop_image.setImageResource(R.drawable.medicine);
            } else if (GlobalData.type.equals("groceries")) {
                shop_type.setText("Grocery store");
                shop_image.setImageResource(R.drawable.groceries);
            }
        }

        shop_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CheckRequestsActivity.class));
            }
        });
    }

    private void startRegistration() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View registerView = LayoutInflater.from(this).inflate(R.layout.register_layout, viewGroup, false);

        if (!isConnected()) {
            AlertDialog alertDialogError;
            AlertDialog.Builder builderError = new AlertDialog.Builder(MainActivity.this);
            builderError.setMessage("Please switch on your internet connection and restart your app for hassle-free usage!")
                    .setCancelable(false);
            alertDialogError = builderError.create();
            alertDialogError.setTitle("Connectivity Issue");
            alertDialogError.show();
        } else {
            builder = new AlertDialog.Builder(this);
            builder.setView(registerView);
            alertDialog = builder
                    .setCancelable(false)
                    .create();
            alertDialog.show();
        }

        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        name = registerView.findViewById(R.id.your_shop);
        final EditText phNumber = registerView.findViewById(R.id.phNum);
        final RadioGroup radioGroup = registerView.findViewById(R.id.radioGroup);
        final Button registerButton = registerView.findViewById(R.id.registerButton);
        ImageView getLocation = registerView.findViewById(R.id.locationButton);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    int select = radioGroup.getCheckedRadioButtonId();
                    RadioButton typeIntent = registerView.findViewById(select);
                    if (select == -1) {
                        Toast.makeText(MainActivity.this, "Please select your shop type first!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        String radioType = typeIntent.getText().toString();
                        if (radioType.equals("Medical shop")) {
                            intent.putExtra("type", "medicine");
                        } else if (radioType.equals("Grocery store")) {
                            intent.putExtra("type", "grocery");
                        }
                        startActivity(intent);
                    }
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String getName, getPhNum;
                getName = name.getText().toString();
                getPhNum = phNumber.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                type = registerView.findViewById(selectedId);

                if (getName.length() == 0 || getPhNum.length() == 0 || getPhNum.equals(" ") || selectedId == -1) {
                    Toast.makeText(MainActivity.this, "Enter all the credentials", Toast.LENGTH_SHORT).show();
                } else if (getPhNum.length() != 10) {
                    Toast.makeText(MainActivity.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putBoolean("firstStart", false);
                    editor.putString("name", getName);
                    GlobalData.name = getName;
                    welcome.setText(getName.toUpperCase());
                    editor.putString("phNum", getPhNum);
                    GlobalData.phNum = getPhNum;
                    if (type.getText().toString().equals("Medical shop")) {
                        editor.putString("type", "prescriptions");
                        GlobalData.type = "prescriptions";
                        shop_type.setText("Medical shop");
                        shop_image.setImageResource(R.drawable.medicine);
                    } else if (type.getText().toString().equals("Grocery store")) {
                        editor.putString("type", "groceries");
                        GlobalData.type = "groceries";
                        shop_type.setText("Grocery store");
                        shop_image.setImageResource(R.drawable.groceries);
                    }
                    editor.apply();
                    alertDialog.cancel();
                }

            }
        });

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
                    Toast.makeText(MainActivity.this, "Please maintain distance from others!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission Denied! Please restart the app to access this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
