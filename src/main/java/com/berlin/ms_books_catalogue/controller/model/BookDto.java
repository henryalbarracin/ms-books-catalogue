package com.berlin.ms_books_catalogue.controller.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
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
