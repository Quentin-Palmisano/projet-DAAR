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
			${requestScope.error}</div>

		<%
			}
			%>

		<h2 class="">Projet DAAR</h2>

		<form action="" method="get" class="form">
			<input type="radio" id="keyword" name="type" value="keyword" checked>
			<label for="html">keyword</label><br>
			<input type="radio" id="regex" name="type" value="regex">
			<label for="html">regex</label><br>
			<input type="text" id="fname" name=keyword placeholder="recherche"><br> 
			<input type="submit" value="Submit">
		</form>

	</div>
</body>
</html>
