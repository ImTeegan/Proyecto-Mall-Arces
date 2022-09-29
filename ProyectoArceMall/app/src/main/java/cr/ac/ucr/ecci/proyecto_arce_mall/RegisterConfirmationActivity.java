package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;
import cr.ac.ucr.ecci.proyecto_arce_mall.mail.JavaMailAPI;

public class RegisterConfirmationActivity extends AppCompatActivity {

    private Button acceptButton;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_confirmation);
        this.instantiateComponents();
        this.setButtonActions();
        Bundle message = getIntent().getExtras();
        if (message != null) {
            String password = message.getString("password");
            String email = message.getString("email");
            try {
                this.sendMail(email, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(this.networkChangeListener,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(this.networkChangeListener);
        super.onStop();
    }

    private void instantiateComponents() {
        this.acceptButton = (Button) findViewById(R.id.accept_button);
    }

    private void setButtonActions() {
        this.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMainScreen();
            }
        });
    }

    private void goBackToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void sendMail(String email, String password) throws Exception {

        String message = "<h2>Gracias por crear su cuenta en Tienda Arce.</h2><br>";
        message += "<h3>Para ingresar por primera vez, ingrese la siguiente contraseña:</h3>";
        message += "<h4><font color=red>" + password + "</font></h4>";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email, message);
        javaMailAPI.execute();
    }
}