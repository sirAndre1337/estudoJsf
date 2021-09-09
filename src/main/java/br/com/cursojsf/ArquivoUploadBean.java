package br.com.cursojsf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Scanner;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import br.com.entidades.ArquivoUpload;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoArquivoUpload;

@ViewScoped
@Named(value = "arquivoUploadBean")
public class ArquivoUploadBean implements Serializable{
	private static final long serialVersionUID = 1L;

	private ArquivoUpload arquivoUpload = new ArquivoUpload();
	
	private Part arquivo;
	
	@Inject
	private IDaoArquivoUpload iDaoArquivoUpload;
	
	public String salvar() throws IOException {
		
		byte[] arquivoEmByte = toByteArrayUssingJava(arquivo.getInputStream());
		
		arquivoUpload.setArquivo(arquivoEmByte);
		
		Pessoa usuarioLogado = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
		
		arquivoUpload.setUsuario(usuarioLogado);
		iDaoArquivoUpload.salvar(arquivoUpload);
		
		Scanner conteudo = new Scanner(arquivo.getInputStream());
		
		while (conteudo.hasNext()) {
				System.out.println(conteudo.next());
		}
		
		
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
	
	
}
