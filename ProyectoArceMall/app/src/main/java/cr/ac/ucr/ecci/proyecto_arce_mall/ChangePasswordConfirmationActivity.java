package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cr.ac.ucr.ecci.proyecto_arce_mall.mail.JavaMailAPI;
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;

public class ChangePasswordConfirmationActivity extends AppCompatActivity {

    private Button acceptButton;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_confirmation);
        this.instantiateComponents();
        this.setButtonActions();
        Bundle message = getIntent().getExtras();
        if (message != null) {
            String email = message.getString("email");
            try {
                this.sendMail(email);
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

    /**
     * Initiate the components used in the user activity view
     */
    private void instantiateComponents() {
        this.acceptButton = (Button) findViewById(R.id.accept_button);
    }

    /**
     * Set the action for the button to go to the store screen
     */
    private void setButtonActions() {
        this.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToStoreScreen();
            }
        });
    }

    /**
     * Return to the Store screen after the email is send
     */
    private void goToStoreScreen() {
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
    }

    /**
     * Sends the email that confirms tha password change
     * @param email the receipt email
     * @throws Exception
     */
    private void sendMail(String email) throws Exception {
        String subject = "Tienda Arce - Cambio de contraseña exitoso";
        String message = "<h2>Se le envia este correo para confirmar el cambio de contraseña de su cuenta de Tienda Arce.</h2><br>";
        message += "<h3>Muchas gracias por preferirnos.</h3>";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email, message, subject);
        javaMailAPI.execute();
    }
}