package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

public class Livre {

	private String Id;
	private String Titre;
	private String Author;
	private String Date;
	private String Language;
	private HashMap<String, Integer> Counter;
	
	public Livre () {
		this.Counter = new HashMap<String, Integer>();
	}
	
	public Livre (String id) {
		Id = id;
		Counter = new HashMap<String, Integer>();
	}

	public String toString() {
		String result = "Id: " + Id + "\n";
		result += "Title: " + Titre + "\n";
		result += "Author: " + Author + "\n";
		result += "Release Date: " + Date + "\n";
		result += "Language: " + Language + "\n";
		return result;	
	}

	public void insertLivre() throws Exception {

		Connection con = ConnectionProvider.getCon();

		PreparedStatement ps=con.prepareStatement("INSERT IGNORE INTO Livre (Id, Titre, Author, Date, Language) VALUES (?,?,?,?,?)");
		ps.setString(1, Id);
		ps.setString(2, Titre);
		ps.setString(3, Author);
		ps.setString(4, Date);
		ps.setString(5, Language);

		ps.execute();
		
	}

	public void updateLivre() throws Exception {

		Connection con = ConnectionProvider.getCon();

		PreparedStatement ps=con.prepareStatement("UPDATE Livre SET Titre=?,Author=?,Date=?,Language=? WHERE Id=?");
		ps.setString(1, Titre);
		ps.setString(2, Author);
		ps.setString(3, Date);
		ps.setString(4, Language);
		ps.setString(5, Id);

		ps.execute();

	}

	public void insertAllOccurences() throws Exception {
		
		Connection con = ConnectionProvider.getCon();

		PreparedStatement ps=con.prepareStatement("INSERT IGNORE INTO Occurence (Id, Mot, Count) VALUES (?,?,?)");
		
		for(Map.Entry<String, Integer> entry : Counter.entrySet()) {

		    String mot = entry.getKey();
		    Integer count = entry.getValue();
		    ps.setString(1, Id);
			ps.setString(2, mot);
			ps.setLong(3, count);
			ps.addBatch();
			
		}
		
		ps.executeBatch();
	}

	
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
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

	public HashMap<String, Integer> getCounter() {
		return Counter;
	}

}
