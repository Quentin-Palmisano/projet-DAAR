package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

public class parseLivre {

	private static final String TITRE = "Title: ";
	private static final String AUTHOR = "Author: ";
	private static final String DATE = "Release Date: ";
	private static final String LANGUAGE = "Language: ";
	private static final String START = "***";

	static final int maxFiles = 100; // 0 is all books
	
	private static HashMap<Integer, Livre> data = new HashMap<>();

	public static void readFile(File file) {
		try {
			FileReader fr = new FileReader(file);     
			BufferedReader br = new BufferedReader(fr);
			String line;
			boolean count = false;
			Livre livre = new Livre();
			String filename = file.getName().substring(0, file.getName().lastIndexOf('.'));
			int id = Integer.parseInt(filename);
			livre.setId(id);
			while((line = br.readLine()) != null) {

				if(line.length()>3) {
					count = START.equals(line.substring(0, 3)) && START.equals(line.substring(line.length()-3));
				}

				count = true;

				if(line.length() > TITRE.length() && TITRE.equals(line.substring(0, TITRE.length()))){
					livre.setTitre(line.substring(TITRE.length()));
				}else if(line.length() > AUTHOR.length() && AUTHOR.equals(line.substring(0, AUTHOR.length()))){
					livre.setAuthor(line.substring(AUTHOR.length()));
				}else if(line.length() > DATE.length() && DATE.equals(line.substring(0, DATE.length()))){
					if(line.indexOf('[') == -1) {
						livre.setDate(line.substring(DATE.length()));
					}else {
						livre.setDate(line.substring(DATE.length(), line.indexOf('[')));
					}
				}else if(line.length() > LANGUAGE.length() && LANGUAGE.equals(line.substring(0, LANGUAGE.length()))){
					livre.setLanguage(line.substring(LANGUAGE.length()));
				}

				if(count) {
					String[] tab = line.split("\\W+");
					for(String mot : tab) {
						if(livre.getCounter().containsKey(mot)) {
							livre.getCounter().put(mot, livre.getCounter().get(mot)+1);
						}else {
							livre.getCounter().put(mot, 1);
						}
					}
				}
			}
			fr.close();
			
			data.put(livre.getId(), livre);
			System.out.println("book " + livre.getId() + " has been parsed");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void readFolder(File folder) {
		int i = 0;
		for (final File fileEntry : folder.listFiles()) {
			if(i==maxFiles) break;
			readFile(fileEntry);
			i++;
		}
		System.out.println("All books have been parsed");
	}
	
	public static void insertInDB() {
		for(Map.Entry<Integer, Livre> entry : data.entrySet()) {
		    int Id = entry.getKey();
		    Livre livre = entry.getValue();
		    
			try {
				livre.insertLivre();
				livre.insertAllOccurences();
				System.out.println("Book " + Id + " has been inserted in the DB");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("All books have been insert in the DB");
	}
	
	public static void updateBooksDB() throws Exception {
		for(Map.Entry<Integer, Livre> entry : data.entrySet()) {
		    int Id = entry.getKey();
		    Livre livre = entry.getValue();
		    
			livre.updateLivre();
			System.out.println("Book " + Id + " has been updated in the DB");
		}
		System.out.println("All books have been updated in the DB");
	}
	
	public static void calculateDistances() throws Exception {
		
		Connection con = ConnectionProvider.getCon();
		PreparedStatement ps = con.prepareStatement("INSERT INTO Distance VALUES (?,?,?)");
		
		for(Livre livre1 : data.values()) {
			for(Livre livre2 : data.values()) {
				if(livre1.getId() >= livre2.getId()) continue;
				double top = 0;
				double bottom = 0;
				
				for(Map.Entry<String, Integer> entry : livre1.getCounter().entrySet()) {
					String w = entry.getKey();
					if(livre2.getCounter().containsKey(w)) {
						int k1 = entry.getValue();
						int k2 = livre2.getCounter().get(w);
						
						int max = Math.max(k1, k2);
						int min = Math.min(k1, k2);
						top += max - min;
						bottom += max;
					}
				}
				
				double distance = top/bottom;
				
				ps.setInt(1, livre1.getId());
				ps.setInt(2, livre2.getId());
				ps.setDouble(3, distance);
				
				ps.addBatch();
				
			}
			System.out.println("Book " + livre1.getId() + " distances have been calculated");
		}
		
		ps.executeBatch();
	}

	public static void calculateCloseness() throws Exception {
		Connection con = ConnectionProvider.getCon();
		
		for(Livre livre : data.values()) {
			PreparedStatement ps = con.prepareStatement("SELECT SUM(Dist) AS sum FROM Distance WHERE (Id1=? or Id2=?) and Id1!=Id2");
			
			ps.setInt(1, livre.getId());
			ps.setInt(2, livre.getId());
			
			var rs = ps.executeQuery();
			
			rs.next();
			
			double sum = rs.getDouble("sum");
			double crank = (data.size() - 1)/sum;
			
			livre.setCloseness(crank);
			
		}

		System.out.println("Closeness ranking was calculated");
	}
	
	public static void main(String[] args) {
		
		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		File folder = new File("./books");
		readFolder(folder);
		
		try {
			calculateDistances();
			calculateCloseness();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//bad files
		//4694 3841 74 890 1250 2194
		
		insertInDB();

	}

}
