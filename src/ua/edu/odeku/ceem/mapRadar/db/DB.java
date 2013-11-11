package ua.edu.odeku.ceem.mapRadar.db;

import org.hibernate.Session;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

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

    public static Session getSession(){
        return getEntityManager().unwrap(Session.class);
    }

    public static void closeSession(Session session){
        if (session != null && session.isOpen()){
            session.close();
        }
    }

    public static final StringType STRING_TYPE = StringType.INSTANCE;
    public static final DoubleType DOUBLE_TYPE = DoubleType.INSTANCE;
    public static final IntegerType INTEGER_TYPE = IntegerType.INSTANCE;
    public static final LongType LONG_TYPE = LongType.INSTANCE;
}
