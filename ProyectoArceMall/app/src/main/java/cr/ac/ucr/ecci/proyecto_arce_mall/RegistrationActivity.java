package cr.ac.ucr.ecci.proyecto_arce_mall;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.DatePicker;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Provinces;

public class RegistrationActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private double currentLatitude = 0;
    private double currentLongitude = 0;
    private TextInputLayout tilLocation;
    LocationManager locationManager;
    private EditText tilBirthDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        tilLocation = (TextInputLayout) findViewById(R.id.til_location);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
        compareLocation();
        tilBirthDate = findViewById(R.id.birthDate_field);
        tilBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePickerDialog();
            }
        });
    }


    //TODO: probar esto con un celular que tenga desde el inicio el gps desactivado
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
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
        if (ActivityCompat.checkSelfPermission(
                RegistrationActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                RegistrationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                currentLatitude = locationGPS.getLatitude();
                currentLongitude = locationGPS.getLongitude();
            }
        }
    }
    private void compareLocation(){
        if(currentLatitude == 0 && currentLongitude == 0){
            tilLocation.getEditText().setText("San José");
        }else {
            Map<String, Float> maps = new HashMap<String, Float>();
            float results[] = new float[8];
            for (Provinces province : Provinces.values()) {
                Location.distanceBetween(currentLatitude, currentLongitude, province.getLatitude(), province.getLongitude(), results);
                maps.put(province.getName(), results[0]);
            }
            tilLocation.getEditText().setText(Collections.min(maps.entrySet(),
                    Map.Entry.comparingByValue()).getKey());
        }
    }

    private void getDatePickerDialog(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegistrationActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tilBirthDate.setText(dayOfMonth + "/" + dayOfMonth + "/" + year);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    public void saveUser(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}