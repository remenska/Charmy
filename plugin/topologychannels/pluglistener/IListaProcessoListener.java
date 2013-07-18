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
    
package plugin.topologychannels.pluglistener;

import java.io.Serializable;
import java.util.EventListener;

import plugin.topologychannels.eventi.listaprocesso.AddEPEvento;
import plugin.topologychannels.eventi.listaprocesso.RemoveEPEvento;
import plugin.topologychannels.eventi.listaprocesso.UpdateEPEvento;

/**
 * IListaProcessoListener è l'interface listener per ricevere la notifica quando una 
 * ListaProcesso è  modificata, attraverso l'inserimento o la cancellazione di un
 * ElementoProcesso l'implementazione di questa interfaccia deve essere registrata
 * nel metodo ListaProcesso.addListener() per ricevere la notifica.  
 *
 *<p>
 *
 * Ci sono quattro metodi di interfaccia, rappresentanti una combinazione di
 * (prima/dopo)(add/remove)ElementoProcesso
 *
 *<p>
 *
 * @author Michele Stoduto
 * @version Charmy 2.1
 */
public interface IListaProcessoListener  extends EventListener, Serializable{
	/**
	 * Chiamata quando un ElementoProcesso sta per essere inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 *
	 * @exception the listener may throw any Exception, which will be
	 * propagated to the code which invoked the original graph modification
	 * method
	 */
	public void processoAdded(AddEPEvento event);
//		throws Exception;
    
	/**
	 * Chiamata quando un ElementoProcesso è stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
//	public void dopoEPAdded(AddEPEvento event);


	/**
	 * Chiamata quando un ElementoProcesso sta per essere rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 *
	 * @exception the listener may throw any Exception, which will be
	 * propagated to the code which invoked the original graph modification
	 * method
	 */
	public void processoRemoved(RemoveEPEvento event);
//		throws Exception;
    
	/**
	 * Chiamata quando un ElementoProcesso è stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
//	public void dopoEPRemoved(RemoveEPEvento event);


	/**
	 * Chiamata quando un ElementoProcesso sta per essere modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 *
	 * @exception the listener may throw any Exception, which will be
	 * propagated to the code which invoked the original graph modification
	 * method
	 */
	public void processoUpdate(UpdateEPEvento event);
//		throws Exception;
    
	/**
	 * Chiamata quando un ElementoProcesso è stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	//public void dopoEPUpdate(UpdateEPEvento event);


	/**
	 * richiamata quando si ha necessità di avvertire i listener
	 * di un generico refresh
	 * adesso viene chiamata quando ls classe viene
	 * cancellata o reimpostata in plugData
	 * è necessaria nelle operazioni di copia-incolla etc
	 */
	public void processoRefresh();


}
