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
    

package plugin.sequencediagram.utility.listaSequence;

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

import plugin.sequencediagram.data.PlugData;
import plugin.sequencediagram.data.SequenceElement;
import plugin.sequencediagram.eventi.listaDS.AddListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSSeqEleEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSTimeEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSSeqEleEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSTimeEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSTimeEvento;
import plugin.sequencediagram.pluglistener.IListaDSListener;
import plugin.topologydiagram.utility.listaProcesso.JPopupMenuProcess;
import core.internal.runtime.data.IPlugData;
//import data.PlugData;


/**
 * questa classe far? parte del core, si registra in modo automatico
 * per ascoltare la modifica delle listeDS,
 * l'oggetto contenuto nella lista ? di tipo <code>JListaSequenceElementInfo</code>
 * @author michele
 *
 */
public class JListaSeqElement extends JList implements IListaDSListener {

	/** Lista dei processi inseriti nel S_A_ Topology Diagram. */
	//private DefaultListModel listProcessFromTopology;

	/** Lista dei processi inseriti direttamente dall'utente,
		ovvero non presenti nel S_A_ Topology Diagram. */
	//private DefaultListModel listProcessFromUser;

	/** Lista ottenuta ricavando i dati da listaDS incapsulati 
	 * in JListaSeqElementInfo. */
	private DefaultListModel listaDsWrapper;
	//	private DefaultListModel listProcess;
	/**
	 * dati relativi al plugin
	 */
	private PlugData plugData;

	/** Popup menu per l'aggiunta, l'eliminazione e
		la modifica delle propriet? di un processo
		non presente nel S_A_ Topology Diagram. */
	private JPopupMenuProcess listprocessPopupMenu;


	private JListaSeqElementEvent jlpe = null;

	/**
	 * Costruttore
	 * @param PlugData per permettere alla classe di registrarsi come listener
	 * in moda da creare la lista visualizzata in automatico  
	 */
	public JListaSeqElement(IPlugData pd) {
		super(((plugin.topologydiagram.data.PlugData)pd.getPlugDataManager().getPlugData("charmy.plugin.topology")).getListaProcesso().toArray());
		plugData = (plugin.sequencediagram.data.PlugData)pd.getPlugDataManager().getPlugData("charmy.plugin.sequence");
		/* registrazione nei listener */
		plugData.getListaDS().addListener(this);
		//listProcess = (DefaultListModel)this.getModel();
		//new DefaultListModel(plugData.getListaProcessiUtente().toArray());

		//listProcessFromTopology = new DefaultListModel();
		//listProcessFromUser = new DefaultListModel();
		//listProcess = new DefaultListModel();
		listaDsWrapper = new DefaultListModel();

		settaDS();
		this.setModel(listaDsWrapper);
		setCellRenderer(new MyCellRenderer());
//		listprocessPopupMenu = new JPopupMenuProcess(this);
//		addMouseListener(new ProcessListMouseAction());
		addListSelectionListener(new ProcessListAction());

	}

	/**
	 * inizializza la lista del wrapper della dinamica dei processi
	 */
	private void settaDS() {

		listaDsWrapper.removeAllElements();
		for (int i = 0; i < plugData.getListaDS().size(); i++) {
			listaDsWrapper.addElement(
				new JListaSeqElementInfo(
					plugData.getListaDS(),
					(SequenceElement) plugData
						.getListaDS()
						.get(i)));
		}
	}



	/**
	 * setta la classe che gestisce l'evento per il cambio di selezione
	 * @param jlpe 
	 */
	public void setJListaProcessoEvent(JListaSeqElementEvent jlpe){
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
	/*
	public boolean containsInUserProcessList(String nomeP) {
		for (int i = 0; i < listProcess.size(); i++) {
			JListaSeqElementInfo jpi = (JListaSeqElementInfo) listProcess.get(i);
			if (nomeP.equals(jpi.getElementoProcesso().getName())) {
				if (plugData.getListaProcessiUtente()
					== jpi.getListaProcesso()) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
*/
	/** Rimuove il nome passato in ingresso dalla lista
		dei processi utente e da quella complessiva. */
	public void removeElementFromUser(String nomeP) {
		/*
			listProcessFromUser.removeElement(nomeP);
			listProcess.removeElement(nomeP);
		*/
	}

	/** Restituisce la dimensione dell'elenco
		di tutti i gli elementi */
	public int number() {
		return listaDsWrapper.size();
	}

	/** Restituisce un array contenente tutti gli oggetti 
		(in questo caso stringhe) memorizzati nella lista
		di tutti i processi. */
	public Object[] getStringArray() {
		String arr[] = new String[listaDsWrapper.size()];
		for (int i = 0; i < listaDsWrapper.size(); i++) {
			JListaSeqElementInfo jpi = (JListaSeqElementInfo) listaDsWrapper.get(i);
			arr[i] = jpi.getSequenceElement().getNomeSequence();
		}

		return arr;
	}

	/**
	 * azzera il contenuto della lista
	 *
	 */
	public void removeList() {
		listaDsWrapper.removeAllElements();
	}


	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoAdded(eventi.AddEPEvento)
	 */

		/**
		 * l'aggiunta potrebbe riguardate un evento di undo
		 * perci? va controllato che l'elemento non sia non sia gi? inserito
		 * non ? quindi una semplice aggiunta
		 */
/*
	public void processoAdded(AddEPEvento event) {

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
	 * controlla se il SequenceElement sia gi? inserito
	 * @return l'indice dell'elemento inserito oppure -1 se
	 * l'elemento non ? inserito.
	 */
	/*
	 * , attenzione, controlla i cloni
	 */
	
	private int findElemento(SequenceElement se) {
		for (int i = 0; i < listaDsWrapper.size(); i++) {
			JListaSeqElementInfo jpi = (JListaSeqElementInfo) listaDsWrapper.get(i);
			if (se.getNomeSequence()
				== jpi.getSequenceElement().getNomeSequence()) {
				return i;
			}
		}
		return -1;
	}



	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRemoved(eventi.RemoveEPEvento)
	 */
/*
	public void processoRemoved(RemoveEPEvento event) {
		for (int i = 0; i < listProcess.size(); i++) {
			JListaSeqElementInfo jpi = (JListaSeqElementInfo) listProcess.get(i);
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
	 /*
	public void processoUpdate(UpdateEPEvento event) {
		int indice = findElemento(event.getNewElementoProcesso());
		if (indice >= 0) { //un evento che mi interessa
			JListaSeqElementInfo jpi =
				(JListaSeqElementInfo) listProcess.get(indice);
			jpi.setElementoProcesso(event.getNewElementoProcesso());
			jpi.setListaProcesso(event.getSorgente());
		}
		repaint();
	}
*/
	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRefresh()
	 */
/*
	public void processoRefresh() {
		settaProcessi();
		repaint();
	}
*/
	/**
	 * aggiunge un processo generico alla lista dei processi utente
	 *
	 */
	public void addGenerico() {
	//	plugData.getListaProcessiUtente().addGenerico();
	}

	/**
	 * edita i dati relativi al processo selezionato
	 * @author michele
	 *
	 */

	public void edita() {
		/*
		int ProcessoScelto;
		FinestraUserProcessProperties finestraDialogo;
		//	rifStateToolBar.setNoPressed();
		ProcessoScelto = getSelectedIndex();

		if (ProcessoScelto >= 0) {
			ElementoProcesso ep = 
				((JListaSeqElementInfo) getSelectedValue())
					.getElementoProcesso();
			finestraDialogo =
				new FinestraUserProcessProperties(
					((JListaSeqElementInfo) getSelectedValue())
						.getElementoProcesso(),
					null,
					"Process Properties");
					
		

			//	((JListaProcessoInfo)getSelectedValue()).getElementoProcesso();
		}
		*/
	}

	/**
	 * gestione del click destro del mouse 
	 * @param posizione x, e y del click
	 */
	private void mouseClick(int x, int y) {

	/*
		JListaSeqElementInfo SelectedItem;

		int k = getSelectedIndex();

		if (k >= 0) {
			SelectedItem = (JListaSeqElementInfo) getSelectedValue();
*/
			/**
			 * controllo a chi appartiene l'evento
			 */
			/*
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
	
	*/
	}


	/** Classe nidificata per gestire gli eventi di "selezione" dalla
		JListaProcesso posta nella zona sinistra della StateWindow,
		cio? dalla lista con i nomi dei processi per i quali ? possibile
		disegnare uno o pi? State Diagram. */
	class ProcessListAction implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (getSelectedIndex() >= 0) {
				if(jlpe != null){				
		   			jlpe.change((JListaSeqElementInfo) getSelectedValue());
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
		final ImageIcon procIcon = new ImageIcon("icon/cut.gif");
		final ImageIcon noneIcon = new ImageIcon("icon/helpstate.gif");

		// This is the only method defined by ListCellRenderer.
		// We just reconfigure the JLabel each time we're called.

		public Component getListCellRendererComponent(JList list, Object value,
		// value to display (? un oggetto della classe ElementoProcesso)
		int index, // cell index
		boolean isSelected, // is the cell selected
		boolean cellHasFocus) // the list and the cell have the focus
		{
			JListaSeqElementInfo ep = (JListaSeqElementInfo) value;
			String s = ep.getSequenceElement().getNomeSequence();
			setText(s);
			
			
			
			//sistemazione icone;
			setIcon(procIcon);
			/*
			if (plugData
				.getListaProcessiUtente()
				.equals(ep.getListaProcesso())) {
				setIcon(userIcon);
			} else if (
				plugData.getListaProcesso().equals(ep.getListaProcesso())) {
				setIcon(procIcon);
			} else {
				setIcon(noneIcon);
			}
*/
		//selezione
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

		}
	}


/* gestione eventi */

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#classeAdded(eventi.listaDS.AddListaDSClasseEvento)
	 */
	public void classeAdded(AddListaDSClasseEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#classeRemoved(eventi.listaDS.RemoveListaDSClasseEvento)
	 */
	public void classeRemoved(RemoveListaDSClasseEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#classeUpdate(eventi.listaDS.UpdateListaDSClasseEvento)
	 */
	public void classeUpdate(UpdateListaDSClasseEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#seqLinkAdded(eventi.listaDS.AddListaDSLinkEvento)
	 */
	public void seqLinkAdded(AddListaDSLinkEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#seqLinkRemoved(eventi.listaDS.RemoveListaDSLinkEvento)
	 */
	public void seqLinkRemoved(RemoveListaDSLinkEvento event) {	
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#seqLinkUpdate(eventi.listaDS.UpdateListaDSLinkEvento)
	 */
	public void seqLinkUpdate(UpdateListaDSLinkEvento event) {
	
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#timeAdded(eventi.listaDS.AddListaDSTimeEvento)
	 */
	public void timeAdded(AddListaDSTimeEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#timeRemoved(eventi.listaDS.RemoveListaDSTimeEvento)
	 */
	public void timeRemoved(RemoveListaDSTimeEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#timeUpdate(eventi.listaDS.UpdateListaDSTimeEvento)
	 */
	public void timeUpdate(UpdateListaDSTimeEvento event) {

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#sequenceAdded(eventi.listaDS.AddListaDSSeqEleEvento)
	 */
	public void sequenceAdded(AddListaDSSeqEleEvento event) {
		if (findElemento(event.getSequenceElement()) < 0) {
			listaDsWrapper.addElement(
				new plugin.sequencediagram.utility.listaSequence.JListaSeqElementInfo(
					event.getSorgente(),
					event.getSequenceElement()));
		}
		repaint();
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#sequenceRemoved(eventi.listaDS.RemoveListaDSSeqEleEvento)
	 */
	public void sequenceRemoved(RemoveListaDSSeqEleEvento event) {
		for (int i = 0; i < listaDsWrapper.size(); i++) {
			
			JListaSeqElementInfo jpi = (JListaSeqElementInfo) listaDsWrapper.get(i);
			if (event.getSequenceElement().equals(jpi.getSequenceElement())) {
				//if (event.getElementoProcesso().getId()
				//		== jpi.getElementoProcesso().getId()) {
					listaDsWrapper.removeElementAt(i);
					break;
				//}
			}
		}
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#listaDSRefresh()
	 */
	public void listaDSRefresh() {

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#constraintAdded(eventi.listaDS.AddListaDSConstraintEvento)
	 */
	public void constraintAdded(AddListaDSConstraintEvento event) {

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#constraintRemoved(eventi.listaDS.RemoveListaDSConstraintEvento)
	 */
	public void constraintRemoved(RemoveListaDSConstraintEvento event) {
	
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#constraintUpdate(eventi.listaDS.UpdateListaDSConstraintEvento)
	 */
	public void constraintUpdate(UpdateListaDSConstraintEvento event) {
		
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#parallelAdded(eventi.listaDS.AddListaDSParallelEvento)
	 */
	public void parallelAdded(AddListaDSParallelEvento event) {

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#parallelRemoved(eventi.listaDS.RemoveListaDSParallelEvento)
	 */
	public void parallelRemoved(RemoveListaDSParallelEvento event) {
	
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#parallelUpdate(eventi.listaDS.UpdateListaDSParallelEvento)
	 */
	public void parallelUpdate(UpdateListaDSParallelEvento event) {
		
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#SimAdded(eventi.listaDS.AddListaDSSimEvento)
	 */
	public void SimAdded(AddListaDSSimEvento event) {

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#SimRemoved(eventi.listaDS.RemoveListaDSSimEvento)
	 */
	public void SimRemoved(RemoveListaDSSimEvento event) {
	
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#SimUpdate(eventi.listaDS.UpdateListaDSSimEvento)
	 */
	public void SimUpdate(UpdateListaDSSimEvento event) {
		
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#LoopAdded(eventi.listaDS.AddListaDSLoopEvento)
	 */
	public void LoopAdded(AddListaDSLoopEvento event) {

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#LoopRemoved(eventi.listaDS.RemoveListaDSLoopEvento)
	 */
	public void LoopRemoved(RemoveListaDSLoopEvento event) {
	
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#LoopUpdate(eventi.listaDS.UpdateListaDSLoopEvento)
	 */
	public void LoopUpdate(UpdateListaDSLoopEvento event) {
		
	}
	

	
}