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


import plugin.sequencediagram.data.ElementoTime;
import plugin.sequencediagram.data.ListaDS;
import plugin.sequencediagram.data.ListaTime;
import plugin.sequencediagram.data.SequenceElement;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author michele
 * Charmy plug-in
 * Update di una linea di tempo, il numero massimo di linee � 11, il minimo 3
 **/
public class UpdateListaDSTimeEvento extends SessioneEvento {
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	private ElementoTime oldElementoTime = null;
	private ElementoTime nuovoElementoTime = null;
	private ListaTime listaTime;
	private SequenceElement sequenceElement;
	/**
	 * Costruttore
	 * @param oldec ElementoTime aggiunto prima della modifica
	 * @param newec ElementoTime dopo la modifica
	 * @param lp lista nel quale l'elemento � modificato
	 * @param lds lista ds generante l'evento
	 * @param Identificativo della sessione corrente
	 */
	public UpdateListaDSTimeEvento(ElementoTime oldec, ElementoTime newec, ListaTime lc,SequenceElement se,ListaDS lds,  long idSessione) {
		super(lds, idSessione);
		nuovoElementoTime = newec;
		oldElementoTime = oldec;
		listaTime = lc;
		sequenceElement = se;
	}

	/**
	 * nuovo ElementoTime (dopo la modifica)
	 * @return
	 */
	public ElementoTime getNuovoElementoTime() {
		return nuovoElementoTime;
	}

	/**
	 * vecchio ElementoTime(prima della modifica)
	 * @return
	 */
	public ElementoTime getVecchioElementoTime() {
		return oldElementoTime;
	}
	/**
	 * preleva la lista contenente l'elemento
	 * @return lista generatrice dell'evento
	 */
	public ListaTime getListaTime() {
		return listaTime;
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

