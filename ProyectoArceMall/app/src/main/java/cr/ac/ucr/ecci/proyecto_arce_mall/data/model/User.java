package cr.ac.ucr.ecci.proyecto_arce_mall.data.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class User implements Parcelable {
    private String identification;
    private String name;
    private String email;
    private String birthday;
    private String province;
    private String password;
    private int firstTime;
    private int login;
    private Bitmap image;

    public User() {

    }

    public User(String identification, String name, String email,
                String birthday, String province, int firstTime, int login, Bitmap image) throws Exception {
        this.identification = identification ;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.province = province;
        this.firstTime = firstTime;
        this.login = login;
        this.image = image;
        this.CreatePassword();
    }

    protected User(Parcel in) {
        this.identification = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.birthday = in.readString();
        this.province = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.identification);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.birthday);
        dest.writeString(this.province);
    }

    /**
     * Creates a random password for a new user.
     */
    private void CreatePassword() throws Exception {
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public int getLogin() {return login;}

    public void setLogin(int login) {this.login = login;}

    /*
    Converts a byte array in bitmap, and assign it to image
    @param byte[]
     */
    public void setImage(byte[] img){
        this.image = BitmapFactory.decodeByteArray(img, 0, img.length);
    }

    /*
    Converts bitmap into byte array
     */
    public byte[] getImage(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }


}