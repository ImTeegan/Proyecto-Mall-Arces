package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.firebase.FirebaseApp;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    private DbHelper dataBase;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBase = new DbHelper(this);

        this.locationManager = (LocationManager)
                                getSystemService(Context.LOCATION_SERVICE);

        if (!this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            this.showEnableGpsDialog();
        } else {
            this.getLocation();
        }
        //init all app if there is a logged user
       //User user =  dataBase.getLoginUser();
//        if(user.getEmail() != null){
//            Intent intent = new Intent(this, StoreActivity.class);
//            startActivity(intent);
//            finish();
//        }
        initFirebase();
    }

    /**
     * Method to initialize firebase app
     */
    private void initFirebase(){
        FirebaseApp.initializeApp(this);
    }


    // TODO: Test with a cell phone that has GPS disabled from the start
    private void showEnableGpsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }

                }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void getLocation() {
        // Get permission values for precise and approximate location.
        int preciseLocationPermission = ActivityCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int approxLocationPermission = ActivityCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if ((preciseLocationPermission != PackageManager.PERMISSION_GRANTED)
                && (approxLocationPermission != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_LOCATION);
        }
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