package com.vali.betterreads.betterreadsloader.repository;

import com.vali.betterreads.betterreadsloader.domain.Author;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends ReactiveCassandraRepository<Author,String> {

}
