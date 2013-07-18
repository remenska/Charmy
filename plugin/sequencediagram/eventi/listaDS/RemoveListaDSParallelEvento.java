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
    
package plugin.sequencediagram.eventi.listaDS;


import plugin.sequencediagram.data.ElementoParallelo;
import plugin.sequencediagram.data.ListaDS;
import plugin.sequencediagram.data.ListaParallel;
import plugin.sequencediagram.data.SequenceElement;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author flamel
 * Charmy plug-in
 * Rimozione di una linea di tempo, il numero massimo di linee ? 11, il minimo 3
 * Evento generato da listaDS
 * 
 **/
public class RemoveListaDSParallelEvento extends SessioneEvento{
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	private ElementoParallelo elementoParallel = null;
	private ListaParallel listaParallel; 	
	private SequenceElement sequenceElement;

	/**
	 * Costruttore
	 * @param ec ElementoParallel aggiunto
	 * @param lc lista al quale si aggiunge L'elemento
	 * @SequenceElement contenente il tutto
	 * @param lds lista ds generante l'evento
	 * @param idDiSessione 
	 */
	public RemoveListaDSParallelEvento(ElementoParallelo ec, ListaParallel lc, SequenceElement se,ListaDS lds, long idSessione){
		super(lds, idSessione);
		elementoParallel = ec;
		listaParallel = lc;
		sequenceElement = se;
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
	 * SequencesElement
	 * @return SequencesElement
	 */
	public SequenceElement getSequenceElement() {
		return sequenceElement;
	}

	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public ListaDS getSorgente() {
		return (ListaDS) super.getSource();
	}
	
	
}


