package com.redhat.f4j.base.scalar;

import java.io.Serializable;

/**
 * Person
 */
public class Person implements Serializable{

    private static final long serialVersionUID = -141284699735453863L;
    
    private String firstName;
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    
    private String lastName;
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    
    private String email;
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

}