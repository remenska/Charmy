/*
 * Created on 22-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.toolbar.event;

import plugin.charmyui.extensionpoint.toolbar.IHostToolbar;

import core.internal.extensionpoint.event.EventBase;

import java.awt.event.MouseEvent;
import java.awt.AWTEvent;


/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ButtonEvent extends EventBase {

    public static final int MOUSE_RELEASED_EVENT = 0;
	public static final int MOUSE_CLICKED_EVENT = 1;
	public static final int MOUSE_ENTERED_EVENT = 2;
	public static final int MOUSE_EXITED_EVENT = 3;
	public static final int MOUSE_PRESSED_EVENT = 4;
	public static final int ACTION_PERFORMED_EVENT = 5;
    
	private AWTEvent mouseEvent;
	private IHostToolbar toolbarOwner;
	private int typeMouseEvent=-1;
	/**
	 * @param localHostID
	 */
	public ButtonEvent(int typeMouseEvent, AWTEvent evt,IHostToolbar toolbarOwner ) {
		super(toolbarOwner.getIdHost());
		
		this.mouseEvent=evt;
		this.toolbarOwner=toolbarOwner;
		this.typeMouseEvent=typeMouseEvent;
	}

	/**
	 * @return
	 */
	public AWTEvent getMouseEvent() {
		return mouseEvent;
	}

	/**
	 * @return
	 */
	public IHostToolbar getToolbarOwner() {
		return toolbarOwner;
	}

	/**
	 * @return
	 */
	public int getTypeMouseEvent() {
		return typeMouseEvent;
	}

}
