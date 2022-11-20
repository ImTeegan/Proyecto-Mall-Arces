package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.UserDataHolder;
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private Button loginButton;
    private TextView tvForgotPassword;
    private FirebaseAuth fAuth;
    private CollectionReference usersCollection;
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

    /**
     * Instantiates the components used in the log in activity view
     */
    private void instantiateComponents() {
        this.tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        this.tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        this.loginButton = (Button) findViewById(R.id.login_button);
        this.tvForgotPassword = (TextView) findViewById(R.id.forgot_password);
        this.fAuth = FirebaseAuth.getInstance();
        this.usersCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    /**
     * Set the actions for the components used in the activity
     */
    private void setComponentActions() {
        // Set log in button click action.
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

        // Set forgot password text view action.
        this.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPasswordScreen();
            }
        });
    }

    /**
     * @throws ParseException
     * Validates that the email and logins the user
     */
    private void validateData() throws Exception {
        String email = this.tilEmail.getEditText().getText().toString();
        String password = this.tilPassword.getEditText().getText().toString();

        boolean validated = this.validateEmailAndPassword(email);

        if (validated) {
            signInUser(email, password);
        }

    }

    /**
     *
     * @param email the user email that we validate
     * @return true if the email is valid, or false if is invalid
     * @throws Exception
     */
    private boolean validateEmailAndPassword(String email) throws Exception {
        if (email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            this.tilEmail.setError("El correo electrónico no es válido");
            return false;
        }
        this.tilEmail.setError(null);
        return true;
    }

    /**
     *
     * @param email the user's email
     * @param password the user's password
     * Sign in the user
     */
    private void signInUser(String email, String password){
        fAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DocumentReference dataRef = usersCollection.document(userId);
                            dataRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    UserDataHolder user = documentSnapshot.toObject(UserDataHolder.class);
                                    if (user.getFirstTime() == 1){
                                        showChangePasswordScreen();
                                    }else{
                                        showStore();
                                    }
                                }
                            });
                        }else{
                            addErrorToFields();
                        }
                    }
                });
    }

    /**
     * if the user can't login it shows errors for the fields
     */
    private void addErrorToFields(){
        this.tilEmail.setError("Correo o contraseña incorrectos");
        this.tilPassword.setError("Correo o contraseña incorrectos");
        this.tilPassword.setErrorIconDrawable(null);
    }

    /**
     * Shows the screen to change the password
     */
    private void showChangePasswordScreen() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Shows the screen of the store
     */
    private void showStore() {
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Shows a screen to enter e-mail and start the password recuperation.
     */
    private void showForgotPasswordScreen() {
        Intent intent = new Intent(this, EmailInputActivity.class);
        startActivity(intent);
    }
}