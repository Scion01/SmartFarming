package com.example.hauntarl.smartfarming;

public class newUser {
    public String Name;
    public String Phone;
    public String Email;
    public String TotLand;
    public String TotLandUnit;
    public String CultLand;
    public String CultLandUnit;
    public String pass;
    public String district;

    public newUser(String name, String phone, String email, String totLand, String totLandUnit, String cultLand, String cultLandUnit, String pass, String district) {
        this.Name = name;
        this.Phone = phone;
        this.Email = email;
        this.TotLand = totLand;
        this.TotLandUnit = totLandUnit;
        this.CultLand = cultLand;
        this.CultLandUnit = cultLandUnit;
        this.pass = pass;
        this.district=district;
    }
}
