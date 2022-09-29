package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.Common;

public class MainActivity extends AppCompatActivity {

    private class NetworkChangeListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(!Common.isConnectedToInternet(context)) {

                this.goToMainScreen();

                AlertDialog.Builder builder =  new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context)
                                          .inflate(R.layout.no_internet_connection,null);
                builder.setView(view);

                AppCompatButton retryButton = view.findViewById(R.id.retry_button);

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);
                dialog.getWindow().setGravity(Gravity.CENTER);

                retryButton.setOnClickListener(view1 -> {
                    dialog.dismiss();
                    onReceive(context,intent);
                });
            }
        }

        private void goToMainScreen() {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(this.networkChangeListener,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(this.networkChangeListener);
        super.onStop();
    }

    public void showRegistrationScreen(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void showLoginScreen(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}