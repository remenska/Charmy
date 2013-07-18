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
    

package plugin.topologydiagram.pluglistener;

import java.io.Serializable;
import java.util.EventListener;

import plugin.topologydiagram.eventi.listacanali.AddCanaleEvento;
import plugin.topologydiagram.eventi.listacanali.RemoveCanaleEvento;
import plugin.topologydiagram.eventi.listacanali.UpdateCanaleEvento;

/**
 * IListaCanaleListener è l'interface listener per ricevere la notifica quando una 
 * ListaCanale è  modificata, attraverso l'inserimento o la cancellazione di un
 * ElementoCanale l'implementazione di questa interfaccia deve essere registrata
 * nel metodo ListaCanale.addListener() per ricevere la notifica.  
 *
 *<p>
 *
 * Ci sono 3 metodi di interfaccia, rappresentanti una combinazione di
 * (add/remove/update)CanaleProcesso
 *
 *<p>
 *
 * @author Michele Stoduto
 * @version Charmy 2.1
 */
public interface IListaCanaleListener extends EventListener, Serializable {

	/**
	 * Chiamata quando un ElementoProcesso è stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void canaleAdded(AddCanaleEvento event);

	/**
	 * Chiamata quando un ElementoProcesso è stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void canaleRemoved(RemoveCanaleEvento event);

	/**
	 * Chiamata quando un ElementoProcesso è stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void canaleUpdate(UpdateCanaleEvento event);

	/**
	 * Chiamata quando un ElementoProcesso è stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void canaleRefresh();

}
