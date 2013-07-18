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
    
package plugin.topologydiagram.general.edcp;


import java.util.Iterator;

import core.resources.general.edcp.CopyCutPaste;

import plugin.topologydiagram.data.ElementoCanale;
import plugin.topologydiagram.data.ElementoProcesso;
import plugin.topologydiagram.data.PlugData;
import plugin.topologydiagram.resource.general.edcp.LinkedListWithID;

/**
 * @author michele
 * Implementazione di un sistema di copy/cut/paste per quanto riguarda
 * le ListeProcesso e ListaCanale.
 * Tale classe può essere instanziata ed utilizzata in ogni contesto
 * di editing attraverso i plug.
 * Spiegazione del funzionamento:
 * Copy
 * 1. prelevo una lista di cloni degli elementi processo selezionati
 * 2. dalla lista dei cloni creo una lista dei cloni di canale sole se selezionati
 *     e con entrambi i nodi all'inteno della lista cloni ElementoProcesso
 * Paste.
 * 1. Controllo se la lista dei cloni ElementiProcesso è piena
 * 	se è piena allora
 * 	1.a Nuova numerazione dell'elementI clone
 * 	1.a Nuova numerazione dell'elementoMessaggio
 * 2.Inserimento della nuova listaProcesso vera
 * 3. Inserimento canali
 * NB. la selezione di un canale senza entrambe i nodi selezionati implica che il canale non viene
 * copiato.
 * Inoltre l'inserimento, la cancellazzione dalle liste interne di questa
 * classe non genera nessun particolare tipo di messaggio.
 **/
public class CopyCutPasteTopology extends CopyCutPaste {

	private LinkedListWithID listaProc = null;
	private LinkedListWithID listaCan = null;
	private PlugData pd;

	public CopyCutPasteTopology(PlugData pl) {
		super(pl.getPlugDataManager());
		pd=(PlugData)plugData.getPlugData("charmy.plugin.topology");

		listaProc = new LinkedListWithID();
		listaCan = new LinkedListWithID();
	}

	/* (non-Javadoc)
	 * @see general.edcp.CopyCutPaste#copy()
	 */
	public void copy() {
		/**
		 * pulizia precedenti copy
		 */

		listaProc.clear();
		/*
		 * processi selezionati
		 */
		Iterator ite = pd.getListaProcesso().iterator();
		while (ite.hasNext()) {
			ElementoProcesso ep = (ElementoProcesso) ite.next();
			if (ep.isSelected()) {
				listaProc.add(ep.cloneProcesso());
			}
		}

		/*
		 *scorrimento dei canali selezionati 
		 * e ne realizza il legame con la nuova lista di elementi
		 **/
		listaCan.clear();
		if(listaProc.isEmpty()){ // lista processi vuota
			return;
		}
		ite = pd.getListaCanale().iterator();
		while (ite.hasNext()) {
			ElementoCanale ec = (ElementoCanale) ite.next();
			if (ec.isSelected()) {
				if (listaProc.containing(ec.getElement_one().getId())
					&& listaProc.containing(ec.getElement_two().getId())) {
					//esistono entrambe i nodi
					ElementoCanale ecn = 
					   new ElementoCanale((ElementoProcesso)listaProc.getElementById(ec.getElementFrom().getId()),
					   (ElementoProcesso)listaProc.getElementById(ec.getElementTo().getId()),
					   true, ec.getId(), null);
					ecn.setValue(ec, true);   
					listaCan.add(ecn);	   
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see general.edcp.CopyCutPaste#paste()
	 */
	/**
	 * genera una nuova numerazione per i canali e i processi e li invia nel
	 * nelle opportune lista
	 */
	public void paste() {
		if(listaProc.isEmpty()){
			return;
		}
       Iterator ite = listaProc.iterator();
	   while(ite.hasNext()){
			ElementoProcesso id = (ElementoProcesso) ite.next();
		
			id.setId(ElementoProcesso.newNumIstanze());
			
			id.setName("process"+ id.getId());
			pd.getListaProcesso().addElement(id.cloneProcesso());
		}


		ite = listaCan.iterator();
		while (ite.hasNext()) {
			ElementoCanale ec = (ElementoCanale) ite.next();
			ElementoProcesso from = pd.getListaProcesso().getProcessoById(ec.getElementFrom().getId());
			ElementoProcesso to = pd.getListaProcesso().getProcessoById(ec.getElementTo().getId());
			ElementoCanale ecn = 
					   new ElementoCanale(from,to, null);
					ecn.setValue(ec, false);   
			        ecn.setName("Channel_" + ecn.getId());
					pd.getListaCanale().addElement(ecn);
		}


/*
		ite = listaCan.iterator();
		while(ite.hasNext()){
			 ElementoCanale id = (ElementoCanale) ite.next();
			 id.setId(ElementoCanale.newNumIstanze());
			 id.setName("Channel_" + id.getId());
			 plugData.getListaCanale().addElement(id);
		 }
*/
	}

	/* (non-Javadoc)
	 * @see general.edcp.CopyCutPaste#cut()
	 */
	public void cut() {
		copy();
		//cancellazzione elementi selezionati
		
		
	}

}
