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

@WebServlet("/Livre")
public class LivreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public LivreServlet() {
        super();
    }
    
    public static boolean isThere2words(String[] tab) {
    	boolean b = false;
    	
    	if(b) {
    		
    	}
    	
    	return false;
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			var id = request.getParameter("id");
			
			var livre = Livre.get(Integer.parseInt(id));
			
			request.setAttribute("livre", livre);
			
			var suggestions = livre.getSuggestions();
			
			request.setAttribute("suggestions", suggestions);
			
			var text = livre.getText(getServletContext());
			
			request.setAttribute("text", text);

			request.getRequestDispatcher("/WEB-INF/livre.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-INF/livre.jsp").forward(request, response);
			
		}
		
	}
	
}
