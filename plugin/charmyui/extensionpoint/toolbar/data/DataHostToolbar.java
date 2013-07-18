/*
 * Created on 22-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.toolbar.data;

import java.util.Vector;
import javax.swing.AbstractButton;

import core.internal.extensionpoint.HostDescriptor;
import core.internal.extensionpoint.IGenericHost;
import core.internal.plugin.ConfigurationElement;
import core.internal.plugin.ConfigurationProperty;
import core.internal.plugin.PlugInDescriptor;
;
//import plugin.charmyui.extensionpoint.editor.host.IExtensionPointEditor;

/**
 * @author ezio di nisio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataHostToolbar extends HostDescriptor {

	
	private DataButton[] buttons;
	
	
	private HostDescriptor hostDescriptor;
	

	private boolean firstActivation=true;

	/**
	 * 
	 */
	public DataHostToolbar(HostDescriptor hostDescriptor) {

		this.hostDescriptor = hostDescriptor;

	}
	
	
	/**
	 * @return
	 */
	public HostDescriptor getHostDescriptor() {
		return hostDescriptor;
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#addHostRequired(java.lang.String)
	 */
	public void addHostRequired(String idHost) {
		// TODO Auto-generated method stub
		hostDescriptor.addHostRequired(idHost);
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#addSubElement(core.plugin.ConfigurationElement)
	 */
	public void addSubElement(ConfigurationElement element) {
		// TODO Auto-generated method stub
		hostDescriptor.addSubElement(element);
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#getClassPath()
	 */
	public String getClassPath() {
		// TODO Auto-generated method stub
		return hostDescriptor.getClassPath();
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#getExtensionPoint()
	 */
	public String getExtensionPoint() {
		// TODO Auto-generated method stub
		return hostDescriptor.getExtensionPoint();
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#getHostReference()
	 */
	public IGenericHost getHostReference() {
		// TODO Auto-generated method stub
		return hostDescriptor.getHostReference();
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#getHostRequired()
	 */
	public String[] getHostRequired() {
		// TODO Auto-generated method stub
		return hostDescriptor.getHostRequired();
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#getId()
	 */
	public String getId() {
		// TODO Auto-generated method stub
		return hostDescriptor.getId();
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return hostDescriptor.getName();
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#getParent()
	 */
	public PlugInDescriptor getParent() {
		// TODO Auto-generated method stub
		return hostDescriptor.getParent();
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#getSubElements()
	 */
	public ConfigurationElement[] getSubElements() {
		// TODO Auto-generated method stub
		return hostDescriptor.getSubElements();
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#isActivated()
	 */
	public boolean isActivated() {
		// TODO Auto-generated method stub
		return hostDescriptor.isActivated();
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#setActivated(boolean)
	 */
	public void setActivated(boolean b) {
		// TODO Auto-generated method stub
		hostDescriptor.setActivated(b);
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#setClassPath(java.lang.String)
	 */
	public void setClassPath(String classPath) {
		// TODO Auto-generated method stub
		hostDescriptor.setClassPath(classPath);
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#setExtensionPoint(java.lang.String)
	 */
	public void setExtensionPoint(String extensionPoint) {
		// TODO Auto-generated method stub
		hostDescriptor.setExtensionPoint(extensionPoint);
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#setHostReference(core.extensionpoint.IGenericHost)
	 */
	public void setHostReference(IGenericHost hostReference) {
		// TODO Auto-generated method stub
		hostDescriptor.setHostReference(hostReference);
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#setId(java.lang.String)
	 */
	public void setId(String id) {
		// TODO Auto-generated method stub
		hostDescriptor.setId(id);
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#setName(java.lang.String)
	 */
	public void setName(String name) {
		// TODO Auto-generated method stub
		hostDescriptor.setName(name);
	}

	/* (non-Javadoc)
	 * @see core.plugin.HostDescriptor#setParent(core.plugin.PlugInDescriptor)
	 */
	public void setParent(PlugInDescriptor plugin) {
		// TODO Auto-generated method stub
		hostDescriptor.setParent(plugin);
	}

	public void addButton(DataButton button){
		
		DataButton[] list;

		if (this.buttons == null) {
			list = new DataButton[1];
		} else {
			list = new DataButton[this.buttons.length + 1];
			System.arraycopy(buttons, 0, list, 0, buttons.length);
		}

		list[list.length - 1] = button;
		this.buttons = list;
	}


	public boolean isFirstActivation() {
		return firstActivation;
	}

	public void setFirstActivation(boolean firstActivation) {
		this.firstActivation = firstActivation;
	}

	public DataButton[] getButtons() {
		return buttons;
	}
}
