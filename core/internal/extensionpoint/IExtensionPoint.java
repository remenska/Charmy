/*
 * Created on 7-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.extensionpoint;



import core.internal.plugin.PlugManager;



/**
 * Interfaccia che un extension point deve implementare per poter essere integrato in Charmy
 * 
 * @author Ezio Di Nisio
 *
 *
 */
public interface IExtensionPoint {
	
	
	
	/**
	 * 
	 *  Charmy Project
	 *  @author ezio di nisio
	 * 
	 */
	
	public void init(PlugManager plugManager, ExtensionPointDescriptor extPointDescriptor);
	
	
	
	/**
	 * notifica da parte del sistema, della richiesta di aggancio di una componente host
	 * se l'extension point ritorna false l'host verra rimosso dalla lista degli host attivi nel sistema 
	 * @author ezio di nisio
	 *
	 */
	
	public boolean addHost(HostDescriptor elementHost);
	
		
}
