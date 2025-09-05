package com.digicert.books.management.app.dao;

import com.digicert.books.management.app.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDao extends JpaRepository<Book,Long> {
}
