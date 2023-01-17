package simulation;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import data.Resource;
import database.ConnectionProvider;

public class Market {
	
	private static long[] prices = new long[Resource.values().length];
	
	public static boolean canAutoSell(Resource res) {
		switch(res) {
			case bread:
			case car:
			case phone:
				return true;
			default:
				return false;
		}
	}
	
	public static long price(Resource res) {
		return prices[res.getID()];
	}
	
	public static void step(ScheduledExecutorService scheduler) throws Exception {
		
		ArrayList<ScheduledFuture<?>> futures = new ArrayList<>();
		
		for(final var res : Resource.values()) {
			
			futures.add(scheduler.schedule(() -> {

				try {
					
					step(res);
				
				} catch (Exception e) {
					e.printStackTrace();
				}

			}, 0, TimeUnit.SECONDS));
			
		}
		
		for(var future : futures) {
			future.get();
		}
		
		
	}
	
	public static void step(Resource res) throws Exception {
		
		Connection con = ConnectionProvider.getCon();
		
		PreparedStatement sellerStatement = con.prepareStatement("select * from offers as o inner join production as p on o.user_id=p.user_id and o.resource=p.resource inner join users as u on o.user_id=u.id where o.resource=? and buy=false and p.count>0 order by o.price");
		PreparedStatement buyerStatement = con.prepareStatement("select * from offers as o left join production as p on o.user_id=p.user_id and o.resource=p.resource inner join users as u on o.user_id=u.id where o.resource=? and buy=true and u.money>0 order by rand()");
		
		sellerStatement.setInt(1, res.getID());
		buyerStatement.setInt(1, res.getID());
		
		ResultSet sellers = sellerStatement.executeQuery();
		ResultSet buyers = buyerStatement.executeQuery();
		
		if(Market.canAutoSell(res)) {
			
			while(sellers.next()) {
				
				long price = Market.price(res);
				long stock = sellers.getLong("p.count");
				
				long quantity = sellers.getLong("o.quantity");
				
				long amount = stock-quantity;
				
				if(amount<=0) continue;
				
				long cost = amount*price;
				
				long seller = sellers.getLong("u.id");
				
				PreparedStatement sellerUpdate = con.prepareStatement("update users as u, production as p set u.money=u.money+?, p.count=p.count-? where p.resource=? and u.id=? and p.user_id=u.id");
				sellerUpdate.setLong(1, cost);
				sellerUpdate.setLong(2, amount);
				sellerUpdate.setInt(3, res.getID());
				sellerUpdate.setLong(4, seller);
				
				sellerUpdate.execute();
				
			}
			
		} else {
		
			if(!sellers.next()) return;
			prices[res.getID()] = sellers.getLong("o.price");
			
			while(buyers.next()) {
				
				long price = sellers.getLong("o.price");
				if(price > buyers.getLong("o.price")) continue;
				
				long money = buyers.getLong("u.money");
				long stock = sellers.getLong("p.count");
				if(stock<=0) {
					if(!sellers.next()) {
						return;
					}
				}
				
				long buyAmount = Math.min(money/price, buyers.getLong("o.quantity") - buyers.getLong("p.count"));
				long sellAmount = stock - sellers.getLong("o.quantity");
				long amount = Math.min(sellAmount, buyAmount);
				

				if(amount<=0) continue;
				
				long cost = amount*price;
				
				long buyer = buyers.getLong("u.id");
				long seller = sellers.getLong("u.id");
				
				if(buyers.getLong("p.user_id") == 0) {
					PreparedStatement ps = con.prepareStatement("insert into production (user_id, resource, count, production, research_cost, research) values (?,?,?,?,?,?);");
					ps.setLong(1, buyer);
					ps.setInt(2, res.getID());
					ps.setLong(3, 0);
					ps.setLong(4, 0);
					ps.setLong(5, 0);
					ps.setLong(6, 0);
					
					ps.executeUpdate();
					continue;
				}
				
				PreparedStatement buyerUpdate = con.prepareStatement("update users as u, production as p set u.money=u.money-?, p.count=p.count+? where p.resource=? and u.id=? and p.user_id=u.id");
				buyerUpdate.setLong(1, cost);
				buyerUpdate.setLong(2, amount);
				buyerUpdate.setInt(3, res.getID());
				buyerUpdate.setLong(4, buyer);
				
				buyerUpdate.executeUpdate();
				
				PreparedStatement sellerUpdate = con.prepareStatement("update users as u, production as p set u.money=u.money+?, p.count=p.count-? where p.resource=? and u.id=? and p.user_id=u.id");
				sellerUpdate.setLong(1, cost);
				sellerUpdate.setLong(2, amount);
				sellerUpdate.setInt(3, res.getID());
				sellerUpdate.setLong(4, seller);
				
				sellerUpdate.executeUpdate();
				
			}
			
		}
		
	}

	public static void updatePrice() throws IOException, InterruptedException, ParseException {
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://yfapi.net/v6/finance/quote?region=US&lang=en&symbols=WMT%2CAAPL%2CTSLA"))
				.header("accept", "application/json")
				.header("x-api-key", "nEJ9s0tymQ9pXFR6NPN1O4hUwllE0pek3I1aenBK")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
			
		HttpResponse<String> response = HttpClient.newHttpClient()
			.send(request, HttpResponse.BodyHandlers.ofString());
			
	    JSONParser parse = new JSONParser();
	    JSONObject data_obj = (JSONObject) parse.parse(response.body());
	    
	    JSONObject obj = (JSONObject) data_obj.get("quoteResponse");
		
	    JSONArray result = (JSONArray) obj.get("result");
		
	    for (int i = 0; i < result.size(); i++) {
	    	JSONObject data = (JSONObject) result.get(i);
	    	long price = ((Double) (((double) data.get("regularMarketPrice")) * 100)).longValue();
	    	if (i == 0) prices[Resource.bread.getID()] = price / 10;
	    	if (i == 1) prices[Resource.phone.getID()] = price * 5;
	    	if (i == 2) prices[Resource.car.getID()] = price * 10;
	    }
	}
	
}
