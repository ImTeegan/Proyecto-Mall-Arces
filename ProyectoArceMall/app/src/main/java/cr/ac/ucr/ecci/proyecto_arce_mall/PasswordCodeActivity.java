package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class PasswordCodeActivity extends AppCompatActivity {

    private TextInputLayout tilCode;
    private EditText codeText;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_code);

        this.instantiateComponents();
    }

    /**
     *
     */
    private void instantiateComponents() {
        this.tilCode = (TextInputLayout) findViewById(R.id.til_code);
        this.codeText = findViewById(R.id.code_field);
        this.continueButton = (Button) findViewById(R.id.continue_button);
    }

    private void setComponentActions() {

    }
}