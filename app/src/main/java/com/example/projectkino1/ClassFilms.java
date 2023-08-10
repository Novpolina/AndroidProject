package com.example.projectkino1;


import java.io.Serializable;

public class ClassFilms implements Serializable {
    private long id;
    private String name;
    private String comment;
    private String author;
    private String category;
    private int review;
    private int rang;

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ClassFilms(long id, String name, String comment, String at, String category, int review, int rang) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.author = at;
        this.category = category;
        this.review = review;
        this.rang = rang;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getAuthor() {
        return author;
    }


}
