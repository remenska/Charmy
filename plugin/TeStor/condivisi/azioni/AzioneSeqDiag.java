/*
 * Created on 7-ott-2004
 */
package plugin.TeStor.condivisi.azioni;

import javax.swing.DefaultListModel;

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
import plugin.sequencediagram.data.SequenceElement;
import plugin.sequencediagram.pluglistener.IListaDSListener;
import core.internal.ui.PlugDataWin;
//import core.internal.runtime.data.PlugData;

import plugin.TeStor.condivisi.interfaccia.*;
import plugin.TeStor.data.PlugData;
/**
 * Gestisce tutte le modifiche apportate ai SD dall'utente di Charmy.
 * <br>Per le finalità del TeStor l'unico aspetto della modifica dei SD che interessa
 * è la creazione e la cancellazione di SD nel Sequence Editor (SE).
 * La classe si occupa, pertanto, di aggiungere i SD (creati dall'utente nel SE
 * o aperti da file) alla lista del TeStor dei SD disponibili, e di rimuovere i SD
 * (rimossi dall'utente nel SE o per la creazione di una nuova architettura)
 * dalla lista del TeStor che li contiene (dei SD disponibili o selezionati).
 * 
 * @author Fabrizio Facchini
 */
public class AzioneSeqDiag implements IListaDSListener{

	/**
	 * I SD disponibili (sottoinsieme dei SD nel Sequence Editor)
	 */
	private DefaultListModel disp;
	/**
	 * I SD selezionati per il TeStor
	 */
	private DefaultListModel sel;
	/**
	 * La parte grafica di Charmy
	 */
	private PlugDataWin pdw;
	/**
	 * La parte dati di Charmy
	 */
	private PlugData pd;
	
	/**
	 * Costruisce un nuovo oggetto AzioneSeqDiag sulla base delle liste di SD specificate
	 *  
	 * @param disp la lista dei SD disponibili
	 * @param sel la lista dei SD selezionati
	 * @param pdw la parte grafica di Charmy
	 * @param pd la parte dati di Charmy
	 */
	public AzioneSeqDiag(DefaultListModel disp, DefaultListModel sel, 
						PlugDataWin pdw, PlugData pd){
		this.disp = disp;
		this.sel = sel;
		this.pdw = pdw;
		this.pd = pd;
	}
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
	/**
	 * Aggiunge il SD specificato nell'evento alla lista dei SD disponibili
	 * 
	 * @param event l'evento che segnala l'aggiunta di un SD nel Sequence Editor
	 */
	public void sequenceAdded(AddListaDSSeqEleEvento event) {
		disp.addElement(new ScrollSequence(event.getSequenceElement(),pd,pdw));
		
	}

	/** 
	 * Rimuove il SD specificato nell'evento dalla lista del TeStor che lo contiene.
	 * Poiché il SD rimosso poteva essere disponibile o selezionato esegue una ricerca
	 * nelle due liste per rimuoverlo da quella in cui si trova effettivamente.
	 * 
	 * @param event l'evento che segnala la rimozione di un SD nel Sequence Editor
	 */
	public void sequenceRemoved(RemoveListaDSSeqEleEvento event) {
		// il SequenceElement rimosso
		SequenceElement se = event.getSequenceElement();
		
		/* esegue una ricerca lineare sulle due liste per rimuovere
		 lo ScrollSequence contenente il SD... */
		 //...nella lista dei SD disponibili
		for(int i=0;i<disp.getSize();i++){
			if(((ScrollSequence)disp.get(i)).getSequence()==se){
				disp.remove(i);
				return;
			}
		}
		//...nella lista dei SD selezionati
		for(int i=0;i<sel.getSize();i++){
			if(((ScrollSequence)sel.get(i)).getSequence()==se){
				sel.remove(i);
				return;
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#listaDSRefresh()
	 */
	public void listaDSRefresh() {
		
	}
	public void LoopAdded(AddListaDSLoopEvento event) {
		// TODO Auto-generated method stub
		
	}
	public void LoopRemoved(RemoveListaDSLoopEvento event) {
		// TODO Auto-generated method stub
		
	}
	public void LoopUpdate(UpdateListaDSLoopEvento event) {
		// TODO Auto-generated method stub
		
	}
	public void parallelAdded(AddListaDSParallelEvento event) {
		// TODO Auto-generated method stub
		
	}
	public void parallelRemoved(RemoveListaDSParallelEvento event) {
		// TODO Auto-generated method stub
		
	}
	public void parallelUpdate(UpdateListaDSParallelEvento event) {
		// TODO Auto-generated method stub
		
	}
	public void SimAdded(AddListaDSSimEvento event) {
		// TODO Auto-generated method stub
		
	}
	public void SimRemoved(RemoveListaDSSimEvento event) {
		// TODO Auto-generated method stub
		
	}
	public void SimUpdate(UpdateListaDSSimEvento event) {
		// TODO Auto-generated method stub
		
	}

}
