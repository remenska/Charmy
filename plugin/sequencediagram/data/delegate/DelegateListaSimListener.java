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

import plugin.sequencediagram.data.ElementoSim;
import plugin.sequencediagram.data.ListaSim;
import plugin.sequencediagram.data.interfacce.IListaSimNotify;
import plugin.sequencediagram.eventi.listaSim.AddSimEvento;
import plugin.sequencediagram.eventi.listaSim.RemoveSimEvento;
import plugin.sequencediagram.eventi.listaSim.UpdateSimEvento;
import plugin.sequencediagram.pluglistener.IListaSimListener;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

public class DelegateListaSimListener
	extends DelegateBase 
	implements IListaSimNotify {
    
    
	/**
	 * listaSim di cui viene effettuata la delega
	 */
	ListaSim listaSim;

	/**
	 * variabile di lavoro, serve per l'update
	 */
	ElementoSim ElementoSim;
        
        /**
	 * costruttore Delega listener per la listaSim
	 * @param lp lista da delegare
	 * @param pl dati di riferimento
	 */
	public DelegateListaSimListener(ListaSim lp, IPlugData pl) {
		super(pl);
		listaSim = lp;

	}

	/**
	 * ridefinizione di add di List
	 * @param lp
	 * @return
	 */
	boolean add(IListaSimListener lp) {
		return super.add((Object) lp);
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da spedire
	 */
	private void notifyAddSim(AddSimEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaSimListener ilpl = (IListaSimListener) ite.next();
			ilpl.SimAdded(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da spedire
	 */
	private void notifyRemoveSim(RemoveSimEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaSimListener ilsim = (IListaSimListener) ite.next();
			ilsim.SimRemoved(event);
		}
	}

	/**
	 * informa tutti i listener registrati
	 * @param eps evento da inviare
	 */
	private void notifyUpdateSim(UpdateSimEvento event) {
		Iterator ite = this.iterator();
		while (ite.hasNext()) {
			IListaSimListener ilsim = (IListaSimListener) ite.next();
			ilsim.SimUpdate(event);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyAdd(data.ElementoCanaleMessaggio)
	 */
	public void notifyAdd(ElementoSim ecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			//ElementoCanale ec = (ElementoCanale) ecm;
			AddSimEvento event =
				new AddSimEvento(ecm.cloneSim(), listaSim, idSessione);
			notifyAddSim(event);
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyRemove(data.ElementoCanaleMessaggio)
	 */
	public void notifyRemove(ElementoSim ecm) {

		if (size() > 0) {

			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			
			RemoveSimEvento event =
				new RemoveSimEvento(
					ecm.cloneSim(),
					listaSim,
					idSessione);
			notifyRemoveSim(event);

			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoSim, data.ElementoSim)
	 */
	public void notifyUpdate(
			ElementoSim oldecm,
			ElementoSim newecm) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();

			UpdateSimEvento event =
				new UpdateSimEvento(
					oldecm.cloneSim(),
					newecm.cloneSim(),
					listaSim,
					idSessione);
			notifyUpdateSim(event);
			stopSessione(bo);

		}
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#notifyUpdate(data.ElementoSim, data.ElementoSim)
	 */
	public void refreshNotify() {
		if (size() > 0) {
			Iterator ite = this.iterator();
			while (ite.hasNext()) {
				IListaSimListener ilpl = (IListaSimListener) ite.next();
				ilpl.SimRefresh();
			}
		}
	}

	/**
	 * ritorna la listaSim di cui si gestisce la delega 
	 * @return
	 * 
	 */
	public ListaSim getListaSim() {
		return listaSim;
	}

	/**
	 * setta la lista Sim di cui gestire gli eventi
	 * @param canale
	 */
	public void setListaSim(ListaSim time) {
		listaSim = time;
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPostUpdate(data.ElementoSim)
	 */
	public void informaPostUpdate(ElementoSim ep) {
		notifyUpdate(ElementoSim, ep.cloneSim());
	}

	/* (non-Javadoc)
	 * @see data.IListaCanMessNotify#informaPreUpdate(data.ElementoSim)
	 */
	public void informaPreUpdate(ElementoSim ep) {
		ElementoSim = ep.cloneSim();
	}
	
	/**
	 * ritorna il contenitore dei dati
	 * @return
	 */
	public IPlugData getPlugData(){
		return plugData;
	}
    
    
    
}