package com.app.library.models;

// LAB 3: annotated for Lab 3 identification

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model: BorrowingRecord
 * Responsibility: represent a borrowing transaction linking a Book and a Member
 * This class accepts either nested `book`/`member` objects or `bookId`/`memberId` in JSON.
 */
public class BorrowingRecord {

    private Long id;

    // The actual Book and Member objects involved in this record
    private Book book;
    private Member member;

    private LocalDate borrowDate;
    private LocalDate returnDate;
    private LocalDate dueDate;

    public BorrowingRecord() {}

    public BorrowingRecord(Book book, Member member, LocalDate borrowDate, LocalDate dueDate) {
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // JSON-friendly accessors so clients can send {"bookId":1, "memberId":2}
    @JsonProperty("bookId")
    public void setBookId(Long bookId) {
        if (bookId != null) {
            Book b = new Book();
            b.setId(bookId);
            this.book = b;
        }
    }

    @JsonProperty("memberId")
    public void setMemberId(Long memberId) {
        if (memberId != null) {
            Member m = new Member();
            m.setId(memberId);
            this.member = m;
        }
    }

    @JsonProperty("bookId")
    public Long getBookId() {
        return (book != null) ? book.getId() : null;
    }

    @JsonProperty("memberId")
    public Long getMemberId() {
        return (member != null) ? member.getId() : null;
    }

    @Override
    public String toString() {
        return "BorrowingRecord{" +
                "id=" + id +
                ", book=" + (book != null ? book.getId() : null) +
                ", member=" + (member != null ? member.getId() : null) +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", dueDate=" + dueDate +
                '}';
    }
}