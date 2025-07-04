package com.berlin.ms_books_catalogue.data;


import com.berlin.ms_books_catalogue.controller.model.AggregationDetails;
import com.berlin.ms_books_catalogue.controller.model.BooksQueryResponse;
import com.berlin.ms_books_catalogue.data.model.Book;
import com.berlin.ms_books_catalogue.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataAccessRespository {
    //@Value("${server.fullAddress:https://eofj8uoj7g:auimsb91ki@personal-search-4382094174.us-east-1.bonsaisearch.net:443}")
    //private String serverFullAddress;

    // BooksRepository la utilizamos para operaciones sencilla como save , delete que  seguimos utilizando JPA Data
    private final BooksRepository booksRepository;
    // ElasticsearchOperations se utiliza para las busquedas mas complejas  como facets , full-text , search as you type
    private final ElasticsearchOperations elasticClient;

    //Definimos el titulo  y autor  para que haga una busqueda multimachQuery
    private final String[] titleSearchFields = {" title", "title._2gram", "title._3gram"};
    private final String[] authorSearchFields = {" author", "author._2gram", "author._3gram"};

    public Book save(Book book) {
        return booksRepository.save(book);
    }

    public Boolean delete(Book book) {
        booksRepository.delete(book);
        return Boolean.TRUE;
    }

    public Optional<Book> findById(String id) {
        return booksRepository.findById(id);
    }

    public List<Book> findByCategory(String category) {
        return booksRepository.findByCategory(category);
    }

    public List<Book> findByTitle(String title) {
        return booksRepository.findByTitle(title);
    }

    public BooksQueryResponse findBooksMin(String title, String author, Date publication, String category, String isbn,
                                           Boolean visible, Double price, Integer stock, Boolean digital) {

        //se crea la consulta vacia y se ira añadiendo las clausulas para dicha consulta con los datos que hallan definido para ello
        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        //Si se ha seleccionado algun valor con el titulo
        /*if (!StringUtils.isEmpty(title)) {
            querySpec.must(QueryBuilders.multiMatchQuery(title, titleSearchFields).type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
        }*/
        if (!StringUtils.isEmpty(author)) {
            querySpec.must(QueryBuilders.multiMatchQuery(author, authorSearchFields).type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
        }
        if (publication != null) {
            querySpec.must(QueryBuilders.rangeQuery(Consts.PUBLICATION).lte(publication));
        }
        if (!StringUtils.isEmpty(category)) {
            querySpec.must(QueryBuilders.termQuery(Consts.CATEGORY, category));
        }
        if (!StringUtils.isEmpty(isbn)) {
            querySpec.must(QueryBuilders.termQuery(Consts.ISBN, isbn));
        }
        if (visible != null) {
            querySpec.must(QueryBuilders.termQuery(Consts.VISIBLE, true));
        }
        if (price != null) {
            querySpec.must(QueryBuilders.rangeQuery(Consts.PRICE).lte(price));
        }
        if (stock != null) {
            querySpec.must(QueryBuilders.rangeQuery(Consts.STOCK).gte(stock));
        }
        if (digital != null) {
            querySpec.must(QueryBuilders.termQuery(Consts.DIGITAL, digital));
        }
        //Si no se ha seleccionado ningun filtro, se añade un filtro por defecto para que la query no sea vacia
        if (!querySpec.hasClauses()) {
            querySpec.must(QueryBuilders.matchAllQuery());
        }

        //Construimos la query
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);

        //Se incluyen las agregaciones de termino para los campos titulo, categoria
        //if (!StringUtils.isEmpty(title)) {
        //nativeSearchQueryBuilder.addAggregation(AggregationBuilders
        //        .terms(Consts.TITLE_VALUES)
        //        .field(Consts.TITLE).size(10000));
        //}
        if (!StringUtils.isEmpty(category)) {
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                    .terms(Consts.CATEGORY_VALUES)
                    .field(Consts.CATEGORY).size(10000));
        }
        if (price != null) {
            //Se incluyen las agregaciones de rango para el campo precio
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                    .range(Consts.PRICE_VALUES)
                    .field(Consts.PRICE)
                    .addUnboundedTo(Consts.RANGE_PRICE_0, 10)
                    .addRange(Consts.RANGE_PRICE_1, 10, 100)
                    .addUnboundedFrom(Consts.RANGE_PRICE_2, 100));
        }
        //Se establece un maximo de 5 resultados, va acorde con el tamaño de la pagina
        nativeSearchQueryBuilder.withMaxResults(25);

        // se define el  tamaño de la pagina de 5 elementos
        nativeSearchQueryBuilder.withPageable(PageRequest.of(0, 5));

        //Se construye la query
        Query query = nativeSearchQueryBuilder.build();
        // Se realiza la busqueda
        SearchHits<Book> result = elasticClient.search(query, Book.class);
        return new BooksQueryResponse(getResponseBooks(result), getResponseAggregations(result));
    }

    //se crea la consulta de forma nativa para elasticsearch
    @SneakyThrows
    public BooksQueryResponse findBooks(String title, String author, Date publication, String category, String isbn,
                                        Boolean visible, Double price, Integer stock, Boolean digital,
                                        List<String> titleCategory, List<String> priceValues, List<String> titleCategoryPrice, String page) {

        log.info("findBooks, category: {}", category);
        //se crea la consulta vacia y se ira añadiendo las clausulas para dicha consulta con los datos que hallan definido para ello
        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        //Si se ha seleccionado algun valor con el titulo
        //if (!StringUtils.isEmpty(title)) {
        //    querySpec.must(QueryBuilders.multiMatchQuery(title, titleSearchFields).type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
        //}
        //Si se ha seleccionado algun valor con el autor
        if (!StringUtils.isEmpty(author)) {
            querySpec.must(QueryBuilders.multiMatchQuery(author, authorSearchFields).type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
        }
        //Si se ha seleccionado algun valor con el categoria
        if (!StringUtils.isEmpty(category)) {
            querySpec.must(QueryBuilders.termQuery(Consts.CATEGORY, category));
        }
        //Si se ha seleccionado algun valor con el isbn
        if (!StringUtils.isEmpty(isbn)) {
            querySpec.must(QueryBuilders.termQuery(Consts.ISBN, isbn));
        }
        //Si se ha seleccionado algun valor con el precio
        if (priceValues != null && !priceValues.isEmpty())
            priceValues.forEach(
                    prices -> {
                        String[] priceRange = prices != null && prices.contains("-") ? prices.split("-") : new String[]{};

                        if (priceRange.length == 2) {
                            if ("".equals(priceRange[0])) {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.PRICE).to(priceRange[1]).includeUpper(false));
                            } else {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.PRICE).from(priceRange[0]).to(priceRange[1]).includeUpper(false));
                            }
                        }
                        if (priceRange.length == 1) {
                            querySpec.must(QueryBuilders.rangeQuery(Consts.PRICE).from(priceRange[0]));
                        }
                    }
            );

        //En este caso, que los libros  sean visibles
        querySpec.must(QueryBuilders.termQuery(Consts.VISIBLE, true));

        //Si no se ha seleccionado ningun filtro, se añade un filtro por defecto para que la query no sea vacia
        if (!querySpec.hasClauses()) {
            querySpec.must(QueryBuilders.matchAllQuery());
        }

        //Construimos la query
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);

        //Se incluyen las agregaciones de termino para los campos titulo, categoria

        //nativeSearchQueryBuilder.addAggregation(AggregationBuilders
        //        .terms(Consts.TITLE_VALUES)
        //        .field(Consts.TITLE).size(10000));

        nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .terms(Consts.CATEGORY_VALUES)
                .field(Consts.CATEGORY).size(10000));

        //Se incluyen las agregaciones de rango para el campo precio
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .range(Consts.PRICE_VALUES)
                .field(Consts.PRICE)
                .addUnboundedTo(Consts.RANGE_PRICE_0, 10)
                .addRange(Consts.RANGE_PRICE_1, 10, 100)
                .addUnboundedFrom(Consts.RANGE_PRICE_2, 100));

        //Se establece un maximo de 5 resultados, va acorde con el tamaño de la pagina
        nativeSearchQueryBuilder.withMaxResults(25);


        // se define el  tamaño de la pagina de 5 elementos
        int pageInt = Integer.parseInt(page);
        if (pageInt >= 0) {
            nativeSearchQueryBuilder.withPageable(PageRequest.of(pageInt, 5));
        }

        //Se construye la query
        Query query = nativeSearchQueryBuilder.build();
        // Se realiza la busqueda
        SearchHits<Book> result = elasticClient.search(query, Book.class);
        return new BooksQueryResponse(getResponseBooks(result), getResponseAggregations(result));
    }

    /**
     * Metodo que convierte los resultados de la busqueda en una lista de libros.
     *
     * @param result Resultados de la busqueda.
     * @return Lista de libros.
     */
    private List<Book> getResponseBooks(SearchHits<Book> result) {
        return result.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    /**
     * Metodo que convierte las agregaciones de la busqueda en una lista de detalles de agregaciones.
     * Se ha de tener en cuenta que el tipo de agregacion puede ser de tipo rango o de tipo termino.
     *
     * @param result Resultados de la busqueda.
     * @return Lista de detalles de agregaciones.
     */
    private Map<String, List<AggregationDetails>> getResponseAggregations(SearchHits<Book> result) {
        //Mapa de detalles de agregaciones
        Map<String, List<AggregationDetails>> responseAggregations = new HashMap<>();

        //Recorremos las agregaciones
        if (result.hasAggregations()) {
            Map<String, Aggregation> aggs = result.getAggregations().asMap();

            //Recorremos las agregaciones
            aggs.forEach((key, value) -> {

                //Si no existe la clave en el mapa, la creamos
                if (!responseAggregations.containsKey(key)) {
                    responseAggregations.put(key, new LinkedList<>());
                }

                //Si la agregacion es de tipo termino, recorremos los buckets
                if (value instanceof ParsedStringTerms parsedStringTerms) {
                    parsedStringTerms.getBuckets().forEach(bucket -> {
                        responseAggregations.get(key).add(new AggregationDetails(bucket.getKey().toString(), (int) bucket.getDocCount()));
                    });
                }

                //Si la agregacion es de tipo rango, recorremos tambien los buckets
                if (value instanceof ParsedRange parsedRange) {
                    parsedRange.getBuckets().forEach(bucket -> {
                        responseAggregations.get(key).add(new AggregationDetails(bucket.getKeyAsString(), (int) bucket.getDocCount()));
                    });
                }
            });
        }
        return responseAggregations;

    }

    public List<String> getBookCategories() {
        // Construir la consulta de agregación
        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery()) // Consulta para obtener todos los documentos
                .addAggregation(AggregationBuilders.terms("categoryAgg").field(Consts.CATEGORY).size(10000)) // Agregación de términos en el campo "category"
                .build();

        // Ejecutar la consulta
        SearchHits<Book> result = elasticClient.search(query, Book.class);

        // Extraer los términos de la agregación
        ParsedStringTerms categoryAgg = result.getAggregations().get("categoryAgg");
        List<String> categories = categoryAgg.getBuckets().stream()
                .map(bucket -> bucket.getKeyAsString())
                .collect(Collectors.toList());

        return categories;
    }
}
