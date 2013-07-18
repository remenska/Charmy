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

import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

import plugin.statediagram.pluglistener.IListaStatoListener;
import plugin.sequencediagram.data.ElementoClasse;
import plugin.sequencediagram.data.ListaClasse;

import plugin.sequencediagram.eventi.listaclasse.AddClasseEvento;
import plugin.sequencediagram.eventi.listaclasse.RemoveClasseEvento;
import plugin.sequencediagram.eventi.listaclasse.UpdateClasseEvento;
import plugin.sequencediagram.pluglistener.IListaClasseListener;
import plugin.topologydiagram.resource.data.ElementoProcessoStato;
import plugin.topologydiagram.resource.data.interfacce.IListaProcStatoNotify;

/**
 * implementazione di un listener per la Lista di processo
 * viene utilizzata come delega per la classe ListaProcesso
 * 
 * @author michele
 * Charmy plug-in
 **/
public class DelegateListaClasseListener
	extends DelegateBase
	implements IListaProcStatoNotify {

	/**
	 * lista del processo di cui viene effettuata la delega
	 */
	private ListaClasse listaClasse;

	/*
	 * elemento temporaneo, utilizzato nel pre update
	 */
	private ElementoClasse elementoClasse;
	/**
	 * costruttore
	 * @param dati globali
	 * @param lp listaClasse
	 * 
	 */
	public DelegateListaClasseListener(IPlugData pd,ListaClasse lp) {
		super(pd);
		listaClasse = lp;

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
	private void addClasse(AddClasseEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaClasseListener ilpl = (IListaClasseListener) ite.next();
			ilpl.classeAdded(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param RemoveEPEvento
	 */
	private void removedClasse(RemoveClasseEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaClasseListener ilpl = (IListaClasseListener) ite.next();
			ilpl.classeRemoved(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param UpdateEPEvento
	 */
	private void updatedClasse(UpdateClasseEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaClasseListener ilpl = (IListaClasseListener) ite.next();
			ilpl.classeUpdate(event);
		}
	}




	/** 
	 * Notifica l'inserimento di un nuovo Elemento 
	 */
	public synchronized void addNotify(ElementoProcessoStato eps) {
		if (size() > 0) { //ho qualcuno a cui fare la notifica
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			ElementoClasse ep = (ElementoClasse) eps;
			AddClasseEvento aee = new AddClasseEvento(ep, listaClasse, idSessione);
			addClasse(aee);
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


			ElementoClasse ep = (ElementoClasse) eps;
			RemoveClasseEvento aee = new RemoveClasseEvento(ep, listaClasse, idSessione);
			removedClasse(aee);
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

			ElementoClasse oldep = (ElementoClasse) oldeps;
			ElementoClasse newep = (ElementoClasse) neweps;

			UpdateClasseEvento uee = new UpdateClasseEvento(oldep, newep, listaClasse, idSessione);
			updatedClasse(uee);
			
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
	public ListaClasse getListaClasse() {
		return listaClasse;
	}

	/**
	 * setta la ListaClasse
	 * @param ListaStato
	 */
	public void setListaClasse(ListaClasse listaClasse) {
		this.listaClasse = listaClasse;
	}

	/* (non-Javadoc)
	 * @see data.IListaProcStatoNotify#informaPreUpdate(data.ElementoProcessoStato)
	 */
	public void informaPreUpdate(ElementoProcessoStato ep) {
	
		elementoClasse = ((ElementoClasse)ep).cloneClasse();
	}

	/* (non-Javadoc)
	 * @see data.IListaProcStatoNotify#informaPostUpdate(data.ElementoProcessoStato)
	 */
	public void informaPostUpdate(ElementoProcessoStato ep) {
		updateNotify(elementoClasse, ((ElementoClasse)ep).cloneClasse());
	}

}
