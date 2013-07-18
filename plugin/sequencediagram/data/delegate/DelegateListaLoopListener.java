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

import plugin.sequencediagram.data.ElementoLoop;
import plugin.sequencediagram.data.ListaLoop;
import plugin.sequencediagram.data.interfacce.IListaLoopNotify;
import plugin.sequencediagram.eventi.listaLoop.AddLoopEvento;
import plugin.sequencediagram.eventi.listaLoop.RemoveLoopEvento;
import plugin.sequencediagram.eventi.listaLoop.UpdateLoopEvento;
import plugin.sequencediagram.pluglistener.IListaLoopListener;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

public class DelegateListaLoopListener
	extends DelegateBase 
	implements IListaLoopNotify {
    
    
	/**
	 * listaLoop di cui viene effettuata la delega
	 */
	ListaLoop listaLoop;

	/**
	 * variabile di lavoro, serve per l'update
	 */
	ElementoLoop ElementoLoop;
        
        /**
	 * costruttore Delega listener per la listaLoop
	 * @param lp lista da delegare
	 * @param pl dati di riferimento
	 */
	public DelegateListaLoopListener(ListaLoop lp, IPlugData pl) {
		super(pl);
		listaLoop = lp;

	}

	/**
	 * ridefinizione di add di List
	 * @param lp
	 * @return
	 */
	boolean add(IListaLoopListener lp) {
		return super.add((Object) lp);
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da spedire
	 */
	private void notifyAddLoop(AddLoopEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaLoopListener ilpl = (IListaLoopListener) ite.next();
			ilpl.LoopAdded(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da spedire
	 */
	private void notifyRemoveLoop(RemoveLoopEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaLoopListener ilLoop = (IListaLoopListener) ite.next();
			ilLoop.LoopRemoved(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da inviare
	 */
	private void notifyUpdateLoop(UpdateLoopEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaLoopListener ilLoop = (IListaLoopListener) ite.next();
			ilLoop.LoopUpdate(event);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyAdd(data.ElementoCanaleMessaggio)
	 */
	public void notifyAdd(ElementoLoop ecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			//ElementoCanale ec = (ElementoCanale) ecm;
			AddLoopEvento event =
				new AddLoopEvento(ecm.cloneLoop(), listaLoop, idSessione);
			notifyAddLoop(event);
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyRemove(data.ElementoCanaleMessaggio)
	 */
	public void notifyRemove(ElementoLoop ecm) {

		if (size() > 0) {

			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			
			RemoveLoopEvento event =
				new RemoveLoopEvento(
					ecm.cloneLoop(),
					listaLoop,
					idSessione);
			notifyRemoveLoop(event);

			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoLoop, data.ElementoLoop)
	 */
	public void notifyUpdate(
			ElementoLoop oldecm,
			ElementoLoop newecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			UpdateLoopEvento event =
				new UpdateLoopEvento(
					oldecm.cloneLoop(),
					newecm.cloneLoop(),
					listaLoop,
					idSessione);
			notifyUpdateLoop(event);
			stopSessione(bo);

		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoLoop, data.ElementoLoop)
	 */
	public void refreshNotify() {
		if (size() > 0) {
			Iterator ite = this.iterator();
			while (ite.hasNext()) {
				IListaLoopListener ilpl = (IListaLoopListener) ite.next();
				ilpl.LoopRefresh();
			}
		}
	}

	/**
	 * ritorna la listaLoop di cui si gestisce la delega 
	 * @return
	 * 
	 */
	public ListaLoop getListaLoop() {
		return listaLoop;
	}

	/**
	 * setta la lista Loop di cui gestire gli eventi
	 * @param canale
	 */
	public void setListaLoop(ListaLoop time) {
		listaLoop = time;
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPostUpdate(data.ElementoLoop)
	 */
	public void informaPostUpdate(ElementoLoop ep) {
		notifyUpdate(ElementoLoop, ep.cloneLoop());
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPreUpdate(data.ElementoLoop)
	 */
	public void informaPreUpdate(ElementoLoop ep) {
		ElementoLoop = ep.cloneLoop();
	}
	
	/**
	 * ritorna il contenitore dei dati
	 * @return
	 */
	public IPlugData getPlugData(){
		return plugData;
	}
    
    
    
}