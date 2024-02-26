package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;


public class UserDaoHibernateImpl extends Util implements UserDao {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Users (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "name VARCHAR(255) NOT NULL," +
            "lastName VARCHAR(255) NOT NULL," +
            "age INT NOT NULL" +
            ")";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS Users";
    private static final String CLEAR = "TRUNCATE TABLE Users";
    private Transaction transaction = null;
    private static final SessionFactory FACTORY = getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = FACTORY.openSession()) {
            transaction = session.beginTransaction();

            session.createSQLQuery(CREATE_TABLE)
                    .executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            e.getStackTrace();
            transaction.rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = FACTORY.openSession()) {
            transaction = session.beginTransaction();

            session.createSQLQuery(DROP_TABLE)
                    .executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            e.getStackTrace();
            transaction.rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = FACTORY.openSession()) {
            transaction = session.beginTransaction();

            session.save(new User(name, lastName, age));

            transaction.commit();
            System.out.println("User " + name + " saved.");
        } catch (HibernateException e) {
            e.getStackTrace();
            transaction.rollback();

        }

    }

    @Override
    public void removeUserById(long id) {
        try (Session session = FACTORY.openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            session.delete(user);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.getStackTrace();
        }

    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        try (Session session = FACTORY.openSession()) {
            transaction = session.beginTransaction();

            users = session.createQuery("FROM User", User.class)
                    .getResultList();

            transaction.commit();
        } catch (HibernateException e) {
            System.err.println(" FAIL to get all users.");
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = FACTORY.openSession()) {
            transaction = session.beginTransaction();

            session.createSQLQuery(CLEAR)
                    .executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            e.getStackTrace();
            transaction.rollback();

        }
    }
}
