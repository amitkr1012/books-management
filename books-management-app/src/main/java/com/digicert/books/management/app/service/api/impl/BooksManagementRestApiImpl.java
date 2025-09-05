package com.digicert.books.management.app.service.api.impl;

import com.digicert.books.management.app.datamapper.BookMngtDatamapper;
import com.digicert.books.management.app.entity.Book;
import com.digicert.books.management.app.service.BooksService;
import com.digicert.openApi.model.service.api.BooksApi;
import com.digicert.openApi.model.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/digicert/books/management/v1")
public class BooksManagementRestApiImpl implements BooksApi {
    @Autowired
    private BooksService booksService;
    @Autowired
    private BookMngtDatamapper bookMngtDatamapper;

    @GetMapping("/findAll")
    public  ResponseEntity<PageBook> findAll(Pageable page){
        PageBook pb = booksService.findAll(page);
        return ResponseEntity.ok(pb);
    }

    @Override
    public ResponseEntity<PageBook> booksGet(Integer page, Integer size, List<String> sort) {
        Pageable pageable=bookMngtDatamapper.toPageable(page,size,sort);
        PageBook pb = booksService.findAll(pageable);
        return ResponseEntity.ok(pb);
    }

    @Override
    public ResponseEntity<BookDTO> booksIdDelete(Long id, Integer ifMatch){
        BookDTO book = booksService.delete(id,ifMatch);
        return ResponseEntity.ok()
                .eTag(String.valueOf(book.getVersion()))
                .body(book);

    }

    @Override
    public ResponseEntity<BookDTO> booksPost(BookRequest bookRequest){
        BookDTO created = booksService.create(bookRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .eTag(String.valueOf(created.getVersion()))
                .body(created);
    }

    @Override
    public ResponseEntity<BookDTO> booksIdGet(Long id) {

        BookDTO book = booksService.findById(id);
        return ResponseEntity.ok()
                .eTag(String.valueOf(book.getVersion()))
                .body(book);
    }

    @Override
    public ResponseEntity<BookDTO> booksIdPatch(Long id, Integer ifMatch, BookUpdateRequest bookUpdateRequest) {
        BookDTO book = booksService.updatePartial(id, bookUpdateRequest, ifMatch);
        return ResponseEntity.ok().eTag(String.valueOf(book.getVersion())).body(book);
    }

    public ResponseEntity<BookDTO> booksIdPut(Long id, Integer ifMatch, BookRequest bookRequest) {
        BookDTO replace = booksService.replace(id, bookRequest, ifMatch);

        return ResponseEntity.ok().eTag(String.valueOf(replace.getVersion())).body(replace);
    }

}
