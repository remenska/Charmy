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
    
package plugin.topologydiagram.eventi.listacanali;



import core.internal.runtime.data.eventi.SessioneEvento;
import plugin.topologydiagram.data.ElementoCanale;
import plugin.topologydiagram.data.ListaCanale;

/**
 * @author michele
 * Charmy plug-in
 **/
public class RemoveCanaleEvento extends SessioneEvento{
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	private ElementoCanale elementoCanale = null;


	/**
	 * Costruttore
	 * @param ep Elemento processo aggiunto
	 * @param lp lista al quale si aggiunge in processo
	 * @param Identificativo della sessione corrente
	 */
	public RemoveCanaleEvento(ElementoCanale ec, ListaCanale lc, long idSessione){
		super(lc, idSessione);
		elementoCanale = ec;
		this.idSessione = idSessione;
	}


	/**
	 * @return
	 */
	public ElementoCanale getElementoCanale() {
		return elementoCanale;
	}

	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public ListaCanale getSorgente(){
		return (ListaCanale)super.getSource();
	}

}
