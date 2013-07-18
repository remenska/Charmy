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

import plugin.statediagram.eventi.thread.AddThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.AddThreadStatoEvento;
import plugin.statediagram.eventi.thread.RemoveThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.RemoveThreadStatoEvento;
import plugin.statediagram.eventi.thread.UpdateThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.UpdateThreadStatoEvento;

/**
 * IListaStatListener è l'interface listener per ricevere la notifica quando una 
 * ListaThread è  modificata, attraverso l'inserimento o la cancellazione di un
 * ThreadElemento l'implementazione di questa interfaccia deve essere registrata
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
public interface IThreadElementListener  extends EventListener, Serializable{
	/**
	 * Chiamata quando un ElementoStatosta per essere inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 *
	 * @exception the listener may throw any Exception, which will be
	 * propagated to the code which invoked the original graph modification
	 * method
	 */
	public void statoAdded(AddThreadStatoEvento event);
//		throws Exception;
    
	/**
	 * Chiamata quando un ElementoStato è stato inserito nella lista
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
	public void statoRemoved(RemoveThreadStatoEvento event);
//		throws Exception;
    
	/**
	 * Chiamata quando un ElementoStato  è stato rimosso dalla lista
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
	public void statoUpdate(UpdateThreadStatoEvento event);
//		throws Exception;
    
	/**
	 * Chiamata quando un ElementoStato è stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	//public void dopoEPUpdate(UpdateEPEvento event);


	/**
	 * richiamata quando si ha necessità di avvertire i listener
	 * di un generico refresh
	 * adesso viene chiamata quando la classe viene
	 * cancellata o reimpostata in plugData
	 * è necessaria nelle operazioni di copia-incolla etc
	 */
	//public void statoRefresh();



	/**
	 * Chiamata quando un Elemento è stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void messaggioAdded(AddThreadMessaggioEvento event);

	/**
	 * Chiamata quando un Elemento è stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void messaggioRemoved(RemoveThreadMessaggioEvento event);

	/**
	 * Chiamata quando un Elemento è stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void messaggioUpdate(UpdateThreadMessaggioEvento event);

	/**
	 * Chiamata quando un Elemento è stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void threadRefresh();

}
