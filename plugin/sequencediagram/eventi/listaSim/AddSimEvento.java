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
 * Aggiunta di un operatore simultaneo
 **/
public class AddSimEvento extends SessioneEvento{
	/**
	 * riferimento all'elemento che si aggiunge
	 */
	ElementoSim elementoSim = null;



	/**
	 * Costruttore
	 * @param ep ElementoSim aggiunto
	 * @param lp lista al quale si aggiunge L'elemento
	 * @param idDiSessione 
	 */
	public AddSimEvento(ElementoSim ec, ListaSim lc, long idSessione){
		super(lc, idSessione);
		elementoSim = ec;
		
	}


	/**
	 * ritorna il clone dell'elemento aggiunto
	 * @return
	 */
	public ElementoSim getElementoSim() {
		return elementoSim;
	}
	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public ListaSim getSorgente() {
		return (ListaSim) super.getSource();
	}

	
}
