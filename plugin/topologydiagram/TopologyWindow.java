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
    

package plugin.topologydiagram;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JScrollPane;

import plugin.charmyui.extensionpoint.window.ExtensionPointWindow;
import plugin.charmyui.extensionpoint.window.IHostWindow;
import plugin.topologydiagram.data.ListaCanale;
import plugin.topologydiagram.data.ListaProcesso;
import plugin.topologydiagram.data.PlugData;
import plugin.topologydiagram.eventi.listacanali.AddCanaleEvento;
import plugin.topologydiagram.eventi.listacanali.RemoveCanaleEvento;
import plugin.topologydiagram.eventi.listacanali.UpdateCanaleEvento;
import plugin.topologydiagram.pluglistener.IListaCanaleListener;
import core.internal.extensionpoint.DeclaredHost;
import core.internal.extensionpoint.IExtensionPoint;
import core.internal.extensionpoint.event.EventService;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.FileManager;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.ui.simpleinterface.ZoomGraphInterface;
import core.resources.ui.WithGraphWindow;
//import plugin.charmyfile.save.semantica.SalvaSemantica;
/** Classe per creare e gestire la creazione di un S_A_ Topology Diagram. */

/**
 * @author Ezio
 *
 */
public  class TopologyWindow
	extends WithGraphWindow
	implements IListaCanaleListener, IMainTabPanel,IHostWindow{

	public static String idPluginFileCharmy ="charmy.plugin.salvastd";
	
	/** Barre di scorrimento per l'editor del Topology Diagram. */
	private JScrollPane ClassScroller;

	/** Riferimento all'editor per il disegno del Topology Diagram. */
	private TopologyEditor vClassEditor;

	/**
	 * riferimento ai dati di plug-in
	 */
	private PlugDataWin plugDataWin; 


	/**
	 * riferimento ai dati di plug-in
	 */
	private PlugData plugData;
	
	ExtensionPointWindow extensionPointWindow;
	EventService eventService;

	IMainTabPanel[] listenerResetForNewFile = null;
	
	public FileManager fileManager;

	/** Costruttore. */
	public TopologyWindow() {
		super();
	}

	/**
	 * inizializzazione
	 *
	 */
	public void init(){
		/*vClassEditor = new TopologyEditor(this, plugDataWin, plugData);
		ClassScroller = new JScrollPane(vClassEditor);
		ClassScroller.setWheelScrollingEnabled(false);
		ClassScroller.setAutoscrolls(true);
		setBorder(BorderFactory.createEtchedBorder(Color.DARK_GRAY, Color.GRAY));
		setLayout(new BorderLayout());
		
		add("Center", ClassScroller);
		setToolBar(new TopologyToolBar(this));*/
		//plugDataWin.getMainPanel().addTab("Topology Editor", this);
		plugData.getListaCanale().addListener(this);
		//plugDataWin.getMainPanel().addTab("Topology Editor", this);		
	}


	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#initHost()
	 */
	public DeclaredHost[] initHost() {
		
		try {
			Class clazz = this.plugDataWin.getPlugManager().getGeneralClassLoader().loadClass("plugin.topologydiagram.TopologyEditor");
			 vClassEditor = (TopologyEditor)clazz.newInstance();
			
			Method setDati = vClassEditor.getClass().getMethod("setDati", new Class[] {TopologyWindow.class,PlugDataWin.class,PlugData.class});
			setDati.invoke(vClassEditor, new Object[] {this,this.plugDataWin,this.plugData});
				
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//vClassEditor = new TopologyEditor(this, plugDataWin, plugData);
		DeclaredHost[] dh = new  DeclaredHost[1];
		
		dh[0]= new DeclaredHost(vClassEditor,"plugin.topologydiagram.topologyeditor");
				
		return dh;
	}
	
	public JScrollPane getScrollPane(){
		return ClassScroller;
	}
	
	/** Metodo per impostare lo stato dell'editor usato
		per il disegno del S_A_ Topology Diagram. */
	public void setWindowStatus(
		int j,
		int tipoprc,
		boolean isDummy,
		boolean ctrl) {
		vClassEditor.setEditorStatus(j, tipoprc, isDummy, ctrl);
	} 


	/** Metodo per restituire lo stato dell'editor usato
		per il disegno del S_A_ Topology Diagram. */
	public int getWindowStatus() {
		return vClassEditor.getEditorStatus();
	}

	/** Operazione di zoom sull'asse X. */
	public void incScaleX() {
		vClassEditor.incScaleX();
	}

	/** Operazione di zoom negativo sull'asse X. */
	public void decScaleX() {
		vClassEditor.decScaleX();
	}

	/** Operazione di zoom sull'asse Y. */
	public void incScaleY() {
		vClassEditor.incScaleY();
	}

	/** Operazione di zoom negativo sull'asse Y. */
	public void decScaleY() {
		vClassEditor.decScaleY();
	}

	/** Ripristino della scala di visualizzazione al 100%. */
	public void resetScale() {
		vClassEditor.resetScale();
	}

	/** Operazione di copy. */
	public void opCopy() {
		vClassEditor.opCopy();
	}

	/** Operazione di paste. */
	public void opPaste() {
		vClassEditor.opPaste();
	}

	/** Operazione di cut. */
	public void opCut() {
		vClassEditor.opCut();
	}

	/** Operazione di cancellazione. */
	public void opDel() {
		vClassEditor.opDel();
	}

	/** Operazione di undo. */
	public void opUndo() {
		vClassEditor.opUndo();
	}

	/** Operazione per salvare su file il 
		Topology Diagram come immagine. */
	public void opImg() {
		vClassEditor.opImg();
	}

	/** Operazione di redo. */
	public void opRedo() {
		vClassEditor.opRedo();
	}

	/** Restituisce un vettore contenente il nome di tutti i processi
		non 'dummy' presenti nel S_A_ Topology Diagram. */
	public Vector getAllProcessName() {
		return vClassEditor.getAllProcessName();
	}

	public TopologyEditor getTopologyEditor() {
		return vClassEditor;
	}

	/** Restituisce la lista di tutti i processi 
		presenti nel S_A_ Topology Diagram. */
	public LinkedList getListaProcessi() {
		return vClassEditor.getListaProcessi();
	}

	/** Restituisce la lista dei processi privata
		di quelli di tipo dummy. */
	public ListaProcesso getListaProcessoSenzaDummy() {
		return vClassEditor.getListaProcessoSenzaDummy();
	}

	/** Metodo per abilitare i pulsanti della EditToolBar. */
	public void setButtonEnabled() {
		plugDataWin.getEditToolBar().setButtonEnabled("Copy", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Paste", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Del", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Cut", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Undo", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Redo", true);
	}

	/** Restituisce la lista dei processi del Topology Diagram. */
	public ListaProcesso getListaDeiProcessi() {
		return vClassEditor.getListaDeiProcessi();
	}

	/** Permette di impostare la lista dei processi del Topology Diagram. */
	public void setListaDeiProcessi(ListaProcesso lp) {
		vClassEditor.setListaDeiProcessi(lp);
	}

	/** Restituisce la lista dei canali del Topology Diagram. */
	public ListaCanale getListaDeiCanali() {
		return vClassEditor.getListaDeiCanali();
	}

	/** Permette di impostare la lista dei canali del Topology Diagram. */
	public void setListaDeiCanali(ListaCanale lc) {
		vClassEditor.setListaDeiCanali(lc);
	}

	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
	public void restoreFromFile() {
		vClassEditor.restoreFromFile();
		repaint();
	}

	/** Metodo per fare il reset del Topology Diagram
		all'atto della creazione di una nuova architettura:
		item "New" del menu "File". */
	public void resetForNewFile() {
		vClassEditor.resetForNewFile();
		vClassEditor.setCEProperties(1200, 1000, Color.white);
		plugDataWin.getStatusBar().setText("Topology ready.");
	}

	/** Restituisce il nome di un canale se ne esiste almeno uno, altrimenti null. */
	public String getAnyNameChannel() {
		return vClassEditor.getAnyNameChannel();
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateActive() {
		//setButtonEnabled();
		//plugDataWin.getToolBarPanel().add(topologyToolBar);
		//plugDataWin.getToolBarPanel().add(plugDataWin.getZoomToolBar());
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#setPlugDataWin(plugin.PlugDataWin)
	 */
	public void setPlugDataWin(PlugDataWin plugDtW) {
		plugDataWin = plugDtW;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getPlugDataWin()
	 */
	public PlugDataWin getPlugDataWin() {
		return plugDataWin;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#removeToolbar()
	 */
	public void stateInactive() {
		//plugDataWin.getToolBarPanel().remove(topologyToolBar);
		//plugDataWin.getToolBarPanel().remove(plugDataWin.getZoomToolBar());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaCanaleListener#canaleAdded(plugin.AddCanaleEvento)
	 */
	public void canaleAdded(AddCanaleEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.IListaCanaleListener#canaleRemoved(plugin.RemoveCanaleEvento)
	 */
	public void canaleRemoved(RemoveCanaleEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.IListaCanaleListener#canaleUpdate(plugin.UpdateCanaleEvento)
	 */
	public void canaleUpdate(UpdateCanaleEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.IListaCanaleListener#canaleRefresh()
	 */
	public void canaleRefresh() {
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#setDati(plugin.PlugDataWin, plugin.PlugData)
	 */
	public void setDati(PlugDataWin plugDtW) {

		plugDataWin = plugDtW;
		
		this.fileManager=plugDtW.getFileManager();
		//init();
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getPlugData()
	 */
	public core.internal.runtime.data.IPlugData getPlugData() {
		return (core.internal.runtime.data.IPlugData)plugData;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getEditMenu()
	 */
	public EditGraphInterface getEditMenu() {
		return this;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getZoomAction()
	 */
	public ZoomGraphInterface getZoomAction() {
		return this;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getDati()
	 */
	public Object[] getDati() {
		//return new String[]{"pippo","topolino"};
		return null;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#newPlugData(data.PlugDataManager)
	 */
	public IPlugData newPlugData(PlugDataManager pm) {
		plugData=new PlugData(pm);
		return plugData;
	}


	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.window.IHostWindow#windowActive()
	 */
	public void windowActive() {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.window.IHostWindow#windowInactive()
	 */
	public void windowInactive() {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see core.extensionpoint.IGenericHost#getIdHost()
	 */
	public String getIdHost() {
		return this.extensionPointWindow.getDataHost(this).getId();
	}

	public EventService getEventService() {
		// TODO Auto-generated method stub
		return this.eventService;
	}

	/*
	 *  (non-Javadoc)
	 * @see core.internal.extensionpoint.IGenericHost#getExtensionPointOwner()
	 */
	public IExtensionPoint getExtensionPointOwner() {
		// TODO Auto-generated method stub
		return this.extensionPointWindow;
	}

	public IMainTabPanel getPluginOwner() {
		// TODO Auto-generated method stub
		return this;
	}

	public void setEventService(EventService eventService) {
		this.eventService= eventService;
	}

	public void setExtensionPointOwner(IExtensionPoint extensionpoint) {
		this.extensionPointWindow= (ExtensionPointWindow)extensionpoint;
	}

	public void setPluginOwner(IMainTabPanel plugin) {
		// TODO Auto-generated method stub
		
	}

	public void notifyMessage(Object callerObject, int status, String message) {
		// TODO Auto-generated method stub
		
	}

	

}