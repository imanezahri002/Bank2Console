package org.example.models;

import java.util.UUID;

public class Client {
    private UUID id;
    private String firstName;
    private String lastName;
    private String cin;
    private String tel;
    private String address;
    private String email;

    public Client(String firstName,String lastName,String cin,String tel,String address,String email){
        this.firstName=firstName;
        this.lastName=lastName;
        this.cin=cin;
        this.tel=tel;
        this.address=address;
        this.email=email;
    }
    public UUID getId(){
        return this.id;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public void setFirstName(String newfirstName){
        this.firstName=newfirstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public void setLastName(String newlastName){
        this.firstName=newlastName;
    }
    public String getCin(){
        return this.cin;
    }
    public void setCin(String newCin){
        this.cin=newCin;
    }
    public String getAddress(){
        return this.address;
    }
    public void setAddress(String newAddress){
        this.address=newAddress;
    }
    public String getTel(){
        return this.tel;
    }
    public void setTel(String newTel){
        this.tel=newTel;
    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String newEmail){
        this.email=newEmail;
    }

}
