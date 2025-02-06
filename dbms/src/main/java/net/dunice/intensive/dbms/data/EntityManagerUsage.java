package net.dunice.intensive.dbms.data;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import net.dunice.intensive.dbms.entities.UserEntity;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EntityManagerUsage {
    private final EntityManagerFactory factory;

    @PostConstruct
    public void rollbackTransaction() {
        initUsers();
        initMessages();
        loadMessagesFromSecondLevelCache();
        loadWithLocking();
    }

    private void initUsers() {
        try (var entityManager = factory.createEntityManager()) {
            final var transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.createQuery("insert into UserEntity (username, passwordHash) " +
                                      "values ('john', 'asf1mo3f9qms9cvk1m9020asxf')").executeUpdate();

            //Hibernate JPA annotations (GeneratedValue etc) dont work here
            entityManager.createNativeQuery("""
                    insert into app_users(id, username, password_hash) \
                    values ('%s', 'admin', 'as2412frasifjh21fridjhnasdfias1rhfolqajcfoashf'), \
                    ('%s','avg_user', 'asjok1r109ojasp0913jck1309dfkq0kvf1f0'), \
                    ('%s','loh_user', 'vzxmlkvqeonvqiq2mf2mf2fmq2309rtj23mvx'), \
                    ('%s','svo_zov', 'goida1241foi123cksaq12fjjaslfa231sf12314')"""
                    .formatted(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
            ).executeUpdate();

            //noinspection unchecked
            entityManager
                    .createQuery("select u from UserEntity u where u.username ilike '%a%'")
                    .getResultList()
                    .forEach(System.out::println);

            transaction.commit();
        }
    }

    private void initMessages() {
        try (var entityManager = factory.createEntityManager()) {
            entityManager.getTransaction().begin();

            entityManager.createQuery("""
                    insert into MessagesEntity(message) \
                    values ('the first'), \
                    ('the second')""").executeUpdate();

            entityManager.createNativeQuery("""
                    insert into messages(id, message) \
                    values (3, 'asd'), \
                    (4, 'dsa'), \
                    (5, 'ads')""").executeUpdate();

            //noinspection unchecked
            entityManager
                    .createQuery("select m from MessagesEntity m")
                    .getResultList()
                    .forEach(System.out::println);

            entityManager.getTransaction().commit();
        }
    }

    private void loadMessagesFromSecondLevelCache() {
        try (var entityManager = factory.createEntityManager()) {
            entityManager.getTransaction().begin();

            System.out.println(
                    entityManager
                            .createQuery("select m from MessagesEntity m where m.message ilike '%f_r_t%'")
                            .getSingleResult()
            );

            entityManager.getTransaction().commit();
        }
    }

    private void loadWithLocking() {
        try (var entityManager = factory.createEntityManager()) {
            entityManager.getTransaction().begin();

            final var user = (UserEntity) entityManager
                    .createQuery("select u from UserEntity u where u.username = 'john'")
                    .getSingleResult();

            //No pessimistic lock usage
            entityManager.find(UserEntity.class, user.getId(), LockModeType.NONE);

            //SELECT ... FOR SHARE
            entityManager.find(UserEntity.class, user.getId(), LockModeType.PESSIMISTIC_READ);

            //SELECT ... FOR UPDATE
            entityManager.find(UserEntity.class, user.getId(), LockModeType.PESSIMISTIC_WRITE);

            //Usual optimistic locking, no force increment
            entityManager.find(UserEntity.class, user.getId(), LockModeType.OPTIMISTIC);

            //Force increment optimistic locking
            entityManager.find(UserEntity.class, user.getId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);

            //SELECT ... FOR UPDATE and force increment optimistic counter
            entityManager.find(UserEntity.class, user.getId(), LockModeType.PESSIMISTIC_FORCE_INCREMENT);

            entityManager.getTransaction().commit();
        }
    }
}
