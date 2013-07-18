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

import plugin.sequencediagram.data.ElementoTime;
import plugin.sequencediagram.data.ListaTime;
import plugin.sequencediagram.data.interfacce.IListaTimeNotify;
import plugin.sequencediagram.eventi.listaTime.AddTimeEvento;
import plugin.sequencediagram.eventi.listaTime.RemoveTimeEvento;
import plugin.sequencediagram.eventi.listaTime.UpdateTimeEvento;
import plugin.sequencediagram.pluglistener.IListaTimeListener;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

/**
 * classe per la delega dell'invio dei messaggi ai gestori della listaTime
 * @author michele
 * Charmy plug-in
 **/
public class DelegateListaTimeListener
	extends DelegateBase 
	implements IListaTimeNotify {

	/**
	 * listaTime di cui viene effettuata la delega
	 */
	ListaTime listaTime;

	/**
	 * variabile di lavoro, serve per l'update
	 */
	ElementoTime elementoTime;
	/**
	 * costruttore Delega listener per la listaTime
	 * @param lp lista da delegare
	 * @param pl dati di riferimento
	 */
	public DelegateListaTimeListener(ListaTime lp, IPlugData pl) {
		super(pl);
		listaTime = lp;

	}

	/**
	 * ridefinizione di add di List
	 * @param lp
	 * @return
	 */
	boolean add(IListaTimeListener lp) {
		return super.add((Object) lp);
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da spedire
	 */
	private void notifyAddTime(AddTimeEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaTimeListener ilpl = (IListaTimeListener) ite.next();
			ilpl.timeAdded(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da spedire
	 */
	private void notifyRemoveTime(RemoveTimeEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaTimeListener ilpl = (IListaTimeListener) ite.next();
			ilpl.timeRemoved(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da inviare
	 */
	private void notifyUpdateTime(UpdateTimeEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaTimeListener ilpl = (IListaTimeListener) ite.next();
			ilpl.timeUpdate(event);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyAdd(data.ElementoCanaleMessaggio)
	 */
	public void notifyAdd(ElementoTime ecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			//ElementoCanale ec = (ElementoCanale) ecm;
			AddTimeEvento event =
				new AddTimeEvento(ecm.cloneTime(), listaTime, idSessione);
			notifyAddTime(event);
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyRemove(data.ElementoCanaleMessaggio)
	 */
	public void notifyRemove(ElementoTime ecm) {

		if (size() > 0) {

			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			
			RemoveTimeEvento event =
				new RemoveTimeEvento(
					ecm.cloneTime(),
					listaTime,
					idSessione);
			notifyRemoveTime(event);

			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoCanaleMessaggio, data.ElementoCanaleMessaggio)
	 */
	public void notifyUpdate(
		ElementoTime oldecm,
		ElementoTime newecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

		//	ElementoCanale ec = (ElementoCanale) oldecm;
		//	ElementoCanale newec = (ElementoCanale) newecm;
			UpdateTimeEvento event =
				new UpdateTimeEvento(
					oldecm.cloneTime(),
					newecm.cloneTime(),
					listaTime,
					idSessione);
			notifyUpdateTime(event);
			stopSessione(bo);

		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoCanaleMessaggio, data.ElementoCanaleMessaggio)
	 */
	public void refreshNotify() {
		if (size() > 0) {
			Iterator ite = this.iterator();
			while (ite.hasNext()) {
				IListaTimeListener ilpl = (IListaTimeListener) ite.next();
				ilpl.timeRefresh();
			}
		}
	}

	/**
	 * ritorna la listaTime di cui si gestisce la delega 
	 * @return
	 * 
	 */
	public ListaTime getListaTime() {
		return listaTime;
	}

	/**
	 * setta la lista time di cui gestire gli eventi
	 * @param canale
	 */
	public void setListaTime(ListaTime time) {
		listaTime = time;
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPostUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPostUpdate(ElementoTime ep) {
		notifyUpdate(elementoTime, ep.cloneTime());
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPreUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPreUpdate(ElementoTime ep) {
		elementoTime = ep.cloneTime();
	}
	
	/**
	 * ritorna il contenitore dei dati
	 * @return
	 */
	public IPlugData getPlugData(){
		return plugData;
	}
	

}
