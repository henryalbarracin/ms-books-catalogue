package com.berlin.ms_books_catalogue.service;


import com.berlin.ms_books_catalogue.controller.model.BooksQueryResponse;
import com.berlin.ms_books_catalogue.controller.model.CreateBooksRequest;
import com.berlin.ms_books_catalogue.data.DataAccessRespository;
import com.berlin.ms_books_catalogue.data.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor

public class BooksServiceImpl implements BooksService {

    private final DataAccessRespository respository;

    @Override
    public BooksQueryResponse getBooksMin(String title, String author, Date publication, String category, String isbn,
                                       Boolean visible, Double price, Integer stock, Boolean digital) {
        return respository.findBooksMin(title, author, publication, category, isbn,  visible, price, stock, digital);
    }

    @Override
    public BooksQueryResponse getBooks(String title, String author, Date publication, String category, String isbn,
                                       Boolean visible, Double price, Integer stock, Boolean digital,
                                       List<String>titleValues, List<String> priceValues,
                                       List<String>categoryValues, String  page) {
        return respository.findBooks(title, author, publication, category, isbn,  visible, price, stock, digital,
                titleValues, priceValues,categoryValues, page);
    }

    @Override
    public Book getBook(String booksId) {
        return respository.findById(booksId).orElse(null);
    }

    @Override
    public List<Book> getBookByCategory(String category) {
        return respository.findByCategory(category);
    }
    @Override
    public List<Book> getBookByTitle(String title){
        return respository.findByTitle(title);
    }

    @Override
    public Boolean removeBook(String booksId) {
        Book book = respository.findById(booksId).orElse(null);
        if (book != null) {
            respository.delete(book);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Book createBook(CreateBooksRequest request) {
        if (request != null && org.springframework.util.StringUtils.hasLength(request.getTitle().trim())
                && org.springframework.util.StringUtils.hasLength(request.getAuthor().trim())
                && request.getPublication() != null
                && org.springframework.util.StringUtils.hasLength(request.getCategory().trim())
                && org.springframework.util.StringUtils.hasLength(request.getIsbn().trim())
                && request.getVisible() != null
                && request.getPrice() != null
                && request.getStock() != null
                && request.getDigital() != null) {

            Book books = Book.builder().title(request.getTitle()).author(request.getAuthor())
                    .publication(String.valueOf(request.getPublication())).category(request.getCategory())
                    .isbn(request.getIsbn()).visible(request.getVisible())
                    .price(String.valueOf(request.getPrice()))
                    .stock(String.valueOf(request.getStock())).digital(request.getDigital()).build();
            return respository.save(books);
        } else {
            return null;
        }

    }

    public List<String> getBookCategories() {
        return respository.getBookCategories();
    }
}
