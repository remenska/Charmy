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
    

package plugin.sequencediagram.eventi.listaParallel;


import plugin.sequencediagram.data.ElementoParallelo;
import plugin.sequencediagram.data.ListaParallel;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author flamel 2005
 * Charmy plug-in
 * Update di un operatore parallelo
 **/
public class UpdateParallelEvento extends SessioneEvento {
	/**
	 * riferimento all'elemento 
	 */
	private ElementoParallelo oldElementoParallel = null;
	private ElementoParallelo nuovoElementoParallel = null;

	/**
	 * Costruttore
	 * @param oldec ElementoParallel aggiunto prima della modifica
	 * @param newec ElementoParallel dopo la modifica
	 * @param lp lista nel quale l'elemento è modificato
	 * @param Identificativo della sessione corrente
	 */
	public UpdateParallelEvento(ElementoParallelo oldec, ElementoParallelo newec, ListaParallel lc, long idSessione) {
		super(lc, idSessione);
		nuovoElementoParallel = newec;
		oldElementoParallel = oldec;
	}

	/**
	 * nuovo ElementoParallel (dopo la modifica)
	 * @return
	 */
	public ElementoParallelo getNuovoElementoParallel() {
		return nuovoElementoParallel;
	}

	/**
	 * vecchio ElementoParallel(prima della modifica)
	 * @return
	 */
	public ElementoParallelo getVecchioElementoParallel() {
		return oldElementoParallel;
	}
	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public ListaParallel getSorgente() {
		return (ListaParallel) super.getSource();
	}
	

}

