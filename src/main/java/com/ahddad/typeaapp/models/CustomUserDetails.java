package com.ahddad.typeaapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CustomUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String username;
    private String gender;
    private String grade;
    private double salary;
    private String etablissement;
    private String departement;
    private String address;

    public CustomUserDetails() {}

    public CustomUserDetails(String username, String gender, String grade, double salary, String etablissement, String departement, String address) {
        this.username = username;
        this.gender = gender;
        this.grade = grade;
        this.salary = salary;
        this.etablissement = etablissement;
        this.departement = departement;
        this.address = address;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "Id=" + Id +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                ", grade='" + grade + '\'' +
                ", salary=" + salary +
                ", etablissement='" + etablissement + '\'' +
                ", departement='" + departement + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
