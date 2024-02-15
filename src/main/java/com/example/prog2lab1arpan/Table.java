package com.example.prog2lab1arpan;

public class Table {
    private int id;
    private String name;
    private int sinNumber;
    private int age;

    public Table(int id, String name, int sinNumber, int age) {
        this.id = id;
        this.name = name;
        this.sinNumber = sinNumber;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSinNumber() {
        return sinNumber;
    }

    public void setSinNumber(int sinNumber) {
        this.sinNumber = sinNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
