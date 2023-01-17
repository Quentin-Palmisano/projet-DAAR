package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import database.ConnectionProvider;
import database.PasswordHash;
import exception.NotEnoughMoneyException;

public class User {
	
	private static final long startingMoney = 100000;
	
	public final long id;
	public final String name;
	public long money;
	private Production production;
	private Offers offers;
	
	public User(long id, String name, long money) {
		this.id = id;
		this.name = name;
		this.money = money;
		
	}
	
	public static User create(ResultSet rs) throws Exception {
		var id = rs.getLong("id");
		return new User(id, rs.getString("user"), rs.getLong("money"));
		
	}
	
	public static User create(long id) throws Exception {
		
		Connection con = ConnectionProvider.getCon();
		
		PreparedStatement ps=con.prepareStatement("select * from users where id=?;");
		ps.setLong(1, id);
		
		ResultSet rs=ps.executeQuery();
		
		if(!rs.next()) throw new SQLException("No user with this id");
		
		return create(rs);
		
	}
	
	private static void connect(HttpSession session, long id) {
		
		session.setAttribute("id", id);
		
	}
	
	public static void disconnect(HttpSession session) {
		session.removeAttribute("id");
	}
	
	public static boolean isConnected(HttpSession session) throws Exception {
		
		var id = session.getAttribute("id");
		if(id == null) return false;
		
		Connection con = ConnectionProvider.getCon();
		
		PreparedStatement ps=con.prepareStatement("select * from users where id=?;");
		ps.setLong(1, (long) id);
		
		ResultSet rs=ps.executeQuery();
		
		if(!rs.next()) return false;
		
		return true;
		
	}
	
	public static long getConnectedID(HttpSession session) throws Exception {
		if(!isConnected(session)) {
			throw new Exception("Not connected, cannot retrieve ID");
		}
		return (long) session.getAttribute("id");
	}
	
	public static User getConnected(HttpSession session) throws Exception {
		try {
			return User.create(getConnectedID(session));
		} catch(SQLException e) {
			disconnect(session);
			return null;
		}
	}
	
	public static void login(HttpServletRequest request) throws Exception {
		
		String user = request.getParameter("user");
		if(user == null || user == "") throw new InputMismatchException("Username not set");
		
		String pass = request.getParameter("pass");
		if(pass == null || pass == "") throw new InputMismatchException("Password not set");
		var hash = PasswordHash.hash(pass);
		
		Connection con = ConnectionProvider.getCon();
		
		PreparedStatement ps=con.prepareStatement("select * from users where user=? and pass=?;");
		ps.setString(1, user);
		ps.setBytes(2, hash);
		
		ResultSet rs=ps.executeQuery();
		
		if(!rs.next()) throw new SQLException("No user with this username/password");
		
		long id = rs.getLong("id");
		
		connect(request.getSession(), id);
		
	}
	
	public static void signup(HttpServletRequest request) throws Exception {
		
		try {
			login(request);
		} catch(Exception e) {}
		
		if(isConnected(request.getSession())) {
			return;
		}
		
		String user = request.getParameter("user");
		if(user == null || user == "") throw new InputMismatchException("Username not set");
		
		String pass = request.getParameter("pass");
		if(pass == null || pass == "") throw new InputMismatchException("Password not set");
		var hash = PasswordHash.hash(pass);
		
		Connection con = ConnectionProvider.getCon();
		
		PreparedStatement ps = con.prepareStatement("insert into users (user, pass, money) values (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, user);
		ps.setBytes(2, hash);
		ps.setLong(3, startingMoney);
		
		int rows = ps.executeUpdate();
		
		if(rows == 0) throw new SQLException("User creation failed, no changes.");
		
		try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
        		connect(request.getSession(), id);
            }
            else {
                throw new SQLException("User creation failed, no ID obtained.");
            }
        }
		
	}
	
	public static void logout(HttpServletRequest request) {
		disconnect(request.getSession());
	}
	
	public Production getProduction() throws Exception {
		if(production == null) production = Production.create(id);
		return production;
	}
	
	public Offers getOffers() throws Exception {
		if(offers == null) offers = Offers.create(id);
		return offers;
	}
	
	public long getId() {
		return id;
	}
	public String getUsername() {
		return name;
	}
	
	public long getMoney() {
		return money;
	}
	
	public static String firstUser;
	public static long firstMoney;
	
	public static void getFirst() throws Exception {
		Connection con = ConnectionProvider.getCon();
		var ps = con.prepareStatement("select user, money from users order by money desc;");
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		firstUser = rs.getString("user");
		firstMoney = rs.getLong("money");
	}
	
	public void update() throws Exception {
		
		Connection con = ConnectionProvider.getCon();
		
		// not finished
		var ps = con.prepareStatement("update users set money=? where id=?;");
		ps.setLong(1, money);
		ps.setLong(2, id);
		
		int rows = ps.executeUpdate();
		
		if(rows == 0) throw new SQLException("No updates");
		
	}

	public void pay(long price) throws Exception {
		if(price > money) {
			throw new NotEnoughMoneyException("Not enough money ; current: " + money + ", price" + price);
		}
		this.money = money - price;
		update();
	}
	
	
}
