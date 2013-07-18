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


import plugin.sequencediagram.data.ElementoSeqLink;
import plugin.sequencediagram.data.ListaDS;
import plugin.sequencediagram.data.ListaSeqLink;
import plugin.sequencediagram.data.SequenceElement;
import core.internal.runtime.data.eventi.SessioneEvento;

/**
 * @author michele
 * Charmy plug-in
 * evento generato da SequenceElement reletivo all'update di un link
 **/
public class UpdateListaDSLinkEvento extends SessioneEvento {
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	private ElementoSeqLink oldElementoSeqLink = null;
	private ElementoSeqLink nuovoElementoSeqLink = null;
	private ListaSeqLink listaSeqLink;
	private SequenceElement sequenceElement;


	/**
	 * 
	 * @param oldec
	 * @param newec
	 * @param lc
	 * @param se
	 * @param idSessione
	 */
	public UpdateListaDSLinkEvento(ElementoSeqLink oldec, ElementoSeqLink newec, ListaSeqLink lc, SequenceElement se,ListaDS lds, long idSessione) {
		super(lds, idSessione);
		nuovoElementoSeqLink = newec;
		oldElementoSeqLink = oldec;
		listaSeqLink = lc;
		sequenceElement = se;
	}

	/**
	 * nuovo SeqLink (dopo la modifica)
	 * @return
	 */
	public ElementoSeqLink getNuovoElementoSeqLink() {
		return nuovoElementoSeqLink;
	}

	/**
	 * vecchio SeqLink(prima della modifica)
	 * @return
	 */
	public ElementoSeqLink getVecchioElementoSeqLink() {
		return oldElementoSeqLink;
	}
	/**
	 * preleva la lista contenente l'evento
	 * @return lista generatrice dell'evento
	 */
	public ListaSeqLink getListaSeqLink() {
		return listaSeqLink;
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

