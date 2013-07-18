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
import plugin.statediagram.data.ListaStato;

import plugin.statediagram.eventi.stato.AddStatoEvento;
import plugin.statediagram.eventi.stato.RemoveStatoEvento;
import plugin.statediagram.eventi.stato.UpdateStatoEvento;
import plugin.statediagram.pluglistener.IListaStatoListener;
import plugin.topologydiagram.resource.data.ElementoProcessoStato;
import plugin.topologydiagram.resource.data.interfacce.IListaProcStatoNotify;

/**
 * implementazione di un listener per la Lista di processo
 * viene utilizzata come delega per la classe ListaProcesso
 * 
 * @author michele
 * Charmy plug-in
 **/
public class DelegateListaStatoListener
	extends DelegateBase
	implements IListaProcStatoNotify {

	/**
	 * lista del processo di cui viene effettuata la delega
	 */
	private ListaStato listaStato;

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
	public DelegateListaStatoListener(IPlugData pd,ListaStato lp) {
		super(pd);
		listaStato = lp;

	}

	/**
	 * ridefinizione di add relativo alla lista
	 * @param lp
	 * @return
	 */
	boolean add(IListaStatoListener lp) {
		return super.add((Object) lp);
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps
	 */
	private void addProcesso(AddStatoEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaStatoListener ilpl = (IListaStatoListener) ite.next();
			ilpl.statoAdded(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param RemoveEPEvento
	 */
	private void removedProcesso(RemoveStatoEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaStatoListener ilpl = (IListaStatoListener) ite.next();
			ilpl.statoRemoved(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param UpdateEPEvento
	 */
	private void updatedProcesso(UpdateStatoEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaStatoListener ilpl = (IListaStatoListener) ite.next();
			ilpl.statoUpdate(event);
		}
	}




	/** 
	 * Notifica l'inserimento di un nuovo Elemento 
	 */
	public synchronized void addNotify(ElementoProcessoStato eps) {
		if (size() > 0) { //ho qualcuno a cui fare la notifica
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			ElementoStato ep = (ElementoStato) eps;
			AddStatoEvento aee = new AddStatoEvento(ep, listaStato, idSessione);
			addProcesso(aee);
			stopSessione(bo);
		}
	}

	/** 
	 * Notifica la cancellazione di un nuovo Elemento 
	 */
	public synchronized void removeNotify(ElementoProcessoStato eps) {
		if (size() > 0) { //ho qualcuno a cui fare la notifica
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();


			ElementoStato ep = (ElementoStato) eps;
			RemoveStatoEvento aee = new RemoveStatoEvento(ep, listaStato, idSessione);
			removedProcesso(aee);
		stopSessione(bo);
		}

	}

	/** 
	 * Notifica la Modifica di un nuovo Elemento 
	 */
	public synchronized void updateNotify(ElementoProcessoStato oldeps, ElementoProcessoStato neweps) {
		if (size() > 0) { //ho qualcuno a cui fare la notifica
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			ElementoStato oldep = (ElementoStato) oldeps;
			ElementoStato newep = (ElementoStato) neweps;

			UpdateStatoEvento uee = new UpdateStatoEvento(oldep, newep, listaStato, idSessione);
			updatedProcesso(uee);
			
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaProcStatoNotify#refreshNotify()
	 */
	public synchronized void refreshNotify() {
		if (size() > 0) { //ho qualcuno a cui fare la notifica
			Iterator ite = this.iterator();
			while (ite.hasNext()) {
				IListaStatoListener ilpl =
					(IListaStatoListener) ite.next();
				ilpl.statoRefresh();
			}
		}

	}

	/**
	 * ritorna la lista stato associata al DelegateListener
	 * @return
	 */
	public ListaStato getListaStato() {
		return listaStato;
	}

	/**
	 * setta la ListaStato
	 * @param ListaStato
	 */
	public void setListaStato(ListaStato listaStato) {
		this.listaStato = listaStato;
	}

	/* (non-Javadoc)
	 * @see data.IListaProcStatoNotify#informaPreUpdate(data.ElementoProcessoStato)
	 */
	public void informaPreUpdate(ElementoProcessoStato ep) {
		elementoStato = ((ElementoStato)ep).cloneStato();
	}

	/* (non-Javadoc)
	 * @see data.IListaProcStatoNotify#informaPostUpdate(data.ElementoProcessoStato)
	 */
	public void informaPostUpdate(ElementoProcessoStato ep) {
		updateNotify(elementoStato, ((ElementoStato)ep).cloneStato());
	}

}
