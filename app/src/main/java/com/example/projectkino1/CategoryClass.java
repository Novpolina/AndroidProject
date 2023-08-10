package com.example.projectkino1;

public class CategoryClass {
    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String name;


    public CategoryClass(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
