/*
 * Created on 23-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.extensionpoint;

import core.internal.plugin.PlugInDescriptor;
import core.internal.plugin.PluginModel;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExtensionPointDescriptor extends PluginModel{

	private String name = null;
	private String classPath = null;
	private String schema = null;
	//private HostDescriptor[] activateHost = null; 
	private String multiplicity = null;

	private PlugInDescriptor plugin = null;
	// Descriptor del plugin dove è dichiarato questo extPoint

	private IExtensionPoint extensionPointReference = null;
	/**
	 * 
	 */
	public ExtensionPointDescriptor() {

	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the classPath.
	 */
	public String getClassPath() {
		return classPath;
	}
	/**
	 * @param classPath The classPath to set.
	 */
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	/**
	 * @return Returns the plugin.
	 */
	public PlugInDescriptor getParent() {
		return plugin;
	}
	/**
	 * @param plugin The plugin to set.
	 */
	public void setParent(PlugInDescriptor plugin) {
		this.plugin = plugin;
	}
	/**
	 * @return Returns the schema.
	 */
	public String getSchema() {
		return schema;
	}
	/**
	 * @param schema The schema to set.
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	/*public void setActivateHost(HostDescriptor host) {
	
		if (this.activateHost==null)
			this.activateHost= new HostDescriptor[1];
		
		this.activateHost[this.activateHost.length]=host;
											   
		
	}*/
	/**
	 * @return Returns the multiplicity.
	 */
	public String getMultiplicity() {
		return multiplicity;
	}
	/**
	 * @param multiplicity The multiplicity to set.
	 */
	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}
	public void addExtensionPointRequired(String idExtPoint) {

		
		PluginModel[] list;

		if (this.dependenceList == null) {
			list = new PluginModel[1];
		} else {
			list = new PluginModel[this.dependenceList.length + 1];
			System.arraycopy(
					dependenceList,
				0,
				list,
				0,
				dependenceList.length);
		}
        
		list[list.length - 1] = new PluginModel(idExtPoint,null);
		this.dependenceList = list;
	}

	/**
	 * @return Returns the extensionPointReference.
	 */
	public IExtensionPoint getExtensionPointReference() {
		return extensionPointReference;
	}
	/**
	 * @param extensionPointReference The extensionPointReference to set.
	 */
	public void setExtensionPointReference(IExtensionPoint extensionPointReference) {
		this.extensionPointReference = extensionPointReference;
	}
}
