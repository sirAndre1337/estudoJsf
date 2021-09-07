package br.com.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {

	public static EntityManagerFactory factory = null;

	static {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("meuprimeiroprojetojsf");
		}
	}

	public static EntityManager getEntityManager() {
		return factory.createEntityManager(); // prover a parte de persistencia
	}

	public static Object getPrimaryKey(Object entidade) { // retorna a primary key
		return factory.getPersistenceUnitUtil().getIdentifier(entidade);
	}

}
