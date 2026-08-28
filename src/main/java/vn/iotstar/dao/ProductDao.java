package vn.iotstar.dao;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JPAConfig;
import vn.iotstar.entity.Product;

public class ProductDao implements IProductDao {

    @Override
    public void insert(Product product) {
        executeInTransaction(entityManager -> entityManager.persist(product));
    }

    @Override
    public void update(Product product) {
        executeInTransaction(entityManager -> entityManager.merge(product));
    }

    @Override
    public void delete(int id) throws Exception {
        EntityManager entityManager = JPAConfig.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Product product = entityManager.find(Product.class, id);
            if (product == null) {
                throw new Exception("Product not found");
            }
            entityManager.remove(product);
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
    public Product findById(int id) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            List<Product> results = entityManager.createQuery(
                            "SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id", Product.class)
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Product findBySlug(String slug) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            List<Product> results = entityManager.createQuery(
                            "SELECT p FROM Product p JOIN FETCH p.category WHERE p.slug = :slug", Product.class)
                    .setParameter("slug", slug)
                    .setMaxResults(1)
                    .getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findByCategoryId(int categoryId) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT p FROM Product p JOIN FETCH p.category WHERE p.category.id = :catId ORDER BY p.id DESC", Product.class)
                    .setParameter("catId", categoryId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findAll() {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.id DESC", Product.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findAll(int page, int pageSize) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = entityManager.createQuery(
                    "SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.id DESC", Product.class);
            return query.setFirstResult(page * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findTop10Newest() {
        return findLatest(10);
    }

    @Override
    public List<Product> findLatest(int limit) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.createdAt DESC, p.id DESC", Product.class)
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> searchByName(String keyword) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT p FROM Product p JOIN FETCH p.category WHERE LOWER(p.name) LIKE LOWER(:keyword) ORDER BY p.id DESC",
                            Product.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findPaginated(int page, int pageSize, Integer categoryId, String keyword, String sortBy) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT p FROM Product p JOIN FETCH p.category WHERE 1=1 ");
            if (categoryId != null && categoryId > 0) {
                jpql.append("AND p.category.id = :catId ");
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                jpql.append("AND LOWER(p.name) LIKE LOWER(:keyword) ");
            }

            if ("price_asc".equalsIgnoreCase(sortBy)) {
                jpql.append("ORDER BY p.price ASC, p.id DESC");
            } else if ("price_desc".equalsIgnoreCase(sortBy)) {
                jpql.append("ORDER BY p.price DESC, p.id DESC");
            } else if ("name_asc".equalsIgnoreCase(sortBy)) {
                jpql.append("ORDER BY p.name ASC");
            } else if ("name_desc".equalsIgnoreCase(sortBy)) {
                jpql.append("ORDER BY p.name DESC");
            } else if ("oldest".equalsIgnoreCase(sortBy)) {
                jpql.append("ORDER BY p.createdAt ASC, p.id ASC");
            } else {
                jpql.append("ORDER BY p.createdAt DESC, p.id DESC");
            }

            TypedQuery<Product> query = entityManager.createQuery(jpql.toString(), Product.class);
            if (categoryId != null && categoryId > 0) {
                query.setParameter("catId", categoryId);
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword.trim() + "%");
            }

            int safePage = Math.max(1, page);
            int safePageSize = Math.max(1, pageSize);
            query.setFirstResult((safePage - 1) * safePageSize);
            query.setMaxResults(safePageSize);

            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public int count() {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery("SELECT COUNT(p) FROM Product p", Long.class)
                    .getSingleResult()
                    .intValue();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public int countFiltered(Integer categoryId, String keyword) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(p) FROM Product p WHERE 1=1 ");
            if (categoryId != null && categoryId > 0) {
                jpql.append("AND p.category.id = :catId ");
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                jpql.append("AND LOWER(p.name) LIKE LOWER(:keyword) ");
            }

            TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
            if (categoryId != null && categoryId > 0) {
                query.setParameter("catId", categoryId);
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword.trim() + "%");
            }

            return query.getSingleResult().intValue();
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
