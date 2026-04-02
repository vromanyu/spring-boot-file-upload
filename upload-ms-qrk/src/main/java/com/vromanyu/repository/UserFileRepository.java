package com.vromanyu.repository;

import com.vromanyu.entity.UserFile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserFileRepository {

    @Inject
    EntityManager entityManager;

    public void save(UserFile userFile) {
        entityManager.persist(userFile);
    }

    public void delete(UserFile userFile) {
        entityManager.remove(userFile);
    }

    public UserFile update(UserFile userFile) {
        return entityManager.merge(userFile);
    }

    public Optional<UserFile> findByFileUuid(String fileUuid) {
        return Optional.ofNullable(entityManager.find(UserFile.class, fileUuid));
    }

    public Set<UserFile> findAllByUserName(String userName) {
        TypedQuery<UserFile> query = entityManager.createQuery("select u From UserFile u where u.userName = :userName", UserFile.class);
        query.setParameter("userName", userName);
        return query.getResultStream().collect(Collectors.toSet());
    }

    public Set<UserFile> findAllCreatedFiles() {
        TypedQuery<UserFile> query = entityManager.createQuery("select u From UserFile u where u.status = UploadStatus.CREATED", UserFile.class);
        return query.getResultStream().collect(Collectors.toSet());
    }
}
