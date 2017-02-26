package edu.washington.gjdevera.awty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String phone = intent.getStringExtra("phone");
        String message = intent.getStringExtra("message");
        Toast.makeText(context, String.format(context.getResources().getString(R.string.sent), phone, message),
                Toast.LENGTH_LONG).show();

        Log.i("Send SMS", "");
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts(message, phone, null)));
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "SMS sending failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
