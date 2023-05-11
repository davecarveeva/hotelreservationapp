package model;

import java.util.*;
import java.util.regex.Pattern;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;
    private final String emailCheck = "^(.+)@(.+).com";
    private final Pattern pattern = Pattern.compile(emailCheck);
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

