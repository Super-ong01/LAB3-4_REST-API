package com.app.library;

import com.app.library.models.Book;
import com.app.library.models.BorrowingRecord;
import com.app.library.models.Member;
import com.app.library.services.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryServiceTests {

    private LibraryService service;

    @BeforeEach
    public void setup() {
        service = new LibraryService();
    }

    @Test
    public void addBook_assignsIdAndStores() {
        Book b = new Book("Clean Code", "Robert C. Martin", 2008, "Programming", 3);
        assertNull(b.getId());
        Book created = service.addBook(b);
        assertNotNull(created.getId(), "Book id should be assigned");
        Optional<Book> fetched = service.getBookById(created.getId());
        assertTrue(fetched.isPresent());
        assertEquals("Clean Code", fetched.get().getTitle());
    }

    @Test
    public void addMember_assignsIdAndStores() {
        Member m = new Member();
        m.setName("Alice");
        m.setEmail("alice@example.com");
        Member created = service.addMember(m);
        assertNotNull(created.getId());
        Optional<Member> fetched = service.getMemberById(created.getId());
        assertTrue(fetched.isPresent());
        assertEquals("Alice", fetched.get().getName());
    }

    @Test
    public void borrowAndReturn_flowUpdatesCopiesAndDates() {
        // prepare book and member
        Book b = new Book("Demo","Author",2026,"F",1);
        Book createdBook = service.addBook(b);
        Member member = new Member();
        member.setName("Bob");
        Member createdMember = service.addMember(member);

        // borrow
        BorrowingRecord rec = new BorrowingRecord();
        Book bookRef = new Book(); bookRef.setId(createdBook.getId());
        Member memRef = new Member(); memRef.setId(createdMember.getId());
        rec.setBook(bookRef);
        rec.setMember(memRef);

        Optional<BorrowingRecord> borrowed = service.borrowBook(rec);
        assertTrue(borrowed.isPresent(), "Borrow should succeed when copies available");
        BorrowingRecord r = borrowed.get();
        assertNotNull(r.getId());
        assertNotNull(r.getBorrowDate());
        assertNotNull(r.getDueDate());
        assertEquals(0, service.getBookById(createdBook.getId()).get().getAvailableCopies());

        // return
        Optional<BorrowingRecord> returned = service.returnBook(r.getId());
        assertTrue(returned.isPresent());
        assertNotNull(returned.get().getReturnDate());
        assertEquals(1, service.getBookById(createdBook.getId()).get().getAvailableCopies());
    }
}
