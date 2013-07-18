package plugin.EsempioPlugin1;

import core.internal.extensionpoint.DeclaredHost;
import core.internal.extensionpoint.IExtensionPoint;
import core.internal.extensionpoint.event.EventService;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.ui.simpleinterface.ZoomGraphInterface;

import plugin.charmyui.extensionpoint.window.ExtensionPointWindow;
import plugin.charmyui.extensionpoint.window.IHostWindow;
import core.resources.ui.WithGraphWindow;

import  plugin.EsempioPlugin1.data.PlugData;

public class EsempioPlugin1 extends WithGraphWindow  implements IMainTabPanel,IHostWindow{

	private PlugData plugData;
	
	private PlugDataWin plugDataWin;
	ExtensionPointWindow extensionPointWindow;
	EventService eventService;

	public static String idPlugFileCharmy = "charmy.plugin.salvastd";
	public static String idPlugFileEsempio = "charmy.plugin.EsempioPluginFile";
	
	
	public EsempioPlugin1(){
		super();
		// TODO Auto-generated constructor stub
	}

	public Object[] getDati() {
		// TODO Auto-generated method stub
		return null;
	}

	public EditGraphInterface getEditMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPlugData getPlugData() {
		
		return plugData;
	}

	public PlugDataWin getPlugDataWin() {
	
		return plugDataWin;
	}

	public ZoomGraphInterface getZoomAction() {
		// TODO Auto-generated method stub
		return null;
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public DeclaredHost[] initHost() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPlugData newPlugData(PlugDataManager pm) {
		this.plugData= new PlugData();
		return plugData;
	}

	public void resetForNewFile() {
		// TODO Auto-generated method stub
		
	}

	public void setDati(PlugDataWin plugDtW) {
		this.plugDataWin=plugDtW;
	}

	public void stateActive() {
		// TODO Auto-generated method stub
		
	}

	public void stateInactive() {
		// TODO Auto-generated method stub
		
	}

	
	public void windowActive() {
		// TODO Auto-generated method stub
		
	}

	public void windowInactive() {
		// TODO Auto-generated method stub
		
	}

	public String getIdHost() {
		// TODO Auto-generated method stub
		return null;
	}

	public void decScaleX() {
		// TODO Auto-generated method stub
		
	}

	public void decScaleY() {
		// TODO Auto-generated method stub
		
	}

	public void incScaleX() {
		// TODO Auto-generated method stub
		
	}

	public void incScaleY() {
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

	public void resetScale() {
		// TODO Auto-generated method stub
		
	}

	public EventService getEventService() {
		// TODO Auto-generated method stub
		return this.eventService;
	}

	public IExtensionPoint getExtensionPointOwner() {
		// TODO Auto-generated method stub
		return this.extensionPointWindow;
	}

	public IMainTabPanel getPluginOwner() {
		// TODO Auto-generated method stub
		return this;
	}

	public void setEventService(EventService eventService) {
		this.eventService= eventService;
	}

	public void setExtensionPointOwner(IExtensionPoint extensionpoint) {
		this.extensionPointWindow= (ExtensionPointWindow)extensionpoint;
	}

	public void setPluginOwner(IMainTabPanel plugin) {
		// TODO Auto-generated method stub
		
	}

	public void notifyMessage(Object callerObject, int status, String message) {
		// TODO Auto-generated method stub
		
	}

}