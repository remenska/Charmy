/*
 * Created on 25-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.window.event;

import core.internal.extensionpoint.event.EventBase;
import plugin.charmyui.extensionpoint.window.IHostWindow;
/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InactiveMainPanel extends EventBase {

	private IHostWindow inactivePanel;
	/**
	 * @param localHostID
	 */
	public InactiveMainPanel(IHostWindow inactivePanel) {

		super(inactivePanel.getIdHost());
		this.inactivePanel = inactivePanel;

	}

	/**
	 * @return
	 */
	public IHostWindow getInactivePanel() {
		return inactivePanel;
	}

}
