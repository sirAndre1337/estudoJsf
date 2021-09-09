package br.com.repository;

import java.util.List;

import br.com.entidades.ArquivoUpload;

public interface IDaoArquivoUpload {

	void salvar(ArquivoUpload arquivoUpload);

	List<ArquivoUpload> carregarArquivosSalvos();
	
	ArquivoUpload buscarArquivo(String id);
	
}
