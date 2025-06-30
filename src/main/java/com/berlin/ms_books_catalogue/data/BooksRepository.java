package com.berlin.ms_books_catalogue.data;

import com.berlin.ms_books_catalogue.data.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends ElasticsearchRepository<Book, String> {

    List<Book> findByTitle(String title);

    Optional<Book> findById(String id);

    Book save(Book book);

    void delete(Book book);

    List<Book> findAll();

    String id(String id);
}