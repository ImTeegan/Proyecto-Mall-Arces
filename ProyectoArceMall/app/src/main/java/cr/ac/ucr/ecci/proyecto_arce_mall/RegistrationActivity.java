package cr.ac.ucr.ecci.proyecto_arce_mall;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Provinces;
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;

public class RegistrationActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private double currentLatitude = 0;
    private double currentLongitude = 0;
    private String province;
    private TextInputLayout tilLocation;
    private TextInputLayout tilIdentification;
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilBirthDate;
    private LocationManager locationManager;
    private EditText birthDate;
    private Button registrationButton;
    private DbHelper dataBase;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.instantiateComponents();
        this.setComponentActions();
    }

    /**
     * Register the receiver that checks
     * if the user has internet at every moment
     */
    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(this.networkChangeListener, intentFilter);
        super.onStart();
    }

    /**
     * Unregister the receiver that checks
     * if the user has internet at every moment
     */
    @Override
    protected void onStop() {
        unregisterReceiver(this.networkChangeListener);
        super.onStop();
    }

    private void instantiateComponents() {
        this.tilLocation = (TextInputLayout) findViewById(R.id.til_location);
        this.locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        this.birthDate = findViewById(R.id.birth_date_field);
        this.tilBirthDate = findViewById(R.id.til_birth_date);
        this.tilIdentification = findViewById(R.id.til_identification);
        this.tilName = findViewById(R.id.til_name);
        this.tilEmail = findViewById(R.id.til_email);
        this.registrationButton = (Button) findViewById(R.id.registration_button);
        this.dataBase = new DbHelper(this);
    }


    private void setComponentActions() {
        this.getLocation();
        this.compareLocation();

        // Set birth date field click action.
        this.birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatePickerDialog();
            }
        });

        // Set registration button click action.
        this.registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validateData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // TODO: Test with a cell phone that has GPS disabled from the start
    private void showEnableGpsDialog() {
        final Builder builder = new Builder(this);

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
                RegistrationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int approxLocationPermission = ActivityCompat.checkSelfPermission(
                RegistrationActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if ((preciseLocationPermission != PackageManager.PERMISSION_GRANTED)
                && (approxLocationPermission != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {

            Location locationGps = this.locationManager.getLastKnownLocation(LocationManager
                    .GPS_PROVIDER);

            if (locationGps != null) {
                this.currentLatitude = locationGps.getLatitude();
                this.currentLongitude = locationGps.getLongitude();
            }
        }
    }

    private void compareLocation() {
        if (this.currentLatitude == 0 && this.currentLongitude == 0) {
            province = "San José";
            this.tilLocation.getEditText().setText("San José");
        } else {
            Map<String, Float> map = new HashMap<String, Float>();
            float[] results = new float[8];

            for (Provinces province : Provinces.values()) {

                Location.distanceBetween(this.currentLatitude,
                        this.currentLongitude,
                        province.getLatitude(),
                        province.getLongitude(),
                        results);
                map.put(province.getName(), results[0]);
            }
            province = (Collections.min(map.entrySet(),
                    Map.Entry.comparingByValue()).getKey());
            tilLocation.getEditText().setText(province);
        }
    }


    private void getDatePickerDialog() {
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
                        birthDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private boolean validateIdentification(String identification) {
        Pattern pattern = Pattern.compile("^[0-9]+$");

        if (!pattern.matcher(identification).matches() || identification.isEmpty()) {
            this.tilIdentification.setError("La identificación no es válida");
            return false;
        }

        this.tilIdentification.setError(null);
        return true;
    }

    private boolean validateName(String name) {
        Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");

        if (!pattern.matcher(name).matches() || name.isEmpty()) {
            this.tilName.setError("El nombre no es válido");
            return false;
        }

        this.tilName.setError(null);
        return true;
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            this.tilEmail.setError("El correo electrónico no es válido");
            return false;
        }

        this.tilEmail.setError(null);
        return true;
    }

    private boolean validateBirthDate(String birthDate) {
        if (!birthDate.isEmpty()) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate pastDate = LocalDate.parse(birthDate, formatter);
            boolean isBefore = pastDate.isBefore(today);

            if (!isBefore) {
                this.tilBirthDate.setError("La fecha ingresada no es válida");
            } else {
                this.tilBirthDate.setError(null);
            }

            return isBefore;
        }

        this.tilBirthDate.setError("La fecha ingresada no es válida");
        return false;
    }

    private void validateData() throws Exception {
        String identification = tilIdentification.getEditText().getText().toString();
        String name = tilName.getEditText().getText().toString();
        String email = tilEmail.getEditText().getText().toString();
        String date = birthDate.getText().toString();

        boolean validIdentification = validateIdentification(identification);
        boolean validName = validateName(name);
        boolean validEmail = validateEmail(email);
        boolean validBirthDate = validateBirthDate(date);

        if (validBirthDate && validEmail && validName && validIdentification) {
            // 1 means TRUE
            User newUser = new User(identification, name, email, date, province, 1, 0, null);
            showConfirmationScreen(newUser);
        }
    }

    /**
     * Shows the confirmation screen after the user is register
     * @param user the user that is register
     */
    private void showConfirmationScreen(User user) {
        Uri image = Uri.parse("android.resource://cr.ac.ucr.ecci.proyecto_arce_mall/drawable/profile_image");
        dataBase.addUserFb(user,image);
        Intent intent = new Intent(  this, RegisterConfirmationActivity.class);
        intent.putExtra("email", user.getEmail());
        intent.putExtra("password", user.getPassword());
        startActivity(intent);
    }
}