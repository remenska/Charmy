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

import plugin.sequencediagram.eventi.listaParallel.AddParallelEvento;
import plugin.sequencediagram.eventi.listaParallel.RemoveParallelEvento;
import plugin.sequencediagram.eventi.listaParallel.UpdateParallelEvento;

/**
 *Abilita il modulo che implementa l'interfaccia a ricevere la notifica relativa al 
 *cambiamento di una ListaParallel, attraverso l'inserimento o la cancellazione o modifica 
 *di un ElementoParallel. 
 *La classe che implementa questa interfaccia deve essere registrata attraverso il metodo 
 *ListaParallel.addListener() per avviare il processo di notifica.  
 *
 *Ci sono 3 metodi di interfaccia, rappresentanti una combinazione di add/remove/update.
 *
 *
 * @author Flamel
 * @version Charmy 2.1
 */
public interface IListaParallelListener extends EventListener, Serializable {

	/**
	 * Chiamata quando un ElementoParallel viene  inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void parallelAdded(AddParallelEvento event);

	/**
	 * Chiamata quando un ElementoParallel � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void parallelRemoved(RemoveParallelEvento event);

	/**
	 * Chiamata quando un oggetto ElementoParallel contenuto nella ListaParallel 
	 * viene modificato
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void parallelUpdate(UpdateParallelEvento event);

	/**
	 * Chiamata quando si necessit� di un refresh globale
	 */
	public void parallelRefresh();

}

