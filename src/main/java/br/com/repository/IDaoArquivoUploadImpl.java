package br.com.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.ArquivoUpload;

@Named
public class IDaoArquivoUploadImpl implements IDaoArquivoUpload , Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	@Override
	public void salvar(ArquivoUpload arquivoUpload) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(arquivoUpload);
		transaction.commit();
	}

}
