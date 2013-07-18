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
    

package plugin.promela;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import core.internal.runtime.data.IPlugData;

import plugin.topologydiagram.data.ListaCanale;
import plugin.statediagram.controllo.ThreadCheck;
import plugin.statediagram.controllo.ThreadCheck.LocalVariablesList;
import plugin.statediagram.controllo.ThreadCheck.VariablesList;
import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.data.ListaDP;
import plugin.statediagram.data.ListaStato;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.dialog.ThreadCheckWindow;
import plugin.statediagram.graph.ElementoMessaggio;
import plugin.sequencediagram.data.ListaDS;
import plugin.sequencediagram.data.SequenceElement;

import plugin.promela.dialog.*;

import plugin.promela.util.*;


/** Questa classe è utilizzata per produrre la specifica Promela a partire  
	dai diagrammi di stato e dai sequence diagram definiti dall'utente_
	La specifica Promela generata viene memorizzata come una sequenza di 
	stringhe in una lista collegata semplice chiamata risultatoSpecifica_ 
	Tale classe contiene il metodo pubblico visualizza(), che mostra in 
	una finestra il risultato della specifica Promela prodotto. */

public class PromelaSpecified 
{
	
	/** Memorizza la costante N definita nell'algoritmo. */
	private static final int N = 10; 

	/** Memorizza la dimensione del buffer per i messaggi asincroni. */
	private static final int dimBuffer = 4; 

	/** Memorizza la lista di stringhe che rappresenta il risultato della specifica Promela. */
	private LinkedList risultatoSpecifica1;

	/**Memorizza la lista dei thread che necessitano di dichiarazioni locali di variabili*/        
	private LinkedList threadWithLocalVariables;		
	
	/** Memorizza tutti gli eventi scambiati. Riempito nella funzione start0 e start1 */
	private LinkedList Events = new LinkedList();
   
	private String label="",guardia="",operazioni="";
    
	private LinkedList eventSend=new LinkedList();
    
	private LinkedList eventReceive=new LinkedList();
    
	private ChannelList chL=new ChannelList();
	
	private ChannelList chTau=new ChannelList();
	
	private IPlugData plugData;
	
	private ThreadCheck tc;
	
	private VariablesList vl;
	
	private LocalVariablesList localVar;
	
	private plugin.topologydiagram.data.PlugData pdTopology;
	
	private plugin.statediagram.data.PlugData pdThread;
	
	private plugin.sequencediagram.data.PlugData pdSequence;
        
	/** Il costruttore prende in input l'insieme delle dinamiche di processo 
		(contenenti tutti i diagrammi di stato, uno per ogni thread del processo) 
		e l'insieme degli scenari del sistema (contenenti tutti i sequence diagram). */ 
	public PromelaSpecified(IPlugData plugData,int tipoAlgo) 
	{ 
		this.plugData=plugData;
		pdTopology=(plugin.topologydiagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.topology");
		pdThread=(plugin.statediagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.state");
		pdSequence=(plugin.sequencediagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.sequence");
		tc = new ThreadCheck(plugData);
		String msg=tc.check();
		if(!msg.equals("")){
			ThreadCheckWindow tcw=new ThreadCheckWindow(null,msg);
			return;
		}

		vl=tc.extractGlobalVariables();
		localVar=tc.extractLocalVariables();

		ListaCanale lCan=pdTopology.getListaCanale();
		ListaDP lDP = pdThread.getListaDP();
		ListaDS lDS = pdSequence.getListaDS();
		// memorizza il risultato della specifica Promela
		switch (tipoAlgo){
			case 0:
				risultatoSpecifica1 = new LinkedList();
				start0(risultatoSpecifica1, lCan, lDP, lDS);
				break;
		}                
	}
    
    
	/** Questo metodo permette di recuperare il risultato prodotto 
		dall'algoritmo di traduzione per la specifica Promela_
		Esso restituisce una lista di stringhe che rappresentano 
		la specifica Promela prodotta. */
	public LinkedList getPromela1(){
		return risultatoSpecifica1;
	}
	 
	public void setEvents(LinkedList ev){
			 Events=ev;
	}
    

	public boolean isIn(int start, int end,String msg,LinkedList list)
	{
		for(int i=start;i<end;i++){
			if(((ElementoMessaggio)list.get(i)).getName().equals(msg))
				return true;
		}
		return false;
	}
    
	public boolean isPresent(int start, int end,String msg,LinkedList list)
	{
		String nome,label;
		for(int i=start;i<end;i++){
			nome=(String)list.get(i);
			String SA[]=nome.split("/");
			if (SA.length==1)
				label=SA[0];
			else if (SA.length==2)
					if (SA[0].startsWith("["))
						label=SA[1];							
					else
						label=SA[0];													 
				 else
					label=SA[1];						
			SA=eventManagement(label);
			label = SA[0];
			if (label.startsWith("?")||label.startsWith("!"))
				if(label.substring(1).compareTo(msg.substring(1))==0)
					return true;
		}
		return false;
	}


	/** Tale funzione fa partire il processo di traduzione. */ 
	private void start0(LinkedList ris, ListaCanale lCan, ListaDP lDP, ListaDS lDS)
	{  
		
		LinkedList listaMessaggi = new LinkedList();	
		listaMessaggi = lDP.getAllMessages();

		LinkedList varDecl=new LinkedList();
		for(int i=0;i<vl.size();i++){
			if(vl.get(i).getDimension()==2){				
				varDecl.add("typedef type"+i+" {bit "+(vl.get(i).getName().split("\\$."))[1]
				+"[10]};\n"+"type"+i+" "+vl.get(i).getName().split("\\$.")[0]+
				"[10];\n");
			}else{				
				if((vl.get(i)).getDimension()==1){
					varDecl.add("bit "+(vl.get(i)).getName()+"[10];\n");
				}else{
					varDecl.add("bit "+(vl.get(i)).getName()+";\n");
				}
			}
		}
		setEvents(varDecl);


		threadWithLocalVariables=new LinkedList();
		for(int i=0;i<localVar.size();i++){
			threadWithLocalVariables.add(new ThreadLocalVariables(localVar.get(i).getThreadName(),
							localVar.get(i).getList(),localVar.get(i).getProcessName()));
		}
        
        FinestraDefineDataStructuresPromela finestraDefineData = new FinestraDefineDataStructuresPromela(Events,threadWithLocalVariables,this,null,"Data Structures");
		
		Inizializza(listaMessaggi);
		calcolaB0(ris);
		calcolaA0(ris, listaMessaggi);
		
		calcolaC0(ris, lDP);
	}


	private void Inizializza(LinkedList listaMessaggi)
	{
		if(listaMessaggi==null)return;
		for (int i=0;i<listaMessaggi.size();i++){
			ElementoMessaggio canale=(ElementoMessaggio)listaMessaggi.get(i);
			String label = canale.getName();
			if((canale.getSendReceive()==ElementoMessaggio.SEND)||(canale.getSendReceive()==ElementoMessaggio.RECEIVE)){
				if ((!(isIn(0,i,label,listaMessaggi)))&&(!canale.getName().equals(""))){
						chL.addChannel(canale.getName(), canale.getParameters().size());
				}
			}
			else{
				if ((!(isIn(0,i,label,listaMessaggi)))&&(!canale.getName().equals(""))){
					chTau.addChannel(canale.getName(), 0);
				}
			}			
		}
		//chL.debug();
	}

	/** Questo metodo calcola la parte (a) dell'algoritmo. Esso prende come parametri
		il numero CN dei canali definiti nell'architettura e la lista di tutte le dinamiche 
		di processo definite. Ogni dinamica di processo contiene più thread. In questa fase 
		ci interesa solo il nome dei messaggi scambiati tra ogni possibile coppia di stati.
		Per ogni messaggio nomeCh identificato, vengono costruite due stringe: 
			# define nomeCh_S i   
			# define nomeCh_R (i+1)
		dove i e' un contatore che viene incrementato per ogni righa. */         
	private void calcolaA0(LinkedList ris, LinkedList listaMessaggi)
	{
		if (listaMessaggi==null) return;
		String row = "";
		int cont = 0;
		int numMsg=0;
		String eventi="";
		for (int i=(listaMessaggi.size()-1);i>=0;i--){
			ElementoMessaggio canale=(ElementoMessaggio)listaMessaggi.get(i);
			String label = canale.getName();
			if(!canale.getName().equals(""))
				if((canale.getSendReceive()==ElementoMessaggio.SEND)||(canale.getSendReceive()==ElementoMessaggio.RECEIVE)){
					if (!(isIn(0,i,label,listaMessaggi))){
						row = "#define "+label+" "+chL.findChannel(label).getDefineNumber();
						ris.addFirst(row);
						numMsg++;
					}
				}
				else{
					if (!(isIn(0,i,label,listaMessaggi))){
						row = "#define "+label+" "+chTau.findChannel(label).getDefineNumber();
						ris.addFirst(row);
						numMsg++;
					}					
				}
		}
	}


	/** Calcola la parte (b) della specifica Promela come descritto nell'algoritmo.*/
	private void calcolaB0(LinkedList ris)
	{
		// Aggiungo una riga vuota all'inizio della parte (b).
		String row = "";		
		ris.addLast(row);
		row = "chan  _nill  =[0] of {bit};";
		ris.addLast(row);
		row = "chan  _lst = _nill;";		
		ris.addLast(row);
		row = "chan  _lstTau = _nill;";		
		ris.addLast(row);
		String row2="";
		ChannelList chN = new ChannelList();		
		chN=chL.cloneCh();
		//chL.debug();
		//chN.debug();
		String numParameters="";                       
		if (chN!=null){
			int counter=0;
			int countChDefine=0;
			while(chN.size()!=0){
			   countChDefine++; 
			   row="chan channel";
			   row2 = "] = [0] of {";
			   chN.get(0).setDelMarked();
			   if(chN.get(0).getNumberParameters()==0)
			   		numParameters="1";
			   else
			   		numParameters=""+chN.get(0).getNumberParameters();
			   for(int j=0;j<chN.get(0).getNumberParameters();j++){
					if (j==0)
						row2+="bit";
					else
						row2+=",bit";
				}      
				if (chN.get(0).getNumberParameters()==0)
					row2+="bit";
				row2+="};";
				for(int j=0;j<chN.size();j++){
					if((chN.get(0).getNumberParameters()==0)||(chN.get(0).getNumberParameters()==1)){
						if((chN.get(j).getNumberParameters()==0)||(chN.get(j).getNumberParameters()==1)){
							chL.findChannel(chN.get(j).getName()).setDefineNumber(counter++);
							chN.setDelMarkedChannel(j); 
						}
					}
					else
						if(chN.get(j).getNumberParameters()==chN.get(0).getNumberParameters()){
							chL.findChannel(chN.get(j).getName()).setDefineNumber(counter++);
							chN.setDelMarkedChannel(j);                                
						}
				}
				for(int j=0;j<chN.size();j++){
					if(chN.isDelMarkedChannel(j)){
						chL.setChannelName(chN.getName(j), "channel"+countChDefine);
						chN.delChannel(j);
						j--;
					}
				}
				row+=numParameters+" ["+counter+row2;
				//row+=countChDefine+" ["+counter+row2;
				ris.addLast(row);
				counter=0;
			}
		}
		//chL.debug();
		for(int i=0;i<chTau.size();i++){
			chTau.setDefineNumber(i,i);
		}
		if(chTau.size()!=0){
			row = "chan channelTau ["+chTau.size()+"] = [1] of {bit};";
			ris.addLast(row);		
		}
		if((Events!=null)&&(Events.size()!=0)){
			row=(String)Events.getLast();
			ris.addLast(row);
		}
	}


	/** Traduce in Promela tutti i processi definiti nella nell'oggetto listaDP_
	In particolare traduce tutti gli stati facenti parte di ogni state diagram 
	relativo ad ogni thread del processo_ 
	La traduzione avviene per ogni thread di ogni processo. */
	private void calcolaC0(LinkedList ris, ListaDP listaDP)
	{
		ListaThread dinamicaP;
		ThreadElement thread;
		
		// Scorre la lista di tutte le dinamiche di processo e per ognuna di esse traduce 
		// tutti i thread che lo compongono.
		// Ogni thread t del processo p viene tradotto in Promela con "proctype p_t() ... ".
		for (int i=0; i<listaDP.size(); i++){
			// Recupero la dinamica di processo.
			dinamicaP = (ListaThread)(listaDP.getDinamicaProcesso(i));
			// Leggo il nome del processo.	 
			String nomeP = dinamicaP.getNameProcesso();
			// Traduco ogni thread del processo
			for (int j=0;j<dinamicaP.size();j++){
				thread = (ThreadElement)(dinamicaP.get(j));
				// traduco il thread
				traduciThread0(ris, nomeP, thread);	     
			}
		}
	}

			/** Questo metodo traduce ogni diagramma di stato rappresentante il thread. */ 
	private void traduciThread0(LinkedList ris, String nomeProc, ThreadElement thread)
	{
		// Aggiungo una riga vuota all'inizio.
		boolean endstate=false;
		String row = "";		
		ris.addLast(row); 		
		// Leggo il nome del thread.	 
		String nomeThread = thread.getNomeThread();
		
		// Aggiungo l'intestazione.
		row = "active proctype " + nomeProc + "_" + nomeThread + "() {";
		ris.addLast(row);
		String decl="";
		
		for(int i=0;i<threadWithLocalVariables.size();i++){
			if(((ThreadLocalVariables)threadWithLocalVariables.get(i)).getThread().equals(thread.getNomeThread())){
				if(((ThreadLocalVariables)threadWithLocalVariables.get(i)).getProcess().equals(thread.getListaThread().getNameProcesso())){
					decl=((ThreadLocalVariables)threadWithLocalVariables.get(i)).getLocalVariables();
				}
			}
		}
		ris.addLast(decl);
		
		
		
		// Ricavo la lista degli stati (oggetti della classe ElementoStato).
		ListaStato listaStati = thread.getListaStato();
		ElementoStato stato;
		boolean startState=false;
		//Questo for è per selezionare lo start state che nella traduzione va messo per primo
		for(int i=0;i<listaStati.size();i++){
			stato=(ElementoStato)(listaStati.getElement(i));
			//Vedo se è uno start state
			if(stato.getTipo()==0){
				startState=true;
				String nomeStato = stato.getName();
				// Leggo la lista dei messaggi associati allo stato.
				LinkedList listaMessaggi = (LinkedList)(thread.getListaMessaggio().getListaMessaggio(stato));
				// Controlla se i nomi dei messaggi sono ammissibili, ovvero iniziano con '?' o con '!'.
				//controllaMessaggi(listaMessaggi);
				// Determina la lista dei messaggi uscenti dallo stato (oggetti della classe LinkDP).
				LinkedList messaggiUscenti = calcolaMessaggiUscenti(stato, listaMessaggi);
				if (messaggiUscenti==null){
					errore("La lista dei messaggi uscenti dallo stato " + nomeStato + " e' nulla!");
				}				
				if (messaggiUscenti.size()==0){ 
					// Aggiungo la parola chiave skip.
					row = nomeStato + ":"; 		
					ris.addLast(row);
					if(stato.getOnEntryCode()!=null){
						if(!stato.getOnEntryCode().replaceAll("\n"," ").trim().equals("")){
							row = "\tatomic{\t\t"+formattaTesto(stato.getOnEntryCode())+"};";
							ris.addLast(row);
						}
					}
					row = "        goto End;";
					ris.addLast(row);
					endstate=true;
				} 
				else if (messaggiUscenti.size()==1){ 
						calcolaCaso12(ris, nomeStato, stato,messaggiUscenti);
					 }
					 else{ 
						calcolaCaso3(ris, nomeStato, stato, messaggiUscenti); 
					 }
			}
		}
	if(startState==false){
		JOptionPane.showMessageDialog(null,"Warning: a start state must be inserted in thread: "+nomeThread+" of process: "+ nomeProc);
	}
	for (int i=0;i<listaStati.size();i++){
		// Recupero lo stato nella lista.
		stato = (ElementoStato)(listaStati.getElement(i));
		if(stato.getTipo()!=0){
			String nomeStato = stato.getName();
			LinkedList listaMessaggi = (LinkedList)(thread.getListaMessaggio().getListaMessaggio(stato));
			LinkedList messaggiUscenti = calcolaMessaggiUscenti(stato, listaMessaggi);
			if (messaggiUscenti==null){
				errore("La lista dei messaggi uscenti dallo stato " + nomeStato + " e' nulla!");
			}
			if (messaggiUscenti.size()==0){ 
				row = nomeStato + ":"; 		
				ris.addLast(row);
				if(stato.getOnEntryCode()!=null){					
					if(!stato.getOnEntryCode().replaceAll("\n"," ").trim().equals("")){
						row = "\tatomic{\n\t\t"+formattaTesto(stato.getOnEntryCode())+"};";
						ris.addLast(row);
					}
				}
				row = "        goto End;";
				ris.addLast(row);
				endstate=true;
			} 
			else if (messaggiUscenti.size()==1){ 
					calcolaCaso12(ris, nomeStato,stato, messaggiUscenti);
				 }
				 else{ 
					calcolaCaso3(ris, nomeStato, stato, messaggiUscenti); 
				 }
			}
		}
	    if(endstate==true){
			row = "End:";
			ris.addLast(row);
			row = "        skip;";
			ris.addLast(row);
		}
		row = " }";
		ris.addLast(row);
	}

	/** Traduce in Promela gli stati del tipo i) e ii), 
		ossia gli stati che hanno un solo messaggio uscente. */
	private void calcolaCaso12(LinkedList ris, String nomeStato, ElementoStato stato,LinkedList listaMessaggi)
	{
		String row;
		ElementoMessaggio link = (ElementoMessaggio)(listaMessaggi.get(0));
		String label="",guardia="",operazioni="",eventSend="",eventReceive="";
		label=link.getName();
		guardia=link.getGuard();
		operazioni=link.getOperations();                          
		if(link.getParameters().size()!=0){
			if(link.getSendReceive()==ElementoMessaggio.SEND){
				for(int j=0;j<link.getParameters().size();j++){
					if(j==0)
						eventSend+=link.getParameters().get(j);
					else
						eventSend+=","+link.getParameters().get(j);
				}
			}
			else
				if(link.getSendReceive()==ElementoMessaggio.RECEIVE){
					for(int j=0;j<link.getParameters().size();j++){
						if(j==0)
							eventReceive+=link.getParameters().get(j);
						else
							eventReceive+=","+link.getParameters().get(j);
					}
				}
		}
		else{
			eventSend="0";
			eventReceive="_";
		}
		ElementoStato statoSucc = (ElementoStato)(link.getElementTo());
		// Determino il nome dello stato successivo.
		String nomeSucc = statoSucc.getName();
		if(!link.getName().equals("")){
			if(link.getSendReceive()==ElementoMessaggio.SEND){
				row = nomeStato + ":";
				ris.addLast(row);
				if(stato.getOnEntryCode()!=null){
					if(!stato.getOnEntryCode().replaceAll("\n"," ").trim().equals("")){
						row = "\tatomic{\n\t\t"+formattaTesto(stato.getOnEntryCode())+"};";
						ris.addLast(row);
					}
				}
			if (guardia.length()==0){
				if (operazioni.length()==0){
					row = "        atomic{"+chL.findChannel(label).getChannelName()+"["+ label +"]!"+eventSend+";_lst=_nill;"+"goto "+nomeSucc+"};";
					ris.addLast(row);
				}
				else{
					row = "        atomic{"+chL.findChannel(label).getChannelName()+"["+ label +"]!"+eventSend+";_lst=_nill;"+ operazioni +"goto "+nomeSucc+"};";
					ris.addLast(row);
				}
			}
			else{
				if(operazioni.length()==0){
					row = "        atomic{("+guardia.substring(1,guardia.length()-1) +") -> "+chL.findChannel(label).getChannelName()+"["+label+"]!"+eventSend+";_lst=_nill"+";goto "+nomeSucc+";};";
					ris.addLast(row);
				}
				else{
				   row = "        atomic{("+ guardia.substring(1,guardia.length()-1) +") -> "+chL.findChannel(label).getChannelName()+"["+label+"]!"+eventSend+";_lst=_nill"+";"+ operazioni +"goto "+nomeSucc+";};";
					ris.addLast(row);
				}
			}
		} 
		else 
			if(link.getSendReceive()==ElementoMessaggio.RECEIVE){
				row = nomeStato + ":";
				ris.addLast(row);
				if(stato.getOnEntryCode()!=null){
					if(!stato.getOnEntryCode().replaceAll("\n"," ").trim().equals("")){
						row = "\tatomic{\n\t\t"+formattaTesto(stato.getOnEntryCode())+"};";
						ris.addLast(row);
					}
				}
				if (guardia.length()==0){
					if (operazioni.length()==0){
						row = "        atomic{"+chL.findChannel(label).getChannelName()+"["+ label +"]?"+eventReceive+";_lst=("+chL.findChannel(label).getChannelName()+"["+label+"]);"+"goto "+nomeSucc+"};";
						ris.addLast(row);
					}
					else{
						row = "        atomic{"+chL.findChannel(label).getChannelName()+"["+ label +"]?"+eventReceive+";_lst=("+chL.findChannel(label).getChannelName()+"["+label+"]);"+ operazioni +"goto "+nomeSucc+"};";
						ris.addLast(row);
					}
				}
				else{
					if(operazioni.length()==0){
						row = "        atomic{("+ guardia.substring(1,guardia.length()-1) +") -> "+chL.findChannel(label).getChannelName()+"["+label+"]?"+eventReceive+";_lst=("+chL.findChannel(label).getChannelName()+"["+label+"]);"+"goto "+nomeSucc+";};";
						ris.addLast(row);
					}
					else{
						row = "        atomic{("+ guardia.substring(1,guardia.length()-1) +") -> "+chL.findChannel(label).getChannelName()+"["+label+"]?"+eventReceive+";"+";_lst=("+chL.findChannel(label).getChannelName()+"["+label+"]);" + operazioni+"goto "+nomeSucc+";};";
						ris.addLast(row);
					}
				}
			}
			else
				if(!label.equals("")){
					row = nomeStato + ":";
					ris.addLast(row);
					if(stato.getOnEntryCode()!=null){
						if(!stato.getOnEntryCode().replaceAll("\n"," ").trim().equals("")){
							row = "\tatomic{\n\t\t"+formattaTesto(stato.getOnEntryCode())+"};";
							ris.addLast(row);
						}
					}
					if (guardia.length()==0){
						if (operazioni.length()==0){
							row = "        atomic{channelTau"+
											"["+label+"]!0;channelTau"+
											"["+label+"]?_"+";_lstTau=(channelTau["+label+"]);"+"goto "+nomeSucc+"};";
							ris.addLast(row);
						}
						else{
							row = "        atomic{channelTau"+
											"["+label+"]!0;channelTau"+
											"["+label+"]?_;"+"_lstTau=(channelTau["+label+"]);"+operazioni+"goto "+nomeSucc+"};";
							ris.addLast(row);
						}
					}
					else{
						if(operazioni.length()==0){
							row = "        atomic{("+ guardia.substring(1,guardia.length()-1) +") -> channelTau"+
											"["+label+"]!0;channelTau"+
											"["+label+"]?_;"+"_lstTau=(channelTau["+label+"]);"+"goto "+nomeSucc+"};";
							
							ris.addLast(row);
						}else{
							
							row = "        atomic{("+ guardia.substring(1,guardia.length()-1) +") -> channelTau"+
											"["+label+"]!0;channelTau"+
											"["+label+"]?_;"+"_lstTau=(channelTau["+label+"]);"+operazioni+"goto "+nomeSucc+"};";
							ris.addLast(row);
							
						}
					}
				}      
				else{
					row = nomeStato + ":";
					ris.addLast(row);
					if(stato.getOnEntryCode()!=null){
						if(!stato.getOnEntryCode().replaceAll("\n"," ").trim().equals("")){
							row = "\tatomic{\n\t\t"+formattaTesto(stato.getOnEntryCode())+"};";
							ris.addLast(row);
						}
					}
					if (guardia.length()==0){
						if (operazioni.length()==0){
							row = "        atomic{goto "+nomeSucc+"};";
							ris.addLast(row);
						}
						else{
							row = "        atomic{"+operazioni +"goto "+nomeSucc+"};";
							ris.addLast(row);
						}
					}
					else{
						row = "        atomic{("+ guardia.substring(1,guardia.length()-1) +") -> "+";goto "+nomeSucc+";};";
						ris.addLast(row);                        
					}  
				}                    
		}
		else{
			if (guardia.length()==0){
				if (operazioni.length()==0){
					row = nomeStato + ":";
					ris.addLast(row);
					row = "        atomic{goto "+nomeSucc+"};";
					ris.addLast(row);
				}
				else{
					row = nomeStato + ":";
					ris.addLast(row);
					row = "        atomic{"+ operazioni +"goto "+nomeSucc+"};";
					ris.addLast(row);
				}
			}
			else{
				if(operazioni.length()==0){
					row = nomeStato + ":";
					ris.addLast(row);
					row = "        atomic{("+guardia.substring(1,guardia.length()-1) +") -> goto "+nomeSucc+";};";
					ris.addLast(row);
				}
				else{
					row = nomeStato + ":";
					ris.addLast(row);
				    row = "        atomic{("+ guardia.substring(1,guardia.length()-1) +") -> "+ operazioni +"goto "+nomeSucc+";};";
					ris.addLast(row);
				}
			}
		}
		guardia="";
		label="";
		operazioni="";
		eventReceive="";
		eventSend="";
	}


	public String formattaTesto(String text)
	{
		String TX[] = text.split("\n");
		for(int i=0;i<TX.length;i++)
			if(i==0)
				text=TX[i];
			else
				text+="\n\t\t"+TX[i];
		return text;
	}


	/** Traduce in Promela gli stati del tipo iii), 
		ossia gli stati in cui vi è più di un messaggio uscente. */
	private void calcolaCaso3(LinkedList ris, String nomeStato, ElementoStato stato, LinkedList listaMess)
	{
		String row;
		String accumulo;
		row = nomeStato+":";
		ris.addLast(row); 
		if(stato.getOnEntryCode()!=null){
			if(!stato.getOnEntryCode().replaceAll("\n"," ").trim().equals("")){
				row = "\tatomic{\n\t\t"+formattaTesto(stato.getOnEntryCode())+"};";
				ris.addLast(row);
			}
		}
		row = "        if";
		ris.addLast(row);
		accumulo = "                ";
		for (int j=0;j<listaMess.size();j++){
			ElementoMessaggio link = (ElementoMessaggio)(listaMess.get(j));
			String label="",guardia="",operazioni="",eventSend="",eventReceive="";
			label=link.getName();
			guardia=link.getGuard();
			operazioni=link.getOperations();                        
			if(link.getParameters().size()!=0){
				if(link.getSendReceive()==ElementoMessaggio.SEND){
					for(int k=0;k<link.getParameters().size();k++){
						if(k==0)
							eventSend+=link.getParameters().get(k);
						else
							eventSend+=","+link.getParameters().get(k);
					}
				}
				else
					if(link.getSendReceive()==ElementoMessaggio.RECEIVE){
						for(int k=0;k<link.getParameters().size();k++){
							if(k==0)
								eventReceive+=link.getParameters().get(k);
							else
								eventReceive+=","+link.getParameters().get(k);
						}
					}
			}
			else{
				eventSend="0";
				eventReceive="_";
			}
			ElementoStato statoSucc = (ElementoStato)(link.getElementTo());
			String nomeSucc = statoSucc.getName();
		    if(!link.getName().equals("")){
				if (guardia.length()==0){
					if (operazioni.length()==0){						
						if(link.getSendReceive()==ElementoMessaggio.RECEIVE)
							row = "        ::atomic{"+chL.findChannel(label).getChannelName()+"["+ label +"]?"+eventReceive+";_lst=("+chL.findChannel(label).getChannelName()+"["+label+"]);"+"goto "+nomeSucc+"};";
						else 
							if(link.getSendReceive()==ElementoMessaggio.SEND)
								row = "        ::atomic{"+chL.findChannel(label).getChannelName()+"["+ label +"]!"+eventSend+";_lst=_nill"+";goto "+nomeSucc+"};";
							else
							if(!label.equals(""))
								row = "        ::atomic{channelTau"+"["+label+"]!0;"+
										"channelTau["+label+"]?_;"+"_lstTau=(channelTau["+label+"]);"+"goto "+nomeSucc+"};";
							else
								row = "        ::atomic{goto "+nomeSucc+"};";
						ris.addLast(row);
					}
				else{
					//guardia vuota con operazioni
					if(link.getSendReceive()==ElementoMessaggio.RECEIVE)
					   row = "        ::atomic{"+chL.findChannel(label).getChannelName()+"["+ label +"]?"+eventReceive+";"+"_lst=("+chL.findChannel(label).getChannelName()+"["+label+"]);"+ operazioni +"goto "+nomeSucc+"};";
					else if (link.getSendReceive()==ElementoMessaggio.SEND)
							  row = "        ::atomic{"+chL.findChannel(label).getChannelName()+"["+ label +"]!"+eventSend+";"+"_lst=_nill;"+ operazioni +"goto "+nomeSucc+"};";
						  else 
							  if(!label.equals(""))
								row = "        ::atomic{channelTau"+"["+label+"]!0;"+
										"channelTau["+label+"]?_;"+"_lstTau=(channelTau["+label+"]);"+operazioni+"goto "+nomeSucc+"};";
							  else
								  row = "        ::atomic{"+ operazioni +"goto "+nomeSucc+"};";
					ris.addLast(row);
				}
			}
			else{
				if(operazioni.length()==0){
					//guardia non vuota, operazioni vuota
					if(link.getSendReceive()==ElementoMessaggio.RECEIVE){					
						//guardia non vuota, operazioni vuota, receive
						row = "        ::atomic{("+ guardia.substring(1,guardia.length()-1) +") -> "+chL.findChannel(label).getChannelName()+"["+label+"]?"+eventReceive+";_lst=("+chL.findChannel(label).getChannelName()+"["+label+"]);"+"goto "+nomeSucc+";};";
						ris.addLast(row);
					}
					else if (link.getSendReceive()==ElementoMessaggio.SEND){
							//guardia non vuota, operazioni vuota, send
							row = "        ::atomic{("+ guardia.substring(1,guardia.length()-1) +") -> "+chL.findChannel(label).getChannelName()+"["+label+"]!"+eventSend+";_lst=_nill;"+"goto "+nomeSucc+";};";
							ris.addLast(row);
						 }
						 else
							if(!label.equals("")){
								//guardia non vuota, operazioni vuota, tau
								row = "        ::atomic{("+ guardia.substring(1,guardia.length()-1) +") -> channelTau"+"["+label+"]!0;"+
										"channelTau["+label+"]?_;"+"_lstTau=(channelTau["+label+"]);"+"goto "+nomeSucc+"};";
								ris.addLast(row);                                     
							}else{
								row = "        ::atomic{("+ guardia.substring(1,guardia.length()-1) +") -> goto "+nomeSucc+";};";
								ris.addLast(row);                                     
							}
			  }
			  else{
				//guardia non vuota, operazioni non vuota
				if(link.getSendReceive()==ElementoMessaggio.RECEIVE){
					//guardia non vuota, operazioni non vuota, receive
					row = "        ::atomic{("+ guardia.substring(1,guardia.length()-1) +") -> "+chL.findChannel(label).getChannelName()+"["+label+"]?"+eventReceive+";_lst=("+chL.findChannel(label).getChannelName()+"["+label+"]);"+ operazioni +"goto "+nomeSucc+";};";
					ris.addLast(row);                                    
				}
				else if(link.getSendReceive()==ElementoMessaggio.SEND){
						//guardia non vuota, operazioni non vuota, send
						row = "        ::atomic{("+ guardia.substring(1,guardia.length()-1) +") -> "+chL.findChannel(label).getChannelName()+"["+label+"]!"+eventSend+";"+"_lst=_nill;"+ operazioni +"goto "+nomeSucc+";};";
						ris.addLast(row);
					 }
					 else					 
						if(!label.equals("")){
							//guardia non vuota, operazioni non vuota, tau
							row = "        ::atomic{("+ guardia.substring(1,guardia.length()-1) +") -> channelTau"+"["+label+"]!0;"+
									"channelTau["+label+"]?_;"+"_lstTau=(channelTau["+label+"]);"+operazioni+"goto "+nomeSucc+"};";
							ris.addLast(row);
   					    }
						else{
						   row = "        ::atomic{("+ guardia.substring(1,guardia.length()-1) +") -> "+ operazioni +"goto "+nomeSucc+";};";
						   ris.addLast(row);
						}
				  }
			  }
			}
			else{
			if (guardia.length()==0){
				if (operazioni.length()==0){
					row = "        ::atomic{goto "+nomeSucc+"};";
					ris.addLast(row);
				}
				else{
					row = "        ::atomic{"+ operazioni +"goto "+nomeSucc+"};";
					ris.addLast(row);
				}
			}
			else{
				if(operazioni.length()==0){
					row = "        ::atomic{("+guardia.substring(1,guardia.length()-1) +") -> goto "+nomeSucc+";};";
					ris.addLast(row);
				}
				else{
				    row = "        ::atomic{("+ guardia.substring(1,guardia.length()-1) +") -> "+ operazioni +"goto "+nomeSucc+";};";
					ris.addLast(row);
				}
			}            
		}
	}

	row = "        fi;";
	ris.addLast(row);
	guardia="";
	label="";
	operazioni="";
	}

        
	private final class ChannelList
	{
		private LinkedList chList=new LinkedList();
        
		public ChannelList cloneCh(){
			ChannelList cloneChList=new ChannelList();
			LinkedList ll=new LinkedList();
			for(int j=0;j<chList.size();j++)
				ll.add(((Channel)chList.get(j)).cloneCh());
			cloneChList.chList=ll;
			return cloneChList;
		}
        
		public void debug(){
			System.out.println("***********");
			for(int j=0;j<chList.size();j++){
				System.out.print("Elemento "+j+":   ");
				System.out.println(((Channel)chList.get(j)).getName()+" num. Par: "+((Channel)chList.get(j)).numberOfParameters+" defineNumber: "+((Channel)chList.get(j)).getDefineNumber());
			}
		}
        
		public int size(){
			return chList.size();
		}
        
		public Channel get(int i){
			if (chList.size()>i)
				return (Channel)chList.get(i);
			else
				return null;
		}
        
		public void setChannelName(String msg,String channel){
			findChannel(msg).setChannelName(channel);
		}
        
		public void delChannel(int i){
		   if(chList.size()>i)
			   chList.remove(i);                
		}
        
		public void addChannel(String msg, int num){
			chList.add(new Channel(msg,num));
		}
        
		public Channel findChannel(String name){
			for(int i=0;i<chList.size();i++){
				if(((Channel)chList.get(i)).getName().equals(name))
					return (Channel)chList.get(i);
			}
			return null;
		}
        
		public void setDelMarkedChannel(int j){
			if (j<chList.size())
				((Channel)chList.get(j)).setDelMarked();
		}
        
		public void setDefineNumber(int i,int number){
			if(i<chList.size())
				((Channel)chList.get(i)).setDefineNumber(number);
		}
        
		public int getDefineNumber(int i){
			if(i<chList.size())
				return ((Channel)chList.get(i)).getDefineNumber();
			return -1;
		}
        
		public boolean isDelMarkedChannel(int j){
			if (j<chList.size())
				return ((Channel)chList.get(j)).getDelMarked();
			return false;
		}

		public String getName(int j){
			if(chList.size()>j)
				return ((Channel)chList.get(j)).getName();
			return  null;
		}
        
        
        
		private final class Channel{
			private String msg;            
			private boolean delMarked=false;
			private int numberOfParameters;
			private String channelName;
			private int defineNumber;
			            
			public void setDefineNumber(int num){
				defineNumber=num;
			}
            
			public int getDefineNumber(){
				return defineNumber;
			}
            
			public void setDelMarked(){
				delMarked=true;
			}
            
			public boolean getDelMarked(){
				return delMarked;
			}
            
			public void setChannelName(String ch){
				if(numberOfParameters==0)
					channelName="channel1";
				else
					channelName="channel"+numberOfParameters;
			}

			public Channel(String msg, int num){
				this.msg=msg;
				numberOfParameters=num;
			}
            
			public int getNumberParameters(){
				return numberOfParameters;
			}
            
			public String getName(){
				return msg;
			}
            
			public String getChannelName(){
				return channelName;
			}
            
			public Channel cloneCh(){
				Channel clone=new Channel(this.msg,this.numberOfParameters);
				clone.delMarked=this.delMarked;
				clone.channelName=this.channelName;
				clone.defineNumber=this.defineNumber;
				return clone;
			}
		}
	}
	
        


	/** Determina, nella lista dei messaggi presa come secondo parametro, 
		i soli messaggi uscenti dallo stato preso nel primo parametro. */
	private LinkedList calcolaMessaggiUscenti(ElementoStato stato, LinkedList lista)
	{
		LinkedList uscenti = new LinkedList();
		for (int i=0;i<lista.size();i++){
			ElementoMessaggio link = (ElementoMessaggio)(lista.get(i));
			ElementoStato prec = (ElementoStato)(link.getElementFrom());
			if (prec==stato){
				// Aggiunge il messaggio al risultato della funzione.
				uscenti.add(link); 
			}
		}
		return uscenti;
	}


        
	/** restituisce 3 stringhe, la prima la label del link, mentre la seconda 
		il parametro di default per la send e il terzo il parametro di default 
		per la receive*/
	private String[] eventManagement(String link)
	{
		int i=link.length();
		String[] linkArray={"","",""};            
		if (link.endsWith(")")){
			i--;
			while ((link.substring(i-1,i).compareTo("(")!=0)&(i-1>0))
			{
				linkArray[1]=link.substring(i-1,i)+linkArray[1];
				i--;
			}
			linkArray[0]=link.substring(0,i-1);
			linkArray[2]=linkArray[1];
		}
		else{
			linkArray[0]=link;
			linkArray[1]="0";
			linkArray[2]="_";
		}
		return linkArray;
	}




	/** Questo metodo permette di tradurre i connettori dell'architettura. */
	private void traduciConnettori(LinkedList ris)
	{
		// Scrive il modello di proctype per i connettori sincroni.
		traduciConnettoreSincrono(ris);
		// Scrive il modello di proctype per i connettori asincroni.
		traduciConnettoreAsincrono(ris);
	}


	/** Traduce il tipo di connettore sincrono. */
	private void traduciConnettoreSincrono(LinkedList ris)
	{
		String row = "";		
		ris.addLast(row);
		row = " proctype SynchConn(int ch_S, ch_R){";
		ris.addLast(row);
		row = " do";
		ris.addLast(row);
		row = " ::atomic {";
		ris.addLast(row);
		row = "   ::if";
		ris.addLast(row);
		row = "     ::((critical[ch_R]) && (critical[ch_S])) -->";
		ris.addLast(row);
		row = "           channel [ch_S]?_; ";
		ris.addLast(row);
		row = "           critical[ch_R] = false; ";
		ris.addLast(row);
		row = "           critical[ch_S] = false; ";
		ris.addLast(row);
		row = "           cont++; ";
		ris.addLast(row);
		row = "           if ";
		ris.addLast(row);
		row = "             :: (position[ch_S]<N) --> ch[ch_S].pos[position[ch_S]] = cont+asyncont;";
		ris.addLast(row);
		row = "             :: (position[ch_S]>=N) --> skip; ";
		ris.addLast(row);
		row = "           fi;";
		ris.addLast(row);
		row = "           position[ch_S]++;";
		ris.addLast(row);
		row = "           /* Any other operation */";
		ris.addLast(row);
		row = "           cont++;";
		ris.addLast(row);
		row = "           if ";
		ris.addLast(row);
		row = "             :: (position[ch_R]<N) --> ch[ch_R].pos[position[ch_R]] = cont+asyncont;";
		ris.addLast(row);
		row = "             :: (position[ch_R]>=N) --> skip;";
		ris.addLast(row);
		row = "           fi;";
		ris.addLast(row);
		row = "           position[ch_R]++;";
		ris.addLast(row);
		row = "           channel [ch_R]!0;";
		ris.addLast(row);
		row = "     :: (!((critical[(ch_R]) && (critical[ch_S]))) --> skip;";
		ris.addLast(row);
		row = "   fi};";
		ris.addLast(row);
		row = " od;";
		ris.addLast(row);
		row = " }";
		ris.addLast(row);
	}


	/** Traduce il tipo di connettore asincrono. */
	private void traduciConnettoreAsincrono(LinkedList ris)
	{
		String row = "";
		ris.addLast(row);
		row = " proctype AsynchConn(int ch_S, ch_R){";
		ris.addLast(row);
		row = "  do";
		ris.addLast(row);
		row = "  ::if ";
		ris.addLast(row);
		row = "     :: atomic {";
		ris.addLast(row);
		row = "                channel [ch_S]?_;";
		ris.addLast(row);
		row = "                cont++;";
		ris.addLast(row);
		row = "                if";
		ris.addLast(row);
		row = "                  :: (position[ch_S]<N) --> ch[ch_S].pos[position[ch_S]] = cont+asyncont;";
		ris.addLast(row);
		row = "                  :: (position[ch_S]>=N) --> skip;";
		ris.addLast(row);
		row = "                fi;";
		ris.addLast(row);
		row = "                position[ch_S]++;"; 
		ris.addLast(row);
		row = "                };"; 
		ris.addLast(row);
		row = "                /* Any other operation */";
		ris.addLast(row);
		row = "     ::atomic {";
		ris.addLast(row);
		row = "               if";
		ris.addLast(row);
		row = "                 ::(critical[ch_R]) --> ";
		ris.addLast(row);
		row = "                     if ";
		ris.addLast(row);
		row = "                       ::position[ch_R]<N) --> ch[ch_R].pos[position[ch_R]] = cont + AsynCont;";
		ris.addLast(row);
		row = "                       ::(position[ch_R]>=N) --> skip;";
		ris.addLast(row);
		row = "                     fi;";
		ris.addLast(row);
		row = "                     position[ch_R]++;";
		ris.addLast(row);
		row = "                     AsynCont++;";
		ris.addLast(row);
		row = "                     channel[ch_R] !0 ;";
		ris.addLast(row);
		row = "                 ::(!(critical[ch_R])) -->  skip;";
		ris.addLast(row);
		row = "               fi;";
		ris.addLast(row);
		row = "               };";
		ris.addLast(row);
		row = "    fi;";
		ris.addLast(row);
		row = " od;";
		ris.addLast(row);
		row = " }";
		ris.addLast(row);
	}


/** Traduce il connettore asincrono con buffer >1. */
	private void traduciConnettoreBuffer(LinkedList ris)
	{
		String row = "";	
		ris.addLast(row);
		row = " int Asynbuffer;";
		ris.addLast(row);
		row = " do";
		ris.addLast(row);
		row = " ::if"; 
		ris.addLast(row);
		row = "   ::if";
		ris.addLast(row);
		row = "     ::(Asynbuffer<ASYNBUFFERDIM)-> ";
		ris.addLast(row);
		row = "         atomic{channel[chS]?_;";
		ris.addLast(row);
		row = " 	    cont++;";
		ris.addLast(row);
		row = " 	    if";
		ris.addLast(row);
		row = " 	    ::(position[chS]<N) -> ch[chS].pos[position[chS]]=cont+asyncont;";
		ris.addLast(row);
		row = " 	    ::(position[chS]>=N) -> skip;";
		ris.addLast(row);
		row = " 	    fi;";
		ris.addLast(row);
		row = " 	    position[chS]++;Asynbuffer++};";
		ris.addLast(row);
		row = "     ::(Asynbuffer>=ASYNBUFFERDIM) -> skip;";
		ris.addLast(row);
		row = "     fi;";
		ris.addLast(row);
		row = "     /*Any other operation*/";
		ris.addLast(row);
		row = "   ::if";
		ris.addLast(row);
		row = "     ::(Asynbuffer>0) -> ";
		ris.addLast(row);
		row = "  	if";
		ris.addLast(row);
		row = "  	::(critical[chR]) -> ";
		ris.addLast(row);
		row = "     	   atomic{";
		ris.addLast(row);
		row = " 	     if";
		ris.addLast(row);
		row = " 	     ::(position[chR]<N) -> ch[chR].pos[position[chR]]=cont+asyncont;";
		ris.addLast(row);
		row = " 	     ::(position[chR]>=N) -> skip;";
		ris.addLast(row);
		row = " 	     fi;";
		ris.addLast(row);
		row = " 	     position[chR]++;";
		ris.addLast(row);
		row = " 	     Asynbuffer--;";
		ris.addLast(row);
		row = " 	     channel[chR]!0";
		ris.addLast(row);
		row = " 	   };";
		ris.addLast(row);
		row = " 	::(!critical[chR]) -> skip;";
		ris.addLast(row);
		row = " 	fi;";
		ris.addLast(row);
		row = "     ::(Asynbuffer<=0) -> skip;";
		ris.addLast(row);
		row = "     fi;";
		ris.addLast(row);
		row = "   fi;";
		ris.addLast(row);
		row = " od;";
		ris.addLast(row);
		row = " }";
		ris.addLast(row);
	}


	/** Calcola la parte (d) della specifica Promela. */
	private void calcolaD(LinkedList ris, ListaDP listaDP, ListaDS listaDS)
	{
		ListaThread dinamicaP;
		String row;
		row = "";
		ris.addLast(row);
		row = " init {";
		ris.addLast(row);
		row = "      atomic {";
		ris.addLast(row);
		// Inizializzo la stringa per raggruppare i nomi dei thread 
		// a seconda dei processi cui appartengono. 
		String accumulo = "     ";
		// Inserisce i nomi dei processi.
		for (int i=0;i<listaDP.size();i++){
			dinamicaP = (ListaThread)(listaDP.getDinamicaProcesso(i));
			String nomeP = dinamicaP.getNameProcesso();
			// Metto in esecuzione tutti i thread del processo.
			for (int j=0;j<dinamicaP.size();j++){
				ThreadElement thread = (ThreadElement)(dinamicaP.get(j));
				// Leggo il nome del thread.
				String nomeT = thread.getNomeThread();
				// Mando in esecuzione il thread.
				accumulo = accumulo + " run "+nomeP+"_"+nomeT+"();";
			}
			// Aggiungo l'inizializzazione dei thread del processo.
			ris.add(accumulo);
			// Aggiorno la stringa per raggruppare i nomi dei thread 
			// a seconda dei processi cui appartengono. 
			accumulo = "     ";
		}

		// *** TRADUZIONE DEI CONNETTORI  *** //

		// Recupero tutti i messaggi, senza ripetizioni, scambiati nei sequence diagram.
		LinkedList listaConnettori = listaDS.getAllMessageName(); 
		// Inserisce i nomi dei connettori.
		for (int i=0;i<listaConnettori.size();i++){
			// Inizializzo accumulo.
			accumulo = "     ";
			String nomeC = (String)(listaConnettori.get(i));
			for (int j=0;j<listaDS.size();j++){
				SequenceElement ds = (SequenceElement)(listaDS.get(j));
				if (ds.getListaSeqLink().isAsynchronous(nomeC)){
					accumulo = accumulo + " run AsynchCon("+nomeC+"_S, "+nomeC+"_R); ";
					break;
				}
				if (ds.getListaSeqLink().isSynchronous(nomeC)){
					accumulo = accumulo + " run SynchCon("+nomeC+"_S, "+nomeC+"_R); ";   
					break; 
				}
			}
			// Aggiungo l'inizializzazione dei connettori.
			if (!accumulo.equals("")) 
				ris.add(accumulo);      
		}
		row = "      }";
		ris.addLast(row);
		// Chiudo.
		row = " }";
		ris.addLast(row);
	}

	
	private void errore(String s) 
	{
		String temp="Si e' verificato un errore in PromelaSpecified \n";
		JOptionPane.showMessageDialog(null, temp+s, "Condizione di errore!",JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}		
        
}