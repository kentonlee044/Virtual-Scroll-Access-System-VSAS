package a3.t10.g09;

public class User {
    private String idkey; // Unique identifier for the user
    private String fullname; // Full name of the user
    private String phone; // Phone number of the user
    private String email; // Unique email address
    private String password; // Hashed password

    // Constructor
    public User(String idkey, String fullname, String phone, String email, String password) {
        this.idkey = idkey;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getIdkey() {
        return idkey;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setIdkey(String idkey) {
        this.idkey = idkey;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}