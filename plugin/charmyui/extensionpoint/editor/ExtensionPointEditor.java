/*
 * Created on 7-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.editor;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Iterator;
import java.util.Vector;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;


import plugin.charmyui.extensionpoint.window.ExtensionPointWindow;
import plugin.charmyui.extensionpoint.window.IHostWindow;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;

import core.internal.extensionpoint.ExtensionPointDescriptor;
import core.internal.extensionpoint.HostDescriptor;
import core.internal.extensionpoint.IExtensionPoint;
import core.internal.extensionpoint.IGenericHost;
import core.internal.extensionpoint.event.EventBase;
import core.internal.extensionpoint.event.EventService;
import core.internal.extensionpoint.event.ISubscriberListener;
import core.internal.plugin.PlugManager;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.resources.ui.WithGraphWindow;

import plugin.charmyui.extensionpoint.editor.data.DataHostEditor;
import plugin.charmyui.extensionpoint.editor.event.ActiveEditor;
import plugin.charmyui.extensionpoint.editor.event.InactiveEditor;
import plugin.charmyui.extensionpoint.window.event.ActiveMainPanel;
import plugin.charmyui.extensionpoint.window.event.InactiveMainPanel;

/**
 * @author eziolotta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExtensionPointEditor
	implements IExtensionPoint, ChangeListener, ISubscriberListener {

	/**
	 * lista che contiene tutti gli host agganciati a questo ext-point
	 */
	private DataHostEditor[] listaHostEditor = null;

	/**
	 * lista contenente Vector[2] - 
	 * Vector[0]: Window proprietario, è il main panel di un plug 
	 * Vector[1]: JTabbedPanel pannello tabbellare di editor associato al pannello principale di un plug
	 */
	private LinkedList listaTabEditor;

	/**
	 * descrittore dell'extensionpoint, contiene i dati ma non vengono usati per il momento 
	 */
	private ExtensionPointDescriptor extPointDescriptor;

	//private IHostEditor lastEditorActive; 

	PlugManager plugManager;
	ExtensionPointWindow extensionPointWindow;

	/**
	 * 
	 */
	public ExtensionPointEditor() {
		super();

		listaTabEditor = new LinkedList();

	}

	/* (non-Javadoc)
	 * @see extensionpoint.IExtensionPoint#init(plugin.PlugManager, java.util.Vector)
	 */
	public void init(
		PlugManager plugManager,
		ExtensionPointDescriptor descriptorExtPoint) {

		this.extPointDescriptor = descriptorExtPoint;
		this.plugManager = plugManager;

		extensionPointWindow =
			(ExtensionPointWindow) plugManager.getExtensionPoint(
				"plugin.charmyui.window");

		// ci sottoscriviamo per ricevere notifiche di eventi relativi al cambiamento di mailPanel di un plug
		plugManager.getEventService().sub(
			this,
			ActiveMainPanel.class.getName());
		plugManager.getEventService().sub(
			this,
			InactiveMainPanel.class.getName());

	}

	/* (non-Javadoc)
	 * @see extensionpoint.IExtensionPoint#addHost(extensionpoint.DataHost)
	 */
	public boolean addHost(HostDescriptor elementHost) {

		try {
			//vincoli di classe
			// bisogna fare casting su JPanel o JComponent e IHostEditor                    
			IHostEditor editorInterfacciato =
				(IHostEditor) elementHost.getHostReference();
			Component editorGenerico =
				(Component) elementHost.getHostReference();

			DataHostEditor datahosteditor = new DataHostEditor(elementHost);
			this.addHostEditor(datahosteditor);

			String idWindowOwner = (elementHost.getHostRequired())[0];

			IHostWindow WindowOwner =
				this.extensionPointWindow.getWindow(idWindowOwner);

			//  CONTROLLARE SE VA BENE- FORSE ERRORE
			//se è il primo editor inserito nel window lo settiamo ad attivo
			if (this.getEditorActive(WindowOwner) == null) {
				datahosteditor.setEditorAttivo(true);
				//lastEditorActive= (IHostEditor)datahosteditor.getHost();
			}

			
			JScrollPane ClassScroller = new JScrollPane(editorGenerico);
			ClassScroller.setWheelScrollingEnabled(false);
			ClassScroller.setAutoscrolls(true);
			datahosteditor.setClassScroller(ClassScroller);

			JTabbedPane tabEditorPanel = this.newTabEditor(WindowOwner);
			tabEditorPanel.addTab(datahosteditor.getName(), ClassScroller);

		} catch (ClassCastException e) {

			return false;
		}

		return true;
	}

	/**
	 * 
	 *  Charmy Project-
	 * crea un nuovo pannello tabbellare associato ad un pannello principale di un plug (un host agganciato sull'Ext-Point window)
	 * Il pannello tab conterrà gli editor  - ritorna il reference al panello creato. Viene inserito in listaTabEditor.
	 * - return JTAbbedPane, se già esiste un tab associato al window
	 *  @author ezio di nisio
	 *
	 */
	private JTabbedPane newTabEditor(IHostWindow windowOwner) {

		for (int i = 0; i < listaTabEditor.size(); i++) {

			IHostWindow checkWindow =
				(IHostWindow) ((Vector) listaTabEditor.get(i)).get(0);
			if (checkWindow.equals(windowOwner)) {

				return (JTabbedPane) ((Vector) listaTabEditor.get(i)).get(1);
			}
		}

		JTabbedPane newTabPanel = new JTabbedPane();
		((JPanel) windowOwner).add(BorderLayout.CENTER, newTabPanel);
		// inseriamo il pannello degli edior nel pannello principale del plug, (extensionpoint ui.window)

		Vector dataTab = new Vector(2);
		dataTab.add(0, windowOwner);
		dataTab.add(1, newTabPanel);

		this.listaTabEditor.add(dataTab);

		//ci mettiamo in ascolto sul cambiamento di tab
		newTabPanel.addChangeListener(this);

		return newTabPanel;
	}

	public IHostEditor getHostEditor(String idHostEditor) {

		if (listaHostEditor != null)
			for (int i = 0; i < listaHostEditor.length; i++) {
				DataHostEditor editorRegistrato = listaHostEditor[i];

				if (editorRegistrato.getId().compareTo(idHostEditor) == 0) {

					return (IHostEditor) editorRegistrato.getHostReference();
				}

			}
		return null;
	}

	public DataHostEditor getDataHost(IHostEditor editor) {

		if (listaHostEditor != null)
			for (int i = 0; i < listaHostEditor.length; i++) {
				DataHostEditor editorRegistrato = listaHostEditor[i];
				if ((editorRegistrato.getHostReference()).equals(editor)) {

					return editorRegistrato;
				}

			}
		return null;
	}

	/**
	 * @return Returns the plugManager.
	 */
	public PlugManager getPlugManager() {
		return plugManager;
	}

	public IHostEditor getEditorActive(IHostWindow window) {

		if (window == null) ///  
			return null;

		if (listaHostEditor != null)
			for (int i = 0; i < listaHostEditor.length; i++) {

				DataHostEditor editorCorrente = listaHostEditor[i];
				if (this
					.extensionPointWindow
					.getWindow(editorCorrente.getWindowOwner())
					.equals(window)) {

					if (editorCorrente.isEditorAttivo()) {

						return (IHostEditor) editorCorrente.getHostReference();
					}
				}
			}
		return null;
	}

	/**
	 * questa classe è in ascolto sui cambiamenti di tab di tutti i JTabbedPanel che inserisce nei pannelli principali dei plug
	 * qui abbiamo la notifica del cambiamento di tab (swing), attiviamo l'editor corrispondente al tab
	 */
	public void stateChanged(ChangeEvent arg0) {

		Component selected =
			((JTabbedPane) arg0.getSource()).getSelectedComponent();

		//prima cosa dobbiamo sapere su quale pannello principale si è verificato l'evento
		IHostWindow mainPanel = null;
		for (int i = 0; i < listaTabEditor.size(); i++) {

			if (((Vector) listaTabEditor.get(i))
				.get(1)
				.equals(arg0.getSource())) {
				mainPanel =
					(IHostWindow) ((Vector) listaTabEditor.get(i)).get(0);
				break;
			}
		}

		//poi cerchiamo fra tutti gli editor quale è stato selezionato, la ricerca viene fatta sulla classe JScrollPane
		//una volta trovato, disattiviamo quello che prima nel pannello era attivo, ed attiviamo il nuovo selezionato
		if (listaHostEditor != null)
			for (int i = 0; i < listaHostEditor.length; i++) {
				DataHostEditor editorCorrente = listaHostEditor[i];

				if (editorCorrente.getClassScroller().equals(selected)) {
					//trovato

					DataHostEditor dataEditorPrecedente =
						this.getDataHost(getEditorActive(mainPanel));
					//NOTIFICHE DISATTIVAZIONE
					((IHostEditor) dataEditorPrecedente.getHostReference())
						.editorInactive();

					dataEditorPrecedente.setEditorAttivo(false);

					//ATTIVAZIONE + NOTIFICHE ATTIVAZIONE                
					editorCorrente.setEditorAttivo(true);
					((IHostEditor) editorCorrente.getHostReference())
						.editorActive();

					//PUBBLICHIAMO gli EVENTi     
					plugManager.getEventService().pub(
						new InactiveEditor(
							(IHostEditor) dataEditorPrecedente
								.getHostReference()));

					plugManager.getEventService().pub(
						new ActiveEditor(
							(IHostEditor) editorCorrente.getHostReference()));

				}
			}
	}

	/* (non-Javadoc)
	 * @see extensionpoint.event.ISubscriberListener#notify(extensionpoint.event.EventBase)
	 */
	public void notify(EventBase event) {

		//ATTIVIAMO L'EDITOR

		if (event.getEventID().compareTo(ActiveMainPanel.class.getName())
			== 0) {

			IHostEditor editorRiattivato =
				this.getEditorActive(
					((ActiveMainPanel) event).getActivePanel());
			if (editorRiattivato != null) {
				editorRiattivato.editorActive();

				//catena evento
				//PUBBLICHIAMO L'EVENTO                              
				plugManager.getEventService().pub(
					new ActiveEditor(editorRiattivato));

			}
		}
		if (event.getEventID().compareTo(InactiveMainPanel.class.getName())
			== 0) {

			IHostEditor editorDisattivato =
				this.getEditorActive(
					((InactiveMainPanel) event).getInactivePanel());
			if (editorDisattivato != null) {
				editorDisattivato.editorInactive();

				//catena evento
				//PUBBLICHIAMO L'EVENTO                              
				plugManager.getEventService().pub(
					new InactiveEditor(editorDisattivato));

			}
		}

	}
	private void addHostEditor(DataHostEditor hostEditor) {

		DataHostEditor[] list;

		if (this.listaHostEditor == null) {
			list = new DataHostEditor[1];
		} else {
			list = new DataHostEditor[this.listaHostEditor.length + 1];
			System.arraycopy(
				listaHostEditor,
				0,
				list,
				0,
				listaHostEditor.length);
		}

		list[list.length - 1] = hostEditor;
		this.listaHostEditor = list;

	}

	public EventService getEventService() {

		return this.plugManager.getEventService();
	}

}
