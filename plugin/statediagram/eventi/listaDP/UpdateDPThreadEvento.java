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
    
package plugin.statediagram.eventi.listaDP;

import core.internal.runtime.data.eventi.SessioneEvento;
import plugin.statediagram.data.ListaDP;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.ThreadElement;

/**
 * messaggio generato per la modifica di una listaThread di un messaggio nella lista dei thread
 * @author michele
 * Charmy plug-in
 **/
public class UpdateDPThreadEvento extends SessioneEvento {
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	ThreadElement oldThreadElement;
	ThreadElement newThreadElement;
	ListaThread listaThread;
	/**
	 * Costruttore
	 * @param olte ThreadElement contenente la listaStato(è la source dell'evento)
	 * @param newte ThreadElement contenente la listaStato(è la source dell'evento)
	 * @param listaThread contentente i dati
	 * @param listaDP sorgente dell'evento
	 * @param identificatore di sessione corrente
	 */
	public UpdateDPThreadEvento(
		ThreadElement oldte,
		ThreadElement newte,
		ListaThread lt,
		ListaDP ldp, long idSessione) {
		super(ldp, idSessione);
		listaThread = lt;
		oldThreadElement = oldte;
		newThreadElement = newte;
	}

	/**
	 * restituisce l'evento scatenante il messaggio
	 * @return
	 */

	public ThreadElement getNewThreadElement() {
		return newThreadElement;
	}

	/**
	 * restituisce l'evento scatenante il messaggio
	 * @return
	 */
	public ThreadElement getOldThreadElement() {
		return oldThreadElement;
	}

	/**
	 * restituisce la listaThread
	 * @return
	 */
	public ListaThread getListaThread() {
		return listaThread;
	}

	/**
	 * restituisce la sorgente del messaggio
	 */
	public ListaDP getListaDP(){
		return (ListaDP)getSource();
	}

}
