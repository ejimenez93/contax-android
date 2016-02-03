package com.edisonjimenez.contax;


import java.util.HashMap;
import java.util.Map;

public class Person{

    // Person Properties
    private String id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String dateOfBirth;
    private String zipcode;

    // Constructors
    public Person() {
    }

    public Person(String id, String firstName, String lastName, String dateOfBirth, String zipcode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.dateOfBirth = dateOfBirth;
        this.zipcode = zipcode;
    }

    // Getters and Setters


    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    // Helper functions

    /**
     * Generates a HashMap representation of a ContactActivity (Person)
     * @return HashMap of Person
     */
    public Map<String, String> toMap() {

        Map<String, String> output = new HashMap<>();

        output.put("id", id);
        output.put("firstName", firstName);
        output.put("lastName", lastName);
        output.put("fullName", fullName);
        output.put("dateOfBirth", dateOfBirth);
        output.put("zipcode", zipcode);

        return output;
    }


}
