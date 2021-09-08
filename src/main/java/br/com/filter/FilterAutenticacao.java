package br.com.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.entidades.Pessoa;
import br.com.hibernate.HibernateUtil;

@WebFilter(urlPatterns = { "/*" })
public class FilterAutenticacao implements Filter {

	@Inject
	private HibernateUtil hibernateUtil;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		hibernateUtil.getEntityManager();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		System.out.println("Invocando o filter");
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		 Pessoa usuarioLogado = (Pessoa) session.getAttribute("usuarioLogado");
		
		
		String url = req.getServletPath();
		
		if (!url.equalsIgnoreCase("/index.xhtml") && usuarioLogado == null) {
			request.getRequestDispatcher("/index.xhtml").forward(request, response);
		} else {
			chain.doFilter(request, response);
		}


	}

	@Override
	public void destroy() {

	}

}
