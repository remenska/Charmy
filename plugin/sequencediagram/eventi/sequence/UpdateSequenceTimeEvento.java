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
    
package plugin.sequencediagram.eventi.sequence;


import plugin.sequencediagram.data.ElementoTime;
import plugin.sequencediagram.data.ListaTime;
import plugin.sequencediagram.data.SequenceElement;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author michele
 * Charmy plug-in
 * Update di una linea di tempo, il numero massimo di linee � 11, il minimo 3
 **/
public class UpdateSequenceTimeEvento 
	extends SessioneEvento {
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	private ElementoTime oldElementoTime = null;
	private ElementoTime nuovoElementoTime = null;
	private ListaTime listaTime;

	/**
	 * Costruttore
	 * @param oldec ElementoTime aggiunto prima della modifica
	 * @param newec ElementoTime dopo la modifica
	 * @param lp lista nel quale l'elemento � modificato
	 * @param Identificativo della sessione corrente
	 */
	public UpdateSequenceTimeEvento(ElementoTime oldec, ElementoTime newec, ListaTime lc,SequenceElement se,  long idSessione) {
		super(se, idSessione);
		nuovoElementoTime = newec;
		oldElementoTime = oldec;
		listaTime = lc;
	}

	/**
	 * nuovo ElementoTime (dopo la modifica)
	 * @return
	 */
	public ElementoTime getNuovoElementoTime() {
		return nuovoElementoTime;
	}

	/**
	 * vecchio ElementoTime(prima della modifica)
	 * @return
	 */
	public ElementoTime getVecchioElementoTime() {
		return oldElementoTime;
	}
	/**
	 * preleva la lista contenente l'elemento
	 * @return lista generatrice dell'evento
	 */
	public ListaTime getListaTime() {
		return listaTime;
	}
	
	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public SequenceElement getSorgente() {
		return (SequenceElement) super.getSource();
	}
}

