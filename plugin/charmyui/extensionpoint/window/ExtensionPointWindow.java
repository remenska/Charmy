/*
 * Created on 16-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import plugin.charmyui.extensionpoint.window.data.DataHostWindow;
import plugin.charmyui.extensionpoint.window.event.ActiveMainPanel;
import plugin.charmyui.extensionpoint.window.event.InactiveMainPanel;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import core.internal.extensionpoint.ExtensionPointDescriptor;
import core.internal.extensionpoint.HostDescriptor;
import core.internal.extensionpoint.IExtensionPoint;
import core.internal.extensionpoint.IGenericHost;
import core.internal.extensionpoint.event.EventService;
import core.internal.plugin.PlugManager;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.ui.PlugDataWin;


/**
 * @author ezio di nisio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExtensionPointWindow
	implements IExtensionPoint, AncestorListener {

	private Object windowRemoved = null;
    
	private DataHostWindow[] listaHostWindow;

	private ExtensionPointDescriptor extPointDescriptor;

	PlugManager plugManager;

	PlugDataWin plugDataWin;

	private IHostWindow currentPanelActive = null;

	//private LinkedList listaExtPointListener;
	/**
	 * 
	 */
	public ExtensionPointWindow() {
		super();

	}

	/* (non-Javadoc)
	 * @see extensionpoint.IExtensionPoint#init(plugin.PlugManager, java.util.Vector)
	 */
	public void init(
		PlugManager plugManager,
		ExtensionPointDescriptor descriptorExtPoint) {

		extPointDescriptor = descriptorExtPoint;
		this.plugManager = plugManager;
		plugDataWin = plugManager.getPlugDataWin();

	}
	/* (non-Javadoc)
	 * @see extensionpoint.IExtensionPoint#addHost(extensionpoint.DataHost)
	 */
	public boolean addHost(HostDescriptor elementHost) {

		// questo extensionpoint chiede che i suoi host siano sottoclasse della nativa javax.swing.JPanel - standardizza un generico pannello 
		//verifico compatibilità
		try {

			//nuovo pannello

			String idHost = elementHost.getId();

			// doppio casting sull'host per verificare i vincoli di classe
			JPanel hostWindow = (JPanel) elementHost.getHostReference();
			IHostWindow hostWindowVincolo2 =
				(IHostWindow) elementHost.getHostReference();
			//controllo sulla molteplicità dell'extension point
			//if (!this.multiplicityRespected(elementHost))
			//	return false;

			//ci teniamo i dati dell'host
			DataHostWindow datahostwindow = new DataHostWindow(elementHost);

			addHostWindow(datahostwindow);

			hostWindow.setBorder(
				BorderFactory.createEtchedBorder(Color.DARK_GRAY, Color.GRAY));
			hostWindow.setLayout(new BorderLayout());
			plugDataWin.getMainPanel().addTab(
				elementHost.getName(),
				hostWindow);

			//ci mettiamo in ascolto sull'attivazione del pannello, manderemo poi una notifica di pannello  attivo(cioè plug attivo o disattivo) , ai listener in ascolto: Comunicazione Extension-point->esterno
			hostWindow.addAncestorListener(this);
		
		} catch (ClassCastException e) {

			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.AncestorListener#ancestorAdded(javax.swing.event.AncestorEvent)
	 */
	public void ancestorAdded(AncestorEvent arg0) {

		//prima notifichiamo solo all'host che è stato attivato
		DataHostWindow windowCheck = null;
		Object source = arg0.getSource();

		// IHostWindow lastPanelActive = currentPanelActive;
		if (listaHostWindow != null)
			for (int i = 0; i < listaHostWindow.length; i++) {

				windowCheck = listaHostWindow[i];
				if (windowCheck.getHostReference().equals(source)) {

					//notifichiamo all'host window la sua attivazione
					currentPanelActive =
						(IHostWindow) windowCheck.getHostReference();
					currentPanelActive.windowActive();
					break;
				}
			}

		//pubblichiamo l'evento per chi ne fosse interessato
		plugManager.getEventService().pub(
			new ActiveMainPanel(currentPanelActive));
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.AncestorListener#ancestorMoved(javax.swing.event.AncestorEvent)
	 */
	public void ancestorMoved(AncestorEvent arg0) {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see javax.swing.event.AncestorListener#ancestorRemoved(javax.swing.event.AncestorEvent)
	 */
	public void ancestorRemoved(AncestorEvent arg0) {

		//prima notifichiamo solo all'host che è stato disattivato
		DataHostWindow windowCheck = null;
		Object source = arg0.getSource();

		IHostWindow panelInactive = null;
		if (listaHostWindow != null)
			for (int i = 0; i < listaHostWindow.length; i++) {

				windowCheck = listaHostWindow[i];
				if (windowCheck.getHostReference().equals(source)) {

					//notifichiamo all'host window la sua attivazione
					panelInactive =
						(IHostWindow) windowCheck.getHostReference();
					panelInactive.windowInactive();
					break;
				}
			}
		this.windowRemoved = source;

		//pubblichiamo l'evento per chi ne fosse interessato
		plugManager.getEventService().pub(
			new InactiveMainPanel(panelInactive));
	}

	public IHostWindow getWindow(String idHostWindow) {

		if (listaHostWindow != null)
			for (int i = 0; i < listaHostWindow.length; i++) {
				DataHostWindow dataWindow = listaHostWindow[i];

				if (dataWindow.getId().compareTo(idHostWindow) == 0) {
					return (IHostWindow) dataWindow.getHostReference();
				}

			}

		return null;
	}
	/*public boolean multiplicityRespected(HostDescriptor elementHost){
		
		if (((String)extPointDescriptor.get(3)).compareTo("1")==0){
			
			for(int i=0;i<listaHostWindow.size();i++){ 
				
				IMainTabPanel plugHost = ((DataHostWindow)listaHostWindow.get(i)).getPlugOwner();
				
				//if (plugHost.equals(elementHost.g)){ 
				//	return false;
				//}
			}
			
		}    	
		return true;    	
	}*/

	private void addHostWindow(DataHostWindow hostWindow) {

		DataHostWindow[] list;

		if (this.listaHostWindow == null) {
			list = new DataHostWindow[1];
		} else {
			list = new DataHostWindow[this.listaHostWindow.length + 1];
			System.arraycopy(
				listaHostWindow,
				0,
				list,
				0,
				listaHostWindow.length);
		}

		list[list.length - 1] = hostWindow;
		this.listaHostWindow = list;

	}
	
	public EventService getEventService (){
		
			return this.plugManager.getEventService();
		}

		public DataHostWindow getDataHost(IHostWindow window) {

				if (listaHostWindow != null)
					for (int i = 0; i < listaHostWindow.length; i++) {
						DataHostWindow windowRegistrato = listaHostWindow[i];
						if ((windowRegistrato.getHostReference()).equals(window)) {

							return windowRegistrato;
						}

					}
				return null;
			}

}
