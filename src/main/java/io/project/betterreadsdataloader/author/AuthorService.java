package io.project.betterreadsdataloader.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService
{
    AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository)
    {
        this.authorRepository = authorRepository;
    }

    public void addAuthor(Author author)
    {
        this.authorRepository.save(author);
    }

    public Author getAuthorById(String authorId)
    {
        Optional<Author> author = authorRepository.findById(authorId);
        return author.orElse(null);
    }

}
