package com.digicert.books.management.app.service.impl;

import com.digicert.books.management.app.dao.BookDao;
import com.digicert.books.management.app.datamapper.BookMngtDatamapper;
import com.digicert.books.management.app.entity.Book;
import com.digicert.books.management.app.service.BooksService;
import com.digicert.openApi.model.service.model.BookDTO;
import com.digicert.openApi.model.service.model.BookRequest;
import com.digicert.openApi.model.service.model.BookUpdateRequest;
import com.digicert.openApi.model.service.model.PageBook;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BooksServiceImpl implements BooksService {
    @Autowired
    BookDao bookDao;
    @Autowired
    BookMngtDatamapper bookMngtDatamapper;
    @Override
    public BookDTO findById(Long id) {
        Optional<Book> byId = bookDao.findById(id);
        return byId.map(bookMngtDatamapper::toBookDTO).orElseThrow(
                ()->new NoSuchElementException("Book Not Found : "+id));
    }

    @Override
    public PageBook findAll(Pageable pageable) {
        Page<Book> pb = bookDao.findAll(pageable);
        return bookMngtDatamapper.toPageBook(pb);
    }

    @Override
    @Transactional
    public BookDTO create(BookRequest request) {
        Book book=new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        if(request.getPublicationYear()!=null){
            book.setPublicationYear(request.getPublicationYear());
        }
        book.setCreatedAt(new Date());
        book.setUpdatedAt(new Date());
        Book save = bookDao.save(book);
        return bookMngtDatamapper.toBookDTO(save);
    }

    @Override
    @Transactional
    public BookDTO replace(Long id, BookRequest request, int version) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found: " + id));

        // Optimistic lock check
        if (version!=(book.getVersion())) {
            throw new OptimisticLockException("Version conflict for book id=" + id);
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setUpdatedAt(new Date());
        book.setPublicationYear(request.getPublicationYear());

        return bookMngtDatamapper.toBookDTO(bookDao.save(book));
    }
    @Transactional
    @Override
    public BookDTO updatePartial(Long id, BookUpdateRequest request, int version) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found: " + id));

        if (version!=(book.getVersion())) {
            throw new OptimisticLockException("Version conflict for book id=" + id);
        }

        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
       // if (request.getIsbn() != null) book.setIsbn(request.getIsbn());
        if (request.getPublicationYear() != null) book.setPublicationYear(request.getPublicationYear());
        book.setUpdatedAt(new Date());

        return bookMngtDatamapper.toBookDTO(bookDao.save(book));
    }

    @Override
    public BookDTO delete(Long id, int version) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found: " + id));

        if (version!=(book.getVersion())) {
            throw new OptimisticLockException("Version conflict for book id=" + id);
        }

        try {
            bookDao.delete(book);
        } catch (EmptyResultDataAccessException ex) {
            throw new NoSuchElementException("Book not found: " + id);
        }
        return  bookMngtDatamapper.toBookDTO(book);
    }
}
