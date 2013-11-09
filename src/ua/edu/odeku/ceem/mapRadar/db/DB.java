package ua.edu.odeku.ceem.mapRadar.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Класс инкапсулирует работу с базой данных
 * User: Aleo skype: aleo72
 * Date: 09.11.13
 * Time: 20:25
 */
public final class DB {

    private static EntityManagerFactory entityManagerFactory = null;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("db_h2");
    }

    public static EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }

}
