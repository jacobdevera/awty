package edu.washington.gjdevera.awty;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private boolean started = false;
    private long intentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button btn =  (Button) findViewById(R.id.start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!started) {
                    TextInputEditText message = (TextInputEditText) findViewById(R.id.message);
                    TextInputEditText phone = (TextInputEditText) findViewById(R.id.phone);
                    TextInputEditText interval = (TextInputEditText) findViewById(R.id.interval);
                    if (TextUtils.isEmpty(message.getText().toString())) {
                        Toast.makeText(getApplication(),
                                "Please enter a message", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean phoneValid = android.util.Patterns.PHONE.matcher(phone.getText()).matches();
                    if (!phoneValid) {
                        Toast.makeText(getApplication(),
                                "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String intervalString = interval.getText().toString();
                    boolean intervalValid = TextUtils.isDigitsOnly(interval.getText())
                            && !intervalString.equals("") && Integer.parseInt(intervalString) > 0;
                    if (!intervalValid) {
                        Toast.makeText(getApplication(),
                                "Please enter an interval greater than 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // passed validation checks
                    btn.setText(getResources().getString(R.string.stop));
                    started = true;

                    intentId = System.currentTimeMillis();
                    Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                    intent.putExtra("phone", phone.getText().toString());
                    intent.putExtra("message", message.getText().toString());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1001, intent, 0);
                    int intervalTimer = Integer.parseInt(intervalString) * 1000 * 60;
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalTimer, pendingIntent);
                } else {
                    btn.setText(getResources().getString(R.string.start));
                    started = false;
                    Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    manager.cancel(pendingIntent);
                }
            }
        });
    }
}
