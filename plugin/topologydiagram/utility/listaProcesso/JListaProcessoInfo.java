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
    
package plugin.topologydiagram.utility.listaProcesso;

import plugin.topologydiagram.data.ElementoProcesso;
import plugin.topologydiagram.data.ListaProcesso;

/**
 * Informazioni inserite in JListaProcesso, permettono di conoscere
 * la lista di provenienza dell'elemento e l'elemento stesso
 * @author michele
 * Charmy plug-in
 **/
public class JListaProcessoInfo {
		/**
		 * Lista Processo da controllare
		 */
		private ListaProcesso listaProcesso;
		
		/**
		 * elemento processo nella lista
		 */
		private ElementoProcesso elementoProcesso;
		
		public JListaProcessoInfo(ListaProcesso lp, ElementoProcesso ep){
			listaProcesso = lp;
			elementoProcesso = ep;
		}
		
		
		
		/**
		 * restituisce l'elemento processo
		 * @return
		 */
		public ElementoProcesso getElementoProcesso() {
			return elementoProcesso;
		}

		/**
		 * restituisce la lista coinvolta
		 * @return
		 */
		public ListaProcesso getListaProcesso() {
			return listaProcesso;
		}

		/**
		 * setta l'elemento interno
		 * @param elementoProcesso
		 */
		public void setElementoProcesso(ElementoProcesso elementoProcesso) {
			this.elementoProcesso = elementoProcesso;
		}

		/**
		 * setta la lista che contiene l'elemento
		 * @param listaProcesso
		 */
		public void setListaProcesso(ListaProcesso listaProcesso) {
			this.listaProcesso = listaProcesso;
		}

}
