package br.com.cursojsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.dao.DaoGeneric;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoLancamentoImpl;
import br.com.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "lancamentoBean")
public class LancamentoBean {

	private Lancamento lancamento = new Lancamento();
	private DaoGeneric<Lancamento> dao = new DaoGeneric<Lancamento>();
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();
	IDaoLancamentoImpl daoLancamentoImpl = new IDaoLancamentoImpl();

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
