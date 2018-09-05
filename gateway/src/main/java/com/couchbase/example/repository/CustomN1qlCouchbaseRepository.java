package com.couchbase.example.repository;

import org.springframework.data.couchbase.core.CouchbaseOperations;
import org.springframework.data.couchbase.core.mapping.CouchbasePersistentEntity;
import org.springframework.data.couchbase.repository.query.CouchbaseEntityInformation;
import org.springframework.data.couchbase.repository.support.N1qlCouchbaseRepository;

import java.io.Serializable;

/**
 * A custom implementation of CouchbaseRepository .
 */
public class CustomN1qlCouchbaseRepository<T, ID extends Serializable> extends N1qlCouchbaseRepository<T, ID> {

    private final CouchbasePersistentEntity<?> persistentEntity;

    /**
     * Create a new Repository.
     *
     * @param metadata            the Metadata for the entity.
     * @param couchbaseOperations the reference to the template used.
     */
    public CustomN1qlCouchbaseRepository(CouchbaseEntityInformation<T, String> metadata, CouchbaseOperations couchbaseOperations) {
        super(metadata, couchbaseOperations);
        persistentEntity = getCouchbaseOperations().getConverter().getMappingContext().getPersistentEntity(getEntityInformation().getJavaType());
    }

    @Override
    public <S extends T> S save(S entity) {
        return super.save(populateIdIfNecessary(entity));
    }

    /**
     * Add generated ID to entity if not already set
     *
     * @param entity
     * @return entity with ID set
     */
    private <S extends T> S populateIdIfNecessary(S entity) {
        if (getEntityInformation().getId(entity) != null) {
            return entity;
        }
        setId(entity, getCouchbaseOperations().getGeneratedId(entity));
        return entity;
    }

    private <S extends T> void setId(S entity, String generatedId) {
        persistentEntity.getPropertyAccessor(entity).setProperty(persistentEntity.getIdProperty(), generatedId);
    }
}
