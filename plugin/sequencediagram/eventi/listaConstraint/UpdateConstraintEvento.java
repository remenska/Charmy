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
    

package plugin.sequencediagram.eventi.listaConstraint;


import plugin.sequencediagram.data.ElementoConstraint;
import plugin.sequencediagram.data.ListaConstraint;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author michele
 * Charmy plug-in
 * Update di una linea di tempo, il numero massimo di linee è 11, il minimo 3
 **/
public class UpdateConstraintEvento extends SessioneEvento {
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	private ElementoConstraint oldElementoConstraint = null;
	private ElementoConstraint nuovoElementoConstraint = null;

	/**
	 * Costruttore
	 * @param oldec ElementoConstraint aggiunto prima della modifica
	 * @param newec ElementoConstraint dopo la modifica
	 * @param lp lista nel quale l'elemento è modificato
	 * @param Identificativo della sessione corrente
	 */
	public UpdateConstraintEvento(ElementoConstraint oldec, ElementoConstraint newec, ListaConstraint lc, long idSessione) {
		super(lc, idSessione);
		nuovoElementoConstraint = newec;
		oldElementoConstraint = oldec;
	}

	/**
	 * nuovo ElementoConstraint (dopo la modifica)
	 * @return
	 */
	public ElementoConstraint getNuovoElementoConstraint() {
		return nuovoElementoConstraint;
	}

	/**
	 * vecchio ElementoConstraint(prima della modifica)
	 * @return
	 */
	public ElementoConstraint getVecchioElementoConstraint() {
		return oldElementoConstraint;
	}
	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public ListaConstraint getSorgente() {
		return (ListaConstraint) super.getSource();
	}
	

}

