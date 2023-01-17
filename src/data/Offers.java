package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.ConnectionProvider;

public class Offers {
	public final long user_id;
	
	public final Map<Resource, ArrayList<Offer>> offers = new HashMap<>();
	
	public Offers(long id) throws Exception {
		this.user_id = id;
	}
	
	public static Offers create(long id) throws Exception {
		var offers = new Offers(id);
		
		Connection con = ConnectionProvider.getCon();
		
		PreparedStatement ps = con.prepareStatement("select * from offers as o inner join users as u on u.id = o.user_id where user_id=?;");
		ps.setLong(1, id);
		
		ResultSet rs=ps.executeQuery();
		
		while(rs.next()) {
			var offer = Offer.create(rs);
			offers.add(offer);
		}
		
		return offers;
		
	}
	
	public void add(Offer offer) {
		var al = get(offer.resource);
		if(al.size() == 0) {
			offers.put(offer.resource, al);
		}
		al.add(offer);
	}
	
	public void insert(Offer offer) throws Exception {
		add(offer);
		offer.insert();
	}
	
	public ArrayList<Offer> get(Resource resource) {
		var al = offers.get(resource);
		if(al == null) {
			return new ArrayList<>();
		}
		return al;
	}

	public void update() throws Exception {
		
		for(var res : offers.keySet()) {
			var al = offers.get(res);
			
			for(var offer : al) {
				offer.update();
			}
		}
		
	}
	
	public ArrayList<Offer> search(Offer search) throws Exception {
		ArrayList<Offer> searched = new ArrayList<>();
		
		Connection con = ConnectionProvider.getCon();
		
		{
			PreparedStatement ps = con.prepareStatement("select * from offers as o inner join users as u on u.id = o.user_id where user_id=? and resource=? and buy=?;");
			ps.setLong(1, user_id);
			ps.setInt(2, search.resource.getID());
			ps.setBoolean(3, search.buy);
			
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				var offer = Offer.create(rs);
				searched.add(offer);
			}
		}
		
		{
			PreparedStatement ps = con.prepareStatement("select * from offers as o inner join users as u on u.id = o.user_id where resource=? and buy=? and user_id!=? order by price " + (search.buy ? "asc" : "desc") + " limit 10;");
			ps.setInt(1, search.resource.getID());
			ps.setBoolean(2, !search.buy);
			ps.setLong(3, user_id);
			
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				var offer = Offer.create(rs);
				searched.add(offer);
			}
		}
		
		return searched;
	}
	
}
