package com.android.on_boardsystemproject;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BeforeDeviceList extends AppCompatActivity {

    //bluetooth
    private BluetoothAdapter myBluetooth = null;

    // widgets
    Button btnPaired;
    TextView textView;
    String Newligne=System.getProperty("line.separator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_device_list);

        //call the widgets
        btnPaired = (Button) findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        textView.setText("App made by:"+Newligne+"Thomas Chamard"+Newligne+"Léo Chauvin"+Newligne+"I1A2");

        //we check if the device has got the bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        //if not...
        if(myBluetooth == null)
        {
            //show a message that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            //finish apk
            finish();
        }
        //else, if the device has the bluetooth adapter, but it's off...
        else if (!myBluetooth.isEnabled())
        {
            //Ask to the user to turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            // if you click on the button
            public void onClick(View v) {
                //send a message to the next activity to start it
                Intent i = new Intent(BeforeDeviceList.this, DeviceList.class);
                //change the activity
                startActivity(i);
            }
        });
    }
}
