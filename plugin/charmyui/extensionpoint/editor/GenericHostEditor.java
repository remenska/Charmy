package plugin.charmyui.extensionpoint.editor;

import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;

import plugin.charmyui.extensionpoint.toolbar.ExtensionPointToolbar;
import core.internal.extensionpoint.IExtensionPoint;
import core.internal.extensionpoint.event.EventService;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.FileManager;
import core.resources.ui.WithGraphEditor;

public abstract class GenericHostEditor extends WithGraphEditor
implements IHostEditor{

	protected  ExtensionPointEditor extensionPointEditor;
	protected  EventService eventService;
	protected IMainTabPanel pluginOwner=null;
	
    protected int editorStatus =0;
	
	public GenericHostEditor(FileManager fileManager) {
		super(fileManager);
		// TODO Auto-generated constructor stub
	}

	public EventService getEventService() {
		return this.eventService;
	}

	public IExtensionPoint getExtensionPointOwner() {
		return this.extensionPointEditor;
	}

	public String getIdHost() {
		
		return this.extensionPointEditor.getDataHost(this).getId();
		
	}

	public IMainTabPanel getPluginOwner() {
		return this.pluginOwner;
	}

	public abstract void notifyMessage(Object callerObject, int status, String message) ;
	
	public void setEventService(EventService eventService) {
		this.eventService=eventService;
		
	}

	/*
	 *  (non-Javadoc)
	 * @see core.internal.extensionpoint.IGenericHost#setExtensionPointOwner(core.internal.extensionpoint.IExtensionPoint)
	 */
	public void setExtensionPointOwner(IExtensionPoint extensionpoint) {
		this.extensionPointEditor= (ExtensionPointEditor)extensionpoint;
		
	}

	public void setPluginOwner(IMainTabPanel plugin) {
		this.pluginOwner=plugin;
	}

	public abstract void opCopy() ;

	public abstract void opCut() ;

	public abstract void opDel() ;

	public abstract void opImg() ;

	public abstract void opPaste();

	public abstract void opRedo() ;

	public abstract void opUndo() ;
	public abstract void editorActive() ;

	public abstract void editorInactive() ;

	public int getEditorStatus() {
		return editorStatus;
	}

	public void setEditorStatus(int status) {
		this.editorStatus = status;
	}
	
	/**
	 * Setta tutti i bottoni di tutte le toolbar associate all'editor in posizione di rilascio
	 *
	 */
	
	public void setAllButtonNoPressed(){   
		
		ExtensionPointToolbar extensionPointToolbar = (ExtensionPointToolbar)extensionPointEditor.getPlugManager().getExtensionPoint("plugin.charmyui.toolbar");
		 	
		extensionPointToolbar.setAllButtonNoPressed();
    	
	   }
	


}
