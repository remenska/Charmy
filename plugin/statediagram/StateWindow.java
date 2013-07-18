/*   Charmy (CHecking Architectural Model consistencY)
 *   Copyright (C) 2004 Patrizio Pelliccione <pellicci@di.univaq.it>,
 *   Henry Muccini <muccini@di.univaq.it>, Paola Inverardi <inverard@di.univaq.it>. 
 *   Computer Science Department, University of L'Aquila. SEA Group. 
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
    

package plugin.statediagram;


import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.internal.extensionpoint.DeclaredHost;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.ui.simpleinterface.ZoomGraphInterface;
import core.resources.ui.WithGraphWindow;

import plugin.topologydiagram.data.ElementoProcesso;
import plugin.topologydiagram.data.ListaProcesso;
import plugin.topologydiagram.eventi.listaprocesso.AddEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.RemoveEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.UpdateEPEvento;
import plugin.topologydiagram.pluglistener.IListaProcessoListener;
import plugin.topologydiagram.utility.listaProcesso.JListaProcessoEvent;
import plugin.topologydiagram.utility.listaProcesso.JListaProcessoInfo;
import plugin.topologydiagram.utility.listaProcesso.JListaProcessoPanel;
import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.graph.ElementoMessaggio;
import plugin.statediagram.data.ListaDP;
import plugin.statediagram.data.ListaMessaggio;
import plugin.statediagram.data.ListaStato;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.data.PlugData;
import plugin.statediagram.dialog.FinestraNuovoThread;
import plugin.statediagram.general.edcp.CopyCutPasteThread;
import plugin.statediagram.toolbar.StateToolBar;

import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.plugin.file.xschema.SimpleValue;
import plugin.statediagram.file.Tag;
/** Classe per creare e gestire la creazione degli State Diagram. */

public class StateWindow
	extends WithGraphWindow
	implements IMainTabPanel, IListaProcessoListener {

	/** Elenco dei nomi dei processi (non dummy).
	 *  pannello sinistro della finestra */
	private JListaProcessoPanel ProcessList;

	/** Pannello contenente la lista 
		dei nomi dei processi (non dummy). */
	private JScrollPane StateLeftPanel;

	/** Pannello contenente gli editor per 
		la creazione degli State Diagram. */
	private JTabbedPane StateRightPanel;

	/** Pannello principale. */
	private JSplitPane StateSplitPanel;

	/** Riferimento alla StateToolBar. */
	private StateToolBar rifStateToolBar;

	/** Lista dei processi non dummy 
		utilizzati nel S_A_ Topology Diagram. */
	private LinkedList ListaProcessi;

	/** Riferimento all'editor sul 
		quale l'utente sta disegnando. */
	private ThreadEditor currentThreadEditor;

	/** Riferimento alla lista dei thread relativa
		al processo selezionato. Tale lista fornisce 
		la dinamica del processo. */
	private ListaThread istanzaListaThread; //l'elemento del processo 

	/** Memorizza il nome del processo selezionato. */
	private JListaProcessoInfo SelectedItem;

	/** Memorizza l'indice (con riferimento allo
		'StateRightPanel') dell'editor in uso. */
	private int ThreadTabIndex = 0;

	/** Riferimento all'oggetto responsabile delle
		azioni associate all'evento di 'TabChange'
		(con riferimento allo 'StateRightPanel').
		Utile per rimuovere il legame tra l'oggetto
		che genera l'evento e quello che ascolta.
		Se il legame non viene rimosso e ripristinato
		ogni volta, si verifica errore. */
	private ThreadTabChange TmpTabChange;


	private CopyCutPasteThread ccpThread;

	/**
	 * riferimento alla plugData
	 */
	private PlugData plugData;
	private plugin.topologydiagram.data.PlugData plugDataTopology;
	private PlugDataWin plugDataWin;


	/**
	 * lista degli editor
	 */
	private ListaThreadEditor listaThreadEditor;

	public ThreadEditor getCurrentThreadEditor() {
		return currentThreadEditor;
	}

	/** Costruttore. */
	public StateWindow() {
		super();		
	}

	public PlugDataManager getPlugDataManager(){
		return plugData.getPlugDataManager();
	}
	/**
	 * funzione di inizializzazione della classe, viene chiamata dopo setDati
	 *
	 */
	public void init() {
		BuildStateSplitPanel();
		/** aggiunta del listener **/
		plugDataTopology=(plugin.topologydiagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.topology");
		if(plugDataTopology!=null)
			plugDataTopology.getListaProcesso().addListener(this);

		setBorder(BorderFactory.createEtchedBorder(Color.DARK_GRAY, Color.gray));
		setLayout(new BorderLayout());
		add("Center", StateSplitPanel);

		this.setToolBar(new StateToolBar(this,plugData));
		plugDataWin.getMainPanel().addTab("State Editor", this);
		listaThreadEditor = new ListaThreadEditor(plugDataWin, plugData, this.rifStateToolBar);	
		ccpThread = new CopyCutPasteThread(plugData);	
		
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#initHost()
	 */
	public DeclaredHost[] initHost() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setImgArea() {
		currentThreadEditor.setEditorStatus(9, 8, false);
		currentThreadEditor.setLocalClassStatus(
			"Click on the window to select the printing area.");
	}

	/** Metodo richiamato dal costruttore per la realizzazione dell'oggetto. */
	private void BuildStateSplitPanel() {
		StateSplitPanel = new JSplitPane();

		/**
		 * lista dei processi sia inseriti dall'utente 
		 * da plugData.getProcess
		 * plugData.getListaProcesso() e plugData.getListaProcessiUtente();
		 * va modificato per permettere la gestione mediante plug
		 */

		ProcessList = new JListaProcessoPanel(plugData);
		ProcessList.setSelectionMode(0);

		ProcessList.setJListaProcessoEvent(new ProcessListAction());

		StateLeftPanel = new JScrollPane(ProcessList, 20, 30);

		StateRightPanel = new JTabbedPane();
		TmpTabChange = new ThreadTabChange();
		StateRightPanel.addChangeListener(TmpTabChange);
		StateSplitPanel.setDividerLocation(200);
		StateSplitPanel.setLeftComponent(StateLeftPanel);
		StateSplitPanel.setRightComponent(StateRightPanel);
	}

	/**
	 *Aggiorna il pannello di destra caricando i relativi thread  
	 *utile nelle operazioni di undo-redo e selezione del processo
	 *nel pannello di sinistra
	 *@param id del processo correntemente selezionato
	 */

	private void refreshPanel(long idProcesso) {
		ThreadEditor tmpThreadEditor;
		int num;
		if (idProcesso > 0) {
			//conservo la pagina attiva
			if(istanzaListaThread != null){
				listaThreadEditor.put(new Long(istanzaListaThread.getIdProcesso()),
				            new Integer(StateRightPanel.getSelectedIndex()));
			}
			StateRightPanel.removeAll();
			StateRightPanel.removeChangeListener(TmpTabChange);
			istanzaListaThread =
				plugData.getListaDP().getListaThread(idProcesso);
			if (istanzaListaThread != null) {
				if (istanzaListaThread.isEmpty()) {
					currentThreadEditor = null;
					rifStateToolBar.setNoPressed();
					rifStateToolBar.setEnableThreadButtons(false);
					rifStateToolBar.setEnableStateButtons(false);
				} 
				else {
					ThreadEditor[] te = listaThreadEditor.get(idProcesso);
					if (te != null) {
						rifStateToolBar.setEnableThreadButtons(true);
						rifStateToolBar.setEnableStateButtons(true);
						if(currentThreadEditor!=null)
							if(currentThreadEditor.getThreadElement()!=null)
								if(currentThreadEditor.getThreadElement().getListaStato()!=null)
									if(currentThreadEditor.getThreadElement().getListaStato().startExist())
										rifStateToolBar.setEnabledStartButton(false);
						for (int i = 0;
							i < te.length;
							i++) { // Aggiungo tanti TAB (editor) quanti sono gli elementi della lista.

							tmpThreadEditor = te[i];
							StateRightPanel.addTab(
								tmpThreadEditor.getName(),
								new JScrollPane(tmpThreadEditor));
							StateRightPanel.setBackgroundAt(i, Color.gray);
						}

						//	Ripristino dell'ascoltatore degli eventi di TabChange.	
						TmpTabChange = new ThreadTabChange();
						StateRightPanel.addChangeListener(TmpTabChange);
						if (istanzaListaThread.size() > 0) {
							//ricerca Ultimo tab Attivo
							num = listaThreadEditor.get(new Long(istanzaListaThread.getIdProcesso()));
							if(num <0){ // di default metto a zero
								num = 0;
							}
							if(te.length>num){
								currentThreadEditor = (ThreadEditor) te[num];
								ThreadTabIndex = num;
								StateRightPanel.setSelectedIndex(num);

								StateRightPanel.setBackgroundAt(
									ThreadTabIndex,
									Color.GRAY);
							}		
													
							plugDataWin.getStatusBar().setText(
								"Process: "
									+ SelectedItem.toString()
									+ ".  "
									+ StateRightPanel.getTitleAt(0)
									+ ".");
						}
					}
				}
			} else {
				currentThreadEditor = null;
			}
			
			return;
		}

	}

	/** Classe nidificata per gestire gli eventi di "selezione" dalla
		JListaProcesso posta nella zona sinistra della StateWindow,
		cio? dalla lista con i nomi dei processi per i quali ? possibile
		disegnare uno o pi? State Diagram. */
	class ProcessListAction implements JListaProcessoEvent {
		
		public void change(JListaProcessoInfo jlpi) {
			SelectedItem = jlpi;
			refreshPanel(
				jlpi.getElementoProcesso().getId());
			
			int i = StateRightPanel.getSelectedIndex();
			if (i < 0) {
				return;
			} else {
				ThreadEditor[] te =
					listaThreadEditor.get(
						jlpi.getElementoProcesso().getId());
				currentThreadEditor = (ThreadEditor) te[i];
				rifStateToolBar.setNoPressed();
				if(currentThreadEditor.getEditorStatus()==1)
					rifStateToolBar.setPressed(1);
				if(currentThreadEditor.getEditorStatus()==3)
					rifStateToolBar.setPressed(4);
				if(currentThreadEditor.getEditorStatus()==2){
					int tipoMsg=currentThreadEditor.getTipoMessaggio();
					if(tipoMsg==1)
						rifStateToolBar.setPressed(2);
					else	
						if(tipoMsg==2)
							rifStateToolBar.setPressed(3);
				}
				if(currentThreadEditor.getThreadElement().getListaStato().startExist()){
					rifStateToolBar.setEnabledStartButton(false);
				}
				else
					rifStateToolBar.setEnabledStartButton(true);
			}
				
		}		
	}		
		

	/** Classe nidificata per gestire gli eventi di "TabChange" relativi
		al JTabbedPane inserito nella zona destra della StateWindow. */
	class ThreadTabChange implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			String str;
			int i = StateRightPanel.getSelectedIndex();
			if (i == ThreadTabIndex || i < 0) {
				return;
			} else {
				//rifStateToolBar.setNoPressed();
				
				SelectedItem =
					(JListaProcessoInfo) ProcessList.getSelectedValue();
				ThreadEditor[] te =
					listaThreadEditor.get(
						SelectedItem.getElementoProcesso().getId());
				currentThreadEditor = (ThreadEditor) te[i];

				rifStateToolBar.setNoPressed();
				if(currentThreadEditor.getEditorStatus()==1)
					rifStateToolBar.setPressed(1);
				if(currentThreadEditor.getEditorStatus()==3)
					rifStateToolBar.setPressed(4);
				if(currentThreadEditor.getEditorStatus()==2)
					if(currentThreadEditor.getTipoMessaggio()==1)
						rifStateToolBar.setPressed(2);
					else	
					  	if(currentThreadEditor.getTipoMessaggio()==2)
							rifStateToolBar.setPressed(3);
				if(currentThreadEditor.getThreadElement().getListaStato().startExist()){
					rifStateToolBar.setEnabledStartButton(false);
				}
				else
					rifStateToolBar.setEnabledStartButton(true);
				StateRightPanel.setBackgroundAt(i, Color.white);
				//StateRightPanel.setBackgroundAt(ThreadTabIndex, Color.lightGray);
				ThreadTabIndex = i;
				str = "Process: " + SelectedItem.toString() + ".  ";
				str = str + StateRightPanel.getTitleAt(i) + ".";
				plugDataWin.getStatusBar().setText(str);
				setToolStatus();
				
				return;
			}
		}
	}

	
	
	/** funzione inserita per recuperare lo stato del 
	 * Thread e per riportarlo nella toolbar 
	 * */

	private void setToolStatus()
	{
		switch (currentThreadEditor.getEditorStatus())
		{
			case 1:
				if (currentThreadEditor.getTipoStato()==0)
					rifStateToolBar.setPressed(0);
				else
					rifStateToolBar.setPressed(1);
				break;
			case 2:
				if (currentThreadEditor.getTipoMessaggio()==1)
				{
					rifStateToolBar.setPressed(2);
				}
				else 
					if (currentThreadEditor.getTipoMessaggio()==2)
					{
						rifStateToolBar.setPressed(3);
					}
				break;
			case 3:
				rifStateToolBar.setPressed(4);
				break;
		}
	}
	

	/** Zoom sull'asse X. */
	public void incScaleX() {
		if ((currentThreadEditor != null)
			&& (ProcessList.getSelectedIndex() >= 0)) {
			currentThreadEditor.incScaleX();
		} else {
			plugDataWin.getStatusBar().setText(
				"<Stretch Horizontal> not succeeded!!  State Diagram not selected!!");
		}
	}

	/** Zoom negativo sull'asse X. */
	public void decScaleX() {
		if ((currentThreadEditor != null)
			&& (ProcessList.getSelectedIndex() >= 0)) {
			currentThreadEditor.decScaleX();
		} else {
			plugDataWin.getStatusBar().setText(
				"<Compress Horizontal> not succeeded!!  State Diagram not selected!!");
		}
	}

	/** Zoom sull'asse Y. */
	public void incScaleY() {
		if ((currentThreadEditor != null)
			&& (ProcessList.getSelectedIndex() >= 0)) {
			currentThreadEditor.incScaleY();
		} else {
			plugDataWin.getStatusBar().setText(
				"<Stretch Vertical> not succeeded!!  State Diagram not selected!!");
		}
	}

	/** Zoom negativo sull'asse Y. */
	public void decScaleY() {
		if ((currentThreadEditor != null)
			&& (ProcessList.getSelectedIndex() >= 0)) {
			currentThreadEditor.decScaleY();
		} else {
			plugDataWin.getStatusBar().setText(
				"<Compress Vertical> not succeeded!!  State Diagram not selected!!");
		}
	}

	/** Imposta la scala di visualizzazione al 100%. */
	public void resetScale() {
		if ((currentThreadEditor != null)
			&& (ProcessList.getSelectedIndex() >= 0)) {
			currentThreadEditor.resetScale();
		} else {
			plugDataWin.getStatusBar().setText(
				"<Zoom Reset> not succeeded!!  State Diagram not selected!!");
		}
	}

	/** Operazione di copy. */
	public void opCopy() {
		if ((currentThreadEditor != null)
			&& (ProcessList.getSelectedIndex() >= 0)) {
			ccpThread.copy(currentThreadEditor.getThreadElement());
			repaint();
		} else {
			plugDataWin.getStatusBar().setText(
				"<Copy> not succeeded!!  State Diagram not selected!!");
		}
	}

	/** Operazione di paste. */
	public void opPaste() {
		if ((currentThreadEditor != null)
			&& (ProcessList.getSelectedIndex() >= 0)) {
			ccpThread.paste(currentThreadEditor.getThreadElement());
			repaint();
			
		} else {
			plugDataWin.getStatusBar().setText(
				"<Paste> not succeeded!!  State Diagram not selected!!");
		}

	}

	/** Operazione di cut. */
	public void opCut() {
		if ((currentThreadEditor != null)
			&& (ProcessList.getSelectedIndex() >= 0)) {
			ccpThread.cut(currentThreadEditor.getThreadElement());
			repaint();
		} else {
			plugDataWin.getStatusBar().setText(
				"<Cut> not succeeded!!  State Diagram not selected!!");
		}
	}

	/** Operazione di cancellazione. */
	public void opDel() {
		if ((currentThreadEditor != null)
			&& (ProcessList.getSelectedIndex() >= 0)) {
			currentThreadEditor.opDel();
		} else {
			plugDataWin.getStatusBar().setText(
				"<Delete> not succeeded!!  State Diagram not selected!!");
		}
		repaint();
	}

	/** Operazione di undo. */
	public void opUndo() {
		
			if ((currentThreadEditor != null) 
					&& (ProcessList.getSelectedIndex() >= 0)) {
				currentThreadEditor.opUndo();
			} else {
				plugDataWin.getStatusBar().setText(
					"<Undo> not succeeded!!  State Diagram not selected!!");
			}
			repaint();
		}		



	/** Operazione di redo. */
	public void opRedo() {
		
		
		if ((currentThreadEditor != null) && (ProcessList.getSelectedIndex() >= 0)) {
			currentThreadEditor.opRedo();
			currentThreadEditor.repaint();
			
		} else {
			plugDataWin.getStatusBar().setText(
				"<Redo> not succeeded!!  State Diagram not selected!!");
		}
		repaint();
	}

	public StateToolBar getStateToolBar(){
		return rifStateToolBar;
	}

	/** Metodo per creare un nuovo thread. */
	public void opNewThread() {
		int k = ProcessList.getSelectedIndex();

		if (k >= 0) {
			FinestraNuovoThread pp = new FinestraNuovoThread(null, this, true);
		} else {
			plugDataWin.getStatusBar().setText(
				"<New Thread> not succeeded!!  Process not selected!!");
			String str;
			str =  	"You must create at least one process\n" +
					"before the creation of a new Thread";	
			JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE);   
				
		}
	}

	/** Metodo per salvare il thread corrente
		come immagine jpeg su file. */
	public void opImg() {
		if ((currentThreadEditor != null)
			&& (ProcessList.getSelectedIndex() >= 0)) {
			currentThreadEditor.opImg();
		} else {
			plugDataWin.getStatusBar().setText(
				"<Save JPEG Image> not succeeded!!  State Diagram not selected!!");
		}
	}

	/** Metodo per aggiungere un nuovo State Diagram al processo selezionato. */
	public void addThread(String nThread) {
		ListaThread tmpListaThread = null;
		ThreadEditor tmpThreadEditor;

		int j = 0;
		boolean ctrl = true;
		int k = ProcessList.getSelectedIndex();

		if (k >= 0) {
			SelectedItem = (JListaProcessoInfo) ProcessList.getSelectedValue();
			//	Ricerca della lista di thread associata con il processo selezionato.
			//	Forse quest'operazione potrebbe essere eliminata usando 'istanzaListaThread'.		

			tmpListaThread =
				plugData.getListaDP().getListaThread(
					SelectedItem.getElementoProcesso().getId());

			if (tmpListaThread != null) {
				tmpThreadEditor =
					new ThreadEditor(
						nThread,
						plugDataWin.getStatusBar(),
						tmpListaThread,
						plugData);
				tmpThreadEditor.setToolBar(
					plugDataWin.getEditToolBar(),
					rifStateToolBar);
				listaThreadEditor.add(tmpThreadEditor);
			}
			ProcessList.clearSelection();
			ProcessList.setSelectedIndex(k);
		/*
		 * attiva il componente appena aggiunto
		 */
	     	if(StateRightPanel.getSelectedIndex() > -1){
		       StateRightPanel.setSelectedIndex(
		                      StateRightPanel.getTabCount()-1);
		    }
			
		} else {
			plugDataWin.getStatusBar().setText("Process not selected!!");
		}
	}

	/** Metodo per consentire all'utente di scegliere 
		un nuovo nome per il thread selezionato. */
	public void opRenameThread() {
		if ((currentThreadEditor != null)
			&& (ProcessList.getSelectedIndex() >= 0)) {
			FinestraNuovoThread pp = new FinestraNuovoThread(null, this, false);
		} else {
			plugDataWin.getStatusBar().setText(
				"<Thread Properties> not succeeded!!  State Diagram not selected!!");
		}
	}

	/** Metodo per rimuovere lo State Diagram selezionato. */
	public void delThread() {
		int k = ProcessList.getSelectedIndex();

		if (k >= 0) {
			if ((istanzaListaThread != null)
				&& (currentThreadEditor != null)) {
				listaThreadEditor.remove(currentThreadEditor);
			} else {
				plugDataWin.getStatusBar().setText(
					"<Delete Thread> not succeeded!!  State Diagram not selected!!");
			}
			ProcessList.clearSelection();
			ProcessList.setSelectedIndex(k);
		} else {
			plugDataWin.getStatusBar().setText(
				"<Delete Thread> not succeeded!!  Process not selected!!");
		}
	}

	/** Metodo per impostare lo stato dell'editor usato
		per il disegno del S_A_ Topology Diagram. */
	public void setWindowStatus(int j, int tipoprc, boolean ctrl) {
		if (currentThreadEditor != null) {
			currentThreadEditor.setEditorStatus(j, tipoprc, ctrl);
		} else {
			rifStateToolBar.setNoPressed();
			plugDataWin.getStatusBar().setText(
				"<Insertion> not available!!  State Diagram not selected!!");
		}
	}

	/** Metodo per impostare il riferimento alla TopologyToolBar. */
	public void setToolBar(StateToolBar ctbar) {
		plugDataWin.getEditToolBar().setButtonEnabled("Copy", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Paste", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Del", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Cut", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Undo", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Redo", true);
		rifStateToolBar = ctbar;
	}

	/** Metodo per abilitare i pulsanti della EditToolBar. */
	public void setButtonEnabled() {
		plugDataWin.getEditToolBar().setButtonEnabled("Copy", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Paste", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Del", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Cut", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Undo", true);
		plugDataWin.getEditToolBar().setButtonEnabled("Redo", true);
	}

	/** Restituisce il nome del thread corrente. */
	public String getNameCurrentThreadEditor() {
		if (currentThreadEditor != null) {
			return currentThreadEditor.getName();
		} else {
			return "";
		}
	}

	/** Metodo per modificare il nome del thread corrente. */
	public void setNameCurrentThreadEditor(String strName) {
		int k = ProcessList.getSelectedIndex();

		if (currentThreadEditor != null) {
			currentThreadEditor.setName(strName);
		}
		if (k >= 0) {
			ProcessList.clearSelection();
			ProcessList.setSelectedIndex(k);
		}
	}

	/** Metodo per creare una copia del thread corrente. */
	public void opCopyThread() {
		int k = ProcessList.getSelectedIndex();
		boolean ctrl = true;
		String nomeThread;
		String tmpnomeThread;
		int j = 0;
		int num = 1;
		ListaThread tmpListaThread;

		if ((k >= 0) && (currentThreadEditor != null)) {
			SelectedItem = (JListaProcessoInfo) ProcessList.getSelectedValue();
			//	Ricerca della lista di thread associata con il processo selezionato.
			//	Forse quest'operazione potrebbe essere eliminata usando 'istanzaListaThread'.		
			tmpListaThread =
				plugData.getListaDP().getListaThread(
					SelectedItem.getElementoProcesso().getId());

			if (tmpListaThread != null) {
				nomeThread = "cp_" + currentThreadEditor.getName();
				tmpnomeThread = nomeThread;
				while (tmpListaThread.nomeGiaPresente(tmpnomeThread)) {
					num++;
					tmpnomeThread = nomeThread + "_" + Integer.toString(num);
				}
				nomeThread = tmpnomeThread;
				ThreadEditor copiaTE =
					new ThreadEditor(
						nomeThread,
						plugDataWin.getStatusBar(),
						tmpListaThread,
						plugData);
				copiaTE.setCEProperties(
					currentThreadEditor.getCEWidth(),
					currentThreadEditor.getCEHeight(),
					currentThreadEditor.getCEColor());

				copiaTE.setStateToolBar(currentThreadEditor.getStateToolBar());
				ListaStato copiaLS =
					(currentThreadEditor.getListaStato()).cloneListaStato();
				copiaTE.setListaStato(copiaLS);
				ListaMessaggio copiaLM =
					(
						currentThreadEditor
							.getListaMessaggio())
							.cloneListaMessaggio(
						copiaLS);
				copiaTE.setListaMessaggio(copiaLM);

				listaThreadEditor.add(copiaTE);
			}
			ProcessList.clearSelection();
			ProcessList.setSelectedIndex(k);
		} else {
			plugDataWin.getStatusBar().setText(
				"<Copy Thread> not succeeded!!  State Diagram not selected!!");
		}
	}

	/** Restituisce un array contentente i
		nomi di tutti i processi della lista. */
	public Object[] getProcessStringArray() {
		return ProcessList.getStringArray();
	}

	/** Restituisce l'indice del processo selezionato. */
	public int getSelectedProcessIndex() {
		return ProcessList.getSelectedIndex();
	}

	/** Metodo per selezionare un certo processo 
		nella lista alla sinistra del pannello. */
	public void setSelectedProcess(int k) {
		ProcessList.clearSelection();
		ProcessList.setSelectedIndex(k);
	}

	/** Metodo per spostare uno State Diagram costruito
		per un certo processo sotto un altro processo. */
	/**
	 * l'interfaccia rimane uguale per evitare di dover riscrivere
	 * tutte le chiamate alla funzione
	 * @author Michele Stoduto
	 * da quello che si capisce
	 * @param k indica il nuovo indice della JListaProcesso
	 * @param strName 
	 */
	public void moveThread(int k, String strName) {
		int j = ProcessList.getSelectedIndex();
		ThreadEditor localThreadEditor = currentThreadEditor;
		if (j >= 0) {
			ProcessList.setSelectedIndex(k);
			SelectedItem = (JListaProcessoInfo) ProcessList.getSelectedValue();
			plugData.getListaDP().moveThreadTo(
				localThreadEditor.getThreadElement(),
				SelectedItem.getElementoProcesso().getId());
			ProcessList.clearSelection();
			ProcessList.setSelectedIndex(k);
		} else {
			plugDataWin.getStatusBar().setText(
				"<Move Thread> not succeeded!!  Process not selected!!");
		}

	}

	/** Metodo per aggiungere un processo 
		non presente nel S_A_ Topology Diagram. */
	public void addUserProcess() {
		
		if(plugDataTopology!=null)
			plugDataTopology.getListaProcesso().addGenerico();
	}

	/** Metodo per eliminare un processo 
		non presente nel S_A_ Topology Diagram. */

	public void delUserProcess() {
		int i = 0;
		long idProcesso = 0;
		ListaThread tmpListaThread = null;
		ElementoProcesso tmpElementoProcesso = null;

		//	Recupero del nome del processo.
		SelectedItem = (JListaProcessoInfo) ProcessList.getSelectedValue();
		//	Ricerca della lista di thread associata con il processo selezionato.		
		idProcesso = SelectedItem.getElementoProcesso().getId();

		tmpListaThread = plugData.getListaDP().getListaThread(idProcesso);
		//	Rimozione della lista di thread ed altre operazioni.
		if (tmpListaThread != null) {
			//	Rimozione della lista di thread.
			plugData.getListaDP().remove(tmpListaThread);
			if(plugDataTopology!=null)
				plugDataTopology.getListaProcesso().removeElementById(idProcesso);
		}
		//	Selezione del primo processo dell'elenco.
		ProcessList.clearSelection();
		if (ProcessList.processNumber() > 0) {
			ProcessList.setSelectedIndex(0);
		} else {
			//	Eliminazione di tutti i thread (State Diagram).
			StateRightPanel.removeAll();
		}
	}


	/** Restituisce la lista rappresentate la dinamica di tutti
		i processi_ Ogni elemento della lista ? costituito da un
		insieme di thread caratterizzanti la dinamica del processo. */
	public ListaDP getDinamicaTuttiProcessi() {
		return plugData.getListaDP();
	}

	/** Restituisce la lista degli stati del corrente State Diagram. */
	public ListaStato getListaStatoCorrente() {
		return currentThreadEditor.getListaStato();
	}

	/** Restituisce la lista dei thread relativi al processo indicato. */
	public ListaThread getListaThread(String nomeProcesso) {
		return plugData.getListaDP().getListaThread(nomeProcesso);
	}

	/** Restituisce il nome di un canale se ne esiste almeno uno, altrimenti null. */
	public String getAnyNameChannel() {
		return plugData.getListaDP().getAnyNameChannel();
	}


	public void resetForNewFile() {
		
		ProcessList.removeList();
		while (StateRightPanel.getTabCount() > 0) {
			StateRightPanel.remove(0);
		}
		if (ListaProcessi != null) {
			while (ListaProcessi.size() > 0) {
				ListaProcessi.removeFirst();
			}
		}
		currentThreadEditor = null;
		if (istanzaListaThread != null) {
			while (istanzaListaThread.size() > 0) {
				istanzaListaThread.removeFirst();
			}
		}
		while (plugData.getListaDP().size() > 0) {
			plugData.getListaDP().removeFirst();
		}
		ThreadTabIndex = 0;
		if(plugDataTopology!=null)
			plugDataTopology.getListaProcesso().removeAll();
		rifStateToolBar.setNoPressed();
		plugDataWin.getStatusBar().setText("State ready.");
	}

	public void stateActive() {
		setButtonEnabled();
		plugDataWin.getEditToolBar().updateRifGraphWindow(this);
		plugDataWin.getZoomToolBar().updateRifGraphWindow(this);
		plugDataWin.getToolBarPanel().add(rifStateToolBar);
		plugDataWin.getToolBarPanel().add(plugDataWin.getZoomToolBar());
		plugDataWin.getStatusBar().setText("State ready.");
		this.StateRightPanel.setSelectedIndex(ThreadTabIndex);
	}

	public void stateInactive() {
		ThreadTabIndex = this.StateRightPanel.getSelectedIndex();
		plugDataWin.getToolBarPanel().remove(rifStateToolBar);
		plugDataWin.getToolBarPanel().remove(plugDataWin.getZoomToolBar());
	}

	/** Metodo per mantenere la lista DinamicaTuttiProcessi aggiornata in modo 
		coerente con i processi introdotti nel S_A_ Topology Diagram_
		E' stata implementata anche l'operazione di eliminazione 
		dei processi cancellati dal S_A_ Topology Diagram. */
	public void setListaProcessi(LinkedList lproc) {
		ElementoProcesso tmpProcesso;
		ListaThread tmpListaThread;
		long nProcesso;
		Integer IntegerProcesso;
		String tmpString = null;
		boolean ctrl;
		int j;

		ListaProcessi = lproc;
		for (int i = 0; i < ListaProcessi.size(); i++) {
			tmpProcesso = (ElementoProcesso) ListaProcessi.get(i);
			nProcesso = tmpProcesso.getId();
			j = 0;
			ctrl = true;

			//	Ogni processo deve avere associata una lista di thread.
			while ((ctrl) && j < plugData.getListaDP().size()) {
				tmpListaThread = (ListaThread) plugData.getListaDP().get(j);
				if (nProcesso == tmpListaThread.getIdProcesso()) {
					// Trovata la lista di thread associata con il processo.
					ctrl = false;
					tmpListaThread.setNameProcesso(tmpProcesso.getName());
				}
				j++;
			}

			if (ctrl) { //	Non esiste alcuna lista di thread associata con il processo.
				plugData.getListaDP().add(
					new ListaThread(
						nProcesso,
						tmpProcesso.getName(),
						plugData));
			} 			
		}
		//	Aggiorno la DinamicaTuttiProcessi cancellando le liste di thread
		//	dei processi eliminati dal S_A_ Topology Diagram.
		for (int i = 0; i < plugData.getListaDP().size(); i++) {
			tmpListaThread = (ListaThread) plugData.getListaDP().get(i);
			nProcesso = tmpListaThread.getIdProcesso();
			ctrl = true;
			j = 0;
			//	Verifico se il processo corrispondente alla lista di thread
			//	in esame appartiene ai processi disegnati nel S_A_ Topology Diagram.
			while ((ctrl) && (j < ListaProcessi.size())) {
				tmpProcesso = (ElementoProcesso) ListaProcessi.get(j);
				if (nProcesso == tmpProcesso.getId()) {
					//	Processo trovato!!
					ctrl = false;
				}
				j++;
			}
			j = 0;
			//	Se il processo non ? stato ancora trovato, verifico se appartiene
			//	alla lista dei processi introdotti dall'utente.			
			if(plugDataTopology!=null)
				while ((ctrl) && (j < plugDataTopology.getListaProcesso().size())) {
					tmpProcesso =
						(ElementoProcesso) plugDataTopology
							.getListaProcesso()
							.getElement(
							j);
					if (nProcesso == tmpProcesso.getId()) {
						//	Processo trovato!!
						ctrl = false;
					}
					j++;
				}
			if (ctrl) {
				//	Processo non trovato, eliminazione 
				//	della corrispondente lista di thread.
				plugData.getListaDP().remove(i);
				//	Eliminazione del processo dalla tabella delle corrispondenze.
			}
		}
		//	Rimozione degli editor presenti nello 'StateRightPanel'.
		StateRightPanel.removeAll();
		StateRightPanel.removeChangeListener(TmpTabChange);
	}

	public void processoAdded(AddEPEvento event) {
	}

	public void processoRemoved(RemoveEPEvento event) {
		if(istanzaListaThread != null){
		if (event.getElementoProcesso().getId()
			== this.istanzaListaThread.getIdProcesso()) {
			StateRightPanel.removeAll();
		}
		}
	}

	public void processoUpdate(UpdateEPEvento event) {
		//plugData.getListaDP().processoRefresh()
	}

	public void processoRefresh() {
		//System.out.println("processoRefresh");
	}

	public void setDati(PlugDataWin plugDtW) {
		plugDataWin = plugDtW;
		//init();
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getPlugDataWin()
	 */
	public PlugDataWin getPlugDataWin() {
		return plugDataWin;
	}

	/* (non Javadoc)
	 * @see simpleinterface.IMainTabPanel#getPlugData()
	 */
	public IPlugData getPlugData() {
		return plugData;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getEditMenu()
	 */
	public EditGraphInterface getEditMenu() {
		return this;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getZoomAction()
	 */
	public ZoomGraphInterface getZoomAction() {
		return this;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getDati()
	 */
	public Object[] getDati() {
		return null;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#newPlugData(data.PlugDataManager)
	 */
	public IPlugData newPlugData(PlugDataManager pm) {
		plugData=new PlugData(pm);
		return plugData;
	}

	
}

