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
    

package plugin.topologydiagram.data.delegate;

import java.util.Iterator;

import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

import plugin.topologydiagram.data.ElementoProcesso;
import plugin.topologydiagram.data.ListaProcesso;

import plugin.topologydiagram.eventi.listaprocesso.AddEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.RemoveEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.UpdateEPEvento;
import plugin.topologydiagram.pluglistener.IListaProcessoListener;
import plugin.topologydiagram.resource.data.ElementoProcessoStato;
import plugin.topologydiagram.resource.data.interfacce.IListaProcStatoNotify;

/**
 * implementazione di un listener per la Lista di processo
 * viene utilizzata come delega per la classe ListaProcesso
 * 
 * @author michele
 * Charmy plug-in
 **/
public class DelegateListaProcessoListener
	extends DelegateBase
	implements IListaProcStatoNotify  {

	/**
	 * lista del processo di cui viene effettuata la delega
	 */
	ListaProcesso listaProcesso;


	/**
	 * elemento processo temporaneo, per la notifica dell'update
	 */
	ElementoProcesso elementoProcesso;

	public DelegateListaProcessoListener(ListaProcesso lp, IPlugData pl) {
		super(pl);
		listaProcesso = lp;
	}

	/**
	 * ridefinizione di add di List
	 * @param lp
	 * @return
	 */
	boolean add(IListaProcessoListener lp) {
		return super.add((Object) lp);
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps
	 */
	private void addProcesso(AddEPEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaProcessoListener ilpl = (IListaProcessoListener) ite.next();
			ilpl.processoAdded(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param RemoveEPEvento
	 */
	private void removedProcesso(RemoveEPEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaProcessoListener ilpl = (IListaProcessoListener) ite.next();
			ilpl.processoRemoved(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param UpdateEPEvento
	 */
	private void updatedProcesso(UpdateEPEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaProcessoListener ilpl = (IListaProcessoListener) ite.next();
			ilpl.processoUpdate(event);
		}
	}

	/** 
	 * Notifica l'inserimento di un nuovo Elemento 
	 */
	public synchronized void addNotify(ElementoProcessoStato eps) {
		if (size() > 0) { //ho qualcuno a cui fare la notifica
			
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			ElementoProcesso ep = (ElementoProcesso) eps;
			AddEPEvento aee =
				new AddEPEvento(ep.cloneProcesso(), listaProcesso, idSessione);
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

			ElementoProcesso ep = (ElementoProcesso) eps;
			RemoveEPEvento aee =
				new RemoveEPEvento(
					ep.cloneProcesso(),
					listaProcesso,
					idSessione);
			removedProcesso(aee);
			stopSessione(bo);
		}
	}

	/** 
	 * Notifica la Modifica di un nuovo Elemento 
	 */
	public synchronized void updateNotify(
		ElementoProcessoStato oldeps,
		ElementoProcessoStato neweps) {
		if (size() > 0) { //ho qualcuno a cui fare la notifica
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			
			ElementoProcesso oldep = (ElementoProcesso) oldeps;
			ElementoProcesso newep = (ElementoProcesso) neweps;

			if((oldep!=null)&&(newep!=null)){
			UpdateEPEvento uee =
				new UpdateEPEvento(
					oldep.cloneProcesso(),
					newep.cloneProcesso(),
					listaProcesso,
					idSessione);
			updatedProcesso(uee);
			stopSessione(bo);
			}
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaProcStatoNotify#refreshNotify()
	 */
	public synchronized void refreshNotify() {
		if (size() > 0) { //ho qualcuno a cui fare la notifica
			Iterator ite = this.iterator();
			while (ite.hasNext()) {
				IListaProcessoListener ilpl =
					(IListaProcessoListener) ite.next();
				ilpl.processoRefresh();
			}
		}

	}

	/**
	 * @return
	 */
	public ListaProcesso getListaProcesso() {
		return listaProcesso;
	}

	/**
	 * @param processo
	 */
	public void setListaProcesso(ListaProcesso processo) {
		listaProcesso = processo;
	}

	/* (non-Javadoc)
	 * @see data.IListaProcStatoNotify#informaPreUpdate(data.ElementoProcessoStato)
	 */
	public void informaPreUpdate(ElementoProcessoStato eps) {
		elementoProcesso = (ElementoProcesso) eps;
	}

	/* (non-Javadoc)
	 * @see data.IListaProcStatoNotify#informaPostUpdate(data.ElementoProcessoStato)
	 */
	public void informaPostUpdate(ElementoProcessoStato ep) {
		updateNotify(elementoProcesso, ep);
	}

}
