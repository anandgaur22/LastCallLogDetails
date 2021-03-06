package com.appzdigital.calllog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]
                        {
                                Manifest.permission.READ_CALL_LOG
                        }, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]
                        {
                                Manifest.permission.READ_CALL_LOG
                        }, 1);
            }
        } else {
            //do stuff
            textView=findViewById(R.id.textCall);
            textView.setText(getCallDetail());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                       // Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();

                        textView=findViewById(R.id.textCall);
                        textView.setText(getCallDetail());

                      //  textView.setText(getCallDetail());
                    }
                } else {
                    Toast.makeText(this, "No permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private String getCallDetail() {
        StringBuffer sb = new StringBuffer();
        @SuppressLint("MissingPermission") Cursor manageCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = manageCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = manageCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = manageCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = manageCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details:\n\n");
        int counter = 0;

        while (manageCursor.moveToLast()) {
            if(counter==0) {
                String phoneNumber = manageCursor.getString(number);
                String callType = manageCursor.getString(type);
                String callDate = manageCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yy HH:mm");
                String dateString = formater.format(callDayTime);
                String callDuration = manageCursor.getString(duration);
                String dir = null;
                int dirCode = Integer.parseInt(callType);
                switch (dirCode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "Outgoing";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "Incoming";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        dir = "Missed";
                        break;
                }
                sb.append("\n Phone Number:" + phoneNumber + " \n Call Type :" + dir + "\n Call Date:" + dateString + " \n Call Duration :" + callDuration);
                counter++;
                sb.append("\n--------------------------------------------------");
            }else{
                break;
            }


        }
        manageCursor.close();
        return sb.toString();
    }
}