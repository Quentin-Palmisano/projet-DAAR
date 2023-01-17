package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import database.ConnectionProvider;

public class Production {
	public final long user_id;
	
	private final Map<Resource, ResourceProduction> resources = new HashMap<>();
	
	public Production(long id) throws Exception {
		this.user_id = id;
	}
	
	public static Production create(long id) throws Exception {
		var prod = new Production(id);
		
		Connection con = ConnectionProvider.getCon();
		
		PreparedStatement ps = con.prepareStatement("select * from production where user_id=?;");
		ps.setLong(1, id);
		
		ResultSet rs=ps.executeQuery();
		
		while(rs.next()) {
			var rp = new ResourceProduction(rs);
			prod.resources.put(rp.resource, rp);
		}
		
		return prod;
		
	}
	
	public ResourceProduction get(Resource resource) {
		var rp = resources.get(resource);
		if(rp == null) {
			return new ResourceProduction(user_id, resource);
		}
		return rp;
	}

	public void update() throws Exception {
		
		for(var res : resources.keySet()) {
			var rp = resources.get(res);
			
			rp.update();
		}
		
	}
	
}
