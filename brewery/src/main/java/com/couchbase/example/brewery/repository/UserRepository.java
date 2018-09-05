package com.couchbase.example.brewery.repository;

import com.couchbase.example.brewery.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

import static com.couchbase.example.brewery.config.Constants.ID_DELIMITER;

/**
 * Spring Data Couchbase repository for the User entity.
 */
@Repository
public interface UserRepository extends N1qlCouchbaseRepository<User, String> {

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByEmailIgnoreCase(String email);

    default Optional<User> findOneByLogin(String login) {
        return findById(User.PREFIX + ID_DELIMITER + login);
    }

    Page<User> findAllByLoginNot(Pageable pageable, String login);
}
