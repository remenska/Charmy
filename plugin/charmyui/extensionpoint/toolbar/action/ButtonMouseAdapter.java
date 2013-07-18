/*
 * Created on 20-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.toolbar.action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import plugin.charmyui.extensionpoint.toolbar.event.ButtonEvent;
import javax.swing.AbstractButton;

import plugin.charmyui.extensionpoint.toolbar.ExtensionPointToolbar;

import plugin.charmyui.extensionpoint.editor.IHostEditor;
import plugin.charmyui.extensionpoint.toolbar.GenericHostToolbar;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class ButtonMouseAdapter
	extends MouseAdapter
	implements IButtonAction {

	private IHostEditor associatedWindow = null;

	protected ExtensionPointToolbar extensionPointToolbar;

	private AbstractButton pulsante;

	protected GenericHostToolbar toolbarOwner = null;

	private int status = -1;

	private int statusOnClick = -1;
	private int statusOnDubleClick = -1;

	protected String idButton = "";

	/**
		 * 
		 */
	public ButtonMouseAdapter() {
		super();

		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.toolbar.action.IButtonAction#setDati(plugin.charmyui.extensionpoint.toolbar.ExtensionPointToolbar, plugin.charmyui.extensionpoint.toolbar.GenericHostToolbar)
	 */
	public void setDati(
		String id,
		ExtensionPointToolbar extensionPointToolbar,
		GenericHostToolbar toolbarOwner) {

		this.idButton = id;
		this.extensionPointToolbar = extensionPointToolbar;
		this.toolbarOwner = toolbarOwner;

	}

	public void mouseReleased(MouseEvent evt) {
		
	
		this.mouseReleasedEvent(evt);

		this.extensionPointToolbar.getEventService().pub(
			new ButtonEvent(
				ButtonEvent.MOUSE_RELEASED_EVENT,
				evt,
				this.toolbarOwner));

	}

	public abstract void mouseReleasedEvent(MouseEvent evt);

	/**
	 * @param statusOnClick The statusOnClick to set.
	 */
	public boolean setStatusOnClick(int statusOnClick) {

		if (statusOnClick > -1) {
			this.statusOnClick = statusOnClick;
			return true;
		}
		return false;
	}

	/**
	 * @param statusOnDubleClick The statusOnDubleClick to set.
	 */
	public boolean setStatusOnDubleClick(int statusOnDubleClick) {

		if (statusOnDubleClick > -1) {
			this.statusOnDubleClick = statusOnDubleClick;
			;
			return true;
		}
		return false;

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
	public String getIdButton() {
		return idButton;
	}

	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.toolbar.action.IButtonAction#setStatus(int)
	 */
	public void setStatus(int status) {

		this.status = status;

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		super.mouseClicked(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		super.mouseEntered(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		super.mouseExited(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		super.mousePressed(arg0);
	}

	

}
