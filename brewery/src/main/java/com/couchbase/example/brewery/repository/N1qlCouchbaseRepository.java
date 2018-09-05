package com.couchbase.example.brewery.repository;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Couchbase specific {@link org.springframework.data.repository.Repository} interface uses N1QL for all requests
 */
@NoRepositoryBean
public interface N1qlCouchbaseRepository<T, ID extends Serializable> extends CouchbasePagingAndSortingRepository<T, ID> {

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter}")
    List<T> findAll();

    @Query("SELECT count(*) FROM #{#n1ql.bucket} WHERE #{#n1ql.filter}")
    long count();

    @Query("DELETE FROM #{#n1ql.bucket} WHERE #{#n1ql.filter} returning #{#n1ql.fields}")
    T removeAll();

    default void deleteAll() {
        removeAll();
    }
}
