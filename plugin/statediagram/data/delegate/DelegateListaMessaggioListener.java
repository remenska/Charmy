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

import plugin.statediagram.data.ListaMessaggio;
import plugin.statediagram.graph.ElementoMessaggio;
import plugin.statediagram.pluglistener.IListaMessaggioListener;

import plugin.statediagram.eventi.messaggio.AddMessaggioEvento;
import plugin.statediagram.eventi.messaggio.RemoveMessaggioEvento;
import plugin.statediagram.eventi.messaggio.UpdateMessaggioEvento;
import plugin.topologydiagram.resource.data.ElementoCanaleMessaggio;
import plugin.topologydiagram.resource.data.interfacce.IListaCanMessNotify;

/**
 * classe per la delega dell'invio dei messaggi ai gestori di canali
 * @author michele
 * Charmy plug-in
 **/
public class DelegateListaMessaggioListener
	extends DelegateBase
	implements IListaCanMessNotify {

	/**
	 * lista del canale di cui viene effettuata la delega
	 */
	ListaMessaggio listaMessaggio;

	ElementoMessaggio elementoMessaggio;

	public DelegateListaMessaggioListener(IPlugData pd, ListaMessaggio lp) {
		super(pd);
		listaMessaggio = lp;
	}

	/**
	 * ritefinizione di add di List
	 * @param lp
	 * @return
	 */
	boolean add(IListaMessaggioListener lp) {
		return super.add((Object) lp);
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps
	 */
	private void notifyAddMsg(AddMessaggioEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaMessaggioListener ilpl = (IListaMessaggioListener) ite.next();
			ilpl.messaggioAdded(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps
	 */
	private void notifyRemoveMsg(RemoveMessaggioEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaMessaggioListener ilpl = (IListaMessaggioListener) ite.next();
			ilpl.messaggioRemoved(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps
	 */
	private void notifyUpdateMsg(UpdateMessaggioEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {			
			IListaMessaggioListener ilpl = (IListaMessaggioListener) ite.next();
			ilpl.messaggioUpdate(event);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyAdd(data.ElementoCanaleMessaggio)
	 */
	public synchronized void notifyAdd(ElementoCanaleMessaggio ecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			ElementoMessaggio ec = (ElementoMessaggio) ecm;
			AddMessaggioEvento event =
				new AddMessaggioEvento(ec, listaMessaggio, idSessione);
			notifyAddMsg(event);
			stopSessione(bo);

		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyRemove(data.ElementoCanaleMessaggio)
	 */
	public synchronized void notifyRemove(ElementoCanaleMessaggio ecm) {

		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			ElementoMessaggio ec = (ElementoMessaggio) ecm;
			RemoveMessaggioEvento event =
				new RemoveMessaggioEvento(ec, listaMessaggio, idSessione);
			notifyRemoveMsg(event);
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoCanaleMessaggio, data.ElementoCanaleMessaggio)
	 */
	public synchronized void notifyUpdate(
		ElementoCanaleMessaggio ecm,
		ElementoCanaleMessaggio newecm) {
		if (size() > 0) {
			//ElementoCanale ec = (ElementoCanale) ecm; 
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			ElementoMessaggio newec = (ElementoMessaggio) newecm;
			ElementoMessaggio oldec = (ElementoMessaggio) ecm;
			
			UpdateMessaggioEvento event =
				new UpdateMessaggioEvento(oldec,newec, listaMessaggio, idSessione);
			notifyUpdateMsg(event);
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoCanaleMessaggio, data.ElementoCanaleMessaggio)
	 */
	public synchronized void refreshCanale() {
		if (size() > 0) {
			Iterator ite = this.iterator();
			while (ite.hasNext()) {
				IListaMessaggioListener ilpl =
					(IListaMessaggioListener) ite.next();
				ilpl.messaggioRefresh();
			}
		}
	}

	/**
	 * ritorna la lista messaggio associata alla classe
	 * @return
	 */
	public ListaMessaggio getListaMessaggio() {
		return listaMessaggio;
	}

	/**
	 * setta la listaMessaggio relativa contente la classe 
	 * @param canale
	 */
	public void setListaMessaggio(ListaMessaggio msg) {
		listaMessaggio = msg;
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPreUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPreUpdate(ElementoCanaleMessaggio ep) {
		elementoMessaggio = ((ElementoMessaggio) ep).cloneMessaggio();
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPostUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPostUpdate(ElementoCanaleMessaggio ep) {
		notifyUpdate(elementoMessaggio, ((ElementoMessaggio) ep).cloneMessaggio());
	}

}
