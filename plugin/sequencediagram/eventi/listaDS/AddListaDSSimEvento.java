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


import plugin.sequencediagram.data.ElementoSim;
import plugin.sequencediagram.data.ListaDS;
import plugin.sequencediagram.data.ListaSim;
import plugin.sequencediagram.data.SequenceElement;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author flamel
 * Charmy plug-in
 * Aggiunta Sim operator,
 * Evento generato da listaDS
 * 
 **/
public class AddListaDSSimEvento 
	extends SessioneEvento{
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	private ElementoSim elementoSim = null;
	private ListaSim listaSim; 	
	private SequenceElement sequenceElement;

	/**
	 * Costruttore
	 * @param ec ElementoSim aggiunto
	 * @param lc lista al quale si aggiunge L'elemento
	 * @SequenceElement contenente il tutto
	 * @param lds lista ds generante l'evento
	 * @param idDiSessione 
	 */
	public AddListaDSSimEvento(ElementoSim ec, ListaSim lc, SequenceElement se,ListaDS lds, long idSessione){
		super(lds, idSessione);
		elementoSim = ec;
		listaSim = lc;
		sequenceElement = se;
	}


	/**
	 * ritorna il clone dell'elemento aggiunto
	 * @return
	 */
	public ElementoSim getElementoSim() {
		return elementoSim;
	}
	
	
	/**
	 * preleva ila lista contentne l'elemento
	 * @return 
	 */
	public ListaSim getListaSim() {
		return listaSim;
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

