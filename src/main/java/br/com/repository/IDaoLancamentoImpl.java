package br.com.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;

@Named
public class IDaoLancamentoImpl implements IDaoLancamento , Serializable{

	@Inject
	private EntityManager entityManager;
	
	@Override
	public List<Lancamento> consultar(Long codUser) {
		
		List<Lancamento> lista = null;
		EntityTransaction transaction = entityManager.getTransaction();
		
		lista =  entityManager.createQuery("from Lancamento where usuario.id = " + codUser).getResultList();
		
		
		return lista;
	}
	
	@Override
	public List<Lancamento> consultarLimite10(Long codUser) {
		
		List<Lancamento> lista = null;
		
		
		lista =  entityManager.createQuery("from Lancamento where usuario.id = " + codUser + "order by id desc")
				.setMaxResults(5)
				.getResultList();
		
		
		return lista;
	}

}
