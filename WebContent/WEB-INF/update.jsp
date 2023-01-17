<%@ page import="data.*" %>
<%@ page import="simulation.Market" %>
<%@ page import="java.util.ArrayList" %>
{
	<%
	if(request.getAttribute("error") != null) {
	%>
	error: "${requestScope.error}",
	<% } %>
	<%
	User user = (User) request.getAttribute("user");
	User.getFirst();
	%>
	"user": "<%= user.name %>",
	"user_id": "<%= user.id %>",
	"money": <%= user.money %>,
	"topPlayer": "<%= User.firstUser %>",
	"topMoney": "<%= User.firstMoney %>",
	"resources": {
		<%
		boolean first = true;
		for(Resource res : Resource.values()) {
			ResourceProduction rp = user.getProduction().get(res);
		%><%= first ? "" : "," %>
		"<%= res %>": {
				"id": <%=res.getID() %>,
				"name": "<%=res %>",
				"count": <%=rp.count %>,
				"price": <%= Market.price(res) %>,
				"production_cost": <%=rp.getProductionCost() %>,
				"production": <%=rp.production %>,
				"research_cost": <%=rp.research_cost %>,
				"research": <%=rp.research %>
			}
		<%
		first = false;
		}
		%>
	},
	"offers": [
		<%
		first = true;
		Object offers = request.getAttribute("offers");
		if(offers != null) {
			for(Offer offer : (ArrayList<Offer>) offers) {
		%><%= first ? "" : "," %>
			{
				"id": <%=offer.id %>,
				"user_id": "<%=offer.user_id %>",
				"user_name": "<%=offer.user_name %>",
				"res_id": "<%=offer.resource %>",
				"buy": <%=offer.buy %>,
				"price": <%=offer.price %>,
				"quantity": <%=offer.quantity %>
			}
		<%
			first = false;
			}
		}
		%>
	]

}