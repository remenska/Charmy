/*
 * Created on 11-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;


import core.internal.extensionpoint.DeclaredHost;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.ui.simpleinterface.ZoomGraphInterface;

//import plugin.charmyTest_modulo2.charmyTest_modulo2;

import java.util.Iterator;
import java.util.LinkedList;



import plugin.charmyui.extensionpoint.editor.ExtensionPointEditor;
import plugin.charmyui.extensionpoint.window.ExtensionPointWindow;


//import plugin.charmyui.extensionpoint.toolbar.host.ExtensionPointToolBar;

/**
 * @author ezio di nisio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UIPlugin implements IMainTabPanel {
    
    /**
	 * riferimento alla tab principale
	 */
	private JTabbedPane mainPanel = null;
	/**
	 * riferimento al pannello delle toolbar
	 */
	
	private JPanel toolBarPanel = null; 
	
		
	PlugDataWin pdw;
	
	LinkedList extensionPoint;

    /**
     * 
     */
	
    public UIPlugin() {
        super();
        // TODO Auto-generated constructor stub
        extensionPoint=new LinkedList();
        

    }

    /* (non-Javadoc)
     * @see simpleinterface.IMainTabPanel#setDati(data.PlugDataWin)
     */
    public void setDati(PlugDataWin plugDtW) {
        // TODO Auto-generated method stub
        pdw = plugDtW;
        
       // topologyWindow = (TopologyWindow)plugDtW.getPlugManager().getHost("plugin.topologydiagram.topologywindow");
        
        /*
        try{
			topologyWindow = (TopologyWindow)plugDtW.getPlugManager().getPlugEdit("");
		}
	    catch(InstantiationException e ){}
		catch(IllegalAccessException e ){}
		catch(ClassNotFoundException e ){}*/
        
        //poi le assegnazioni dovranno essere fatte quando si parsa plugin.xml, tag extension-point e file schema
        
       /* IExtensionPoint WindowPoint = new ExtensionPointWindow(pdw);
        extensionPoint.add(WindowPoint);
        
        IExtensionPoint  EditorPoint =  new ExtensionPointEditor(WindowPoint);
        extensionPoint.add(EditorPoint);*/
        
        
        //IExtensionPoint  ToolBarPoint = new ExtensionPointToolBar(this);

    }

    /* (non-Javadoc)
     * @see simpleinterface.IMainTabPanel#newPlugData(data.PlugDataManager)
     */
    public IPlugData newPlugData(PlugDataManager pm) {
        // TODO Auto-generated method stub
      //  pm.get
        return null;
    }

    /* (non-Javadoc)
     * @see simpleinterface.IMainTabPanel#init()
     */
    public void init() {
        // TODO Auto-generated method stub

    }

       
    /* (non-Javadoc)
     * @see simpleinterface.IMainTabPanel#getPlugDataWin()
     */
    public PlugDataWin getPlugDataWin() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see simpleinterface.IMainTabPanel#getPlugData()
     */
    public IPlugData getPlugData() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see simpleinterface.IMainTabPanel#getEditMenu()
     */
    public EditGraphInterface getEditMenu() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see simpleinterface.IMainTabPanel#getZoomAction()
     */
    public ZoomGraphInterface getZoomAction() {
        // TODO Auto-generated method stub
        return null;
    }

   

  
    /* (non-Javadoc)
     * @see simpleinterface.IMainTabPanel#getDati()
     */
    public Object[] getDati() {
        // TODO Auto-generated method stub
        return null;
    }

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#initHost()
	 */
	public DeclaredHost[] initHost() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#stateActive()
	 */
	public void stateActive() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#stateInactive()
	 */
	public void stateInactive() {
		// TODO Auto-generated method stub

	}

}
