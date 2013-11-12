/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {

    private static final AnnotationConfiguration configuration;
    private static final SessionFactory sessionFactory;

    static {
        try {
            configuration = new AnnotationConfiguration();
            sessionFactory = configuration.configure().buildSessionFactory();

        } catch (Throwable ex) {

            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {

        return sessionFactory;
    }

}
