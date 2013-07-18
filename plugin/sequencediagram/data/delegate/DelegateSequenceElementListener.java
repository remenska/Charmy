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

import plugin.sequencediagram.data.SequenceElement;
import plugin.sequencediagram.eventi.listaConstraint.AddConstraintEvento;
import plugin.sequencediagram.eventi.listaConstraint.RemoveConstraintEvento;
import plugin.sequencediagram.eventi.listaConstraint.UpdateConstraintEvento;
import plugin.sequencediagram.eventi.listaLoop.AddLoopEvento;
import plugin.sequencediagram.eventi.listaLoop.RemoveLoopEvento;
import plugin.sequencediagram.eventi.listaLoop.UpdateLoopEvento;
import plugin.sequencediagram.eventi.listaParallel.AddParallelEvento;
import plugin.sequencediagram.eventi.listaParallel.RemoveParallelEvento;
import plugin.sequencediagram.eventi.listaParallel.UpdateParallelEvento;
import plugin.sequencediagram.eventi.listaSim.AddSimEvento;
import plugin.sequencediagram.eventi.listaSim.RemoveSimEvento;
import plugin.sequencediagram.eventi.listaSim.UpdateSimEvento;
import plugin.sequencediagram.eventi.listaTime.AddTimeEvento;
import plugin.sequencediagram.eventi.listaTime.RemoveTimeEvento;
import plugin.sequencediagram.eventi.listaTime.UpdateTimeEvento;
import plugin.sequencediagram.eventi.listaclasse.AddClasseEvento;
import plugin.sequencediagram.eventi.listaclasse.RemoveClasseEvento;
import plugin.sequencediagram.eventi.listaclasse.UpdateClasseEvento;
import plugin.sequencediagram.eventi.listaseqlink.AddSeqLinkEvento;
import plugin.sequencediagram.eventi.listaseqlink.RemoveSeqLinkEvento;
import plugin.sequencediagram.eventi.listaseqlink.UpdateSeqLinkEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceTimeEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceTimeEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceTimeEvento;
import plugin.sequencediagram.pluglistener.IListaClasseListener;
import plugin.sequencediagram.pluglistener.IListaConstraintListener;
import plugin.sequencediagram.pluglistener.IListaLoopListener;
import plugin.sequencediagram.pluglistener.IListaParallelListener;
import plugin.sequencediagram.pluglistener.IListaSeqLinkListener;
import plugin.sequencediagram.pluglistener.IListaSimListener;
import plugin.sequencediagram.pluglistener.IListaTimeListener;
import plugin.sequencediagram.pluglistener.ISequenceElementListener;
import plugin.statediagram.data.ElementoStato;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

/**
 * implementazione di un listener per la Lista di processo
 * viene utilizzata come delega per la classe ListaProcesso
 * 
 * @author michele
 * Charmy plug-in
 **/
public class DelegateSequenceElementListener
	extends DelegateBase
	implements IListaClasseListener, 
                    IListaTimeListener, 
                    IListaSeqLinkListener,
                    IListaConstraintListener,
                    IListaParallelListener,
                    IListaSimListener,
                    IListaLoopListener
{
	/**
	 * lista del processo di cui viene effettuata la delega
	 */
	private SequenceElement sequenceElement;

	/*
	 * elemento temporaneo, utilizzato nel pre update
	 */
	private ElementoStato elementoStato;
	/**
	 * costruttore
	 * @param dati globali
	 * @param lp SequenceElement di cui si opera la delega
	 * 
	 */
	public DelegateSequenceElementListener(IPlugData pd, SequenceElement lp) {
		super(pd);
		sequenceElement = lp;

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaClasseListener#classeAdded(eventi.listaclasse.AddClasseEvento)
	 */
	public void classeAdded(AddClasseEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddSequenceClasseEvento evt =
				new AddSequenceClasseEvento(
					event.getElementoClasse(),
					event.getSorgente(),
					sequenceElement,
					idSessione);
			while (ite.hasNext()) {
				ISequenceElementListener itl =
					(ISequenceElementListener) ite.next();
				itl.classeAdded(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaClasseListener#classeRemoved(eventi.listaclasse.RemoveClasseEvento)
	 */
	public void classeRemoved(RemoveClasseEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveSequenceClasseEvento evt =
				new RemoveSequenceClasseEvento(
					event.getElementoClasse(),
					event.getSorgente(),
					sequenceElement,
					idSessione);			
			sequenceElement.getListaSeqLink().removeAllLink(event.getElementoClasse());
			
			
			while (ite.hasNext()) {
				ISequenceElementListener itl =
					(ISequenceElementListener) ite.next();
				itl.classeRemoved(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaClasseListener#classeUpdate(eventi.listaclasse.UpdateClasseEvento)
	 */
	public void classeUpdate(UpdateClasseEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			UpdateSequenceClasseEvento evt =
				new UpdateSequenceClasseEvento(
					event.getVecchioElementoClasse(),
					event.getNuovoElementoClasse(),
					event.getSorgente(),
					sequenceElement,
					idSessione);
			while (ite.hasNext()) {
				ISequenceElementListener itl =
					(ISequenceElementListener) ite.next();
				itl.classeUpdate(evt);
			}
			stopSessione(bo);
		}		

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaClasseListener#canaleRefresh()
	 */
	public void classeRefresh() {
		timeRefresh();
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaTimeListener#timeAdded(eventi.listatime.AddTimeEvento)
	 */
	public void timeAdded(AddTimeEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddSequenceTimeEvento evt =
				new AddSequenceTimeEvento(
					event.getElementoTime(),
					event.getSorgente(),
					sequenceElement,
					idSessione);
			while (ite.hasNext()) {
				ISequenceElementListener itl =
					(ISequenceElementListener) ite.next();
				itl.timeAdded(evt);
			}
			stopSessione(bo);
		}		

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaTimeListener#timeRemoved(eventi.listatime.RemoveTimeEvento)
	 */
	public void timeRemoved(RemoveTimeEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveSequenceTimeEvento evt =
				new RemoveSequenceTimeEvento(
					event.getElementoTime(),
					event.getSorgente(),
					sequenceElement,
					idSessione);
			while (ite.hasNext()) {
				ISequenceElementListener itl =
					(ISequenceElementListener) ite.next();
				itl.timeRemoved(evt);
			}
			stopSessione(bo);
		}		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaTimeListener#timeUpdate(eventi.listatime.UpdateTimeEvento)
	 */
	public void timeUpdate(UpdateTimeEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			UpdateSequenceTimeEvento evt =
				new UpdateSequenceTimeEvento(
					event.getVecchioElementoTime(),
					event.getNuovoElementoTime(),
					event.getSorgente(),
					sequenceElement,
					idSessione);
			while (ite.hasNext()) {
				ISequenceElementListener itl =
					(ISequenceElementListener) ite.next();
				itl.timeUpdate(evt);
			}
			stopSessione(bo);
		}		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaTimeListener#timeRefresh()
	 */
	public void timeRefresh() {
		if (size() > 0) {
			boolean bo = startSessione();

			Iterator ite = iterator();
			while (ite.hasNext()) {
				ISequenceElementListener itl =
					(ISequenceElementListener) ite.next();
				itl.sequenceRefresh();
			}
			stopSessione(bo);
		}		

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaSeqLinkListener#seqLinkAdded(eventi.listaseqlink.AddSeqLinkEvento)
	 */
	public void seqLinkAdded(AddSeqLinkEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddSequenceLinkEvento evt =
				new AddSequenceLinkEvento(
					event.getElementoSeqLink(),
					event.getSorgente(),
					sequenceElement,
					idSessione);
			while (ite.hasNext()) {
				ISequenceElementListener itl =
					(ISequenceElementListener) ite.next();
				itl.seqLinkAdded(evt);
			}
			stopSessione(bo);
		}		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaSeqLinkListener#seqLinkRemoved(eventi.listaseqlink.RemoveSeqLinkEvento)
	 */
	public void seqLinkRemoved(RemoveSeqLinkEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveSequenceLinkEvento evt =
				new RemoveSequenceLinkEvento(
					event.getElementoSeqLink(),
					event.getSorgente(),
					sequenceElement,
					idSessione);
			while (ite.hasNext()) {
				ISequenceElementListener itl =
					(ISequenceElementListener) ite.next();
				itl.seqLinkRemoved(evt);
			}
			stopSessione(bo);
		}		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaSeqLinkListener#seqLinkUpdate(eventi.listaseqlink.UpdateSeqLinkEvento)
	 */
	public void seqLinkUpdate(UpdateSeqLinkEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			UpdateSequenceLinkEvento evt =
				new UpdateSequenceLinkEvento(
					event.getVecchioElementoSeqLink(),
					event.getNuovoElementoSeqLink(),
					event.getSorgente(),
					sequenceElement,
					idSessione);
			while (ite.hasNext()) {
				ISequenceElementListener itl =
					(ISequenceElementListener) ite.next();
				itl.seqLinkUpdate(evt);
			}
			stopSessione(bo);
		}		

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaSeqLinkListener#seqLinkRefresh()
	 */
	public void seqLinkRefresh() {
		timeRefresh();
	}
	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaConstraintListener#constraintRefresh()
	 */
	public void constraintRefresh() {
		timeRefresh();
		
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaParallelListener#constraintRefresh()
	 */
	public void parallelRefresh() {
		timeRefresh();
		
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaSimListener#constraintRefresh()
	 */
	public void SimRefresh() {
		timeRefresh();
		
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaLoopListener#LoopRefresh()
	 */
	public void LoopRefresh() {
		timeRefresh();
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaConstraintListener#constraintAdded(eventi.listaConstraint.AddConstraintEvento)
	 */
	public void constraintAdded(AddConstraintEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaConstraintListener#constraintRemoved(eventi.listaConstraint.RemoveConstraintEvento)
	 */
	public void constraintRemoved(RemoveConstraintEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaConstraintListener#constraintUpdate(eventi.listaConstraint.UpdateConstraintEvento)
	 */
	public void constraintUpdate(UpdateConstraintEvento event) {
	}
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaParallelListener#ParallelAdded(eventi.listaParallel.AddParallelEvento)
	 */
	public void parallelAdded(AddParallelEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaParallelListener#ParallelRemoved(eventi.listaParallel.RemoveParallelEvento)
	 */
	public void parallelRemoved(RemoveParallelEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaParallelListener#ParallelUpdate(eventi.listaParallel.UpdateParallelEvento)
	 */
	public void parallelUpdate(UpdateParallelEvento event) {
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaSimListener#SimAdded(eventi.listaSim.AddSimEvento)
	 */
	public void SimAdded(AddSimEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaSimListener#SimRemoved(eventi.listaSim.RemoveSimEvento)
	 */
	public void SimRemoved(RemoveSimEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaSimListener#SimUpdate(eventi.listaSim.UpdateSimEvento)
	 */
	public void SimUpdate(UpdateSimEvento event) {
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaLoopListener#LoopAdded(eventi.listaLoop.AddLoopEvento)
	 */
	public void LoopAdded(AddLoopEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaLoopListener#LoopRemoved(eventi.listaLoop.RemoveLoopEvento)
	 */
	public void LoopRemoved(RemoveLoopEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaLoopListener#LoopUpdate(eventi.listaLoop.UpdateLoopEvento)
	 */
	public void LoopUpdate(UpdateLoopEvento event) {
	}


}
