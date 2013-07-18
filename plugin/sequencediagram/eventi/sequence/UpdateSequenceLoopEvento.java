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


import plugin.sequencediagram.data.ElementoLoop;
import plugin.sequencediagram.data.ListaLoop;
import plugin.sequencediagram.data.SequenceElement;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author flamel 2005
 * Charmy plug-in
 * Update di un operatore loop
 **/
public class UpdateSequenceLoopEvento 
	extends SessioneEvento {
	/**
	 * riferimento all'elemento  che si aggiunge
	 */
	private ElementoLoop oldElementoLoop = null;
	private ElementoLoop nuovoElementoLoop = null;
	private ListaLoop listaLoop;

	/**
	 * Costruttore
	 * @param oldec ElementoLoop aggiunto prima della modifica
	 * @param newec ElementoLoop dopo la modifica
	 * @param lp lista nel quale l'elemento � modificato
	 * @param Identificativo della sessione corrente
	 */
	public UpdateSequenceLoopEvento(
			ElementoLoop oldec, 
			ElementoLoop newec, 
			ListaLoop lc,
			SequenceElement se,  
			long idSessione) {
		super(se, idSessione);
		nuovoElementoLoop = newec;
		oldElementoLoop = oldec;
		listaLoop = lc;
	}

	/**
	 * nuovo ElementoLoop (dopo la modifica)
	 * @return
	 */
	public ElementoLoop getNuovoElementoLoop() {
		return nuovoElementoLoop;
	}

	/**
	 * vecchio ElementoLoop(prima della modifica)
	 * @return
	 */
	public ElementoLoop getVecchioElementoLoop() {
		return oldElementoLoop;
	}
	/**
	 * preleva la lista contenente l'elemento
	 * @return lista generatrice dell'evento
	 */
	public ListaLoop getListaLoop() {
		return listaLoop;
	}
	
	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public SequenceElement getSorgente() {
		return (SequenceElement) super.getSource();
	}
}

