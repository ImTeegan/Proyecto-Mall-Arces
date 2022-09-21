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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
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

public class RegistrationActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private double currentLatitude = 0;
    private double currentLongitude = 0;
    private TextInputLayout tilLocation;
    private TextInputLayout tilIdentification;
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilBirthDate;
    private String province;
    LocationManager locationManager;
    private EditText birthDate;
    private DbHelper DataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBase = new DbHelper(this);
        setContentView(R.layout.activity_registration);
        tilLocation = (TextInputLayout) findViewById(R.id.til_location);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
        compareLocation();
        birthDate = findViewById(R.id.birthDate_field);
        tilBirthDate = findViewById(R.id.til_birthDate);
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePickerDialog();
            }
        });
        tilIdentification = findViewById(R.id.til_identification);
        tilName = findViewById(R.id.til_name);
        tilEmail = findViewById(R.id.til_email);
        Button registrationButon = (Button) findViewById(R.id.registerButton);
        registrationButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validateData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
            province = (Collections.min(maps.entrySet(),
                    Map.Entry.comparingByValue()).getKey());
            tilLocation.getEditText().setText(province);
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
                        birthDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private boolean validateIdentification(String identification){
        Pattern patron = Pattern.compile("^[0-9]+$");
        if (!patron.matcher(identification).matches() || identification.isEmpty()) {
            tilIdentification.setError("La identificación no es válida");
            return false;
        } else {
            tilIdentification.setError(null);
        }
        return true;
    }

    private boolean validateName(String name){
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(name).matches() || name.isEmpty()) {
            tilName.setError("El nombre no es válido");
            return false;
        } else {
            tilName.setError(null);
        }
        return true;
    }

    private boolean validateEmail(String email){
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("El correo electronico no es válido");
            return false;
        } else {
            tilEmail.setError(null);
        }
        return true;
    }

    private boolean validateBirthDate(String birthDate) throws ParseException {
        if (!birthDate.isEmpty()){
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate pastDate = LocalDate.parse(birthDate, formatter);
            boolean isBefore = pastDate.isBefore(today);
            if(!isBefore){
                tilBirthDate.setError("La fecha ingresada no es valida");
            }
            else{
                tilBirthDate.setError(null);
            }
            return isBefore;
        }
        tilBirthDate.setError("La fecha ingresada no es valida");
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
        if(validBirthDate && validEmail && validName && validIdentification){
            User newUser = new User(identification, name,email, date,province,1); //1 means TRUE
            registerUser(newUser);
        }
    }

    public void registerUser(User user){
        DataBase.addUser(user);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}