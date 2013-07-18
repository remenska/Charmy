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
    

package plugin.topologychannels.data.delegate;

import java.util.Iterator;

import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

import plugin.topologychannels.data.ElementoCanale;
import plugin.topologychannels.data.ListaCanale;

import plugin.topologychannels.eventi.listacanali.AddCanaleEvento;
import plugin.topologychannels.eventi.listacanali.RemoveCanaleEvento;
import plugin.topologychannels.eventi.listacanali.UpdateCanaleEvento;
import plugin.topologychannels.pluglistener.IListaCanaleListener;
import plugin.topologychannels.resource.data.ElementoCanaleMessaggio;
import plugin.topologychannels.resource.data.interfacce.IListaCanMessNotify;

/**
 * classe per la delega dell'invio dei messaggi ai gestori di canali
 * @author michele
 * Charmy plug-in
 **/
public class DelegateListaCanaleListener
	extends DelegateBase
	implements IListaCanMessNotify {

	/**
	 * lista del canale di cui viene effettuata la delega
	 */
	ListaCanale listaCanale;

	/**
	 * variabile di lavoro, serve per l'update
	 */
	ElementoCanale elementoCanale;
	/**
	 * costruttore Delega listener per la lista dei canali
	 * @param lp
	 * @param pl
	 */
	public DelegateListaCanaleListener(ListaCanale lp, IPlugData pl) {
		super(pl);
		listaCanale = lp;

	}

	/**
	 * ritefinizione di add di List
	 * @param lp
	 * @return
	 */
	boolean add(IListaCanaleListener lp) {
		return super.add((Object) lp);
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps
	 */
	private void notifyAddCanale(AddCanaleEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaCanaleListener ilpl = (IListaCanaleListener) ite.next();
			ilpl.canaleAdded(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps
	 */
	private void notifyRemoveCanale(RemoveCanaleEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaCanaleListener ilpl = (IListaCanaleListener) ite.next();
			ilpl.canaleRemoved(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps
	 */
	private void notifyUpdateCanale(UpdateCanaleEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaCanaleListener ilpl = (IListaCanaleListener) ite.next();
			ilpl.canaleUpdate(event);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyAdd(data.ElementoCanaleMessaggio)
	 */
	public void notifyAdd(ElementoCanaleMessaggio ecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			ElementoCanale ec = (ElementoCanale) ecm;
			AddCanaleEvento event =
				new AddCanaleEvento(ec.cloneCanale(), listaCanale, idSessione);
			notifyAddCanale(event);
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

			ElementoCanale ec = (ElementoCanale) ecm;
			RemoveCanaleEvento event =
				new RemoveCanaleEvento(
					ec.cloneCanale(),
					listaCanale,
					idSessione);
			notifyRemoveCanale(event);

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

			ElementoCanale ec = (ElementoCanale) oldecm;
			ElementoCanale newec = (ElementoCanale) newecm;
			UpdateCanaleEvento event =
				new UpdateCanaleEvento(
					ec.cloneCanale(),
					newec.cloneCanale(),
					listaCanale,
					idSessione);
			notifyUpdateCanale(event);
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
				IListaCanaleListener ilpl = (IListaCanaleListener) ite.next();
				ilpl.canaleRefresh();
			}
		}
	}

	/**
	 * @return
	 */
	public ListaCanale getListaCanale() {
		return listaCanale;
	}

	/**
	 * @param canale
	 */
	public void setListaCanale(ListaCanale canale) {
		listaCanale = canale;
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPostUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPostUpdate(ElementoCanaleMessaggio ep) {
		notifyUpdate(elementoCanale, ep);
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPreUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPreUpdate(ElementoCanaleMessaggio ep) {
		elementoCanale = ((ElementoCanale) ep).cloneCanale();
	}

}
