package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this is a test
        /*
        Button button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String Name = myPreferences.getString("Name","unknown");
                int Password = myPreferences.getInt("Password",0);
                if(Password == 12345678 && Name.compareTo("julio") == 0){
                    Log.i("SUCCES" ,"Exitoso");
                }


            }
        });
        */


    }

    public void registrationScreen(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}