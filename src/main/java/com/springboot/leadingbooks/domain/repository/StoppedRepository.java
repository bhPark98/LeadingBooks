package com.springboot.leadingbooks.domain.repository;

import com.springboot.leadingbooks.domain.entity.Stopped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoppedRepository {
    private final EntityManager em;

    public void save(Stopped stopped) {
        em.persist(stopped);
    }

    public void update(Stopped stopped) {
        em.merge(stopped);
    }

    public void delete(Stopped stopped) {
        em.remove(stopped);
    }

    public Optional<Stopped> findById(Long sId) {
        return em.createQuery("select s from Stopped s where s.id =:id", Stopped.class)
                .setParameter("id", sId)
                .getResultStream().findAny();
    }
}
