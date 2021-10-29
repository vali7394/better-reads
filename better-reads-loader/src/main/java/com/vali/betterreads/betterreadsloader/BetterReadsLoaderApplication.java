package com.vali.betterreads.betterreadsloader;

import com.vali.betterreads.betterreadsloader.config.AstraDBConfig;
import com.vali.betterreads.betterreadsloader.domain.Author;
import com.vali.betterreads.betterreadsloader.domain.Book;
import com.vali.betterreads.betterreadsloader.repository.AuthorRepository;
import com.vali.betterreads.betterreadsloader.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.UUID;

@SpringBootApplication
@EnableConfigurationProperties(AstraDBConfig.class)
public class BetterReadsLoaderApplication {

    @Autowired
    AuthorRepository authorRepository;

    public static void main(String[] args) {
        SpringApplication.run(BetterReadsLoaderApplication.class, args);
    }

    /**
     * This is necessary to have the Spring Boot app use the Astra secure bundle
     * to connect to the database
     */
    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(AstraDBConfig astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }


   // @PostConstruct
    public void testRepo(){
        Author author = new Author();
        author.setId(UUID.randomUUID().toString());
        author.setName("Demo");
        Mono<Author> authorMono = authorRepository.save(author);
        authorMono.subscribe();
    }

}
