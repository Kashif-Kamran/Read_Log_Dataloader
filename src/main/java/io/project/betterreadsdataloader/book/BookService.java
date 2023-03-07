package io.project.betterreadsdataloader.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService
{
    BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository)
    {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book newBook)
    {
        this.bookRepository.save(newBook);
    }
}
