package edu.csumb.cgieg.mainmenu;

public class Book {
    private String title;
    private String author;
    private double feeperhour;

    public Book(String aTitle, String aAuthor, double aFeePerHour) {
        this.title = aTitle;
        this.author = aAuthor;
        this.feeperhour = aFeePerHour;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public double getFeePerHour() {
        return this.feeperhour;
    }
}
