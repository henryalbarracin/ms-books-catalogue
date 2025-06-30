package com.berlin.ms_books_catalogue.controller;

import com.berlin.ms_books_catalogue.controller.model.BooksQueryResponse;
import com.berlin.ms_books_catalogue.controller.model.CreateBooksRequest;
import com.berlin.ms_books_catalogue.data.model.Book;
import com.berlin.ms_books_catalogue.service.BooksService;
import io.swagger.v3.oas.annotations.Parameter;
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
    public ResponseEntity<BooksQueryResponse> getBooks(
            @Parameter(name = "title", description = "Nombre del libro", required = false)
            @RequestParam(required = false) String title,
            @Parameter(name = "author", description = "Nombre del autor del libro", required = false)
            @RequestParam(required = false) String author,
            @Parameter(name = "publication", description = "Fecha de publicacion del libro", required = false)
            @RequestParam(required = false) Date publication,
            @Parameter(name = "category", description = "Categoria del libro ", required = false)
            @RequestParam(required = false) String category,
            @Parameter(name = "isbn", description = "Codigo del libro ", required = false)
            @RequestParam(required = false) String isbn,
            @Parameter(name = "visible", description = "Estado del producto. true o false", required = false)
            @RequestParam(required = false, defaultValue = "true") Boolean visible,
            @Parameter(name = "digital", description = "indicador de tipo del producto. digital o fisico", required = false)
            @RequestParam(required = false, defaultValue = "true") Boolean digital,
            @Parameter(name = "price", description = "Precio del producto.", required = false)
            @RequestParam(required = false) Double price,
            @Parameter(name = "stock", description = "Cantidad del producto.", required = false)
            @RequestParam(required = false) Integer stock,

            @RequestParam(required = false) List<String> titleValues,
            @RequestParam(required = false) List<String> priceValues,
            @RequestParam(required = false) List<String> categoryValues,
            @RequestParam(required = false, defaultValue = "0") String page) {

        log.info("Request received for search book, category: {}", category);
        BooksQueryResponse books = service.getBooks(title, author, publication, category, isbn, visible, price, stock,
                digital, titleValues, priceValues, categoryValues, page);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/booksMin")
    public ResponseEntity<BooksQueryResponse> getBooksMin(
            @Parameter(name = "title", description = "Nombre del libro", required = false)
            @RequestParam(required = false) String title,
            @Parameter(name = "author", description = "Nombre del autor del libro", required = false)
            @RequestParam(required = false) String author,
            @Parameter(name = "publication", description = "Fecha de publicacion del libro", required = false)
            @RequestParam(required = false) Date publication,
            @Parameter(name = "category", description = "Categoria del libro ", required = false)
            @RequestParam(required = false) String category,
            @Parameter(name = "isbn", description = "Codigo del libro ", required = false)
            @RequestParam(required = false) String isbn,
            @Parameter(name = "visible", description = "Estado del producto. true o false", required = false)
            @RequestParam(required = false, defaultValue = "true") Boolean visible,
            @Parameter(name = "digital", description = "indicador de tipo del producto. digital o fisico", required = false)
            @RequestParam(required = false, defaultValue = "true") Boolean digital,
            @Parameter(name = "price", description = "Precio del producto.", required = false)
            @RequestParam(required = false) Double price,
            @Parameter(name = "stock", description = "Cantidad del producto.", required = false)
            @RequestParam(required = false) Integer stock) {

        log.info("Request received for search book, category: {}", category);
        BooksQueryResponse books = service.getBooksMin(title, author, publication, category, isbn, visible, price, stock,
                digital);
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

    @GetMapping("/booksbycategory")
    public List<Book> getBookByCategory(
            @Parameter(name = "category", required = false)
            @RequestParam(required = false) String category) {
        log.info("Request received for search book, category: {}", category);
        if (category != null) {
            return service.getBookByCategory(category);
        } else {
            return Collections.emptyList();
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

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getBookCategories() {
        List<String> categories = service.getBookCategories();
        return ResponseEntity.ok(categories);
    }


}
