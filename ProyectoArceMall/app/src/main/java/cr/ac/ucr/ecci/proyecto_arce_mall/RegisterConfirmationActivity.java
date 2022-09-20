package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterConfirmationActivity extends AppCompatActivity {

    private Button acceptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_confirmation);
        this.instantiateComponents();
        this.setButtonActions();
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
}