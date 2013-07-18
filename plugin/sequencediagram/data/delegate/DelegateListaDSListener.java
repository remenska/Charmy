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

import plugin.sequencediagram.data.ListaDS;
import plugin.sequencediagram.data.SequenceElement;
import plugin.sequencediagram.eventi.listaDS.AddListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSSeqEleEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSTimeEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSSeqEleEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSTimeEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSTimeEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceConstraintEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceLoopEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceParallelEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceSimEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceTimeEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceConstraintEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceLoopEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceParallelEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceSimEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceTimeEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceConstraintEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceLoopEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceParallelEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceSimEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceTimeEvento;
import plugin.sequencediagram.pluglistener.IListaDSListener;
import plugin.sequencediagram.pluglistener.ISequenceElementListener;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.delegate.DelegateBase;

/**
 * implementazione di un listener per la Lista di processo
 * viene utilizzata come delega per la classe ListaProcesso
 * 
 * @author michele
 * Charmy plug-in
 **/
public class DelegateListaDSListener
	extends DelegateBase
	implements ISequenceElementListener {
	/**
	 * lista del processo di cui viene effettuata la delega
	 */
	private ListaDS listaDS;


	/**
	 * costruttore
	 * @param dati globali
	 * @param lp SequenceElement di cui si opera la delega
	 * 
	 */
	public DelegateListaDSListener(IPlugData pd, ListaDS lds) {
		super(pd);
		listaDS = lds;

	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#classeAdded(eventi.sequence.AddSequenceClasseEvento)
	 */
	public void classeAdded(AddSequenceClasseEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddListaDSClasseEvento evt =
				new AddListaDSClasseEvento(
					event.getElementoClasse(),
					event.getListaClasse(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.classeAdded(evt);
			}
			stopSessione(bo);
		}
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#classeRemoved(eventi.sequence.RemoveSequenceClasseEvento)
	 */
	public void classeRemoved(RemoveSequenceClasseEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveListaDSClasseEvento evt =
				new RemoveListaDSClasseEvento(
					event.getElementoClasse(),
					event.getListaClasse(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.classeRemoved(evt);
			}
			stopSessione(bo);
		}
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#classeUpdate(eventi.sequence.UpdateSequenceClasseEvento)
	 */
	public void classeUpdate(UpdateSequenceClasseEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			UpdateListaDSClasseEvento evt =
				new UpdateListaDSClasseEvento(
					event.getVecchioElementoClasse(),
					event.getNuovoElementoClasse(),
					event.getListaClasse(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.classeUpdate(evt);
			}
			stopSessione(bo);
		}	
		
	}
	
	

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#timeAdded(eventi.sequence.AddSequenceTimeEvento)
	 */
	public void timeAdded(AddSequenceTimeEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddListaDSTimeEvento evt =
				new AddListaDSTimeEvento(
					event.getElementoTime(),
					event.getListaTime(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.timeAdded(evt);
			}
			stopSessione(bo);
		}	
	}




	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#timeRemoved(eventi.sequence.RemoveSequenceTimeEvento)
	 */
	public void timeRemoved(RemoveSequenceTimeEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveListaDSTimeEvento evt =
				new RemoveListaDSTimeEvento(
					event.getElementoTime(),
					event.getListaTime(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.timeRemoved(evt);
			}
			stopSessione(bo);
		}		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#timeUpdate(eventi.sequence.UpdateSequenceTimeEvento)
	 */
	public void timeUpdate(UpdateSequenceTimeEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			UpdateListaDSTimeEvento evt =
				new UpdateListaDSTimeEvento(
					event.getVecchioElementoTime(),
					event.getNuovoElementoTime(),
					event.getListaTime(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.timeUpdate(evt);
			}
			stopSessione(bo);
		}		
	}
	

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#seqLinkAdded(eventi.sequence.AddSequenceLinkEvento)
	 */
	public void seqLinkAdded(AddSequenceLinkEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddListaDSLinkEvento evt =
				new AddListaDSLinkEvento(
					event.getElementoSeqLink(),
					event.getListaSeqLink(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.seqLinkAdded(evt);
			}
			stopSessione(bo);
		}	
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#seqLinkRemoved(eventi.sequence.RemoveSequenceLinkEvento)
	 */
	public void seqLinkRemoved(RemoveSequenceLinkEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveListaDSLinkEvento evt =
				new RemoveListaDSLinkEvento(
					event.getElementoSeqLink(),
					event.getListaSeqLink(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.seqLinkRemoved(evt);
			}
			stopSessione(bo);
		}	
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#seqLinkUpdate(eventi.sequence.UpdateSequenceLinkEvento)
	 */
	public void seqLinkUpdate(UpdateSequenceLinkEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			UpdateListaDSLinkEvento evt =
				new UpdateListaDSLinkEvento(
					event.getVecchioElementoSeqLink(),
					event.getNuovoElementoSeqLink(),
					event.getListaSeqLink(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.seqLinkUpdate(evt);
			}
			stopSessione(bo);
		}
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListener#sequenceRefresh()
	 */
	public void sequenceRefresh() {
		if (size() > 0) {
			boolean bo = startSessione();

			Iterator ite = iterator();
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.listaDSRefresh();
			}
			stopSessione(bo);
		}
		
	}


	/*
	 * Gestione eventi propri di listaDS 
	 *
	 *
	 */

	 /**
	  * Chiamata quando un Elemento ? stato inserito nella lista
	  * @param event l'evento che descrive il tipo di inserimento
	  */
	 public void sequenceAdded(SequenceElement event){
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddListaDSSeqEleEvento evt =
				new AddListaDSSeqEleEvento(
					event,
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.sequenceAdded(evt);
			}
			stopSessione(bo);
		}		 	
	 }

	 /**
	  * Chiamata quando un Elemento ? stato rimosso dalla lista
	  * @param event l'evento che descrive il tipo di rimozione
	  */
	 public void sequenceRemoved(SequenceElement event){
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveListaDSSeqEleEvento evt =
				new RemoveListaDSSeqEleEvento(
					event,
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
					(IListaDSListener) ite.next();
				itl.sequenceRemoved(evt);
			}
			stopSessione(bo);
		}		 		 	
	 }


	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#constraintAdded(eventi.sequence.AddSequenceConstraintEvento)
	 */
	public void constraintAdded(AddSequenceConstraintEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddListaDSConstraintEvento evt =
			new AddListaDSConstraintEvento(
					event.getElementoConstraint(),
					event.getListaConstraint(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.constraintAdded(evt);
			}
			stopSessione(bo);
		}	
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#constraintRemoved(eventi.sequence.RemoveSequenceConstraintEvento)
	 */
	public void constraintRemoved(RemoveSequenceConstraintEvento event) {

		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveListaDSConstraintEvento evt =
			new RemoveListaDSConstraintEvento(
					event.getElementoConstraint(),
					event.getListaConstraint(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.constraintRemoved(evt);
			}
			stopSessione(bo);
		}			
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#constraintUpdate(eventi.sequence.UpdateSequenceConstraintEvento)
	 */
	public void constraintUpdate(UpdateSequenceConstraintEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			UpdateListaDSConstraintEvento evt =
			new UpdateListaDSConstraintEvento(
					event.getVecchioElementoConstraint(),
					event.getNuovoElementoConstraint(),
					event.getListaConstraint(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.constraintUpdate(evt);
			}
			stopSessione(bo);
		}
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#parallelAdded(eventi.sequence.AddSequenceParallelEvento)
	 */
	public void parallelAdded(AddSequenceParallelEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddListaDSParallelEvento evt =
			new AddListaDSParallelEvento(
					event.getElementoParallel(),
					event.getListaParallel(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.parallelAdded(evt);
			}
			stopSessione(bo);
		}	
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#parallelRemoved(eventi.sequence.RemoveSequenceParallelEvento)
	 */
	public void parallelRemoved(RemoveSequenceParallelEvento event) {

		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveListaDSParallelEvento evt =
			new RemoveListaDSParallelEvento(
					event.getElementoParallel(),
					event.getListaParallel(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.parallelRemoved(evt);
			}
			stopSessione(bo);
		}			
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#parallelUpdate(eventi.sequence.UpdateSequenceParallelEvento)
	 */
	public void parallelUpdate(UpdateSequenceParallelEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			UpdateListaDSParallelEvento evt =
			new UpdateListaDSParallelEvento(
					event.getVecchioElementoParallel(),
					event.getNuovoElementoParallel(),
					event.getListaParallel(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.parallelUpdate(evt);
			}
			stopSessione(bo);
		}
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#SimAdded(eventi.sequence.AddSequenceSimEvento)
	 */
	public void SimAdded(AddSequenceSimEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddListaDSSimEvento evt =
			new AddListaDSSimEvento(
					event.getElementoSim(),
					event.getListaSim(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.SimAdded(evt);
			}
			stopSessione(bo);
		}	
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#SimRemoved(eventi.sequence.RemoveSequenceSimEvento)
	 */
	public void SimRemoved(RemoveSequenceSimEvento event) {

		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveListaDSSimEvento evt =
			new RemoveListaDSSimEvento(
					event.getElementoSim(),
					event.getListaSim(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.SimRemoved(evt);
			}
			stopSessione(bo);
		}			
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#SimUpdate(eventi.sequence.UpdateSequenceSimEvento)
	 */
	public void SimUpdate(UpdateSequenceSimEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			UpdateListaDSSimEvento evt =
			new UpdateListaDSSimEvento(
					event.getVecchioElementoSim(),
					event.getNuovoElementoSim(),
					event.getListaSim(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.SimUpdate(evt);
			}
			stopSessione(bo);
		}
	}
        
         /* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#LoopAdded(eventi.sequence.AddSequenceLoopEvento)
	 */
	public void LoopAdded(AddSequenceLoopEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			AddListaDSLoopEvento evt =
			new AddListaDSLoopEvento(
					event.getElementoLoop(),
					event.getListaLoop(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.LoopAdded(evt);
			}
			stopSessione(bo);
		}	
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#LoopRemoved(eventi.sequence.RemoveSequenceLoopEvento)
	 */
	public void LoopRemoved(RemoveSequenceLoopEvento event) {

		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			RemoveListaDSLoopEvento evt =
			new RemoveListaDSLoopEvento(
					event.getElementoLoop(),
					event.getListaLoop(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.LoopRemoved(evt);
			}
			stopSessione(bo);
		}			
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#LoopUpdate(eventi.sequence.UpdateSequenceLoopEvento)
	 */
	public void LoopUpdate(UpdateSequenceLoopEvento event) {
		if (size() > 0) {
			boolean bo = startSessione();
			long idSessione = plugData.getPlugDataManager().getIdSessione();
			Iterator ite = iterator();
			UpdateListaDSLoopEvento evt =
			new UpdateListaDSLoopEvento(
					event.getVecchioElementoLoop(),
					event.getNuovoElementoLoop(),
					event.getListaLoop(),
					event.getSorgente(),
					listaDS,
					idSessione);
			while (ite.hasNext()) {
				IListaDSListener itl =
				(IListaDSListener) ite.next();
				itl.LoopUpdate(evt);
			}
			stopSessione(bo);
		}
	}

}
