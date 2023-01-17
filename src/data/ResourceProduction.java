package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.ConnectionProvider;

public class ResourceProduction {
	public final long user_id;
	public final Resource resource;
	public long count;
	public long production;
	public long research_cost;
	public long research;
	public final boolean empty;
	
	ResourceProduction(long user_id, Resource resource) {
		this.user_id = user_id;
		this.resource = resource;
		count = 0;
		production = 0;
		research_cost = 0;
		research = 0;
		empty = true;
	}
	
	ResourceProduction(ResultSet rs) throws Exception {
		user_id = rs.getLong("user_id");
		resource = Resource.get(rs.getInt("resource"));
		count = rs.getLong("count");
		production = rs.getLong("production");
		research_cost = rs.getLong("research_cost");
		research = rs.getLong("research");
		empty = false;
	}
	
	public void addProduction() throws Exception {
		production++;
		update();
	}
	
	public void update() throws Exception {
		Connection con = ConnectionProvider.getCon();
		PreparedStatement ps;
		if (empty) {
			ps = con.prepareStatement("insert into production (user_id, resource, count, production, research_cost, research) values (?,?,?,?,?,?);");
			ps.setLong(1, user_id);
			ps.setInt(2, resource.getID());
			ps.setLong(3, count);
			ps.setLong(4, production);
			ps.setLong(5, research_cost);
			ps.setLong(6, research);
		}
		else {
			ps = con.prepareStatement("update production set count=?, production=?, research_cost=?, research=? where user_id=? and resource=?;");
			ps.setLong(1, count);
			ps.setLong(2, production);
			ps.setLong(3, research_cost);
			ps.setLong(4, research);
			ps.setLong(5, user_id);
			ps.setInt(6, resource.getID());
		}

		ps.executeUpdate();
	}
	
	public int getProductionCost() {
		return 10000;
	}
	
}
