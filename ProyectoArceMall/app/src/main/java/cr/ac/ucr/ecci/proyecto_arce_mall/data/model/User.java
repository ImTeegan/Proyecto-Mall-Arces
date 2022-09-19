package cr.ac.ucr.ecci.proyecto_arce_mall.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.material.internal.ParcelableSparseArray;

public class User implements Parcelable {
    private String Identification;
    private String Name;
    private String Email;
    private String Birthday;
    private String Province;
    private String Password;

    public User(){

    }
    public User(  String Identification,String Name,String Email, String Birthday, String Province){
        this.Identification = Identification ;
        this.Name = Name;
        this.Email = Email;
        this.Birthday = Birthday;
        this.Province = Province;
    }

    protected User(Parcel in) {
        Identification = in.readString();
        Name = in.readString();
        Email = in.readString();
        Birthday = in.readString();
        Province = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Identification);
        dest.writeString(Name);
        dest.writeString(Email);
        dest.writeString(Birthday);
        dest.writeString(Province);
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
        return Identification;
    }

    public void setIdentification(String identification) {
        Identification = identification;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
