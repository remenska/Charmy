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


import plugin.sequencediagram.data.ElementoClasse;
import plugin.sequencediagram.data.ListaClasse;
import plugin.sequencediagram.data.ListaDS;
import plugin.sequencediagram.data.SequenceElement;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author michele
 * Charmy plug-in
 * evento generato da SequenceElement reletivo all'update di una classe
 **/
public class UpdateListaDSClasseEvento 
	extends SessioneEvento {
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	private ElementoClasse oldElementoClasse = null;
	private ElementoClasse nuovoElementoClasse = null;
	private ListaClasse listaClasse = null;
	private SequenceElement sequenceElement;
	
	/**
	 * Costruttore
	 * @param oldec vecchio ElementoClasse
	 * @param newec nuovo ElementoClassse
	 * @param lc listaclasse contenitore 
	 * @param se SequenceElement contenitore
	 * @param lds lista ds generante l'evento
	 * @param idSessione sessione del messaggio
	 */
	public UpdateListaDSClasseEvento(ElementoClasse oldec, ElementoClasse newec, ListaClasse lc, SequenceElement se,ListaDS lds,  long idSessione) {
		super(lds, idSessione);
		nuovoElementoClasse = newec;
		oldElementoClasse = oldec;
		listaClasse = lc;
		sequenceElement = se;
	}

	/**
	 * nuovo canale (dopo la modifica)
	 * @return
	 */
	public ElementoClasse getNuovoElementoClasse() {
		return nuovoElementoClasse;
	}

	/**
	 * vecchio canale(prima della modifica)
	 * @return
	 */
	public ElementoClasse getVecchioElementoClasse() {
		return oldElementoClasse;
	}
	/**
	 * preleva la lista contentente il nuovo elemento
	 * @return lista nuovo elemento
	 */
	public ListaClasse getListaClasse() {
		return listaClasse;
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

