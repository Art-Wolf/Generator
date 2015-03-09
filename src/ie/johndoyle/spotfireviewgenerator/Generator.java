package ie.johndoyle.spotfireviewgenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Generator{ 
	public static void main(String args[]) { 
		String filepath = "/Users/johndoyle/";
		Path path = Paths.get(filepath, "data.txt");
		
		List<InfoLink> infoLinks = new ArrayList<InfoLink>(); 
		
		try(Stream<String> lines = Files.lines(path)){
			InfoLink il = new InfoLink(filepath);
			
			lines
				.map(s -> s.split(" "))
				.flatMap(Arrays::stream)
				.forEach(s ->  il.addColumn(s));
			
			infoLinks.add(il);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		infoLinks
			.stream()
			.filter(i -> i.checkFileExists())
			.filter(i -> i.checkParameters() < 2)
			.forEach(i -> i.createScript());
	} 
} 

class InfoLink {

	private final String filepath;
	private String name; 
	
	List<String> columns = new ArrayList<String>();
	
	public InfoLink(String filepath) {
		this.filepath = filepath;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addColumn(String column) {
		if (this.name.isEmpty()) {
			setName(column);
		} else {
			columns.add(column);
		}
	}
	
	public boolean checkFileExists() {
		return new File(filepath).exists();
	}
	
	public long checkParameters() {
		
		Path path = Paths.get(filepath, name + ".sql");
		
		try(Stream<String> lines = Files.lines(path)){
			return lines
					.filter(s->s.contains("<conditions>") || s.contains("?"))
					.count();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return 0;
	}
	
	public void createScript() {
		// do stuff here
	}
}
