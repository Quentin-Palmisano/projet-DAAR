package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class parseLivre {

	private static final String TITRE = "Title: ";
	private static final String AUTHOR = "Author: ";
	private static final String DATE = "Release Date: ";
	private static final String LANGUAGE = "Language: ";
	private static final String START = "***";

	private static HashMap<String, Livre> data = new HashMap<String, Livre>();

	public static void readFile(File file) {
		try {
			FileReader fr = new FileReader(file);     
			BufferedReader br = new BufferedReader(fr);
			String line;
			boolean count = false;
			Livre livre = new Livre();
			livre.setId(file.getName().substring(0, file.getName().lastIndexOf('.')));
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
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void readFolder(File folder) {
		for (final File fileEntry : folder.listFiles()) {
			readFile(fileEntry);
		}
		System.out.println("All books have been parsed");
	}
	
	public static void insertInDB() {
		for(Map.Entry<String, Livre> entry : data.entrySet()) {
		    String Id = entry.getKey();
		    Livre livre = entry.getValue();
		    
			livre.insertLivre();
			livre.insertAllOccurences();			
			System.out.println("Book " + Id + " has been insert in the DB");
		}
		System.out.println("All books have been insert in the DB");
	}
	
	public static void updateBooksDB() {
		for(Map.Entry<String, Livre> entry : data.entrySet()) {
		    String Id = entry.getKey();
		    Livre livre = entry.getValue();	
		    
			livre.updateLivre();			
			System.out.println("Book " + Id + " has been updated in the DB");		  
		}
		System.out.println("All books have been updated in the DB");
	}

	public static void main(String[] args) {
		
		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		File folder = new File("./books");
		readFolder(folder);
		
		insertInDB();
	}

}
