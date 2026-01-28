package com.app.library.models;

// LAB 3: annotated for Lab 3 identification

public class Book {

    private Long id;

    private String title;
    private String author;
    private int publicationYear;
    private String genre;
    private int availableCopies;

    // Default constructor
    public Book() {}

    // Parameterized constructor
    // Create a Book object. Note: `id` is expected to be set externally (e.g., by controller or test).
    public Book(String title, String author, int publicationYear, String genre, int availableCopies) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.availableCopies = availableCopies;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    // toString
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", genre='" + genre + '\'' +
                ", availableCopies=" + availableCopies +
                '}';
    }
}