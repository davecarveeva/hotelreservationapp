package model;

import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }

    public Customer(String firstName, String lastName, String email){
        final String emailCheck = "^(.+)@(.+).com";
        final Pattern pattern = Pattern.compile(emailCheck);
        if(!pattern.matcher(email).matches()){
            throw new IllegalArgumentException("Invalid email, please try again.");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;


    }
    @Override
    public String toString(){
        return "Customer: " + firstName + ", " + lastName + "; " + email;
    }
}

