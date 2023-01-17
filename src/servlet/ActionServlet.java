package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import data.Money;
import data.Offer;
import data.Resource;
import data.ResourceProduction;
import data.User;
import exception.NotEnoughMoneyException;

@WebServlet("/action")
public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ActionServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if (!User.isConnected(request.getSession())) {
				response.sendRedirect("/");
				return;
			}
			
			User user = User.getConnected(request.getSession());
			var action = request.getParameter("action");
			
			if (action.equals("addProduction")) {

				var resource = request.getParameter("resource");
				Resource r = Resource.get(Integer.parseInt(resource));
				ResourceProduction rp = user.getProduction().get(r);
				
				try {
					user.pay(rp.getProductionCost());
					rp.addProduction();
				} catch(NotEnoughMoneyException e) {}
				
			} else if(action.equals("publish")) {

				var resource = request.getParameter("resource");
				Resource r = Resource.get(Integer.parseInt(resource));
				
				boolean buy = Boolean.parseBoolean(request.getParameter("buy"));
				long price = Money.parse(request.getParameter("price"));
				long quantity = Long.parseLong(request.getParameter("quantity"));
				
				Offer offer = new Offer(user.id, r, buy, price, quantity);
				request.getSession().setAttribute("lastOffer", offer);
				
				user.getOffers().insert(offer);
				
				var offers = user.getOffers().search(offer);
				
				request.setAttribute("offers", offers);
				
				request.getRequestDispatcher("/update").forward(request, response);
				return;
				
			}  else if(action.equals("search")) {

				var resource = request.getParameter("resource");
				Resource r = Resource.get(Integer.parseInt(resource));
				
				boolean buy = Boolean.parseBoolean(request.getParameter("buy"));
				long price = Money.parse(request.getParameter("price"));
				long quantity = Long.parseLong(request.getParameter("quantity"));
				
				Offer offer = new Offer(user.id, r, buy, price, quantity);
				request.getSession().setAttribute("lastOffer", offer);
				
				var offers = user.getOffers().search(offer);
				
				request.setAttribute("offers", offers);
				
				request.getRequestDispatcher("/update").forward(request, response);
				return;
				
			} else if(action.equals("delete")) {
				
				long id = Integer.parseInt(request.getParameter("id"));
				
				Offer.delete(id);
				
				request.getRequestDispatcher("/update").forward(request, response);
				return;
			
			} else if(action.equals("changeResearch")) {
				
				var resource = request.getParameter("resource");
				Resource r = Resource.get(Integer.parseInt(resource));
				ResourceProduction rp = user.getProduction().get(r);
				
				var cost = Money.parse(request.getParameter("cost"));
				rp.research_cost = cost;
				
				rp.update();
				
			} else {
				System.err.println("Unknown action : " + action);
			}
			
			request.getRequestDispatcher("/update").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
