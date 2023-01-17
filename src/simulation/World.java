package simulation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import data.Crafting;
import data.Resource;
import data.ResourceProduction;
import data.User;
import database.ConnectionProvider;
import exception.NotEnoughMoneyException;

public class World {
	
	public static World create() throws Exception {
		
		World world = new World();
		
		return world;
	}
	
	public void research(User user, ResourceProduction rp) throws Exception {
		
		try {
			user.pay(rp.research_cost);
		
			final double ratio = 2000;
			var prob = 1. - ratio / (ratio+rp.research_cost/100.);

			if(Math.random() < prob) rp.research += 1;
		} catch (NotEnoughMoneyException ex) {}
		
	}
	
	public void step(long user_id) throws Exception {
		
		User user = User.create(user_id);
		
		for(Resource resource : Resource.values()) {
			
			var rp = user.getProduction().get(resource);
			
			research(user, rp);
			
			var efficiency = (100. + rp.research/100.)/100.;
			
			long max = (long) (rp.production * efficiency);

			var recipe = Crafting.recipes.get(resource);
			
			if(recipe == null) {
				
				rp.count += max;
				
			} else {
				
				for(var ing : recipe.getIngredients()) {
					
					var irp = user.getProduction().get(ing.getResource());
					
					max = Math.min(max, irp.count / ing.getCount());
					
				}
				
				if(max < 0) {
					continue;
				}
				
				rp.count += max;
				
				for(var ing : recipe.getIngredients()) {
					
					var irp = user.getProduction().get(ing.getResource());
					
					irp.count -= max * ing.getCount();
					
				}
				
			}
			
		}
		
		user.update();
		user.getProduction().update();
		
	}

	public void step(ScheduledExecutorService scheduler) throws Exception {
		
		Connection con = ConnectionProvider.getCon();
		
		PreparedStatement userps = con.prepareStatement("select id from users;");
		
		ResultSet users = userps.executeQuery();
		
		ArrayList<ScheduledFuture<?>> futures = new ArrayList<>();
		
		while(users.next()) {
			
			final var user_id = users.getLong("id");
			
			futures.add(scheduler.schedule(() -> {

				try {
					
					step(user_id);
				
				} catch (Exception e) {
					e.printStackTrace();
				}

			}, 0, TimeUnit.SECONDS));
			
		}
		
		for(var future : futures) {
			future.get();
		}
		
	}
	
}
