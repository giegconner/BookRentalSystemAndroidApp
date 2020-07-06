package edu.csumb.cgieg.mainmenu;

public class Customer {
    private String transType;
    private String username;
    private String password;
    private String dateHour;

    public Customer(String aTransType, String aUsername, String aPassword, String aDateHour) {
        this.transType = aTransType;
        this.username = aUsername;
        this.password = aPassword;
        this.dateHour = aDateHour;
    }

    public String getTransType() {
        return this.transType;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDateHour() {
        return this.dateHour;
    }
}
