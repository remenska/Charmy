/*
 * Created on 21-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.topologydiagram.action.menuhelp;

import java.awt.event.ActionEvent;

import plugin.charmyui.extensionpoint.toolbar.action.ButtonActionListener;
import plugin.topologydiagram.TopologyEditor;
import javax.swing.JOptionPane;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TopologyItemAction extends ButtonActionListener {

	/**
	 * 
	 */
	public TopologyItemAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.toolbar.action.ButtonActionListener#actionPerformedEvent(java.awt.event.ActionEvent)
	 */
	public void actionPerformedEvent(ActionEvent arg0) {
		String titolo="Topology Panel";
		String messaggio="With this editor you can describe" +
						 "\nthe system Software Architecture.";
		JOptionPane.showMessageDialog(null, messaggio,titolo,JOptionPane.INFORMATION_MESSAGE);        

	}

}
