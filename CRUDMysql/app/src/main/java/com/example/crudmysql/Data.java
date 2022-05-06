package com.example.crudmysql;

public class Data {
    private String id, name, hobby, password;

    public Data() {
    }

    public Data(String id, String name, String hobby, String password ){
        this.id = id;
        this.hobby = hobby;
        this.name = name;
        this.password = password;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
