/*
 * Created on 23-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import core.internal.extensionpoint.ExtensionPointDescriptor;
import core.internal.extensionpoint.HostDescriptor;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.IFilePlug;

//import org.eclipse.core.runtime.model.PluginDescriptorModel;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PluginRegistry {

	//mappa contenente i descriptor dei plugin istallati
	private Map plugins = new HashMap(30);
	
	private InternalError internalError;

	/**
	 * 
	 */
	public PluginRegistry(InternalError internalError) {
		
	this.internalError=internalError;
	}

	public void addPlugin(PlugInDescriptor plugin) {

		String key = plugin.getIdentifier();
		
		Object[] infoPluginList = (Object[])this.plugins.get(key);
		if (infoPluginList == null) {
			
			Object[] infoPlugin = new Object[2];
			infoPlugin[0]=plugin;//descriptor
			infoPlugin[1]=null; ///contiene il reference alla classe principale del plugin
			Object[] newInfoPluginList = new Object[1];
			newInfoPluginList[0]=infoPlugin;
			plugins.put(key, newInfoPluginList);
		} else {
			//controlliamo se già esiste un descriptor con idPlug identico
			for (int i = 0; i < infoPluginList.length; i++) {
				PlugInDescriptor currentDescriptor = (PlugInDescriptor)((Object[])infoPluginList[i])[0];
				if (currentDescriptor.getVersion().equals(plugin.getVersion())) {
					//sostituiamo con il descriptor corrente e riportiamo l'errore
					this.internalError.addError(Error.WARNING,plugin.getIdentifier(),null,plugin.getName()+". Duplicate plugin whith same id and same version",null);
					//pluginList[i] = plugin;
					return;
				}
			}		
			
			Object[] infoPlugin = new Object[2];
			infoPlugin[0]=plugin;//descriptor
			infoPlugin[1]=null; ///contiene il reference alla classe principale del plugin
			
			Object[] newInfoPluginList =
				new Object[infoPluginList.length + 1];
			System.arraycopy(
					infoPluginList,
				0,
				newInfoPluginList,
				0,
				infoPluginList.length);
			newInfoPluginList[infoPluginList.length] = infoPlugin;
			plugins.put(key, newInfoPluginList);
		}

	}


	/**
	 * Restituisce tutti i descriptor con versioni differenti del plugin con identificatore passato come argomento. 
	 *   
	 * @param pluginId
	 * @return
	 */
	public PlugInDescriptor[] getPluginsDescriptorFor(String pluginId) {
		
		Object[] infoPluginList = (Object[])this.plugins.get(pluginId);
		
		if (infoPluginList != null) {
			
			PlugInDescriptor[] plugins = new PlugInDescriptor[infoPluginList.length];
			for (int i = 0; i < infoPluginList.length; i++) {
				plugins[i]=(PlugInDescriptor)((Object[])infoPluginList[i])[0];
			}
			return plugins;
		}
		
		return null;
		
		
	}

	/**
	 * Restituisce il descriptor del plugin con identificatore passato come argomento.
	 * Se ci sono più versioni dello stesso plugin, viene restituito il descriptor
	 * della versione più recente.
	 * @param pluginId
	 * @return
	 */
	public PlugInDescriptor getPluginDescriptor(String pluginId) {

		PlugInDescriptor[] list = this.getPluginsDescriptorFor(pluginId);
		if (list == null)
			return null;

		String versionTemp = "0.0.0";
		int pos = -1;
		for (int i = 0; i < list.length; i++) {

			String pluginVersion = list[i].getVersion();
			if (versionUpVersion(pluginVersion, versionTemp) == 1) {
				versionTemp = pluginVersion;
				pos = i;
			}

		}

		if (pos == -1)
			return null;
		else
			return list[pos];
	}

	/**
	 * Ritorna il descrittore del plugin, null se non esiste.
	 *
	 * @param pluginId : identificatore univoco del plugin
	 * @param version : versione del plugin. Se è null verrà restituito il descriptor con la versione più recente.
	 * @return 
	 */
	public PlugInDescriptor getPluginDescriptor(
		String pluginId,
		String version) {
		PlugInDescriptor[] list = getPluginsDescriptorFor(pluginId);
		if (list == null || list.length == 0)
			return null;
		if (version == null)
			// Just return the first one in the list (random)
			return this.getPluginDescriptor(pluginId);

		for (int i = 0; i < list.length; i++) {
			PlugInDescriptor element = list[i];

			if (element.getVersion().equals(version))
				return element;
		}
		return null;
	}

	/**
	 * Ritorna tutti i descriptor gestiti da questo registro.
	 *
	 * 
	 */
	public PlugInDescriptor[] getAllPluginDescriptor() {
		List result = new ArrayList();

		for (Iterator i = plugins.values().iterator(); i.hasNext();) {
			Object[] entries = (Object[]) i.next();

			for (int j = 0; j < entries.length; j++){
				
				
				result.add(((Object[])entries[j])[0]);
			}
			
		}
		return (PlugInDescriptor[]) result.toArray(
			new PlugInDescriptor[result.size()]);
	}

	/**
		 * Ritorna la lista dei descriptor dei plugin istallati  con la versione più recente.
		 *
		 *
		 */

	public PlugInDescriptor[] getAllRecentPluginDescriptor() {
		List result = new ArrayList();

		for (Iterator i = plugins.values().iterator(); i.hasNext();) {
			Object[] entries = (Object[]) i.next();

			result.add(this.getPluginDescriptor(((PlugInDescriptor)((Object[])entries[0])[0]).getIdentifier()));

		}
		return (PlugInDescriptor[]) result.toArray(
			new PlugInDescriptor[result.size()]);
	}

	/**
	 * Rimuove il plugin dal registro.
	 *
	 * @param pluginId the unique identifier of the plug-in to remove
	 * @param version the version of the plug-in to remove
	 */
	/*public void removePlugin(String pluginId, String version) {

		PlugInDescriptor[] plugins = getPluginsDescriptorFor(pluginId);
		if (plugins == null || plugins.length == 0)
			return;
		int removedCount = 0;
		for (int i = 0; i < plugins.length; i++) {
			if (version.equals(plugins[i].getVersion())) {
				plugins[i] = null;
				removedCount++;
			}
		}
		// If all were removed, toss the whole entry.  Otherwise, compact the array
		if (removedCount == plugins.length)
			removePlugins(pluginId);
		else {
			PlugInDescriptor[] newList =
				new PlugInDescriptor[plugins.length - removedCount];
			int index = 0;
			for (int i = 0; i < plugins.length; i++) {
				if (plugins[i] != null)
					newList[index++] = plugins[i];
			}
			this.plugins.put(pluginId, newList);
		}
	}*/

	/**
	 * Removes all versions of the given plug-in from this registry.
	 * This method has no effect if such a plug-in cannot be found.
	 *
	 * @param pluginId the unique identifier of the plug-ins to remove
	 */
	public void removePlugins(String pluginId) {
		plugins.remove(pluginId);
	}

	public void setInstancePluginEditor(String idPlugin,String pluginVersion, IMainTabPanel plugin){
		
		Object[] infoPluginList = (Object[])this.plugins.get(idPlugin);
		
		if (infoPluginList != null) {
			
			for (int i = 0; i < infoPluginList.length; i++) {
				PlugInDescriptor currentPlugin=(PlugInDescriptor)((Object[])infoPluginList[i])[0];
				if (currentPlugin.getVersion().compareTo(pluginVersion)==0){
					((Object[])infoPluginList[i])[1]=plugin;
					return;
				}				
			}			
		}		
	}
	
	public void setInstancePluginFile(String idPlugin,String pluginVersion, IFilePlug plugin){
		
		Object[] infoPluginList = (Object[])this.plugins.get(idPlugin);
		
		if (infoPluginList != null) {
			
			for (int i = 0; i < infoPluginList.length; i++) {
				PlugInDescriptor currentPlugin=(PlugInDescriptor)((Object[])infoPluginList[i])[0];
				if (currentPlugin.getVersion().compareTo(pluginVersion)==0){
					((Object[])infoPluginList[i])[1]=plugin;
					return;
				}				
			}			
		}		
	}
	
	public PlugInDescriptor getPluginDescriptorFor(IMainTabPanel pluginInstance){
		
		for (Iterator i = plugins.values().iterator(); i.hasNext();) {
			Object[] entries = (Object[]) i.next();

			for (int j = 0; j < entries.length; j++){
				
				Object currentInstance = ((Object[])entries[j])[1];
				if (currentInstance!=null)
					if(currentInstance.equals(pluginInstance))
						return (PlugInDescriptor)((Object[])entries[j])[0];
			}
			
		}
		
		return null;
	}
	
	public PlugInDescriptor getPluginDescriptorFor(IFilePlug pluginInstance){
		
		for (Iterator i = plugins.values().iterator(); i.hasNext();) {
			Object[] entries = (Object[]) i.next();

			for (int j = 0; j < entries.length; j++){
				
				Object currentInstance = ((Object[])entries[j])[1];
				if (currentInstance!=null)
					if(currentInstance.equals(pluginInstance))
						return (PlugInDescriptor)((Object[])entries[j])[0];
			}
			
		}
		
		return null;
	}
	//////////////GESTIONE EXTENSION POINT - SPERIMENTALE
	/**
	 * Restituisce i descrittori di tutti gli extension point di tutti i plugin istallati,
	 * compresi quelli di plugin con stesso id e diversa versione.
	 * 
	 * @return
	 */
	public ExtensionPointDescriptor[] getAllExtensionPoints() {

		PlugInDescriptor[] list = this.getAllPluginDescriptor();
		if (list == null)
			return new ExtensionPointDescriptor[0];
		ArrayList result = new ArrayList();
		for (int i = 0; i < list.length; i++) {
			ExtensionPointDescriptor[] pointList = list[i].getExtensionPoints();
			if (pointList != null) {
				for (int j = 0; j < pointList.length; j++)
					result.add(pointList[j]);
			}
		}
		return (ExtensionPointDescriptor[]) result.toArray(
			new ExtensionPointDescriptor[result.size()]);

	}

	/**
		 * Restituisce i descrittori di tutti gli extension point di tutti i plugin istallati.
		 * Se sono istallati plugin con stesso id e diversa versione, vengono restituiti gli
		 *  extension point del plug con versione più recente
		 * 
		 * @return
		 */
	public ExtensionPointDescriptor[] getExtensionPoints() {

		PlugInDescriptor[] list = this.getAllRecentPluginDescriptor();
		if (list == null)
			return new ExtensionPointDescriptor[0];
		ArrayList result = new ArrayList();
		for (int i = 0; i < list.length; i++) {
			ExtensionPointDescriptor[] pointList = list[i].getExtensionPoints();
			if (pointList != null) {
				for (int j = 0; j < pointList.length; j++)
					result.add(pointList[j]);
			}
		}
		return (ExtensionPointDescriptor[]) result.toArray(
			new ExtensionPointDescriptor[result.size()]);

	}

	/**
	 * restituisce la lista degli extension point  dichiarati nel manifest file dal plugin con identificatore passato come argomento.
	 * Se ci sono più versioni dello stesso plugin restituisce gli host della versione più recente.
	 * 
	 * @return
	 */
	public ExtensionPointDescriptor[] getExtensionPointForPlugId(String pluginId) {

		PlugInDescriptor pd = this.getPluginDescriptor(pluginId);
		ExtensionPointDescriptor[] list = pd.getExtensionPoints();

		if (list == null)
			return new ExtensionPointDescriptor[0];
		else
			return list;

	}

	/**
			 * Restituisce il descriptor dell'extension point  con identificatore passato come argomento.
			 * Null se non esiste.
			 * 
			 * @return
			 */
	public ExtensionPointDescriptor getExtensionPointForId(String extPointId) {

		PlugInDescriptor[] pdList = this.getAllRecentPluginDescriptor();

		for (int i = 0; i < pdList.length; i++) {

			ExtensionPointDescriptor[] extPoints =
				pdList[i].getExtensionPoints();

			if (extPoints != null)
				for (int j = 0; j < extPoints.length; j++) {

					if (extPoints[j].getIdentifier().compareTo(extPointId) == 0)
						return extPoints[j];

				}
		}
		return null;

	}

	/**
	 * restituisce la lista degli host dichiarati nel manifest file da tutti i plugin (con versione più recente).
	 * 
	 * @return
	 */
	public HostDescriptor[] getHosts() {

		PlugInDescriptor[] list = this.getAllRecentPluginDescriptor();

		if (list == null)
			return new HostDescriptor[0];
		ArrayList result = new ArrayList();
		for (int i = 0; i < list.length; i++) {
			HostDescriptor[] pointList = list[i].getHosts();
			if (pointList != null) {
				for (int j = 0; j < pointList.length; j++)
					result.add(pointList[j]);
			}
		}
		return (HostDescriptor[]) result.toArray(
			new HostDescriptor[result.size()]);

	}

	/**
	 * restituisce la lista degli host dichiarati nel manifest dile dal plugin con identificatore passato come argomento.
	 * Se ci sono più versioni dello stesso plugin restituisce gli host della versione più recente.
	 * 
	 * @return
	 */
	public HostDescriptor[] getHostsForPlugId(String pluginId) {

		PlugInDescriptor pd = this.getPluginDescriptor(pluginId);
		HostDescriptor[] list = pd.getHosts();

		if (list == null)
			return new HostDescriptor[0];
		else
			return list;

	}

	/**
		 * Restituisce il descriptor dell'hosts con identificatore passato come argomento.
		 * Null se non esiste.
		 * 
		 * @return
		 */
	public HostDescriptor getHostForId(String hostId) {

		PlugInDescriptor[] pdList = this.getAllRecentPluginDescriptor();

		for (int i = 0; i < pdList.length; i++) {

			HostDescriptor[] hosts = pdList[i].getHosts();
			
			if (hosts!=null)
			for (int j = 0; j < hosts.length; j++) {

				if (hosts[j].getId().compareTo(hostId) == 0)
					return hosts[j];

			}
		}
		return null;

	}

	public HostDescriptor[] getHostsForExtPointId(String idExtPoint) {

		PlugInDescriptor[] list = this.getAllRecentPluginDescriptor();

		if (list == null)
			return new HostDescriptor[0];

		ArrayList result = new ArrayList();

		for (int i = 0; i < list.length; i++) {

			HostDescriptor[] hostList = list[i].getHosts();

			if (hostList != null) {
				for (int j = 0; j < hostList.length; j++)
					if (hostList[j].getExtensionPoint().compareTo(idExtPoint)
						== 0)
						result.add(hostList[j]);
			}
		}
		return (HostDescriptor[]) result.toArray(
			new HostDescriptor[result.size()]);

	}
	
	public boolean removeHost(String idHost) {

		PlugInDescriptor[] list = this.getAllRecentPluginDescriptor();
		if (list == null)
			return false;

		for (int i = 0; i < list.length; i++) {

			HostDescriptor[] currentHosts = list[i].getHosts();
			if (currentHosts!=null)
				for (int j = 0; j < currentHosts.length; j++) {
					
					if (currentHosts[j].getId().compareTo(idHost)==0){
						
						list[i].removeHost(idHost);
						return true;
					}
						
				}
			
		}

		return false;
	}

	public PlugInDescriptor getSubVersion(String PluginId, String version){
		
		
		PlugInDescriptor[] listaAllVersion=this.getPluginsDescriptorFor(PluginId);
		String tempVersion = "0.0.0";
		int indice = -1;
		for (int i = 0; i < listaAllVersion.length; i++) {
			
			if ((versionUpVersion(listaAllVersion[i].getVersion(),version)==0)&&
					(versionUpVersion(listaAllVersion[i].getVersion(),tempVersion)==1)&&(listaAllVersion[i].getVersion().compareTo(tempVersion)!=0)){
				tempVersion=listaAllVersion[i].getVersion();
				indice=i;
			}					
		}
		
		if (indice==-1)
			return null;
		else
			return listaAllVersion[indice];
		
	}
	/**
	 * restituisce 1 se version1 è maggiore o uguale a version2,
	 *  0 se version1 è minore strettamente di version2,
	 *  -1 se le stringhe version1 o version2 non sono nella forma: I.I.I dove I è un intero 
	 * @param version1
	 * @param version2
	 * @return
	 */
	public static int versionUpVersion(String version1, String version2) {

		if (version2 == null)
			return 1;

		boolean result = false;
		try {
			// VINCOLO: la versione deve essere nella forma I.I.I , dove I è un intero
			Integer uno =
				new Integer(version1.substring(0, version1.indexOf(".")));
			Integer due =
				new Integer(
					version1.substring(
						version1.indexOf(".") + 1,
						version1.lastIndexOf(".")));
			Integer tre =
				new Integer(
					version1.substring(
						version1.lastIndexOf(".") + 1,
						version1.length()));

			Integer quattro =
				new Integer(version2.substring(0, version2.indexOf(".")));
			Integer cinque =
				new Integer(
					version2.substring(
						version2.indexOf(".") + 1,
						version2.lastIndexOf(".")));
			Integer sei =
				new Integer(
					version2.substring(
						version2.lastIndexOf(".") + 1,
						version2.length()));

			if ((uno.intValue() == quattro.intValue())
				&& (due.intValue() == cinque.intValue())
				&& (tre.intValue() == sei.intValue()))
				return 1;

			if (uno.intValue() > quattro.intValue())
				return 1;

			if ((uno.intValue() == quattro.intValue())
				&& (due.intValue() > cinque.intValue()))
				return 1;

			if ((uno.intValue() == quattro.intValue())
				&& (due.intValue() == cinque.intValue())
				&& (tre.intValue() > sei.intValue()))
				return 1;

		} catch (Exception e) {
			return -1;
		}
		return 0;

	}
}
