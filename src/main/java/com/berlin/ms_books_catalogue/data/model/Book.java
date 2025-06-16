package com.berlin.ms_books_catalogue.data.model;

import com.berlin.ms_books_catalogue.controller.model.BookDto;
import com.berlin.ms_books_catalogue.data.utils.Consts;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Consts.TITLE)
    private String title;
	
	@Column(name = Consts.AUTHOR)
    private String author;

    @Column(name = Consts.PUBLISH)
    @Temporal(TemporalType.TIMESTAMP)
    public Date publication;

    @Column(name = Consts.CATEGORY)
    private String category;

    @Column(name = Consts.ISBN)
    private String isbn;

    @Column(name = Consts.SCORE)
    private Integer score;

    @Column(name = Consts.VISIBLE)
    private Boolean visible;
	
	@Column(name = Consts.PRICE)
    private Integer price;
	
	@Column(name = Consts.STOCK)
    private Integer stock;
	
	@Column(name = Consts.DIGITAL)
    private Boolean digital;


    public void update(BookDto bookDto) {
        this.id = id;
        this.title = bookDto.getTitle();
        this.author = bookDto.getAuthor();
		this.publication = bookDto.getPublication();
        this.category = bookDto.getCategory();
        this.isbn = bookDto.getIsbn();
		this.score = bookDto.getScore();
        this.visible = bookDto.getVisible();
		this.price = bookDto.getPrice();
		this.stock = bookDto.getStock();
		this.digital = bookDto.getDigital();
    }
}

