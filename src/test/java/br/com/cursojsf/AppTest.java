package br.com.cursojsf;

import java.util.List;

import javax.faces.model.SelectItem;

import org.junit.Test;

import br.com.dao.DaoGeneric;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.entidades.TelefoneUser;
import br.com.hibernate.HibernateUtil;
import br.com.repository.IDaoPessoaImpl;

public class AppTest {

	@Test
	public void testeHibernateUtil() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();
		Pessoa usuario = new Pessoa();
		usuario.setIdade(26);
		usuario.setNome("André Luis");
		usuario.setSobrenome("Lacerda");

		dao.salvar(usuario);

	}

	@Test
	public void testaBuscarPorId() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();

		Pessoa usuario = dao.consultarPorId(Pessoa.class, 1L);

		if (usuario == null) {
			System.out.println("Nenhum usuario para esse id");
		} else {

			System.out.println(usuario);
		}

	}

	@Test
	public void testaAtualizarOuSalva() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();

		Pessoa usuario = new Pessoa();
		usuario.setId(8L);
		usuario.setIdade(30);
		usuario.setNome("carlos");
		usuario.setSobrenome("Lacerda");

		Pessoa objetoSalvoUatualizado = dao.atualizarOuSalva(usuario);

		System.out.println(objetoSalvoUatualizado);
	}

	@Test
	public void testaExcluir() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();

		Pessoa usuario = dao.consultarPorId(Pessoa.class, 4L);

		dao.deletarPorid(usuario);

	}

	@Test
	public void testaExcluirComQueryNative() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();

		Pessoa usuario = dao.consultarPorId(Pessoa.class, 2L);

		dao.deletarPoridComQuery(usuario);

	}

	@Test
	public void testaBuscarTodos() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();

		List<Pessoa> lista = dao.buscaTodos(Pessoa.class);

		for (Pessoa usuarioPessoa : lista) {
			System.out.println(usuarioPessoa);
		}

	}

	@Test
	public void testaQueryList() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();

		List<Pessoa> lista = dao.getEntityManager().createQuery("from UsuarioPessoa").getResultList();

		for (Pessoa usuarioPessoa : lista) {
			System.out.println(usuarioPessoa);
		}

	}

	@Test
	public void testaQueryListMaxResult() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();

		List<Pessoa> list = dao.getEntityManager().createQuery("from UsuarioPessoa order by id").setMaxResults(3)
				.getResultList();

		for (Pessoa p : list) {
			System.out.println(p);
		}

	}

	@Test
	public void testaQueryListParameter() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();

		List<Pessoa> lista = dao.getEntityManager()
				.createQuery("from UsuarioPessoa where nome = :nome and sobrenome = :sobrenome")
				.setParameter("nome", "André Luis").setParameter("sobrenome", "Lacerda").getResultList();

		System.out.println(lista);

	}

	@Test
	public void testaQuerySomaIdade() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();

		Double somaIdade = (Double) dao.getEntityManager().createQuery("select avg(u.idade) from UsuarioPessoa u")
				.getSingleResult();

		System.out.println(somaIdade);
	}

	@Test
	public void testaNamedQuery() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();

		List<Pessoa> lista = dao.getEntityManager().createNamedQuery("UsuarioPessoa.findAll").getResultList();

		Pessoa usuario = (Pessoa) dao.getEntityManager().createNamedQuery("UsuarioPessoa.buscaPorNome")
				.setParameter("nome", "carlos").getSingleResult();

		System.out.println("Usuario buscado : " + usuario);
		System.out.println("-----------------");

		for (Pessoa usuarioPessoa : lista) {

			System.out.println(usuarioPessoa);
		}

	}

	@Test
	public void testaNamedQuery2() {

		DaoGeneric<Pessoa> dao = new DaoGeneric<Pessoa>();

		Pessoa usuario = (Pessoa) dao.getEntityManager().createNamedQuery("UsuarioPessoa.buscaPorNome")
				.setParameter("nome", "roberto").getSingleResult();

		System.out.println("Usuario buscado : " + usuario);

	}

	@Test
	public void testaSalvarComTelefones() {

		DaoGeneric dao = new DaoGeneric();

		Pessoa usuario = (Pessoa) dao.consultarPorId(Pessoa.class, 1L);

		TelefoneUser tel1 = new TelefoneUser();
		tel1.setNumero("35624888");
		tel1.setTipo("casa");
		tel1.setUsuarioPessoa(usuario);

		dao.salvar(tel1);

	}

	@Test
	public void testaBuscarTelefones() {

		DaoGeneric dao = new DaoGeneric();

		Pessoa pessoa = (Pessoa) dao.consultarPorId(Pessoa.class, 22L);

		System.out.println(pessoa);

	}

	@Test
	public void testaSalvarUsuarioComTel() {

		DaoGeneric dao = new DaoGeneric();

		Pessoa usuario = new Pessoa();
		usuario.setIdade(18);
		usuario.setNome("fernando");
		usuario.setSobrenome("Lacerda");

		Pessoa usuarioSalvo = (Pessoa) dao.atualizarOuSalva(usuario);

		TelefoneUser tel1 = new TelefoneUser();
		tel1.setNumero("333333333");
		tel1.setTipo("casa");
		tel1.setUsuarioPessoa(usuarioSalvo);

		TelefoneUser tel2 = new TelefoneUser();
		tel2.setNumero("999999999");
		tel2.setTipo("celular");
		tel2.setUsuarioPessoa(usuarioSalvo);

		dao.atualizarOuSalva(tel1);
		dao.atualizarOuSalva(tel2);

	}
	
	@Test
	public void testaBuscarCidades() {
		
		IDaoPessoaImpl iDaoPessoaImpl = new IDaoPessoaImpl();
		
		List<SelectItem> listaCidades = iDaoPessoaImpl.listaCidades(1L);
		
		for (SelectItem selectItem : listaCidades) {
			System.out.println(selectItem.getLabel());
		}
		
		
	}

}
