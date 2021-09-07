package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.hibernate.HibernateUtil;

@FacesConverter(forClass = Cidades.class, value = "cidadeConverter")
public class CidadeConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override // Retorna obj inteiro
	public Object getAsObject(FacesContext context, UIComponent component, String codigoCidade) {

		if (codigoCidade.equalsIgnoreCase("--[Selecione]--")) {
			return null;
		} else {

			return HibernateUtil.getEntityManager().find(Cidades.class, Long.parseLong(codigoCidade));
		}

	}

	@Override // Retorna codigo em String
	public String getAsString(FacesContext context, UIComponent component, Object cidade) {

		if (cidade == null) {
			return null;
		}

		if (cidade instanceof Cidades) {

			return ((Cidades) cidade).getId().toString();
		} else {
			return cidade.toString();
		}

	}

}
