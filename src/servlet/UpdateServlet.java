package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import data.User;

@WebServlet("/update")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			if(!User.isConnected(request.getSession())) {
				response.sendRedirect("/");
				return;
			}
			
			response.setContentType("application/json");
			response.setHeader("Content-Disposition", "inline");
		
			
			User user = User.getConnected(request.getSession());
			request.setAttribute("user", user);
			
			data.Offer search = (data.Offer) request.getSession().getAttribute("lastOffer");
			if(search != null) {
				var offers = user.getOffers().search(search);
				
				request.setAttribute("offers", offers);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			request.setAttribute("error", e.getMessage());
			
		}
		
		request.getRequestDispatcher("/WEB-INF/update.jsp").include(request, response);
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
