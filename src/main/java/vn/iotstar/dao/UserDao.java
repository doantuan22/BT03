package vn.iotstar.dao;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JPAConfig;
import vn.iotstar.entity.User;

public class UserDao implements IUserDao {
    @Override
    public void insert(User user) {
        executeInTransaction(entityManager -> entityManager.persist(user));
    }

    @Override
    public void update(User user) {
        executeInTransaction(entityManager -> entityManager.merge(user));
    }

    @Override
    public void delete(int id) throws Exception {
        EntityManager entityManager = JPAConfig.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            User user = entityManager.find(User.class, id);
            if (user == null) {
                throw new Exception("User not found");
            }
            entityManager.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User findById(int id) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.find(User.class, id);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User findByUsername(String username) {
        return findOne("SELECT u FROM User u WHERE u.username = :value", username);
    }

    @Override
    public User findByEmail(String email) {
        return findOne("SELECT u FROM User u WHERE u.email = :value", email);
    }

    @Override
    public User findByPhone(String phone) {
        return findOne("SELECT u FROM User u WHERE u.phone = :value", phone);
    }

    @Override
    public List<User> findAll() {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createNamedQuery("User.findAll", User.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<User> findAll(int page, int pageSize) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            TypedQuery<User> query = entityManager.createNamedQuery("User.findAll", User.class);
            return query.setFirstResult(page * pageSize).setMaxResults(pageSize).getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<User> searchByKeyword(String keyword) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery(
                    "SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(:keyword) "
                            + "OR LOWER(u.fullName) LIKE LOWER(:keyword) "
                            + "OR LOWER(u.email) LIKE LOWER(:keyword) ORDER BY u.id DESC",
                    User.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public int count() {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class)
                    .getSingleResult()
                    .intValue();
        } finally {
            entityManager.close();
        }
    }

    private User findOne(String jpql, String value) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            List<User> results = entityManager.createQuery(jpql, User.class)
                    .setParameter("value", value)
                    .setMaxResults(1)
                    .getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            entityManager.close();
        }
    }

    private void executeInTransaction(EntityManagerOperation operation) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            operation.execute(entityManager);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @FunctionalInterface
    private interface EntityManagerOperation {
        void execute(EntityManager entityManager);
    }
}
