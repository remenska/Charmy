/*
 * Created on 21-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.topologydiagram.action;

import java.awt.event.MouseEvent;

import plugin.charmyui.extensionpoint.toolbar.action.ButtonMouseAdapter;
import plugin.topologydiagram.TopologyEditor;
import javax.swing.SwingUtilities;
import plugin.topologydiagram.data.ElementoProcesso;
import javax.swing.BorderFactory;
import java.awt.Color;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DummyTopologyButtonAction extends ButtonMouseAdapter {

	/**
	 * 
	 */
	public DummyTopologyButtonAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.toolbar.action.ButtonMouseAdapter#mouseReleasedEvent(java.awt.event.MouseEvent)
	 */
	public void mouseReleasedEvent(MouseEvent evt) {
		TopologyEditor topologyEditor = (TopologyEditor)this.extensionPointToolbar.getEditorAssociated(this.toolbarOwner);

		int numClick = evt.getClickCount();	
		
				if (SwingUtilities.isLeftMouseButton(evt))
					  {
						  if (numClick == 1)
						  {				  	
							topologyEditor.setEditorStatus(TopologyEditor.DISEGNA_PROCESSO,ElementoProcesso.PROCESS,true,false);
						  }
						  else
						  {
							  // Doppio click => Blocco del pulsante.
							this.toolbarOwner.getButtonFor(this.getIdButton()).setBorder(BorderFactory.createBevelBorder(1,Color.LIGHT_GRAY,Color.GRAY));
							topologyEditor.setEditorStatus(TopologyEditor.DISEGNA_PROCESSO,ElementoProcesso.PROCESS,true,true);
						  }        
					  }
					  else
					  {
						  // Pressione del tasto destro del mouse => Sblocco pulsante.
						topologyEditor.setEditorStatus(TopologyEditor.ATTESA,ElementoProcesso.PROCESS,true,false);
					  }
	}

}
