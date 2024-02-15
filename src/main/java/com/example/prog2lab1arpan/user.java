package com.example.prog2lab1arpan;

public class user {
    private int id;
    private String name;
    private int sinNumber;

    public user(int id, String name, int sinNumber) {
        this.id = id;
        this.name = name;
        this.sinNumber = sinNumber;
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
}
