package com.couchbase.example.brewery.repository;

import com.couchbase.example.brewery.domain.Brewery;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Couchbase repository for the Brewery entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BreweryRepository extends N1qlCouchbaseRepository<Brewery, String> {

}
