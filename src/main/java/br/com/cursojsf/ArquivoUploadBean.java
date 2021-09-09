package br.com.cursojsf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import br.com.dao.DaoGeneric;
import br.com.entidades.ArquivoUpload;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoArquivoUpload;

@ViewScoped
@Named(value = "arquivoUploadBean")
public class ArquivoUploadBean implements Serializable{
	private static final long serialVersionUID = 1L;

	private ArquivoUpload arquivoUpload = new ArquivoUpload();
	
	private Part arquivo;
	
	private List<ArquivoUpload> listaArquivoSalvos = new ArrayList<ArquivoUpload>();
	
	@Inject
	private IDaoArquivoUpload iDaoArquivoUpload;
	
	@Inject
	private DaoGeneric<Pessoa> dao;
	
	public String salvar() throws IOException {
		
		byte[] arquivoEmByte = toByteArrayUssingJava(arquivo.getInputStream());
		
		arquivoUpload.setArquivo(arquivoEmByte);
		
		Pessoa usuarioLogado = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
		
		arquivoUpload.setUsuario(usuarioLogado);
		iDaoArquivoUpload.salvar(arquivoUpload);
		
		carregarArquivosSalvos();
		
		return "";
	}
	
	@PostConstruct
	private void carregarArquivosSalvos() {
		
		listaArquivoSalvos = iDaoArquivoUpload.carregarArquivosSalvos();
		
	}
	
	public void download() throws IOException {
		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownloadId = params.get("fileDownloadId");
		
		ArquivoUpload arquivoRetornado  = iDaoArquivoUpload.buscarArquivo(fileDownloadId);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		response.addHeader("Content-Disposition" , "attachment; filename=download.csv");
		response.setContentType("application/octet-stream");
		response.setContentLength(arquivoRetornado.getArquivo().length);
		response.getOutputStream().write(arquivoRetornado.getArquivo());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
		
	}

	public String upload() throws IOException {
		
		// Lendo arquivo em CSV --------
		
				Scanner scanner = new Scanner(arquivo.getInputStream() , "UTF-8");
				scanner.useDelimiter(",");
				
				String linha;
				int aux = 0;
				
				while (scanner.hasNext() && aux == 0) {
					linha = scanner.nextLine();
					
					if (linha != null && !linha.trim().isEmpty()) {
						
						if (!linha.equalsIgnoreCase(";")) {
						String[] dados = linha.split("\\;"); // onde tem a virgula ele quebra
						
						Pessoa p = new Pessoa();
						p.setNome(dados[0]);
						p.setLogin(dados[1]);
						dao.atualizarOuSalva(p);
						
						} else {
							aux = 1;
						}
						
					}
					
				}
				salvar();
				
				return "";
	}
	
	// Pega o arquivo e transforma em byte
	public byte[] toByteArrayUssingJava(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();
		while (reads != -1) {
			baos.write(reads);
			reads = is.read();
		}
		return baos.toByteArray();
	}

	public ArquivoUpload getArquivoUpload() {
		return arquivoUpload;
	}

	public void setArquivoUpload(ArquivoUpload arquivoUpload) {
		this.arquivoUpload = arquivoUpload;
	}

	public Part getArquivo() {
		return arquivo;
	}

	public void setArquivo(Part arquivo) {
		this.arquivo = arquivo;
	}

	
	public List<ArquivoUpload> getListaArquivoSalvos() {
		return listaArquivoSalvos;
	}
	
	
}
