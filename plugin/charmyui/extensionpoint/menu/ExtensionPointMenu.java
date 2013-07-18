/*
 * Created on 24-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.menu;

import java.util.Vector;

import core.internal.extensionpoint.ExtensionPointDescriptor;
import core.internal.extensionpoint.HostDescriptor;
import core.internal.extensionpoint.IExtensionPoint;
import core.internal.plugin.PlugManager;


/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExtensionPointMenu implements IExtensionPoint {

	/**
	 * 
	 */
	public ExtensionPointMenu() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see core.extensionpoint.IExtensionPoint#addHost(core.plugin.HostDescriptor)
	 */
	public boolean addHost(HostDescriptor elementHost) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see core.extensionpoint.IExtensionPoint#init(core.plugin.PlugManager, core.plugin.ExtensionPointDescriptor)
	 */
	public void init(
		PlugManager plugManager,
		ExtensionPointDescriptor extPointDescriptor) {
		// TODO Auto-generated method stub

	}

}
