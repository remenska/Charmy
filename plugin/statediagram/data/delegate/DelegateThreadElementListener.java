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

import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.data.ListaMessaggio;
import plugin.statediagram.data.ListaStato;
import plugin.statediagram.data.ThreadElement;

import plugin.statediagram.eventi.messaggio.AddMessaggioEvento;
import plugin.statediagram.eventi.messaggio.RemoveMessaggioEvento;
import plugin.statediagram.eventi.messaggio.UpdateMessaggioEvento;
import plugin.statediagram.eventi.stato.AddStatoEvento;
import plugin.statediagram.eventi.stato.RemoveStatoEvento;
import plugin.statediagram.eventi.stato.UpdateStatoEvento;
import plugin.statediagram.eventi.thread.AddThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.AddThreadStatoEvento;
import plugin.statediagram.eventi.thread.RemoveThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.RemoveThreadStatoEvento;
import plugin.statediagram.eventi.thread.UpdateThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.UpdateThreadStatoEvento;
import plugin.statediagram.pluglistener.IListaMessaggioListener;
import plugin.statediagram.pluglistener.IListaStatoListener;
import plugin.statediagram.pluglistener.IThreadElementListener;

/**
 * implementazione di un listener per la Lista di processo
 * viene utilizzata come delega per la classe ListaProcesso
 * 
 * @author michele
 * Charmy plug-in
 **/
public class DelegateThreadElementListener
	extends DelegateBase
	implements IListaStatoListener, IListaMessaggioListener {

	/**
	 * lista del processo di cui viene effettuata la delega
	 */
	private ThreadElement threadElement;

	/*
	 * elemento temporaneo, utilizzato nel pre update
	 */
	private ElementoStato elementoStato;
	/**
	 * costruttore
	 * @param dati globali
	 * @param lp listaStato
	 * 
	 */
	public DelegateThreadElementListener(IPlugData pd, ThreadElement lp) {
		super(pd);
		threadElement = lp;

	}



	/* (non-Javadoc)
	 * @see plugin.IListaMessaggioListener#messaggioAdded(eventi.messaggio.AddMessaggioEvento)
	 */
	public void messaggioAdded(AddMessaggioEvento event) {

		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			
			Iterator ite = iterator();
			AddThreadMessaggioEvento evt =
				new AddThreadMessaggioEvento(
					event.getElementoMessaggio(),
					(ListaMessaggio) event.getSource(),
			threadElement, idSessione);
			while (ite.hasNext()) {
				IThreadElementListener itl = (IThreadElementListener) ite.next();
				itl.messaggioAdded(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaMessaggioListener#messaggioRemoved(eventi.messaggio.RemoveMessaggioEvento)
	 */
	public void messaggioRemoved(RemoveMessaggioEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			
			Iterator ite = iterator();
			RemoveThreadMessaggioEvento evt =
				new RemoveThreadMessaggioEvento(
					event.getElementoMessaggio(),
					(ListaMessaggio) event.getSource(),
					threadElement, idSessione);
			while (ite.hasNext()) {
				IThreadElementListener itl = (IThreadElementListener) ite.next();
				itl.messaggioRemoved(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaMessaggioListener#messaggioUpdate(eventi.messaggio.UpdateMessaggioEvento)
	 */
	public void messaggioUpdate(UpdateMessaggioEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			
			Iterator ite = iterator();
			UpdateThreadMessaggioEvento evt =
				new UpdateThreadMessaggioEvento(
				    event.getVecchioElementoMessaggio(),
					event.getNuovoElementoMessaggio(),
					(ListaMessaggio) event.getSource(),
					threadElement, idSessione);
			while (ite.hasNext()) {
				IThreadElementListener itl = (IThreadElementListener) ite.next();
				itl.messaggioUpdate(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaMessaggioListener#messaggioRefresh()
	 */
	public void messaggioRefresh() {

		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			
			Iterator ite = iterator();
			while (ite.hasNext()) {
				IThreadElementListener itl = (IThreadElementListener) ite.next();
				itl.threadRefresh();
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaStatoListener#statoAdded(eventi.stato.AddStatoEvento)
	 */
	public void statoAdded(AddStatoEvento event) {

		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			
			Iterator ite = iterator();
			AddThreadStatoEvento evt =
				new AddThreadStatoEvento(
					event.getElementoStato(),
					(ListaStato) event.getSource(),
					threadElement, idSessione);
			while (ite.hasNext()) {
				IThreadElementListener itl = (IThreadElementListener) ite.next();
				itl.statoAdded(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaStatoListener#statoRemoved(eventi.stato.RemoveStatoEvento)
	 */
	public void statoRemoved(RemoveStatoEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			
			Iterator ite = iterator();
			RemoveThreadStatoEvento evt =
				new RemoveThreadStatoEvento(
					event.getElementoStato(),
					(ListaStato) event.getSource(),
					threadElement, idSessione);
			while (ite.hasNext()) {
				IThreadElementListener itl = (IThreadElementListener) ite.next();
				itl.statoRemoved(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaStatoListener#statoUpdate(eventi.stato.UpdateStatoEvento)
	 */
	public void statoUpdate(UpdateStatoEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			
			Iterator ite = iterator();
			UpdateThreadStatoEvento evt =
				new UpdateThreadStatoEvento(
					event.getOldElementoStato(),
					event.getNewElementoStato(),
					(ListaStato) event.getSource(),
					threadElement, idSessione);
			while (ite.hasNext()) {
				IThreadElementListener itl = (IThreadElementListener) ite.next();
				itl.statoUpdate(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaStatoListener#statoRefresh()
	 */
	public void statoRefresh() {
		messaggioRefresh();
	}



}
