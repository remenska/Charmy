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
    

package plugin.sequencediagram.utility.listaSequence;

import plugin.sequencediagram.data.ListaDS;
import plugin.sequencediagram.data.SequenceElement;

/**
 * Informazioni inserite in JListaSeqElement, permettono di conoscere
 * la lista di provenienza dell'elemento e l'elemento stesso
 * @author michele
 * Charmy plug-in
 **/
public class JListaSeqElementInfo {
		/**
		 * Lista Processo da controllare
		 */
		private ListaDS listaDS; 
		
		/**
		 * elemento processo nella lista
		 */
		private SequenceElement sequenceElement;
		
		public JListaSeqElementInfo(ListaDS lds, SequenceElement se){
			listaDS = lds;
			sequenceElement = se;
		}
		
		
		
		/**
		 * restituisce il SequenceElement
		 * @return
		 */
		public SequenceElement getSequenceElement() {
			return sequenceElement;
		}

		/**
		 * restituisce la lista coinvolta
		 * @return
		 */
		public ListaDS getListaDS() {
			return listaDS;
		}

		/**
		 * setta l'elemento interno
		 * @param SequenceElement
		 */
		public void setSequenceElement(SequenceElement se) {
			this.sequenceElement = se;
		}

		/**
		 * setta la lista che contiene l'elemento
		 * @param listaProcesso
		 */
		public void setListaDS(ListaDS listaDS) {
			this.listaDS = listaDS;
		}

}
