package com.berlin.ms_books_catalogue.data;

import com.berlin.ms_books_catalogue.data.model.Book;
import com.berlin.ms_books_catalogue.data.utils.SearchCriteria;
import com.berlin.ms_books_catalogue.data.utils.SearchOperation;
import com.berlin.ms_books_catalogue.data.utils.SearchStatement;
import com.berlin.ms_books_catalogue.data.utils.Consts;
import com.berlin.ms_books_catalogue.service.BooksService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class BooksRepository {

    private final BooksJpaRepository repository;

    public List<Book> getBooks(){
        return repository.findAll();
    }

        public Book getById(Long id){return  repository.findById(id).orElse(null);}

        public Book save(Book books) {return repository.save(books);}

        public void delete(Book books){repository.delete(books);}

        public List<Book> search (String title, String author, Date publication, String category, String isbn, Integer score, Boolean visible){
                    SearchCriteria<Book> spec = new SearchCriteria<>();

                if (StringUtils.isNotBlank(title)) {
                    spec.add(new SearchStatement(Consts.TITLE, title, SearchOperation.MATCH));}

                if (StringUtils.isNotBlank(author)) {
                    spec.add(new SearchStatement(Consts.AUTHOR, author, SearchOperation.EQUAL));}

                if (publication != null) {
                    spec.add(new SearchStatement(Consts.PUBLISH, publication, SearchOperation.EQUAL));}

                if (StringUtils.isNotBlank(category)) {
                    spec.add(new SearchStatement(Consts.CATEGORY, category, SearchOperation.MATCH));}

                if (StringUtils.isNotBlank(isbn)) {
                    spec.add(new SearchStatement(Consts.ISBN, category, SearchOperation.EQUAL));}

                if (score != null) {
                   spec.add(new SearchStatement(Consts.SCORE, score, SearchOperation.MATCH));}

                if (visible != null) {
                   spec.add(new SearchStatement(Consts.VISIBLE, visible, SearchOperation.EQUAL));}

                  return repository.findAll(spec);
     }

}