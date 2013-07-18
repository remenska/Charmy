/*
 * Created on 16-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.extensionpoint.toolbar;

import java.awt.Component;
import java.awt.Insets;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import plugin.charmyui.extensionpoint.editor.ExtensionPointEditor;
import plugin.charmyui.extensionpoint.editor.IHostEditor;
import plugin.charmyui.extensionpoint.editor.event.ActiveEditor;
import plugin.charmyui.extensionpoint.editor.event.InactiveEditor;
import plugin.charmyui.extensionpoint.toolbar.data.DataButton;
import plugin.charmyui.extensionpoint.toolbar.data.DataHostToolbar;
import plugin.charmyui.extensionpoint.toolbar.event.ButtonEvent;
import plugin.charmyui.utility.EditToolbar;
import core.internal.extensionpoint.ExtensionPointDescriptor;
import core.internal.extensionpoint.HostDescriptor;
import core.internal.extensionpoint.IExtensionPoint;
import core.internal.extensionpoint.event.EventBase;
import core.internal.extensionpoint.event.EventService;
import core.internal.extensionpoint.event.ISubscriberListener;
import core.internal.plugin.PlugManager;
import core.internal.ui.PlugDataWin;

/**
 * @author ezio di nisio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExtensionPointToolbar
	implements IExtensionPoint, ISubscriberListener {

	private DataHostToolbar[] listaHostToolbar;

	ExtensionPointDescriptor extPointDescriptor;

	PlugManager plugManager;

	PlugDataWin pdw;

	ExtensionPointEditor extensionPointEditor;

	Border defaultBorder;

	private IHostEditor editorAttivo = null;

	private EditToolbar editToolbar;

	/**
	 * 
	 */
	public ExtensionPointToolbar() {

		defaultBorder = new EmptyBorder(3,3, 3, 3);

	}

	/* (non-Javadoc)
	 * @see extensionpoint.IExtensionPoint#init(plugin.PlugManager, java.util.Vector)
	 */
	public void init(
		PlugManager plugManager,
		ExtensionPointDescriptor descriptorExtPoint) {

		//setto i dati
		extPointDescriptor = descriptorExtPoint;
		this.plugManager = plugManager;
		pdw = plugManager.getPlugDataWin();
		extensionPointEditor =
			(ExtensionPointEditor) plugManager.getExtensionPoint(
				"plugin.charmyui.editor");

		//ci registriamo per l'evento pressbutton sugli host di questo extension point
		plugManager.getEventService().sub(this, ButtonEvent.class.getName());

		//GESTIONE DIPENDENZA EDITOR
		//troviamo il reference dell'extension point editor , e registriamoci per l'evento editor attivo , dobbiamo aggiungere le toolbar sul pannello

		plugManager.getEventService().sub(this, ActiveEditor.class.getName());

		plugManager.getEventService().sub(this, InactiveEditor.class.getName());

		////PROVA - EDIT TOOLBAR        
		editToolbar = new EditToolbar(null);

	}
	/* (non-Javadoc)
	 * @see extensionpoint.IExtensionPoint#addHost(extensionpoint.DataHost)
	 */
	public boolean addHost(HostDescriptor elementHost) {

		try {

			//casting: questo extension-poin accetta solo host sottoclasse di JToolBar ed interfacciati con IHostToolBar
			JToolBar hostToolbar = (JToolBar) elementHost.getHostReference();
			IHostToolbar toolbarInterfacciato =
				(IHostToolbar) elementHost.getHostReference();

			String idHost = elementHost.getId();
			DataHostToolbar datahostToolbar = new DataHostToolbar(elementHost);
			this.addHostToolbar(datahostToolbar);
			

		} catch (Exception e) {

			return false;
		}

		return true;
	}
	/**
	 * 
	 * @param editor
	 */
	private void attivaToolbar(IHostEditor editor) {

		
		try {
		this.editorAttivo = editor;
		
		
		pdw.getEditToolBar().setButtonEnabled("Copy", true);
		pdw.getEditToolBar().setButtonEnabled(
			"Paste",
			true);
		pdw.getEditToolBar().setButtonEnabled("Del", true);
		pdw.getEditToolBar().setButtonEnabled("Cut", true);
		pdw.getEditToolBar().setButtonEnabled("Undo", true);
		pdw.getEditToolBar().setButtonEnabled("Redo", true);
		
		if (listaHostToolbar != null)
			for (int i = 0; i < listaHostToolbar.length; i++) {

				String[] listaEditor =
					((DataHostToolbar) listaHostToolbar[i]).getHostRequired();
				if (listaEditor != null)
					for (int j = 0; j < listaEditor.length; j++) {

						IHostEditor checkEditor =
							extensionPointEditor.getHostEditor(listaEditor[j]);
						if (checkEditor.equals(editor)) {
							DataHostToolbar currentToolbar = (DataHostToolbar) listaHostToolbar[i];
							JToolBar toolbar =(JToolBar)(currentToolbar.getHostReference());
							pdw.getToolBarPanel().add(toolbar);
							
							
							//alla prima attivazione della toolbar inseriamo i dati di default relativi ai bottoni.
							//in questo modo memorizziamo le eventuali personalizzazioni ai bottoni  implementate dal programmatore del plugin 
							if (currentToolbar.isFirstActivation()){
								currentToolbar.setFirstActivation(false);
								
								Component[] bottoni = toolbar.getComponents();
								for (int z = 0; z < bottoni.length; z++) {
									
									AbstractButton pulsante = (AbstractButton)bottoni[z];
									DataButton dataPulsante = new DataButton(pulsante);
									dataPulsante.setReleaseButtonBorder(pulsante.getBorder());
									currentToolbar.addButton(dataPulsante);
									
								}
							}
							
							///////////////////

						}

					}
			}
		pdw.getToolBarPanel().add(
				plugManager.getPlugDataWin().getZoomToolBar());
		pdw.getToolBarPanel().revalidate();
		pdw.getToolBarPanel().repaint();

		} catch (Exception e) {

			///PROBLEMI NELL'EXTENSION POINT - RICHIEDE HOST REQUIRED DI TIPO HOST EDITOR
		}
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param editor
	 */
	private void disattivaToolbar(IHostEditor editor) {

		if (listaHostToolbar != null)
			for (int i = 0; i < listaHostToolbar.length; i++) {

				String[] listaEditor = (listaHostToolbar[i]).getHostRequired();

				if (listaEditor != null)
					for (int j = 0; j < listaEditor.length; j++) {

						IHostEditor checkEditor =
							extensionPointEditor.getHostEditor(listaEditor[j]);
						if (checkEditor.equals(editor)) {

							JToolBar toolbar =
								(JToolBar) (listaHostToolbar[i])
									.getHostReference();
							pdw.getToolBarPanel().remove(toolbar);

							pdw.getToolBarPanel().remove(
								plugManager.getPlugDataWin().getZoomToolBar());

							// pdw.getToolBarPanel().remove(editToolbar);

							pdw.getToolBarPanel().revalidate();
							pdw.getToolBarPanel().repaint();

						}

					}
			}
	}

	/**
	 * 
	 *  Charmy Project -
	 * Setta a pressed il bottone passato come argomento
	 * IMP: contemporaneamente setta in posizione di rilascio l'ultimo 
	 * bottone premuto di tutte le toolbar presenti nell'editor attivo
	 *  @author ezio di nisio
	 *
	 */
	private void setButtonPressed(
		IHostToolbar toolbar,
		AbstractButton buttonPressed,
		int typeButton) {

		

		//settiamo a pressed il bottone premuto
		/*	if (doppioClick)
				buttonPressed.setBorder(
					BorderFactory.createBevelBorder(
						1,
						Color.LIGHT_GRAY,
						Color.GRAY));
			else*/
		if (typeButton == ButtonEvent.MOUSE_RELEASED_EVENT){
			
			if (listaHostToolbar != null)
				for (int i = 0; i < listaHostToolbar.length; i++) {
					
					DataHostToolbar currentHost = listaHostToolbar[i];
					if (currentHost.getHostReference().equals(toolbar)){
						
						DataButton[] buttons = currentHost.getButtons();
						for (int z = 0; z < buttons.length; z++) {
							
							if (buttons[z].getReference().equals(buttonPressed)){
								this.setAllButtonNoPressed();
								buttons[z].setPressed(true);
								buttonPressed.setBorder(BorderFactory.createBevelBorder(1));
								pdw.getToolBarPanel().revalidate();
								pdw.getToolBarPanel().repaint();
								return;
							}
							
						}						
					}				
					
				}
			
		}
			
		//((JToolBar) toolbar).revalidate();
	}

	/**
	 * 
	 *  Charmy Project-
	 * 
	 * setta in posizione di rilascio l'ultimo bottone premuto rispetto a tutte le  toolbar associate all'editor attualmente attivo
	 *  @author ezio di nisio
	 *
	 */

	public void setAllButtonNoPressed() {

		if (listaHostToolbar != null)
			for (int i = 0; i < listaHostToolbar.length; i++) {

				DataHostToolbar currentHost = listaHostToolbar[i];

				String[] listaEditorAssociati = currentHost.getHostRequired();

				if (listaEditorAssociati != null)
					for (int k = 0; k < listaEditorAssociati.length; k++) {

						IHostEditor editorCheck =
							extensionPointEditor.getHostEditor(
								listaEditorAssociati[k]);
						if (editorCheck.equals(editorAttivo)) {

							DataButton[] dataButtons = currentHost.getButtons();
							for (int z = 0; z < dataButtons.length; z++) {
								
								if (dataButtons[z].isPressed()){
									dataButtons[z].getReference().setBorder(dataButtons[z].getReleaseButtonBorder());
									dataButtons[z].setPressed(false);
									pdw.getToolBarPanel().revalidate();
									pdw.getToolBarPanel().repaint();
									return;
								}
									
							}

							
						}
					}
			}
		}

	public IHostToolbar getHostToolbar(String idHostToolbar) {

		if (listaHostToolbar != null)
			for (int i = 0; i < listaHostToolbar.length; i++) {
				DataHostToolbar ToolbarRegistrato = listaHostToolbar[i];

				if (ToolbarRegistrato.getId().compareTo(idHostToolbar) == 0) {

					return (IHostToolbar) ToolbarRegistrato.getHostReference();
				}

			}
		return null;
	}

	public DataHostToolbar getDataHostToolbarFor(IHostToolbar hostToolbar) {

		if (listaHostToolbar != null)
			for (int i = 0; i < listaHostToolbar.length; i++) {

				if (listaHostToolbar[i].getHostReference().equals(hostToolbar))
					return listaHostToolbar[i];

			}
		return null;
	}

	public AbstractButton addButton(
		IHostToolbar toolbar,
		String fileNameIcon,
		String strToolTipText) {

		if (listaHostToolbar != null)
			for (int i = 0; i < listaHostToolbar.length; i++) {

				DataHostToolbar currentHost = listaHostToolbar[i];
				IHostToolbar checkToolbar =
					(IHostToolbar) currentHost.getHostReference();

				if (checkToolbar.equals(toolbar)) {
					//inseriamo il bottone
					JToolBar toolbarRecast = (JToolBar) checkToolbar;
					JButton button =
						createToolbarButton(fileNameIcon, strToolTipText);
					toolbarRecast.add(button);

					return button;
					//button.addMouseListener(mouseListener);
				}
			}
		return null;
	}

	public JButton createToolbarButton(
		String fileNameIcon,
		String strToolTipText) {

		String mypathName = fileNameIcon;

		File myfile = new File(mypathName);
		if (myfile.exists()) {
			ImageIcon myicon = new ImageIcon(mypathName); //myurl);
			JButton LocalJButton = new JButton(myicon);
			LocalJButton.setMargin(new Insets(0, 0, 0, 0));
			LocalJButton.setToolTipText(strToolTipText);
			return LocalJButton;
		} else {
			JOptionPane.showMessageDialog(
				null,
				"Unable to load the buttons icons.:\n " + mypathName,
				"Information",
				JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}

	public JButton createToolbarButton(
		String fileNameIcon,
		String strToolTipText,
		int intMnemonic) {

		String mypathName = fileNameIcon;

		File myfile = new File(mypathName);
		if (myfile.exists()) {
			ImageIcon myicon = new ImageIcon(mypathName); //myurl);
			JButton LocalJButton = new JButton(myicon);
			LocalJButton.setMargin(new Insets(0, 0, 0, 0));
			LocalJButton.setToolTipText(strToolTipText);
			LocalJButton.setMnemonic(intMnemonic);
			return LocalJButton;
		} else {
			JOptionPane.showMessageDialog(
				null,
				"Unable to load the buttons icons.:\n " + mypathName,
				"Information",
				JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see extensionpoint.event.ISubscriberListener#notify(extensionpoint.event.EventBase)
	 */
	public void notify(EventBase event) {

		// gestione pulsante toolbar premuto, rilascio del pulsante precedentemente premuto

		if (event.getEventID().compareTo(ButtonEvent.class.getName()) == 0) {
			ButtonEvent buttonEvent = (ButtonEvent) event;

			if (buttonEvent.getTypeMouseEvent()
				== ButtonEvent.ACTION_PERFORMED_EVENT)
				this.setButtonPressed(
					buttonEvent.getToolbarOwner(),
					(AbstractButton) buttonEvent.getMouseEvent().getSource(),
					ButtonEvent.ACTION_PERFORMED_EVENT);

			else if (
				buttonEvent.getTypeMouseEvent()
					== ButtonEvent.MOUSE_RELEASED_EVENT)
				this.setButtonPressed(
					buttonEvent.getToolbarOwner(),
					(AbstractButton) buttonEvent.getMouseEvent().getSource(),
					ButtonEvent.MOUSE_RELEASED_EVENT);

		}

		// gestione evento pannello editor cambiato, vengono attivate le toolbar associate all'editor
		if (event.getEventID().compareTo(ActiveEditor.class.getName()) == 0) {
			ActiveEditor eventEditorActive = (ActiveEditor) event;

			if (eventEditorActive.getActiveEditor() != null) {
				this.attivaToolbar(eventEditorActive.getActiveEditor());
			}
		}

		if (event.getEventID().compareTo(InactiveEditor.class.getName())
			== 0) {
			InactiveEditor eventEditorActive = (InactiveEditor) event;

			if (eventEditorActive.getInactiveEditor() != null) {
				this.disattivaToolbar(eventEditorActive.getInactiveEditor());
			}
		}

	}

	private void addHostToolbar(DataHostToolbar hostToolbar) {

		DataHostToolbar[] list;

		if (this.listaHostToolbar == null) {
			list = new DataHostToolbar[1];
		} else {
			list = new DataHostToolbar[this.listaHostToolbar.length + 1];
			System.arraycopy(
				listaHostToolbar,
				0,
				list,
				0,
				listaHostToolbar.length);
		}

		list[list.length - 1] = hostToolbar;
		this.listaHostToolbar = list;

	}

	public EventService getEventService() {

		return this.plugManager.getEventService();
	}

	/**
	 * restituisce tutti gli editor associati alla toolbar
	 * @param hostToolbar
	 * @return
	 */
	public IHostEditor[] getEditorsAssociated(IHostToolbar hostToolbar) {

		IHostEditor[] result = null;
		if (listaHostToolbar != null)
			for (int i = 0; i < listaHostToolbar.length; i++) {

				if (listaHostToolbar[i]
					.getHostReference()
					.equals(hostToolbar)) {

					String[] listaEditor =
						listaHostToolbar[i].getHostRequired();

					if (listaEditor != null) {

						result = new IHostEditor[listaEditor.length];
						for (int j = 0; j < listaEditor.length; j++) {

							result[j] =
								this.extensionPointEditor.getHostEditor(
									listaEditor[j]);

						}
					}
				}
			}
		return result;
	}

	/**
	 * restituisce l'editor associato alla toolbar. 
	 * Se ci sono più editor associati ne restituisce il primo 
	 * @param hostToolbar
	 * @return
	 */
	public IHostEditor getEditorAssociated(IHostToolbar hostToolbar) {

		IHostEditor result = null;

		if (listaHostToolbar != null)
			for (int i = 0; i < listaHostToolbar.length; i++) {

				if (listaHostToolbar[i]
					.getHostReference()
					.equals(hostToolbar)) {

					String[] listaEditor =
						listaHostToolbar[i].getHostRequired();

					if (listaEditor != null) {

						result =
							this.extensionPointEditor.getHostEditor(
								listaEditor[0]);

					}
				}
			}
		return result;
	}

}
