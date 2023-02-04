<%@ page import="database.*"%>
<%@ page import="java.util.ArrayList"%>

<!DOCTYPE html>
<html>
<head>
<title>Login</title>
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

		<%
		if(request.getAttribute("error") != null) {
		%>

		<div class="alert alert-danger" role="alert">
			${requestScope.error}
		</div>

		<%
		}
		%>

		<h2 class="">Projet DAAR</h2>

		<form action="" method="post" class="form">
			
			<%
			String option = "keyword";
			if(request.getParameter("type") != null) {
				option = request.getParameter("type");
			}
			%>
			
			<input type="radio" id="keyword" name="type" value="keyword" <%= option.equals("keyword")?"checked":"" %>>
			<label for="html">Keywords</label><br>
			<input type="radio" id="regex" name="type" value="regex" <%= option.equals("regex")?"checked":"" %>>
			<label for="html">Regex</label><br>
			
			<%
			String keywords = "";
			if(request.getParameter("keywords") != null) {
				keywords = request.getParameter("keywords");
			}
			%>
			<input type="text" id="fname" name="keywords" placeholder="Recherche" value="<%= keywords %>"><br>
			
			<%
			String selection = "occ";
			if(request.getParameter("tri") != null) {
				selection = request.getParameter("tri");
			}
			%>
			<select name="tri" id="tri-select">
			    <option value="occ" <%= selection.equals("occ")?"selected='selected'":"" %>>Nombre d'occurence</option>
			    <option value="jacc" <%= selection.equals("occ")?"selected='selected'":"" %>>Jaccard</option>
			</select>
			
			<input type="submit" value="Submit">
		</form>
		
		<%
			var attr = request.getAttribute("livres");
			System.out.println(request.getAttribute("livres"));
			var livres = new ArrayList<Livre>();
			if(attr != null && attr instanceof ArrayList<?>) {
				livres = (ArrayList<Livre>) attr;
			}
		%>
		
		<div><%= livres.size() %> résultat(s) trouvé(s)</div>

		<ul>
			<%
				for(Livre livre : livres) {
			%>
			
			<li>
				<div> <a href="<%= livre.getId() %>" >Title : <%= livre.getTitre() %></a></div>
				<div>Author : <%= livre.getAuthor() %></div>
				<div>Language : <%= livre.getLanguage() %></div>
			</li>

			<%
				}
			%>
			
		</ul>
		

	</div>
</body>
</html>
