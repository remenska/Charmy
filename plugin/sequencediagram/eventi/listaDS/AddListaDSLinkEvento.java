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
 * evento generato dalla SequenceElement
 **/
public class AddListaDSLinkEvento extends SessioneEvento{
	/**
	 * riferimento all'elemento del canale che si aggiunge
	 */
	private ElementoSeqLink elementoSeqLink = null;
	private ListaSeqLink listaSeqLink = null;
	private SequenceElement sequenceElement;

	/**
	 * Costruttore
	 * @param ep Elemento processo aggiunto
	 * @param lc lista al quale si aggiunge in processo
	 * @param se sequenceElement coinvolto nella modifica
	 * @param idDiSessione 
	 */
	public AddListaDSLinkEvento(ElementoSeqLink ec, ListaSeqLink lc, SequenceElement se,ListaDS lds, long idSessione){
		super(lds, idSessione);
		elementoSeqLink = ec;
		listaSeqLink = lc;	
		sequenceElement = se;
	}


	/**
	 * ritorna il clone dell'elemento aggiunto
	 * @return
	 */
	public ElementoSeqLink getElementoSeqLink() {
		return elementoSeqLink;
	}
	
	/**
	 * preleva la lista Classe modificata
	 * @return listaClasse modificata
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
