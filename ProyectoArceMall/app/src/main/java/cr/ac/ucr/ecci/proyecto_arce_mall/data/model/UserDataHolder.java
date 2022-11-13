package cr.ac.ucr.ecci.proyecto_arce_mall.data.model;

public class UserDataHolder {
    private String identification;
    private String name;
    private String email;
    private String birthday;
    private String province;
    private String password;
    private int firstTime;
    private int login;

    UserDataHolder(User user) {
        setIdentification(user.getIdentification());
        setName(user.getName());
        setEmail(user.getEmail());
        setBirthday(user.getBirthday());
        setProvince(user.getProvince());
        setPassword(user.getPassword());
        setFirstTime(user.getFirstTime());
        setLogin(user.getLogin());
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentification() {
        return identification;
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
