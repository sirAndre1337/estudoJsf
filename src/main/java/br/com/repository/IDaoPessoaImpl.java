package br.com.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.hibernate.HibernateUtil;

@Named
public class IDaoPessoaImpl implements IDaoPessoa , Serializable{

	@Inject
	private EntityManager entityManager;
	
	@Override
	public Pessoa consultarUsuario(String login, String senha) {
		
		Pessoa pessoa = null;
		
		
		 pessoa = (Pessoa) entityManager.createQuery("select p from Pessoa p where p.login = :login and p.senha = :senha")
		.setParameter("login", login)
		.setParameter("senha", senha).getSingleResult();
		
		return pessoa;
	}

	@Override
	public List<SelectItem> listaEstados() {
		
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		
		List<Estados> estados =  entityManager.createQuery("from Estados").getResultList();
		
		for (Estados estado : estados) {
			selectItems.add(new SelectItem(estado , estado.getNome()));
		}
		
		return selectItems;
	}

	@Override
	public List<SelectItem> listaCidades(Long idEstado) {
		
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		
		List<Cidades> cidades = entityManager.createQuery("from Cidades where estados.id = :id ")
		.setParameter("id", idEstado)
		.getResultList();
		
		for (Cidades cidade : cidades) {
			selectItems.add(new SelectItem(cidade , cidade.getNome()));
		}
		
		return selectItems;
	}
	
	

}
