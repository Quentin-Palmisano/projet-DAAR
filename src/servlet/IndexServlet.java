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
	
	public static ArrayList<Livre> create(String keywords) throws Exception {
		
		String[] tab = keywords.split("\\W+");
		ArrayList<Livre> livres = new ArrayList<>();
		
		Connection con = ConnectionProvider.getCon();
		PreparedStatement ps=con.prepareStatement("SELECT Livre.Id, Livre.Titre, Livre.Author, Livre.Date, Livre.Language, Occurence.Count FROM Livre "
				+ "INNER JOIN Occurence ON Occurence.Id=Livre.Id WHERE Occurence.Mot = \"?\" AND Occurence.Count > 0 ORDER BY Occurence.Count DESC;");
		ps.setString(1, keywords);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			livres.add(new Livre(rs));
		}
		
		System.out.println(livres.size());
		
		return livres;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			var type = request.getParameter("type");
			var keywords = request.getParameter("keywords");
			
			if(type.equals("keyword")) {
				
				ArrayList<Livre> livres = create(keywords);
				request.setAttribute("livres", livres);
				
			}else if(type.equals("regex")) {
				
			}
			

			request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
			
		}
		
	}
	
}
