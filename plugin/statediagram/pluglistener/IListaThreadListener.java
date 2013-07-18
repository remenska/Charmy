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
    

package plugin.statediagram.pluglistener;

import java.io.Serializable;
import java.util.EventListener;

import plugin.statediagram.eventi.listathread.AddLTMessaggioEvento;
import plugin.statediagram.eventi.listathread.AddLTStatoEvento;
import plugin.statediagram.eventi.listathread.AddThreadEvento;
import plugin.statediagram.eventi.listathread.RemoveLTMessaggioEvento;
import plugin.statediagram.eventi.listathread.RemoveLTStatoEvento;
import plugin.statediagram.eventi.listathread.RemoveThreadEvento;
import plugin.statediagram.eventi.listathread.UpdateLTMessaggioEvento;
import plugin.statediagram.eventi.listathread.UpdateLTStatoEvento;

/**
 * IListaStatListener � l'interface listener per ricevere la notifica quando una 
 * ListaStato �  modificata, attraverso l'inserimento o la cancellazione di un
 * ElementoStato l'implementazione di questa interfaccia deve essere registrata
 * nel metodo ListaStato.addListener() per ricevere la notifica.  
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
public interface IListaThreadListener  extends EventListener, Serializable{
	/**
	 * Chiamata quando un ElementoStatosta per essere inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 *
	 * @exception the listener may throw any Exception, which will be
	 * propagated to the code which invoked the original graph modification
	 * method
	 */
	public void statoAdded(AddLTStatoEvento event);
//		throws Exception;
    
	/**
	 * Chiamata quando un ElementoStato � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
//	public void dopoEPAdded(AddEPEvento event);


	/**
	 * Chiamata quando un ElementoStato sta per essere rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 *
	 * @exception the listener may throw any Exception, which will be
	 * propagated to the code which invoked the original graph modification
	 * method
	 */
	public void statoRemoved(RemoveLTStatoEvento event);
//		throws Exception;
    
	/**
	 * Chiamata quando un ElementoStato  � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
//	public void dopoEPRemoved(RemoveEPEvento event);


	/**
	 * Chiamata quando un ElementoStato sta per essere modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 *
	 * @exception the listener may throw any Exception, which will be
	 * propagated to the code which invoked the original graph modification
	 * method
	 */
	public void statoUpdate(UpdateLTStatoEvento event);
//		throws Exception;
    
	/**
	 * Chiamata quando un ElementoStato � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	//public void dopoEPUpdate(UpdateEPEvento event);


	/**
	 * richiamata quando si ha necessit� di avvertire i listener
	 * di un generico refresh
	 * adesso viene chiamata quando la classe viene
	 * cancellata o reimpostata in plugData
	 * � necessaria nelle operazioni di copia-incolla etc
	 */
	//public void statoRefresh();



	/**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void messaggioAdded(AddLTMessaggioEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void messaggioRemoved(RemoveLTMessaggioEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void messaggioUpdate(UpdateLTMessaggioEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 */
	public void listaThreadRefresh();


	/**
	 * Chiamata quando un Elemento � aggiunto alla lista
	 */
	public void threadAdded(AddThreadEvento ate);
	
	/**
	 * chiamate quando un thread � rimosso dalla lista
	 * @param rte
	 */
	
	public void threadRemoved(RemoveThreadEvento rte);
	
	/**
	 * chiamata quando un thread � aggiornato
	 * @param ute
	 */
//	public void threadUpdated(UpdateThreadEvento ute);
	



}
