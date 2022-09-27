package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private Button loginButton;
    private DbHelper database;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.instantiateComponents();
        this.setComponentActions();
    }

    private void instantiateComponents() {
        this.tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        this.tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        this.loginButton = (Button) findViewById(R.id.login_button);
        this.database = new DbHelper(this);
        this.users = this.database.getAllUser();
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
                } catch (ParseException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    /**
     *
     * @throws ParseException
     */
    private void validateData() throws ParseException {
        String email = this.tilEmail.getEditText().getText().toString();
        String password = this.tilEmail.getEditText().getText().toString();

        boolean validEmail = this.validateEmailAndPassword(email, password);

    }

    private boolean validateEmailAndPassword(String email, String password) {
        if (email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            this.tilEmail.setError("El correo electrónico no es válido");
            return false;
        }

        if (!database.checkUser(email, password)) {
            this.tilEmail.setError(null);
            this.tilPassword.setError("Correo o contraseña incorrectos");
            return false;
        }

        this.tilEmail.setError(null);
        return true;
    }

    private boolean validatePassword(String password) {


        return true;
    }
}