package org.koskos.be;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/// no CDI dummy JPA with tomcat
public class JPAUtil {
    private static final String  PERSISTENCE = "PERSISTENCE";
    private  static EntityManagerFactory entityManagerFactory;

    public static EntityManagerFactory getEntityManagerFactory(){
        if(null == entityManagerFactory){
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE);
        }
        return entityManagerFactory;
    }

    public static void shutDown(){
        if(null != entityManagerFactory)
            entityManagerFactory.close();
    }
}
