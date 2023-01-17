const refresh_rate = 1000; // milliseconds

function error(e) {
    console.log(e);
}


function refresh() {
    $.getJSON("/update", {}, reload, "json").fail(error);
}

// Production

function addProduction(resource) {
    $.post("/action", {"action":"addProduction", resource}, reload, "json").fail(error);
}

function changeResearch(resource, element) {
    $.post("/action", {"action":"changeResearch", resource, "cost":element.value}, reload, "json").fail(error);
}


// Offers

function search() {
	let resource = $("#search [name='resource']").val();
	let buy = $("#search [name='achat_vente']:checked").val();
	let price = $("#search [name='price']").val();
	let quantity = $("#search [name='quantity']").val();
	
	$.post("/action", {"action":"search", resource, buy, price, quantity}, reload, "json").fail(error);
}

function publish() {
	let resource = $("#search [name='resource']").val();
	let buy = $("#search [name='achat_vente']:checked").val();
	let price = $("#search [name='price']").val();
	let quantity = $("#search [name='quantity']").val();
	
	$.post("/action", {"action":"publish", resource, buy, price, quantity}, reload, "json").fail(error);
}


function deleteOffer(e) {
	let id = $(e).attr("for");
	
	$.post("/action", {"action":"delete", id}, reload, "json").fail(error);
	
	$(e.target).remove();
}

function reload(data) {
    
    $("#money").html(formatMoney(data.money));
 	$("#firstUser").html(data.topPlayer);
 	$("#firstMoney").html(formatMoney(data.topMoney));

    for(let i in data.resources) {
    	let res = data.resources[i];
        $("#" + res.name + " .count").html(formatNumber(res.count));
        $("#" + res.name + " .price").html(formatMoney(res.price));
        $("#" + res.name + " .production").html(formatNumber(res.production));
        $("#" + res.name + " .production-cost").html(formatMoney(res.production_cost));
        $("#" + res.name + " .research").html(formatMoney(res.research+10000));
        $("#" + res.name + " .research-cost").attr('value', money(res.research_cost));
    }
    
    let offers = $("#offer-list");
	offers.empty();
	
	let template = $("#template");
	
	for(let i in data.offers) {
		let offer = data.offers[i];
		
		let offer_id = offer.id;
		
		let offer_card = template.clone();
		offer_card.appendTo(offers);
		
		offer_card.attr("id", "modify");
		
		offer_card.attr("buy", offer.buy);
		
		$("#modify .price").html(formatMoney(offer.price));
		
		$("#modify .quantity").html(formatNumber(offer.quantity));
		
		let deleteButton = $("#modify .delete");
		
		$("#modify .offerer").html(offer.user_name);
		
		if(offer.user_id == data.user_id) {
			deleteButton.attr("for", offer_id);
		} else {
			deleteButton.attr("hidden", true);
		}
		
		offer_card.attr("id", "offer" + offer_id);
		//offer_card.removeAttr("hidden");
		
	}
	
	
	updateCurrencySpy();

}




window.addEventListener("load", (e) => {
	updateCurrencySpy();
    $.ajaxSetup({ cache: false });
    refresh();
    setInterval(refresh, refresh_rate);
});

/*
$('.auto-submit').submit(function(e){
    e.preventDefault();
    
    $.ajax({
        type: 'POST',
        cache: false,
        url: $(this).attr('action'),
        data: 'action-type='+$(this).attr('action-type')+'&'+$(this).serialize(), 
        success: update
    }).fail(error);
    
    const form = e.target;
	let data = new FormData(form);
	data.set("action-type", "publish");
    fetch(form.action, {
        method: form.method,
        body: data
    }).then(update).catch(error);
});
*/
    
