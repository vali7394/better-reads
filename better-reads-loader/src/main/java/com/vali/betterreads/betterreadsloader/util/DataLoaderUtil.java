package com.vali.betterreads.betterreadsloader.util;

import com.vali.betterreads.betterreadsloader.domain.Author;
import com.vali.betterreads.betterreadsloader.domain.Book;
import com.vali.betterreads.betterreadsloader.repository.AuthorRepository;
import com.vali.betterreads.betterreadsloader.repository.BookRepository;
import io.netty.util.internal.StringUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataLoaderUtil {

    AuthorRepository authorRepository;
    BookRepository bookRepository;

    @Value("${datadump.location.authors}")
    private String authDataDumpPath;

    @Value("${datadump.location.works}")
    private String workDataDumpPath;

    public DataLoaderUtil(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void loadAppData() {
       //  loadAuthorData();
        loadWorkData();
    }

    private void loadAuthorData() {
        Path path = Paths.get(authDataDumpPath);
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(line.substring(line.indexOf("{")));
                    Author author = new Author();
                    author.setId(jsonObject.optString("key").replace("/authors/", ""));
                    author.setName(jsonObject.optString("name"));
                    author.setPersonalName(jsonObject.optString("personal_name"));
                    authorRepository.save(author).subscribe();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private void loadWorkData() {
        Path path = Paths.get(workDataDumpPath);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(line->{
                try {
                    JSONObject jsonObject = new JSONObject(line.substring(line.indexOf("{")));
                    Book book = new Book();
                    book.setId(jsonObject.optString("key").replace("/works/",""));
                    book.setName(jsonObject.optString("title"));
                    JSONObject description = jsonObject.optJSONObject("description");
                    if(description!=null){
                        book.setBookDescription(description.optString("value"));
                    }
                    JSONObject pubDate = jsonObject.optJSONObject("created");
                    if(pubDate!=null){
                        book.setPublishedDate(LocalDate.parse(pubDate.optString("value"),dateTimeFormatter));
                    }

                    JSONArray coverIdArray = jsonObject.optJSONArray("covers");
                    if(coverIdArray!=null){
                        List<String> coverIds = new ArrayList<>();
                        for(int i=0; i<coverIdArray.length() ; i++){
                            coverIds.add(coverIdArray.getString(i));
                        }
                        book.setCoverIds(coverIds);
                    }

                    JSONArray authorArray = jsonObject.optJSONArray("authors");
                    if(authorArray!=null) {
                        List<String> authorIds = new ArrayList<>();
                        for(int i=0; i<authorArray.length(); i++){
                            authorIds.add(authorArray.getJSONObject(i).getJSONObject("author").optString("key").replace("/authors/",""));
                        }
                        book.setAuthorIds(authorIds);
                        book.setAuthorNames(authorIds.stream().map(authorId->{
                                    Author author = authorRepository.findById(authorId)
                                            .block();
                            return author!=null && StringUtil.isNullOrEmpty(author.getName())? author.getName() : "Unknown Author";
                        }).collect(Collectors.toList()));
                    }
                    System.out.println("Book saved" + book.getName());
                    bookRepository.save(book).subscribe();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });


        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

}