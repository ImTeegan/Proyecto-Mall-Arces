package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button loginButton;
    private DbHelper dataBase;
    private TextInputLayout tilNewPassword;
    private TextInputLayout tilConfirmPassword;
    private User user;
    private String changePassword;
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
        this.user = getIntent().getParcelableExtra("user");

        Bundle message = getIntent().getExtras();
        if (message != null) {
            this.changePassword = message.getString("changePassword");
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
            savePassword(user, newPassword);
        } else {
            this.tilConfirmPassword.setError("Las contraseñas deben ser igual y" +
                                             "tener un minimo de 8 caracteres");
        }
    }

    /**
     * Encrypts the new password and saves it in the database
     *
     * @param user The user whose password is changed
     * @param newPassword The user's new password
     * @throws Exception
     */
    protected void savePassword(User user, String newPassword) throws Exception {
        user.setFirstTime(0);
        user.setLogin(1);
        EncryptPassword encryptPassword = new EncryptPassword();
        user.setPassword(encryptPassword.encryptPassword(newPassword));

        this.dataBase.updateUser(user);

        if (this.changePassword != null) {
            Intent intent = new Intent(this, ChangePasswordConfirmationActivity.class);
            intent.putExtra("email", user.getEmail());
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, StoreActivity.class);
            startActivity(intent);
            finish();
        }
    }
}