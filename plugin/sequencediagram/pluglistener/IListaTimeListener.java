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
    

package plugin.sequencediagram.pluglistener;

import java.io.Serializable;
import java.util.EventListener;

import plugin.sequencediagram.eventi.listaTime.AddTimeEvento;
import plugin.sequencediagram.eventi.listaTime.RemoveTimeEvento;
import plugin.sequencediagram.eventi.listaTime.UpdateTimeEvento;


/**
 * IListaTimeListener � l'interface listener per ricevere la notifica quando una 
 * ListaTime �  modificata, attraverso l'inserimento o la cancellazione di un
 * ElementoTime l'implementazione di questa interfaccia deve essere registrata
 * nel metodo ListaTime.addListener() per ricevere la notifica.  
 *
 *<p>
 *
 * Ci sono 3 metodi di interfaccia, rappresentanti una combinazione di
 * (add/remove/update)ElementoTime
 *
 *<p>
 *
 * @author Michele Stoduto
 * @version Charmy 2.1
 */
public interface IListaTimeListener extends EventListener, Serializable {

	/**
	 * Chiamata quando un ElementoTime � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void timeAdded(AddTimeEvento event);

	/**
	 * Chiamata quando un ElementoTime � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void timeRemoved(RemoveTimeEvento event);

	/**
	 * Chiamata quando un ElementoTime � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void timeUpdate(UpdateTimeEvento event);

	/**
	 * Chiamata quando si necessit� di un refresh globale
	 */
	public void timeRefresh();

}
