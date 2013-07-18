

/*Charmy (CHecking Architectural Model consistencY)
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
    
package plugin.topologychannels.utility.listaProcesso;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import core.internal.runtime.data.IPlugData;

import plugin.topologychannels.data.ElementoProcesso;
import plugin.topologychannels.data.ListaProcesso;
import plugin.topologychannels.data.PlugData;
import plugin.topologychannels.dialog.FinestraElementoBoxTesto;

import plugin.topologychannels.pluglistener.IListaProcessoListener;
import plugin.topologychannels.utility.listaProcesso.action.ListaProcessoInterface;
//import dialog.FinestraUserProcessProperties;
import plugin.topologychannels.eventi.listaprocesso.AddEPEvento;
import plugin.topologychannels.eventi.listaprocesso.RemoveEPEvento;
import plugin.topologychannels.eventi.listaprocesso.UpdateEPEvento;

/**
 * questa classe far? parte del core, si registra in modo automatico
 * per ascoltare la modifica delle listeProcesso, sia utente che ricavata
 * dalla topologyWindow, l'oggetto contenuto nella lista ? di tipo <code>JListaProcessoInfo</code>
 * @author michele
 *
 */

/** Classe che estende JList di Java_ E' utilizzata per la creazione della
	lista di processi (disegnati nella S_A_ Topology o inseriti dall'utente)
	per i quali ? possibile creare uno o pi? State Diagram. */

public class JListaProcesso 
	extends JList 
	implements IListaProcessoListener, ListaProcessoInterface {

	/** Lista dei processi inseriti nel S_A_ Topology Diagram. */
	//private DefaultListModel listProcessFromTopology;

	/** Lista dei processi inseriti direttamente dall'utente,
		ovvero non presenti nel S_A_ Topology Diagram. */
	//private DefaultListModel listProcessFromUser;

	
	public static final int ALL				= 0;
	public static final int USER			= 1;
	public static final int GLOBAL		= 2;
	public static final int NODUMMY 	= 4;
	
	private int modality;
	
	
	/** Lista ottenuta come concatenazione delle due precedenti_
		Pertanto, la lista contiene l'elenco di tutti i processi. */
	private DefaultListModel listProcess;
	//	private DefaultListModel listProcess;
	/**
	 * dati relativi al plugin
	 */
	private PlugData plugData;

	/** Popup menu per l'aggiunta, l'eliminazione e
		la modifica delle propriet? di un processo
		non presente nel S_A_ Topology Diagram. */
	private JPopupMenuProcess listprocessPopupMenu;


	private JListaProcessoEvent jlpe = null;

	/**
	 * Costruttore
	 * @param PlugData per permettere alla classe di registrarsi come listener
	 * in moda da creare la lista visualizzata in automatico  
	 */
	public JListaProcesso(IPlugData pd) {
		super(((plugin.topologychannels.data.PlugData)
				(pd.getPlugDataManager().getPlugData("charmy.plugin.topologychannels"))).getListaProcesso().toArray());
		plugData = ((plugin.topologychannels.data.PlugData)
		(pd.getPlugDataManager().getPlugData("charmy.plugin.topologychannels")));
		modality = NODUMMY;
		/* registrazione nei listener */
		//plugData.getListaProcessiUtente().addListener(this);
		plugData.getListaProcesso().addListener(this);
		//listProcess = (DefaultListModel)this.getModel();
		//new DefaultListModel(plugData.getListaProcessiUtente().toArray());

		//listProcessFromTopology = new DefaultListModel();
		//listProcessFromUser = new DefaultListModel();
		//listProcess = new DefaultListModel();
		listProcess = new DefaultListModel();

		settaProcessi();
		this.setModel(listProcess);
		setCellRenderer(new MyCellRenderer());
		listprocessPopupMenu = new JPopupMenuProcess(this);
		addMouseListener(new ProcessListMouseAction());
		addListSelectionListener(new ProcessListAction());

	}

	/**
	 * inizializza la lista dei processi
	 */
	private void settaProcessi() {
		ElementoProcesso tmp;
		listProcess.removeAllElements();
		/*
		for (int i = 0; i < plugData.getListaProcessiUtente().size(); i++) {
			listProcess.addElement(
				new JListaProcessoInfo(
					plugData.getListaProcessiUtente(),
					(ElementoProcesso) plugData
						.getListaProcessiUtente()
						.getElement(
						i)));
		}
		*/
		for (int i = 0; i < plugData.getListaProcesso().size(); i++) {
			tmp = (ElementoProcesso) plugData.getListaProcesso().getElement(i);
			addProcesso(plugData.getListaProcesso(), tmp);

		}

	}
	/**
	 * aggiungi un elemento alla lista
	 * @param obj
	 */
	private void addElement(Object obj){
		listProcess.addElement(obj);
	}
	
	/**
	 * aggiunge un processo alla lista tenendo conto di eventuali
	 * settaggi in modality
	 * @param tmp
	 */
	private void addProcesso(ListaProcesso lp, ElementoProcesso tmp){
		switch (modality){
			case ALL:
				addElement(	new JListaProcessoInfo(
						lp, tmp));
				break;
			case USER:
				if(tmp.getAppartenenza()==ElementoProcesso.USER){
					addElement(	new JListaProcessoInfo(
							lp, tmp));
					
				}
				break;
			case GLOBAL:
				if(tmp.getAppartenenza()==ElementoProcesso.GLOBALE){
					addElement(	new JListaProcessoInfo(
							lp, tmp));
				}
				break;
			case NODUMMY:
				if(!tmp.isDummy()){
					addElement(	new JListaProcessoInfo(
							lp, tmp));
				}
				break;
		}		
	}
	
	
	/**
	 * setta la classe che gestisce l'evento per il cambio di selezione
	 * @param jlpe 
	 */
	public void setJListaProcessoEvent(JListaProcessoEvent jlpe){
		this.jlpe = jlpe;
	}

	/** Crea la lista di processi a partire dalla S_A_ Topology ed a questa
		aggiunge quella dei processi inseriti direttamente dall'utente. */
	//public void setProcessFromTopology(Vector vProcName) {
	/*
	String tmpProcesso;
	
	if (vProcName != null) {
		listProcessFromTopology.removeAllElements();
		for (int i = 0; i < vProcName.size(); i++) {
			listProcessFromTopology.addElement((String) vProcName.get(i));
		}
		listProcess.removeAllElements();
		if (listProcessFromTopology != null) {
			for (int i = 0; i < listProcessFromTopology.size(); i++) {
				listProcess.addElement(listProcessFromTopology.get(i));
			}
		}
		if (listProcessFromUser != null) {
			for (int i = 0; i < listProcessFromUser.size(); i++) {
				tmpProcesso = (String) listProcessFromUser.get(i);
				if (!listProcess.contains(tmpProcesso)) {
					listProcess.addElement(tmpProcesso);
				} else {
					listProcessFromUser.remove(i);
					i--;
				}
			}
		}
	}
	*/
	//}

	/** Aggiunge alla lista dei processi utente ed a quella
		complessiva il processo inserito dall'utente. */
	//public void addElementFromUser(String nstr) {
	/*
	if (!listProcess.contains(nstr)) {
		listProcessFromUser.addElement(nstr);
		listProcess.addElement(nstr);
	}
	*/
	//}

	/** Verifica se la stringa passata in ingresso (nome
		del processo) ? presente nella lista dei processi
		inseriti direttamente dall'utente. */
	public boolean containsInUserProcessList(String nomeP) {
		for (int i = 0; i < listProcess.size(); i++) {
			JListaProcessoInfo jpi = (JListaProcessoInfo) listProcess.get(i);
			if (nomeP.equals(jpi.getElementoProcesso().getName())) {
				if (plugData.getListaProcesso()
					== jpi.getListaProcesso()) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/** Rimuove il nome passato in ingresso dalla lista
		dei processi utente e da quella complessiva. */
	public void removeElementFromUser(String nomeP) {
		/*
			listProcessFromUser.removeElement(nomeP);
			listProcess.removeElement(nomeP);
		*/
	}

	/** Restituisce la dimensione dell'elenco
		di tutti i processi (quelli utente pi?
		quelli inseriti nel S_A_ Topology Diagram). */
	public int processNumber() {
		return listProcess.size();
	}

	/** Restituisce un array contenente tutti gli oggetti 
		(in questo caso stringhe) memorizzati nella lista
		di tutti i processi. */
	public Object[] getStringArray() {
		String arr[] = new String[listProcess.size()];
		for (int i = 0; i < listProcess.size(); i++) {
			JListaProcessoInfo jpi = (JListaProcessoInfo) listProcess.get(i);
			arr[i] = jpi.getElementoProcesso().getName();
		}

		return arr;
	}

	public void removeList() {
		listProcess.removeAllElements();
	}

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoAdded(eventi.AddEPEvento)
	 */
	public void processoAdded(AddEPEvento event) {
		/**
		 * l'aggiunta potrebbe riguardate un evento di undo
		 * perci? va controllato che l'elemento non sia non sia gi? inserito
		 * non ? quindi una semplice aggiunta
		 */

		if (findElemento(event.getElementoProcesso()) < 0) {
			addProcesso(event.getSorgente(),event.getElementoProcesso() );
			repaint();
		}
	}

	/**
	 * controlla se l'elemento processo sia gi? inserito
	 * @return l'indice dell'elemento inserito oppure -1 se
	 * l'elemento non ? inserito.
	 */
	/*
	 * , attenzione, controlla i cloni
	 */
	private int findElemento(ElementoProcesso ep) {
		for (int i = 0; i < listProcess.size(); i++) {
			JListaProcessoInfo jpi = (JListaProcessoInfo) listProcess.get(i);
			if (ep.getId()
				== jpi.getElementoProcesso().getId()) {
				return i;
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRemoved(eventi.RemoveEPEvento)
	 */
	public void processoRemoved(RemoveEPEvento event) {
		for (int i = 0; i < listProcess.size(); i++) {
			JListaProcessoInfo jpi = (JListaProcessoInfo) listProcess.get(i);
			if (event.getSorgente().equals(jpi.getListaProcesso())) {
				if (event.getElementoProcesso().getId()
					== jpi.getElementoProcesso().getId()) {
					listProcess.removeElementAt(i);
					break;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoUpdate(eventi.UpdateEPEvento)
	 */
	public void processoUpdate(UpdateEPEvento event) {
		int indice = findElemento(event.getNewElementoProcesso());
		if (indice >= 0) { //un evento che mi interessa
			JListaProcessoInfo jpi =
				(JListaProcessoInfo) listProcess.get(indice);
			jpi.setElementoProcesso(event.getNewElementoProcesso());
			jpi.setListaProcesso(event.getSorgente());
		}
		repaint();
	}

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRefresh()
	 */
	public void processoRefresh() {
		settaProcessi();
		repaint();
	}

	/**
	 * aggiunge un processo generico alla lista dei processi utente
	 *
	 */
	public void addGenerico() {
		plugData.getListaProcesso().addGenerico();
	}

	/**
	 * edita i dati relativi al processo selezionato
	 * @author michele
	 *
	 */

	public void edita() {
		int ProcessoScelto;
		//FinestraUserProcessProperties finestraDialogo;
		//	rifStateToolBar.setNoPressed();
		ProcessoScelto = getSelectedIndex();

		if (ProcessoScelto >= 0) {  // processo originale
			ElementoProcesso ep = plugData.getListaProcesso().getProcessoById(
				((JListaProcessoInfo) getSelectedValue())
					.getElementoProcesso().getId());
					
			
			//ElementoProcesso ep = 
			//				((JListaProcessoInfo) getSelectedValue())
			//					.getElementoProcesso();
					
			//finestraDialogo =
			//	new FinestraUserProcessProperties(ep,
			//		null,
			//		"Process Properties");
					
			//finestraProprietaProcesso =
				new FinestraElementoBoxTesto(
					ep,
					null,   //g2,
					null,
					"Process Properties",
					0,
			plugData.getPlugDataManager());		
					
			//	((JListaProcessoInfo)getSelectedValue()).getElementoProcesso();
		}
	}

	/**
	 * gestione del click destro del mouse 
	 * @param posizione x, e y del click
	 */
	private void mouseClick(int x, int y) {

		JListaProcessoInfo SelectedItem;

		int k = getSelectedIndex();

		if (k >= 0) {
			SelectedItem = (JListaProcessoInfo) getSelectedValue();

			/**
			 * controllo a chi appartiene l'evento
			 */
			if (containsInUserProcessList(SelectedItem
				.getElementoProcesso()
				.getName())) {
				listprocessPopupMenu.setAllEnabled();
			} else {
				listprocessPopupMenu.setOnlyAddProcessEnabled();
			}
		} else {
			listprocessPopupMenu.setOnlyAddProcessEnabled();
		}
		listprocessPopupMenu.show(this, x, y);
	}


	/** Classe nidificata per gestire gli eventi di "selezione" dalla
		JListaProcesso posta nella zona sinistra della StateWindow,
		cio? dalla lista con i nomi dei processi per i quali ? possibile
		disegnare uno o pi? State Diagram. */
	class ProcessListAction implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (getSelectedIndex() >= 0) {
				if(jlpe != null){				
		   			jlpe.change((JListaProcessoInfo) getSelectedValue());
				}
			}
		}
	}


	/**
	 * classe per ridisegnare l'oggetto di JList
	 * @author michele
	 * Charmy plug-in
	 *
	 */
	class MyCellRenderer extends JLabel implements ListCellRenderer {
//		final ImageIcon procIcon = new ImageIcon("icon/cut.gif");
//		final ImageIcon userIcon = new ImageIcon("icon/del.gif");
//		final ImageIcon noneIcon = new ImageIcon("icon/helpstate.gif");
		
		// This is the only method defined by ListCellRenderer.
		// We just reconfigure the JLabel each time we're called.

		public Component getListCellRendererComponent(JList list, Object value,
		// value to display (? un oggetto della classe ElementoProcesso)
		int index, // cell index
		boolean isSelected, // is the cell selected
		boolean cellHasFocus) // the list and the cell have the focus
		{
			ElementoProcesso eproce = null;
			JListaProcessoInfo ep = (JListaProcessoInfo) value;
			eproce = ep.getElementoProcesso();
			String s = ep.getElementoProcesso().getName();
			setText(s);
			//sistemazione icone;

			if (eproce.getAppartenenza() == ElementoProcesso.USER) {
				setIcon(new ImageIcon("icon/del.gif"));
			} else if (eproce.getAppartenenza() == ElementoProcesso.GLOBALE) {
				
				if(eproce.isDummy()){
					setIcon(new ImageIcon("icon/dummy.gif"));	
				}
				else if(eproce.getTipo() == ElementoProcesso.PROCESS){
				     	setIcon(new ImageIcon("icon/process.gif"));	
				     	System.out.println("WTF!!");
				    }
				 else if(eproce.getTipo() == ElementoProcesso.STORE){
					setIcon(new ImageIcon("icon/store.gif"));
				}
			} else {
				setIcon(new ImageIcon("icon/helpstate.gif"));
			}

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;
		}
	}

	/** Classe nidificata per gestire l'evento "pressione del pulsante
		destro del mouse" sulla JListaProcesso (posta nella zona 
		sinistra della StateWindow), cio? sulla lista con i nomi dei 
		processi per i quali ? possibile disegnare uno o pi? State Diagram. */
	class ProcessListMouseAction
		extends MouseAdapter
		implements MouseListener {
		public void mouseClicked(MouseEvent evt) {
			if (SwingUtilities.isRightMouseButton(evt)) {
				mouseClick(evt.getX(), evt.getY());
			}
			if(evt.getClickCount()>1){
				edita();
			}

		}
	}

	/* (non-Javadoc)
	 * @see utility.listaProcesso.action.ListaProcessoInterface#vediAll()
	 */
	public void vediAll() {
		this.modality = ALL;
		settaProcessi();
	}

	/* (non-Javadoc)
	 * @see utility.listaProcesso.action.ListaProcessoInterface#vediUser()
	 */
	public void vediUser() {
		this.modality = USER;
		settaProcessi();
	}

	/* (non-Javadoc)
	 * @see utility.listaProcesso.action.ListaProcessoInterface#vediGlobal()
	 */
	public void vediGlobal() {
		this.modality = GLOBAL;
		settaProcessi();
	}

	/* (non-Javadoc)
	 * @see utility.listaProcesso.action.ListaProcessoInterface#vediAllNoDummy()
	 */
	public void vediAllNoDummy() {
		this.modality = NODUMMY;
		settaProcessi();
	}
}