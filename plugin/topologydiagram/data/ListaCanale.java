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
    


package plugin.topologydiagram.data;

import java.io.Serializable;
import java.util.Iterator;

import core.internal.runtime.data.IPlugData;

import plugin.topologydiagram.data.delegate.DelegateListaCanaleListener;

//import plugin.topologydiagram.data.*;

import plugin.topologydiagram.eventi.listaprocesso.AddEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.RemoveEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.UpdateEPEvento;
import plugin.topologydiagram.pluglistener.IListaCanaleListener;
import plugin.topologydiagram.pluglistener.IListaProcessoListener;
import plugin.topologydiagram.resource.data.ListaCanaleMessaggio;
import plugin.topologydiagram.resource.graph.GraficoCollegamento;

/** Questa classe e' utilizzata per memorizzare la lista dei canali di
 *	collegamento tra i processi introdotti nel S_A_ Topology Diagram. 
 *	
 */

public class ListaCanale extends ListaCanaleMessaggio 
	implements Serializable, 
			IListaProcessoListener
{

	/**
	 * delega per la gestione degli eventi
	 */
	DelegateListaCanaleListener delegateListener = null;

    /** Costruttore. 
     * @param riferimento al contenitore dei dati
     * */
    public ListaCanale(IPlugData pl){
  		super(pl);
  		setDelegateListener(new DelegateListaCanaleListener(this, pl));
    } 
 

	/** 
	 * Restituisce il nome di un canale se ne esiste almeno uno, altrimenti null. 
	 * 
	 * */
	public String getAnyNameChannel() {
		if (isEmpty())
			return null;
		return ((ElementoCanale) (getElement(0))).getName();
	}


	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
    public void restoreFromFile(){	
    	GraficoCollegamento grafico;
		ElementoCanale can;
		int tipomessaggio = 0;
		boolean flusso;
		if (lista!=null){			
			for (int i=0; i<lista.size(); i++){
				can = (ElementoCanale)(lista.get(i));				
				flusso = can.getFlussoDiretto();	
				can.updateCanalePosizione();			
				grafico = (GraficoCollegamento)(can.getGrafico());
				grafico.restoreFromFile(tipomessaggio,flusso);
			}
    	}  
	}
	
    
	/**
	 * ritorna l'elemento canale identificato da id
	 * @param id identificativo del canale
	 * @return null se il canale non viene trovato altrimenti il canale
	 */
	public ElementoCanale getCanaleById(long id){
		return (ElementoCanale) super.getElementById(id);
	}

	/**
	 * aggiunta di un listener per la lista
	 * @param ilpl
	 */
	public void addListener(IListaCanaleListener ilpl){
		this.delegateListener.add(ilpl);
	}

	/**
	 * rimozione di   un listener per la lista
	 * @param ilpl
	 */
	public void removeListener(IListaCanaleListener ilpl){
		this.delegateListener.removeElement(ilpl);
	}

	/**
	 * rimuove tutti i listener registrati
	 *
	 */
	public void removeAllListener(){
		this.delegateListener.removeAllElements();
	}
	
	
	/**
	 * ritorna la classe di delega per la gestione degli eventi
	 * @return
	 */
	public DelegateListaCanaleListener getDelegateListener() {
		return delegateListener;
	}

	/**
	 * setta la delega per la gestione degli eventi
	 * @param listener
	 */
	public void setDelegateListener(DelegateListaCanaleListener listener) {
		delegateListener = listener;
		listener.setListaCanale(this);
		setNotify(delegateListener);
		
	}

	/**
	 * listener per controllare i processi
	 * @author michele
	 */

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoAdded(eventi.listaprocesso.AddEPEvento)
	 */
	public void processoAdded(AddEPEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRemoved(eventi.listaprocesso.RemoveEPEvento)
	 */
	public void processoRemoved(RemoveEPEvento event) {
		removeAllLink(event.getElementoProcesso());
	}


	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoUpdate(eventi.listaprocesso.UpdateEPEvento)
	 */
	public void processoUpdate(UpdateEPEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRefresh()
	 */
	public void processoRefresh() {
	}

	public void setUnselected(){
		Iterator ite = iterator();
		while (ite.hasNext()) {
			((ElementoCanale) ite.next()).setSelected(false);
		}
	}


}