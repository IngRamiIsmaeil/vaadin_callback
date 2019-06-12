package org.koskos.be;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Logger;

public class JPAMySql {

    private EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

    private final Logger LOG = Logger.getLogger(JPAUtil.class.getSimpleName());

    private static JPAMySql ourInstance = new JPAMySql();

    public static JPAMySql getInstance() {
        return ourInstance;
    }

    private JPAMySql() {
        entityManager.getTransaction().begin();
        final String testEM = (String) entityManager.createNativeQuery("select version()").getSingleResult();
        LOG.info("Test Jpa Initializierung -> MYSQL Version is " + testEM);
        entityManager.getTransaction().commit();
    }

    public List<DB_Product_Entity> getAllProductList(int offset, int limit){
        LOG.info("getAllProductList offset {"+offset+"}" +" limit {"+limit+"}");
        return entityManager.createNamedQuery(DB_Product_Entity.SELECT_ALL_PRODUCT).setFirstResult(offset).setMaxResults(offset+limit).getResultList();
    }

    public int getCountProducts(){
        LOG.info("getCountProductsById initial");
        return ((Long) entityManager.createNamedQuery(DB_Product_Entity.SELECT_COUNT_PRODUCT).getSingleResult()).intValue();
    }

    public List<DB_Product_Entity> getAllProductsListById(int id, int offset, int limit){
        LOG.info("getAllProductList id {"+id+"} offset {"+offset+"}" +" limit {"+limit+"}");
        return entityManager.createNamedQuery(DB_Product_Entity.SELECT_ALL_PRODUCT_BY_ID).setParameter("id", id).setFirstResult(offset).setMaxResults(offset+limit).getResultList();
    }

    public int getCountProductsById(int id){
        LOG.info("getCountProductsById id {"+id+"}");
        return ((Long) entityManager.createNamedQuery(DB_Product_Entity.SELECT_COUNT_PRODUCT_BY_ID).setParameter("id", id).getSingleResult()).intValue();
    }
    @Override
    protected void finalize() throws Throwable {
        JPAUtil.shutDown();
    }
}
