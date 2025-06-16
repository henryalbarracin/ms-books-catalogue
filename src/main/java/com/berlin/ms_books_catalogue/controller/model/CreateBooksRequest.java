package com.berlin.ms_books_catalogue.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBooksRequest {
    private String title;
    private String author;
    private Date publication;
    private String category;
    private String isbn;
    private Integer score;
    private Boolean visible;
    private Integer price;
    private Integer stock;
    private Boolean digital;
}
