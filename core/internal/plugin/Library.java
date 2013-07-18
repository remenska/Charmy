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

package core.internal.plugin;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import core.internal.datistatici.CharmyFile;

/**
 * Classe relativa al plug-in manager, contiene le librerie richieste
 * dal plugin attraverso il files plugin.xml.
 * Le librerie vanno caricate nel classloader per poter utilizzare l'aggiunta,
 * questo avviene restituendo un array di URL da passare al class loader
 * @author michele
 * Charmy plug-in
 **/
public class Library {

	/**
	 * desrittore del plugin dove è contenuta la classe plugRequired
	 */
	private PlugInDescriptor pluginDescriptor = null;

	private URL[] externalLibrary = null;

	private URL libraryPlugEditor = null;

	private URL libraryPluginFile = null;
	
//	private URL libraryPlugFileOpen = null;

	//private URL libraryPlugFileSave = null;

	/**
	 * costruttore 
	 */
	public Library(PlugInDescriptor pd) {
		super();
		pluginDescriptor = pd;

	}

	/**
	 * @return ritorna il contenuto del vettore come vettori di url concatenato con le directory
	 *    altrimenti ritorna null 
	 */
	/*public URL[] URLs() {
	 if (size() == 0) {
	 return null;
	 }

	 String dir = CharmyFile.addFileSeparator(pluginDescriptor
	 .getDirectory());
	 URL url[] = new URL[size()];
	 for (int i = 0; i < size(); i++) {
	 String lib = dir.concat((String) get(i));

	 try {
	 url[i] = new File(lib).toURL();
	 } catch (MalformedURLException e) {
	 e.printStackTrace();
	 }

	 }
	 return url;
	 }*/

	/**
	 * prende il plugin descriptor padre
	 * @return null se non ha impostato il Descriptor
	 */
	public PlugInDescriptor getPluginDescriptor() {
		return pluginDescriptor;
	}

	/**
	 * setta il parent plugindescriptor
	 * @param descriptor
	 */
	public void setPluginDescriptor(PlugInDescriptor descriptor) {
		pluginDescriptor = descriptor;
	}

	/**
	 * rappresenta la classe mediante una stringa 
	 */
	/*public String toString(){
	 URL url[] = URLs();
	 String fine = "[" + this.getClass().getName() + "]\r\n";
	 
	 for(int i = 0 ; i < url.length; i ++ ){
	 fine = fine.concat(url[i].toString().concat("\r\n"));	
	 }
	 return fine;
	 }*/

	/**
	 * nota: dovrebbe funzionare anche su linux, ma è da provare.
	 */
	public boolean add(String pathLibraryName) {

		String[] elementPath = pathLibraryName.split("/");

		//String dir = CharmyFile.addFileSeparator(pluginDescriptor.getDirectory());

		
		//File dir = new File(this.pluginDescriptor.getDirectory());
		//String nameDir = dir.getName();

		/*String fullPath = System.getProperty("user.dir")
				+ CharmyFile.FileSeparator() + CharmyFile.PLUGIN
				+ CharmyFile.FileSeparator() + nameDir;*/
		
       String lib = pluginDescriptor.getDirectory();
		for (int i = 0; i < elementPath.length; i++) {

			lib = CharmyFile.addFileSeparator(lib);
			lib = lib.concat(elementPath[i]);
		}

		/* if(System.getProperty("os.name").startsWith("Wi")){
		 
		 //String a = System.getProperty("user.dir");
		 //String b = System.getProperty("user.dir").substring(2).replace('\\','/');
		 fullPath=System.getProperty("user.dir").substring(2).replace('\\','/')+'/'+
		 CharmyFile.PLUGIN + "/" +nameDir+ "/" + pathLibraryName;
		 
		 }
		 else
		 fullPath=System.getProperty("user.dir")+'/'+ CharmyFile.PLUGIN + "/"+nameDir+"/"+
		 pathLibraryName;*/

		URL urlLibrary = null;
		try {

			urlLibrary = new File(lib).toURL();
		} catch (Exception e) {

			System.out.println("Library plugin ERROR: "+e);
			return false;

		}

		URL[] lista = null;
		if (this.externalLibrary == null) {
			lista = new URL[1];
		} else {
			lista = new URL[this.externalLibrary.length + 1];
			System.arraycopy(externalLibrary, 0, lista, 0,
					externalLibrary.length);
		}

		lista[lista.length - 1] = urlLibrary;
		this.externalLibrary = lista;

		return true;

	}

	public URL[] getExternalLibrary() {
		return externalLibrary;
	}

	public URL getLibraryPlugEditor() {
		return libraryPlugEditor;
	}

	

	public void setPlugEditor(String entryPoint) {

		String temp1 = entryPoint.replace(".", ":");

		String[] elementPath = temp1.split(":");

		elementPath[elementPath.length - 1] = elementPath[elementPath.length - 1]
				+ ".jar";

		////////////

		String lib = pluginDescriptor.getDirectory();
		for (int i = 2; i < elementPath.length; i++) {

			lib = CharmyFile.addFileSeparator(lib);
			lib = lib.concat(elementPath[i]);
		}
						

		URL urlLibrary = null;
		try {

			urlLibrary = new File(lib).toURL();
		} catch (Exception e) {

			System.out.println("Library plugin ERROR: " + e);
			return;

		}

		//////////////////	

		//String path = entryPoint.replace('.', '/') + ".jar";
		ArrayList temp = new ArrayList();

		for (int i = 0; i < this.externalLibrary.length; i++) {

			if (externalLibrary[i].sameFile(urlLibrary))
				this.libraryPlugEditor = externalLibrary[i];
			else
				temp.add(externalLibrary[i]);

		}

		this.externalLibrary = (URL[]) temp.toArray(new URL[temp.size()]);

	}

	public void setPluginFile(String classFile) {

		String temp1 = classFile.replace(".", ":");

		String[] elementPath = temp1.split(":");

		elementPath[elementPath.length - 1] = elementPath[elementPath.length - 1]
				+ ".jar";

		////////////

		String lib = pluginDescriptor.getDirectory();
		for (int i = 2; i < elementPath.length; i++) {

			lib = CharmyFile.addFileSeparator(lib);
			lib = lib.concat(elementPath[i]);
		}
						

		URL urlLibrary = null;
		try {

			urlLibrary = new File(lib).toURL();
		} catch (Exception e) {

			System.out.println("Library plugin ERROR: " + e);
			return;

		}
		//////////////////	

		//String path = entryPoint.replace('.', '/') + ".jar";
		ArrayList temp = new ArrayList();

		for (int i = 0; i < this.externalLibrary.length; i++) {

			if (externalLibrary[i].sameFile(urlLibrary))
				this.libraryPluginFile = externalLibrary[i];
			else
				temp.add(externalLibrary[i]);

		}

		this.externalLibrary = (URL[]) temp.toArray(new URL[temp.size()]);

	}

	public URL getLibraryPluginFile() {
		return libraryPluginFile;
	}
	
}
