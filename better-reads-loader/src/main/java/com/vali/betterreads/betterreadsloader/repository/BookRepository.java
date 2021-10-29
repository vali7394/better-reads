package com.vali.betterreads.betterreadsloader.repository;

import com.vali.betterreads.betterreadsloader.domain.Book;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ReactiveCassandraRepository<Book,String> {
}
