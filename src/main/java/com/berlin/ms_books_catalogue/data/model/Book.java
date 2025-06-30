package com.berlin.ms_books_catalogue.data.model;

import com.berlin.ms_books_catalogue.utils.Consts;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "books", createIndex = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {

    @Id
    private String id;

    @Field(type = FieldType.Search_As_You_Type, name = Consts.AUTHOR)
    private String author;

    @Field(type = FieldType.Keyword, name = Consts.CATEGORY)
    private String category;

    @Field(type = FieldType.Boolean, name = Consts.DIGITAL)
    private Boolean digital;

    @Field(type = FieldType.Keyword, name = Consts.ISBN)
    private String isbn;

    @Field(type = FieldType.Double, name = Consts.PRICE)
    private String price;

    @Field(type = FieldType.Date, name = Consts.PUBLICATION)
    private String publication;

    @Field(type = FieldType.Integer, name = Consts.STOCK)
    private String stock;

    @Field(type = FieldType.Search_As_You_Type, name = Consts.TITLE)
    private String title;

    @Field(type = FieldType.Boolean, name = Consts.VISIBLE)
    private Boolean visible;

}

