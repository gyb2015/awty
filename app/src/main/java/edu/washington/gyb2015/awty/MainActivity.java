package edu.washington.gyb2015.awty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private boolean running;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button startBtn = (Button) findViewById(R.id.button);
        final EditText intervalTxt = (EditText) findViewById(R.id.interval);
        final EditText phoneTxt = (EditText) findViewById(R.id.phone);
        final EditText messageTxt = (EditText) findViewById(R.id.message);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        running = (PendingIntent.getBroadcast(MainActivity.this, 0,
                new Intent(MainActivity.this, AlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        if(running){
            startBtn.setText("Stop Alarm");
        }else{
            Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    cancel();
                    startBtn.setText("Start Alarm");
                } else if(validate(intervalTxt, phoneTxt, messageTxt)) {
                    String phone = phoneTxt.getText().toString();
                    phone = phone.replaceAll("\\D+", "");
                    phone = phone.substring(0,3) + "-" + phone.substring(3,6) +"-"+ phone.substring(6);
                    phone = "(" + phone.substring(0,3) + ") " + phone.substring(4);
                    phoneTxt.setText(phone);
                    String interval = intervalTxt.getText().toString();
                    String message = messageTxt.getText().toString();
                    start(Integer.parseInt(interval),phone,message);
                    startBtn.setText("Stop ALARM");
                }
            }
        });
    }
    public void start(int interval, String phone, String message){
        running = true;
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("phone", phone);
        mBundle.putString("interval", String.valueOf(interval));
        mBundle.putString("message", message);
        alarmIntent.putExtras(mBundle);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (interval * 1000 * 60), pendingIntent);
        Toast.makeText(this, phone + message, Toast.LENGTH_SHORT).show();
    }
    public void cancel(){

        if(pendingIntent == null) {
            Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        running = false;
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private boolean validate(EditText interval, EditText phone, EditText message) {

        if(!phone.getText().toString().isEmpty()) {
            String phoneNum = phone.getText().toString();
            phoneNum = phoneNum.replaceAll("\\D+", "");
            phone.setText(phoneNum);
            if(phoneNum.length() != 10) {
                Toast.makeText(this, "Phone number should be 10 digits", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if(interval.getText().toString().isEmpty() || message.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in the required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

