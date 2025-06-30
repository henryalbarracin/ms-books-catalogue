package com.berlin.ms_books_catalogue.service;

import com.berlin.ms_books_catalogue.controller.model.BooksQueryResponse;
import com.berlin.ms_books_catalogue.controller.model.CreateBooksRequest;
import com.berlin.ms_books_catalogue.data.model.Book;

import java.util.Date;
import java.util.List;

public interface BooksService {

  BooksQueryResponse  getBooks(String title, String author, Date publication, String category, String isbn,
                               Boolean visible, Double price, Integer stock, Boolean digital ,
                               List<String> titleValues,List<String> priceValues,
                               List<String>categoryValues, String  page);

  Book getBook(String booksId);

  Boolean removeBook(String booksId);

  Book createBook(CreateBooksRequest request);

}