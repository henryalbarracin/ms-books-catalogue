package com.berlin.ms_books_catalogue.controller;

import com.berlin.ms_books_catalogue.controller.model.BooksQueryResponse;
import com.berlin.ms_books_catalogue.controller.model.CreateBooksRequest;
import com.berlin.ms_books_catalogue.data.model.Book;
import com.berlin.ms_books_catalogue.service.BooksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BooksController {

        private final BooksService service;

        @GetMapping("/test")
        public String test() {
                return "test: respuesta de prueba de micro ms-books-catalogue";
        }

        @GetMapping("/books")
        public ResponseEntity<BooksQueryResponse>getBooks(
                @RequestParam(required = false) String title,
                @RequestParam(required = false) String author,
                @RequestParam(required = false) Date publication,
                @RequestParam(required = false) String category,
                @RequestParam(required = false) String isbn,
                @RequestParam(required = false, defaultValue = "false") Boolean visible,
                @RequestParam(required = false, defaultValue = "false") Boolean digital,
                @RequestParam(required = false) Double price,
                @RequestParam(required = false) Integer stock,
                @RequestParam(required = false) List<String> titleValues,
                @RequestParam(required = false) List<String> priceValues,
                @RequestParam(required = false) List<String> categoryValues,
                @RequestParam(required = false, defaultValue = "0") String page)

        {
                BooksQueryResponse books = service.getBooks(title, author, publication, category, isbn, visible, price, stock,
                        digital, titleValues,priceValues,categoryValues,page);
                return ResponseEntity.ok(books);
        }


        @GetMapping("/books/{booksId}")
        public ResponseEntity<Book> getBook(@PathVariable String booksId) {

                log.info("Request received for book {}", booksId);
                Book book = service.getBook(booksId);

                if (book != null) {
                        return ResponseEntity.ok(book);
                } else {
                        return ResponseEntity.notFound().build();
                }

        }

        @DeleteMapping("/books/{booksId}")
        public ResponseEntity<Void> deleteBook(@PathVariable String booksId) {

                Boolean removed = service.removeBook(booksId);

                if (Boolean.TRUE.equals(removed)) {
                        return ResponseEntity.ok().build();
                } else {
                        return ResponseEntity.notFound().build();
                }

        }

        @PostMapping("/books")
        public ResponseEntity<Book> getBook(@RequestBody CreateBooksRequest request) {

                Book createdBook = service.createBook(request);

                if (createdBook != null) {
                        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
                } else {
                        return ResponseEntity.badRequest().build();
                }

        }


}
