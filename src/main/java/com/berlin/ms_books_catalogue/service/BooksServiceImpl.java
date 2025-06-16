package com.berlin.ms_books_catalogue.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.berlin.ms_books_catalogue.controller.model.BookDto;
import com.berlin.ms_books_catalogue.controller.model.CreateBooksRequest;
import com.berlin.ms_books_catalogue.data.BooksRepository;
import com.berlin.ms_books_catalogue.data.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;



import java.sql.Date;
import java.util.List;

@Service
@Slf4j
public abstract class BooksServiceImpl implements BooksService  {

    @Autowired
    private BooksRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Book> getBooks(String title, String author, java.util.Date publication, String category, String isbn, Integer score ,
                               Boolean visible, Integer price, Integer stock, Boolean digital){

        if (StringUtils.hasLength(title) || StringUtils.hasLength(author) || publication != null || StringUtils.hasLength(category)
                || StringUtils.hasLength(isbn) || score != null  || visible != null || price != null || digital != null) {

            return repository.search(title, author, publication, category, isbn, score , visible);
        }

        List<Book> books = repository.getBooks();
        return books.isEmpty() ? null : books;
    }

    @Override
    public Book getBook(String booksId) {
        return repository.getById(Long.valueOf(booksId));
    }

    @Override
    public Boolean removeBook(String booksId) {

        Book books = repository.getById(Long.valueOf(booksId));

        if (books != null) {
            repository.delete(books);
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
                && request.getScore() != null
                && request.getVisible() != null
                && request.getPrice() != null
                && request.getStock() != null
                && request.getDigital() != null) {

            Book books = Book.builder().title(request.getTitle()).author(request.getAuthor())
                    .publication(request.getPublication()).category(request.getCategory())
                    .isbn(request.getIsbn()).score(request.getScore()).visible(request.getVisible())
                    .price(request.getPrice()).stock(request.getStock()).digital(request.getDigital()).build();
            return repository.save(books);
        } else {
            return null;
        }
    }

    /* @Override
   public Books updateBook(String BooksId, String request) {
        Books books = repository.getById(Long.valueOf(BooksId));
        if (books != null) {
            try {
                JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
                JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(books)));
                Books patched = objectMapper.treeToValue(target, Books.class);
                repository.save(patched);
                return patched;
            } catch (JsonProcessingException | JsonPatchException e) {
                log.error("Error updating books {}", books, e);
                return null;
            }
        } else {
            return null;
        }
    }*/
    @Override
    public Book updateBook(String booksId, BookDto updateRequest) {
        Book books = repository.getById(Long.valueOf(booksId));
        if (books != null) {
            books.update(updateRequest);
            repository.save(books);
            return books;
        } else
            return null;
        }
    }


