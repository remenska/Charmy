package plugin.EsempioPlugin1;

import javax.swing.JOptionPane;

import plugin.EsempioPlugin1.data.PlugData;
import plugin.charmyui.extensionpoint.editor.ExtensionPointEditor;
import plugin.charmyui.extensionpoint.editor.GenericHostEditor;
import core.internal.extensionpoint.IExtensionPoint;
import core.internal.extensionpoint.event.EventService;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.FileManager;


public class EsempioPlugin1Editor extends GenericHostEditor{

	
	//private FileManager fileManager;
	
	public EsempioPlugin1Editor() {
		super(null);
		// TODO Auto-generated constructor stub
	}

	public void editorActive() {
		// TODO Auto-generated method stub
		
	}

	public void editorInactive() {
		// TODO Auto-generated method stub
		
	}

	public void opCopy() {
		// TODO Auto-generated method stub
		
	}

	public void opCut() {
		// TODO Auto-generated method stub
		
	}

	public void opDel() {
		// TODO Auto-generated method stub
		
	}

	public void opImg() {
		// TODO Auto-generated method stub
		
	}

	public void opPaste() {
		// TODO Auto-generated method stub
		
	}

	public void opRedo() {
		// TODO Auto-generated method stub
		
	}

	public void opUndo() {
		// TODO Auto-generated method stub
		
	}

	

	public void notifyMessage(Object callerObject, int status, String message) {
		
			if (status==1)	{
				((PlugData)this.getPluginOwner().getPlugData()).setInformazione1(message);
				JOptionPane.showMessageDialog(null, "Settato valore dato1. Salvando la topologia questo plugin aggiunge questo dato come  attributo alla lista processo","Esempio",JOptionPane.INFORMATION_MESSAGE);
				this.pluginOwner.getPlugDataWin().getFileManager().setChangeWorkset(EsempioPlugin1.idPlugFileCharmy,true);
				
				
			}
				
			if (status==2)	{
				((PlugData)this.getPluginOwner().getPlugData()).setInformazione2(message);
				JOptionPane.showMessageDialog(null, "Settato valore dato2. Salvando un file con estensione .prova, il dato viene salvato nel file","Esempio",JOptionPane.INFORMATION_MESSAGE);       
				this.pluginOwner.getPlugDataWin().getFileManager().setChangeWorkset(EsempioPlugin1.idPlugFileEsempio,true);
				
			}
				
		 
	}

	public EventService getEventService() {
		// TODO Auto-generated method stub
		return this.eventService;
	}


	public IExtensionPoint getExtensionPointOwner() {
		// TODO Auto-generated method stub
		return this.extensionPointEditor;
	}


	public IMainTabPanel getPluginOwner() {
		// TODO Auto-generated method stub
		return this.pluginOwner;
	}


	public void setEventService(EventService eventService) {
		this.eventService=eventService;
	}


	public void setExtensionPointOwner(IExtensionPoint extensionpoint) {
		this.extensionPointEditor= (ExtensionPointEditor)extensionpoint;
	}


	public void setPluginOwner(IMainTabPanel plugin) {
		
		this.pluginOwner=plugin;
		//this.fileManager= plugin.getPlugData().getPlugDataManager().getFileManager();
	}

}
