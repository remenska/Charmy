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
    

package plugin.statediagram.eventi.listathread;

import core.internal.runtime.data.eventi.SessioneEvento;
import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.data.ListaStato;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.ThreadElement;

/**
 * @author michele
 * Charmy plug-in
 **/
public class UpdateLTStatoEvento extends SessioneEvento{

	/**
	 * identificativo dei processi
	 */
	private ElementoStato vecchioElementoStato = null;
	private ElementoStato nuovoElementoStato = null;
	private ListaStato 		listaStato = null;
	private ThreadElement threadElement;

	/**
	 * Costruttore
	 * @param elementoStato  clone dell'elementoStatoaggiunto prima della modifica
	 * @param clone nuovo elemento dopo la modifica
	 * @param listaStato lista dove si è aggiunto lo Stato
	 * @param threadElement dove è conservata la lista
	 * @param ListaThread listaDeiThread
	 * @param idSessione identificativo della sessione del messaggio
	 */

	public UpdateLTStatoEvento(		ElementoStato oldep,
	ElementoStato newep,
	ListaStato lp,
	ThreadElement te,

		ListaThread lt, long idSessione) {
		super(lt, idSessione);
		vecchioElementoStato = oldep;
		nuovoElementoStato = newep;
		listaStato = lp;
		threadElement = te;

	}

	/**
	 * restituisce la sorgente del messaggio
	 */
	public ListaThread getSorgente(){
		return (ListaThread)getSource();
	}
	
	
	
	
	/**
	 * ritorna la listaStato dove è cambiato l'elemento
	 * @return
	 */
	public ListaStato getListaStato() {
		return listaStato;
	}

	/**
	 * clone del nuovo elemento
	 * @return
	 */
	public ElementoStato getNuovoElementoStato() {
		return nuovoElementoStato;
	}

	/**
	 * contenitore della listaStato
	 * @return
	 */
	public ThreadElement getThreadElement() {
		return threadElement;
	}

	/**
	 * clone dell'elemento precedente la modifica
	 * @return
	 */
	public ElementoStato getVecchioElementoStato() {
		return vecchioElementoStato;
	}

}
