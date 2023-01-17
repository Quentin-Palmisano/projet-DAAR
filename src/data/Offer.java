package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.ConnectionProvider;

public class Offer {
	public long id;
	public final long user_id;
	public final String user_name;
	public final Resource resource;
	public boolean buy;
	public long price;
	public long quantity;
	
	public Offer(long user_id, Resource resource, boolean buy, long price, long quantity) {
		this(-1L, user_id, null, resource, buy, price, quantity);
	}
	
	public Offer(long id, long user_id, String user_name, Resource resource, boolean buy, long price, long quantity) {
		this.id = id;
		this.user_id = user_id;
		this.resource = resource;
		this.buy = buy;
		this.price = price;
		this.quantity = quantity;
		this.user_name = user_name;
	}
	
	public static Offer create(ResultSet rs) throws Exception {
		String user_name = rs.getString("u.user");
		return new Offer(rs.getLong("id"), rs.getLong("user_id"), user_name, Resource.get(rs.getInt("resource")), rs.getBoolean("buy"), rs.getLong("price"), rs.getLong("quantity"));
	}
	
	public void insert() throws Exception {
		
		Connection con = ConnectionProvider.getCon();
		PreparedStatement ps = con.prepareStatement("insert into offers (user_id, resource, buy, price, quantity) values (?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, user_id);
		ps.setInt(2, resource.getID());
		ps.setBoolean(3, buy);
		ps.setLong(4, price);
		ps.setLong(5, quantity);
		
		ps.executeUpdate();
		
		try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("User creation failed, no ID obtained.");
            }
        }
	}
	
	public void update() throws Exception {
		if(id == -1) throw new Exception("Doesn't exist yet");
		Connection con = ConnectionProvider.getCon();
		PreparedStatement ps = con.prepareStatement("update production set buy=?, price=?, quantity=? where id=?;");
		ps.setBoolean(1, buy);
		ps.setLong(2, price);
		ps.setLong(3, quantity);
		ps.setLong(4, id);

		ps.executeUpdate();
	}
	
	public static void delete(long id) throws Exception {
		Connection con = ConnectionProvider.getCon();
		PreparedStatement ps = con.prepareStatement("delete from offers where id=?;");
		ps.setLong(1, id);
		ps.executeUpdate();
	}
}
