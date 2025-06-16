package com.berlin.ms_books_catalogue.controller;


import com.berlin.ms_books_catalogue.data.model.Book;
import com.berlin.ms_books_catalogue.service.BooksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Books Controller", description = "Microservicio encargado de exponer operaciones CRUD" )
public class BooksController {

        private final BooksService service;

        @GetMapping("/book")
        @Operation(
                operationId = "Obtener libros",
                description = "Operacion de lectura",
                summary = "Se devuelve una lista de todos los libros almacenados")
        @ApiResponse(
                responseCode = "200",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
        public ResponseEntity<List<Book>> getBooks(
                @RequestHeader Map<String, String> headers,
                @Parameter(name = "title", description = "Nombre del libro", required = false)
                @RequestParam(required = false) String title,

                @Parameter(name = "author", description = "Nombre del autor del libro", required = false)
                @RequestParam(required = false) String author,

                @Parameter(name = "publication", description = "Fecha de publicacion del libro", required = false)
                @RequestParam(required = false) Date publication,

                @Parameter(name = "category", description = "Categoria del libro ", required = false)
                @RequestParam(required = false) String category,

                @Parameter(name = "isbn", description = "Codigo del libro ", required = false)
                @RequestParam(required = false) String isbn,

                @Parameter(name = "score", description = "Valoracion del libro ", required = false)
                @RequestParam(required = false) Integer score,

                @Parameter(name = "visible", description = "Estado del producto. true o false", required = false)
                @RequestParam(required = false) Boolean visible,

                @Parameter(name = "price", description = "Precio del producto.", required = false)
                @RequestParam(required = false) Integer price,

                @Parameter(name = "stock", description = "Cantidad del producto.", required = false)
                @RequestParam(required = false) Integer stock,

                @Parameter(name = "digital", description = "indicador de tipo del producto. digital o fisico", required = false)
                @RequestParam(required = false) Boolean digital

        )
    {
            log.info("headers: {}", headers);
            List<Book> books = service.getBooks(title, author, publication, category, isbn, score, visible, price, stock, digital);

            if (books != null) {
                return ResponseEntity.ok(books);
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }



    }

