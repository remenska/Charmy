/*
 * Created on 21-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.topologychannels.action;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.Vector;

import plugin.statediagram.data.ListaDP;
import plugin.topologychannels.TopologyEditor;
import plugin.topologychannels.data.ElementoCanale;
import plugin.topologychannels.data.ElementoProcesso;
import plugin.topologychannels.data.PlugData;
import plugin.charmyui.extensionpoint.toolbar.action.ButtonActionListener;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ComputeItemAction extends ButtonActionListener {

	/**
	 * 
	 */
	
	private plugin.statediagram.data.PlugData plugDataState;
	
	public ComputeItemAction() {
		super();
	}

	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.toolbar.action.ButtonActionListener#actionPerformedEvent(java.awt.event.ActionEvent)
	 */
	public void actionPerformedEvent(ActionEvent arg0) {
		// TODO Auto-generated method stub
		TopologyEditor topologyEditor = (TopologyEditor)this.extensionPointToolbar.getEditorAssociated(this.toolbarOwner);
		topologyEditor.getListaDeiCanali().removeAll();
		Vector processes=topologyEditor.getAllProcessName();
		ListaDP listProcessState;
		String label=new String();
		ElementoCanale currentChannel;
		ElementoProcesso currentProcess;
		plugDataState=(plugin.statediagram.data.PlugData)topologyEditor.getPlugData().getPlugDataManager().getPlugData("charmy.plugin.state");
		if(plugDataState!=null){
			listProcessState= plugDataState.getListaDP();
			
			for(int i=0;i<processes.size();i++){
				//System.out.println("Process: "+processes.get(i));			
				for(int j=0;j<processes.size();j++){
					label="";
					if(i!=j){
						currentProcess=(ElementoProcesso)topologyEditor.getListaDeiProcessi().getElement((String)processes.get(i));
						LinkedList ll=listProcessState.getAllMessageNameForProcess((String)processes.get(i), (String)processes.get(j));
						if(ll.size()!=0){
							for(int k=0;k<ll.size();k++)
								label+="\n"+ll.get(k);
							currentChannel =
								new ElementoCanale((ElementoProcesso)topologyEditor.getListaDeiProcessi().getElement((String)processes.get(i)), 
										(ElementoProcesso)topologyEditor.getListaDeiProcessi().getElement((String)processes.get(j)), 
										null);
							currentChannel.setName("from."+processes.get(i)+".to."+processes.get(j));
							currentProcess.setConnectedProcess((String)processes.get(j), label);
							topologyEditor.getListaDeiCanali().addElement(currentChannel);
							
							//System.out.println(processes.get(i)+" to "+processes.get(j)+": "+label);
						}
						
					}
				}			
			}
			topologyEditor.repaint();
		}
	}

}
