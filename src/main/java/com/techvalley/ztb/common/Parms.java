package com.techvalley.ztb.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Parms {
	private static String ip;    
    private static String port; 
    private static String project;
    private static String url;
    
    static  {    
        Properties prop =  new  Properties();    
        InputStream in = Parms.class.getClassLoader().getResourceAsStream("web-servlet.properties");   
         try  {    
            prop.load(in);    
            ip = prop.getProperty( "ip" ).trim();    
            port = prop.getProperty( "port" ).trim();    
            project = prop.getProperty( "project" ).trim(); 
            url = ip+":"+port+project;
        }  catch  (IOException e) {    
            e.printStackTrace();    
        }    
    }    
    
	public static String getIp() {
		return ip;
	}
	public static void setIp(String ip) {
		Parms.ip = ip;
	}
	public static String getPort() {
		return port;
	}
	public static void setPort(String port) {
		Parms.port = port;
	}
	public static String getProject() {
		return project;
	}
	public static void setProject(String project) {
		Parms.project = project;
	}
	public static String getUrl() {
		return url;
	}
	public static void setUrl(String url) {
		Parms.url = url;
	}
    
	public static void  main(String args[]){    
        System.out.println(ip);    
        System.out.println(port);    
        System.out.println(project);    
        System.out.println(url);    
    }   
	
}
