package cr.ac.ucr.ecci.proyecto_arce_mall;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.os.Build;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Provinces;

public class RegistrationActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private double currentLatitude = 0;
    private double currentLongitude = 0;
    private TextInputLayout tilLocation;
    private TextInputLayout tilIdentification;
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilBirthDate;
    private LocationManager locationManager;
    private EditText birthDate;
    private Button registrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        this.instantiateComponents();
        this.setComponentActions();
    }

    private void instantiateComponents() {
        this.tilLocation = (TextInputLayout) findViewById(R.id.til_location);
        this.locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        this.birthDate = findViewById(R.id.birth_date_field);
        this.tilBirthDate = findViewById(R.id.til_birthDate);
        this.tilIdentification = findViewById(R.id.til_identification);
        this.tilName = findViewById(R.id.til_name);
        this.tilEmail = findViewById(R.id.til_email);
        this.registrationButton = (Button) findViewById(R.id.registration_button);
    }

    private void setComponentActions() {
        // Verify if GPS provider is enabled or not.
        if (!this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            this.showEnableGpsDialog();
        } else {
            this.getLocation();
        }
        this.compareLocation();

        // Set birth date field click action.
        this.birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatePickerDialog();
            }
        });

        // Set registration button click action.
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validateData();
                } catch (ParseException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    // TODO: Test with a cell phone that has GPS disabled from the start
    private void showEnableGpsDialog() {
        final Builder builder = new Builder(this);

        builder.setMessage("Enable GPS")
               .setCancelable(false)
               .setPositiveButton("Yes", new  DialogInterface.OnClickListener() {

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
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
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
            this.tilLocation.getEditText()
                    .setText(Collections.min(map.entrySet(),
                            Map.Entry.comparingByValue()).getKey());
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
                    birthDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                }
            }, year, month, day);

        datePickerDialog.show();
    }

    private boolean validateIdentification(String identification) {
        Pattern pattern = Pattern.compile("^[0-9]+$");

        if (!pattern.matcher(identification).matches() || identification.isEmpty()) {
            tilIdentification.setError("La identificación no es válida");
            return false;
        }

        tilIdentification.setError(null);
        return true;
    }

    private boolean validateName(String name) {
        Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");

        if (!pattern.matcher(name).matches() || name.isEmpty()) {
            tilName.setError("El nombre no es válido");
            return false;
        }

        tilName.setError(null);
        return true;
    }

    private boolean validateEmail(String email){
        if (email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            tilEmail.setError("El correo electrónico no es válido");
            return false;
        }

        tilEmail.setError(null);
        return true;
    }

    private boolean validateBirthDate(String birthDate) throws ParseException {
        if (!birthDate.isEmpty()) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate pastDate = LocalDate.parse(birthDate, formatter);
            boolean isBefore = pastDate.isBefore(today);

            if (!isBefore) {
                tilBirthDate.setError("La fecha ingresada no es válida");
            } else {
                tilBirthDate.setError(null);
            }

            return isBefore;
        }

        tilBirthDate.setError("La fecha ingresada no es válida");
        return false;
    }

    private void validateData() throws ParseException {
        String identification = tilIdentification.getEditText().getText().toString();
        String name = tilName.getEditText().getText().toString();
        String email = tilEmail.getEditText().getText().toString();
        String date = birthDate.getText().toString();

        boolean validIdentification = validateIdentification(identification);
        boolean validName = validateName(name);
        boolean validEmail = validateEmail(email);
        boolean validBirthDate = validateBirthDate(date);

        if (validBirthDate && validEmail && validName && validIdentification) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show();
            // saveUser();
            showConfirmationScreen();
        }
    }

    // TODO: Change this method to save an user.
    private void saveUser() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showConfirmationScreen() {
        Intent intent = new Intent(this, RegisterConfirmationActivity.class);
        startActivity(intent);
    }
}