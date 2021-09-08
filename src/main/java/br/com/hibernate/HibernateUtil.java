package br.com.hibernate;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class HibernateUtil {

	private EntityManagerFactory factory = null;

	public HibernateUtil() {
		
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("meuprimeiroprojetojsf");
		}
	}

	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		return factory.createEntityManager(); // prover a parte de persistencia
	}

	public Object getPrimaryKey(Object entidade) { // retorna a primary key
		return factory.getPersistenceUnitUtil().getIdentifier(entidade);
	}

}
