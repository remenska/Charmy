/*
 * Created on 26-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.window;


import core.internal.extensionpoint.IGenericHost;
import core.internal.extensionpoint.event.EventService;

/**
 *
 * Interfaccia che devono implementare gli Host window che vogliono agganciarsi a questo extension point.
 * Verrà utilizzato per la notifica agli host di eventi relativi al pannello principale. L'Extension Point è il gestore unico di queste notifiche.
 *  
 * Chiunque voglia essere in ascolto su questi eventi deve implemetare l'interfaccia e registrarsi.
 * @author ezio di nisio
 */
public interface IHostWindow extends IGenericHost {

    /**
     * 
     *  Charmy Project -
     * L'extension point invoca questo metodo subito dopo che l'aggancio dell'host è andato a buon fine.
     *  @author ezio di nisio
     *
     */
    
    public void windowActive();
 
    public void windowInactive();
    
    
}
