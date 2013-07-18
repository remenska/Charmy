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
    

package plugin.sequencediagram.pluglistener;

import java.io.Serializable;
import java.util.EventListener;

import plugin.sequencediagram.eventi.sequence.AddSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceConstraintEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceParallelEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceSimEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceLoopEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceTimeEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceConstraintEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceParallelEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceSimEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceLoopEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceTimeEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceConstraintEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceParallelEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceSimEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceLoopEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceTimeEvento;


/**
 *ISequenceElementListener è  l'interfaccia per ricevere la notifica
 *da parte di SequenceElement di un cambiamento di stato
 *degli elementi in esso contenuti.
 *Tale cambiamento si verifica nel caso della modifica,
 *inserimento o  cancellazione di uno qualsiasi degli elementi
 *contenuti nelle liste che compongono SequenceElement ovvero di
 *ListaConstraint, ListaTime, ListaClasse e ListaSeqLink.
 *La classe che implementa l'interfaccia deve essere registrata
 *attraverso il metodo SequencesElement.addListener() dopo questo si
 *è abilitati per ricevere notifica del cambiamento di stato.
 *Questi messaggi riflettono il fatto che SequenceElementListener è
 *utilizzato per creare un unico generatore di eventi per le
 *ListaTime, ListaSeqLink, ListaClasse e ListaConstraint,
 *inoltre dagli eventi è possibile risalire la scala dei livelli dei messaggi.
 * @author Flamel
 * @version Charmy 2.1
 */
public interface ISequenceElementListener  extends EventListener, Serializable{
	/**
	 * Chiamata quando un ElementoClasse sta per essere inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 *
	 */
	public void classeAdded(AddSequenceClasseEvento event);
//		throws Exception;
    

	/**
	 * Chiamata quando un ElementoStato sta per essere rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 *
	 */
	public void classeRemoved(RemoveSequenceClasseEvento event);
//		throws Exception;

	/**
	 * Chiamata quando un ElementoClasse sta per essere modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 *
	 * @exception the listener may throw any Exception, which will be
	 * propagated to the code which invoked the original graph modification
	 * method
	 */
	public void classeUpdate(UpdateSequenceClasseEvento event);
//		throws Exception;
    


	/**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void seqLinkAdded(AddSequenceLinkEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void seqLinkRemoved(RemoveSequenceLinkEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void seqLinkUpdate(UpdateSequenceLinkEvento event);



	/**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void timeAdded(AddSequenceTimeEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void timeRemoved(RemoveSequenceTimeEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void timeUpdate(UpdateSequenceTimeEvento event);

	
	

	/**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void constraintAdded(AddSequenceConstraintEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void constraintRemoved(RemoveSequenceConstraintEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void constraintUpdate(UpdateSequenceConstraintEvento event);

        /**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void parallelAdded(AddSequenceParallelEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void parallelRemoved(RemoveSequenceParallelEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void parallelUpdate(UpdateSequenceParallelEvento event);
        
        /**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void SimAdded(AddSequenceSimEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void SimRemoved(RemoveSequenceSimEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void SimUpdate(UpdateSequenceSimEvento event);

        /**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void LoopAdded(AddSequenceLoopEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void LoopRemoved(RemoveSequenceLoopEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void LoopUpdate(UpdateSequenceLoopEvento event);
	

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void sequenceRefresh();

}
