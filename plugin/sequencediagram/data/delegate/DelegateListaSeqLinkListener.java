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
    
package plugin.sequencediagram.data.delegate;

import java.util.Iterator;

import plugin.sequencediagram.data.ElementoSeqLink;
import plugin.sequencediagram.data.ListaSeqLink;
import plugin.sequencediagram.eventi.listaseqlink.AddSeqLinkEvento;
import plugin.sequencediagram.eventi.listaseqlink.RemoveSeqLinkEvento;
import plugin.sequencediagram.eventi.listaseqlink.UpdateSeqLinkEvento;
import plugin.sequencediagram.pluglistener.IListaSeqLinkListener;
import plugin.topologydiagram.resource.data.ElementoCanaleMessaggio;
import plugin.topologydiagram.resource.data.interfacce.IListaCanMessNotify;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

/**
 * classe per la delega dell'invio dei messaggi ai gestori di canali
 * @author michele
 * Charmy plug-in
 **/
public class DelegateListaSeqLinkListener extends DelegateBase implements IListaCanMessNotify {

	/**
	 * lista del canale di cui viene effettuata la delega
	 */
	ListaSeqLink listaSeqLink;

	/**
	 * variabile di lavoro, serve per l'update
	 */
	ElementoSeqLink elementoSeqLink;

	/**
	 * costruttore Delega listener per la lista dei canali
	 * @param lp
	 * @param pl
	 */
	public DelegateListaSeqLinkListener(ListaSeqLink lp, IPlugData pl) {
		super(pl);
		listaSeqLink = lp;

	}

	/**
	 * ritefinizione di add di List
	 * @param lp
	 * @return
	 */
	boolean add(IListaSeqLinkListener lp) {
		return super.add((Object) lp);
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps
	 */
	private void notifyAddSeqLink(AddSeqLinkEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaSeqLinkListener ilpl = (IListaSeqLinkListener) ite.next();
			ilpl.seqLinkAdded(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps
	 */
	private void notifyRemoveSeqLink(RemoveSeqLinkEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaSeqLinkListener ilpl = (IListaSeqLinkListener) ite.next();
			ilpl.seqLinkRemoved(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps
	 */
	private void notifyUpdateSeqLink(UpdateSeqLinkEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaSeqLinkListener ilpl = (IListaSeqLinkListener) ite.next();
			ilpl.seqLinkUpdate(event);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyAdd(data.ElementoCanaleMessaggio)
	 */
	public void notifyAdd(ElementoCanaleMessaggio ecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			ElementoSeqLink ec = (ElementoSeqLink) ecm;
			AddSeqLinkEvento event = new AddSeqLinkEvento(ec.cloneSeqLink(), listaSeqLink, idSessione);
			notifyAddSeqLink(event);
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyRemove(data.ElementoCanaleMessaggio)
	 */
	public void notifyRemove(ElementoCanaleMessaggio ecm) {

		if (size() > 0) {

			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			ElementoSeqLink ec = (ElementoSeqLink) ecm;
			RemoveSeqLinkEvento event =
				new RemoveSeqLinkEvento(
					ec.cloneSeqLink(),
					listaSeqLink,
					idSessione);
			notifyRemoveSeqLink(event);

			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoCanaleMessaggio, data.ElementoCanaleMessaggio)
	 */
	public void notifyUpdate(
		ElementoCanaleMessaggio oldecm,
		ElementoCanaleMessaggio newecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			ElementoSeqLink ec = (ElementoSeqLink) oldecm;
			ElementoSeqLink newec = (ElementoSeqLink) newecm;
			UpdateSeqLinkEvento event =
				new UpdateSeqLinkEvento(
					ec.cloneSeqLink(),
					newec.cloneSeqLink(),
					listaSeqLink,
					idSessione);
			notifyUpdateSeqLink(event);
			stopSessione(bo);

		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoCanaleMessaggio, data.ElementoCanaleMessaggio)
	 */
	public void refreshCanale() {
		if (size() > 0) {
			Iterator ite = this.iterator();
			while (ite.hasNext()) {
				IListaSeqLinkListener ilpl = (IListaSeqLinkListener) ite.next();
				ilpl.seqLinkRefresh();
			}
		}
	}

	/**
	 * @return
	 */
	public ListaSeqLink getListaSeqLink() {
		return listaSeqLink;
	}

	/**
	 * @param canale
	 */
	public void setListaSeqLink(ListaSeqLink canale) {
		listaSeqLink = canale;
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPostUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPostUpdate(ElementoCanaleMessaggio ep) {
		notifyUpdate(elementoSeqLink, ep);
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPreUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPreUpdate(ElementoCanaleMessaggio ep) {
		elementoSeqLink = ((ElementoSeqLink) ep).cloneSeqLink();
	}

}
