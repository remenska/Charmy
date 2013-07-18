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
public class ActiveEditor extends EventBase {

private IHostEditor activeEditor;

	/**
	 * @param localHostID
	 */
	public ActiveEditor(IHostEditor activeEditor) {
		
		super(activeEditor.getIdHost());
		
		this.activeEditor=activeEditor;
		
	}

/**
 * @return
 */
public IHostEditor getActiveEditor() {
	return activeEditor;
}

}
