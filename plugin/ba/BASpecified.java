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
    

package plugin.ba;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import plugin.sequencediagram.data.ElementoSeqLink;
import plugin.sequencediagram.data.ElementoTime;
import plugin.sequencediagram.data.ListaConstraint;
import plugin.sequencediagram.data.ListaDS;
import plugin.sequencediagram.data.SequenceElement;
import plugin.statediagram.data.ListaDP;
import plugin.topologydiagram.data.ListaCanale;
import plugin.topologydiagram.resource.data.ImpElementoId;

/*
 *
 *@author FLAMEL 2005
 */
/** Questa classe e' utilizzata per produrre la formula LTL a partire dai sequence diagram
	disegnati dall'utente_ 
	Tale classe contiene il metodo pubblico visualizza(), che mostra in una finestra il
	risultato della formula LTL_
	Un importante vincolo da rispettare ?: 
	L'INSIEME DEI MESSAGGI SCAMBIATI TRA LE COMPONENTI DEL SEQUENCE DIAGRAM DEVE ESSERE UN 
	SOTTOINSIEME DI TUTTI I MESSAGGI DEFINITI IN OGNI DIAGRAMMA DI STATO!!
	Questo vincolo e' dovuto al fatto che per produrre la specifica Promela si fa uso di 
	un array di canali, presi dall'insieme dei messaggi scambiati nei state diagram_ 
	Allora, se per produrre la formula LTL si utilizzasse un canale dello scenario non 
	specificato in un qualche state diagram definito dall'utente, si potrebbe indirizzare 
	un indice inesistente dell'array. */

public class BASpecified extends ImpElementoId
{

    /** Memorizza la lista di stringhe che rappresenta il risultato 
            della specifica LTL */
    private LinkedList risultatoBA;
  	  
    /** Memorizza il numero di define*/
    private int countDefine;
    
    /** Memorizza il numero di nodi*/
    private int countNode;
    
    /*Memorizzano rispettivamente la prima e lA
     *seconda stringa da inserire nel link aggiunto di un
     *eventuale operatore di simultaneita */
    private String link_1;
    private String link_2;
    
    /** Memorizza il nodo iniziale per l'operazione si simultaneita */
    private int init_n_for_sim;
    
    //private String define="";
        
    private Buchi buchi;
    
    private ListBuchi listBuchi;
    
    private BAWindow baWindow;
    
	private ListaDP lDP;
	
/** Il costruttore prende in input la lista dei canali attualmente definiti 
	nell'architettura, l'insieme delle dinamiche di processo (tutti i diagrammi 
	di stato), l'insieme delle dinamiche di sistema (tutti i sequence diagram) 
	definite dall'utente, e due caratteri_ Il primo carattere determina la particolare 
	formula da produrre_ In particolare, se tale variabile assume valore 'A' verr? 
	prodotta la formula di tipo a), se invece assume valore 'B' verr? prodotta la 
	formula LTL di tipo b)_ Nel primo caso si assume che possano, eventualmente, 
	essere generati altri messaggi tra due qualsiasi messaggi consecutivi della lista, 
	mentre nel secondo caso tale possibilit? e' preclusa_ Il secondo carattere, preso 
	come ultimo parametro, ha il seguente significato: 
	'E' per esprimere la condizione "Esiste", 
	'F' per esprimere la condizione "Per ogni"_ 
	La formula LTL generata viene memorizzata come una sequenza di stringhe in una 
	lista collegata semplice (LinkedList) chiamata risultatoLTL. */ 
	public BASpecified(ListaCanale lCan, ListaDP lDP, ListaDS lDS,  BAWindow rifBA){
        this.lDP=lDP;
        baWindow = rifBA;       
		buchi=new Buchi();
                listBuchi = new ListBuchi();
                listBuchi.addElement(buchi);
		// Crea memoria per contenere il risultato.
		risultatoBA = new LinkedList();			
		// Avvio il processo di traduzione
		start(lCan, lDP, lDS);	                 
	}

 
	/** Tale funzione fa partire il processo di traduzione.
	 * per ogni sequence
	 *  */ 
	private void start(ListaCanale lCan, ListaDP lDP, ListaDS lDS){ 
		ListaConstraint lCon;
 		// Produco una formula LTL per ogni scenario definito.
        for (int i=0;i<lDS.size();i++){
            lCon = ((SequenceElement)lDS.get(i)).getListaConstraint();
	 		// Aggiungo una riga vuota.
	 		String row = "";
	 		risultatoBA.addFirst(row);		 
	 		// Fa partire il processo di traduzione a seconda del parametro tipo.
	 		calcolaBA(risultatoBA,(SequenceElement)(lDS.get(i)),lCon);
		} 
	}


	/** Questo metodo permette di recuperare il risultato prodotto 
		dall'algoritmo di traduzione per la formula LTL_ Esso restituisce
		una lista di stringhe che rappresentano la formula LTL prodotta. */
	public LinkedList getBA(){
		return risultatoBA;
	}
	

	/** Questo metodo visualizza il risultato della fornula LTL prodotta */
	public void visualizza(){
	} 
                
        /**
         * modifica del tipo di restituzione
       * @param risultato
         * @param ds
         * @param lC
         */
        
   private void calcolaBA(LinkedList risultato, SequenceElement ds,ListaConstraint lC)
	{            

	 	// Leggo la lista di tutti i messaggi, con eventuali ripetizioni, 
	 	// scambiati tra le componenti dello scenario.
	   
	    //ezio 2006 fixed bug - consecutività dei messaggi 
	 	Iterator scorriMessaggiTemporalmente = ds.getListaSeqLink().iteratorTemporal();	 	
	 	LinkedList messaggi = new LinkedList();
	 	while(scorriMessaggiTemporalmente.hasNext()){
	 		messaggi.add(scorriMessaggiTemporalmente.next());	 		
	 	}
	 	////////
	 			
		//if (l==null) return; mai null
		if (messaggi.size()==0) return;

		// Inizializzo le liste. 
		LinkedList ris = new LinkedList();
		String strTMP;

	 	// Recupero il nome dello scenario (sequence diagram).
	 	String nomeScenario = ds.getNomeSequence();
                String formula="";
                String define="";
		// Scandisco la lista dei messaggi dello scenario.
               PSC2BA psc = new PSC2BA(messaggi,lDP); 
               buchi = psc.getBuchi(); 
               formula = buchi.stringForPrint();
               ris.add(formula);
               buchi=new Buchi();
	       for (int i=0; i<ris.size(); i++){
                 strTMP = (String)(ris.get(i));
                  risultato.add(strTMP);
		}
               BAEdit le = baWindow.getEdit(ds);
               le.setTypeBA_text(ris);
	}


	/** Funzione per mostrare l'errore. */			
	private void errore(String s) 
	{
		String temp="Si e' verificato un errore in LTLSpecified.java \n";
		JOptionPane.showMessageDialog(null,temp+s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
		System.exit(0);
	}


	/** Debugging. */
	private void debug(LinkedList l)
	{
		for (int i=0;i<l.size();i++){
	 		ElementoSeqLink link = (ElementoSeqLink)(l.get(i));
	 		ElementoTime tDa = (ElementoTime)(link.getTimeFrom());
	 		ElementoTime tA = (ElementoTime)(link.getTimeTo());
    	}	 
		System.exit(0);	
	}

}	// End BASpecified.