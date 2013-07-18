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
    

package plugin.topologychannels.eventi.listaprocesso;


import core.internal.runtime.data.eventi.SessioneEvento;
import plugin.topologychannels.data.ElementoProcesso;
import plugin.topologychannels.data.ListaProcesso;

/**
 * evento generato in seguito all'aggiunta di un ElementoProcesso nella lista
 * 
 * @author michele
 * Charmy plug-in
 **/
public class AddEPEvento extends  SessioneEvento{

	/**
	 * riferimento all'elemento del processo che si aggiunge
	 */
	private ElementoProcesso elementoProcesso = null;


	
	/**
	 * Costruttore
	 * @param ep Elemento processo aggiunto
 	 * @param lp lista al quale si aggiunge in processo
 	 * @param idSessione identifica la sessione nel quale l'evento � stato mandato
	 */
	public AddEPEvento(ElementoProcesso ep, ListaProcesso lp, long idSessione){
		super(lp, idSessione);
		elementoProcesso = ep;
	}

	/**
	 * preleva il processo corrente, il processo � un clone di quello reale
	 * @return processo clone di quello aggiunto
	 */
	public ElementoProcesso getElementoProcesso() {
		return elementoProcesso;
	}

	/**
	 * preleva il generatore dell'evento
	 * @return lista generatrice dell'evento
	 */
	public ListaProcesso getSorgente() {
		return (ListaProcesso) super.getSource();
	}
	
}
