/*
 * Created on 21-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.topologydiagram.action;

import java.awt.event.ActionEvent;
import plugin.topologydiagram.TopologyEditor;
import plugin.charmyui.extensionpoint.toolbar.action.ButtonActionListener;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ImageItemAction extends ButtonActionListener {

	/**
	 * 
	 */
	public ImageItemAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.toolbar.action.ButtonActionListener#actionPerformedEvent(java.awt.event.ActionEvent)
	 */
	public void actionPerformedEvent(ActionEvent arg0) {
		// TODO Auto-generated method stub
		TopologyEditor topologyEditor = (TopologyEditor)this.extensionPointToolbar.getEditorAssociated(this.toolbarOwner);
		
		topologyEditor.opImg();
	}

}
