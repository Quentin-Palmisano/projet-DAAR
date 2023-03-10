<%@ page import="database.*"%>
<%@ page import="java.util.ArrayList"%>

<%
	var attr = request.getAttribute("livre");
	Livre livre = new Livre();
	if(attr != null && attr instanceof Livre) {
		livre = (Livre) attr;
	}
%>

<!DOCTYPE html>
<html>
<head>
<title><%= livre.getTitre() %></title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
	crossorigin="anonymous">
<link href="/css/common.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
	crossorigin="anonymous"></script>
<script src="/js/common.js"></script>
<meta charset="utf-8">
</head>
<body>

	<div class="container">

		<h2 class=""><%= livre.getTitre() %></h2>

		<div>Author : <%= livre.getAuthor() %></div>
		<div>Date : <%= livre.getDate() %></div>
		<div>Language : <%= livre.getLanguage() %></div>
		
		<%
			var sugg = request.getAttribute("suggestions");
			ArrayList<Livre> suggestions = new ArrayList<>();
			if(sugg != null && sugg instanceof ArrayList<?>) {
				suggestions = (ArrayList<Livre>) sugg;
			}
		%>
		
		<br/>
		
		<div>Suggestions :</div>

		<ul>
			<%
				for(Livre suggestion : suggestions) {
			%>
			
			<li>
				<div> <a href="/Livre?id=<%= ""+suggestion.getId() %>" >Title : <%=suggestion.getTitre() %></a></div>
				<div>Author : <%= suggestion.getAuthor() %></div>
				<div>Language : <%= suggestion.getLanguage() %></div>
			</li>

			<%
				}
			%>
			
		</ul>
		
		<br/>
		
		<div>Book text :</div>
		
		<div>
			<%= request.getAttribute("text") %>
		</div>
		
	</div>
</body>
</html>
