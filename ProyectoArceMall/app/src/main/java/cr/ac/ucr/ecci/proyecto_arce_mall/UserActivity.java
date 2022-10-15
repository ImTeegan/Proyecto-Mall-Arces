package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.reginald.editspinner.EditSpinner;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;

public class UserActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DbHelper dataBase;
    private User activeUser;
    private EditSpinner provinceSpinner;
    private EditText userName;
    private EditText userId;
    private EditText userEmail;
    private EditText userAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        this.instantiateComponents();
        this.setDropdownProvinces();
        this.setInformationFields();

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_user);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
     * Initiate the components used in the user activity view
     */
    private void instantiateComponents() {
        this.dataBase = new DbHelper(this);
        this.activeUser = dataBase.getLoginUser();
        this.provinceSpinner = (EditSpinner) findViewById(R.id.edit_spinner);
        this.userId = (EditText) findViewById(R.id.user_id);
        this.userName = (EditText) findViewById(R.id.user_name);
        this.userEmail = (EditText) findViewById(R.id.email_user);
        this.userAge = (EditText) findViewById(R.id.age_user);
    }

    /**
     * Set the list of provinces and set the adapter for the spinner
     */
    private void setDropdownProvinces() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.provinces_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setText(this.activeUser.getProvince());
        provinceSpinner.setAdapter(adapter);
    }

    /**
     * Set the user information in the respective fields
     */
    private void setInformationFields(){
        userId.setText(this.activeUser.getIdentification());
        userName.setText(this.activeUser.getName());
        userEmail.setText(this.activeUser.getEmail());
        userAge.setText(getAgeFromBirthdate() + " años");
    }

    /**
     * Calculates the age based on the birth date
     * @return age The age of the user
     */
    private String getAgeFromBirthdate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse(this.activeUser.getBirthday(), formatter);
        LocalDate curDate = LocalDate.now();
        String age = String.valueOf(Period.between(localDate, curDate).getYears());
        return age;
    }
}