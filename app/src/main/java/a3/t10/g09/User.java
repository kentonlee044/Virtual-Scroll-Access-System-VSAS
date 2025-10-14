package a3.t10.g09;

public class User {
    private String idkey;
    private String username;
    private String email;
    private String fullname;
    private String phoneNumber;
    private String role;
    private String password; // This should be the hashed password

    public User(String idkey, String username, String email, String fullname, String phoneNumber, String role, String password){
        this.idkey = idkey;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.password = password;
    }

    // Getters
    public String getIdkey() { 
        return idkey; 
    }
    public String getUsername() { 
        return username; 
    }
    public String getEmail() { 
        return email; 
    }
    public String getFullname() { 
        return fullname; 
    }
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    public String getRole() { 
        return role; 
    }
    public String getPassword() { 
        return password; 
    }
}
