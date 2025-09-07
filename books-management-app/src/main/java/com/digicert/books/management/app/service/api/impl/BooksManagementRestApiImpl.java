package com.digicert.books.management.app.service.api.impl;

import com.digicert.books.management.app.datamapper.BookMngtDatamapper;
import com.digicert.books.management.app.service.BooksService;
import com.digicert.openApi.model.service.api.BooksApi;
import com.digicert.openApi.model.service.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/digicert/books/management/v1")
public class BooksManagementRestApiImpl implements BooksApi {
    @Autowired
    private BooksService booksService;
    @Autowired
    private BookMngtDatamapper bookMngtDatamapper;

    private static final String SUCCESS="SUCCESS";

    @Autowired
    HttpServletRequest servletRequest;

    @Override
    public ResponseEntity<PageBook> fetchAllBook(Integer page, Integer size, List<String> sort) {
        String[] sort2 = servletRequest.getParameterValues("sort");
        if(sort2!=null&& sort2.length>0){
            sort=Arrays.asList(sort2);
        }
        Pageable pageable=bookMngtDatamapper.toPageable(page,size,sort);
        PageBook pb = booksService.findAll(pageable);
        return ResponseEntity.ok(pb);
    }

    @Override
    public ResponseEntity<ResultStatus> deleteBookById(Long id, Integer ifMatch){
        BookDTO book = booksService.delete(id,ifMatch);
        ResultStatus rs=new ResultStatus();
        rs.setStatus(SUCCESS);
        rs.setMessage("Book has been delete for ID "+id);
        rs.setBookRes(book);
        return ResponseEntity.ok()
                .eTag(String.valueOf(book.getVersion()))
                .body(rs);

    }

    @Override
    public ResponseEntity<ResultStatus> createBook(BookRequest bookRequest){
        BookDTO book = booksService.create(bookRequest);
        ResultStatus rs=new ResultStatus();
        rs.setStatus(SUCCESS);
        rs.setMessage("Book has been successfully created");
        rs.setBookRes(book);
        return ResponseEntity.status(HttpStatus.CREATED)
                .eTag(String.valueOf(book.getVersion()))
                .body(rs);
    }

    @Override
    public ResponseEntity<ResultStatus> getBookById(Long id) {

        BookDTO book = booksService.findById(id);
        ResultStatus rs=new ResultStatus();
        rs.setStatus(SUCCESS);
        rs.setBookRes(book);
        return ResponseEntity.ok()
                .eTag(String.valueOf(book.getVersion()))
                .body(rs);
    }

    @Override
    public ResponseEntity<ResultStatus> updateBook(Long id, Integer ifMatch, BookUpdateRequest bookUpdateRequest) {
        BookDTO book = booksService.updatePartial(id, bookUpdateRequest, ifMatch);
        ResultStatus rs=new ResultStatus();
        rs.setStatus(SUCCESS);
        rs.setMessage("Book has been updated for ID "+id);
        rs.setBookRes(book);
        return ResponseEntity.ok().eTag(String.valueOf(book.getVersion())).body(rs);
    }

    public ResponseEntity<ResultStatus> replaceBook(Long id, Integer ifMatch, BookUpdateRequest bookRequest) {
        BookDTO replace = booksService.replace(id, bookRequest, ifMatch);
        ResultStatus rs=new ResultStatus();
        rs.setStatus(SUCCESS);
        rs.setMessage("Book has been Updated for ID "+id);
        rs.setBookRes(replace);

        return ResponseEntity.ok().eTag(String.valueOf(replace.getVersion())).body(rs);
    }

}
