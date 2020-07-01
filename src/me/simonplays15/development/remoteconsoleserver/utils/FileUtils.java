package me.simonplays15.development.remoteconsoleserver.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.simonplays15.development.remoteconsoleserver.Core;

public class FileUtils {
	
	public static void writeFile(File file, String...lines) {
		
		try(BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
			
			for(String str : lines) {
				writer.write(str);
				writer.newLine();
			}
			
			writer.flush();
			writer.close();
			
		} catch(Exception ex) {
			Core.getInstance().getLogger().log(Level.SEVERE, "An error occurred while writing the file "+file, ex);
		}
		
	}
	
	public static List<String> readFile(File file) {
		
		List<String> output = new ArrayList<String>();
		
		try(BufferedReader reader = Files.newBufferedReader(file.toPath(), Charset.forName("UTF-8"))){
			
			String val;
			while((val = reader.readLine()) != null)
				output.add(val);
			
			reader.close();
			
		} catch(Exception ex) {
			Core.getInstance().getLogger().log(Level.SEVERE, "An error occurred while reading the file "+file, ex);
		}
		
		return output;
		
	}
	
}
