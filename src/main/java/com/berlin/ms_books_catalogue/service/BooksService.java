package com.berlin.ms_books_catalogue.service;


import java.util.Date;
import java.util.List;
import com.berlin.ms_books_catalogue.data.model.Book;
import com.berlin.ms_books_catalogue.controller.model.BookDto;
import com.berlin.ms_books_catalogue.controller.model.CreateBooksRequest;

public interface BooksService {

  List<Book> getBooks(String title, String author, Date publication, String category, String isbn, Integer score ,
                      Boolean visible, Integer price, Integer stock, Boolean digital);
  Book getBook(String id);
  Book createBook(CreateBooksRequest request);
  Boolean removeBook(String  bookId);
  Book updateBook( String  bookId, BookDto updateRequest);
  Book updateBook( String  bookId, String updateRequest);



}
