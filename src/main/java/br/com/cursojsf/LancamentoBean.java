package br.com.cursojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DaoGeneric;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoLancamento;

@ViewScoped
@Named(value = "lancamentoBean")
public class LancamentoBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private Lancamento lancamento = new Lancamento();
	
	@Inject
	private DaoGeneric<Lancamento> dao;
	
	@Inject
	IDaoLancamento daoLancamentoImpl;
	
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();

	public String salvar() {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaConsultado = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		lancamento.setUsuario(pessoaConsultado);
		lancamento = dao.atualizarOuSalva(lancamento);
		carregarLancamentos();

		return "";
	}

	public String novo() {
		lancamento = new Lancamento();
		return "";
	}

	public String excluir() {
		dao.deletarPoridComQuery(lancamento);
		lancamento = new Lancamento();
		carregarLancamentos();
		return "";
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	@PostConstruct
	public void carregarLancamentos() {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaConsultado = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		

		lancamentos = daoLancamentoImpl.consultar(pessoaConsultado.getId());
		
	}

	public List<Lancamento> getLancamentos() {

		return this.lancamentos;
	}
	
}
