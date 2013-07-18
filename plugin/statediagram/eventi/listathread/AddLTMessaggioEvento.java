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
 * messaggio generato per l'aggiunta di un Elemento nella lista dei thread
 * @author michele
 * Charmy plug-in
 **/
public class AddLTMessaggioEvento extends SessioneEvento {

	/**
	 * riferimento per il messaggio che si aggiunge
	 */
	private ElementoMessaggio elementoMessaggio;
	private ListaMessaggio listaMessaggio;
	private ThreadElement threadElement;

	/**
	 * Costruttore
	 * @param elementoMessaggio  clone dell'elementoMessaggio aggiunto
	 * @param listaMessaggio lista dove si è aggiunto il messaggio
	 * @param threadElement dove è conservata la lista
	 * @param ListaThread listaDeiThread
	 * @param idSessione identificativo della sessione del messaggio
	 */	
	public AddLTMessaggioEvento(
		ElementoMessaggio elementoMessaggio,
		ListaMessaggio listaMessaggio,
		ThreadElement threadElement,
		ListaThread lt,
		long idSessione) {
			
		super(lt, idSessione);
		this.elementoMessaggio = elementoMessaggio;
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
	 * ritorna il clone dell'elemento aggiunto
	 * @return clone ElementoMessaggio
	 */
	public ElementoMessaggio getElementoMessaggio() {
		return elementoMessaggio;
	}

	/**
	 * lista dove è stato aggiunto il messaggio
	 * @return
	 */
	public ListaMessaggio getListaMessaggio() {
		return listaMessaggio;
	}

	/**
	 * ThreadElement dove è stato aggiunto il messaggio
	 * @return
	 */
	public ThreadElement getThreadElement() {
		return threadElement;
	}

}
