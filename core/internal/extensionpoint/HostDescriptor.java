/*
 * Created on 30-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.extensionpoint;

import core.internal.plugin.ConfigurationElement;
import core.internal.plugin.PlugInDescriptor;
/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HostDescriptor {

	private String extensionPoint = null;
	private String id = null;
	private String name = null;
	private String classPath = null;
	private String[] hostRequired = null;

	private boolean activated = false;
	private ConfigurationElement[] elements = null;
	private PlugInDescriptor plugin = null;

	private IGenericHost hostReference = null;

	/**
	 * 
	 */
	public HostDescriptor() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return Returns the extensionPoint.
	 */
	public String getExtensionPoint() {
		return extensionPoint;
	}
	/**
	 * @param extensionPoint The extensionPoint to set.
	 */
	public void setExtensionPoint(String extensionPoint) {
		this.extensionPoint = extensionPoint;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the name ffffff.
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
	 * @return Returns the hostRequired.
	 */
	public String[] getHostRequired() {
		return hostRequired;
	}

	public void addHostRequired(String idHost) {

		String[] list;

		if (this.hostRequired == null) {
			list = new String[1];
		} else {
			list = new String[this.hostRequired.length + 1];
			System.arraycopy(hostRequired, 0, list, 0, hostRequired.length);
		}

		list[list.length - 1] = idHost;
		this.hostRequired = list;

	}

	public void addSubElement(ConfigurationElement element) {

		ConfigurationElement[] list;

		if (this.elements == null) {
			list = new ConfigurationElement[1];
		} else {
			list = new ConfigurationElement[this.elements.length + 1];
			System.arraycopy(elements, 0, list, 0, elements.length);
		}

		list[list.length - 1] = element;
		this.elements = list;

	}

	/**
	 * @return Returns the elements.
	 */
	public ConfigurationElement[] getSubElements() {
		return elements;
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
	 * @return Returns the hostReference.
	 */
	public IGenericHost getHostReference() {
		return hostReference;
	}
	/**
	 * @param hostReference The hostReference to set.
	 */
	public void setHostReference(IGenericHost hostReference) {
		this.hostReference = hostReference;
	}
	/**
	 * @return
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * @param b
	 */
	public void setActivated(boolean b) {
		activated = b;
	}
	
	

}
