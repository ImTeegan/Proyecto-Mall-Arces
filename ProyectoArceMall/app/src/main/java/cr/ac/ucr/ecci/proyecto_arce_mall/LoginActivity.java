package cr.ac.ucr.ecci.proyecto_arce_mall;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.biometric.BiometricPrompt;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import java.util.concurrent.Executor;

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
    private Button biometryButton;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.instantiateComponents();
        this.setComponentActions();
    }

    /**
     * Register the receiver that checks
     * if the user has internet at every moment
     */
    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(this.networkChangeListener, intentFilter);
        super.onStart();
    }

    /**
     * Unregister the receiver that checks
     * if the user has internet at every moment
     */
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
        this.biometryButton = (Button) findViewById(R.id.login_Biometry);
        executor = ContextCompat.getMainExecutor(this);
        handleBiometryLogin();
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

                                        //store credentials on shared preferences
                                        SharedPreferences pref =
                                                getSharedPreferences("credentials", LoginActivity.MODE_PRIVATE);
                                        SharedPreferences.Editor edt = pref.edit();
                                        edt.putString("email", email);
                                        edt.putString("password",password);
                                        edt.putInt("isLogged", 0);


                                        SharedPreferences preferences =
                                                getSharedPreferences("logged", LoginActivity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putInt("isLogged", 0);

                                        editor.commit();
                                        edt.commit();

                                        showChangePasswordScreen();
                                    }else{

                                        //store credentials on shared preferences
                                        SharedPreferences pref =
                                                getSharedPreferences("credentials", LoginActivity.MODE_PRIVATE);
                                        SharedPreferences.Editor edt = pref.edit();
                                        edt.putString("email", email);
                                        edt.putString("password",password);

                                        SharedPreferences preferences =
                                                getSharedPreferences("logged", LoginActivity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putInt("isLogged", 0);


                                        editor.commit();
                                        edt.commit();

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
     * handle Sign in the user with Biometry
     */
    private void handleBiometryLogin(){
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(LoginActivity.this,"Biometria validada ",
                        Toast.LENGTH_SHORT).show();

                //take credentials from shared preferences
                SharedPreferences preferences =
                        getSharedPreferences("credentials",Context.MODE_PRIVATE);
                String email = preferences.getString("email","NULL");
                String password = preferences.getString("password","NULL");
                signInUser(email,password);

            }
            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(LoginActivity.this,"Falla al validar biometría ",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthenticationError(int errorCode,@NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode,errString);
                //fail login
                Toast.makeText(LoginActivity.this,"Error de autenticación : " + errString,
                        Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación Biometrica")
                .setSubtitle("Inicio sesión con Biometría")
                .setNegativeButtonText("Contraseña del App")
                .build();

        if(isLogged()){
            biometryButton.setVisibility(View.VISIBLE);
        }

        biometryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-1 never logged , o loged
                if(isLogged()) {
                    biometryButton.setVisibility(View.VISIBLE);
                    biometricPrompt.authenticate(promptInfo);
                }else{
                    Toast.makeText(LoginActivity.this,"Debe iniciar sesión por primera vez para usar esta funcionalidad",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * Method to identificate if an user is logged
     * @return true if is logged
     */
    public boolean isLogged(){
        boolean isLoggeg = false;
        SharedPreferences preferences =
                getSharedPreferences("logged",Context.MODE_PRIVATE);
        int firstTime = preferences.getInt("isLogged",-1);
        if(firstTime == 0) {
            isLoggeg = true;
        }
        return isLoggeg;
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