/*
 * Created on 20-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.toolbar.action;

import plugin.charmyui.extensionpoint.toolbar.ExtensionPointToolbar;
import plugin.charmyui.extensionpoint.toolbar.GenericHostToolbar;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IButtonAction {
	
	
	public void setDati(String id, ExtensionPointToolbar extensionPointToolbar, GenericHostToolbar toolbarOwner);
    public void setStatus (int status);
    public String getIdButton();
    
   
}
