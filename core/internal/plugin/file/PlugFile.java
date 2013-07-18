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
    

package core.internal.plugin.file;

import core.internal.plugin.PlugExtension;
import core.internal.plugin.PlugInDescriptor;




/**
 * è la classe che gestisce le indicazioni relativi alla page per la classe PlugFile
 * @author michele
 * Charmy plug-in
   <extension
		 point="file">
	  <objectContribution
			objectClass="charmy.init.resources.IFile"
			nameFilter="*.zargo"
			id="charmy.contribution">
		 <action
			   label="Convert to Zargo"
			   class_save="charmy.SaveAction"
			   class_open="charmy.OpenAction">
		 </action>
	  </objectContribution>
   </extension>
 *
 ***/


/**
 * descrizione del plug-in relativo ai files, 
 * ogni plug in al massimo ne puo avere 1
 * @author michele
 * Charmy plug-in
 **/
public class PlugFile extends PlugExtension {

	
	PlugInDescriptor pluginDescriptor;

	/**
	 * stringa rappresentante la classe di entrata per l'inizializzazione
	 */
	private String   objectClass = "";
	
	/**
	 * estenzione gestita dal filtro
	 */
	private String   nameFilter ="";
	/**
	 * identificatore univoco 
	 */
	private String   id ="";
	
	/**
	 * descrizione del filtro
	 */
	private String   label ="";
	
	/**
	 * classe di salvataggio
	 */
	//private String	classSave ="";
	
	/**
	 * classe di apertura
	 */
//	private String	classOpen ="";
	private String	classFile ="";
	
	
	/**
	 * costruttore
	 * @param pep plug file page
	 */
	public PlugFile(){
		super();
		this.setClassPoint("file");
	}
	
 



	/**
	 * identificativo globale del plug
	 * @return id del plugin
	 */
	public String getId() {
		return id;
	}

	/**
	 * descrizione aggiuntiva del plugin
	 * @return 
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * filtro per l'apertura del file del tipo "*.ppp"
	 * @return stringa per il filtro dei files
	 */
	public String getNameFilter() {
		return nameFilter;
	}

	/**
	 * entry point per l'inizializzazione del modulo
	 * @return null se non vi è necessità di inizializzare il modulo
	 */
	public String getObjectClass() {
		return objectClass;
	}

	

	/**
	 * vedi <code>getId()</code>
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

	/**
	 * @param string
	 */
	public void setLabel(String string) {
		label = string;
	}

	/**
	 * @param string
	 */
	public void setNameFilter(String string) {
		nameFilter = string;
	}

	/**
	 * @param string
	 */
	public void setObjectClass(String string) {
		objectClass = string;
	}


	public String toString(){
		String fine = "[" + this.getClass().getName() + "]\r\n" + "\r\n objectClass: " + objectClass 
		                     +  "\r\n nameFilter: " + nameFilter +   "\r\n id: " + id +  "\r\n label " + label 
		                     + "\r\n classFile: " + classFile;
		return fine;
	}



	public PlugInDescriptor getPluginDescriptor() {
		return pluginDescriptor;
	}



	public void setPluginDescriptor(PlugInDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}





	public String getClassFile() {
		return classFile;
	}





	public void setClassFile(String classFile) {
		this.classFile = classFile;
		this.pluginDescriptor.getLibrary().setPluginFile(classFile);
		
	}


}



