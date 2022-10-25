package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;
import cr.ac.ucr.ecci.proyecto_arce_mall.mail.JavaMailApi;

public class EmailInputActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private Button acceptButton;
    private TextInputLayout tilCode;
    private Button codeInputButton;
    private DbHelper database;
    private String digitCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_input);

        this.instantiateComponents();
        this.setComponentActions();
    }

    /**
     * Instantiates the components used in the e-mail input activity view
     */
    private void instantiateComponents() {
        this.tilEmail = findViewById(R.id.til_passrec_email);
        this.acceptButton = findViewById(R.id.accept_pr_button);
        this.database = new DbHelper(this);
    }

    /**
     * Set the actions for the components used in the activity
     */
    private void setComponentActions() {
        // Set accept button click action.
        this.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    /**
     * Checks if the inserted e-mail is valid
     */
    private void validateData() {
        final String email = this.tilEmail.getEditText().getText().toString();

        if (email.isEmpty()
            || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())
            || !(this.database.userIsRegistered(email))) {

            this.tilEmail.setError("El correo electrónico no es válido");

        } else {
            this.tilEmail.setError(null);
            this.sendEmailWithCode(email);
            this.showCodeInputDialog(email);
        }
    }

    /**
     * Sends an e-mail with a six digit code to a user of a given e-mail.
     * @param email The e-mail of the user
     */
    private void sendEmailWithCode(String email) {
        this.digitCode = RandomStringUtils.randomNumeric(6);

        final String subject = "Tienda Arce - Olvido de contraseña";

        String message = "<h3>Para obtener una nueva contraseña, ingrese el siguiente código:</h3>";
        message += "<h4><font color=red>" + this.digitCode + "</font></h4>";

        JavaMailApi javaMailApi = new JavaMailApi(this, email, message, subject);
        javaMailApi.execute();
    }

    /**
     * Shows a dialog to enter the code sent by e-mail
     * @param email The e-mail of the user
     */
    private void showCodeInputDialog(String email) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.input_code_dialog, null);
        dialogBuilder.setView(view);

        this.tilCode = view.findViewById(R.id.til_code);
        this.codeInputButton = view.findViewById(R.id.code_input_button);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setGravity(Gravity.CENTER);

        this.codeInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCode(dialog, email);
            }
        });
    }

    /**
     * Checks if the inserted code matches with the e-mail sent
     */
    private void validateCode(AlertDialog dialog, String email) {
        final String insertedCode = this.tilCode.getEditText().getText().toString();

        if (insertedCode.equals(this.digitCode)) {
            this.tilCode.setError(null);
            dialog.dismiss();

            try {
                this.sendEmailWithNewPassword(email);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else {
            this.tilCode.setError("El código no es correcto");
        }
    }

    /**
     * Sends an e-mail with a new password to the user with the given e-mail
     * @param email e-mail of user
     * @throws Exception
     */
    private void sendEmailWithNewPassword(String email) throws Exception {
        User user = this.database.getUserByEmail(email);

        final String newPassword = createNewPassword();
        final EncryptPassword passwordEncrypter = new EncryptPassword();
        user.setPassword(passwordEncrypter.encryptPassword(newPassword));
        user.setFirstTime(1);

        this.database.updateUser(user);

        final String subject = "Tienda Arce - Nueva contraseña";

        String message = "<h2>Se ha generado una nueva contraseña temporal para Tienda Arce.</h2>";
        message += "<h3>Para ingresar, ingrese la siguiente contraseña temporal:</h3>";
        message += "<h4><font color=red>" + newPassword + "</font></h4>";

        JavaMailApi javaMailApi = new JavaMailApi(this, email, message, subject);
        javaMailApi.execute();
    }

    private String createNewPassword() {
        final int leftLimit = 97;
        final int rightLimit = 122;
        final int targetStringLength = 15; // Length of string

        Random random = new Random();

        final String password = random.ints(leftLimit, rightLimit + 1)
                                      .limit(targetStringLength)
                                      .collect(StringBuilder::new,
                                               StringBuilder::appendCodePoint,
                                               StringBuilder::append)
                                      .toString();

        return password;
    }
}