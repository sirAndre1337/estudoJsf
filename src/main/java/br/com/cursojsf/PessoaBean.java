package br.com.cursojsf;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;

@ViewScoped
@Named(value = "pessoaBean")
public class PessoaBean implements Serializable{

	private static final long serialVersionUID = 1L;
		
	private Pessoa pessoa = new Pessoa();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private BarChartModel barChartModel = new BarChartModel();

	public BarChartModel getBarChartModel() {
		return barChartModel;
	}

	@Inject
	private DaoGeneric<Pessoa> dao;
	
	@Inject
	private IDaoPessoa iDaoPessoaImpl;

	private List<SelectItem> estados;
	
	private List<SelectItem> cidades;
	
	private Part arquivoFoto;

	public String salvar() throws IOException {

		// processar imagem
		
		if (arquivoFoto != null) {
		
		byte[] imagemByte = getByteNaMao(arquivoFoto.getInputStream());
		pessoa.setFotoIconBase64Original(imagemByte); // salva imagem original
		
		//Transformar a img em miniatura
		//Transforma a imagem em um array de byte em um bufferedimg
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
		
		//Pega o tipo da imagem
		int tipo = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		
		int largura = 200;
		int altura = 200;
		
		// Criar a miniatura
		BufferedImage resizedImage = new BufferedImage(altura, largura, tipo);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(bufferedImage, 0, 0, largura, altura, null);
		g.dispose();
		
		// Escrever novamente a imagem em tamanho menor
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extensao = arquivoFoto.getContentType().split("\\/")[1]; // metodo retorna 'image/png' o split faz ele retorna so a 1 posicao (png)
		ImageIO.write(resizedImage, extensao, baos);
		
		String miniImagem = "data:" + arquivoFoto.getContentType() + ";base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
		
		pessoa.setFotoIconBase64(miniImagem);
		pessoa.setExtensao(extensao);
		} 
		
		pessoa = dao.atualizarOuSalva(pessoa);
		pesquisar();
		

		if (pessoa.getCidades() != null) {
			Estados estado = pessoa.getCidades().getEstado();
			pessoa.setEstados(estado);
			setCidades(iDaoPessoaImpl.listaCidades(estado.getId()));
		}
		
		
		messagens("Cadastrado com sucesso");
		return "";
	}

	private void messagens(String msg) {

		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}

	public String novo() {

		pessoa = new Pessoa();
		return "";

	}
	
	public void mudancaDeValor(ValueChangeEvent evento) {
		System.out.println("Valor antigo :"  + evento.getOldValue() );
		System.out.println("Valor novo :"  + evento.getNewValue() );
	}
	
	// chamado com o action listener antes do action normal
	public void registrarLog() {
		// rotina
	}

	public String excluir() {

		dao.deletarPoridComQuery(pessoa);
		pessoa = new Pessoa();
		pesquisar();
		messagens("Excluido com sucesso");
		return "";

	}

	@PostConstruct
	public void pesquisar() {
		pessoas = dao.buscaTodos(Pessoa.class);
		
		ChartSeries medida = new ChartSeries();
		medida.setLabel("Users");
		
		for (Pessoa pes : pessoas) {
			
			medida.set(pes.getNome(), pes.getIdade());
		}
		
		barChartModel.addSeries(medida);
		barChartModel.setTitle("Grafico de salarios");
		

	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public String logar() {

		Pessoa pessoaConsultada = iDaoPessoaImpl.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());

		if (pessoaConsultada == null) {
			messagens("Login ou senha incorretos.");
			return "index.xhtml";
		} else {

			// adiciona o usuario logado na sessao
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", pessoaConsultada);

			return "primeirapagina";
		}

	}

	public boolean permitirAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaConsultado = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

		return pessoaConsultado.getPerfilUser().equals(acesso);

	}

	public String logout() {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.invalidateSession();

		return "index.xhtml";
	}

	public void pesquisarCep(AjaxBehaviorEvent event) {

		try {

			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/"); // url do webservice

			URLConnection connection = url.openConnection(); // cria a conexao com a url
			InputStream is = connection.getInputStream(); // busca do webservice os dados

			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String cep = "";
			StringBuilder jsonCep = new StringBuilder();

			while ((cep = br.readLine()) != null) { // enquanto tiver dados append

				jsonCep.append(cep);

			}

			Pessoa aux = new Gson().fromJson(jsonCep.toString(), Pessoa.class); // obj aux para settar os dados do json

			pessoa.setLogradouro(aux.getLogradouro());
			pessoa.setComplemento(aux.getComplemento());
			pessoa.setBairro(aux.getBairro());
			pessoa.setLocalidade(aux.getLocalidade());
			pessoa.setUf(aux.getUf());
			pessoa.setIbge(aux.getIbge());
			pessoa.setGia(aux.getGia());
			pessoa.setDdd(aux.getDdd());
			pessoa.setSiafi(aux.getSiafi());

			System.out.println(pessoa);

		} catch (Exception e) {
			e.printStackTrace();
			messagens("Erro ao consultar o cep");
		}
	}

	public List<SelectItem> getEstados() {
		return estados = iDaoPessoaImpl.listaEstados();
	}

	public void pesquisarCidades(AjaxBehaviorEvent event) {

		//String codigoEstado = (String) event.getComponent().getAttributes().get("submittedValue");
		Estados estado = (Estados) ((HtmlSelectOneMenu)event.getSource()).getValue(); // pega o estado selecionado


			pessoa.setEstados(estado);

			setCidades(iDaoPessoaImpl.listaCidades(estado.getId()));


	}
	
	public String editar() {
		
		if (pessoa.getCidades() != null) {
			Estados estado = pessoa.getCidades().getEstado();
			pessoa.setEstados(estado);
			
			setCidades(iDaoPessoaImpl.listaCidades(estado.getId()));
		}
		return "";
	}
	
	
	// MEtodo que converte inputStream para array de bytes
	private byte[] getByteNaMao(InputStream iStream) throws IOException{
		int length;
		int size = 1024;
		byte[] buff = null;
		
		// Se ele nao for um inputStream entra nesse IF
		if (iStream instanceof ByteArrayInputStream) {
			size = iStream.available();
			buff = new byte[size];
			length = iStream.read(buff , 0 ,size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buff = new byte[size];
			
			//  
			while ((length = iStream.read(buff, 0, size)) != -1) {
				bos.write(buff , 0 , length);
			}
			buff = bos.toByteArray();
		}
		return buff;
	}
	
	public void download() throws IOException {
		
		// pega o parametro passado
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownloadId = params.get("fileDownloadId");
		
		//Busca a pessoa pelo id passado por parametro
		Pessoa p = dao.consultarPorId(Pessoa.class,Long.parseLong(fileDownloadId));
		
		// Download da imagem
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-Disposition", "attachment; filename=download."+p.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(p.getFotoIconBase64Original().length);
		response.getOutputStream().write(p.getFotoIconBase64Original());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
		
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	public Part getArquivoFoto() {
		return arquivoFoto;
	}

	public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

}