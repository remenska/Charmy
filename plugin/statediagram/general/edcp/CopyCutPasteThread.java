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
    
package plugin.statediagram.general.edcp;


import java.util.Iterator;

import core.internal.runtime.data.IPlugData;

import plugin.topologydiagram.data.ElementoProcesso;
import plugin.topologydiagram.resource.general.edcp.LinkedListWithID;
import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.graph.ElementoMessaggio;


/**
 * @author michele
 * Implementazione di un sistema di copy/cut/paste per quanto riguarda
 * le Liste dei thread
 * Tale classe pu? essere instanziata ed utilizzata in ogni contesto
 * di editing attraverso i plug.
 * Spiegazione del funzionamento:
 * Copy
 * 1. prelevo una lista di cloni degli elementi processo selezionati dal ThreadElement
 *     passato n ingresso.
 * 2. dalla lista dei cloni creo una lista degli stati passati in ingresso solo se selezionati
 *     e con entrambi i nodi all'inteno della lista cloni ElementoStato
 * Paste.
 * 1. Controllo se la lista dei cloni ElementoStato ? piena
 * 	se ? piena allora
 * 	1.a Nuova numerazione dell'elementI clone
 * 	1.a Nuova numerazione dell'elementoMessaggio
 * 2.Inserimento nella lista ThreadElement  passata in ingresso vera
 * 3. Inserimento canali
 * NB. la selezione di un canale senza entrambe i nodi selezionati implica che il canale non viene
 * copiato.
 * Inoltre l'inserimento, la cancellazzione dalle liste interne di questa
 * classe non genera nessun particolare tipo di messaggio.
 **/
public class CopyCutPasteThread  {

	private LinkedListWithID listaStato = null;
	private LinkedListWithID listaMess = null;
	
	private IPlugData plugData;

	public CopyCutPasteThread(IPlugData pl) {
		plugData = pl;
		listaStato = new LinkedListWithID();
		listaMess = new LinkedListWithID();
	}

	/**
	 * preleva i dati per la copia dal ThreadElement
	 * @param te
	 */
	public void copy(ThreadElement te) {
		/**
		 * pulizia precedenti copy
		 */

		listaStato.clear();
		/*
		 * processi selezionati
		 */
		Iterator ite = te.getListaStato().iterator();
		while (ite.hasNext()) {
			ElementoStato ep = (ElementoStato) ite.next();
			if (ep.isSelected()) {
				     listaStato.add(ep.cloneStato());
			}
		}

		/*
		 *scorrimento dei canali selezionati 
		 * e ne realizza il legame con la nuova lista di elementi
		 **/
		listaMess.clear();
		if(listaStato.isEmpty()){ // lista Stato vuota
			return;
		}
		ite = te.getListaMessaggio().iterator();
		while (ite.hasNext()) {
			ElementoMessaggio ec = (ElementoMessaggio) ite.next();
			if (ec.isSelected()) {
				if (listaStato.containing(ec.getElement_one().getId())
					&& listaStato.containing(ec.getElement_two().getId())) {
					//esistono entrambe i nodi
					ElementoMessaggio ecn = 
  					  new ElementoMessaggio(
  					  	te,
 						     (ElementoStato)listaStato.getElementById(ec.getElementFrom().getId()),
							  (ElementoStato)listaStato.getElementById(ec.getElementTo().getId()),
							  ec.getTipo(),
							  "mitoppe",
							 ec.getMsgRRF(),
							  true,
							  ec.getId(),
							  null);
					ecn .setValue(ec, true);   
					listaMess.add(ecn);	   
				}
			}
		}

	}


/*
	if(ep.getTipo() == ElementoStato.START){
		if(!te.getListaStato().startExist()){
			listaStato.add(ep.cloneStato());
		}
	//se lo stato di partenza esiste allora non lo aggiungo	
	}
	else{
		 listaStato.add(ep.cloneStato());
	}
}
*/
	/**
	 * genera una nuova numerazione per i canali e i processi e li invia nel
	 * nelle opportune lista
	 * @param ThreadElement su cui effettuare la copia
	 */
	public void paste(ThreadElement te) {
		if(listaStato.isEmpty()){
			return;
		}
       Iterator ite = listaStato.iterator();
	   while(ite.hasNext()){
			ElementoStato id = (ElementoStato) ite.next();
			//controllo se lo stato di partenza gi? esiste
			if(id.getTipo() == ElementoStato.START){
				//	se lo stato di partenza esiste allora
				// il mio elemento assume lo stesso
				// identificatore di quello stato
				// e non viene aggiunto nella lista
				if(te.getListaStato().startExist()){
					id.setId(te.getListaStato().getStartState().getId());
					continue; 
					// non fare altro	
				}
			}
				id.setId(ElementoProcesso.newNumIstanze());
				id.setName("S"+ id.getId());
				te.getListaStato().addElement(id.cloneStato());
		}

		ite = listaMess.iterator();
		while (ite.hasNext()) {
			ElementoMessaggio ec = (ElementoMessaggio) ite.next();
			ElementoStato from =(ElementoStato) te.getListaStato().getElementoById(ec.getElementFrom().getId());
			ElementoStato to =(ElementoStato)  te.getListaStato().getElementoById(ec.getElementTo().getId());
			ElementoMessaggio ecn = 
					  // new ElementoMessaggio(from,to, null);
			new ElementoMessaggio(
				te,
				from,
				to,
				ec.getTipo(),
				"!link_",
				null);    	   
					ecn.setValue(ec, false);   
			        ecn.setName("!link_" + ecn.getId());
					te.getListaMessaggio().addElement(ecn);
		}

	}


	public void cut(ThreadElement te) {
		copy(te);
		//cancellazzione elementi selezionati
	}

}
