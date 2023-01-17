<%@ page import="data.*"%>
<%@ page import="simulation.*"%>

<!DOCTYPE html>
<html>
	<head>
		<title>Login</title>
		<link
			href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
			rel="stylesheet"
			integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
			crossorigin="anonymous">
		<link href="/css/game.css" rel="stylesheet">
		<link href="/css/common.css" rel="stylesheet">
		<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
		<script
			src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
			integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
			crossorigin="anonymous"></script>
		<script src="/js/common.js"></script>
		<script src="/js/game.js"></script>
		<meta charset="utf-8">
	</head>
	<body class="bg-dark text-white">
	
		<svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
			<symbol id="info-fill" fill="currentColor" viewBox="0 0 16 16">
				<path d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm.93-9.412-1 4.705c-.07.34.029.533.304.533.194 0 .487-.07.686-.246l-.088.416c-.287.346-.92.598-1.465.598-.703 0-1.002-.422-.808-1.319l.738-3.468c.064-.293.006-.399-.287-.47l-.451-.081.082-.381 2.29-.287zM8 5.5a1 1 0 1 1 0-2 1 1 0 0 1 0 2z"/>
			</symbol>
		</svg>

		<div class="container">
	
			<%
			if (request.getAttribute("error") != null) {
			%>
	
			<div class="alert alert-danger" role="alert">
				${requestScope.error}</div>
	
			<%
			}
			%>
	
			<%
			User user = (User) request.getAttribute("user");
			%>
	
			<nav class="navbar fixed-top navbar-expand-lg navbar-dark bg-secondary container-fluid">
				<div class="navrow row">
					<span class="navitem navbar-brand col">Game</span>
					<span id="username" class="navitem navbar-text col"><%= user.name %></span>
					<span class="navitem navbar-text col">$<span id="money" class="currency"></span></span>
					<span class="navitem navbar-text col-8">Top player : <span id="firstUser"></span> with $<span id="firstMoney"></span></span>
					
					<form class="navitem col" method="post" action="/">
						<button type="submit" name="action" value="logout"
							class="btn btn-secondary">Log out</button>
					</form>
				</div>
			</nav>
	
			<div class="row m-top">
				<div class="col">
					<ul class="scroll group-list bg-dark">
			
						<%
						for (Resource res : Resource.values()) {
							ResourceProduction rp = user.getProduction().get(res);
							long price = Market.price(res);
						%>
						
						<li id="<%=res%>" class="<%=res%> list-group-item list-group-item-dark bg-dark">
							<div class="card text-white dark mb-3">
								<div class="card-body">
								
								<div class="form-group">
									
							  		<h5 class="resource card-title">
									<%=res%>
							  		<%-- <span title=<%=info %>>
							  		<svg class="bi flex-shrink-0 me-2" width="20" height="20" role="img" aria-label="Info:"><use xlink:href="#info-fill"/></svg>
							  		</span> --%>
							  		
							  		</h5>
							  		
									</div>
									<%
									Recipe recipe = Crafting.recipes.get(res);
									String craft="";
									if(recipe!=null){
										craft = "<h6 class='card-subtitle1 mb-2 text-muted'>Craft : <span class='craft'>" + recipe.getInfo() + "<//span></h6>";
									}
									%>
									<%=craft %>
									<h6 class="card-subtitle1 mb-2 text-muted">Price : $<span class="price"></span></h6>
							    	<h6 class="card-subtitle1 mb-2 text-muted">Stock : <span class="count"></span></h6>
							    	<h6 class="card-subtitle2 mb-2 text-muted">Production : <span class="production"></span></h6>
									<div class="card-subtitle1 form-group text-muted">
										<label class="card-subtitle1 form-check-label">Research investment : </label>
					    				$<input type="number" data-type="currency" step=0.01 class="research-cost area mb-2" name="invest" onchange="changeResearch(<%=res.getID()%>, this)" value="">
					    			</div>
							    	<h6 class="card-subtitle1 mb-2 text-muted">Production efficiency : <span class="research"></span>%</h6>
									<button onclick="addProduction(<%=res.getID()%>)" type="button" class="btn btn-secondary">Add production for $<span class="production-cost currency"></span></button>
							  	</div>
							</div>
						</li>
						
						<%
						}
						%>
			
					</ul>
				</div>
				
				<div class="col">
	
					<form id="search" class="auto-submit" method="post" action="/action">
					
						<div class="form-check form-check-inline mb-2">
							<input class="form-check-input" type="radio" name="achat_vente" value="true" onchange="search()" required checked>
					  		<label class="form-check-label">Buy</label>
						</div>
						
						<div class="form-check form-check-inline mb-2">
					  		<input class="form-check-input" type="radio" name="achat_vente" value="false" onchange="search()" required>
					  		<label class="form-check-label">Sell</label>
						</div>
						
						<div class="form-group">
							<select class="custom-select mb-2" name="resource" onchange="search()" required>
						      	
						      	<%
								for (Resource res : Resource.values()) {
								%>
								
								<option value="<%=res.getID() %>"><%=res %></option>
								
								<%
								}
								%>
								
						    </select>
				     	</div>
				     	
				     	<div class="form-group form-check-inline">
					    	<label class="form-check-label">Target Price $</label>
					    	<input type="text" data-type="currency" step=0.01 class="area mb-2" value="1" name="price" required>
					  	</div>
					  	
					  	<div class="form-group">
					    	<label class="form-check-label">Target Quantity</label>
					    	<input type="number" step=1 class="area mb-2" value="10" name="quantity" required>
					    </div>
					  	
					  	<div class="form-group row">
					  		<button type="button" onclick="search()" name="action" value="search" class="btn btn-secondary m-2 col-md-2">Search</button>
					  		<button type="button" onclick="publish()" name="action" value="publish" class="btn btn-secondary m-2 col-md-2">Publish</button>
					  	</div>
					</form>
					
					<ul hidden="" class= "scroll2">
						<li id="template" class="list-group-item list-group-item-dark bg-dark">
							<div class="card text-white dark mb-3">
								<div class="card-body">
							    	<h6 class="card-subtitle1 mb-2 text-muted">User : <span class="offerer"></span></h6>
							    	<h6 class="card-subtitle1 mb-2 text-muted">Price : $<span class="price currency"></span></h6>
							    	<h6 class="card-subtitle1 mb-2 text-muted">Quantity : <span class="quantity"></span></h6>
									<button type="button" onclick="deleteOffer(this)" class="delete btn btn-secondary">Delete</button>
							  	</div>
							</div>
						</li>
					</ul>
					
					<ul id="offer-list" class="scroll group-list bg-dark">
						
						
			
					</ul>
					
				</div>
				
			</div>
			
	
		</div>
		
	</body>
</html>
