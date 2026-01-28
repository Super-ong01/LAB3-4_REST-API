package com.app.library.services;

// LAB 3: annotated for Lab 3 identification

import com.app.library.models.Book;
import com.app.library.models.Member;
import com.app.library.models.BorrowingRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Responsibility: manage in-memory lists for Books, Members and BorrowingRecords
 * and contain all business logic (borrowing/returning, id generation).
 */
@Service
public class LibraryService {

    private final List<Book> books = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<BorrowingRecord> borrowingRecords = new ArrayList<>();

    private long nextBookId = 1L;
    private long nextMemberId = 1L;
    private long nextRecordId = 1L;

    // ==================== Book Methods ====================

    public List<Book> getAllBooks() {
        return books;
    }

    public Optional<Book> getBookById(Long id) {
        for (Book b : books) {
            if (b.getId() != null && b.getId().equals(id)) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    public Book addBook(Book book) {
        // Assign id if not provided
        if (book.getId() == null) {
            book.setId(nextBookId++);
        }
        books.add(book);
        return book;
    }

    public Optional<Book> updateBook(Book updatedBook) {
        if (updatedBook.getId() == null) {
            return Optional.empty();
        }
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(updatedBook.getId())) {
                books.set(i, updatedBook);
                return Optional.of(updatedBook);
            }
        }
        return Optional.empty();
    }

    public boolean deleteBook(Long id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(id)) {
                books.remove(i);
                return true;
            }
        }
        return false;
    }

    // ==================== Member Methods ====================

    public List<Member> getAllMembers() {
        return members;
    }

    public Optional<Member> getMemberById(Long id) {
        for (Member m : members) {
            if (m.getId() != null && m.getId().equals(id)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    public Member addMember(Member member) {
        if (member.getId() == null) {
            member.setId(nextMemberId++);
        }
        members.add(member);
        return member;
    }

    public Optional<Member> updateMember(Member updatedMember) {
        if (updatedMember.getId() == null) {
            return Optional.empty();
        }
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId().equals(updatedMember.getId())) {
                members.set(i, updatedMember);
                return Optional.of(updatedMember);
            }
        }
        return Optional.empty();
    }

    public boolean deleteMember(Long id) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId().equals(id)) {
                members.remove(i);
                return true;
            }
        }
        return false;
    }

    // ==================== BorrowingRecord Methods (business logic here) ====================

    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecords;
    }

    public Optional<BorrowingRecord> getBorrowingRecordById(Long id) {
        for (BorrowingRecord r : borrowingRecords) {
            if (r.getId() != null && r.getId().equals(id)) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    /**
     * Borrow a book: resolves provided book/member (by id inside the passed objects),
     * checks availability, sets borrowDate/dueDate, decreases availableCopies,
     * assigns record id and stores it.
     */
    public Optional<BorrowingRecord> borrowBook(BorrowingRecord record) {
        if (record == null || record.getBook() == null || record.getMember() == null) {
            return Optional.empty();
        }

        Long bookId = record.getBook().getId();
        Long memberId = record.getMember().getId();
        if (bookId == null || memberId == null) {
            return Optional.empty();
        }

        Optional<Book> bookOpt = getBookById(bookId);
        Optional<Member> memberOpt = getMemberById(memberId);

        if (!bookOpt.isPresent() || !memberOpt.isPresent()) {
            return Optional.empty();
        }

        Book book = bookOpt.get();
        if (book.getAvailableCopies() <= 0) {
            return Optional.empty();
        }

        // Business rules: set dates and decrease availableCopies
        record.setId(nextRecordId++);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        record.setBook(book);
        record.setMember(memberOpt.get());

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        borrowingRecords.add(record);
        return Optional.of(record);
    }

    /**
     * Return a book: find record, set returnDate, increase availableCopies.
     */
    public Optional<BorrowingRecord> returnBook(Long recordId) {
        Optional<BorrowingRecord> recOpt = getBorrowingRecordById(recordId);
        if (!recOpt.isPresent()) {
            return Optional.empty();
        }
        BorrowingRecord rec = recOpt.get();
        if (rec.getReturnDate() != null) {
            // already returned
            return Optional.of(rec);
        }
        rec.setReturnDate(LocalDate.now());
        Book book = rec.getBook();
        if (book != null) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
        }
        return Optional.of(rec);
    }
}
