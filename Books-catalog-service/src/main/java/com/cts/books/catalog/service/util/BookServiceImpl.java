package com.cts.books.catalog.service.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cts.books.catalog.service.model.Book;
@Service("bookService")
public class BookServiceImpl implements BookService {
	public static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
	private static final AtomicLong counter = new AtomicLong();
	private static List<Book> books;
	static {
		books = populateDummyBooks();
	}
	
	public List<Book> findAllBooks() {
		return books;
	}

   public Book findById(long id) {
		for (Book book : books) {
			if (book.getId() == id) {
				return book;
			}
		}
		return null;
	}
	public List<Book> listFindById(long id) {
		List<Book> bookList = new ArrayList<Book>();
		for (Book book : books) {
			Long books=new Long(book.getId());
			Long bookId=new Long(id);
			if (books.equals(bookId)) {
				bookList.add(book);
			}
		}
		return bookList;
	}
	public List<Book> findByName(String name) {
		List<Book> bookList = new ArrayList<Book>();
		for (Book book : books) {
			if (book.getName().equalsIgnoreCase(name)) {
				bookList.add(book);
			}
		}
		return bookList;
	}

	@Override
	public List<Book> findByAuthorName(String name) {
		List<Book> bookList = new ArrayList<Book>();
		for (Book book : books) {
			if (book.getAuthorName().equalsIgnoreCase(name)) {
				bookList.add(book);
			}
		}
		return bookList;
	}

	public void saveBook(Book book) {
		book.setId(counter.incrementAndGet());
		books.add(book);
	}

	public void updateBook(Book book) {
		int index = books.indexOf(book);
		books.set(index, book);
	}

	public void deleteBookById(long id) {
		for (Iterator<Book> iterator = books.iterator(); iterator.hasNext();) {
			Book book = iterator.next();
			if (book.getId() == id) {
				iterator.remove();
			}
		}
	}

	public boolean isBookExist(Book book) {
		return findByName(book.getName()) != null;
	}

	public void deleteAllBooks() {
		books.clear();
	}

	private static List<Book> populateDummyBooks() {
		List<Book> books = new ArrayList<Book>();
		books.add(new Book(counter.incrementAndGet(), "C++", "Balaguruswammy", 400));
		books.add(new Book(counter.incrementAndGet(), "Java", "Cathy Sierra", 900));
		books.add(new Book(counter.incrementAndGet(), "Java", "John Sierra", 900));
		books.add(new Book(counter.incrementAndGet(), "C", "Krishna", 2000));
		books.add(new Book(counter.incrementAndGet(), "C", "Krishna Nagar", 2000));
		books.add(new Book(counter.incrementAndGet(), "Machine Learning", "Simond", 2000));
		books.add(new Book(counter.incrementAndGet(), "BigData", "Simond", 2000));
		books.add(new Book(counter.incrementAndGet(), "Data Science", "Richmond", 4000));
		books.add(new Book(counter.incrementAndGet(), "Internet Of Things", "Drew Jordan", 5000));
		books.add(new Book(counter.incrementAndGet(), "Internet Of Things", "Mike", 4500));
		return books;
	}
}
