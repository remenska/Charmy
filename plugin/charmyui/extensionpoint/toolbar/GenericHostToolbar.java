/*
 * Created on 19-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.toolbar;

import java.awt.Insets;
import java.io.File;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import core.internal.extensionpoint.IExtensionPoint;
import core.internal.extensionpoint.event.EventService;
import core.internal.plugin.ConfigurationElement;
import core.internal.plugin.ConfigurationProperty;
import core.internal.plugin.PlugInDescriptor;
import core.internal.plugin.editoralgo.IMainTabPanel;
import plugin.charmyui.extensionpoint.toolbar.data.DataHostToolbar;
import plugin.charmyui.extensionpoint.toolbar.action.ButtonMouseAdapter;
import plugin.charmyui.extensionpoint.toolbar.action.ButtonActionListener;
import plugin.charmyui.extensionpoint.toolbar.action.IButtonAction;
import plugin.charmyui.extensionpoint.editor.IHostEditor;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import plugin.charmyui.extensionpoint.toolbar.model.TagToolbar;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class GenericHostToolbar
extends JToolBar
implements IHostToolbar, TagToolbar {
	
	/** Per la costruzione del file jar, nell'ipotesi d'inserire 
	 tutte le icone in una directory icon che si trova nella 
	 stessa directory del file jar. 
	 Funziona sotto Win2000, ma non ? stato verificato
	 con altri sistemi operativi!! */
	public static String PathGif = "";
	//= "F:\\documenti\\Dottorato\\Ricerca\\CHARMY\\CodiceCHARMY\\icon\\";  
	public static final String CLASS_ACTION_LISTENER =
		"plugin.charmyui.extensionpoint.toolbar.action.ButtonActionListener";
	
	public static final String CLASS_MOUSE_ADAPTER =
		"plugin.charmyui.extensionpoint.toolbar.action.ButtonMouseAdapter";
	
	/** Tavola Hash per i pulsanti. */
	protected Hashtable TavolaPulsanti;
	
	protected EventService eventService;
	
	protected ExtensionPointToolbar extensionPointToolbar;
	private Border DefaultBorder;
	
	protected DataHostToolbar dataHostToolbar;
	
	private String currentIdButton = null;
	protected IMainTabPanel pluginOwner=null;
	/**
	 * 
	 */
	public GenericHostToolbar() {
		
		super();
		
		//SETTIAMO TUTTI GLI ATTRIBUTI DI DEFAULT
		
		DefaultBorder = new EmptyBorder(3, 3, 3, 3);
		this.TavolaPulsanti= new Hashtable();
		
	}
	
	
	public static URL createURLPath(String fileName) {
		if (System.getProperty("os.name").startsWith("Wi")) {
			PathGif =
				"jar:file:"
				+ System.getProperty("user.dir").substring(2).replace(
						'\\',
				'/')
				+ "/Charmy.jar!/icon/";
		} else
			PathGif =
				"jar:file:"
				+ System.getProperty("user.dir")
				+ "/Charmy.jar!/icon/";
		try {
			return new URL(PathGif + fileName);
		} catch (Exception e) {
			System.out.println("TypeToolBar.createURLPath ERROR: " + e);
			return null;
		}
	}
	
	protected AbstractButton createToolbarButtonJar(
			String pathFileNameIcon, URL internalLibrary, String strToolTipText) {
		
		
		
		//URL myurl =core.internal.datistatici.CharmyFile.createURLPath(pathFileNameIcon,pathFileNameJar);
		URL temp = null;
		File f = null;
		String newName = "file:"+pathFileNameIcon;
		try {
			temp = new URL(newName);
			//f = new File (temp.toURI()); 
		}
		catch (Exception e){
			//errore : la libreria non � un jar o non esiste il file
			System.out.println("Errore nel path dell'icona del bottone "+e);
			return null;
		}
		
		
		/*String mypathName = internalLibrary.getFile();
		 mypathName = mypathName.substring(0, mypathName.indexOf('!'));
		 mypathName = mypathName.substring(mypathName.lastIndexOf('/')+1, mypathName.length());
		 mypathName = System.getProperty("user.dir") + File.separator + mypathName;*/
		
		// File myfile = new File(mypathName);
		
		if(/*f.exists()*/true){
			ImageIcon myicon = new ImageIcon(temp);
			JButton LocalJButton = new JButton(myicon);
		LocalJButton.setMargin(new Insets(0, 0, 0, 0));
			LocalJButton.setToolTipText(strToolTipText);
			
			if (currentIdButton != null)
				this.TavolaPulsanti.put(this.currentIdButton, LocalJButton);
			
			
			return LocalJButton;
		}
		else{
			JOptionPane.showMessageDialog(null,"Unable to load the buttons icons.\nThe .jar file must be in the directory: "+ internalLibrary.getPath(),"Information",JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
	/** Abilita/disabilita un pulsante selezionandolo tramite
	 id Button (identificatore associato al pulsante). */
	public void setButtonEnabled(String idButton, boolean ctrlIfEnabled) {
		JButton LocalJButton;
		if (TavolaPulsanti != null) {
			LocalJButton = (JButton) TavolaPulsanti.get(idButton);
			LocalJButton.setEnabled(ctrlIfEnabled);
		}
	}
	public JButton createToolbarButton(
			String fileNameIcon,
			String strToolTipText,
			int intMnemonic) {
		// URL myurl = createURLPathGif(fileNameIcon);
		// String mypathName = myurl.getFile();
		// mypathName = mypathName.substring(0, mypathName.indexOf('!'));
		// mypathName = mypathName.substring(mypathName.lastIndexOf('/')+1, mypathName.length());
		// mypathName = System.getProperty("user.dir") + File.separator + mypathName;
		String mypathName = fileNameIcon;
		
		File myfile = new File(mypathName);
		if (myfile.exists()) {
			ImageIcon myicon = new ImageIcon(mypathName); //myurl);
			JButton LocalJButton = new JButton(myicon);
			LocalJButton.setMargin(new Insets(0, 0, 0, 0));
			LocalJButton.setToolTipText(strToolTipText);
			LocalJButton.setMnemonic(intMnemonic);
			
			if (currentIdButton != null)
				this.TavolaPulsanti.put(this.currentIdButton, LocalJButton);
			
			return LocalJButton;
		} else {
			JOptionPane.showMessageDialog(
					null,
					"Unable to load the buttons icons.:\n " + mypathName,
					"Information",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
	public JButton createToolbarButton(
			String fileNameIcon,
			String strToolTipText) {
		// URL myurl = createURLPathGif(fileNameIcon);
		// String mypathName = myurl.getFile();
		// mypathName = mypathName.substring(0, mypathName.indexOf('!'));
		// mypathName = mypathName.substring(mypathName.lastIndexOf('/')+1, mypathName.length());
		// mypathName = System.getProperty("user.dir") + File.separator + mypathName;
		String mypathName = fileNameIcon;
		
		File myfile = new File(mypathName);
		if (myfile.exists()) {
			ImageIcon myicon = new ImageIcon(mypathName); //myurl);
			JButton LocalJButton = new JButton(myicon);
			LocalJButton.setMargin(new Insets(0, 0, 0, 0));
			LocalJButton.setToolTipText(strToolTipText);
			
			if (currentIdButton != null)
				this.TavolaPulsanti.put(this.currentIdButton, LocalJButton);
			
			return LocalJButton;
		} else {
			JOptionPane.showMessageDialog(
					null,
					"Unable to load the buttons icons.:\n " + mypathName,
					"Information",
					JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
	/** Riporta nella posizione di riposo tutti i pulsanti della toolbar. */
	public void setNoPressed() {
		
		// if (extensionPoint==null){
		for (int i = 0; i < getComponentCount(); i++) {
			AbstractButton button = (AbstractButton) this.getComponent(i);
			button.setBorder(DefaultBorder);
		}
		// }
		//  else
		//     extensionPoint.setAllButtonNoPressed(this);
	}
	//???Patrizio???
	/*    
	 protected AbstractButton createToolbarToogleButton(String fileNameIcon, String strToolTipText)
	 {
	 AbstractButton LocalJButton = new JToggleButton(new ImageIcon(PathGif + fileNameIcon));                
	 LocalJButton.setMargin(new Insets(0, 0, 0, 0));
	 LocalJButton.setBorder(new BevelBorder(0));
	 LocalJButton.setToolTipText(strToolTipText);
	 return LocalJButton;
	 }*/
	
	/**
	 * Crea e aggiunge un bottone alla toolbar, viene chiamato da setEventService, aggancia automaticamente i bottoni dichiararti in manifest file
	 * @param abstractButton
	 */
	public AbstractButton addButtonJar(
			String pathFileNameIcon,URL internalLibraryPlugin,
			String strToolTipText) {
		
		AbstractButton button =
			createToolbarButtonJar(pathFileNameIcon,internalLibraryPlugin, strToolTipText);
		if (button != null)
		add(button);
		return button;
	}
	/**
	 * Crea e aggiunge un bottone alla toolbar con set del campo intMnemonic del bottone.
	 * @param abstractButton
	 */
	public AbstractButton addButton(
			String fileNameIcon,
			String strToolTipText,
			int intMnemonic) {
		
		AbstractButton button =
			createToolbarButton(fileNameIcon, strToolTipText, intMnemonic);
		add(button);
		return button;
		
	}
	
	public AbstractButton getButtonFor(String idButton) {
		AbstractButton localButton = null;
		if (TavolaPulsanti != null) {
			localButton = (AbstractButton) TavolaPulsanti.get(idButton);
			
		}
		return localButton;
	}
	
	
	/* (non-Javadoc)
	 * @see core.extensionpoint.IGenericHost#getIdHost()
	 */
	public String getIdHost() {
		// TODO Auto-generated method stub
		return this.extensionPointToolbar.getDataHostToolbarFor(this).getId();
	}
	
	
	public EventService getEventService() {
		// TODO Auto-generated method stub
		return this.eventService;
	}
	public IExtensionPoint getExtensionPointOwner() {
		// TODO Auto-generated method stub
		return this.extensionPointToolbar;
	}
	public IMainTabPanel getPluginOwner() {
		// TODO Auto-generated method stub
		return this.pluginOwner;
	}
	
	
	
	public void setEventService(EventService eventService) {
		
		///setto i dati
		this.eventService= eventService;
		dataHostToolbar =
			this.extensionPointToolbar.getDataHostToolbarFor(this);
		
		this.setName(dataHostToolbar.getName());
		
/////////////////////
		/* AGGANCIO BOTTONI ALLA TOOLBAR + 
		 * ATTIVAZIONE CALLBACK OBJECT: GESTIONE DELLE AZIONI DEL MOUSE SUI BOTTONI - ACTION PERFORMED- 
		 * Viene chiamato dall'extenion point (toolbar) per settare i dati dell'host -
		 * RINOMINARE IN INIT
		 */
		
		//prende i dati di configurazione della toolbar: dal manifest file tag button	
		
		ConfigurationElement[] listaTagRoot = dataHostToolbar.getSubElements();
		
		//cerchiamo i bottoni definiti nel manifest file
		if (listaTagRoot != null)
			for (int i = 0; i < listaTagRoot.length; i++) {
				
				if (listaTagRoot[i].getName().compareTo(BUTTON) == 0) {
					
					String classPath = listaTagRoot[i].getPropertyValue(CLASS);
					String name = listaTagRoot[i].getPropertyValue(NAME);
					String id = listaTagRoot[i].getPropertyValue(ID);
					String icon = listaTagRoot[i].getPropertyValue(ICON);
					
					
					this.currentIdButton = id;
					//String typeButton = listaTagRoot[i].getPropertyValue(TYPE);
					//Integer typeButtonInt = new Integer(typeButton);
					
					URL URLInternalJar = null;
					PlugInDescriptor plugDescriptor = extensionPointToolbar.getDataHostToolbarFor(this).getParent();
					
					if (plugDescriptor.isPlugEdit())
						URLInternalJar = plugDescriptor.getLibrary().getLibraryPlugEditor();
					
					
					if (plugDescriptor.isPluginFile())
						URLInternalJar = plugDescriptor.getLibrary().getLibraryPluginFile();
					
					//if (plugDescriptor.isPlugSave())
					// URLInternalJar = plugDescriptor.getLibrary().getLibraryPlugFileSave();
					
					AbstractButton bottone = this.addButtonJar(icon,URLInternalJar, name);
					this.currentIdButton = null;
					///GESTIONE CLASSE CALLBACKOBJECT - CLASSI PACKAGE ACTION - 
					// a seconda della tipologia di pulsante abbiamo diverse classi per la gestione delle azioni del mouse e dell'evento ritornato (????)
					
					try {
						Class actionListenerClass = this.extensionPointToolbar.plugManager.getGeneralClassLoader().loadClass(CLASS_ACTION_LISTENER);
						Class mouseAdapterClass = this.extensionPointToolbar.plugManager.getGeneralClassLoader().loadClass(CLASS_MOUSE_ADAPTER);
						
						Class classPath2 = this.extensionPointToolbar.plugManager.getGeneralClassLoader().loadClass(classPath);
						
						if (actionListenerClass
								.isAssignableFrom(classPath2)) {
							
							///GESTIRE ECCEZZIONI SUL RECAST E L'ISTANZA DEL CALLBACKOBJECT
							ActionListener callBackObject =
								(ActionListener) classPath2
								.newInstance();
							
							((IButtonAction) callBackObject).setDati(
									id,
									this.extensionPointToolbar,
									this);
							
							bottone.addActionListener(callBackObject);
							
						}
						
						if (mouseAdapterClass
								.isAssignableFrom(classPath2)) {
							
							MouseAdapter callBackObject =
								(MouseAdapter) (classPath2
										.newInstance());
							
							((IButtonAction) callBackObject).setDati(
									id,
									this.extensionPointToolbar,
									this);
							
							bottone.addMouseListener(callBackObject);
							
						}
					} catch (Exception e) {
						
					}
				}
			}
		
		
	}
	public void setExtensionPointOwner(IExtensionPoint extensionpoint) {
		this.extensionPointToolbar= (ExtensionPointToolbar)extensionpoint;
	}
	public void setPluginOwner(IMainTabPanel plugin) {
		this.pluginOwner= plugin;
	}
	
}
