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
import plugin.statediagram.data.ListaMessaggio;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.graph.ElementoMessaggio;

/**
 * @author michele
 * Charmy plug-in
 **/
public class UpdateDPMessaggioEvento extends SessioneEvento {
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	private ElementoMessaggio nuovoElementoMessaggio;
	private ElementoMessaggio vecchioElementoMessaggio;
	
	private ListaMessaggio listaMessaggio;
	private ThreadElement threadElement;
	private ListaThread listaThread;
	
	/**
	 * Costruttore
	 * @param oldec Elemento messaggio scatenante l'evento
	 * @param newec nuova versione messaggio
	 * @param lc lista al quale si aggiunge l'elemento
	 * @param te thread element contenente la lista
	 * @param listaThread 
	 * @param ldp sorgente dell'evento
	 * @param idSessione, identificativo della sessione corrente
	 */
	public UpdateDPMessaggioEvento(
	    ElementoMessaggio oldec,
		ElementoMessaggio newec,
		ListaMessaggio lc, 
		ThreadElement te,
		ListaThread lt,
		ListaDP ldp, long idSessione) {
		super(ldp, idSessione);		
		nuovoElementoMessaggio = newec;
		vecchioElementoMessaggio = oldec;
		
		listaMessaggio = lc;
		threadElement = te;
		listaThread = lt;
	}


	/**
	 * listaMessaggio
	 * @return
	 */
	public ListaMessaggio getListaMessaggio() {
		return listaMessaggio;
	}

	/**
	 * listaThread
	 * @return
	 */
	public ListaThread getListaThread() {
		return listaThread;
	}

	/**
	 * threadElement
	 * @return
	 */
	public ThreadElement getThreadElement() {
		return threadElement;
	}
	/**
	 * restituisce la sorgente del messaggio
	 */
	public ListaDP getListaDP(){
		return (ListaDP)getSource();
	}
	/**
	 * ritorna il clone della nuova versione del messaggio
	 * @return
	 */
	public ElementoMessaggio getNuovoElementoMessaggio() {
		return nuovoElementoMessaggio;
	}

	/**
	 * clone della precedente versione del messaggio
	 * @return
	 */
	public ElementoMessaggio getVecchioElementoMessaggio() {
		return vecchioElementoMessaggio;
	}

}
