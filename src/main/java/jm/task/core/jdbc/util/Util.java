package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/baledbtest";
    private static final String DB_USERNAME = "rootroot";
    private static final String DB_PASSWORD = "rootroot";
    private static final String DIALECT = "org.hibernate.dialect.MySQLDialect";

    private static org.hibernate.SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties properties = new Properties();

                properties.put(Environment.DRIVER, DRIVER);
                properties.put(Environment.URL, DB_URL);
                properties.put(Environment.USER, DB_USERNAME);
                properties.put(Environment.PASS, DB_PASSWORD);
                properties.put(Environment.DIALECT, DIALECT);
                properties.put(Environment.SHOW_SQL, "true");
                properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                configuration.setProperties(properties);
                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                System.out.println("Connected!");

            } catch (HibernateException e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_PASSWORD, DB_USERNAME);
            if (!connection.isClosed()) {
                System.out.println("Connected!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
