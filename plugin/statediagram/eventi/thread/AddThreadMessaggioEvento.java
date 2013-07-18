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
    

package plugin.statediagram.eventi.thread;


import core.internal.runtime.data.eventi.SessioneEvento;
import plugin.statediagram.data.ListaMessaggio;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.graph.ElementoMessaggio;


/**
 * messaggio generato per l'aggiunta di un messaggio nella lista dei thread
 * @author michele
 * Charmy plug-in
 **/
public class AddThreadMessaggioEvento extends SessioneEvento {
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	private ElementoMessaggio elementoMsg = null;
	private ListaMessaggio listaMessaggio = null;
	/**
	 * Costruttore
	 * @param ep Elemento messaggio aggiunto
	 * @param lm lista al quale si aggiunge in processo
	 * @param te ThreadElement contenente la listaStato(è la source dell'evento)
	 */
	public AddThreadMessaggioEvento(ElementoMessaggio em, ListaMessaggio lm, ThreadElement te, long idSessione){
		super(te, idSessione);
		listaMessaggio = lm;
		elementoMsg = em;
	}


	/**
	 * preleva l'elemento Messaggio aggiunto
 	 * @return
	 */
	public ElementoMessaggio getElementoMessaggio() {
		return elementoMsg;
	}



	/**
	 * restituisce la lista contenente l'elemento del messaggio
	 * @return
	 */
	public ListaMessaggio getListaMessaggio() {
		return listaMessaggio;
	}

	/**
	 * restituisce la sorgente del messaggio
	 */
	public ThreadElement getThreadElement(){
		return (ThreadElement)getSource();
	}
	

}
