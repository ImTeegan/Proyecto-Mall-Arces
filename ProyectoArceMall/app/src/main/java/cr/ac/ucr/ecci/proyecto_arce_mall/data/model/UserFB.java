package cr.ac.ucr.ecci.proyecto_arce_mall.data.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class UserFB {
    private String identification;
    private String name;
    private String email;
    private String birthday;
    private String province;
    private String password;
    private int firstTime;
    private int login;


    public UserFB() {

    }

    public UserFB(String identification, String name, String email,
                String birthday, String province, int firstTime, int login) throws Exception {
        this.identification = identification;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.province = province;
        this.firstTime = firstTime;
        this.login = login;
        this.createPassword();
    }

    /**
     * Creates a random password for a new user.
     */
    private void createPassword() throws Exception {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 15; // Length of string

        Random random = new Random();

        String password = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();

        this.setPassword(password);
    }

    public String getIdentification() {
        return this.identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(int firstTime) {
        this.firstTime = firstTime;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }





}