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
    

package plugin.sequencediagram.eventi.listaSim;


import plugin.sequencediagram.data.ElementoSim;
import plugin.sequencediagram.data.ListaSim;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author flamel 2005
 * Charmy plug-in
 * Update di un operatore simultaneo
 **/
public class UpdateSimEvento extends SessioneEvento {
	/**
	 * riferimento all'elemento 
	 */
	private ElementoSim oldElementoSim = null;
	private ElementoSim nuovoElementoSim = null;

	/**
	 * Costruttore
	 * @param oldec ElementoSim aggiunto prima della modifica
	 * @param newec ElementoSim dopo la modifica
	 * @param lp lista nel quale l'elemento è modificato
	 * @param Identificativo della sessione corrente
	 */
	public UpdateSimEvento(ElementoSim oldec, ElementoSim newec, ListaSim lc, long idSessione) {
		super(lc, idSessione);
		nuovoElementoSim = newec;
		oldElementoSim = oldec;
	}

	/**
	 * nuovo ElementoSim (dopo la modifica)
	 * @return
	 */
	public ElementoSim getNuovoElementoSim() {
		return nuovoElementoSim;
	}

	/**
	 * vecchio ElementoSim(prima della modifica)
	 * @return
	 */
	public ElementoSim getVecchioElementoSim() {
		return oldElementoSim;
	}
	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public ListaSim getSorgente() {
		return (ListaSim) super.getSource();
	}
	

}

