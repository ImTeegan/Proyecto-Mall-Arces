package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;

public class RegistrationActivity extends AppCompatActivity {
    private TextView Identification;
    private TextView Name;
    private TextView Email;
    private TextView Birthday;
    private TextView Province;
    private DbHelper DataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBase = new DbHelper(this);
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
        DataBase.addUser(user);
    }
}