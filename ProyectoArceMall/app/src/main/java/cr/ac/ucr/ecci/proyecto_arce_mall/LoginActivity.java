package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private Button loginButton;
    private DbHelper database;
    private List<User> users;
    private User user;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.instantiateComponents();
        this.setComponentActions();
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(this.networkChangeListener, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(this.networkChangeListener);
        super.onStop();
    }

    private void instantiateComponents() {
        this.tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        this.tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        this.loginButton = (Button) findViewById(R.id.login_button);
        this.database = new DbHelper(this);

        // The following code is only for testing.
        this.users = this.database.getAllUser();

        for (int index = 0; index < this.users.size(); ++index) {
            User current = this.users.get(index);
            Log.i("e-mail: ", current.getEmail());
            Log.i("Password: ", current.getPassword());
            Log.i("First time: ", String.valueOf(current.getFirstTime()));
        }
    }

    /**
     *
     */
    private void setComponentActions() {
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validateData();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    /**
     * @throws ParseException
     */
    private void validateData() throws Exception {
        String email = this.tilEmail.getEditText().getText().toString();
        String password = this.tilPassword.getEditText().getText().toString();

        boolean validated = this.validateEmailAndPassword(email, password);

        if (validated) {
            if (this.database.isFirstTime(email)) {
                this.showChangePasswordScreen(email);
            } else {
                this.showStore();
            }
        }

    }

    private boolean validateEmailAndPassword(String email, String password) throws Exception {
        if (email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            this.tilEmail.setError("El correo electrónico no es válido");
            return false;
        }

        if (!database.checkUser(email, password)) {
            this.tilEmail.setError("Correo o contraseña incorrectos");
            this.tilPassword.setError("Correo o contraseña incorrectos");
            this.tilPassword.setErrorIconDrawable(null);
            return false;
        }

        this.tilEmail.setError(null);
        return true;
    }

    private void showChangePasswordScreen(String email) {
        user = users.stream().filter(user -> user.getEmail().equals(email)).findFirst().get();
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    private void showStore() {
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
        finish();
    }
}