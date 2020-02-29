package utils;

import java.util.HashMap;

public class ConfigUtils {
	
	public static HashMap<String,String> readConfigFile(String filePath){
		
		HashMap<String,String> dictionary = new HashMap<String,String>();
		
		try {
			String[] lines = FileUtils.readLines(filePath);
			
			for(String line : lines) {
				if(!line.startsWith("#") && line.contains(":")) {
					String key = line.substring(0,line.indexOf(":")).trim();
					String value = line.substring(line.indexOf(":")+1,line.length()).trim();
					dictionary.put(key, value);
				}
			}
			
		} catch (Exception e) {}
		
		return dictionary;
	}
	
}
