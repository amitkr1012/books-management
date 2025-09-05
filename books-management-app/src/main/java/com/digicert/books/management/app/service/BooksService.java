package com.digicert.books.management.app.service;

import com.digicert.openApi.model.service.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BooksService {

    BookDTO findById(Long id);

    PageBook findAll(Pageable pageable);

    BookDTO create(BookRequest request);

    BookDTO replace(Long id, BookRequest request, int version);

    BookDTO updatePartial(Long id, BookUpdateRequest request, int version);

    BookDTO delete(Long id, int version);
}
