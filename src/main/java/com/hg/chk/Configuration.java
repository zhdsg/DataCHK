package com.hg.chk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class Configuration {
	
	public static Properties prop =new Properties();
	static {
		
		try {
			InputStream is =Configuration.class.getClassLoader().getResourceAsStream("my.properties");
			prop.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getProperty(String key) {
		return prop.getProperty(key);
	}
	public static Object[] getObjs (String key){

		String[] values =prop.getProperty(key).split(",");
		return values;
 	}
//	public static void main(String[] args) {
//		System.out.println( Configuration.getObjs("oracle.params"));
//	}
}
