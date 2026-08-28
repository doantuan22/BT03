package vn.iotstar.dao;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JPAConfig;
import vn.iotstar.entity.Category;

public class CategoryDao implements ICategoryDao {
    @Override
    public void insert(Category category) {
        executeInTransaction(entityManager -> entityManager.persist(category));
    }

    @Override
    public void update(Category category) {
        executeInTransaction(entityManager -> entityManager.merge(category));
    }

    @Override
    public void delete(int id) throws Exception {
        EntityManager entityManager = JPAConfig.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Category category = entityManager.find(Category.class, id);
            if (category == null) {
                throw new Exception("Category not found");
            }
            entityManager.remove(category);
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
    public Category findById(int id) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.find(Category.class, id);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Category findByName(String name) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            List<Category> results = entityManager.createQuery(
                            "SELECT c FROM Category c WHERE c.name = :name", Category.class)
                    .setParameter("name", name)
                    .setMaxResults(1)
                    .getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Category> findAll() {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createNamedQuery("Category.findAll", Category.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Category> findAll(int page, int pageSize) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            TypedQuery<Category> query = entityManager.createNamedQuery("Category.findAll", Category.class);
            return query.setFirstResult(page * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Category> searchByName(String keyword) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(:keyword) ORDER BY c.id",
                            Category.class)
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
            return entityManager.createQuery("SELECT COUNT(c) FROM Category c", Long.class)
                    .getSingleResult()
                    .intValue();
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
