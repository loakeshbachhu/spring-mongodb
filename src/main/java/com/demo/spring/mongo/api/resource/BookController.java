package com.demo.spring.mongo.api.resource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.mongo.api.model.Book;
import com.demo.spring.mongo.api.repository.BookRepository;

@RestController
public class BookController {
	
	@Autowired
	private BookRepository repository;
	
	@PostMapping("/addBook")
	public String saveBook(@RequestBody Book book) {
		repository.save(book);
		return "Added book with Id : "+book.getId();
	}
	
	@GetMapping("/findAllBooks")
	public List<Book> getBooks(){
		return repository.findAll();
	}
	
	@GetMapping("/findBookById/{id}")
	public Optional<Book> getBookById(@PathVariable int id){
		return repository.findById(id);
	}
	
	@DeleteMapping("/delete/{id}")
	public String deletBook(@PathVariable int id) {
		repository.deleteById(id);
		return "Book deleted with Id : " + id;
	}
	
	@GetMapping("/uploadBooks")
	public String uploadBooks() {
		String record = "";
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader("src/main/resources/Books.csv"));
				while((record = bufferedReader.readLine()) != null) {
					String[] data = record.split(",");
					var book = new Book();
					book.setId(Integer.valueOf(data[0]));
					book.setBookName(data[1]);
					book.setAuthorName(data[2]);
					repository.save(book);
				}
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
				return "Books Upload failed. something went wrong.."+ e.getMessage();
			}
			
			return "All Books are Uploaded..";
	}

}
