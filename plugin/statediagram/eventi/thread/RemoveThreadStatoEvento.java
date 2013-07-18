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
import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.data.ListaStato;
import plugin.statediagram.data.ThreadElement;

/**
 * evento generato in seguito alla rimozione (prima/dopo) di un ElementoStato nella lista
 * 
 * @author michele
 * Charmy plug-in
 **/
public class RemoveThreadStatoEvento extends SessioneEvento{

	/**
	 * riferimento all'elemento del processo che si rimuove
	 */
	private ElementoStato elementoStato = null;
	private ListaStato 		listaStato = null;

	
	/**
	 * Costruttore
	 * @param ep Elemento processo rimosso
 	 * @param lp lista al quale si rimuove il processo
 	 * @param te ThreadElement contenente la listaStato(è la source dell'evento)
 	 * @param Identificativo della sessione corrente
	 */
	public RemoveThreadStatoEvento(ElementoStato ep, ListaStato lp, ThreadElement te, long idSessione){
		super(te, idSessione);
		listaStato = lp;
		elementoStato = ep;
	}

	/**
	 * restituisce l'elemento stato rimosso
	 * @return
	 */
	public ElementoStato getElementoStato() {
		return elementoStato;
	}

	
	/**
	 * restituisce la listaStato generante l'evento
	 * @return
	 */
	public ListaStato getListaStato() {
		return listaStato;
	}
	/**
	 * restituisce la sorgente del messaggio
	 */
	public ThreadElement getThreadElement(){
		return (ThreadElement)getSource();
	}

}
