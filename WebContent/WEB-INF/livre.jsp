<%@ page import="database.*"%>
<%@ page import="java.util.ArrayList"%>

<!DOCTYPE html>
<html>
<head>
<title>Livre</title>
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
		var attr = request.getAttribute("livre");
		Livre livre = new Livre();
		if(attr != null && attr instanceof Livre) {
			livre = (Livre) attr;
		}
	%>

		<h2 class=""><%= livre.getTitre() %></h2>

		<div>Author : <%= livre.getAuthor() %></div>
		<div>Date : <%= livre.getDate() %></div>
		<div>Language : <%= livre.getLanguage() %></div>

	</div>
</body>
</html>
