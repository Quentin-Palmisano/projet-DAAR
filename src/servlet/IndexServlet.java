package servlet;

import java.io.IOException;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.ConnectionProvider;
import database.Livre;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public IndexServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
			
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			var type = request.getParameter("type");
			var keywords = request.getParameter("keywords");
			var tri = request.getParameter("tri");
			
			if(type.equals("keyword")) {
				
				ArrayList<Livre> livres = Livre.searchKeywords(keywords, tri);
				request.setAttribute("livres", livres);
				
			}else if(type.equals("regex")) {
				
				ArrayList<Livre> livres = Livre.searchRegex(keywords, tri);
				request.setAttribute("livres", livres);
				
			}

			request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
			
		}
		
	}
	
}
