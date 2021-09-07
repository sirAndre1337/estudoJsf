package br.com.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.hibernate.HibernateUtil;

public class DaoGeneric<E> {

	private EntityManager entityManager = HibernateUtil.getEntityManager();

	public void salvar(E entidade) {

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}

	public E consultarPorId(Class<E> entidade, Long id) {

		E e = (E) entityManager.find(entidade, id);

		return e;

	}

	public E atualizarOuSalva(E entidade) {

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadSalva = entityManager.merge(entidade);
		transaction.commit();

		return entidadSalva;

	}

	public void deletarPorid(E entidade) {

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.remove(entidade);
		transaction.commit();

	}

	public void deletarPoridComQuery(E entidade) {

		Object id = HibernateUtil.getPrimaryKey(entidade);

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager
				.createNativeQuery(
						"delete from " + entidade.getClass().getSimpleName().toLowerCase() + " where id = " + id)
				.executeUpdate();
		transaction.commit();
	}

	public List<E> buscaTodos(Class<E> entidade) {


		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = (List<E>) entityManager.createQuery("from " + entidade.getName()).getResultList();
		transaction.commit();

		return lista;

	}
	

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
}
