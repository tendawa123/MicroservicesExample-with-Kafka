package com.cts.books.catalog.service.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.cts.books.catalog.service.customerror.CustomError;
import com.cts.books.catalog.service.kafka.KafkaSender;
import com.cts.books.catalog.service.model.Book;
import com.cts.books.catalog.service.util.BookService;
import com.google.gson.Gson;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Applications;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@SpringBootApplication
@EnableHystrix
@RequestMapping("/api/catalog")
public class BookRestController {
	
	public static final Logger logger = LoggerFactory.getLogger(BookRestController.class);
	private final Gson gson = new Gson();
	@Autowired
	KafkaSender kafkaSender;
	@Autowired
	BookService bookService; // Service which will do all data
	// retrieval/manipulation work.
	@Autowired
	private EurekaClient eurekaClient;
	@GetMapping("/applications")

	public Applications getApplications() {
		String result = gson.toJson(eurekaClient.getApplications());
		kafkaSender.sendTopicFoo(result);
		return eurekaClient.getApplications();
	}

	/**
	 * 
	 * @return gets all Books.
	 */
	@RequestMapping(value = "/books", method = RequestMethod.GET)
	@HystrixCommand(fallbackMethod = "getDataFallBack")
	public ResponseEntity<List<Book>> listAllBooks() {
		List<Book> Books = bookService.findAllBooks();
		if (!Books.isEmpty()) {
			//return new ResponseEntity(new CustomError("No Books available"), HttpStatus.NO_CONTENT);
			 throw new RuntimeException();
		}
		String result = gson.toJson(Books);
		logger.info("data in json :" + result);
		kafkaSender.sendTopicBar(result);
		logger.info("data sent to kafka topic successfully...");
		return new ResponseEntity<List<Book>>(Books, HttpStatus.OK);
	}

	public ResponseEntity<List<Book>> getDataFallBack() {
		logger.info("inside the getDataFallBack method.....");
		return new ResponseEntity(new CustomError("No Books available"), HttpStatus.NO_CONTENT);
	}

	/**
	 * 
	 * @param name
	 * @return gets one Book by name.
	 */
	@RequestMapping(value = "/book/{name}", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> listAllBookByName(@RequestParam(value = "name") String name) {
		logger.info("Fetching Book with name {}", name);
		List<Book> bookList = bookService.findByName(name);
		if (bookList.isEmpty()) {
			logger.error("Book with Name {} not found.", name);
			return new ResponseEntity(new CustomError("Book with Name " + name + " not found"), HttpStatus.NOT_FOUND);
		}
		String result = gson.toJson(bookList);
		kafkaSender.sendTopicFoo(result.toString());
		return new ResponseEntity<List<Book>>(bookList, HttpStatus.OK);
	}

	/**
	 * 
	 * @param name
	 * @return gets book by authorName
	 */
	@RequestMapping(value = "/book/{authorName}", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> listAllBookByAuthorName(@RequestParam(value = "authorName") String name) {
		logger.info("Fetching Book with authorName {}", name);
		List<Book> bookList = bookService.findByAuthorName(name);
		if (bookList.isEmpty()) {
			logger.error("Book with authorName {} not found.", name);
			return new ResponseEntity(new CustomError("Book with authorName " + name + " not found"),
					HttpStatus.NOT_FOUND);
		}
		String result = gson.toJson(bookList);
		kafkaSender.sendTopicFoo(result.toString());
		return new ResponseEntity<List<Book>>(bookList, HttpStatus.OK);
	}

	/**
	 * 
	 * @param id
	 * @return gets one Book by id.
	 */
	@RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> getBookById(@RequestParam(value = "id") long id) {
		logger.info("Fetching Book with id {}", id);
		List<Book> Book = bookService.listFindById(id);
		if (Book.isEmpty()) {
			logger.error("Book with id {} not found.", id);
			return new ResponseEntity(new CustomError("Book with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		String result = gson.toJson(Book);
		kafkaSender.sendTopicFoo(result.toString());
		return new ResponseEntity<List<Book>>(Book, HttpStatus.OK);
	}

	/**
	 * 
	 * @param Book
	 * @param ucBuilder
	 * @return creates one Book.
	 * 
	 */
	@RequestMapping(value = "/book/", method = RequestMethod.POST)
	public ResponseEntity<?> createBook(@RequestBody Book Book, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Book : {}", Book);
		if (bookService.isBookExist(Book)) {
			logger.error("Unable to create. A Book with name {} already exist", Book.getName());
			return new ResponseEntity(
					new CustomError("Unable to create. A Book with name " + Book.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		bookService.saveBook(Book);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/Book/{id}").buildAndExpand(Book.getId()).toUri());
		String result = gson.toJson(Book);
		kafkaSender.sendTopicFoo(result.toString());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	/**
	 * 
	 * @param id
	 * @param Book
	 * @return updates one Book.
	 */
	@RequestMapping(value = "/book/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateBookById(@PathVariable("id") long id, @RequestBody Book Book) {
		logger.info("Updating Book with id {}", id);
		Book currentBook = bookService.findById(id);
		if (currentBook == null) {
			logger.error("Unable to update. Book with id {} not found.", id);
			return new ResponseEntity(new CustomError("Unable to upate. Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		currentBook.setName(Book.getName());
		currentBook.setAuthorName(Book.getAuthorName());
		currentBook.setPrice(Book.getPrice());
		bookService.updateBook(currentBook);
		String result = gson.toJson(currentBook);
		kafkaSender.sendTopicFoo(result.toString());
		return new ResponseEntity<Book>(currentBook, HttpStatus.OK);
	}

	/**
	 * 
	 * @param id
	 * @return deletes one Book.
	 */
	@RequestMapping(value = "/book/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBookById(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Book with id {}", id);
		Book Book = bookService.findById(id);
		if (Book == null) {
			logger.error("Unable to delete. Book with id {} not found.", id);
			return new ResponseEntity(new CustomError("Unable to delete Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		bookService.deleteBookById(id);
		String result = gson.toJson(Book);
		kafkaSender.sendTopicFoo(result.toString());
		return new ResponseEntity<Book>(HttpStatus.NO_CONTENT);
	}

	/**
	 * 
	 * @return deletes all Books.
	 */
	@RequestMapping(value = "/books/", method = RequestMethod.DELETE)
	public ResponseEntity<Book> deleteAllBooks() {
		logger.info("Deleting All Books");
		bookService.deleteAllBooks();
		// String result = gson.toJson(Book);
		// kafkaSender.send(result.toString());
		return new ResponseEntity<Book>(HttpStatus.NO_CONTENT);
	}
}