package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cr.ac.ucr.ecci.proyecto_arce_mall.mail.JavaMailApi;
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
        this.getUserEmail();
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
     * Instantiates the components used in the password change confirmation activity view
     */
    private void instantiateComponents() {
        this.acceptButton = (Button) findViewById(R.id.accept_button);
    }

    /**
     * Sets the action for the button to go to the store screen
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
     * Gets the user e-mail from the password change activity
     */
    private void getUserEmail() {
        Bundle message = getIntent().getExtras();

        if (message != null) {
            String email = message.getString("email");
            try {
                this.sendMail(email);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Return to the store screen after the email is send
     */
    private void goToStoreScreen() {
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
    }

    /**
     * Sends the email that confirms the password change
     *
     * @param email The receipt email
     * @throws Exception
     */
    private void sendMail(String email) throws Exception {
        String subject = "Tienda Arce - Cambio de contrase??a exitoso";

        String message = "<h2>Se le envia este correo para confirmar el cambio " +
                         "de contrase??a de su cuenta de Tienda Arce.</h2><br>";
        message += "<h3>Muchas gracias por preferirnos.</h3>";

        JavaMailApi javaMailApi = new JavaMailApi(this, email, message, subject);
        javaMailApi.execute();
    }
}