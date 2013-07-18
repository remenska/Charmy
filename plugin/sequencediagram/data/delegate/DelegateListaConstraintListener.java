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

import plugin.sequencediagram.data.ElementoConstraint;
import plugin.sequencediagram.data.ListaConstraint;
import plugin.sequencediagram.data.interfacce.IListaConstraintNotify;
import plugin.sequencediagram.eventi.listaConstraint.AddConstraintEvento;
import plugin.sequencediagram.eventi.listaConstraint.RemoveConstraintEvento;
import plugin.sequencediagram.eventi.listaConstraint.UpdateConstraintEvento;
import plugin.sequencediagram.pluglistener.IListaConstraintListener;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

/**
 * classe per la delega dell'invio dei messaggi ai gestori della listaConstraint
 * @author michele
 * Charmy plug-in
 **/
public class DelegateListaConstraintListener
	extends DelegateBase 
	implements IListaConstraintNotify {

	/**
	 * listaConstraint di cui viene effettuata la delega
	 */
	ListaConstraint listaConstraint;

	/**
	 * variabile di lavoro, serve per l'update
	 */
	ElementoConstraint elementoConstraint;
	/**
	 * costruttore Delega listener per la listaConstraint
	 * @param lp lista da delegare
	 * @param pl dati di riferimento
	 */
	public DelegateListaConstraintListener(ListaConstraint lp, IPlugData pl) {
		super(pl);
		listaConstraint = lp;

	}

	/**
	 * ridefinizione di add di List
	 * @param lp
	 * @return
	 */
	boolean add(IListaConstraintListener lp) {
		return super.add((Object) lp);
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da spedire
	 */
	private void notifyAddConstraint(AddConstraintEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaConstraintListener ilpl = (IListaConstraintListener) ite.next();
			ilpl.constraintAdded(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da spedire
	 */
	private void notifyRemoveConstraint(RemoveConstraintEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaConstraintListener ilpl = (IListaConstraintListener) ite.next();
			ilpl.constraintRemoved(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da inviare
	 */
	private void notifyUpdateConstraint(UpdateConstraintEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaConstraintListener ilpl = (IListaConstraintListener) ite.next();
			ilpl.constraintUpdate(event);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyAdd(data.ElementoCanaleMessaggio)
	 */
	public void notifyAdd(ElementoConstraint ecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			//ElementoCanale ec = (ElementoCanale) ecm;
			AddConstraintEvento event =
				new AddConstraintEvento(ecm.cloneConstraint(), listaConstraint, idSessione);
			notifyAddConstraint(event);
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyRemove(data.ElementoCanaleMessaggio)
	 */
	public void notifyRemove(ElementoConstraint ecm) {

		if (size() > 0) {

			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			
			RemoveConstraintEvento event =
				new RemoveConstraintEvento(
					ecm.cloneConstraint(),
					listaConstraint,
					idSessione);
			notifyRemoveConstraint(event);

			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoCanaleMessaggio, data.ElementoCanaleMessaggio)
	 */
	public void notifyUpdate(
			ElementoConstraint oldecm,
			ElementoConstraint newecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

		//	ElementoCanale ec = (ElementoCanale) oldecm;
		//	ElementoCanale newec = (ElementoCanale) newecm;
			UpdateConstraintEvento event =
				new UpdateConstraintEvento(
					oldecm.cloneConstraint(),
					newecm.cloneConstraint(),
					listaConstraint,
					idSessione);
			notifyUpdateConstraint(event);
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
				IListaConstraintListener ilpl = (IListaConstraintListener) ite.next();
				ilpl.constraintRefresh();
			}
		}
	}

	/**
	 * ritorna la listaConstraint di cui si gestisce la delega 
	 * @return
	 * 
	 */
	public ListaConstraint getListaConstraint() {
		return listaConstraint;
	}

	/**
	 * setta la lista Constraint di cui gestire gli eventi
	 * @param canale
	 */
	public void setListaConstraint(ListaConstraint time) {
		listaConstraint = time;
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPostUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPostUpdate(ElementoConstraint ep) {
		notifyUpdate(elementoConstraint, ep.cloneConstraint());
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPreUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPreUpdate(ElementoConstraint ep) {
		elementoConstraint = ep.cloneConstraint();
	}
	
	/**
	 * ritorna il contenitore dei dati
	 * @return
	 */
	public IPlugData getPlugData(){
		return plugData;
	}
	

}
