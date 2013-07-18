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
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeMap;

import core.internal.datistatici.CharmyFile;
import core.internal.extensionpoint.DeclaredHost;
import core.internal.extensionpoint.ExtensionPointDescriptor;
import core.internal.extensionpoint.HostDescriptor;
import core.internal.extensionpoint.IExtensionPoint;
import core.internal.extensionpoint.IGenericHost;
import core.internal.extensionpoint.event.EventService;
import core.internal.plugin.PlugManager.GrafoDipendenze.Nodo;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.feature.FeatureDescriptor;
import core.internal.plugin.feature.XMLParseFeature;
import core.internal.runtime.GeneralURLClassLoader;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;

//interfacce per la gestione delle estensioni
//import extensionpoint.DataHost;

/**
 * ricerca tutti i plugin, ne costruisce i relativi
 * pluginDescriptor e instanzia le classi trovate, 
 * inoltre gestisce la separazione tra core plug-in ed altri plug
 * @author michele
 * Charmy plug-in
 **/
public class PlugManager {
	
	private EventService eventService;
	
	/**
	 * dati della finestra
	 */
	private PlugDataWin plugDataWin;
	
	/**
	 * dati
	 */
	private PlugDataManager plugDataManager;
	
	private PluginRegistry pluginRegistry;
	
	private InternalError internalError;
	
	private PluginModel[] pluginsCurrentFeature;
	private FeatureDescriptor featureDefault;
	
	private GeneralURLClassLoader generalClassLoader = new GeneralURLClassLoader();
	
	private int ordineAttivazionePlug =0;
	
	/**
	 * vettore per tutti i descrittori di plug-in 
	 */
	//private Vector allDescriptor;
	//private LinkedList editorsPlug;
	//private LinkedList ListaHost;
	/**
	 * Costruttore
	 * @param pdw dati relativi alle finestre
	 * @param pd    classe per i dati
	 */
	public PlugManager(PlugDataWin pdw, PlugDataManager pd) {
		plugDataManager = pd;
		plugDataWin = pdw;
		this.internalError = new InternalError();
		
		this.pluginRegistry = new PluginRegistry(internalError);
		
		//allDescriptor = new Vector();
		//editorsPlug = new LinkedList();
		
	}
	
	/**
	 *  
	 *  1) Vengono parsati i manifest file e le info sono inserite nel registro.
	 *  2) Vengono istanziati i plugin contenuti nel registro
	 *  3) Vengono istanziati gli extension point contenuti nel reistro
	 * 
	 *  4) in seguito viene chiamato il metodo init() dalla classe Interfaccia
	 * MODIFICA: IMPLEMENTATE LE FEATURE - EZIO
	 * 
	 * @author Ezio
	 */
	public void start() {
		
		//Operazioni di avvio :
		
		//attivazione event service per le estensioni
		this.eventService = new EventService();
		
		//vengono parsati tutti i manifest file di tutti i plugin istallati nel package plugins
		//le informazioni vengono raccolte nel registro principale : clASSE PluginRegistry
		processPluginsManifestFile();
		
		//vengono parsati tutti i file feature.xml nellle directory nel package features
		//vengono inseriti le info riguardanti la feature di default (cioè l'elenco dei plug della feature)
		// nel regiatro featureDefault:FeatureDescriptor
		setFeatureDefault();
		
		//viene  creato il grafo delle dipendenze dei plugin da caricare nel framework
		//Il grafo dei plugin enumerati nella feature di default viene completato con i plugin richiesti (sempre che siano presenti)
		GrafoDipendenze grafoDipendenze = new GrafoDipendenze();		
		this.pluginsCurrentFeature = grafoDipendenze.makeGrafoFeaturePlugin(this.pluginRegistry.getAllPluginDescriptor());
		
		
		startPlug(grafoDipendenze); // attiva i plugins in pluginCurrentFeature
		
		//processo delle estensioni: attivazione degli extension point
		startExtensionPoint(grafoDipendenze);
		
	}
	
	/**
	 * 
	 *  Charmy Project
	 *  modificata da: ezio di nisio
	 *  Nuova versione: aggancio delle componenti host e extender di tutti i plug agli extension-point 
	 * * + ricrea le dipendenze fra gli host/extender con l'extension-point su cui si agganciano
	 * + in ogni caso fa anche plug.init()
	 */
	public void init() {
		
		/// plugin -> newPlugData(plugDataManager)	
		
		PlugInDescriptor[] plugins = this.getPluginInFeature();
		
		if (plugins != null)
			for (int i = 0; i < plugins.length; i++) {
				if (plugins[i].isPlugEdit())
					try {
						IPlugData plugData = plugins[i].getIstanceEdit()
						.newPlugData(plugDataManager);
						
						if (plugData!=null)
							plugDataManager.addPlugin(plugData);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		
		////plugin -> setDati(plugDataWin)
		if (plugins != null)
			for (int i = 0; i < plugins.length; i++) {
				if (plugins[i].isPlugEdit())
					try {
						plugins[i].getIstanceEdit().setDati(plugDataWin);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		
		// plugin -> initHost , i plugin istanziano le componenti host dichiarate in plugin.xml
		this.initHost();
		
		// PROCESSO DELLE ESTENSIONI - aggancio delle componenti host di tutti i plug ai relativi extension-point
		this.extensionProcess();
		
		////init sui plugin -  forse si potrebbe eliminare
		if (plugins != null)
			for (int i = 0; i < plugins.length; i++) {
				if (plugins[i].isPlugEdit())
					try {
						plugins[i].getIstanceEdit().init();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
			}
		
		// (report su file da implementare, facile...)visualizziamo eventuali errori, warning allo start-up
		this.internalError.displayError();
		
	}
	
	/**
	 * Vengono parsati tutti i manifest file (plugin.xml)dei plugin istallati
	 *  e le informazioni vengono inserite nel registro.
	 * 
	 *
	 *@author Ezio
	 */
	private void processPluginsManifestFile() {
		
		
		
		String dirPlugin = CharmyFile.dirPlug();
		/* preleva la lista delle directory in dirPlugin */
		
		Collection sm = getListaOrder(dirPlugin);
		if (sm == null)
			return;
		Iterator ite = sm.iterator();
		
		while (ite.hasNext()) {
			File f = new File(filePlug(dirPlugin, (String) ite.next()));
			
			//il files esiste e pu? essere parsato
			if ((f.exists()) && (f.isFile())) {
				
				XMLParsePlug xmlParser;
				xmlParser = new XMLParsePlug(this.internalError,this.pluginRegistry);
				
				PlugInDescriptor pd = xmlParser.parseFile(f);
				
				//////ESEGUIRE CONTROLLI SUL DESCRIPTOR PER VALORI OBBLIGATORI - PER EVITARE IN SEGUITO ECCEZZIONI DI PUNTAMENTO A NULL
				if ((pd != null) && (vincoliRispettati(pd))) {
					
					this.pluginRegistry.addPlugin(pd);
					pd.setGeneralClassLoader(this.generalClassLoader);
				}
				
			}
			
		}
	}
	
	/**
	 * ritorna i plugin elencati nel file di feature di default.
	 * - PluginModel[0] se nella feature di default non sono elencati plugin 
	 * - null se non esiste una feature di default o si sono riscontrati errori fatali nel parsing della feature di default.
	 * 
	 * 
	 * @author Ezio Di Nisio
	 * @return
	 */
	private  FeatureDescriptor processFeature() {
		
		String dirFeatures = CharmyFile.dirFeature();
		
		Collection sm = getListaOrder(dirFeatures);
		
		if (sm == null)
			return null;
		Iterator ite = sm.iterator();
		ArrayList lFileXml = new ArrayList();
		while (ite.hasNext()) {
			File f = new File(fileFeature(dirFeatures, (String) ite.next()));
			
			if ((f.exists()) && (f.isFile())) {
				
				lFileXml.add(f);
			}
			
		}
		
		File[] listaFileFeature = (File[]) lFileXml.toArray(new File[lFileXml
		                                                             .size()]);
		
		return this.parseFeature(listaFileFeature);
		
	}
	
	/**
	 * @author Ezio
	 * @param listaFileFeature
	 * @return
	 */
	private FeatureDescriptor parseFeature(File[] listaFileFeature) {
		
		for (int i = 0; i < listaFileFeature.length; i++) {
			
			XMLParseFeature xmlParser;
			xmlParser = new XMLParseFeature(this.internalError);
			
			FeatureDescriptor featureDescriptor = xmlParser.parseXmlFeature(listaFileFeature[i]);
			if (featureDescriptor!=null)
				if (featureDescriptor.isFeatureDefault()){
					
					return featureDescriptor;
				}
			
		}
		return null;
	}
	
	/**
	 * Vengono processate le features istallate.
	 * Rispetto alla feature di default, su tutti i plugin elencati viene settato a true il flag feature del plugin.
	 * 
	 * Se non c'è una feature di default definita, viene considerata la feature di default
	 * con tutti i plugin con versione più recente istallati.
	 * 
	 *@author Ezio Di Nisio
	 *@return true se si riesce a caricare una feature di default definita.
	 * false altrimenti. In questo caso la feature di default diventa tutti i plugin istallati con versione più recente.
	 *  
	 */
	private boolean setFeatureDefault() {
		
		boolean result = true; 
		this.featureDefault = this.processFeature();
		
		if (featureDefault != null)
			for (int i = 0; i < featureDefault.getListaPluginFeature().length; i++) {
				
				PlugInDescriptor[] allPlugin = this.pluginRegistry
				.getAllPluginDescriptor();
				
				boolean ok = false;
				if (allPlugin != null)
					for (int j = 0; j < allPlugin.length; j++) {
						
						if (featureDefault.getListaPluginFeature()[i].matchModel(allPlugin[j])) {
							
							allPlugin[j].setInFeature(true);
							ok = true;
						}
					}
				
				if (!ok)
					this.internalError
					.addError(
							Error.ERROR,
							featureDefault.getListaPluginFeature()[i].getIdentifier(),
							featureDefault.getListaPluginFeature()[i].getVersion(),
							"plugin defined in default feature not found. See log file",
							null);
				
			}
		else {////SE NON TROVA LA FEATURE DI DEFAULT NE CREIAMO UNA DI SISTEMA. (TUTTI I PLUGIN PIù RECENTI)
			//CARICHIAMO UNA FEATURE CON ATTIVI TUTTI I PLUGIN ISTALLATI CON LA VERSION EPIU' RECENTE
			
			result = false;
			this.featureDefault= new FeatureDescriptor("Feature di Sistema","1.0.0");
			PlugInDescriptor[] allPlugToLoad = this.pluginRegistry
			.getAllRecentPluginDescriptor();
			for (int i = 0; i < allPlugToLoad.length; i++) {
				
				featureDefault.addPluginFeature(allPlugToLoad[i].getIdentifier(),allPlugToLoad[i].getVersion());
				allPlugToLoad[i].setInFeature(true);
				
			}
			
		}
		return result;
	}
	
	/**
	 * Istanzia i plugin nel sistema 
	 * rispettando il grafo delle dipendenze fra i plugin
	 * 
	 */
	private void startPlug(GrafoDipendenze grafoDipendenze) {
		
		
		Nodo[] grafoPlugin = grafoDipendenze.makeGrafoMarcabile(this.pluginsCurrentFeature);
		
		///prima vengono istanziati i plug (nodi grafo) senza dipendenze ,marchiamo il nodo ed istanziamo il plug
		if (grafoPlugin != null)
			for (int i = 0; i < grafoPlugin.length; i++) {
				
				PluginModel[] listaArchiUscenti = grafoPlugin[i]
				                                              .getListaArchiUscenti();
				
				if (listaArchiUscenti == null) {
					////marchiamo il nodo : T = marcato , F = non marcato
					grafoPlugin[i].setMarchio('T');
					
					// istanziamo il plug
					
					PlugInDescriptor pd = (PlugInDescriptor) grafoPlugin[i]
					                                                     .getDataNodo();
					try {
						
						this.istanzaPlugEdit(pd); ///
						pd.setOrdineDiAttivazione(this.ordineAttivazionePlug);
						ordineAttivazionePlug++;
					} catch (Exception e) {
						e.printStackTrace(); //// IMPORTANTE: GESTIRE ECCEZzIONE
					}
					
				}
			}
		
		// vengono istanziati i nodi (plugin) i cui archi uscenti arrivano tutti su nodi già istanziati      
		while (!grafoDipendenze.IsGrafoMarcato(grafoPlugin)) {
			
			for (int i = 0; i < grafoPlugin.length; i++) {
				
				if (grafoDipendenze
						.IsNodoMarcabile(grafoPlugin[i], grafoPlugin)) {
					
					// marchiamo il nodo ed istanziamo il plugin
					grafoPlugin[i].setMarchio('T');
					
					PlugInDescriptor pd = (PlugInDescriptor) grafoPlugin[i]
					                                                     .getDataNodo();
					try {
						this.istanzaPlugEdit(pd);
						pd.setOrdineDiAttivazione(this.ordineAttivazionePlug);
						ordineAttivazionePlug++;
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
			}
			
		}
	}
	
	/**
	 * 
	 *  Charmy Project
	 *  @author ezio di nisio
	 *  Per ogni plugin edit/algo istanzia ed inizializza tutti gli extension-point.
	 */
	private void startExtensionPoint(GrafoDipendenze grafoDipendenze) {
		
		ExtensionPointDescriptor[] allExtensionPointFeature = this.getExtensionPointInFeature();
		
		// istanziamo gli extension point
		for (int i = 0; i < allExtensionPointFeature.length; i++) {
			this.IstanceExtPoint(allExtensionPointFeature[i]);
			
		}
		
		// inizializzazione degli extension point
		for (int i = 0; i < allExtensionPointFeature.length; i++) {
			
			allExtensionPointFeature[i].getExtensionPointReference().init(this,
					allExtensionPointFeature[i]);
		}
	}
	
	/**
	 * crea una istanza di una classe ricavato dal plugIndescriptor
	 * @param pd PlugInDescriptor che descrive la classe da istanziare
	 * 					e le funzioni di ingresso.
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private IMainTabPanel istanzaPlugEdit(PlugInDescriptor pd) {
		
		if (!pd.isPlugEdit()) {
			return null;
		}
		IMainTabPanel imp = null;
		try {
			imp = pd.getIstanceEdit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//imp.setDati(plugDataWin);
		return imp;
	}
	
	/**
	 * 
	 *  Charmy Project
	 *  @author ezio di nisio
	 *  Istanzia un extension point 
	 */
	private IExtensionPoint IstanceExtPoint(
			ExtensionPointDescriptor extPointDescriptor) {
		
		Class extPointClass = null;
		IExtensionPoint refExtPoint = null;
		String classpath = extPointDescriptor.getClassPath();
		try {
			//extPointClass = Class.forName(classpath);
			extPointClass = generalClassLoader.loadClass(classpath);
			refExtPoint = (IExtensionPoint) extPointClass.newInstance();
			
			//inseriamo il reference nella lista del descriptor
			extPointDescriptor.setExtensionPointReference(refExtPoint);
			
		} catch (ClassNotFoundException e) {
			return null;
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (SecurityException e) {
			return null;
		} catch (ExceptionInInitializerError e) {
			return null;
		} catch (ClassCastException e) {
			
			return null;
		}
		
		return refExtPoint;
	}
	
	private void initHost() {
		
		//INIZIALIZZAZIONE PLUG - FASE 1
		//lanciamo initHost(), per ogni plug,  il plug restituisce le 
		//istanze dei suoi host Component 
		
		PlugInDescriptor[] plugins = this.getPluginInFeature();
		
		// per ogni plugin
		if (plugins != null)
			for (int i = 0; i < plugins.length; i++) {
				
				if (plugins[i].isPlugEdit())
					try {
						IMainTabPanel refPlug = plugins[i].getIstanceEdit();
						if (refPlug != null) {
							
							// in initHost il plugin istanzia (se vuole) qualche host definito nel manifest file
							DeclaredHost[] hostsRuntime = null;
							hostsRuntime = refPlug.initHost();
							
							if (hostsRuntime == null)
								// per evitare errori di puntamento a null
								hostsRuntime = new DeclaredHost[0];
							
							HostDescriptor[] hostsDeclaredInManifest = plugins[i]
							                                                   .getHosts();
							
							//per ogni host definito nel manifest file del plug corrente
							if (hostsDeclaredInManifest != null)
								for (int j = 0; j < hostsDeclaredInManifest.length; j++) {
									
									String currentIdHost = hostsDeclaredInManifest[j]
									                                               .getId();
									
									boolean atRuntime = false;
									for (int k = 0; k < hostsRuntime.length; k++) {
										if (hostsRuntime[k].getIdHost()
												.compareTo(currentIdHost) == 0) {
											atRuntime = true;
											
											// l'host è stato istanziato a runtime dal plugin, settiamo il reference
											hostsDeclaredInManifest[j]
											                        .setHostReference(hostsRuntime[k]
											                                                       .getHost());
											IGenericHost host = hostsRuntime[k].getHost();
											
											//host.setPluginOwner(refPlug); 
											
											Method setPluginOwner = host.getClass().getMethod("setPluginOwner", new Class[] {IMainTabPanel.class});
											setPluginOwner.invoke(host, new IMainTabPanel[] {(IMainTabPanel)refPlug});
											
											IExtensionPoint extPoint = this.getExtensionPoint(hostsDeclaredInManifest[j].getExtensionPoint());
											Method setExtensionPointOwner = host.getClass().getMethod("setExtensionPointOwner", new Class[] {IExtensionPoint.class});
											setExtensionPointOwner.invoke(host, new Object[] {extPoint});
											
											//host.setPluginOwner(refPlug);											
											//host.setExtensionPointOwner();
											break;
										}
									}
									
									// l'host non è stato istanziato a runtime, se esiste un costruttore senza parametri lo istanzia il sistema
									if (!atRuntime) {
										
										try {
											
											//Class HostClass = Class
											//.forName(hostsDeclaredInManifest[j]
											//							 .getClassPath());
											Class HostClass = generalClassLoader.loadClass(hostsDeclaredInManifest[j].getClassPath());
											// l'host non va istanziato se il plugin  contemporaneamente si comporta da host
											
											if (!HostClass
													.isInstance(plugins[i]
													                    .getIstanceEdit())) {
												
												IGenericHost hostRef = (IGenericHost) HostClass
												.newInstance();
												
												// se non ci sono state eccezioni inserimao il reference dell'host nel descriptor
												hostsDeclaredInManifest[j]
												                        .setHostReference(hostRef);
												
												
												Method setPluginOwner = hostRef.getClass().getMethod("setPluginOwner", new Class[] {IMainTabPanel.class});
												setPluginOwner.invoke(hostRef, new Object[] {refPlug});
												
												IExtensionPoint extPoint = this.getExtensionPoint(hostsDeclaredInManifest[j].getExtensionPoint());
												Method setExtensionPointOwner = hostRef.getClass().getMethod("setExtensionPointOwner", new Class[] {IExtensionPoint.class});
												setExtensionPointOwner.invoke(hostRef, new Object[] {extPoint});
												
												
												//hostRef.setPluginOwner(refPlug);
												
												//hostRef.setExtensionPointOwner(this.getExtensionPoint(hostsDeclaredInManifest[j].getExtensionPoint()));
												
												
											} else{
												hostsDeclaredInManifest[j].setHostReference((IGenericHost) plugins[i].getIstanceEdit());
												
												IGenericHost host = (IGenericHost) plugins[i].getIstanceEdit();
												
												Method setPluginOwner = host.getClass().getMethod("setPluginOwner", new Class[] {IMainTabPanel.class});
												setPluginOwner.invoke(host, new Object[] {refPlug});
												
												IExtensionPoint extPoint = this.getExtensionPoint(hostsDeclaredInManifest[j].getExtensionPoint());
												Method setExtensionPointOwner = host.getClass().getMethod("setExtensionPointOwner", new Class[] {IExtensionPoint.class});
												setExtensionPointOwner.invoke(host, new Object[] {extPoint});
												
												//().setPluginOwner(refPlug);
												
												//((IGenericHost) plugins[i].getIstanceEdit()).setExtensionPointOwner(this.getExtensionPoint(hostsDeclaredInManifest[j].getExtensionPoint()));
												                                                                              
											}
											
										} catch (Exception e) {
											
											//non facciamo nulla: il reference dell'host nel descriptor rimane a null;
											e.printStackTrace();
										}
										
									}
								}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
	}
	
	/** 
	 *  Charmy Project -
	 * Vengono processate le richieste di aggancio degli host da 
	 * parte dei plugin sugli extension point. Nell'aggancio si segue il grafo delle dipendenze fra ext-point- 
	 * L'extension point esegue l'aggancio vero e proprio, se ritorna errore/null l'host viene rimosso dalla lista del PlugManager
	 * 
	 *  @author ezio di nisio
	 *
	 */
	private void extensionProcess() {
		
		GrafoDipendenze grafoDipendenze = new GrafoDipendenze();
		
		Nodo [] grafoExtensionPoint = grafoDipendenze.makeGrafoMarcabile(this.getExtensionPointInFeature());
		
		///prima le radici senza dipendenze , e li marchiamo
		for (int y = 0; y < grafoExtensionPoint.length; y++) {
			
			PluginModel[] listaArchiUscenti = grafoExtensionPoint[y].getListaArchiUscenti();
			
			if (listaArchiUscenti == null) {
				////marchiamo il nodo
				grafoExtensionPoint[y].setMarchio('T');
				
				//ATTACH TUTTI GLI HOST DI QUESTO EXTENSION-POINT
				//QUI
				this.notifyHostAttach((ExtensionPointDescriptor)grafoExtensionPoint[y].getDataNodo());
			}
		}
		
		while (!grafoDipendenze.IsGrafoMarcato(grafoExtensionPoint)) {
			
			for (int y = 0; y < grafoExtensionPoint.length; y++) {
				
				if (grafoDipendenze.IsNodoMarcabile(grafoExtensionPoint[y],grafoExtensionPoint)){
					
					grafoExtensionPoint[y].setMarchio('T');
					this.notifyHostAttach((ExtensionPointDescriptor)grafoExtensionPoint[y].getDataNodo());
					
				}
			}
		}
		
		//////////FINE PERCORSO GRAFO   
		
	}
	
	/** 
	 *  Charmy Project -
	 *  Notifica ad un extension point(idExtPoint) la richiesta di attacco degli host da parte di tutti i plugin
	 *  @author ezio di nisio
	 *
	 */
	private void notifyHostAttach(ExtensionPointDescriptor extPointDescriptor) {
		
		IExtensionPoint extensionPoint = this.getExtensionPoint(extPointDescriptor.getIdentifier());
		
		HostDescriptor[] list = this.pluginRegistry
		.getHostsForExtPointId(extPointDescriptor.getIdentifier());
		
		if (list != null)
			for (int i = 0; i < list.length; i++) {
				
				if (list[i].getHostReference() != null) {
					
					//  gli host richiesti devono essere tutti istanziati
					
					String[] hostsRequired = list[i].getHostRequired();
					if (hostsRequired != null)
						for (int k = 0; k < hostsRequired.length; k++) {
							
							if (this.getHost(hostsRequired[k]) == null) {
								
								// rimuoviamo l'host dal regitro - corretto???
								this.pluginRegistry.removeHost(list[i].getId());
								return;
							}
							
						}
					
					//list[i].getHostReference().setExtensionPointOwner(extensionPoint);
					
					boolean result = extensionPoint.addHost(list[i]);
					
					if (!result) { // l'aggancio non è andato a buon fine - rimuoviamo l'host dai registri
						
						this.pluginRegistry.removeHost(list[i].getId());
					} else{
						list[i].setActivated(true);
						list[i].getHostReference().setEventService(this.eventService);
						
					}
					
				}
				
			}
		
	}
	
	public class GrafoDipendenze {
		
		//private PluginModel[] grafoCompletoCoerente;
		
		//private PluginModel[] sottoGrafoFeature;
		
		//private Nodo[] grafo;
		
		/**
		 * 
		 * @author Ezio
		 *
		 * TODO To change the template for this generated type comment go to
		 * Window - Preferences - Java - Code Style - Code Templates
		 */
		public class Nodo {
			private char marchio = 'F';
			
			//private GrafoDipendenze grafo;
			private PluginModel dataNodo;
			
			public Nodo(PluginModel dataNodo) {
				
				//this.grafo=grafo;			
				this.dataNodo = dataNodo;
				
			}
			
			/**
			 * @return Returns the dataNodo.
			 */
			public PluginModel getDataNodo() {
				return dataNodo;
			}
			
			/**
			 * @return Returns the listaArchiUscenti.
			 */
			public PluginModel[] getListaArchiUscenti() {
				return this.dataNodo.dependenceList;
			}
			
			/**
			 * @return Returns the marchio.
			 */
			public char getMarchio() {
				return marchio;
			}
			
			/**
			 * @param marchio The marchio to set.
			 */
			public void setMarchio(char marchio) {
				this.marchio = marchio;
			}
			
		}
		
		/**
		 * 
		 * @param listaNodiCandidati
		 */
		public GrafoDipendenze() {
			
			
			//AGGIUSTAMENTO GRAFO - VERSIONI PLUG RICHIESTI ASSENTI
			
		}
		
		private PluginModel[] verificaCoerenza(PluginModel[] grafo){
			
			/////////VERIFICA COORENZA GRAFO - fare un controllo sulla struttura del grafo - proprietà grafo da verificare: diretto e aciclico.
			
			ArrayList result = new ArrayList();
			
			if (grafo != null)
				for (int i = 0; i < grafo.length; i++) {
					PluginModel[] listaCorrenteArchiUscenti = grafo[i]
					                                                .getDependenceList();
					if (listaCorrenteArchiUscenti.length==0)
						result.add(grafo[i]); // nodo pozzo
					
				}
			
			
			boolean finito = false;
			while (!finito){
				finito=true;
				if (grafo != null)
					for (int i = 0; i < grafo.length; i++) {
						
						PluginModel[] listaCorrenteArchiUscenti = grafo[i].getDependenceList();
						//((PluginModel)result.get(i)).getDependenceList() ;
						
						
						// aggiungiamo il candidato solo se tutti gli archi uscenti arrivano su nodi che esistono.
						boolean ok = true;
						if (listaCorrenteArchiUscenti.length!=0){
							
							for (int j = 0; j < listaCorrenteArchiUscenti.length; j++) {
								
								if (!this.nodoExist(listaCorrenteArchiUscenti[j],
										grafo)) {
									// se è null significa che il nodo non esiste in questo grafo
									ok = false;
									finito=false;
									break;
								}
							}
							
							if (ok){
								if (!result.contains(grafo[i]))
									result.add(grafo[i]);
							}
							else
								if (result.contains(grafo[i]))
									result.remove(grafo[i]);
							
							
						}
						
					}
				
				grafo =(PluginModel[]) result
				.toArray(new PluginModel[result.size()]);
				
			}
			
			return grafo;
		}
		
		private PluginModel[] getSottoGrafoCompleto(PluginModel[] grafo,
				PluginModel nodoIniziale) {
			
			ArrayList result = new ArrayList();
			Stack stackAppoggio = new Stack();
			
			stackAppoggio.push(nodoIniziale);
			
			while (!stackAppoggio.isEmpty()) {
				
				PluginModel nodoCorrente = (PluginModel) stackAppoggio.pop();
				
				if (!result.contains(nodoCorrente)) {
					
					result.add(nodoCorrente);
					PluginModel[] listaArchiUscenti = nodoCorrente
					.getDependenceList();
					
					if (listaArchiUscenti != null)
						for (int i = 0; i < listaArchiUscenti.length; i++) {
							
							PluginModel nodoAdiacente = null;
							for (int j = 0; j < grafo.length; j++) {
								
								if (grafo[j].matchModel(listaArchiUscenti[i])) {
									nodoAdiacente = grafo[j];
									break;
								}
							}
							if (nodoAdiacente != null)
								stackAppoggio.push(nodoAdiacente);
							
						}
					
				}
				
			}
			
			return (PluginModel[]) result
			.toArray(new PluginModel[result.size()]);
		}
		
		public PluginModel[] makeGrafoFeaturePlugin(PluginModel[] allPluginInstalled) {
			
			ArrayList result = new ArrayList();
			
			PluginModel[] grafoCompletoCoerente = verificaCoerenza(allPluginInstalled);
			
			
			if (grafoCompletoCoerente != null)
				for (int i = 0; i < grafoCompletoCoerente.length; i++) {
					
					// (NOTA: un grafo feature  necessariamente avrà almeno un nodo sorgente)
					// prendiamo il primo nodo in feture e aggiungiamo tutte le dipendenze fino ai pozzi (visita in profondità)
					// solo se quello che aggiungiamo non è già nel contenitore
					
					if ((grafoCompletoCoerente[i].isInFeature())) {
						
						if (!result.contains(grafoCompletoCoerente[i])) {
							
							result.add(grafoCompletoCoerente[i]);
							PluginModel[] subGrafoFeature = this
							.getSottoGrafoCompleto(
									grafoCompletoCoerente,
									grafoCompletoCoerente[i]);
							
							if (subGrafoFeature != null)
								for (int j = 0; j < subGrafoFeature.length; j++) {
									
									if (!result.contains(subGrafoFeature[j]))
										result.add(subGrafoFeature[j]);
								}
						}
						
					}
				}
			return (PluginModel[]) result
			.toArray(new PluginModel[result.size()]);
			
		}
		
		public Nodo[] makeGrafoMarcabile(PluginModel[] nodi) {
			
			if (nodi == null)
				return new Nodo[0];
			
			Nodo[] result = new Nodo[nodi.length];
			for (int i = 0; i < nodi.length; i++) {
				
				result[i] = new Nodo(nodi[i]);
				
			}
			return result;
		}
		
		public boolean IsGrafoMarcato(Nodo[] grafoMarcabile) {
			
			if (grafoMarcabile==null)
				return true;
			for (int i = 0; i < grafoMarcabile.length; i++) {
				
				if (grafoMarcabile[i].getMarchio() == 'F')
					return false;
			}
			
			return true;
		}
		
		/**
		 * ritorna il nodo associato all'oggetto plug passato come argomento.
		 * la ricerca è fatta nel grafo passato come argomento.
		 * se ritorna null significa che il nodo non è presente nel grafo passato come argomento .
		 * @param dataNodo
		 * @return
		 */
		public Nodo getNodo(PluginModel dataNodo, Nodo[] grafo) {
			
			if ((grafo == null) || (dataNodo == null))
				return null;
			
			for (int i = 0; i < grafo.length; i++) {
				
				if (dataNodo.matchModel(grafo[i].getDataNodo()))
					return grafo[i];
			}
			
			return null;
		}
		
		public boolean nodoExist(PluginModel dataNodo, PluginModel[] grafo) {
			
			if ((grafo == null) || (dataNodo == null))
				return false;
			
			for (int i = 0; i < grafo.length; i++) {
				
				if (dataNodo.matchModel(grafo[i]))
					return true;
			}
			
			return false;
		}
		
		/**
		 * 
		 * PER DEFINIZIONE
		 * Un nodo di un grafo (Nodo[])è definito marcabile se:
		 * 1) è già marcato, oppure
		 * 2)non ha archi uscenti, oppure
		 * 3) gli archi uscenti arrivano tutti su nodi già marcati
		 * 
		 * 
		 * @param grafo
		 * @author Ezio Di Nisio
		 * @return true se il nodo è marcabile, false altrimenti
		 */
		private boolean IsNodoMarcabile(Nodo nodo, Nodo[] grafo) {
			
			if (nodo == null)
				return false;
			
			if (nodo.getMarchio() == 'T') // già marcato
				return false;
			
			PluginModel[] archiUscenti = nodo.getListaArchiUscenti();
			
			if (archiUscenti == null)
				return true; // nodo pozzo
			
			if (archiUscenti.length == 0)
				return true; // nodo pozzo
			
			int cont = 0;
			for (int i = 0; i < archiUscenti.length; i++) {
				
				if (this.getNodo(archiUscenti[i], grafo).getMarchio() == 'T')
					cont++;
				//	this.getListaArchiUscenti()[i].
				
			}
			
			if (cont == archiUscenti.length)
				return true;
			else
				return false;
			
		}
		
		private boolean isNodoSorgente(PluginModel nodo, PluginModel[] grafo) {
			
			int test = -1;
			if ((grafo != null) && (nodo != null))
				for (int i = 0; i < grafo.length; i++) {
					PluginModel[] listaCorrenteArchiUscenti = grafo[i]
					                                                .getDependenceList();
					test = 1;
					if (listaCorrenteArchiUscenti != null)
						for (int j = 0; j < listaCorrenteArchiUscenti.length; j++) {
							
							if (listaCorrenteArchiUscenti[j].matchModel(nodo)) {
								
								test = 0;
								break;
							}
						}
					if (test == 0)
						break;
					
				}
			
			if (test == -1)
				return false;
			
			if (test == 0)
				return false;
			
			if (test == 1)
				return true;
			
			return false;
			
		}
		
	}
	
	/**
	 * Controlliamo i vincoli che deve rispettare un plugin per poter essere inserito nel registro
	 * - tipo id, classe , ... dichiararti nel manifest file ->>>>>> DA IMPLEMENTARE ---
	 * @param pd
	 * @return
	 */
	private boolean vincoliRispettati(PlugInDescriptor pd) {
		
		//CONTROLLO SULL'ESISTENZA DELLE CLASSI DI STARTUP DEL PLUG
		/*if (pd.isPlugEdit())
		 try {
		 Class.forName(pd.getPlugEditor().getEntryPoint());
		 } catch (ClassNotFoundException e) {
		 //esiste il manifest file del plug ma non esiste la classe di start up. Rimuoviamo il descriptor dal registro
		  
		  this.internalError.addError(Error.FATAL_ERROR,
		  pd.getIdentifier(), pd.getVersion(),
		  "entry point class missed", e);
		  return false;
		  }
		  
		  if (pd.isPlugOpen())
		  try {
		  Class.forName(pd.getPlugFile().getClassOpen());
		  } catch (ClassNotFoundException e) {
		  //esiste il manifest file del plug ma non esiste la classe di start up. Rimuoviamo il descriptor dal registro
		   this.internalError.addError(Error.FATAL_ERROR,
		   pd.getIdentifier(), pd.getVersion(),
		   "entry point class missed", e);
		   return false;
		   }
		   if (pd.isPlugSave())
		   try {
		   Class.forName(pd.getPlugFile().getClassSave());
		   } catch (ClassNotFoundException e) {
		   //esiste il manifest file del plug ma non esiste la classe di start up. Rimuoviamo il descriptor dal registro
		    this.internalError.addError(Error.FATAL_ERROR,
		    pd.getIdentifier(), pd.getVersion(),
		    "entry point class missed", e);
		    return false;
		    }*/
		
		// DA IMPLEMENTARE ALTRI CONTROLLI SU VINCOLI DSCRIPTOR-PLUG
		
		return true;
	}
	
	/**
	 * 
	 *  Charmy Project
	 *  @author ezio di nisio
	 *
	 */
	/*	private ElementHost schemaRispected (ElementHost elementHost, IMainTabPanel plug){          
	 
	 PlugInDescriptor plugDescriptor = this.getPlugEditDescriptor(plug);
	 LinkedList listaHost = plugDescriptor.getListaHost();
	 
	 
	 for(int i=0;i<listaHost.size();i++){
	 
	 ElementHost checkElement = (ElementHost)listaHost.get(i);
	 //check 1) l'host id dichiarato a runtime deve mettchare con un id di un host dichiarato nello schema.
	  if (elementHost.getIdHost().compareTo(checkElement.getIdHost())==0){
	  
	  //check 2) - l'extension point  deve esistere
	   for(int k=0;k<extensionPoints.size();k++){                	
	   String checkExtPoint = (String)((Vector)extensionPoints.get(k)).get(0);
	   
	   if (checkExtPoint.compareTo(checkElement.getExtPoint())==0){
	   // check 3) gli host required devono essere dichiarati se l'ext point ha dipendenze
	    //da implementare
	     
	     // check 4) deve essere rispettata la molteplicità dell'extension point
	      if (!isMolteplicitaRaggiunta(checkExtPoint)){
	      
	      checkElement.setPlugOwner(plug);
	      checkElement.setHost(elementHost.getHost());
	      this.setMolteplicitaRaggiunta(checkExtPoint);
	      
	      return checkElement;
	      }
	      
	      }                	
	      }        		
	      }        	
	      }
	      
	      return null; 
	      }*/
	
	/**
	 * 
	 *  Charmy Project
	 * 
	 
	 * @author ezio di nisio     
	 * @return IExtensionPoint - Ritorna il reference all'extension-point 
	 * con identificatore uguale a quello passato come argomento, null se non c'è. 
	 */
	public IExtensionPoint getExtensionPoint(String extPointId) {
		
		return this.pluginRegistry.getExtensionPointForId(extPointId)
		.getExtensionPointReference();
		
	}
	
	/**
	 * 
	 */
	public ExtensionPointDescriptor getExtensionPointDescriptor(
			String idExtPoint) {
		
		return this.pluginRegistry.getExtensionPointForId(idExtPoint);
	}
	
	public IGenericHost getHost(String idHost) {
		
		HostDescriptor host = this.pluginRegistry.getHostForId(idHost);
		if (host != null)
			return host.getHostReference();
		
		return null;
	}
	
	/**
	 * crea un'array di plug-in ordinato per nome
	 * @return ritorna una Collection Ordinata  di nomi di file 
	 * 				oppure null se la Collection ? vuota
	 */
	public Collection getListaOrder(String directory) {
		
		File fLista = new File(directory);
		TreeMap sm = null;
		if (!fLista.exists()) {
			return null;
		}
		String lista[] = fLista.list(new FiltraDir());
		if (lista.length > 0) {
			sm = new TreeMap();
			for (int i = 0; i < lista.length; i++) {
				sm.put(lista[i], lista[i]);
			}
		}
		if (sm == null)
			return null;
		return sm.values();
		
	}
	
	
	
	
	/**
	 * ritorna un riferimento al plug-in editor con 
	 * id ottenuto dal file xml = identificativo
	 * @return IMainTabPanel rappresentante il plug  oppure null se 
	 *                  nessun plug ha quell'identificativo
	 */
	public IMainTabPanel getPlugEdit(String identificativo)
	throws ClassNotFoundException, InstantiationException,
	IllegalAccessException {
		
		//PlugInDescriptor[] allDescriptor = this.pluginRegistry
		//.getAllRecentPluginDescriptor();
		
		
		for (int i = 0; i < this.pluginsCurrentFeature.length; i++) {
			
			PlugInDescriptor pd = (PlugInDescriptor)pluginsCurrentFeature[i];
			if (pd.getIdentifier().equals(identificativo)) {
				if (pd.isPlugEdit()) {
					return pd.getIstanceEdit();
				} 
			}
		}
		
		return null;
	}
	
	/**
	 *  restituisce il PlugInDescriptor relativo al plug edit passato come argomento. 
	 * 
	 *  Charmy Project
	 *  @author ezio di nisio
	 *
	 */
	public PlugInDescriptor getPlugEditDescriptor(IMainTabPanel plug) {
		
		return this.pluginRegistry.getPluginDescriptorFor(plug);
	}
	
	/**
	 * restituisce il descriptor del plugin in feature passato come argomento.
	 * Se nella feature corrente non è presente il plug viene restituito il descriptor con versione più recente istallato, null altrimenti.
	 * @param idPlugin 
	 * @return
	 */
	public PlugInDescriptor getPluginDescriptor(String idPlugin) {
		
		for (int i = 0; i < this.pluginsCurrentFeature.length; i++) {
			
			if(((PlugInDescriptor)pluginsCurrentFeature[i]).getIdentifier().compareTo(idPlugin)==0){
				return (PlugInDescriptor)pluginsCurrentFeature[i];
			}
		}
		return this.pluginRegistry.getPluginDescriptor(idPlugin);
	}
	
	/**
	 * ritorna una stringa del filename plugin.xml
	 * completo di  percorso assoluto
	 * 
	 * @param absFile directory da agganciare
	 * @return dir + FileSeparator + absFile + FileSeparator + plugin.xml
	 */
	private String filePlug(String dir, String absFile) {
		String d = CharmyFile.addFileSeparator(dir).concat(absFile);
		return CharmyFile.addFileSeparator(d)
		.concat(CharmyFile.PLUGIN_FILENAME);
	}
	
	/**
	 * ritorna una stringa del filename feature.xml
	 * completo di  percorso assoluto
	 * @author Ezio
	 * @param absFile directory da agganciare
	 * @return dir + FileSeparator + absFile + FileSeparator + feature.xml
	 */
	private String fileFeature(String dir, String absFile) {
		String d = CharmyFile.addFileSeparator(dir).concat(absFile);
		return CharmyFile.addFileSeparator(d)
		.concat(CharmyFile.FEATURE_FILENAME);
	}
	
	/**
	 * Inner class per implementare un filtro di file
	 * per cercare sole le directory
	 * @author michele
	 * Charmy plug-in
	 *
	 */
	public class FiltraDir implements FilenameFilter {
		
		/* (non-Javadoc)
		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
		 */
		public boolean accept(File dir, String name) {
			String direct = dir.getAbsolutePath();
			File f = new File(CharmyFile.addFileSeparator(direct).concat(name));
			return f.isDirectory();
		}
	}
	
	/**
	 * Preleva il contenitore dei dati
	 * @return Contenitore centralizzato dei dati
	 */
	public PlugDataManager getPlugData() {
		return plugDataManager;
	}
	
	/**
	 * Preleva il contenitore della finestra
	 * @return Contenitore dati finestre
	 */
	public PlugDataWin getPlugDataWin() {
		return plugDataWin;
	}
	 
	
	/**
	 * @return Returns the eventService.
	 */
	public EventService getEventService() {
		return eventService;
	}
	
	/**
	 * @return Returns the pluginRegistry.
	 */
	public PluginRegistry getPluginRegistry() {
		return pluginRegistry;
	}
	
	public PlugInDescriptor[] getPluginInFeature() {
		
		PlugInDescriptor[] pd = new PlugInDescriptor[this.pluginsCurrentFeature.length];
		for (int j = 0; j < this.pluginsCurrentFeature.length; j++) {
			
			pd[j]=(PlugInDescriptor)pluginsCurrentFeature[j];
		}
		
		return pd;
	}
	
	public ExtensionPointDescriptor[] getExtensionPointInFeature() {
		
		ArrayList extensionPointFeature = new ArrayList();
		PlugInDescriptor[] list = this.getPluginInFeature();
		for (int i = 0; i < list.length; i++) {
			
			ExtensionPointDescriptor[] currentExtPoint = list[i].getExtensionPoints();
			if(currentExtPoint!=null)
				for (int j = 0; j < currentExtPoint.length; j++) {
					extensionPointFeature.add(currentExtPoint[j]);
				}
		}
		return (ExtensionPointDescriptor[]) extensionPointFeature
		.toArray(new ExtensionPointDescriptor[extensionPointFeature.size()]);
	}
	/**
	 * @return Returns the featureDefault.
	 */
	public FeatureDescriptor getFeatureDefault() {
		return featureDefault;
	}
	
	public GeneralURLClassLoader getGeneralClassLoader() {
		return generalClassLoader;
	}
}
