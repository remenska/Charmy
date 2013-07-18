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
import java.util.ArrayList;
import java.util.Vector;

import core.internal.extensionpoint.ExtensionPointDescriptor;
import core.internal.extensionpoint.HostDescriptor;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.editoralgo.PlugEditor;
import core.internal.plugin.file.FileManager;
import core.internal.plugin.file.IFilePlug;
import core.internal.plugin.file.PlugFile;
import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.runtime.GeneralURLClassLoader;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;

/**
 * Destinzione completa del plugin, ricavata attraverso il files plugin.xml
 * @author michele
 * Charmy plug-in
 **/
public class PlugInDescriptor extends PluginModel {
	
	private PluginRegistry pluginRegistry;
	
	private int ordineDiAttivazione = -1;
	
	private HostDescriptor[] hosts = null;
	
	private ExtensionPointDescriptor[] extensionPoints = null;
	
	
	/**
	 * directory dove ? contenuto il plugin
	 */
	private String directory;
	
	/**
	 * Stringa di indicazione del plug
	 */
	private String name;
	
	/**
	 * Stringa indicante il produttore del plug-in
	 */
	private String vendor;
	
	/**
	 * classe di startup del plug-in, questa classe prepara l'ambiente all'esecuzione
	 * del plug-in 
	 */
	private String classStartup;
	
	/**
	 * Riferimento alle librerie richieste dal plug-in
	 */
	private Library library;
	
	/**
	 * ClassLoaderPrivato
	 */
	private GeneralURLClassLoader generalClassLoader = null;
	
	/**
	 * vettore contenente i plug relativi all'editor
	 */
	
	private PlugEditor plugEditor = null;
	/**
	 * vettore contenente i plug relativi ai file
	 */
	private PlugFile plugFile = null;
	
	/**
	 * Classe di lavoro
	 */
	private Class cPlug = null;
	
	/**
	 * Classe per generare l'editing
	 */
	private IMainTabPanel mainEdit = null;
	
	/**
	 * classe per la generazione del file
	 */
	//private IFilePlug saveFile = null;
	//private IFilePlug openFile = null;
	private IFilePlug pluginFile = null;
	
	private Vector infoXmlModel = null;
	
	private String[] idPlugFileRequest=null;
	
	private Vector xSchemaInfo = null;
	
	private String tagRootXMLFile = null;
	//private Vector xSchemaFileInfo=null;
	//private String classMenagementFile = null;
	
	/**
	 * ritorna la stringa relativa alla classe di startup
	 * @return identificativo classe di start
	 */
	public String getClassStartup() {
		return classStartup;
	}
	
	/**
	 * ritorna il nome del plug
	 * @return nome plug
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * costruttore
	 * @param file ? il file del plugin.xml completo di path assoluto
	 */
	public PlugInDescriptor(File file,PluginRegistry pluginRegistry) {
		directory = file.getParent();
		this.pluginRegistry=pluginRegistry;
		if (directory == null) {
			directory = "";
		}
	}
	
	/**
	 * ritorna una stringa rappresentante il costruttore del plug
	 * @return
	 */
	public String getVendor() {
		return vendor;
	}
	
	/**
	 * setta la stringa della classe di startup
	 * @param string
	 */
	public void setClassStartup(String string) {
		classStartup = string;
	}
	
	/**
	 * setta il nome del plug
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}
	
	/**
	 * @param string
	 */
	public void setVendor(String string) {
		vendor = string;
	}
	
	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}
	
	/**
	 * ritorna la directory del plugin
	 * @return
	 */
	public String getDirectory() {
		return directory;
	}
	
	/**
	 * setta la directory del plugin
	 * @param string
	 */
	public void setDirectory(String string) {
		directory = string;
	}
	
	/**
	 * ritorna una rappresentazione in formato stringa della classe
	 */
	public String toString() {
		String fine =
			"["
			+ this.getClass().getName()
			+ "]\r\n"
			+ " nameSpace: "
			+ this.identifier
			+ "\r\n directory: "
			+ directory
			+ "\r\n name: "
			+ name
			+ " \r\n version: "
			+ version
			+ "\r\n vendor: "
			+ vendor
			+ "\r\n classStartup: "
			+ classStartup
			+ "\r\n";
		
		fine = fine.concat(this.getLibrary().toString());
		return fine;
	}
	
	/**
	 * aggiunge un nuovo editor al vettore degli editor
	 * @param pe
	 */
	public void setEditor(PlugEditor pe) {
		//if(plugEditor == null){
		//	plugEditor = new Vector();
		//}
		//plugEditor.add(pe);
		plugEditor = pe;
		this.library.setPlugEditor(plugEditor.getEntryPoint());
		
	}
	
	/**
	 * aggiunge un nuovo FilePlug al vettore dei file
	 * @param pf
	 */
	public void setPlugFile(PlugFile pf) {
		plugFile = pf;
	}
	
	/**
	 * restituisce un vettore di pluginEditor
	 * @return
	 */
	//public Vector getPlugEditor() {
	//	return plugEditor;
	//}
	
	/**
	 * restituisce il  plugEditor associato
	 * @return null se non ? associato nessun plug editor al
	 * plug-in
	 */
	public PlugEditor getPlugEditor() {
		return plugEditor;
	}
	
	/**
	 * restituisce un vettore di pluginfile
	 * @return
	 */
	public PlugFile getPlugFile() {
		return plugFile;
	}
	
	/**
	 * Crea una istanza di Edit, se l'istanza ? gi? stata creata ne restituisce il puntatore
	 * @return istanza IMainTabPanel (plug di edit)
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public IMainTabPanel getIstanceEdit()
	throws
	ClassNotFoundException,
	InstantiationException,
	IllegalAccessException {
		
		if (plugEditor == null) { //il plug-in non gestisce l'editor
			return null;
		}
		
		if (mainEdit == null) {
			if (cPlug == null) {
				
				this.generalClassLoader.addURL(getLibrary().getLibraryPlugEditor());
				if (getLibrary().getExternalLibrary()!=null)
					if(getLibrary().getExternalLibrary().length!=0)
						this.generalClassLoader.addURLs(getLibrary().getExternalLibrary());
				//cPlug = new URLClassLoader()
				//String ent=plugEditor.getEntryPoint();
				cPlug = generalClassLoader.loadClass(plugEditor.getEntryPoint());
				
				mainEdit = (IMainTabPanel) cPlug.newInstance();
				
				this.pluginRegistry.setInstancePluginEditor(this.getIdentifier(),this.getVersion(),mainEdit);
			}
			
		}
		return mainEdit;
	}
	/**
	 * Ritorna una istanza delle classe per il salvataggio
	 * @param PlugDataWin, classe finestra
	 * @param PlugData contenitore dei dati
	 * @return istanza di un classe di salvataggio se il plug non gestisce
	 *                  il salvataggio ritorna null.
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public IFilePlug getIstancePluginFile(
			PlugDataWin plugDataWin,
			PlugDataManager plugDataManager,FileManager fileManager)
	throws
	ClassNotFoundException,
	InstantiationException,
	IllegalAccessException {
		
		Class cPlug;
		
		if (plugFile == null) { //il plug-in non gestisce i file
			return null;
		}
		
		if (!isPluginFile()) {
			//non gestisce il salvataggio dei file
			return null;
		}
		
		if (this.pluginFile == null) {
			
			///CARICHIAMO LE LIBRERIE ESTERNE E INTERNE NEL GENERALCLASSLOADER
			this.generalClassLoader.addURL(getLibrary().getLibraryPluginFile());
			if (getLibrary().getExternalLibrary()!=null)
				if(getLibrary().getExternalLibrary().length!=0)
					this.generalClassLoader.addURLs(getLibrary().getExternalLibrary());
			
			
			
			////ISTANZIAMO PRIMA LE CLASSI CALLBACK DEI PLUGIN CHE VOGLIONO SALVARE/APRIRE CON IL PLUGFILE CORRENTE
			
//			otteniamo i plugin che intendono salvare o aprire modelli xml associati al plugin file corrente, serve al factory per le info suggli xschema
			ArrayList result = new ArrayList();
			IMainTabPanel[] plugins = fileManager.getPluginStorageFile(this.getIdentifier());
			
			for (int i = 0; i < plugins.length; i++) {
				result.add(fileManager.getPlugManager().getPlugEditDescriptor(plugins[i]));
			}
			
			PlugInDescriptor [] pluginsDescriptor = (PlugInDescriptor[]) result.toArray(
					new PlugInDescriptor[result.size()]);			
			
			
			
			for (int j = 0; j < this.infoXmlModel.size(); j++) {
				
				result.clear();
				for (int i = 0; i < pluginsDescriptor.length; i++) {
					
					try {	
						
						 String menagementFileClassName = pluginsDescriptor[i].getMenagementFileClass((String)((Vector)this.infoXmlModel.get(j)).get(0));
						 
						 if (menagementFileClassName!=null){
							 
							// Class clazz= Class.forName(menagementFileClassName);								
								
								//ClassLoader cl = clazz.getClassLoader();								
							//	SerializableCharmyFile currentMenagementFile = (SerializableCharmyFile)clazz.newInstance();
								Class clazz = generalClassLoader.loadClass(menagementFileClassName);
								SerializableCharmyFile currentMenagementFile = (SerializableCharmyFile) clazz.newInstance();
								//currentMenagementFile.setPlugData(pluginsDescriptor[i].getIstanceEdit().getPlugData());
								currentMenagementFile.setPlugin(pluginsDescriptor[i].getIstanceEdit());
								
								
								result.add(currentMenagementFile);						
							
							pluginsDescriptor[i].setMenagementFile(currentMenagementFile,(String)((Vector)infoXmlModel.get(j)).get(0));
							
							 
						 }
							
						//spostare nel desccriptor del plugin file.
						}catch (Exception e) {e.printStackTrace();}
					
				}
				
				SerializableCharmyFile [] pluginsMenagementFile = (SerializableCharmyFile[]) result.toArray(
						new SerializableCharmyFile[result.size()]);	
				
				//((Vector)infoXmlModel.get(j)).setElementAt(pluginsMenagementFile,2);
				((Vector)infoXmlModel.get(j)).add(2,pluginsMenagementFile);
				
				
			}			
					
			//////
			
			///ISTANZIAMO IL PLUGIN DI TIPO FILE
			
			cPlug = generalClassLoader.loadClass(plugFile.getClassFile());
			this.pluginFile = (IFilePlug) cPlug.newInstance();
			this.pluginFile.setDati(plugDataWin, plugDataManager);
			
			this.pluginRegistry.setInstancePluginFile(this.getIdentifier(),this.getVersion(),pluginFile);
			
			
		}
		return this.pluginFile;
		
	}
	
	/**
	 * Controlla se la classe ha una funzione di openFile
	 * @return true se il plug ha una funzione di open
	 */
	public boolean isPluginFile() {
		if (plugFile == null) {
			return false;
		}
		return plugFile.getClassFile().length() > 0;
	}
	
	/**
	 * Ritorna una istanza delle classe per il salvataggio
	 * @param PlugDataWin, classe finestra
	 * @param PlugData contenitore dei dati
	 * @return istanza di un classe di salvataggio se il plug non gestisce
	 *                  il salvataggio ritorna null.
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	/*public IFilePlug getIstanceSave(
	 PlugDataWin plugDataWin,
	 PlugDataManager plugDataManager)
	 throws
	 ClassNotFoundException,
	 InstantiationException,
	 IllegalAccessException {
	 
	 Class cPlug;
	 
	 if (plugFile == null) { //il plug-in non gestisce i file
	 return null;
	 }
	 
	 if (!isPlugSave()) {
	 //non gestisce il salvataggio dei file
	  return null;
	  }
	  
	  
	  if (saveFile == null) {
	  
	  this.generalClassLoader.addURL(getLibrary().getLibraryPlugFileSave());
	  if (getLibrary().getExternalLibrary()!=null)
	  if(getLibrary().getExternalLibrary().length!=0)
	  this.generalClassLoader.addURLs(getLibrary().getExternalLibrary());
	  
	  cPlug = generalClassLoader.loadClass(plugFile.getClassSave());
	  saveFile = (IFilePlug) cPlug.newInstance();
	  saveFile.setDati(plugDataWin, plugDataManager);
	  }
	  return saveFile;
	  }*/
	
	/**
	 * Ritorna una istanza delle classe per l'apertura del file
	 * @param PlugDataWin, classe finestra
	 * @param PlugData contenitore dei dati
	 * @return istanza di un classe di apertura se il plug non gestisce
	 *                  il salvataggio ritorna null.
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	/*public IFilePlug getIstanceOpen(
	 PlugDataWin plugDataWin,
	 PlugDataManager plugData)
	 throws
	 ClassNotFoundException,
	 InstantiationException,
	 IllegalAccessException {
	 
	 Class cPlug;
	 
	 if (plugFile == null) { //il plug-in non gestisce i file
	 return null;
	 }
	 
	 if (!isPlugOpen()) {
	 //non gestisce il salvataggio dei file
	  return null;
	  }
	  
	  
	  if (openFile == null) {
	  
	  this.generalClassLoader.addURL(getLibrary().getLibraryPlugFileOpen());
	  if (getLibrary().getExternalLibrary()!=null)
	  if(getLibrary().getExternalLibrary().length!=0)
	  this.generalClassLoader.addURLs(getLibrary().getExternalLibrary());
	  
	  
	  cPlug = generalClassLoader.loadClass(plugFile.getClassOpen());
	  openFile = (IFilePlug) cPlug.newInstance();
	  openFile.setDati(plugDataWin, plugData);
	  }
	  return openFile;
	  }*/
	
	/**
	 * Controlla se la classe ha una funzione di openFile
	 * @return true se il plug ha una funzione di open
	 */
	/*public boolean isPlugOpen() {
	 if (plugFile == null) {
	 return false;
	 }
	 return plugFile.getClassOpen().length() > 0;
	 }*/
	
	/**
	 * Controlla se la classe ha una funzione di openFile
	 * @return true se il plug ha una funzione di open
	 */
	public boolean isPlugEdit() {
		if (plugEditor == null) {
			return false;
		}
		return plugEditor.getEntryPoint().length() > 0;
	}
	
	/**
	 * Controlla se la classe ha una funzione di saveFile
	 * @return true se il plug ha una funzione di save
	 */
	/*public boolean isPlugSave() {
	 if (plugFile == null) {
	 return false;
	 }
	 
	 return plugFile.getClassSave().length() > 0;
	 
	 }*/
	
	/**
	 * 
	 */
	public void addPluginRequired(
			String idPlugin,
			String versionPluginRequired) {
		
		PluginModel[] list;
		
		if (this.dependenceList == null) {
			list = new PluginModel[1];
		} else {
			list = new PluginModel[this.dependenceList.length + 1];
			System.arraycopy(
					dependenceList,
					0,
					list,
					0,
					dependenceList.length);
		}
		
		PluginModel plug = new PluginModel();
		plug.setIdentifier(idPlugin);
		plug.setVersion(versionPluginRequired);
		list[list.length - 1] = plug;
		
		this.dependenceList = list;
		
	}
	
	/**
	 * 
	 *  Charmy Project
	 *  @author ezio di nisio
	 * 
	 * 
	 */
	
	public void addExtensionPoint(ExtensionPointDescriptor extPoint) {
		
		ExtensionPointDescriptor[] list;
		
		if (this.extensionPoints == null) {
			list = new ExtensionPointDescriptor[1];
		} else {
			list =
				new ExtensionPointDescriptor[this.extensionPoints.length + 1];
			System.arraycopy(
					extensionPoints,
					0,
					list,
					0,
					extensionPoints.length);
		}
		
		list[list.length - 1] = extPoint;
		this.extensionPoints = list;
		
	}
	
	/**
	 * 
	 * @param host
	 */
	public void addHost(HostDescriptor host) {
		
		HostDescriptor[] list;
		
		if (this.hosts == null) {
			list = new HostDescriptor[1];
		} else {
			list = new HostDescriptor[this.hosts.length + 1];
			System.arraycopy(hosts, 0, list, 0, hosts.length);
		}
		
		list[list.length - 1] = host;
		this.hosts = list;
		
	}
	
	/**
	 * @return Returns the listaPluginRequired.
	 */
	public PluginModel[] getPluginsRequired() {
		return dependenceList;
	}
	
	/**
	 * @return Returns the extensionPoints.
	 */
	public ExtensionPointDescriptor[] getExtensionPoints() {
		return extensionPoints;
	}
	/**
	 * @return Returns the hosts.
	 */
	public HostDescriptor[] getHosts() {
		return hosts;
	}
	
	/**
	 * rimuove una componente host dal registro.
	 * In caso di successo restituisce true, in tutti gli altri casi false.
	 * @param idHost
	 * @return
	 */
	public boolean removeHost(String idHost) {
		
		if (this.hosts == null)
			return false;
		
		HostDescriptor[] newList = new HostDescriptor[hosts.length - 1];
		int count = 0;
		for (int i = 0; i < hosts.length; i++) {
			
			if (hosts[i].getId().compareTo(idHost) != 0) {
				
				newList[count] = hosts[i];
				count++;
			}
			
		}
		
		if (count == hosts.length - 1) {
			hosts = newList;
			return true;
			
		}
		
		return false;
	}
	
	public void setGeneralClassLoader(GeneralURLClassLoader generalClassLoader) {
		this.generalClassLoader = generalClassLoader;
	}
	
	public Library getLibrary() {
		
		if (this.library==null)
			this.library=new Library (this);
		
		return library;
	}
	
	
	public String[] getIdPlugFileRequest() {
		return idPlugFileRequest;
	}
	
	public void setIdPlugFileRequest(String idPlugFileRequest) {
		
		String[] list;
		
		if (this.idPlugFileRequest == null) {
			list = new String[1];
			this.xSchemaInfo=new Vector();
		} else {
			list = new String[this.idPlugFileRequest.length + 1];
			System.arraycopy(this.idPlugFileRequest, 0, list, 0, this.idPlugFileRequest.length);
		}
		
		list[list.length - 1] = idPlugFileRequest;
		this.idPlugFileRequest = list;
		
	}
	
	public String getXSchemaFile(String idRef) {
		
		
		if (idRef==null)
			return null;
		
		for (int i = 0; i < this.xSchemaInfo.size(); i++) {
			
			if  (((String)((Vector)xSchemaInfo.get(i)).get(0)).compareTo(idRef)==0){
				
				return ((String)((Vector)xSchemaInfo.get(i)).get(1));
			}
		}
		
		return null;
	}
	
	public String getMenagementFileClass(String idRef) { 
		
		
		if (idRef==null)
			return null;
		
		for (int i = 0; i < this.xSchemaInfo.size(); i++) {
			
			if  (((String)((Vector)xSchemaInfo.get(i)).get(0)).compareTo(idRef)==0){
				
				return ((String)((Vector)xSchemaInfo.get(i)).get(2));
			}
		}
		
		return null;
	}
	
	public SerializableCharmyFile getMenagementFile(String idRef) { 
		
		
		if (idRef==null)
			return null;
		
		for (int i = 0; i < this.xSchemaInfo.size(); i++) {
			
			if  (((String)((Vector)xSchemaInfo.get(i)).get(0)).compareTo(idRef)==0){
				
				return ((SerializableCharmyFile)((Vector)xSchemaInfo.get(i)).get(3));
			}
		}
		
		return null;
	}
	
	public void setMenagementFile(SerializableCharmyFile plugin,String idXmlModel) { 
		
		
		if (idXmlModel==null)
			return;
		
		for (int i = 0; i < this.xSchemaInfo.size(); i++) {
			 
			if  (((String)((Vector)xSchemaInfo.get(i)).get(0)).compareTo(idXmlModel)==0){
				
				//((Vector)xSchemaInfo.get(i)).setElementAt(plugin,3);
				((Vector)xSchemaInfo.get(i)).add(3,plugin);
			}
		}
		
		
	}

	public void addXmlModel(String id, String nameXMLModel){
		
		if (this.infoXmlModel==null)
			infoXmlModel=new Vector();
		
		Vector info = new Vector(3);
		
		info.add(0,id);
		info.add(1,nameXMLModel);
		
		infoXmlModel.add(info);
		
	}
	
	public void addXSchemaInfo (String idRef, String xSchemaFile,String classMenagementFile){
		Vector info = new Vector (4);
		info.add(0,idRef);
		info.add(1,xSchemaFile);
		info.add(2,classMenagementFile);
		this.xSchemaInfo.add(info);
	}
	
	
	/**
	 * 
	 * 
	 * @author Ezio Di Nisio
	 * @return
	 */
	public Vector getInfoXmlModel() {
		return infoXmlModel;
	}
	
	public String getTagRootXMLFile() {
		return tagRootXMLFile;
	}
	
	public void setTagRootXMLFile(String tagRootXMLFile) {
		this.tagRootXMLFile = tagRootXMLFile;
	}

	public int getOrdineDiAttivazione() {
		return ordineDiAttivazione;
	}

	public void setOrdineDiAttivazione(int ordineDiAttivazione) {
		this.ordineDiAttivazione = ordineDiAttivazione;
	}
	
	
	
}
