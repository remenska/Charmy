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
    

package plugin.topologydiagram.utility.listaProcesso;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import core.internal.runtime.data.IPlugData;

import plugin.topologydiagram.data.PlugData;
import plugin.topologydiagram.utility.listaProcesso.toolbar.ListaProcessoToolBar;

/**
 * questa classe farà parte del core, si registra in modo automatico
 * per ascoltare la modifica delle listeProcesso, sia utente che ricavata
 * dalla topologyWindow, l'oggetto contenuto nella lista è di tipo <code>JListaProcessoInfo</code>
 * @author michele
 *
 */

/** Classe che estende JList di Java_ E' utilizzata per la creazione della
	lista di processi (disegnati nella S_A_ Topology o inseriti dall'utente)
	per i quali è possibile creare uno o più State Diagram. */

public class JListaProcessoPanel 
	extends JPanel {

	/** Lista dei processi inseriti nel S_A_ Topology Diagram. */
	//private DefaultListModel listProcessFromTopology;

	/** Lista dei processi inseriti direttamente dall'utente,
		ovvero non presenti nel S_A_ Topology Diagram. */
	//private DefaultListModel listProcessFromUser;

	/** Lista ottenuta come concatenazione delle due precedenti_
		Pertanto, la lista contiene l'elenco di tutti i processi. */
	//private DefaultListModel listProcess;
	//	private DefaultListModel listProcess;
	/**
	 * dati relativi al plugin
	 */
	private PlugData plugData;

	/** Popup menu per l'aggiunta, l'eliminazione e
		la modifica delle proprietà di un processo
		non presente nel S_A_ Topology Diagram. */
	//private JPopupMenuProcess listprocessPopupMenu;


	//private JListaProcessoEvent jlpe = null;

	private JListaProcesso jlp = null;
	
	private ListaProcessoToolBar lpt;
	
	/**
	 * Costruttore
	 * @param PlugData per permettere alla classe di registrarsi come listener
	 * in moda da creare la lista visualizzata in automatico  
	 */
	public JListaProcessoPanel(IPlugData pd) {
		super();
		
		//this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.setLayout(new BorderLayout());
		
		
		
		jlp = new JListaProcesso(pd);
		
		lpt = new ListaProcessoToolBar(jlp);
		
		//jlp.setBorder(new LineBorder(new Color(0,0,0), 2));
		jlp.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
	//	this.setBorder(new LineBorder(new Color(0,0,0), 2));
		//BorderLayout bl = new BorderLayout();
		this.add(lpt, BorderLayout.NORTH);
		this.add(jlp, BorderLayout.CENTER);
		
		plugData = (plugin.topologydiagram.data.PlugData)pd.getPlugDataManager().getPlugData("charmy.plugin.topology");
		
		/* registrazione nei listener */
		//plugData.getListaProcessiUtente().addListener(this);
//		plugData.getListaProcesso().addListener(this);
		//listProcess = (DefaultListModel)this.getModel();
		//new DefaultListModel(plugData.getListaProcessiUtente().toArray());

		//listProcessFromTopology = new DefaultListModel();
		//listProcessFromUser = new DefaultListModel();
		//listProcess = new DefaultListModel();
//		listProcess = new DefaultListModel();

//		settaProcessi();
//		this.setModel(listProcess);
//		setCellRenderer(new MyCellRenderer());
//		listprocessPopupMenu = new JPopupMenuProcess(this);
//		addMouseListener(new ProcessListMouseAction());
//		addListSelectionListener(new ProcessListAction());

	}




	/**
	 * setta la classe che gestisce l'evento per il cambio di selezione
	 * @param jlpe 
	 */
	public void setJListaProcessoEvent(JListaProcessoEvent jlpe){
		this.jlp.setJListaProcessoEvent(jlpe);
	}
	/** Verifica se la stringa passata in ingresso (nome
		del processo) è presente nella lista dei processi
		inseriti direttamente dall'utente. */
	public boolean containsInUserProcessList(String nomeP) {
		return jlp.containsInUserProcessList(nomeP);
	}

	/** Restituisce la dimensione dell'elenco
		di tutti i processi (quelli utente più
		quelli inseriti nel S_A_ Topology Diagram). */
	public int processNumber() {
		return jlp.processNumber();
	}

	/** Restituisce un array contenente tutti gli oggetti 
		(in questo caso stringhe) memorizzati nella lista
		di tutti i processi. */
	public Object[] getStringArray() {
	
		return jlp.getStringArray();
	}

	public void removeList() {
		jlp.removeList();
	}

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoAdded(eventi.AddEPEvento)
	 */
//	public void processoAdded(AddEPEvento event) {
		/**
		 * l'aggiunta potrebbe riguardate un evento di undo
		 * perciò va controllato che l'elemento non sia non sia già inserito
		 * non è quindi una semplice aggiunta
		 */
/*
		if (findElemento(event.getElementoProcesso()) < 0) {
			listProcess.addElement(
				new utility.listaProcesso.JListaProcessoInfo(
					event.getSorgente(),
					event.getElementoProcesso()));
		}
		repaint();
	}
*/
	/**
	 * controlla se l'elemento processo sia già inserito
	 * @return l'indice dell'elemento inserito oppure -1 se
	 * l'elemento non è inserito.
	 */
	/*
	 * , attenzione, controlla i cloni
	 */
/*	private int findElemento(ElementoProcesso ep) {
		for (int i = 0; i < listProcess.size(); i++) {
			JListaProcessoInfo jpi = (JListaProcessoInfo) listProcess.get(i);
			if (ep.getId()
				== jpi.getElementoProcesso().getId()) {
				return i;
			}
		}
		return -1;
	}
*/
	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRemoved(eventi.RemoveEPEvento)
	 */
/*	
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
*/
	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoUpdate(eventi.UpdateEPEvento)
	 */
/*	public void processoUpdate(UpdateEPEvento event) {
		int indice = findElemento(event.getNewElementoProcesso());
		if (indice >= 0) { //un evento che mi interessa
			JListaProcessoInfo jpi =
				(JListaProcessoInfo) listProcess.get(indice);
			jpi.setElementoProcesso(event.getNewElementoProcesso());
			jpi.setListaProcesso(event.getSorgente());
		}
		repaint();
	}
*/
	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRefresh()
	 */
/*	public void processoRefresh() {
		settaProcessi();
		repaint();
	}
*/
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
		jlp.edita();
	}



	/**
	 * gestione del click destro del mouse 
	 * @param posizione x, e y del click
	 */

/*	private void mouseClick(int x, int y) {

		JListaProcessoInfo SelectedItem;

		int k = getSelectedIndex();

		if (k >= 0) {
			SelectedItem = (JListaProcessoInfo) getSelectedValue();
*/
			/**
			 * controllo a chi appartiene l'evento
			 */
/*			if (containsInUserProcessList(SelectedItem
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
*/

	/** Classe nidificata per gestire gli eventi di "selezione" dalla
		JListaProcesso posta nella zona sinistra della StateWindow,
		cioé dalla lista con i nomi dei processi per i quali è possibile
		disegnare uno o più State Diagram. */
/* class ProcessListAction implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (getSelectedIndex() >= 0) {
				if(jlpe != null){				
		   			jlpe.change((JListaProcessoInfo) getSelectedValue());
				}
			}
		}
	}
*/

	/**
	 * classe per ridisegnare l'oggetto di JList
	 * @author michele
	 * Charmy plug-in
	 *
	 */
/*	class MyCellRenderer extends JLabel implements ListCellRenderer {
//		final ImageIcon procIcon = new ImageIcon("icon/cut.gif");
//		final ImageIcon userIcon = new ImageIcon("icon/del.gif");
//		final ImageIcon noneIcon = new ImageIcon("icon/helpstate.gif");
		
		// This is the only method defined by ListCellRenderer.
		// We just reconfigure the JLabel each time we're called.

		public Component getListCellRendererComponent(JList list, Object value,
		// value to display (è un oggetto della classe ElementoProcesso)
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
*/
	/** Classe nidificata per gestire l'evento "pressione del pulsante
		destro del mouse" sulla JListaProcesso (posta nella zona 
		sinistra della StateWindow), cioé sulla lista con i nomi dei 
		processi per i quali è possibile disegnare uno o più State Diagram. */
/*	class ProcessListMouseAction
		extends MouseAdapter
		implements MouseListener {
		public void mouseClicked(MouseEvent evt) {
			if (SwingUtilities.isRightMouseButton(evt)) {
				mouseClick(evt.getX(), evt.getY());
			}

		}
	}
*/	
	
	/**
	 * @param selectionMode
	 */
	public void setSelectionMode(int selectionMode) {
		jlp.setSelectionMode(selectionMode);
	}

	
	
	
	/**
	 * @return
	 */
	public int getSelectedIndex() {
		return jlp.getSelectedIndex();
	}

	/**
	 * @param index
	 */
	public void setSelectedIndex(int index) {
		jlp.setSelectedIndex(index);
	}

	/**
	 * 
	 */
	public void clearSelection() {
		jlp.clearSelection();
	}

	/**
	 * @return
	 */
	public Object getSelectedValue() {
		return jlp.getSelectedValue();
	}

	/**
	 * @param nomeP
	 */
	public void removeElementFromUser(String nomeP) {
		jlp.removeElementFromUser(nomeP);
	}

}