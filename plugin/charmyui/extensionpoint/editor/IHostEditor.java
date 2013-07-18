/*
 * Created on 24-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.editor;

import core.internal.extensionpoint.IGenericHost;
import core.internal.extensionpoint.event.EventService;
/**
 * @author ezio di nisio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IHostEditor extends IGenericHost {
    
   
    
    public void editorActive();
    
    public void editorInactive();
    
    public void setEditorStatus(int status);
    
    public int getEditorStatus();
    
   
    
}
