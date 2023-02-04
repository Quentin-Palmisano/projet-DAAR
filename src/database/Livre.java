package database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletContext;

public class Livre {

	private int Id;
	private String Titre;
	private String Author;
	private String Date;
	private String Language;
	private double Closeness;
	private HashMap<String, Integer> Counter;
	
	public Livre () {
		this.Counter = new HashMap<String, Integer>();
	}
	
	public Livre (int id) {
		Id = id;
		Counter = new HashMap<String, Integer>();
	}

	public Livre(ResultSet rs) throws Exception {
		Id = rs.getInt("Id");
		Titre = rs.getString("Titre");
		Author = rs.getString("Author");
		Date = rs.getString("Date");
		Language = rs.getString("Language");
	}

	public String toString() {
		String result = "Id: " + Id + "\n";
		result += "Title: " + Titre + "\n";
		result += "Author: " + Author + "\n";
		result += "Release Date: " + Date + "\n";
		result += "Language: " + Language + "\n";
		return result;		
	}
	
	public static Livre get(int id) throws Exception {
		Connection con = ConnectionProvider.getCon();
		
		PreparedStatement ps=con.prepareStatement("SELECT Livre.Id, Livre.Titre, Livre.Author, Livre.Date, Livre.Language FROM Livre "
				+ "WHERE Livre.Id=?;");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		rs.next();
		Livre livre = new Livre(rs);
		return livre;
	}

	public void insertLivre() {
		try {
			Connection con = ConnectionProvider.getCon();
			PreparedStatement ps=con.prepareStatement("INSERT IGNORE INTO Livre (Id, Titre, Author, Date, Language, Closeness) VALUES (?,?,?,?,?,?)");
			ps.setInt(1, Id);
			ps.setString(2, Titre);
			ps.setString(3, Author);
			ps.setString(4, Date);
			ps.setString(5, Language);
			ps.setDouble(6, Closeness);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateLivre() {
		try {
			Connection con = ConnectionProvider.getCon();
			PreparedStatement ps=con.prepareStatement("UPDATE Livre SET Titre=?,Author=?,Date=?,Language=? WHERE Id=?");
			ps.setString(1, Titre);
			ps.setString(2, Author);
			ps.setString(3, Date);
			ps.setString(4, Language);
			ps.setInt(5, Id);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void insertAllOccurences() throws Exception {
		
		Connection con = ConnectionProvider.getCon();
		PreparedStatement ps=con.prepareStatement("INSERT IGNORE INTO Occurence (Id, Mot, Count) VALUES (?,?,?)");
		
		for(Map.Entry<String, Integer> entry : Counter.entrySet()) {
		    String mot = entry.getKey();
		    Integer count = entry.getValue();
		    
		    ps.setInt(1, Id);
			ps.setString(2, mot);
			ps.setLong(3, count);
			
			ps.addBatch();
			
		}
		
		ps.executeBatch();
		
	}

	public static ArrayList<Livre> searchKeywords(String keywords, String tri) throws Exception {
		
		ArrayList<Livre> livres = new ArrayList<>();
		
		Connection con = ConnectionProvider.getCon();
		PreparedStatement ps=con.prepareStatement("SELECT Livre.Id, Livre.Titre, Livre.Author, Livre.Date, Livre.Language, SUM(Occurence.Count) FROM Livre "
				+ "INNER JOIN Occurence ON Occurence.Id=Livre.Id WHERE Occurence.Mot=? AND Occurence.Count > 0 GROUP BY Livre.Id " + getOrder(tri) + ";");
		ps.setString(1, keywords);
		ResultSet rs = ps.executeQuery();

		while(rs.next()) {
			livres.add(new Livre(rs));
		}
		
		return livres;
	}
	
	public static ArrayList<Livre> searchKeywords(ArrayList<String> keywords, String tri) throws Exception {
		
		ArrayList<Livre> livres = new ArrayList<>();
		
		Connection con = ConnectionProvider.getCon();
		String requete = "SELECT Livre.Id, Livre.Titre, Livre.Author, Livre.Date, Livre.Language, SUM(Occurence.Count) FROM Livre "
				+ "INNER JOIN Occurence ON Occurence.Id=Livre.Id WHERE Occurence.Count > 0 AND (Occurence.Mot=? ";
		for(int i=1; i<keywords.size()-1; i++) {
			requete += "OR Occurence.Mot=? ";
		}
		requete += ") GROUP BY Livre.Id " + getOrder(tri) + ";";
		
		PreparedStatement ps=con.prepareStatement(requete);
		
		int i = 1;
		for(String s : keywords) {
			ps.setString(i++, s);
		}
		
		ResultSet rs = ps.executeQuery();

		while(rs.next()) {
			livres.add(new Livre(rs));
		}
		
		return livres;
	}
	
	public static ArrayList<Livre> searchRegex(String regex, String tri) throws Exception {
		
		ArrayList<Livre> livres = new ArrayList<>();
		
		Connection con = ConnectionProvider.getCon();
		PreparedStatement ps=con.prepareStatement(
				"SELECT Livre.Id, Livre.Titre, Livre.Author, Livre.Date, Livre.Language, SUM(Occurence.Count) FROM Livre INNER JOIN Occurence "
				+ "ON Occurence.Id=Livre.Id WHERE Occurence.Mot REGEXP ? AND Occurence.Count > 0 GROUP BY Livre.Id " + getOrder(tri) + ";");
		ps.setString(1, regex);
		ResultSet rs = ps.executeQuery();

		while(rs.next()) {
			livres.add(new Livre(rs));
		}
		
		return livres;
	}
	
	private static String getOrder(String tri) {
		String order = "";
		if(tri.equals("occ")) {
			order = "ORDER BY SUM(Occurence.Count) DESC";
		}else if(tri.equals("jacc")){
			order = "ORDER BY Livre.Closeness DESC";
		}
		return order;
	}
	
	public ArrayList<Livre> getSuggestions() throws Exception {
		
		ArrayList<Livre> livres = new ArrayList<>();
		
		Connection con = ConnectionProvider.getCon();
		PreparedStatement ps=con.prepareStatement("SELECT l.Id, l.Titre, l.Author, l.Date, l.Language FROM Livre AS l "
				+ "INNER JOIN Distance AS d ON (d.Id1=? AND d.Id2=l.Id) OR (d.Id2=? AND d.Id1=l.Id) ORDER BY d.Dist ASC LIMIT 10");
		// pas besoin de parenthèses
		ps.setInt(1, Id);
		ps.setInt(2, Id);
		
		var rs = ps.executeQuery();
		
		while(rs.next()) {
			livres.add(new Livre(rs));
		}
		
		return livres;
	}
	
	private static final String START = "***";
	public String getText(ServletContext context) throws Exception {
		StringBuilder text = new StringBuilder();
		
		var in = context.getResourceAsStream("/WEB-INF/books/"+Id+".txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		boolean started = false;
		String line;
		while((line = br.readLine()) != null) {
			
			if(!started && line.contains(START)) {
				started = true;
				continue;
			}
			
			if(started) {
				text.append(line + "<br/>");
			}
			
		}
		return text.toString();
	}
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getTitre() {
		return Titre;
	}

	public void setTitre(String titre) {
		Titre = titre;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public double getCloseness() {
		return Closeness;
	}

	public void setCloseness(double closeness) {
		Closeness = closeness;
	}

	public HashMap<String, Integer> getCounter() {
		return Counter;
	}

}
