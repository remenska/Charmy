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
    
package plugin.sequencediagram.eventi.listaLoop;



import plugin.sequencediagram.data.ElementoLoop;
import plugin.sequencediagram.data.ListaLoop;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author flamel
 * Charmy plug-in
 * Rimozione di un operatore loop
 **/
public class RemoveLoopEvento extends SessioneEvento{
	/**
	 * riferimento all'operatore che si rimuove
	 */
	private ElementoLoop elementoLoop = null;


	/**
	 * Costruttore
	 * @param ep ElementoLoop rimosso
	 * @param lp lista 
	 * @param Identificativo della sessione corrente
	 */
	public RemoveLoopEvento(ElementoLoop ec, ListaLoop lc, long idSessione){
		super(lc, idSessione);
		elementoLoop = ec;
		this.idSessione = idSessione;
	}


	/**
	 * @return l'elementoLoop
	 */
	public ElementoLoop getElementoLoop() {
		return elementoLoop;
	}

	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public ListaLoop getSorgente(){
		return (ListaLoop)super.getSource();
	}

}

