package com.app.library.controllers;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.library.models.Book;
import com.app.library.models.BorrowingRecord;
import com.app.library.models.Member;
import com.app.library.services.LibraryService;

@RestController
@RequestMapping("/api")
public class LibraryController {
    // Logger for request/response tracing and debugging
    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);
    // LAB 3: annotated for Lab 3 identification

    @Autowired
    private LibraryService libraryService;

    // ==================== Book Endpoints ====================

    // Return all books currently stored in the in-memory repository
    @GetMapping("/books")
    public ResponseEntity<Collection<Book>> getAllBooks() {
        Collection<Book> books = libraryService.getAllBooks();
        logger.info("Returning list of books: {}", books);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Return a single book by its ID. If not found, respond 404.
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> bookOpt = libraryService.getBookById(id);
        logger.info("Lookup book id={}", id);
        return bookOpt.map(b -> new ResponseEntity<>(b, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new book resource. The created book is returned with 201.
    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book created = libraryService.addBook(book);
        logger.info("Added book id={}", created.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Update an existing book. If the book does not exist, return 404.
    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        updatedBook.setId(id);
        Optional<Book> updated = libraryService.updateBook(updatedBook);
        if (updated.isPresent()) {
            logger.info("Updated book id={}", id);
            return new ResponseEntity<>(updated.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a book by ID. Returns 204 No Content on success.
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean removed = libraryService.deleteBook(id);
        if (!removed) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Deleted book id={}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ==================== Member Endpoints ====================

    // Return all members
    @GetMapping("/members")
    public ResponseEntity<Collection<Member>> getAllMembers() {
        Collection<Member> members = libraryService.getAllMembers();
        logger.info("Returning members count={}", members.size());
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    // Return a member by ID or 404 if not found
    @GetMapping("/members/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> memberOpt = libraryService.getMemberById(id);
        logger.info("Lookup member id={}", id);
        return memberOpt.map(m -> new ResponseEntity<>(m, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Add a new member
    @PostMapping("/members")
    public ResponseEntity<Member> addMember(@RequestBody Member member) {
        Member created = libraryService.addMember(member);
        logger.info("Added member id={}", created.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Update an existing member
    @PutMapping("/members/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member updatedMember) {
        updatedMember.setId(id);
        Optional<Member> updated = libraryService.updateMember(updatedMember);
        if (updated.isPresent()) {
            logger.info("Updated member id={}", id);
            return new ResponseEntity<>(updated.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a member by ID
    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        boolean removed = libraryService.deleteMember(id);
        if (!removed) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Deleted member id={}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ==================== BorrowingRecord Endpoints ====================

    // Return all borrowing records
    @GetMapping("/borrowing-records")
    public ResponseEntity<Collection<BorrowingRecord>> getAllBorrowingRecords() {
        Collection<BorrowingRecord> records = libraryService.getAllBorrowingRecords();
        logger.info("Returning borrowing records count={}", records.size());
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // Create a borrowing record (borrow a book). This sets borrow/due dates and persists the record.
    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestBody BorrowingRecord record) {
        if (record == null || record.getBook() == null || record.getMember() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long bookId = record.getBook().getId();
        Long memberId = record.getMember().getId();
        if (bookId == null || memberId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // If book or member not found -> 404
        if (!libraryService.getBookById(bookId).isPresent() || !libraryService.getMemberById(memberId).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<BorrowingRecord> created = libraryService.borrowBook(record);
        if (!created.isPresent()) {
            // e.g., no available copies or other business rule violation
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Borrowed book id={} by member={}", bookId, memberId);
        return new ResponseEntity<>(created.get(), HttpStatus.CREATED);
    }

    // Mark a borrowing record as returned. The service updates available copies accordingly.
    @PutMapping("/return/{recordId}")
    public ResponseEntity<?> returnBook(@PathVariable Long recordId) {
        Optional<BorrowingRecord> rec = libraryService.returnBook(recordId);
        if (!rec.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Returned borrowing record id={}", recordId);
        return new ResponseEntity<>(rec.get(), HttpStatus.OK);
    }
}
