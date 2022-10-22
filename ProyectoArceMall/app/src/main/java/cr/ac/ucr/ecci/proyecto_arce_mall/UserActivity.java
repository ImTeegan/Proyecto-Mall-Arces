package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reginald.editspinner.EditSpinner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;

public class UserActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DbHelper dataBase;
    private User activeUser;
    private EditSpinner provinceSpinner;
    private ImageButton userImageButton;
    private EditText userName;
    private EditText userId;
    private EditText userEmail;
    private EditText userAge;
    private String newDate;
    private Button changePasswordButton;
    private Button updateUserButton;
    private DatePickerDialog datePickerDialog;
    private byte[] userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        this.instantiateComponents();
        this.setDropdownProvinces();
        try {
            this.setInformationFields();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.setComponentActions();

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_user);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(),StoreActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_user:
                        return true;
                    case R.id.navigation_cart:
                        startActivity(new Intent(getApplicationContext(),CartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Instantiates the components used in the user activity view
     */
    private void instantiateComponents() {
        this.dataBase = new DbHelper(this);
        this.activeUser = dataBase.getLoginUser();
        this.provinceSpinner = findViewById(R.id.edit_spinner);
        this.userImageButton = findViewById(R.id.user_image);
        this.userId = findViewById(R.id.user_id);
        this.userName = findViewById(R.id.user_name);
        this.userEmail = findViewById(R.id.email_user);
        this.userAge = findViewById(R.id.user_age);
        this.updateUserButton = findViewById(R.id.update_button);
        this.changePasswordButton = findViewById(R.id.change_password_button);
    }

    /**
     * Sets the actions for the buttons update data and change password
     */
    private void setComponentActions() {

        this.userImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    pickImage();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        this.updateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validateData();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        this.changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showChangePasswordScreen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Allows the user to upload an image from camera or pick it from gallery.
     */
    private void pickImage() {
        ImagePicker.with(this)
                   .crop()
                   .compress(1024)
                   .maxResultSize(1080, 1080)
                   .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        this.userImageButton.setImageURI(uri);
        try {
            InputStream iStream =   getContentResolver().openInputStream(uri);
            userImage = getBytes(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    /**
     * Sets the list of provinces and set the adapter for the spinner
     */
    private void setDropdownProvinces() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.provinces_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        this.provinceSpinner.setText(this.activeUser.getProvince());
        this.provinceSpinner.setAdapter(adapter);
    }

    /**
     * Sets the user information in the respective fields
     */
    private void setInformationFields() throws UnsupportedEncodingException {
        this.userId.setText(this.activeUser.getIdentification());
        this.userName.setText(this.activeUser.getName());
        this.userEmail.setText(this.activeUser.getEmail());
        InputStream is = new ByteArrayInputStream(this.activeUser.getImage());
        Bitmap bmp = BitmapFactory.decodeStream(is);
        this.userImageButton.setImageBitmap(bmp);
        this.userImageButton.setBackgroundResource(R.drawable.profile_image);
        this.userAge.setText(getAgeFromBirthdate(this.activeUser.getBirthday()) + " años");

        this.userAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatePickerDialog();
            }
        });
    }

    /**
     * Calculates the age based on the birth date
     *
     * @return The age of the user
     */
    private String getAgeFromBirthdate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDate currentDate = LocalDate.now();

        String age = String.valueOf(Period.between(localDate, currentDate).getYears());
        return age;
    }

    /**
     * Updates the user with the new information if the birth date is valid.
     *
     * Checks if the birth date is a valid date and then updates the user information.
     * @throws ParseException
     */
    private void validateData() throws ParseException {
        String newBirthDate = this.newDate;
        String newProvince = this.provinceSpinner.getText().toString();
        if (!this.activeUser.getBirthday().equals(this.newDate) && newDate != null){
            if (validateBirthDate(newBirthDate)){
                this.activeUser.setBirthday(newBirthDate);
            }
        }
        this.activeUser.setProvince(newProvince);
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
        if(userImage != null){
            this.activeUser.setImage(userImage);
        }
        this.dataBase.updateUser(this.activeUser);
    }

    /**
     * Checks if birthdate is a valid date
     *
     * @param birthDate the birthdate enter
     * @return true if is a valid date and false if is not
     * @throws ParseException
     */
    private boolean validateBirthDate(String birthDate) throws ParseException {
        if (!birthDate.isEmpty()) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate pastDate = LocalDate.parse(birthDate, formatter);
            boolean isBefore = pastDate.isBefore(today);

            if (!isBefore) {
                this.userAge.setError("La fecha ingresada no es válida");
            } else {
                this.userAge.setError(null);
            }
            return isBefore;
        }

        this.userAge.setError("La fecha ingresada no es válida");
        return false;
    }

    /**
     * Opens the date picker dialog in order to pick a new birth date
     */
    private void getDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(
                UserActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        newDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        userAge.setText(getAgeFromBirthdate(newDate) + " años");
                    }
                }, year, month, day);

        if(this != null && !this.isFinishing()) {
            datePickerDialog.show();
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (datePickerDialog != null && datePickerDialog.isShowing()) {
            datePickerDialog.dismiss();
        }
    }

    /**
     * Shows the change password screen when the change password button is pressed
     */
    private void showChangePasswordScreen() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("changePassword", "changePassword");
        startActivity(intent);
        finish();
    }
}