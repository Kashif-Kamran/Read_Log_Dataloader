package io.project.betterreadsdataloader;

import io.project.betterreadsdataloader.author.Author;
import io.project.betterreadsdataloader.author.AuthorService;
import io.project.betterreadsdataloader.book.Book;
import io.project.betterreadsdataloader.book.BookService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class InitilizationService
{
    @Value("${datadump.location.author}")
    private String authorDumpLocation;

    @Value("${datadump.location.works}")
    private String bookDumpLocation;

    @Autowired
    AuthorService authorService;
    @Autowired
    BookService bookService;

    public void initAuthorsFromFile(String filePath)
    {
        Path path = Path.of(filePath);
        try
        {
            Stream<String> lines = Files.lines(path);
            lines.forEach(line ->
            {
                String jsonString = line.substring(line.indexOf("{"));
                JSONParser jsonParser = new JSONParser();
                try
                {
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
                    Author newAuthor = new Author();
                    newAuthor.setId(((String) jsonObject.getOrDefault("key", null)).replace("/authors/", ""));
                    newAuthor.setName((String) jsonObject.getOrDefault("name", null));
                    newAuthor.setPersonalName((String) jsonObject.getOrDefault("personal_name", null));
                    this.authorService.addAuthor(newAuthor);
                    System.out.println("Saving Author : " + newAuthor.getName());
                } catch (ParseException e)
                {
                    System.out.println("Unable TO parse Author Json Object");
                    e.printStackTrace();
                }
            });

        } catch (IOException exc)
        {
            System.out.println("Author File Reading Exception Found");
            exc.printStackTrace();
        }

    }

    public void initBookFromFile(String filePath)
    {
        Path path = Path.of(filePath);
        try
        {
            Stream<String> lines = Files.lines(path);
            lines.forEach(line ->
            {
//                Read The Json File and Parse it To JSON Object
                String jsonString = line.substring(line.indexOf("{"));
                JSONParser jsonParser = new JSONParser();
                try
                {
                    Book newBook = new Book();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);

//                    Getting Book Id
                    String bookId = ((String) jsonObject.getOrDefault("key", null)).replace("/works/", "");
                    newBook.setId(bookId);
//                    Getting Book Title
                    String bookTitle = (String) jsonObject.getOrDefault("title", null);
                    newBook.setName(bookTitle);
//                  Getting Description

                    String desciption = null;
                    JSONObject desciptionJson = (JSONObject) jsonObject.get("description");
                    if (desciptionJson != null)
                    {
                        desciption = (String) desciptionJson.get("value");
                    }
                    newBook.setDescription(desciption);
//                  Getting Published Date
                    LocalDate publishedDate = null;
                    JSONObject publishedDateJson = (JSONObject) jsonObject.get("created");
                    if (publishedDateJson != null)
                    {
                        String tempString = ((String) publishedDateJson.get("value"));
                        String pubDateString = tempString.substring(0, tempString.indexOf("T"));
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        publishedDate = LocalDate.parse(pubDateString, formatter);
                    }

                    newBook.setPublished_date(publishedDate);
//                    Getting Cover IDs
                    JSONArray covJson = (JSONArray) jsonObject.get("covers");
                    List<String> covIds = new ArrayList<>();
                    if (covJson != null)
                    {
                        covJson.forEach(ele ->
                        {
                            covIds.add(Long.toString((Long) ele));
                        });
                    }
                    newBook.setCoversIds(covIds);
//                    Get Author Ids
                    List<String> authorsIds = new ArrayList<>();
                    JSONArray authorsArray = (JSONArray) jsonObject.get("authors");

                    if (authorsArray != null)
                    {
                        authorsArray.forEach(ele ->
                        {
                            String authorId = ((String) ((JSONObject) ((JSONObject) ele).get("author")).get("key")).replace("/authors/", "");
                            authorsIds.add(authorId);
                        });
                    }
                    newBook.setAuthorsIds(authorsIds);
//                    Get Author Names
                    List<String> authorsNames = new ArrayList<>();
                    authorsIds.forEach(ele ->
                    {
                        Author author = this.authorService.getAuthorById(ele);
                        if (author != null)
                        {
                            authorsNames.add(author.getName());
                            newBook.setAuthorsNames(authorsNames);
                        } else
                        {
                            authorsNames.add("Unknown Author");
                        }
                    });
                    newBook.setAuthorsNames(authorsNames);
                    System.out.println(newBook);
                    System.out.println("          Saving .........      ");
                    this.bookService.addBook(newBook);
//                    System.out.println("Book Saving : " + newBook.getName());
                } catch (ParseException e)
                {
                    System.out.println("Unable to Get Works JSON Object ");
                    e.printStackTrace();
                }
            });
        } catch (Exception exc)
        {
            System.out.println("Works File Reading Exception");
            exc.printStackTrace();
        }
    }

    @PostConstruct
    public void start()
    {
        this.initAuthorsFromFile(authorDumpLocation);
        this.initBookFromFile(bookDumpLocation);
    }
}
