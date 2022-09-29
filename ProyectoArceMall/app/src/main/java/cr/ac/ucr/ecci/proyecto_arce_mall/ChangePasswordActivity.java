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

    private void instantiateComponents() {
        this.dataBase = new DbHelper(this);
        this.loginButton = (Button) findViewById(R.id.login_button);
        this.tilNewPassword = findViewById(R.id.til_new_password);
        this.tilConfirmPassword = findViewById(R.id.til_confirm_password);
        this.user = getIntent().getParcelableExtra("user");
    }

    private void setComponentActions() {
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validatePassword();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void validatePassword() throws Exception {
        String newPassword = tilNewPassword.getEditText().getText().toString();
        String confirmPassword = tilConfirmPassword.getEditText().getText().toString();

        if (newPassword.equals(confirmPassword)
            && newPassword.length() > 8) {
            // Save new password.
            savePassword(user, newPassword);
        } else {
            this.tilConfirmPassword.setError("Las contraseñas deben ser igual y" +
                                             "tener un minimo de 8 caracteres");
        }
    }

    protected void savePassword(User user, String newPassword) throws Exception {
        user.setFirstTime(0);
        EncryptPassword encryptPassword = new EncryptPassword();
        user.setPassword(encryptPassword.encryptPassword(newPassword));

        this.dataBase.updateUser(user);

        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
        finish();
    }
}