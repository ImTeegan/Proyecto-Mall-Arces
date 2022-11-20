package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button loginButton;
    private DbHelper dataBase;
    private TextInputLayout tilNewPassword;
    private TextInputLayout tilConfirmPassword;
    private String changePassword;
    private String userEmail;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        this.instantiateComponents();
        this.setComponentActions();
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
     * Instantiates the components used in the password change activity view
     */
    private void instantiateComponents() {
        this.dataBase = new DbHelper(this);
        this.loginButton = (Button) findViewById(R.id.login_button);
        this.tilNewPassword = findViewById(R.id.til_new_password);
        this.tilConfirmPassword = findViewById(R.id.til_confirm_password);

        Bundle message = getIntent().getExtras();
        if (message != null) {
            this.changePassword = message.getString("changePassword");
            this.userEmail = message.getString("userEmail");
        }
    }

    /**
     * Sets the action for the button to change the password
     */
    private void setComponentActions() {
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validatePassword();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    /**
     * Change the password and validate that is a valid password
     * @throws Exception
     */
    protected void validatePassword() throws Exception {
        String newPassword = tilNewPassword.getEditText().getText().toString();
        String confirmPassword = tilConfirmPassword.getEditText().getText().toString();

        if (newPassword.equals(confirmPassword) && newPassword.length() > 8) {
            // Save new password.
            savePassword(newPassword);
        } else {
            this.tilConfirmPassword.setError("Las contraseñas deben ser iguales y " +
                                             "tener un mínimo de 8 caracteres");
            this.tilConfirmPassword.setErrorIconDrawable(null);
        }
    }

    /**
     * Encrypts the new password and saves it in the database
     *
     * @param newPassword The user's new password
     * @throws Exception
     */
    protected void savePassword(String newPassword) throws Exception {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId= user.getUid();
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

        dataBase.collection("Users").document(userId).update("firstTime", 0);

        user.updatePassword(newPassword);

        if (this.changePassword != null) {
            Intent intent = new Intent(this, ChangePasswordConfirmationActivity.class);
            intent.putExtra("email", this.userEmail);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, StoreActivity.class);
            startActivity(intent);
            finish();
        }
    }
}