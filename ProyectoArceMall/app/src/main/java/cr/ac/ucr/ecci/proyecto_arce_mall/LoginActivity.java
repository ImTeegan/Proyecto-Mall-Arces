package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private Button loginButton;

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
        String email = tilEmail.getEditText().getText().toString();

        boolean validEmail = this.validateEmail(email);

    }

    private boolean validateEmail(String email){
        if (email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            this.tilEmail.setError("El correo electrónico no es válido");
            return false;
        }

        this.tilEmail.setError(null);
        return true;
    }
}