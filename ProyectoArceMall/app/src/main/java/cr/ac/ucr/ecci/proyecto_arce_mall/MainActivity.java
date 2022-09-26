package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this is a test
        Button button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity Activity = MainActivity.this;
                DbHelper Database = new DbHelper(Activity);

               List<User> Users =  Database.getAllUser();

               for(int i=0;i<Users.size();i++){
                   User newUser = Users.get(i);
                   Log.i("ID:" , newUser.getIdentification());
                   Log.i("Name" , newUser.getName());
                   Log.i("Email" , newUser.getEmail());
                   Log.i("Province" , newUser.getProvince());
                   Log.i("Birthday" , newUser.getBirthday());
                   Log.i("Password",newUser.getPassword());
               }

               if(Database.checkUser("cesar@lopez.com","DEDOS")){
                   Log.i("INGRESA CORRECTAMENTE","SI INGRESA");
               };


            }
        });



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