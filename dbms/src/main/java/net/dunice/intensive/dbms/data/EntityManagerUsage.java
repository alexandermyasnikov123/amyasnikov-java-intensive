package net.dunice.intensive.dbms.data;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EntityManagerUsage {
    private final EntityManagerFactory factory;

    @PostConstruct
    public void rollbackTransaction() {
        try (var entityManager = factory.createEntityManager()) {
            final var transaction = entityManager.getTransaction();
            transaction.begin();

            //Can't execute insert or update using jpql/hql. Only native queries are allowed here.
            @SuppressWarnings("CheckStyle") final var insertQuery = entityManager.createNativeQuery(("""
                            insert into app_users(id, username, password_hash) \
                            values ('%s', 'admin', 'as2412frasifjh21fridjhnasdfias1rhfolqajcfoashf'), \
                            ('%s','avg_user', 'asjok1r109ojasp0913jck1309dfkq0kvf1f0'), \
                            ('%s','loh_user', 'vzxmlkvqeonvqiq2mf2mf2fmq2309rtj23mvx'), \
                            ('%s','svo_zov', 'goida1241foi123cksaq12fjjaslfa231sf12314')"""
                    ).formatted(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
            ).executeUpdate();

            //noinspection unchecked
            entityManager
                    .createQuery("select u from UserEntity u where u.username ilike '%a%'")
                    .getResultList()
                    .forEach(System.out::println);

            transaction.rollback();
        }
    }
}
