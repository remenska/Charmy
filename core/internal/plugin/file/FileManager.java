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



import java.awt.FileDialog;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import core.internal.plugin.PlugInDescriptor;
import core.internal.plugin.PlugManager;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.xschema.SchemaEntry;
import core.internal.plugin.file.xschema.SchemaForest;
import core.internal.plugin.file.xschema.SimpleValue;
import core.internal.plugin.file.xschema.XMLParser;
import core.internal.plugin.file.xschema.XSchemaForestFactory;
import core.internal.runtime.Interfaccia;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;


/** Classe per gestire tutte le operazioni attivate
 dagli item presenti nel menu "File": 
 "New", "Open", "Save", "Save As", "Print", "Quit". */

public class FileManager {  
	
	///ezio 2006 - ogni plugin informa il filemanager sulla modifica del proprio workset
	//private static boolean modificata = false;
	public static String untitledFileName="untitled";
	
	private  Object[] changeWorksetPlugin=null;
	
	
	private Interfaccia inter;	 
	
	private Interfaccia mainLevel;
	
	private Vector plugFileInfo=null;

	private PlugDataManager plugDataManager;
	
	
	
	/**
	 * riferiemnto al gestore di plug-in
	 */
	private PlugManager plugManager;
	
	private Hashtable schemaForestMap = new Hashtable();
	
	private Document doc=null;
	//private SchemaForest schemaForest=null;
	
	private IMainTabPanel pluginActivated;
	
	/** Costruttore. */
	public FileManager(Interfaccia topLevel, PlugManager plugManager) {
		
		inter = topLevel;
		this.plugManager = plugManager;
		mainLevel = topLevel;
		
		//gestione file di salvataggio apertura.
	}
	
	/**Ezio Di Nisio - 
	 * 
	 * Metodo interno invocato da Interfaccia.
	 * In questo metodo controlliamo la presenza dei plugin tipo file (open-save file) per 
	 * la gestione nel menu dell'item di nuovo file (azzeramento variabili)
	 *
	 */
	public void start(){ 
		
		
		//ATTIVIAMO LE VOCI DI MENU RELATIVE A NEW FILE -> 
		//TANTI ITEM QUANTI SONO I PLUG (IN FEATURE CORRENTE) PER IL SALVATAGGIO/APERTURA DI FILE
		
		PlugInDescriptor[] plugins =plugManager.getPluginInFeature();
		
		// per ogni plugin
		if (plugins != null)
			for (int i = 0; i < plugins.length; i++) {
				
				if (plugins[i].isPluginFile()){
					
					String dir = System.getProperty("user.dir");
					String extensionFile  = plugins[i].getPlugFile().getNameFilter().substring(plugins[i].getPlugFile().getNameFilter().indexOf('.')+1);					
					this.addPlugFileInfo(plugins[i].getIdentifier(),new File(dir+"/"+untitledFileName+"."+ extensionFile));
					
					this.plugManager.getPlugDataWin().getFileMenu().getNewFileMenu().addNewFileItem(plugins[i]);
					
					///QUI INSERIRE CODICE PER GESTIRE I PULSANTI DELLA TOOLBAR STANDARD
					////
				}
				
			}
		
		/////
		
	}
	
	
	
	///modifica ezio 2006 - ogni plugin informa il filemanager sulla modifica del proprio workset
	/*public static void setModificata(boolean value) {
	 
	 modificata = value;
	 }
	 
	 public static boolean getModificata() {
	 return modificata;
	 }*/
	
	
	/**
	 * Setta la modifica del file corrente definito dal plugin file passato come argomento.
	 * 
	 * 
	 * @author Ezio Di Nisio
	 */
	public  void setChangeWorkset(String  idPluginFile,boolean value) {
		if (idPluginFile==null)
			return;
		
		if (this.changeWorksetPlugin!=null)
			for (int i = 0; i < changeWorksetPlugin.length; i++) {
				
				Object[] infoWorkset= (Object[])changeWorksetPlugin[i];
				String currentPluginFile = (String)infoWorkset[0];
				if (currentPluginFile.compareTo(idPluginFile)==0)
				{
					infoWorkset[1]=new Boolean(value);
					return;
				}
			}
		
		Object[] list;
		if (this.changeWorksetPlugin == null) {
			list = new Object[1];
		} else {
			list = new Object[this.changeWorksetPlugin.length + 1];
			System.arraycopy(changeWorksetPlugin, 0, list, 0, changeWorksetPlugin.length);
		}
		
		Object[] infoWorkset = new Object[2] ;
		infoWorkset[0]= idPluginFile;
		infoWorkset[1]= new Boolean(value);
		list[list.length - 1] = infoWorkset;
		this.changeWorksetPlugin = list;
		
	}
	
	
	
	/**
	 * Controlla se il file corrente definito dal plugin file passato come argomento è stato modificato da qualche plugin.
	 * 
	 * 
	 * @author Ezio Di Nisio
	 */
	public  boolean isWorksetChanged(String idPluginFile) {
		
		if (idPluginFile==null)
			return false;
		
		
		if (this.changeWorksetPlugin!=null)
			for (int j = 0; j < changeWorksetPlugin.length; j++) {
				
				Object[] infoWorkset= (Object[])changeWorksetPlugin[j];
				String currentPluginFile = (String)infoWorkset[0];
				if (currentPluginFile.compareTo(idPluginFile)==0)							
					return((Boolean)infoWorkset[1]).booleanValue();
				
			}						
		
		return false;		
		
	}
	
	/** Metodo per la creazione di una nuova architettura. */
	public void New(PlugInDescriptor plugFileDescriptor) {
		//IdCollection istanzaID = new IdCollection();
		
		
		String idCurrentPluginFile = plugFileDescriptor.getIdentifier();
		String dir = System.getProperty("user.dir");
		String extensionFile  = plugFileDescriptor.getPlugFile().getNameFilter().substring(plugFileDescriptor.getPlugFile().getNameFilter().indexOf('.')+1);					
		String completeName=dir+"/"+untitledFileName+"."+ extensionFile;
		
		try {
			
			if (this.isWorksetChanged(idCurrentPluginFile)) {
				switch (JOptionPane
						.showConfirmDialog(
								null,
								"Changes were made to the SA.\nDo you want to save before to quit?",
								"Information",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE)) {
								case JOptionPane.YES_OPTION:
									Save();
									//pathFile = null;
									
									this.addPlugFileInfo(idCurrentPluginFile,new File(completeName));
									
									
									//mainLevel.setTitle(CharmyFile.DESCRIZIONE);
									//mainLevel.adjustTitle(this.getPathFile(idCurrentPluginFile).getName());
									this.adjustTitle();
									
									
									
									plugFileDescriptor.getIstancePluginFile(
											plugManager.getPlugDataWin(),
											plugManager.getPlugData(),this).resetForNewFile();
									
									///reset delegato sui plugin
									this.resetForNewFile(plugFileDescriptor);
									
									this.setChangeWorkset(idCurrentPluginFile,false);
									
									
									break;
								case JOptionPane.NO_OPTION:
									
									this.addPlugFileInfo(idCurrentPluginFile,new File(completeName));
									
									//mainLevel.setTitle(CharmyFile.DESCRIZIONE);
									//mainLevel.adjustTitle(this.getPathFile(idCurrentPluginFile).getName());
									this.adjustTitle();
									
									
									plugFileDescriptor.getIstancePluginFile(
											plugManager.getPlugDataWin(),
											plugManager.getPlugData(),this).resetForNewFile();
									
									this.resetForNewFile(plugFileDescriptor);
									
									this.setChangeWorkset(idCurrentPluginFile,false);
									
									break;
								case JOptionPane.CANCEL_OPTION:
									break;
				}
			} else {
				
				//pathFile = null;
				this.addPlugFileInfo(idCurrentPluginFile,new File(completeName));
				
				//mainLevel.setTitle(CharmyFile.DESCRIZIONE);
				//mainLevel.adjustTitle(this.getPathFile(idCurrentPluginFile).getName());
				this.adjustTitle();
				
				
				plugFileDescriptor.getIstancePluginFile(plugManager.getPlugDataWin(),
						plugManager.getPlugData(),this).resetForNewFile();
				
				this.resetForNewFile(plugFileDescriptor);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void PrintingArea() {
		if (inter.focusIndex() == 1) {
		}
	}
	
	
	/** Metodo per l'apertura di un file contenente 
	 un'architettura precedentemente salvata. */
	public void Open() {
		
		try {
			
			IFilePlug pff = OpenRoutine();
			
			if (pff != null) {
				
				PlugInDescriptor pd = this.getPluginDescriptor(pff);
				pff.resetForNewFile(); 
				this.resetForNewFile(pd);
				
				this.startOpen(pff);
				
			
				//String[] idPlugFileRequest = this.plugManager.getPlugEditDescriptor(this.pluginActivated).getIdPlugFileRequest();
				
				//mainLevel.adjustTitle(this.getPathFile(pd.getIdentifier()).getName());
				this.adjustTitle();
				return;
			}
			
			
			
		} catch (Exception ex) {
			//String str;
			//str =  	"Error: " + ex + ". \nAction: New File."; 
			//JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.WARNING_MESSAGE);
			//istanzaID = new IdCollection();	
			
			
			/* ezio: , il codice seguente secondo me non serve a niente 
			 * pathFile = null;
			 try {
			 plugManager.resetForNewFile();
			 } catch (Exception e) {
			 e.printStackTrace();
			 }
			 //genera errori
			  if (pathFile != null) {
			  mainLevel.adjustTitle(pathFile.getName());
			  }*/
			
			ex.printStackTrace();
		}
		
	}
	
	
	/**
	 * funzione comune per il salvataggio mediante la richiesta
	 * del nome del file
	 * @return IFilePlug contente il plugDescriptor
	 * 				 oppure null se non viene gestito il tipo
	 */
	private IFilePlug SaveAsRoutine() throws Exception {
		JFileChooser chooser;
		chooser = new JFileChooser();
		PlugFileFilter[] pf = fileExtention();
		if (pf != null) {
			for (int i = 0; i < pf.length; i++) {
				chooser.addChoosableFileFilter(pf[i]);
			}
		}
		int returnVal = chooser.showSaveDialog(inter); 
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			FileFilter ff = chooser.getFileFilter(); 
			if (ff instanceof PlugFileFilter) {
				PlugFileFilter pff = (PlugFileFilter) ff;
				IFilePlug ifp = pff.getPlugInDescriptor()
				.getIstancePluginFile(plugManager.getPlugDataWin(),
						plugManager.getPlugData(),this);
				//ifp.setFile(chooser.getSelectedFile());
				
				
				File pathFile = chooser.getSelectedFile();
				
				String extensionFile  = pff.getPlugInDescriptor().getPlugFile().getNameFilter().substring(pff.getPlugInDescriptor().getPlugFile().getNameFilter().indexOf('.')+1);
				
				if(pathFile.getName().indexOf('.')==-1)
					pathFile=new File(pathFile.getAbsoluteFile()+"."+extensionFile);
				
				this.addPlugFileInfo(pff.getPlugInDescriptor().getIdentifier(),pathFile);
				
				
				//	this.currentFileWork=pathFile;
				//this.currentPlugFile =pff.getPlugInDescriptor().getIdentifier();
				return ifp;
			}
		}
		return null;
	}
	
	/**
	 * funzione comune per il salvataggio mediante la richiesta
	 * del nome del file
	 * @return IFilePlug contente il plugDescriptor
	 * 				 oppure null se non viene gestito il tipo
	 */
	private IFilePlug SaveRoutine(String idPlugFile) throws Exception {
		
		PlugFileFilter[] pf = fileExtention();
		PlugFileFilter pff=null;
		for (int i = 0; i < pf.length; i++) {
			
			if(pf[i].getPlugInDescriptor().getIdentifier().compareTo(idPlugFile)==0){
				
				pff=pf[i];
			}
		}
		
		//PlugFileFilter pff = pf[0];
		IFilePlug ifp = null;
		if (pff!=null)
			ifp = pff.getPlugInDescriptor().getIstancePluginFile(
					plugManager.getPlugDataWin(), plugManager.getPlugData(),this);
		//ifp.setFile(pathFile);
		
		//this.currentPlugFile =pff.getPlugInDescriptor().getIdentifier();
		//this.currentFileWork=pathFile;
		
		return ifp;
	}
	
	/**
	 * funzione comune per l'apertura mediante la richiesta
	 * del nome del file
	 * @return IFilePlug contente il plugDescriptor
	 * 				 oppure null se non viene gestito il tipo
	 */
	private IFilePlug OpenRoutine() throws Exception {
		JFileChooser chooser;
		
		chooser = new JFileChooser();
		PlugFileFilter[] pf = fileExtention();
		if (pf != null) {
			for (int i = 0; i < pf.length; i++) {
				chooser.addChoosableFileFilter(pf[i]);
			}
		}
		int returnVal = chooser.showOpenDialog(inter);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			////////////MODIFICA PER GARANTIRE ALTRI PLUG DI OPEN FILE. - 
			FileFilter ff = chooser.getFileFilter();
			if (ff instanceof PlugFileFilter) {
				PlugFileFilter pff = (PlugFileFilter) ff;
				IFilePlug plugin = pff.getPlugInDescriptor()
				.getIstancePluginFile(plugManager.getPlugDataWin(),
						plugManager.getPlugData(),this);
				
				//////CONTROLLO
				String idPlugFileSelected =  pff.getPlugInDescriptor().getIdentifier();
				///se è stato cambiato il file precedente del filtro selezionato allora lanciamo il save				
				if (isWorksetChanged(idPlugFileSelected))
				{
					
					switch (JOptionPane
							.showConfirmDialog(
									null,
									"Do you want to save " + this.getPathFile(idPlugFileSelected) + "?",
									"Information",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE)) {
									case JOptionPane.CANCEL_OPTION:
										break;
									case JOptionPane.YES_OPTION:
										
										String currentFileName=this.getPathFile(idPlugFileSelected).getName().substring(0,this.getPathFile(idPlugFileSelected).getName().indexOf("."));
										
										if (currentFileName.compareTo(untitledFileName)==0) {
											
											SaveAs();
										} else {
											this.startSave(plugin,idPlugFileSelected);
											
										}
										
										this.setChangeWorkset(idPlugFileSelected,false);
										//attenzione non serve ill break
									case JOptionPane.NO_OPTION:
										
										
										
					}
					
				}
				//////////////////
				///////////
				
				
				//ifp.setFile(chooser.getSelectedFile());
				File pathFile = chooser.getSelectedFile();
				
				
				this.addPlugFileInfo(pff.getPlugInDescriptor().getIdentifier(),pathFile);
				//this.currentPlugFile =pff.getPlugInDescriptor().getIdentifier();
				//	this.currentFileWork = chooser.getSelectedFile();
				
				return plugin;
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 *@author Ezio Di Nisio
	 */
	private void startOpen(IFilePlug plugFile){
		
		Vector infoSchemaForest = null;
		
		PlugInDescriptor pd = this.getPluginDescriptor(plugFile);
		String idPlugFile = pd.getIdentifier();
		if (this.schemaForestMap.get(idPlugFile)==null){
			
			infoSchemaForest = this.buildSchemaForest(idPlugFile);
			///almeno un xml ci deve essere per forza, prendiamo lo schema forest associato
			SchemaForest primoSchemaForest = (SchemaForest)((Vector)infoSchemaForest.get(0)).get(3);
			plugFile.setPlugins(primoSchemaForest.getPlugins());
			
		}else infoSchemaForest = this.getInfoSchemaForest(idPlugFile);
		
		if (this.getPathFile(idPlugFile)!=null){
			try{
				ZipFile zip = new ZipFile(this.getPathFile(idPlugFile));	
				ZipEntry zipEntry=null;
				Enumeration en = zip.entries();
				
				for (int i = 0; i < infoSchemaForest.size(); i++) {
					
					String idXmlModel = (String)((Vector)infoSchemaForest.get(i)).get(0);
					SchemaEntry schemaEntryRootMerged = (SchemaEntry)((Vector)infoSchemaForest.get(i)).get(1);			
					String nameXmlModel = (String)((Vector)infoSchemaForest.get(i)).get(2);
					SchemaForest schemaForest = (SchemaForest)((Vector)infoSchemaForest.get(i)).get(3);
					
					String tagRoot = this.plugManager.getPluginDescriptor(idPlugFile).getTagRootXMLFile();
					
					if ((schemaEntryRootMerged==null)||(schemaEntryRootMerged.getElementName().compareTo(tagRoot)!=0)){					
						System.out.println("errore nella definizione di un xschema:  manca il tag principale richiesto dal plugin file in uso ---");
						return;
					}					
					
					
					ZipEntry currentZe;
					while(en.hasMoreElements()){
						
						currentZe = (ZipEntry)en.nextElement();
						if(currentZe.getName().equals(nameXmlModel)){
							zipEntry = currentZe;
							break;
						}
					}
					
					if (zipEntry==null){
						
						////GESTIONE ERRORE - IL FILE ZIP NON CONTIENE IL FILE XML RICHIESTO DAL PLUGIN FILE
					}
					else{
						
						///PARTE CENTRALE DELL'APERTURA FILE - IL PARSER INTERNO PARSA I FILE XML CONTENUTI NEL FILE ZIP 
						//E DELEGA AI GIUSTI PLUGIN LE INFORMAZIONI CONTENUTE NEI TAG - IL PARSER FA QUESTO LAVORO CONSIDERANDO ANCHE L'ALBERO DELL' XSCHEMA UNICO OTTENUTO FACENDO IL MERGE DI TUTTI GLI XSCHEMA 
						SAXParserFactory factory = SAXParserFactory.newInstance();
						SAXParser saxParser = factory.newSAXParser();
						
						XMLParser internalParser = new XMLParser(this.plugDataManager,schemaEntryRootMerged);
						saxParser.parse(zip.getInputStream(zipEntry), internalParser);
						zipEntry=null;
						//////////////////////
					}
				}	
				
			}
			
			catch (Exception ex) {
				
				ex.printStackTrace();
				
			}
			
		}
		
		
	}
	
	
	
	
	/**
	 * 
	 *@author Ezio Di Nisio
	 */
	private void startSave(IFilePlug plugFile,String idPlugFile){
		
		Vector infoSchemaForest = null;
		if (this.schemaForestMap.get(idPlugFile)==null){
			
			infoSchemaForest = this.buildSchemaForest(idPlugFile);
			///almeno un xml ci deve essere per forza, prendiamo lo schema forest associato
			SchemaForest primoSchemaForest = (SchemaForest)((Vector)infoSchemaForest.get(0)).get(3);
			plugFile.setPlugins(primoSchemaForest.getPlugins());
			
		}
		else infoSchemaForest = this.getInfoSchemaForest(idPlugFile);
		
		ZipOutputStream out = null;
		FileOutputStream dest = null;
		
		for (int i = 0; i < infoSchemaForest.size(); i++) {
			
			String idXmlModel = (String)((Vector)infoSchemaForest.get(i)).get(0);
			SchemaEntry schemaEntryRootMerged = (SchemaEntry)((Vector)infoSchemaForest.get(i)).get(1);			
			String nameXmlModel = (String)((Vector)infoSchemaForest.get(i)).get(2);
			SchemaForest schemaForest = (SchemaForest)((Vector)infoSchemaForest.get(i)).get(3);
			
			if (this.getPathFile(idPlugFile)!=null){
				
				////CONTROLLO: facendo il merge di tutti gli xschema alla fine dobbiamo avere
				//un unico albero con un'unico elemento radice: questo elemento rappresenta il tag che identifica il tipo di file (file .charmy)
				String tagRoot = this.plugManager.getPluginDescriptor(idPlugFile).getTagRootXMLFile();
				
				if ((schemaEntryRootMerged==null)||(schemaEntryRootMerged.getElementName().compareTo(tagRoot)!=0)){					
					System.out.println("errore nella definizione di un xschema:  manca il tag principale richiesto dal plugin file in uso ---");
					return;
				}	
				
				this.doc = new DocumentImpl();
				//			this.doc = new DefaultDocument();
				Element elementRootXMLModel = this.creaDoc(schemaEntryRootMerged);
				
				///caso documento vuoto, salviamo ugualmente il file 
				if (elementRootXMLModel==null)
					elementRootXMLModel = doc.createElement(tagRoot);
				
				this.doc.appendChild(elementRootXMLModel);
				
				
				try {
					
					if (i==0){
						dest = new FileOutputStream(this.getPathFile(idPlugFile));						
						out = new ZipOutputStream(new BufferedOutputStream(dest));
						
					}
					
					plugFile.appendInfoFile(idXmlModel,doc,elementRootXMLModel);
					
					this.serializzaDom(out,nameXmlModel);
				}
				catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("general.FileManager: " + ex);
				}
				
			}
			
		}
		
		this.setChangeWorkset(idPlugFile,false);
		
		try {
			out.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("general.FileManager: " + ex);
		}
		
		
	}
	
	/**
	 * invoca il metodo resetForNewFile sul plugin di tipo file e su tutti i plugin su cui vi sono  agganciati
	 * - inoltre invoca resetForNewFile sui tutti i plugdata e i relativi  plugin 
	 *@author Ezio Di Nisio
	 */
	private void resetForNewFile(PlugInDescriptor pluginFileDescriptor){
		
		IMainTabPanel[] plugins = this.getPluginStorageFile(pluginFileDescriptor.getIdentifier());
		
		IPlugData[] plugsData = new IPlugData[plugins.length];
		for (int i = 0; i < plugins.length; i++) {
			
			plugsData[i]=plugins[i].getPlugData();
		}
		
		
		Vector infoXMLModel = pluginFileDescriptor.getInfoXmlModel();
		
		plugDataManager.clearAll(plugsData); 
		
		/*for (int i = 0; i < plugins.length; i++) {			
		 plugins[i].resetForNewFile();
		 }*/
		
		
		
		for (int i = 0; i < infoXMLModel.size(); i++) {
			
			SerializableCharmyFile[] currentPlugins = (SerializableCharmyFile[])((Vector)infoXMLModel.get(i)).get(2);
			for (int j = 0; j < currentPlugins.length; j++) {
				
				currentPlugins[j].resetForNewFile();
				
			}
			
		}
		
		//mainLevel.adjustTitle("senza nome");
		
		
	}
	
	
	
	/**
	 * serializza e scrive il dom su file
	 *  
	 */
	private void serializzaDom(ZipOutputStream zip, String xmlFile) {
		
		ZipEntry ze = new ZipEntry(xmlFile);
		
		try {
			zip.putNextEntry(ze);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//creazione file xml vero e proprio
		OutputFormat format = new OutputFormat(doc, "UTF-8", true);
		
		
		XMLSerializer serial = new XMLSerializer(zip, format);
		try {
			serial.asDOMSerializer();
			serial.serialize(doc.getDocumentElement());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			zip.closeEntry();
		} catch (IOException e2) {
			
			e2.printStackTrace();
		}
		
	}
	
	
	/**
	 * 
	 * 
	 * @author Ezio Di Nisio
	 * @param idPlugFile
	 * @return
	 */
	private Vector buildSchemaForest(String idPlugFile){
		
		Vector infoXMLModel = this.plugManager.getPluginDescriptor(idPlugFile).getInfoXmlModel();
		String tagNameEntryRoot = this.plugManager.getPluginDescriptor(idPlugFile).getTagRootXMLFile();
		
		
		
		Vector dataTree = new Vector(infoXMLModel.size());
		
		ArrayList result = new ArrayList();
		for (int i = 0; i < infoXMLModel.size(); i++) {
			
			String idXMLModel = (String)((Vector)infoXMLModel.get(i)).get(0);
			String nameXMLModel = (String)((Vector)infoXMLModel.get(i)).get(1);
			SerializableCharmyFile[] pluginsMenagementFile = (SerializableCharmyFile[])((Vector)infoXMLModel.get(i)).get(2);
			
			IMainTabPanel[] plugins = this.getPluginStorageFile(idPlugFile);
			
			for (int j = 0; j < plugins.length; j++) {
				
				PlugInDescriptor pd = this.getPlugManager().getPlugEditDescriptor(plugins[j]);
				if (pd.getMenagementFile(idXMLModel)!=null)
					result.add(pd);
			}
			PlugInDescriptor [] pluginsDecriptor = (PlugInDescriptor[]) result.toArray(
					new PlugInDescriptor[result.size()]);			
			
			//////
			SchemaForest schemaForest = (new XSchemaForestFactory(pluginsDecriptor,idXMLModel)).buildSchemaForest();
			//successivamente al merge lo schema forest viene alterato perchè vengono modificate le entry (child e attrib).
			//l'albero definitivo ha un unico elemento root, che è la entry root del primo plug, sotto di questo vengono inglobati gli elementi degli altri xschema
			//////forse è un po contorto...
			
			SchemaEntry entryRootMerged = this.mergeSchemaForest(schemaForest,tagNameEntryRoot);
			
			Vector data = new Vector (4);
			data.add(0,idXMLModel);
			data.add(1,entryRootMerged);
			data.add(2,nameXMLModel);				
			data.add(3,schemaForest);
			
			dataTree.add(data);
			
			result.clear();
		}
		
		this.schemaForestMap.put(idPlugFile,dataTree);
		
		return dataTree;
		
	}
	
	
	
	/**
	 * fa il merge tra gli alberi relativi a tutti gli xschema associati ad un modello xml definiti da un plugin di tipo file.
	 * Restituisce gli elementi di root relativi alla nuova foresta. la foresta rappresenta un xschema che è la fusione di tutti gli xschema definiti dai plugin che vogliono salvarre i dati.
	 * @param idPlugFile
	 * @param idXmlModel
	 * @author Ezio Di Nisio
	 * @return
	 */
	private SchemaEntry mergeSchemaForest(SchemaForest schemaForest, String tagNameEntryRoot){
		
		
		Vector forest = schemaForest.getForest();
		
		///allSchemaEntryRoot conterrà tutti gli element root di tutti gli xschema(tutti i plugin) il cui nome è
		//lo stesso dell'elemento root definito dal plugin di tipo file
		ArrayList allSchemaEntryRoot = new ArrayList(); 
		
		for (int i = 0; i < forest.size(); i++) {
			
			SerializableCharmyFile menagementFile = (SerializableCharmyFile)((Vector)forest.get(i)).get(0);
			SchemaEntry[] schemaEntryRoot = schemaForest.getRoot(menagementFile);
			
			for (int z = 0; z < schemaEntryRoot.length; z++) {
				
				if (schemaEntryRoot[z].getElementName().compareTo(tagNameEntryRoot)==0)
					allSchemaEntryRoot.add(schemaEntryRoot[z]);
			}			
		}	
		
		///////
		SchemaEntry uniqueSchemaEntryRoot = null;
		if (allSchemaEntryRoot.size()>0){
			
			uniqueSchemaEntryRoot = (SchemaEntry)allSchemaEntryRoot.get(0);
			
			for (int i = 1; i < allSchemaEntryRoot.size(); i++) {
				
				uniqueSchemaEntryRoot = this.mergeSchemaEntry(uniqueSchemaEntryRoot,(SchemaEntry)allSchemaEntryRoot.get(i));
			}
			
		}
		
		return uniqueSchemaEntryRoot;
	}
	
	/**
	 * metodo ricorsivo usato da mergeSchemaForest
	 * @param schemaEntry1
	 * @param schemaEntry2
	 * @author Ezio
	 * @return
	 */
	private SchemaEntry mergeSchemaEntry(SchemaEntry schemaEntry1, SchemaEntry schemaEntry2){
		
		if (schemaEntry1.getElementName().compareTo(schemaEntry2.getElementName())==0){
			
			//MERGE DEGLI ATTRIBUTI 
			XSTypeDefinition xsType = ((XSElementDecl)schemaEntry2.getElementSource()).fType;						
			if (xsType.getTypeCategory()==XSTypeDefinition.COMPLEX_TYPE){
				
				XSComplexTypeDefinition complexType= (XSComplexTypeDefinition)xsType;
				XSObjectList attributeUses = complexType.getAttributeUses();
				
				if (attributeUses.getLength()>0)
					schemaEntry1.addAttributesExtension(schemaEntry2.getPlugin(),attributeUses);
				else
					schemaEntry1.addAttributesExtension(schemaEntry2.getPlugin(),new XSObjectListImpl());
				
			}
			else
				schemaEntry1.addAttributesExtension(schemaEntry2.getPlugin(),new XSObjectListImpl());
			
			/////// RICORSIONE PER MERGE DELLE ENTRY SUI NODI FIGLIO
			
			SchemaEntry[] childSchemaEntry2 = schemaEntry2.getChild();
			if (childSchemaEntry2!=null)
				for (int i = 0; i < childSchemaEntry2.length; i++) {
					
					SchemaEntry[] childSchemaEntry1 = schemaEntry1.getChild();
					boolean isNewChild = true;
					if (childSchemaEntry1!=null)
						for (int j = 0; j < childSchemaEntry1.length; j++) {
							
							if (childSchemaEntry2[i].getElementName().compareTo(childSchemaEntry1[j].getElementName())==0){
								
								this.mergeSchemaEntry(childSchemaEntry1[j],childSchemaEntry2[i]);
								isNewChild = false;
								break;
							}
							//else
							//schemaEntry1.appendChild(childSchemaEntry2[j]);
						}
					if (isNewChild)
						schemaEntry1.appendChild(childSchemaEntry2[i]);
				}
			
			
		}
		
		return schemaEntry1;
		
	}
	
	private Element creaDoc(SchemaEntry schemaEntryRoot){
		
		
		//ArrayList result = new ArrayList(); 
		//	for (int i = 0; i < schemaEntryRoot.length; i++) {
		Object instanceCurrentElement = schemaEntryRoot.getPlugin().getObject(schemaEntryRoot);
		schemaEntryRoot.setInstanceElement(instanceCurrentElement);
		
		Element element = this.creaElement(schemaEntryRoot);					
		
		if (element!=null){				
			//result.add(element);
			return element;
		}
		//}
		return null;
		//return (Element[]) result.toArray(
		//	new Element[result.size()]);
	}
	
	
	/**
	 * Metoto ricorsivo che naviga nell'XSchema, e costruisce il file xml di output. Le informazioni (valori attributi dei tag) vengono ottenute interrogando il plugin costruttore dell'XSchema
	 * @param schemaEntry  - contiene le informazioni relative all'XSchema element - API Post Schema Info Validation
	 * @param dataCurrentElement  (istanza dell'oggetto associato al tag, null altrimenti). In entrata sempre null. Viene ottenuto all'interno del metodo stesso, viene passato come valore parent nella chiamata ricorsiva.
	 * @param dataElementParent Istanza dell'oggetto associato al parent del tag corrente.
	 * @return xml element - tag
	 * @author Ezio
	 */
	private Element creaElement (SchemaEntry schemaEntry){
		
		///PASSO 1 - CREIAMO L'ELEMENTO E SETTIAMO GLI ATTRIBUTI
		Element element = null;
		boolean isXmlElement = false;
		///PASSO 2
		// Interroghiamo il plugin per sapere se al tag è associato una istanza di oggetto.
		
		//	SchemaEntry schemaEntryParent = schemaEntry.getParent();
		//String parentElementName=null;
		//if (schemaEntryParent!=null)
		//parentElementName=schemaEntryParent.getElementSource().getName();
		
		//	Object instanceCurrentElement = schemaEntry.getInstanceElement();		
		//if (instanceCurrentElement==null){			
		//	}
		
		element = this.setAttributes(schemaEntry,doc);
		
		/*if (instanceCurrentElement!=null){			
		 element = doc.createElement(schemaEntry.getElementSource().getName());
		 this.setAttributes(schemaEntry,element);
		 isXmlElement = true;			
		 }*/
		
		
		
		//PASSO 3 - settiamo gli attributi definiti nel XSchema - interrogazione plugin per ottenere i valori degli attributi
		//XSComplexTypeDefinition complexType = schemaEntry.getElementSource().getEnclosingCTDefinition();
		
		////PASSO 4 >>>GESTIONE ITERAZIONE<<<<
		
		if (schemaEntry.isSequence()){
			
			SchemaEntry[] elementChild = schemaEntry.getChild();
			
			for (int i = 0; i < elementChild.length; i++){
				
				//se l'elemento schema è una lista : 2*-unbounded
				if (elementChild[i].isList()){
					Object[] objectList = elementChild[i].getPlugin().getList(elementChild[i]);
					
					/*	if ((objectList!=null)&&(objectList.length > 0)&& (!isXmlElement)){
					 
					 element = doc.createElement(schemaEntry.getElementSource().getName());
					 this.setAttributes(schemaEntry,element);
					 isXmlElement = true;
					 }*/
					
					if (objectList!=null)
						for (int j = 0; j < objectList.length; j++){
							
							elementChild[i].setInstanceElement(objectList[j]);
							Element elementXMLChild = this.creaElement(elementChild[i]);
							
							if ((elementXMLChild!=null)&&(element==null))
								element = doc.createElement(schemaEntry.getElementSource().getName());
							
							if (element!=null)
								element.appendChild(elementXMLChild);
							//isXmlElement = true;
						}
					
				} //else è un elemento con  maxOccur 1
				else{
					
					
					Object instanceCurrentElement = elementChild[i].getPlugin().getObject(elementChild[i]);
					elementChild[i].setInstanceElement(instanceCurrentElement);
					
					Element elementXMLChild = this.creaElement(elementChild[i]);
					
					if ((elementXMLChild!=null)&&(element==null))
						element = doc.createElement(schemaEntry.getElementSource().getName());
					
					if ((element!=null)&&(elementXMLChild!=null))
						element.appendChild(elementXMLChild);
					
					
					//element.appendChild(this.creaElement(elementChild[i],null,dataCurrentElement));
				}
			}
			
		}
		else{
			
		}	
		
		///gestione del tipo dell'elemento: cosa va scritto tra i tag di apertura chiusura.
		
		
		if (!schemaEntry.isAnyTypeDefinition()){
			
			
			if (schemaEntry.getTypeDefinitionName().compareTo("string")==0){
				
				CDATASection cdata = this.creaSezioneCDATA(schemaEntry);
				if (cdata!=null){
					
					if (element==null)
						element = doc.createElement(schemaEntry.getElementSource().getName());
					element.appendChild(cdata);
				}
				
			}
			
			
		}
		
		
		return element;
	}
	
	
	
	private Element setAttributes(SchemaEntry schemaEntry, Document document){
		
		XSTypeDefinition xsType = ((XSElementDecl)schemaEntry.getElementSource()).fType;
		
		boolean elementCreated = false;
		Element element = null;
		if (xsType.getTypeCategory()==XSTypeDefinition.COMPLEX_TYPE){
			
			XSComplexTypeDefinition complexType= (XSComplexTypeDefinition)xsType;
			XSObjectList attributeUses = complexType.getAttributeUses();
			
			for (int i = 0; i < attributeUses.getLength(); i++) {
				
				XSAttributeUse xsAttributeUse=(XSAttributeUse)attributeUses.item(i);
				
				SimpleValue attributeValue = schemaEntry.getPlugin().getAttributeValue(schemaEntry,xsAttributeUse.getAttrDeclaration().getName());
				if (attributeValue!=null){
					
					if (!elementCreated){
						
						element = document.createElement(schemaEntry.getElementSource().getName());
						elementCreated=true;
					}
					
					element.setAttribute(xsAttributeUse.getAttrDeclaration().getName(),attributeValue.getValue());					
					
				}
			} 
		}
		
		///settiamo gli attributi dei plugin estensori
		Vector infoExtensionAttribute = schemaEntry.getInfoExtensionAttribute();
		if (infoExtensionAttribute!=null)
			for (int i = 0; i < infoExtensionAttribute.size(); i++) {
				
				Vector info = (Vector)infoExtensionAttribute.get(i);
				
				SerializableCharmyFile plugin = (SerializableCharmyFile)info.get(0);
				XSObjectList attributeUses = (XSObjectList)info.get(1);
				
				for (int j = 0; j < attributeUses.getLength(); j++) {
					
					XSAttributeUse xsAttributeUse=(XSAttributeUse)attributeUses.item(j);
					
					SimpleValue attributeValue = plugin.getAttributeValue(schemaEntry,xsAttributeUse.getAttrDeclaration().getName());
					
					if (attributeValue!=null){
						
						if (!elementCreated){
							
							element = document.createElement(schemaEntry.getElementSource().getName());
							elementCreated=true;
						}						
						element.setAttribute(xsAttributeUse.getAttrDeclaration().getName(),attributeValue.getValue());					
						
					}
				}			
			}
		
		
		return element;
	}
	
	private CDATASection creaSezioneCDATA(SchemaEntry schemaEntry){
		
		//ATTENZIONEnon gestiamo l'errore di cast: diamo fiducia nel plugin che restituisce una stringa
		String element = (String)schemaEntry.getPlugin().getElement(schemaEntry);
		
		if (element!=null){
			
			return doc.createCDATASection(element);
		}
		return null;
	}
	
	private void adjustTitle (){
		
		String totalFileManagement ="";
		
		if (pluginActivated!=null){
			
			String[] idPlugFileRequest = this.plugManager.getPlugEditDescriptor(this.pluginActivated).getIdPlugFileRequest();
			
			if (idPlugFileRequest!=null)
				for (int i = 0; i < idPlugFileRequest.length; i++) {
					
					if (totalFileManagement.equals(""))
						totalFileManagement=totalFileManagement+ this.getPathFile(idPlugFileRequest[i]).getName();
					else
						totalFileManagement=totalFileManagement+ ";"+this.getPathFile(idPlugFileRequest[i]).getName();
					
					
				}
			
		}
		
		
		if (totalFileManagement.equals(""))
			mainLevel.adjustTitle(null);
		else
			mainLevel.adjustTitle(totalFileManagement);
		
	}
	
	
	/** Metodo per salvare su file l'architettura corrente. */
	public void Save() {
		//String strFile;
		//FileOutputStream fos;
		//ObjectOutputStream oos;
		//IdCollection istanzaID;
		//JFileChooser chooser;
		
		String[] idPlugFileRequest = this.plugManager.getPlugEditDescriptor(this.pluginActivated).getIdPlugFileRequest();
		
		if (idPlugFileRequest!=null)
			for (int i = 0; i < idPlugFileRequest.length; i++) {
				
				try {
					String currentFileName=this.getPathFile(idPlugFileRequest[i]).getName().substring(0,this.getPathFile(idPlugFileRequest[i]).getName().indexOf("."));
					
					if (currentFileName.compareTo(untitledFileName)==0) {
						//this.currentPlugFile=idPlugFileRequest[i];
						SaveAs();
					} else {
						//if (fileSave == null) {
						IFilePlug pff = SaveRoutine(idPlugFileRequest[i]);
						//	if (pff != null) {
						//		fileSave = pff;
						//	} else {
						//		return;
						//	}
						//}
						
						//	parseSchemaFile(fileSave);
						if (pff!=null){
							this.startSave(pff,idPlugFileRequest[i]);
							//mainLevel.adjustTitle(this.getPathFile(idPlugFileRequest[i]).getName());
							this.adjustTitle();
						}
						
						//this.doc = new DocumentImpl();  
						//fileSave.start(doc,this.creaDoc());
						
						
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("general.FileManager: " + ex);
				}
				
				
			}
		
		
		
	}
	
	/** Metodo per salvare l'architettura corrente su un nuovo file. */
	public void SaveAs() {
		FileDialog fd;
		String strFile;
		FileOutputStream fos;
		ObjectOutputStream oos;
		//IdCollection istanzaID;
		try {
			IFilePlug pff = SaveAsRoutine();
			if (pff != null) {
				//fileSave = pff;
				String plugFileID = this.getPluginDescriptor(pff).getIdentifier();
				
				this.startSave(pff,plugFileID);
				//this.doc = new DocumentImpl();  
				//fileSave.start(this.doc,this.creaDoc());
				
				//mainLevel.adjustTitle(this.getPathFile(plugFileID).getName());
				this.adjustTitle();
			} else {
				//routine non gestista
				//far vedere un dialog box
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("generale.FileManager.SaveAs: " + ex);
		}
	}
	
	/** Metodo per avviare la stampa dell'architettura corrente. */
	public void Print() {
		try {
			PrinterJob pjob = PrinterJob.getPrinterJob();
			if (pjob.printDialog()) {
				PageFormat pf = pjob.defaultPage();
				pjob.print();
			}
		} catch (PrinterException e) {
			e.printStackTrace();
			System.out.println("FileManager.Print: " + e);
		}
	}
	
	/** Metodo per gestire l'uscita dal programma. */
	public void Quit() {
		
		if  (this.changeWorksetPlugin!=null)
			for (int i = 0; i < this.changeWorksetPlugin.length; i++) {
				
				String idPlugFile = (String)((Object[])changeWorksetPlugin[i])[0];
				boolean modificata = ((Boolean)((Object[])changeWorksetPlugin[i])[1]).booleanValue();
				
				if (modificata) {
					
					switch (JOptionPane
							.showConfirmDialog(
									null,
									"Do you want to save " + this.getPathFile(idPlugFile)  +" before to quit?",
									"Information", JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE)) {
									case JOptionPane.YES_OPTION:
										
										try {
											String currentFileName=this.getPathFile(idPlugFile).getName().substring(0,this.getPathFile(idPlugFile).getName().indexOf("."));
											
											if (currentFileName.compareTo(untitledFileName)==0) {
												
												SaveAs();
											} else {
												
												IFilePlug pff = SaveRoutine(idPlugFile);										
												if (pff!=null){
													this.startSave(pff,idPlugFile);
												}
												
												
											}
										} catch (Exception ex) {
											ex.printStackTrace();
											System.out.println("general.FileManager: " + ex);
										}
										
										
										//break;
									case JOptionPane.NO_OPTION:
										
										break;
									case JOptionPane.CANCEL_OPTION:
										return;
					}
				} 
				
			}
		
		System.exit(0);
		
		
		
	}
	
	/**
	 * inserisce un riferimento al plugData
	 * @param plugData
	 */
	public void setPlugData(PlugDataManager plugDataManager) {
		this.plugDataManager = plugDataManager;
	}
	
	
	/**
	 * otteniamo l'elenco dei plugin che intendono salvare sul plug file corrente. Attenzione : il vettore è ordinato in base all'ordine di attivazione dei plugin (dipendenze)
	 * @author Ezio
	 * @param idPlugFileSave 
	 * @return
	 */
	public IMainTabPanel[] getPluginStorageFile(String idPlugFileSave){
		
		//consideriamo solo i plugin in feature --- ??? __---
		Vector result = new Vector();
		
		PlugInDescriptor[] listaPlugFeature=  this.plugManager.getPluginInFeature();
		
		for (int i = 0; i < listaPlugFeature.length; i++) {
			
			String[] PlugSaveId = listaPlugFeature[i].getIdPlugFileRequest();
			if (PlugSaveId!=null)
				for (int j = 0; j < PlugSaveId.length; j++) {
					if (PlugSaveId[j].compareTo(idPlugFileSave)==0){
						try{
							result.add(listaPlugFeature[i]);
							break;
						}
						catch (Exception e){
							//occhio: gestire eccezione.
							e.printStackTrace();
						}
						
					}	
					
				}
			
			
			
		}
		
		IMainTabPanel[]  result2= new IMainTabPanel[result.size()];
		int cont = 0;
		while (result.size()>0){
			
			PlugInDescriptor tempDescr = (PlugInDescriptor)result.get(0);
			
			for (int i = 1; i < result.size(); i++) {
				int ordineplug = ((PlugInDescriptor)result.get(i)).getOrdineDiAttivazione();
				int ordineTempDescr =  tempDescr.getOrdineDiAttivazione();
				//int ordineplug2 = ((PlugInDescriptor)result.get(i+1)).getOrdineDiAttivazione();
				if (ordineplug<ordineTempDescr)
					tempDescr=(PlugInDescriptor)result.get(i);
				
			}
			result.remove(tempDescr);
			try{
				result2[cont]= tempDescr.getIstanceEdit();
			}
			catch (Exception e){
				//occhio: gestire eccezione.
				e.printStackTrace();
			}
			cont++;
			
			
		}
		
		return result2;
	}
	
	
	
	/**
	 * Restituisce un vettore in cui ogni entry è un vettore di 3 elementi: nell'ordine id del modello xml, schemaForest degli xschema associati a quel modello, tutte le entry radice della foresta ottenuta come merge della foresta in posizione 2.
	 * @author Ezio
	 */
	public Vector getInfoSchemaForest(String idPlugFile){		
		return (Vector)this.schemaForestMap.get(idPlugFile);
		
		
	}
	
	public SchemaEntry[] getSchemaEntryRootMerged(String idPlugFile,String idXML){	
		
		Vector info =  (Vector)this.schemaForestMap.get(idPlugFile);
		
		for (int i = 0; i < info.size(); i++) {
			
			if (((String)((Vector)info.get(i)).get(0)).compareTo(idXML)==0)
				return ((SchemaEntry[])((Vector)info.get(i)).get(1));
		}
		
		return null;
	}
	
	
	/*public static long getIdElemento(Class classElement) {
	 
	 long result = -1;
	 try {
	 
	 Method getNumIstanze = classElement.getDeclaredMethod(
	 "getNumIstanze", new Class[] {});//$NON-NLS-1$
	 Object result2 = getNumIstanze.invoke(null, new Object[] {});//$NON-NLS-1$
	 
	 result = ((Long) result2).longValue();
	 
	 } catch (Exception e) {
	 //e.printStackTrace();
	  //NOTA DA INSERIRE NELLA DOCUMENTAZIONE METODO:
	   // significa che la classe dell'elemento non implementa i metodi
	    // richiesti per la gestione dell'identificativo (numero di istanze)
	     // il plugin di salvataggio file gestirà questa eccezzione
	      result = -1;
	      }
	      return result;
	      }*/
	
	public PlugManager getPlugManager() {
		return plugManager;
	}
	
	/**
	 * ritorna un'array contente la lista dei PlugFileFilter
	 * delle classi contenti PlugFile di apertura/salvataggio
	 * @return PlugFileFilter[] se almeno 1 plugIn implementa una funzione di open/save
	 * 					null se nessun plug-in lo implementa
	 */
	public PlugFileFilter[] fileExtention() {
		return fileFilter(this.pluginFile());
	}
	/**
	 * ritorna un'array di plugin descriptor con 
	 * delle classi di file  implementate
	 * @return PlugInDescriptor[] file  oppure null se 
	 *                  nessun plug-file  ? definito
	 */
	private Vector pluginFile() {
		Vector ccc = null;
		
		PlugInDescriptor[] allDescriptor = this.getPlugManager().getPluginRegistry().getAllRecentPluginDescriptor();
		
		for (int i = 0; i < allDescriptor.length; i++) {
			
			if (allDescriptor[i].getPlugFile() != null) {
				if (allDescriptor[i].isPluginFile()) {
					if (ccc == null) {
						ccc = new Vector();
					}
					ccc.add(allDescriptor[i]);
				}
			}
			
		}
		
		if (ccc == null) {
			return null;
		}
		return ccc;
	}
	
	
	
	/**
	 * crea un'array di PlugFileFilter dai plug-in descriptor in ingresso
	 * @param pdArray Array di pluInDescriptor
	 * @return null se non ho nessun plug valido
	 */
	private PlugFileFilter[] fileFilter(Vector pdArray) {
		PlugFileFilter[] pff = null;
		if (pdArray == null) {
			return null;
		}
		
		/*
		 * creazione array
		 */
		pff = new PlugFileFilter[pdArray.size()];
		
		for (int i = 0; i < pdArray.size(); i++) {
			pff[i] = new PlugFileFilter((PlugInDescriptor) pdArray.get(i));
		}
		return pff;
	}
	
	private void addPlugFileInfo(String idPlugFile, File fileWork){
		
		if (this.plugFileInfo==null)
			this.plugFileInfo=new Vector();
		
		for (int i = 0; i < plugFileInfo.size(); i++) {
			
			if (((String)((Vector )plugFileInfo.get(i)).get(0)).compareTo(idPlugFile)==0){
				
				((Vector )plugFileInfo.get(i)).setElementAt(fileWork,1);
				return;
			}
		}
		
		Vector info = new Vector(2);
		info.add(0,idPlugFile);
		info.add(1,fileWork);
		plugFileInfo.add(info);
	}
	
	private File getPathFile(String idPlugFile){
		
		if (plugFileInfo!=null)
			for (int i = 0; i < plugFileInfo.size(); i++) {
				String id = (String)((Vector )plugFileInfo.get(i)).get(0);
				
				if (id.compareTo(idPlugFile)==0)
					return (File)((Vector )plugFileInfo.get(i)).get(1);
			}
		
		return null;
	}
	
	public void setPluginActivated(IMainTabPanel plugin){
		
		this.pluginActivated=plugin;
		
		this.adjustTitle();
		
	}
	
	public PlugInDescriptor getPluginDescriptor(IFilePlug pluginFile){
		
		return this.plugManager.getPluginRegistry().getPluginDescriptorFor(pluginFile);
	}
	
}