/*
 * Created on 16-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.extensionpoint;

import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.extensionpoint.event.EventService;

/**
 * 
 * Interfaccia di tipo. 
 * Identifica una componente host di un plugin agganciata ad un extension point
 * 
 * @author ezio di nisio
  */
public interface IGenericHost{ 
	
	public String getIdHost(); 
	
	public void setPluginOwner(IMainTabPanel plugin);
	
	public void setExtensionPointOwner(IExtensionPoint extensionpoint);
	
	public void setEventService(EventService eventService);
	
	public EventService getEventService();
		
	public IMainTabPanel getPluginOwner();
	
	public IExtensionPoint getExtensionPointOwner();
	
	public void notifyMessage(Object callerObject, int status, String message);
}
