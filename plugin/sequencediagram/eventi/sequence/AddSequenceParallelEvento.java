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


import plugin.sequencediagram.data.ElementoParallelo;
import plugin.sequencediagram.data.ListaParallel;
import plugin.sequencediagram.data.SequenceElement;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author flamel 2005
 * Charmy plug-in
 * Aggiunta di un operatore parallelo
 **/
public class AddSequenceParallelEvento 
	extends SessioneEvento{
	/**
	 * riferimento all'elemento che si aggiunge
	 */
	private ElementoParallelo elementoParallel = null;
	private ListaParallel listaParallel; 	


	/**
	 * Costruttore
	 * @param ec ElementoParallelo aggiunto
	 * @param lc lista al quale si aggiunge L'elemento
	 * @SequenceElement contenente il tutto
	 * @param idDiSessione 
	 */
	public AddSequenceParallelEvento(ElementoParallelo ec, ListaParallel lc, SequenceElement se, long idSessione){
		super(se, idSessione);
		elementoParallel = ec;
		listaParallel = lc;
	}


	/**
	 * ritorna il clone dell'elemento aggiunto
	 * @return
	 */
	public ElementoParallelo getElementoParallel() {
		return elementoParallel;
	}
	
	
	/**
	 * preleva ila lista contentne l'elemento
	 * @return 
	 */
	public ListaParallel getListaParallel() {
		return listaParallel;
	}

	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public SequenceElement getSorgente() {
		return (SequenceElement) super.getSource();
	}

	
}
