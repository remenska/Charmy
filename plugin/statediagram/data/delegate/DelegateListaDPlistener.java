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

import plugin.statediagram.data.ListaDP;
import plugin.statediagram.data.ListaThread;

import plugin.statediagram.eventi.listaDP.AddDPLTEvento;
import plugin.statediagram.eventi.listaDP.AddDPMessaggioEvento;
import plugin.statediagram.eventi.listaDP.AddDPStatoEvento;
import plugin.statediagram.eventi.listaDP.AddDPThreadEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPLTEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPMessaggioEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPStatoEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPThreadEvento;
import plugin.statediagram.eventi.listaDP.UpdateDPMessaggioEvento;
import plugin.statediagram.eventi.listaDP.UpdateDPStatoEvento;
import plugin.statediagram.eventi.listathread.AddLTMessaggioEvento;
import plugin.statediagram.eventi.listathread.AddLTStatoEvento;
import plugin.statediagram.eventi.listathread.AddThreadEvento;
import plugin.statediagram.eventi.listathread.RemoveLTMessaggioEvento;
import plugin.statediagram.eventi.listathread.RemoveLTStatoEvento;
import plugin.statediagram.eventi.listathread.RemoveThreadEvento;
import plugin.statediagram.eventi.listathread.UpdateLTMessaggioEvento;
import plugin.statediagram.eventi.listathread.UpdateLTStatoEvento;
import plugin.statediagram.pluglistener.IListaDPListener;
import plugin.statediagram.pluglistener.IListaThreadListener;

/**
 * @author michele
 * Classe per la delega del listener della lista di Dinamica del processo
 */
public class DelegateListaDPlistener
	extends DelegateBase
	implements IListaThreadListener {


	/**
	 * riferimento alla listaDP di cui si effettua la delega
	 */
	private ListaDP listaDP;

	public DelegateListaDPlistener(IPlugData plugData, ListaDP lista) {
		super(plugData);
		this.listaDP = lista;
	}

	/* (non-Javadoc)
	 * @see plugin.IListaThreadListener#statoAdded(eventi.listathread.AddLTStatoEvento)
	 */
	public void statoAdded(AddLTStatoEvento event) {

		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			
			Iterator ite = iterator();
			AddDPStatoEvento evt =
				new AddDPStatoEvento(
					event.getElementoStato(),
					event.getListaStato(),
					event.getThreadElement(),
					event.getSorgente(),
					listaDP,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.statoAdded(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaThreadListener#statoRemoved(eventi.listathread.RemoveLTStatoEvento)
	 */
	public void statoRemoved(RemoveLTStatoEvento event) {

		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveDPStatoEvento evt =
				new RemoveDPStatoEvento(
					event.getElementoStato(),
					event.getListaStato(),
					event.getThreadElement(),
					event.getSorgente(),
					listaDP,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.statoRemoved(evt);
			}
			stopSessione(bo);

		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaThreadListener#statoUpdate(eventi.listathread.UpdateLTStatoEvento)
	 */
	public void statoUpdate(UpdateLTStatoEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			
			Iterator ite = iterator();
			UpdateDPStatoEvento evt =
				new UpdateDPStatoEvento(
					event.getNuovoElementoStato(),
					event.getListaStato(),
					event.getThreadElement(),
					event.getSorgente(),
					listaDP,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.statoUpdate(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaThreadListener#messaggioAdded(eventi.listathread.AddLTMessaggioEvento)
	 */
	public void messaggioAdded(AddLTMessaggioEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddDPMessaggioEvento evt =
				new AddDPMessaggioEvento(
					event.getElementoMessaggio(),
					event.getListaMessaggio(),
					event.getThreadElement(),
					event.getSorgente(),
					listaDP,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.messaggioAdded(evt);
			}
			stopSessione(bo);

		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaThreadListener#messaggioRemoved(eventi.listathread.RemoveLTMessaggioEvento)
	 */
	public void messaggioRemoved(RemoveLTMessaggioEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveDPMessaggioEvento evt =
				new RemoveDPMessaggioEvento(
					event.getElementoMessaggio(),
					event.getListaMessaggio(),
					event.getThreadElement(),
					event.getSorgente(),
					listaDP,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.messaggioRemoved(evt);
			}
		stopSessione(bo);

		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaThreadListener#messaggioUpdate(eventi.listathread.UpdateLTMessaggioEvento)
	 */
	public void messaggioUpdate(UpdateLTMessaggioEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			UpdateDPMessaggioEvento evt =
				new UpdateDPMessaggioEvento(
					event.getVecchioElementoMessaggio(),
					event.getNuovoElementoMessaggio(),
					event.getListaMessaggio(),
					event.getThreadElement(),
					event.getSorgente(),
					listaDP,
					event.getIdSessione());
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.messaggioUpdate(evt);
			}
 			stopSessione(bo);

		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaThreadListener#listaThreadRefresh()
	 */
	public void listaThreadRefresh() {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();

			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.listaDPRefresh();
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaThreadListener#threadAdded(eventi.listathread.AddThreadEvento)
	 */
	public void threadAdded(AddThreadEvento ate) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddDPThreadEvento evt =
				new AddDPThreadEvento(
					ate.getThreadElement(),
					ate.getListaThread(),
					listaDP,
					ate.getIdSessione());
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.threadAdded(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaThreadListener#threadRemoved(eventi.listathread.RemoveThreadEvento)
	 */
	public void threadRemoved(RemoveThreadEvento rte) {
		if (size() > 0) {
			boolean bo = startSessione();
	long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveDPThreadEvento evt =
				new RemoveDPThreadEvento(
					rte.getThreadElement(),
					rte.getListaThread(),
					listaDP,
					rte.getIdSessione());
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.threadRemoved(evt);
			}
			stopSessione(bo);

		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaThreadListener#threadUpdated(eventi.listathread.UpdateThreadEvento)
	 */
/*	public void threadUpdated(UpdateThreadEvento ute) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getIdSessione();
			Iterator ite = iterator();
			UpdateDPThreadEvento evt =
				new UpdateDPThreadEvento(
					ute.getOldThreadElement(),
					ute.getNewThreadElement(),
					ute.getListaThread(),
					listaDP,
					ute.getIdSessione());
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.threadUpdated(evt);
			}
		stopSessione(bo);
		}
	}
*/
	/**
	 * eventi specifici di DP
	 *
	 */

	/**
	 * informa il sistema dell'aggiunta di una listaThread
	 */
	public synchronized void informaAddDP(ListaThread lt) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			Iterator ite = iterator();
			AddDPLTEvento evt = new AddDPLTEvento(lt, listaDP, idSessione);
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.listaThreadAdded(evt);
			}
			stopSessione(bo);
		}
	}

	/**
	 * informa il sistema della rimozione di una listaThread
	 */
	public synchronized void informaRemoveDP(ListaThread lt) {
		if (size() > 0) {
			boolean bo = startSessione();
	long idSessione = plugData.getPlugDataManager().getIdSessione();

			Iterator ite = iterator();
			RemoveDPLTEvento evt =
				new RemoveDPLTEvento(lt, listaDP, idSessione);
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.listaThreadRemoved(evt);
			}
			stopSessione(bo);
		}
	}

	/**
	 * informa il sistema della rimozione di una listaThread
	 */
/*	public synchronized void informaUpdateDP(ListaThread lt) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getIdSessione();
			Iterator ite = iterator();
			UpdateDPLTEvento evt =
				new UpdateDPLTEvento(lt, listaDP, idSessione);
			while (ite.hasNext()) {
				IListaDPListener itl = (IListaDPListener) ite.next();
				itl.listaThreadUpdated(evt);
			}
			stopSessione(bo);
		}
	}
*/
}
