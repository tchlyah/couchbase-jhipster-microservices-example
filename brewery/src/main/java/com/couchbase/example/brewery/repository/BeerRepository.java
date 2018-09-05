package com.couchbase.example.brewery.repository;

import com.couchbase.example.brewery.domain.Beer;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Couchbase repository for the Beer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeerRepository extends N1qlCouchbaseRepository<Beer, String> {

}
