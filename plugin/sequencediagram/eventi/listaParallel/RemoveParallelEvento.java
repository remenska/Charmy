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
 * Rimozione di un operatore parallelo
 **/
public class RemoveParallelEvento extends SessioneEvento{
	/**
	 * riferimento all'elemento che si rimuove
	 */
	private ElementoParallelo elementoParallel = null;


	/**
	 * Costruttore
	 * @param ep Elemento  aggiunto
	 * @param lp lista al quale si aggiunge in processo
	 * @param Identificativo della sessione corrente
	 */
	public RemoveParallelEvento(ElementoParallelo ec, ListaParallel lc, long idSessione){
		super(lc, idSessione);
		elementoParallel = ec;
		this.idSessione = idSessione;
	}


	/**
	 * @return l'elementoParallel
	 */
	public ElementoParallelo getElementoParallel() {
		return elementoParallel;
	}

	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public ListaParallel getSorgente(){
		return (ListaParallel)super.getSource();
	}

}

