/*
 * Created on 23-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.plugin;

import core.internal.extensionpoint.HostDescriptor;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConfigurationElement {

	
	private String name = null;
	private ConfigurationProperty[] properties = null;
	private ConfigurationElement[] children = null;

	private Object parent = null;
	// elemento genitore oppure l'host nella quale l'elemento è dichiarato

	/**
	 * 
	 */
	public ConfigurationElement() {
		super();
		// TODO Auto-generated constructor stub
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
	 * @return Returns the parent.
	 */
	public Object getParent() {
		return parent;
	}

	/**
	 * Ritorna il descrittore dell'host nel quale questo elemento è dichiarato.
	 * Se l'elemento è alla radice dell'albero degli elementi dell'host il metodo è equivalente a getParent()
	 * 
	 * @return
	 */

	public HostDescriptor getParentHost() {
		Object p = getParent();
		while (p != null && p instanceof ConfigurationElement)
			p = ((ConfigurationElement) p).getParent();
		return (HostDescriptor) p;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(Object parent) {
		this.parent = parent;
	}
	
	/**
	 * @return Returns the children.
	 */
	public ConfigurationElement[] getChildren() {
		return children;
	}
	/**
	 * @return Returns the properties.
	 */
	public ConfigurationProperty[] getProperties() {
		return properties;
	}
	
	
	public String getPropertyValue (String propertyName){
		
		if (this.properties!=null)
		for (int i = 0; i < properties.length; i++) {
			
			if (properties[i].getName().compareTo(propertyName)==0)
			return properties[i].getValue();
		}
		
		return null;
	}

	public void addConfigurationProperty(ConfigurationProperty property) {

		ConfigurationProperty[] list;

		if (this.properties == null) {
			list = new ConfigurationProperty[1];
		} else {
			list = new ConfigurationProperty[this.properties.length + 1];
			System.arraycopy(properties, 0, list, 0, properties.length);
		}

		list[list.length - 1] = property;
		this.properties = list;

	}

	public void addChildren(ConfigurationElement element) {

		ConfigurationElement[] list;

		if (this.children == null) {
			list = new ConfigurationElement[1];
		} else {
			list = new ConfigurationElement[this.children.length + 1];
			System.arraycopy(children, 0, list, 0, children.length);
		}

		list[list.length - 1] = element;
		this.children = list;
	}

}
