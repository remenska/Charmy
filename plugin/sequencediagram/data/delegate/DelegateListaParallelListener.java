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

import plugin.sequencediagram.data.ElementoParallelo;
import plugin.sequencediagram.data.ListaParallel;
import plugin.sequencediagram.data.interfacce.IListaParallelNotify;
import plugin.sequencediagram.eventi.listaParallel.AddParallelEvento;
import plugin.sequencediagram.eventi.listaParallel.RemoveParallelEvento;
import plugin.sequencediagram.eventi.listaParallel.UpdateParallelEvento;
import plugin.sequencediagram.pluglistener.IListaParallelListener;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

public class DelegateListaParallelListener
	extends DelegateBase 
	implements IListaParallelNotify {
    
    
	/**
	 * listaParallel di cui viene effettuata la delega
	 */
	ListaParallel listaParallel;

	/**
	 * variabile di lavoro, serve per l'update
	 */
	ElementoParallelo ElementoParallelo;
        
        /**
	 * costruttore Delega listener per la listaParallel
	 * @param lp lista da delegare
	 * @param pl dati di riferimento
	 */
	public DelegateListaParallelListener(ListaParallel lp, IPlugData pl) {
		super(pl);
		listaParallel = lp;

	}

	/**
	 * ridefinizione di add di List
	 * @param lp
	 * @return
	 */
	boolean add(IListaParallelListener lp) {
		return super.add((Object) lp);
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da spedire
	 */
	private void notifyAddParallel(AddParallelEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaParallelListener ilpl = (IListaParallelListener) ite.next();
			ilpl.parallelAdded(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da spedire
	 */
	private void notifyRemoveParallel(RemoveParallelEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaParallelListener ilpl = (IListaParallelListener) ite.next();
			ilpl.parallelRemoved(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da inviare
	 */
	private void notifyUpdateParallel(UpdateParallelEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaParallelListener ilpl = (IListaParallelListener) ite.next();
			ilpl.parallelUpdate(event);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyAdd(data.ElementoCanaleMessaggio)
	 */
	public void notifyAdd(ElementoParallelo ecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			//ElementoCanale ec = (ElementoCanale) ecm;
			AddParallelEvento event =
				new AddParallelEvento(ecm.cloneParallel(), listaParallel, idSessione);
			notifyAddParallel(event);
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyRemove(data.ElementoCanaleMessaggio)
	 */
	public void notifyRemove(ElementoParallelo ecm) {

		if (size() > 0) {

			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			
			RemoveParallelEvento event =
				new RemoveParallelEvento(
					ecm.cloneParallel(),
					listaParallel,
					idSessione);
			notifyRemoveParallel(event);

			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoCanaleMessaggio, data.ElementoCanaleMessaggio)
	 */
	public void notifyUpdate(
			ElementoParallelo oldecm,
			ElementoParallelo newecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

		//	ElementoCanale ec = (ElementoCanale) oldecm;
		//	ElementoCanale newec = (ElementoCanale) newecm;
			UpdateParallelEvento event =
				new UpdateParallelEvento(
					oldecm.cloneParallel(),
					newecm.cloneParallel(),
					listaParallel,
					idSessione);
			notifyUpdateParallel(event);
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
				IListaParallelListener ilpl = (IListaParallelListener) ite.next();
				ilpl.parallelRefresh();
			}
		}
	}

	/**
	 * ritorna la listaParallel di cui si gestisce la delega 
	 * @return
	 * 
	 */
	public ListaParallel getListaParallel() {
		return listaParallel;
	}

	/**
	 * setta la lista Parallel di cui gestire gli eventi
	 * @param canale
	 */
	public void setListaParallel(ListaParallel time) {
		listaParallel = time;
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPostUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPostUpdate(ElementoParallelo ep) {
		notifyUpdate(ElementoParallelo, ep.cloneParallel());
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPreUpdate(data.ElementoCanaleMessaggio)
	 */
	public void informaPreUpdate(ElementoParallelo ep) {
		ElementoParallelo = ep.cloneParallel();
	}
	
	/**
	 * ritorna il contenitore dei dati
	 * @return
	 */
	public IPlugData getPlugData(){
		return plugData;
	}
    
    
    
}



