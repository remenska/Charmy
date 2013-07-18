/*
 * Created on 25-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.editor.event;

import core.internal.extensionpoint.event.EventBase;
import plugin.charmyui.extensionpoint.editor.IHostEditor;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InactiveEditor extends EventBase {

	private IHostEditor inactiveEditor;
	/**
	 * @param localHostID
	 */
	public InactiveEditor(IHostEditor inactiveEditor) {
		super(inactiveEditor.getIdHost());
		
		this.inactiveEditor=inactiveEditor;
	}

	/**
	 * @return
	 */
	public IHostEditor getInactiveEditor() {
		return inactiveEditor;
	}

}
