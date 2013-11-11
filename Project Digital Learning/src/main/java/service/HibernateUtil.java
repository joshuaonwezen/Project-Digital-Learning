
package service;


import org.hibernate.SessionFactory;
import javax.naming.*;
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