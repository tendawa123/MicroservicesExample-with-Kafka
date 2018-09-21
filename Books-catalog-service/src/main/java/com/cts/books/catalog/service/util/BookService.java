package com.cts.books.catalog.service.util;

import java.util.List;

import com.cts.books.catalog.service.model.Book;

public interface BookService {

	List<Book> listFindById(long id);
	Book findById(long id);
	List<Book> findByName(String name);

	List<Book> findByAuthorName(String name);

	void saveBook(Book book);

	void updateBook(Book book);

	void deleteBookById(long id);

	List<Book> findAllBooks();

	void deleteAllBooks();

	boolean isBookExist(Book book);

}
