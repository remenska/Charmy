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
    
package plugin.statediagram.data.delegate;

import java.util.Iterator;

import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.ThreadElement;

import plugin.statediagram.eventi.listathread.AddLTMessaggioEvento;
import plugin.statediagram.eventi.listathread.AddLTStatoEvento;
import plugin.statediagram.eventi.listathread.AddThreadEvento;
import plugin.statediagram.eventi.listathread.RemoveLTMessaggioEvento;
import plugin.statediagram.eventi.listathread.RemoveLTStatoEvento;
import plugin.statediagram.eventi.listathread.RemoveThreadEvento;
import plugin.statediagram.eventi.listathread.UpdateLTMessaggioEvento;
import plugin.statediagram.eventi.listathread.UpdateLTStatoEvento;
import plugin.statediagram.eventi.thread.AddThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.AddThreadStatoEvento;
import plugin.statediagram.eventi.thread.RemoveThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.RemoveThreadStatoEvento;
import plugin.statediagram.eventi.thread.UpdateThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.UpdateThreadStatoEvento;
import plugin.statediagram.pluglistener.IListaThreadListener;
import plugin.statediagram.pluglistener.IThreadElementListener;

/**
 * implementazione di un listener per la Lista di processo
 * viene utilizzata come delega per la classe ListaThread
 * in alcune funzione non ? necessario implementare lo startSessione()
 * in quanto la sessione viene prelevata dal messaggio generante l'evento
 * 
 * @author michele
 * Charmy plug-in
 **/
public class DelegateListaThreadListener
	extends DelegateBase
	implements IThreadElementListener {

	/**
	 * lista del processo di cui viene effettuata la delega
	 */
	private ListaThread listaThread;

	/**
	 * costruttore
	 * @param dati globali
	 * @param lp ListaThread
	 * 
	 */
	public DelegateListaThreadListener(IPlugData pd, ListaThread lp) {
		super(pd);
		listaThread = lp;

	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoAdded(eventi.thread.AddThreadStatoEvento)
	 */
	public void statoAdded(AddThreadStatoEvento event) {
		if (size() > 0) {
			Iterator ite = iterator();
			AddLTStatoEvento evt =
				new AddLTStatoEvento(
					event.getElementoStato(),
					event.getListaStato(),
					event.getThreadElement(),
					listaThread,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.statoAdded(evt);
			}

		}

	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoRemoved(eventi.thread.RemoveThreadStatoEvento)
	 */
	public void statoRemoved(RemoveThreadStatoEvento event) {
		if (size() > 0) {
			Iterator ite = iterator();
			RemoveLTStatoEvento evt =
				new RemoveLTStatoEvento(
					event.getElementoStato(),
					event.getListaStato(),
					event.getThreadElement(),
					listaThread,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.statoRemoved(evt);
			}
		}

	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoUpdate(eventi.thread.UpdateThreadStatoEvento)
	 */
	public void statoUpdate(UpdateThreadStatoEvento event) {
		if (size() > 0) {
			Iterator ite = iterator();
			UpdateLTStatoEvento evt =
				new UpdateLTStatoEvento(
					event.getVecchioElementoStato(),
					event.getNuovoElementoStato(),
					event.getListaStato(),
					event.getThreadElement(),
					listaThread,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.statoUpdate(evt);
			}
		}

	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioAdded(eventi.thread.AddThreadMessaggioEvento)
	 */
	public void messaggioAdded(AddThreadMessaggioEvento event) {

		if (size() > 0) {
			Iterator ite = iterator();
			AddLTMessaggioEvento evt =
				new AddLTMessaggioEvento(
					event.getElementoMessaggio(),
					event.getListaMessaggio(),
					event.getThreadElement(),
					listaThread,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.messaggioAdded(evt);
			}
		}

	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioRemoved(eventi.thread.RemoveThreadMessaggioEvento)
	 */
	public void messaggioRemoved(RemoveThreadMessaggioEvento event) {
		if (size() > 0) {
			Iterator ite = iterator();
			RemoveLTMessaggioEvento evt =
				new RemoveLTMessaggioEvento(
					event.getElementoMessaggio(),
					event.getListaMessaggio(),
					event.getThreadElement(),
					listaThread,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.messaggioRemoved(evt);
			}

		}
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioUpdate(eventi.thread.UpdateThreadMessaggioEvento)
	 */
	public void messaggioUpdate(UpdateThreadMessaggioEvento event) {
		if (size() > 0) {
			Iterator ite = iterator();
			UpdateLTMessaggioEvento evt =
				new UpdateLTMessaggioEvento(
					event.getVecchioElementoMessaggio(),
					event.getNuovoElementoMessaggio(),
					event.getListaMessaggio(),
					event.getThreadElement(),
					listaThread,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.messaggioUpdate(evt);
			}
		}

	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#threadRefresh()
	 */
	public void threadRefresh() {
		if (size() > 0) {
			Iterator ite = iterator();
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.listaThreadRefresh();
			}
		}

	}

	/**
	 * Chiamata quando un Elemento ? aggiunto alla lista
	 */
	public void threadAdded(AddThreadEvento ate) {
		if (size() > 0) {
			Iterator ite = iterator();
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.threadAdded(ate);
			}
		}
	}


	/**
	 * Chiamata quando un Elemento ? aggiunto alla lista
	 * gestendo la sessione
	 */
	public void threadAdded(ThreadElement te, ListaThread lt) {
		if (size() > 0) {
			boolean bo = startSessione();
			
			Iterator ite = iterator();
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.threadAdded(new AddThreadEvento(te,lt, plugData.getPlugDataManager().getIdSessione()));
			}
			stopSessione(bo);
		}
	}




	/**
	 * chiamate quando un thread ? rimosso dalla lista
	 * @param rte
	 */

	public void threadRemoved(ThreadElement te, ListaThread lt) {
		if (size() > 0) {
			boolean bo = startSessione();
			
			Iterator ite = iterator();
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.threadRemoved(new RemoveThreadEvento(te,lt, plugData.getPlugDataManager().getIdSessione()));
			}
			stopSessione(bo);
		}
	}

	/**
	 * chiamate quando un thread ? rimosso dalla lista
	 * @param rte
	 */

	public void threadRemoved(RemoveThreadEvento rte) {
		if (size() > 0) {
			Iterator ite = iterator();
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.threadRemoved(rte);
			}
		}
	}



	/**
	 * chiamata quando un thread ? aggiornato (non tratta il caso old, new)
	 * @param ute
	 */
/*	public void threadUpdated(UpdateThreadEvento ute) {
		if (size() > 0) {
			Iterator ite = iterator();
			while (ite.hasNext()) {
				IListaThreadListener itl = (IListaThreadListener) ite.next();
				itl.threadUpdated(ute);
			}
		}

	}
*/




}
