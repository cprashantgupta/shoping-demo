package com.hypretail.vcart.repository;

import com.hypretail.vcart.domain.User;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Micronaut Data JPA repository for the {@link User} entity.
 */
@Repository
public abstract class UserRepository implements JpaRepository<User, String> {

    private EntityManager entityManager;

    public static String USERS_BY_LOGIN_CACHE = "usersByLogin";

    public static String USERS_BY_EMAIL_CACHE = "usersByEmail";
    

    public abstract Optional<User> findOneByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "authorities")
    public abstract Optional<User> findOneById(String id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = "usersByLogin")
    public abstract Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = "usersByEmail")
    public abstract Optional<User> findOneByEmail(String email);

    public abstract Page<User> findAllByLoginNot(String login, Pageable pageable);

    public abstract void update(@Id String id, Instant createdDate);

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public User mergeAndSave(User user) {
        user = entityManager.merge(user);
        return save(user);
    }
}
