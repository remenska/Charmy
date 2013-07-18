/*
 * Created on 20-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.toolbar.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import plugin.charmyui.extensionpoint.toolbar.ExtensionPointToolbar;
import plugin.charmyui.extensionpoint.toolbar.GenericHostToolbar;
import plugin.charmyui.extensionpoint.toolbar.event.ButtonEvent;


/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class ButtonActionListener
	implements ActionListener, IButtonAction {

	protected ExtensionPointToolbar extensionPointToolbar;
	protected GenericHostToolbar toolbarOwner;
	protected String idButton="";

	/**
	 * 
	 */
	public ButtonActionListener() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	 public  void actionPerformed(ActionEvent arg0){
	 	
		this.actionPerformedEvent(arg0);
		
		
	 	////POI >> PUBBLICA EVENTO - 
	 	
	 	this.extensionPointToolbar.getEventService().pub(
	 	    new ButtonEvent(ButtonEvent.ACTION_PERFORMED_EVENT,arg0,this.toolbarOwner));
	 	
	 	
	 	
	 }
	
	 
	public abstract void actionPerformedEvent(ActionEvent arg0);

	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.toolbar.action.IButtonAction#setDati(plugin.charmyui.extensionpoint.toolbar.ExtensionPointToolbar, plugin.charmyui.extensionpoint.toolbar.GenericHostToolbar)
	 */
	public void setDati(String id,
		ExtensionPointToolbar extensionPointToolbar,
		GenericHostToolbar toolbarOwner) {

        this.idButton=id;
		this.extensionPointToolbar = extensionPointToolbar;
		this.toolbarOwner = toolbarOwner;
	}

	/**
	 * @return
	 */
	public ExtensionPointToolbar getExtensionPointToolbar() {
		return extensionPointToolbar;
	}

	/**
	 * @return
	 */
	public GenericHostToolbar getToolbarOwner() {
		return toolbarOwner;
	}

	/**
	 * @return
	 */
	public String getIdButton() {
		return idButton;
	}

	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.toolbar.action.IButtonAction#setStatus(int)
	 */
	public void setStatus(int status) {
		// TODO Auto-generated method stub

	}

	

}
