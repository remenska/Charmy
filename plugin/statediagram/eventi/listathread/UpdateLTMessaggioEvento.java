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
import plugin.statediagram.data.ListaMessaggio;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.graph.ElementoMessaggio;

/**
 * @author michele
 * Charmy plug-in
 **/
public class UpdateLTMessaggioEvento extends SessioneEvento {
	/**
	 * riferimento per il messaggio che si aggiunge
	 */
	private ElementoMessaggio nuovoElementoMessaggio;
	private ElementoMessaggio vecchioElementoMessaggio;
	
	private ListaMessaggio listaMessaggio;
	private ThreadElement threadElement;

	/**
	 * Costruttore
	 * @param elementoMessaggio  clone dell'elementoMessaggio aggiunto prima della modifica
	 * @param clone nuovo elemento dopo la modifica
	 * @param listaMessaggio lista dove si � aggiunto il messaggio
	 * @param threadElement dove � conservata la lista
	 * @param ListaThread listaDeiThread
	 * @param idSessione identificativo della sessione del messaggio
	 */

	public UpdateLTMessaggioEvento(
		ElementoMessaggio vecchioElementoMessaggio,
	    ElementoMessaggio nuovoElementoMessaggio,
		
		ListaMessaggio listaMessaggio,
		ThreadElement threadElement,
		ListaThread lt,
		long idSessione) {

		super(lt, idSessione);

		this.nuovoElementoMessaggio = nuovoElementoMessaggio;
		this.vecchioElementoMessaggio = vecchioElementoMessaggio;
		
		this.listaMessaggio = listaMessaggio;
		this.threadElement = threadElement;

	}

	/**
	 * restituisce la sorgente del messaggio
	 */
	public ListaThread getSorgente() {
		return (ListaThread) getSource();
	}

	/**
	 * ritorna il clone dell'elemento dopo l a modifica
	 * @return clone ElementoMessaggio
	 */
	public ElementoMessaggio getNuovoElementoMessaggio() {
		return nuovoElementoMessaggio;
	}

	/**
	 * ritorna il clone dell'elemento prima della modifica
	 * @return clone ElementoMessaggio
	 */
	public ElementoMessaggio getVecchioElementoMessaggio() {
		return vecchioElementoMessaggio;
	}

	/**
	 * lista dove � stato aggiunto il messaggio
	 * @return
	 */
	public ListaMessaggio getListaMessaggio() {
		return listaMessaggio;
	}

	/**
	 * ThreadElement dove � stato aggiunto il messaggio
	 * @return
	 */
	public ThreadElement getThreadElement() {
		return threadElement;

	}
}
