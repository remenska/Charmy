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
    

package plugin.sequencediagram.controllo;
import java.util.LinkedList;

import core.internal.runtime.controllo.RifToControl;
import core.internal.runtime.data.PlugDataManager;

/** Classe per gestire i nomi utilizzati per i link dei sequence diagram. */ 

public class ControlloIntegrita extends RifToControl
{

	/** Variabile che memorizza il numero di canali presenti
		negli scenari ma non usati n? nel Topology Diagram n? 
		negli state diagram: numero di errori sui canali. */
	private int errMSG = 0;
	
	/** Variabile che memorizza il numero di processi presenti
		negli scenari ma non usati n? nel Topology Diagram  
		n? negli state diagram: numero di errori sui processi 
		(classi). */
	private int errCLASS = 0;
	
	private plugin.statediagram.data.PlugData plugDataThread;

	private plugin.topologydiagram.data.PlugData plugDataTopology;
	
	private plugin.sequencediagram.data.PlugData plugDataSequence;
	 
	 
	/** Costruttore. */
	public ControlloIntegrita(PlugDataManager pdm){
		plugDataManager=pdm;
	}


	/** ... */
	public boolean checkIntegrity()
	{	
		plugDataThread=(plugin.statediagram.data.PlugData)plugDataManager.getPlugData("charmy.plugin.state");
		plugDataSequence=(plugin.sequencediagram.data.PlugData)plugDataManager.getPlugData("charmy.plugin.sequence");
		plugDataTopology=(plugin.topologydiagram.data.PlugData)plugDataManager.getPlugData("charmy.plugin.topology");

  		// Recupero tutti i messaggi definiti nei vari diagrammi di stato.
  		LinkedList listaNomiMsg = new LinkedList();
		
  		listaNomiMsg = (plugDataThread.getListaDP()).getAllMessageName();
  		
  		// Recupero i canali (messaggi) definiti nel Topology Diagram.
  		LinkedList listaNomi = new LinkedList();
  		listaNomi = (plugDataTopology.getListaCanale()).getAllMessageName();
  		
  		// Recupero tutti i messaggi definiti nei vari sequence diagram.
  		LinkedList listaCanaliSequence = new LinkedList();
  		listaCanaliSequence = (plugDataSequence.getListaDS()).getAllMessageName();
  		
  		// Recupero i processi definiti nel Topology Diagram.
  		LinkedList listaNomiProcessiTopology = new LinkedList();
  		listaNomiProcessiTopology = (plugDataTopology.getListaProcesso()).getListaAllGlobalName();
  		
  		// Recupero i processi definiti nello State Diagram.
  		LinkedList listaNomiProcessiState = new LinkedList();
  		listaNomiProcessiState =(plugDataTopology.getListaProcesso()).getListaAllUserName();
  		
  		// Recupero i processi usati negli scenari.
  		LinkedList listaNomiProcessiSequence = new LinkedList();
  		listaNomiProcessiSequence = (plugDataSequence.getListaDS()).getAllClassName();
  		
  		// Costruisco la lista dei messaggi (canali) definiti  
  		// nei diagrammi di stato o nel Topology Diagram, 
  		// eliminando i duplicati.
  		for (int i=0; i<listaNomiMsg.size(); i++){
	   		//String nome = ((String)(listaNomiMsg.get(i))).substring(1);
                        
			String nome = ((String)(listaNomiMsg.get(i)));
	   		if (!(listaNomi.contains(nome))) {
	   			listaNomi.add(nome);
	   		}
      	}
      	
      	// Conto il numero di canali presenti negli scenari ma non usati 
      	// n? nel Topology Diagram n? negli state diagram. */
      	for (int i=0; i<listaCanaliSequence.size(); i++){
      		if (!listaNomi.contains((String)(listaCanaliSequence.get(i)))){
                    if(!listaNomiMsg.contains((String)(listaCanaliSequence.get(i))))
                        errMSG++;
      		}
      	}

      	// Conto il numero di processi presenti negli scenari ma non usati 
      	// n? nel Topology Diagram n? negli state diagram. */      	
      	String tmpString;
      	for (int i=0; i<listaNomiProcessiSequence.size(); i++){
      		tmpString = (String)(listaNomiProcessiSequence.get(i));
      		if ((!listaNomiProcessiTopology.contains(tmpString))
      			&& (!listaNomiProcessiState.contains(tmpString))){
      			errCLASS++;
      		}
      	}
      	
      	if ((errMSG==0)&&(errCLASS==0)){		
			return true;
		}
		else{
			return false;
		}    			
	}
	
	
	public int getErrorMessage(){
		return errMSG;
	}
	

	public int getErrorClass(){
		return errCLASS;
	}
	
	
	public void resetError(){
		errMSG = 0;
		errCLASS = 0;
	}
	
} 