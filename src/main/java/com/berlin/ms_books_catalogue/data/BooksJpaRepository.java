package com.berlin.ms_books_catalogue.data;

import com.berlin.ms_books_catalogue.data.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.List;

interface BooksJpaRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByIsbn(String isbn);
    List<Book> findByCategory(String category);
    List<Book> findByValoration(String score);
    List<Book> findByVisible(String visible);
    List<Book> findByPublication(String publication);
    List<Book> findByTitleAuthor(String title, String author);
    List<Book> findByCodeIsbnAuthor(String codeIsbn, String author);
    List<Book> findByCategoryAuthor(String category, String author);
    List<Book> findByValorationAuthor(String score, String author);
    List<Book> findByDateAuthor(String date, String author);
    List<Book> findByTitleValoration(String title, String score);

}
