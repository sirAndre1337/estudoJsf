package br.com.converter;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import br.com.entidades.Estados;

@FacesConverter(forClass = Estados.class, value = "estadoConverter")
public class EstadoConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	

	@Override // Retorna obj inteiro
	public Object getAsObject(FacesContext context, UIComponent component, String codigoEstado) {

		if (codigoEstado.equalsIgnoreCase("--[Selecione]--")) {
			return null;
		} else {

			EntityManager entityManager = CDI.current().select(EntityManager.class).get();

			return entityManager.find(Estados.class, Long.parseLong(codigoEstado));
		}
	}

	@Override // Retorna codigo em String
	public String getAsString(FacesContext context, UIComponent component, Object estado) {

		if (estado == null) {
			return null;
		}

		if (estado instanceof Estados) {
			return ((Estados) estado).getId().toString();

		} else {
			return estado.toString();
		}

	}

}
