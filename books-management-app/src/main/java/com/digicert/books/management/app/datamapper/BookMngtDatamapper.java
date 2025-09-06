package com.digicert.books.management.app.datamapper;

import com.digicert.books.management.app.entity.Book;
import com.digicert.openApi.model.service.model.BookDTO;
import com.digicert.openApi.model.service.model.PageBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMngtDatamapper {

    public BookDTO toBookDTO(Book books){
        BookDTO bookDto=new BookDTO();
        bookDto.setId(books.getId());
        bookDto.setAuthor(books.getAuthor());
        bookDto.setTitle(books.getTitle());
        books.setId(books.getId());
        bookDto.setVersion(books.getVersion());
        bookDto.setCreatedAt(books.getCreatedAt());
        bookDto.setUpdatedAt(books.getUpdatedAt());
        bookDto.setPublicationYear(books.getPublicationYear());
        return bookDto;
    }

    public PageBook toPageBook(Page<Book> book){
        PageBook pb=new PageBook();
        pb.setPage(book.getNumber());
        pb.setTotalPages(book.getTotalPages());
        pb.setLast(book.isLast());
        pb.setFirst(book.isFirst());
        pb.setTotalElements(book.getTotalElements());
        pb.setSize(book.getSize());
        pb.setContent(book.getContent().stream().map(this::toBookDTO).toList());
        return pb;
    }

    public Pageable toPageable(Integer page, Integer size, List<String> sort) {
        if (sort == null || sort.isEmpty()) {
            return PageRequest.of(page, size); // no sorting
        }

        // Example: sort=title,asc&sort=author,desc
        List<Sort.Order> orders = sort.stream()
                .map(s -> {
                    String[] parts = s.split(",");
                    String property = parts[0];
                    Sort.Direction direction = (parts.length > 1)
                            ? Sort.Direction.fromString(parts[1])
                            : Sort.Direction.ASC; // default asc
                    return new Sort.Order(direction, property);
                })
                .toList();

        return PageRequest.of(page, size, Sort.by(orders));
    }
}

