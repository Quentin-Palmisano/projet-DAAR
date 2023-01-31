package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class parseLivre {

	private static final String TITRE = "Title: ";
	private static final String AUTHOR = "Author: ";
	private static final String DATE = "Release Date: ";
	private static final String LANGUAGE = "Language: ";
	private static final String START = "***";
	public static int i = 1;

	private static HashMap<String, Livre> data = new HashMap<String, Livre>();

	public static void readFile(File file) {
		try {
			FileReader fr = new FileReader(file);     
			BufferedReader br = new BufferedReader(fr);
			String line;
			boolean count = false;
			Livre livre = new Livre();
			livre.Id = file.getName().substring(0, file.getName().lastIndexOf('.'));
			while((line = br.readLine()) != null) {

				if(line.length()>3) {
					count = START.equals(line.substring(0, 3)) && START.equals(line.substring(line.length()-3));
				}

				count = true;

				if(line.length() > TITRE.length() && TITRE.equals(line.substring(0, TITRE.length()))){
					livre.Titre = line.substring(TITRE.length());
				}else if(line.length() > AUTHOR.length() && AUTHOR.equals(line.substring(0, AUTHOR.length()))){
					livre.Author = line.substring(AUTHOR.length());
				}else if(line.length() > DATE.length() && DATE.equals(line.substring(0, DATE.length()))){
					if(line.indexOf('[') == -1) {
						livre.Date = line.substring(DATE.length());
					}else {
						livre.Date = line.substring(DATE.length(), line.indexOf('['));
					}
				}else if(line.length() > LANGUAGE.length() && LANGUAGE.equals(line.substring(0, LANGUAGE.length()))){
					livre.Language = line.substring(LANGUAGE.length());
				}

				if(count) {
					String[] tab = line.split("\\W+");
					for(String mot : tab) {
						if(livre.counter.containsKey(mot)) {
							livre.counter.put(mot, livre.counter.get(mot)+1);
						}else {
							livre.counter.put(mot, 1);
						}
					}
				}
			}
			fr.close();
			data.put(livre.Id, livre);
			livre.insertLivre();
			livre.updateLivre();
			livre.insertAllOccurences();
			System.out.println(i++);
			System.out.println(livre.Id);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void readFolder(File folder) {
		for (final File fileEntry : folder.listFiles()) {
			readFile(fileEntry);
		}
	}

	public static void main(String[] args) {

		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		File folder = new File("./books");
		readFolder(folder);
		//4694 3841 74 890 1250
	}

}
