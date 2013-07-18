/*   Charmy (CHecking Architectural Model consistencY)
 *   Copyright (C) 2004 Patrizio Pelliccione <pellicci@di.univaq.it>,
 *   Henry Muccini <muccini@di.univaq.it>, Paola Inverardi <inverard@di.univaq.it>. 
 *   Computer Science Department, University of L'Aquila. SEA Group. 
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
    

package core.internal.datistatici;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;


/**
 * classe contentente informazioni riguardnti la struttura files e
 * costanti di Charmy nonche alcune funzioni comuni riguardanti i files
 * @author michele
 * Charmy plug-in
 **/
public class CharmyFile { 

	/**
	 * diciture per frame
	 */
	public static String DESCRIZIONE = "Charmy 2.0 - BetaVersion";
	
	public static String LOGO = "charmyLogo.gif";
	
	public static String ABOUT_IMAGE = "charmySplash.jpeg";
	
	/**
	 * directory di plug in
	 */
	public static String PLUGIN = "plugin";
	
	/**
	 * directory contenente le features
	 */
	public static String FEATURES = "features";
	
		
	
	/**
	 * nome del file di plugin
	 */
	public static String PLUGIN_FILENAME = "plugin.xml";
	
	/**
	 * nome del file di feature
	 */
	public static String FEATURE_FILENAME = "feature.xml";
	
	
	/**
	 * preleva la directory di lavoro
	 * @return directory di lavoro
	 */
	public static String WorkDir() {
		return System.getProperty("user.dir");
	}
	
	/**
	 * restituisce il file separator
	 * @return directory di lavoro
	 */
	public static String FileSeparator() {
			return File.separator;
	}
	
	
	/**
	 * aggiunge se necessario un ifle separator alla fine di dir
	 * @param dir stringa rappresentante una directory
	 * @return directory con fileSeparator
	 */
	public static  String addFileSeparator(String dir){
		if(  ! dir.endsWith(CharmyFile.FileSeparator())) {
			dir = dir.concat(CharmyFile.FileSeparator());
		}
		return dir;
	}


	/**
	 * preleva la directory dei plugin
	 * @return
	 */
	public static String dirPlug(){
		String dir = CharmyFile.WorkDir();
		dir = CharmyFile.addFileSeparator(dir);
		dir = dir.concat(CharmyFile.PLUGIN);
		return CharmyFile.addFileSeparator(dir);
		//return dir;
	}

	/**
	 * preleva la directory delle features
	 * @return
	 */
	public static String dirFeature(){
		String dir = CharmyFile.WorkDir();
		dir = CharmyFile.addFileSeparator(dir);
		dir = dir.concat(CharmyFile.FEATURES);
		return CharmyFile.addFileSeparator(dir);
		//return dir;
	}

	
	
	public static URL createURLPath(String pathFileName, String pathJarFileName)
    {
		try{
			
		/*	String file = System.getProperty("user.dir")+pathJarFile+ '\\'+ jarFileName;
			JarFile jarFile = new JarFile(file);
			
			Enumeration entry = jarFile.entries();
			while (entry.hasMoreElements()) {
				
				Object a = entry.nextElement();
				int t =0;
			}
			
						
			int t =0;*/
		}
		catch (Exception e ){   
			
		}
		
		
		String fullPath;
        if(System.getProperty("os.name").startsWith("Wi")){
        	
        	//String a = System.getProperty("user.dir");
        	//String b = System.getProperty("user.dir").substring(2).replace('\\','/');
        	fullPath="jar:file:"+System.getProperty("user.dir").substring(2).replace('\\','/')+'/'+
        	pathJarFileName+ "!/"+pathFileName;
            
        }
        else
        	fullPath="jar:file:"+System.getProperty("user.dir")+'/'+
        	pathJarFileName+ "!/"+pathFileName;
        try{
            return new URL(fullPath);
        }
        catch(Exception e){
            System.out.println("CharmyFile.createURLPath ERROR: "+e);
            return null;
        }
    }
	
	public static URL getParent(URL url) {
		String file = url.getFile();
		int len = file.length();
		if (len == 0 || len == 1 && file.charAt(0) == '/')
			return null;
		int lastSlashIndex = -1;
		for (int i = len - 2; lastSlashIndex == -1 && i >= 0; --i){
			if (file.charAt(i) == '/')
				lastSlashIndex = i;
		}
		if (lastSlashIndex == -1)
			file = ""; //$NON-NLS-1$
		else
			file = file.substring(0, lastSlashIndex + 1);

		try {
			url = new URL(url.getProtocol(), url.getHost(), url.getPort(), file);
		} catch(MalformedURLException e){
			
		}
		return url;
	}
	
	public static String getLastElement(URL url){
		String file = url.getFile();
		int len = file.length();
		if (len == 0 || len == 1 && file.charAt(0) == '/')
			return null;

		int lastSlashIndex = -1;
		for(int i = len - 2; lastSlashIndex == -1 && i >= 0; --i) {
			if (file.charAt(i) == '/')
				lastSlashIndex = i;
		}
		boolean isDirectory = file.charAt(len - 1) == '/';
		if (lastSlashIndex == -1) {
			if (isDirectory){
				return file.substring(0, len - 1);
			} else {
				return file;
			}
		} else {
			if (isDirectory) {
				return file.substring(lastSlashIndex + 1, len - 1);
			} else {
				return file.substring(lastSlashIndex + 1, len);
			}
		}
	}
	
	public static Vector getElements(URL url){
		Vector result = new Vector(5);
		String lastElement = null;
		while((lastElement = getLastElement(url)) != null){
			result.insertElementAt(lastElement, 0);
			url = getParent(url);
		}
		return result;
	}

}
