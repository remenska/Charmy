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

import plugin.sequencediagram.eventi.listaDS.AddListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSSeqEleEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSTimeEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSSeqEleEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSTimeEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSTimeEvento;


/**
 * IListaDSListener � l'interface listener per ricevere la notifica quando un 
 * SequenceElement e una lista � stata  modificata, attraverso l'inserimento o la cancellazione di uno
 * qualsiasi dei suoi elementi, l'implementazione di questa interfaccia deve essere registrata
 * nel metodo SequencesElement.addListener() per ricevere la notifica.  
 * viene utilizzato per creare un unico generatore di eventi per le liste Time, SeqLink, Classe
 * in pi� nei messaggi aggiunge solo il SequenceElement di appartenenza
 **<p>
 *
 * @author Stoduto
 * @version Charmy 2.1
 */
public interface IListaDSListener  extends EventListener, Serializable{
	/**
	 * Chiamata quando un ElementoClasse sta per essere inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 *
	 */
	public void classeAdded(AddListaDSClasseEvento event);
//		throws Exception;
    

	/**
	 * Chiamata quando un ElementoStato sta per essere rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 *
	 */
	public void classeRemoved(RemoveListaDSClasseEvento event);
//		throws Exception;

	/**
	 * Chiamata quando un ElementoClasse sta per essere modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 *
	 * @exception the listener may throw any Exception, which will be
	 * propagated to the code which invoked the original graph modification
	 * method
	 */
	public void classeUpdate(UpdateListaDSClasseEvento event);
//		throws Exception;
    


	/**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void seqLinkAdded(AddListaDSLinkEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void seqLinkRemoved(RemoveListaDSLinkEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void seqLinkUpdate(UpdateListaDSLinkEvento event);



	/**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void timeAdded(AddListaDSTimeEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void timeRemoved(RemoveListaDSTimeEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void timeUpdate(UpdateListaDSTimeEvento event);


	/**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void constraintAdded(AddListaDSConstraintEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void constraintRemoved(RemoveListaDSConstraintEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void constraintUpdate(UpdateListaDSConstraintEvento event);
	
        /**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void parallelAdded(AddListaDSParallelEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void parallelRemoved(RemoveListaDSParallelEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void parallelUpdate(UpdateListaDSParallelEvento event);
        
        /**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void SimAdded(AddListaDSSimEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void SimRemoved(RemoveListaDSSimEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void SimUpdate(UpdateListaDSSimEvento event);
        
        /**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void LoopAdded(AddListaDSLoopEvento event);

	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void LoopRemoved(RemoveListaDSLoopEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void LoopUpdate(UpdateListaDSLoopEvento event);
	
	/**
	 * Chiamata quando un Elemento � stato inserito nella lista
	 * @param event l'evento che descrive il tipo di inserimento
	 */
	public void sequenceAdded(AddListaDSSeqEleEvento event);


	/**
	 * Chiamata quando un Elemento � stato rimosso dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void sequenceRemoved(RemoveListaDSSeqEleEvento event);

	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
//	public void sequenceUpdate(UpdateListaDSTimeEvento event);



	/**
	 * Chiamata quando un Elemento � stato modificato dalla lista
	 * @param event l'evento che descrive il tipo di rimozione
	 */
	public void listaDSRefresh();


}
