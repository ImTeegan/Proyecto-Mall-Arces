package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;

public class RegistrationActivity extends AppCompatActivity {
    private TextView Identification;
    private TextView Name;
    private TextView Email;
    private TextView Birthday;
    private TextView Province;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Identification = (TextView) findViewById(R.id.name_field);
        Name = (TextView) findViewById(R.id.telephone_field);
        Email = (TextView) findViewById(R.id.email_field);
        Birthday = (TextView) findViewById(R.id.birthDate_field);
        Province = (TextView) findViewById(R.id.location_field);
        Button buttonRegister = (Button) findViewById(R.id.registerButton);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User newUser = new User(Identification.getText().toString(),
                        Name.getText().toString(),Email.getText().toString(),
                        Birthday.getText().toString(),Province.getText().toString());
                registerUser(newUser);
            }
        });

    }

    protected void registerUser(User user){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Identification" , user.getIdentification());
        editor.putString("Name" , user.getName());
        editor.putString("Email" , user.getEmail());
        editor.putString("Birthday" , user.getBirthday());
        editor.putString("Province" , user.getProvince());
        //generate alphanumeric password and send it to mail and create the user
        editor.putInt("Password",12345678);//this must be encrypted
        editor.commit();
    }
    /*
    To obtain data, use this king of sentences
    String name = preferences.getString("Name", "unknown");
  */
}