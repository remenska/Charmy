/*
 * Created on 28-ott-2004
 */
package plugin.TeStor.condivisi;

import java.awt.Container;
import java.awt.Cursor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.TreeMap;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import plugin.TeStor.condivisi.interfaccia.ListaIndiciInteriLunghi;
import plugin.statediagram.controllo.ThreadCheck;
import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.data.ListaDP;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.dialog.ThreadCheckWindow;
import plugin.statediagram.graph.ElementoMessaggio;
import plugin.sequencediagram.SequenceEditor;
import plugin.sequencediagram.data.ElementoClasse;
import plugin.sequencediagram.data.ElementoSeqLink;
import plugin.sequencediagram.data.SequenceElement;
import plugin.topologydiagram.resource.data.ElementoCanaleMessaggio;
import core.internal.runtime.data.IPlugData;

/**
 * La classe realizza l'algoritmo TeStor.
 * 
 * @author Fabrizio Facchini
 */
public class AlgoritmoTeStor {
	/**
	 * La classe rappresenta un insieme di tracce.
	 * É una lista (concatenata) di oggetti di tipo {@link AlgoritmoTeStor.Traccia}.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class Tracce extends LinkedList{
		/**
		 * Aggiunge a queste tracce la traccia specificata.
		 * La traccia non viene aggiunta se ne è già presente una uguale.
		 * 
		 * @param traccia una traccia lineare
		 * @return <code>true</code> se l'aggiunta è avvenuta, <code>false</code> altrimenti
		 * @see AlgoritmoTeStor.Tracce#contiene(Traccia)
		 */
		protected boolean add(Traccia traccia) {
			return contiene(traccia)?
					false:
					super.add(traccia);
		}
		/**
		 * Stabilisce se tra queste tracce ce n'è una uguale alla traccia specificata.
		 * 
		 * @param traccia una traccia
		 * @return <code>true</code> se tra queste tracce ce n'è una uguale a quella specificata,
		 * <code>false</code> altrimenti
		 * @see java.util.AbstractList#equals(java.lang.Object)
		 */
		protected boolean contiene(Traccia traccia){
			Iterator scorriTracce = iterator();
			while(scorriTracce.hasNext()){
				Traccia tracciaCorr = (Traccia)scorriTracce.next();
				if(tracciaCorr.getSmLineare().equals(traccia.getSmLineare())
						&& tracciaCorr.equals(traccia)){
					return true;
				}
			}
			return false;
		}
		/**
		 * Aggiunge a queste tracce le tracce specificate.
		 * 
		 * @param tracce le tracce che si intendono aggiungere
		 * @return ???
		 */
		protected boolean addAll(Tracce tracce) {
			boolean modificato = false;
			Iterator scorriTracce = tracce.iterator();
			while(scorriTracce.hasNext()){
				if(add((Traccia)scorriTracce.next())){
					modificato = true;
				}
			}
			return modificato;
		}
	}
	/**
	 * La classe rappresentante una traccia.
	 * É una lista (concatenata) di oggetti di tipo {@link core.a02thread.graph.ElementoMessaggio}.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class Traccia extends LinkedList{
		/**
		 * Identificatore univoco per questa traccia
		 */
		private final long idTraccia;
		/**
		 * La SM lineare a cui appartiene questa traccia
		 */
		private final SMLineare smLineare;
		/**
		 * Costruisce un nuovo oggetto Traccia sulla SM linearizzata specificata.
		 * Aggiorna il contatore delle tracce (utilizzato 
		 * per generare gli identificatori delle tracce).
		 * 
		 * @param sml la SM linearizzata a cui appartiene questa traccia
		 */
		private Traccia(SMLineare sml){
			super();
			idTraccia = ++contaTracce;
			smLineare = sml;
		}
		/**
		 * Stabilisce se la transizione specificata è addizionabile a questa traccia.
		 * La transizione è <i>addizionabile</i> se valgono le seguenti condizioni:<br>
		 * - la transizione è presente nella state machine a cui appartiene questa traccia,<br>
		 * - questa traccia è vuota, oppure, se non lo è, lo stato terminale di questa traccia
		 * coincide con quello terminale della transizione.<br>
		 * Gli stati terminali di cui sopra dipendono dal valore di <code>inCoda</code>.
		 * 
		 * @param trans una transizione
		 * @param inCoda indica che si vuole stabilire se la transizione specificata può
		 * essere aggiunta in coda a questa traccia, piuttosto che in testa
		 * @return <code>true</code> se la transizione è addizionabile, <code>false</code> altrimenti
		 */
		protected boolean isTransAddizionabile(ElementoMessaggio trans, boolean inCoda){
			return
				getSmLineare().getStateMachine().getListaMessaggio().giaPresente(trans.getName()) &&
				(isEmpty() || (inCoda?
						getStatoFinale().equals(trans.getElementFrom()):
						getStatoIniziale().equals(trans.getElementTo())
					)
				);
		}
		/**
		 * Accoda la transizione specificata a questa traccia.
		 * L'accodamento non avviene se la transizione non è addizionabile in coda a questa traccia.
		 * 
		 * @param trans una transizione di una SM
		 * @return <code>true</code> se l'accodamento è avvenuto, <code>false</code> altrimenti
		 * @see AlgoritmoTeStor.Traccia#isTransAddizionabile(ElementoMessaggio, boolean)
		 */
		protected boolean add(ElementoMessaggio trans) {
			return addLast(trans);
		}
		/**
		 * Aggiunge in coda a questa traccia la transizione specificata.
		 * L'accodamento non avviene se la transizione non è addizionabile in coda a questa traccia.
		 * 
		 * @param trans una transizione
		 * @return <code>true</code> se l'aggiunta è avvenuta, <code>false</code> altrimenti
		 * @see AlgoritmoTeStor.Traccia#isTransAddizionabile(ElementoMessaggio, boolean)
		 */
		protected boolean addLast(ElementoMessaggio trans){
			if(isTransAddizionabile(trans,true)){
				return super.add(trans);
			}
			return false;
		}
		/**
		 * Aggiunge in testa a questa traccia la transizione specificata.
		 * L'aggiunta in testa non avviene se la transizione non è addizionabile in testa a questa traccia.
		 * 
		 * @param trans una transizione
		 * @return <code>true</code> se l'aggiunta è avvenuta, <code>false</code> altrimenti
		 * @see AlgoritmoTeStor.Traccia#isTransAddizionabile(ElementoMessaggio, boolean)
		 */
		protected boolean addFirst(ElementoMessaggio trans){
			if(isTransAddizionabile(trans,false)){
				super.addFirst(trans);
				return true;
			}
			return false;
		}
		/**
		 * Stabilisce se la traccia specificata è addizionabile a questa traccia.
		 * La traccia specificata è <i>addizionabile</i>
		 * se è vuota, oppure se la sua transizione terminale è addizionabile a questa traccia.<br>
		 * La transizione terminale di cui sopra e la sua addizionabilità
		 * dipendono dal valore di <code>inCoda</code>.
		 * 
		 * @param traccia una traccia
		 * @param inCoda indica che si vuole stabilire se la traccia specificata può
		 * essere aggiunta in coda a questa traccia, piuttosto che in testa
		 * @return <code>true</code> se la traccia specificata è addizionabile, <code>false</code> altrimenti
		 * @see AlgoritmoTeStor.Traccia#isTransAddizionabile(ElementoMessaggio, boolean)
		 */
		protected final boolean isTracciaAddizionabile(Traccia traccia, boolean inCoda){
			return traccia.isEmpty()?
					true:
					isTransAddizionabile(
						(inCoda?
							traccia.getTransIniziale():
							traccia.getTransFinale()
						),
						inCoda);
		}
		/**
		 * Aggiunge in coda a questa traccia la traccia specificata.
		 * L'aggiunta in coda non avviene se la traccia non è addizionabile in coda a questa traccia.
		 * 
		 * @param traccia una traccia
		 * @return <code>true</code> se l'aggiunta è avvenuta, <code>false</code> altrimenti
		 * @see AlgoritmoTeStor.Traccia#isTracciaAddizionabile(Traccia, boolean)
		 */
		protected final boolean aggiungiInCoda(Traccia traccia){
			if(isTracciaAddizionabile(traccia,true)){
				return addAll(traccia);
			}
			return false;
		}
		/**
		 * Aggiunge in testa a questa traccia la traccia specificata.
		 * L'aggiunta in testa non avviene se la traccia non è addizionabile in testa a questa traccia.
		 * 
		 * @param traccia una traccia
		 * @return <code>true</code> se l'aggiunta è avvenuta, <code>false</code> altrimenti
		 * @see AlgoritmoTeStor.Traccia#isTracciaAddizionabile(Traccia, boolean)
		 */
		protected final boolean aggiungiInTesta(Traccia traccia){
			if(isTracciaAddizionabile(traccia,false)){
				return addAll(0,traccia);
			}
			return false;
		}
		/**
		 * @return la prima transizione di questa traccia
		 */
		protected ElementoMessaggio getTransIniziale(){
			return (ElementoMessaggio)getFirst();
		}
		/**
		 * @return l'ultima transizione di questa traccia
		 */
		protected ElementoMessaggio getTransFinale(){
			return (ElementoMessaggio)getLast();
		}
		/**
		 * Ritorna lo stato finale in questa traccia.
		 * 
		 * @return lo stato finale in questa traccia
		 */
		protected ElementoStato getStatoFinale() {
			return (ElementoStato)getTransFinale().getElementTo();
		}
		/**
		 * Ritorna lo stato iniziale di questa traccia.
		 * 
		 * @return lo stato iniziale di questa traccia
		 */
		protected ElementoStato getStatoIniziale() {
			return (ElementoStato)getTransIniziale().getElementFrom();
		}
		/**
		 * Restituisce la SM linearizzata a cui appartiene questa traccia.
		 * 
		 * @return la SM linearizzata a cui appartiene questa traccia
		 */
		protected SMLineare getSmLineare() {
			return smLineare;
		}
		/**
		 * Restituisce l'identificativo di questa traccia.
		 * 
		 * @return l'identificativo di questa traccia
		 */
		protected long getIdTraccia() {
			return idTraccia;
		}
		/**
		 * Restituisce il nome del processo dell'architettura relativo alla SM di cui questa traccia fa parte.
		 * 
		 * @return il nome del processo che contiene questa traccia
		 */
		protected String getNomeProcesso(){
			return getSmLineare().getProcesso().getNameProcesso();
		}
		/**
		 * Stabilisce se questa traccia è un cappio (una traccia ciclica di una transizione).
		 * 
		 * @return <code>true</code> se è un cappio, <code>false</code> altrimenti
		 */
		protected boolean isCappio(){
			return size()==1 && isCiclo();
		}
		/**
		 * Stabilisce se questa traccia è un ciclo.
		 * 
		 * @return <code>true</code> se è un ciclo, <code>false</code> altrimenti
		 */
		protected boolean isCiclo(){
			return !isEmpty() && getStatoIniziale().equals(getStatoFinale());
		}
		/**
		 * Stabilisce se questa traccia contiene (almeno) un ciclo non terminale.
		 * Un ciclo si dice <i>terminale</i> di una traccia quando lo stato ripetuto
		 * occorre come stato finale della traccia.
		 * Non consideriamo interessanti i cicli terminali
		 * dal momento che questo metodo si applica a tracce valide,
		 * che, quindi, possono contenerne uno.
		 * 
		 * @return <code>true</code> se questa traccia contiene (almeno) un ciclo non terminale,
		 * <code>false</code> altrimenti
		 */
		protected boolean contieneCicliNonTerminali(){
			MappaStati statiPresenti = new MappaStati();
			Iterator scorriTraccia = iterator();
			ElementoMessaggio transCorr = null;
			ElementoStato statoCorr;
			
			//ezio 2006 - fixed bug
			//ll
			ElementoStato statoFinale = this.getStatoFinale();
			
			while(scorriTraccia.hasNext()){
				transCorr = (ElementoMessaggio)scorriTraccia.next();
				statoCorr = (ElementoStato)transCorr.getElementFrom();
				
				if (!statoFinale.equals(statoCorr))///ezio 2006
				if(statiPresenti.contieneChiave(statoCorr)){// ciclo
					return true;
				}else{
					statiPresenti.put(statoCorr);
				}
			}
			return false;
		}

		/**
		 * Stabilisce se questa traccia contiene (almeno) un ciclo.
		 * 
		 * @return <code>true</code> se questa traccia contiene (almeno) un ciclo,
		 * <code>false</code> altrimenti
		 */
		protected boolean contieneCicli(){
			MappaStati statiPresenti = new MappaStati();
			Iterator scorriTraccia = iterator();
			ElementoMessaggio transCorr = null;
			ElementoStato statoCorr;
			
			while(scorriTraccia.hasNext()){
				transCorr = (ElementoMessaggio)scorriTraccia.next();
				statoCorr = (ElementoStato)transCorr.getElementFrom();
				if(statiPresenti.contieneChiave(statoCorr)){// ciclo
					return true;
				}else{
					statiPresenti.put(statoCorr);
				}
			}
			statoCorr = (ElementoStato)transCorr.getElementTo();
			if(statiPresenti.contieneChiave(statoCorr)){// ciclo
				return true;
			}else{
				statiPresenti.put(statoCorr);
			}
			return false;
		}
		/**
		 * @return <code>true</code> se questa traccia contiene un ciclo terminale, <code>false</false> altrimenti
		 */
		protected boolean contieneCicloTerminale(){
			return getCicloTerminale() == null;
		}
		/**
		 * @return il ciclo terminale di questa traccia, se presente, <code>null</code> altrimenti
		 */
		protected Traccia getCicloTerminale(){
			ListIterator scorri = listIterator(size());
			Traccia temp = new Traccia(getSmLineare());
			while(scorri.hasPrevious()){
				ElementoCanaleMessaggio transCorr = (ElementoCanaleMessaggio)scorri.previous();
				temp.addFirst(transCorr);
				if(transCorr.getElementFrom().equals(getStatoFinale())){
					return temp;
				}
			}
			return null;
		}
		/**
		 * Stabilisce la parte comune tra questa traccia e quella spcificata
		 * a partire dall'inizio di ciascuna delle due.
		 * 
		 * @param traccia una traccia
		 * @return un array di tre componenti:<br>
		 * - la prima è la sotto-traccia comune,<br>
		 * - la seconda è la restante di quella specificata,<br>
		 * - la terza è la restante di questa traccia.
		 */
		protected Traccia[] comuneRestante(Traccia traccia){
			Traccia[] risultato = {new Traccia(getSmLineare()), new Traccia(getSmLineare()), new Traccia(getSmLineare())};
			risultato[1].aggiungiInCoda(traccia);
			risultato[2].aggiungiInCoda(this);
			
			Iterator scorriQuesta = iterator();
			Iterator scorriTraccia = traccia.iterator();
			ElementoMessaggio transQuesta, transTraccia;
			while(true){
				if(scorriQuesta.hasNext()){// questa traccia non è finita
					transQuesta = (ElementoMessaggio)scorriQuesta.next();
					if(scorriTraccia.hasNext()){// la traccia non è finita
						transTraccia = (ElementoMessaggio)scorriTraccia.next();
						if(transQuesta == transTraccia){// siamo ancora nella parte comune
							risultato[0].add(transQuesta);
							risultato[1].removeFirst();
							risultato[2].removeFirst();
						}else{// finita parte comune
							break;
						}
					}else{// la traccia è finita
						break;
					}
				}else{// questa traccia è finita
					break;
				}
			}
			System.out.println("Parte comune: "+risultato[0]);// DEBUG
			System.out.println("Parte restante traccia passata: "+risultato[1]);// DEBUG
			System.out.println("Parte restante questa traccia: "+risultato[2]);// DEBUG
			return risultato;
		}
		/**
		 * Stabilisce se questa traccia contiene una transizione
		 * la cui altra SM coinvolta coincide con quella specificata.
		 * 
		 * @param sml una SM lineare
		 * @return <code>true</code> se questa traccia contiene la transizione di cui sopra, <code>false</code> altrimenti
		 */
		protected boolean contieneTransDiSM(SMLineare sml){
			Iterator scorri = iterator();
			 while(scorri.hasNext()){
			 	if(
			 		getSmLineare().getRifInfoTrans().getAltraSM(
			 			(ElementoMessaggio)scorri.next()
					).equals(sml))
				{
			 		return true;
			 	}
			 }
			 return false;
		}
		/**
		 * Restituisce la sotto-traccia di questa dall'inizio per le prime n transizioni,
		 * secondo il parametro specificato, o comunque fino all'ultima di questa traccia.
		 * 
		 * @param n le prime transizione della sotto-traccia da restituire
		 * @return la sotto-traccia di questa dall'inizio per le prime n transizioni
		 */
		protected Traccia primeNtrans(int n){
			Traccia out = new Traccia(getSmLineare());
			Iterator scorri = iterator();
			int i = 0;
			while(scorri.hasNext() && i < n){
				out.add((ElementoMessaggio)scorri.next());
				i++;
			}
			return out;	
		}
		/**
		 * Restituisce le sup-trace individuate da questa traccia, dalla transizione
		 * e dallo stato iniziale specificati.
		 * La transizione è richiamabile con una <code>next()</code> sull'iteratore specificato.
		 * Se la traccia t contiene lo stato iniziale is prima della transizione (caso base),
		 * restituisce la sotto-traccia di t da is allo stato secondo estremo della transizione.
		 * Altrimenti (caso ricorsivo) individua tutte le tracce della SM linearizzata
		 * che hanno per stato finale il primo stato della traccia.
		 * Per ognuna di queste tracce chiama se stessa ricorsivamente, ponendo come transizione
		 * di partenza l'ultima della traccia stessa.
		 * Quindi accoda alle sup-trace parziali restituite da queste chiamate la sotto-traccia di t
		 * dalla transizione iniziale alla transizione indicata dall'iteratore.
		 * Infine restituisce il tutto.<br>
		 * Una traccia completa (restituita dalla prima chiamata di questa funzione) si dice essere
		 * una traccia <i>valida</i>; una traccia valida gode delle seguenti proprietà:<br>
		 * - il primo stato della traccia è lo stato is, e al più un altro stato della tracca è is;<br>
		 * - esiste una sola transizione etichettata m, ed è l'ultima della traccia;<br>
		 * - la traccia non contiene cicli non contenenti la trasizione m (segue dalla prima proprietà);<br>
		 * - ciascuna traccia lineare <i>completa</i> che compone la traccia appare una sola volta.
		 * 
		 * @param 	is uno stato della SM a cui appartiene questa traccia
		 * @param 	scorriTraccia un iteratore indicante la posizione nella traccia della transizione
		 * 			da cui deve partire la ricerca a ritroso di is (è richiamabile con una <code>next()</code>)
		 * @param 	m il nome di una transizione della SM; alla <i>prima chiamata</i> coincide
		 * 			con la transizione indicata dall'iteratore
		 * @param 	idTracceVisitate una lista contenente gli identificatori delle tracce visitate
		 * 			al momento della chiamata ricorsiva di questa funzione
		 * @return 	le tracce valide parziali individuate da questa traccia, dalla transizione e dallo stato iniziale specificati
		 * @throws	WrongStateException se lo stato specificato non esiste nella SM di appartenenza di questa traccia
		 */
		////ezio 2006 FIXED BUG
		
		private Tracce validazione(ElementoStato is, ListIterator scorriTraccia, String m, ListaIndiciInteriLunghi idTracceVisitate)
							throws WrongStateException {
		    // la SM linearizzata a cui appartiene t
			SMLineare sml = getSmLineare(); 
			// le tracce da ritornare
			Tracce tracceDaRitornare = new Tracce();
			// tracce utilizzate nella ricorsione
			Tracce tracceDaStatoFinale;
			// iteratore per tracce
			Iterator scorriTracce;
			// tracce ritornate da una chiamata ricorsiva alla funzione
			Tracce tracceRitornate;
			// iteratore per tracce
			Iterator scorriTracceRitornate;
			// la transizione corrente
			/* Se è la prima chiamata di questa funzione la trans. è m, 
			 * altrimenti non lo è, in quanto le chiamate ricorsive non sono effettuate
			 * su tracce che contengano la trans. m in ultima posizione
			 */
			ElementoMessaggio transCorr = (ElementoMessaggio)scorriTraccia.next();
			// condizione corrispondente alla prima chiamata di questa funzione (vedi sù)
		    boolean primaChiamata = transCorr.getName().equals(m);
			// sotto-traccia di t
			Traccia tracciaTemp = new Traccia(sml);
			// una traccia che ha per stato finale lo stato iniziale di t
			Traccia tracciaDaStatoFinale;
			// traccia tra quelle ritornate da una chiamata ricorsiva
			Traccia tracciaRitornata;
			/* utilizzato nel caso base per escludere le tracce che hanno
			 * il messaggio in esame presente più di una volta;
			 * indica che la transizione corrente è il messaggio della prima chiamata (m)
			 */
			boolean isM = true;
			
			// CASO BASE
			//System.out.println("\t\t\tvalidazione traccia "+this+": m = "+m+", is = "+is.getName());//DEBUG
			// scorriamo a ritroso la traccia in cerca di is
			while(scorriTraccia.hasPrevious()){
				transCorr = (ElementoMessaggio)scorriTraccia.previous();
				if(!isM && transCorr.getName().equals(m)){
					//System.out.println("\t\t\t\tvalidazione: CASO BASE: la traccia contiene già m in una posizione anticipata, RETURN\n");//DEBUG
					return tracceDaRitornare;
				}
				// aggiorniamo la traccia d'appoggio (a ritroso)
				tracciaTemp.addFirst(transCorr);
				
				
				if((ElementoStato)transCorr.getElementFrom()==is){// trovato is!
					// aggiorniamo la lista di tracce da ritornare
					tracceDaRitornare.add(tracciaTemp);
					
					///ezio 2006 - fixed bug: ciclo terminale (su traccia valida) che coincide con lo stato iniziale della configurazione corrente della SM
					if ((primaChiamata)&&(this.isCiclo())&&(this.getStatoFinale().equals(is))){
						//non fa nulla: va alla ricorsione
					}
					else
					//System.out.println("\t\t\t\tvalidazione: CASO BASE: trovato is, restituita SOTTO-TRACCIA "+tracciaTemp+"\n");//DEBUG
					//System.out.println("\t\t\tvalidazione traccia "+this+": RETURN\n");//DEBUG
					return tracceDaRitornare;
				}
				// dopo la prima iterazione aggiorniamo isM
				isM = false;
			}
			boolean isCicloTerminaleTracciaValida=false;
		
			if(!primaChiamata){// questa è una chiamata ricorsiva
			    /* Abbiamo scorso tutta la traccia a ritroso dall'ultima trans. di essa
			     * senza trovare lo stato is.
			     * Se la traccia è un ciclo deve essere esclusa (non è valida).
			     */
			    if(isCiclo()){
			    	
			    	///ezio 2006 fixed bug - bisogna controllare che il ciclo non sia un ciclo terminale della traccia valida
			    	
			    	if (!idTracceVisitate.isEmpty()){
			    		Long idUltimaTraccia = (Long)idTracceVisitate.get(0);
			    		Traccia ultimaTraccia = smLineare.getTraccia(idUltimaTraccia.longValue());
			    		if (ultimaTraccia.getStatoFinale().equals(this.getStatoFinale()))
			    			isCicloTerminaleTracciaValida=true;
			    	}
			    	
			    	if(!isCicloTerminaleTracciaValida)
			           return tracceDaRitornare;
			    }
			}
			
			
			// CASO RICORSIVO
			/* chiamiamo ricorsivamente la funzione sulle tracce con stato finale
			 * uguale allo stato iniziale di t
			 */
			//System.out.println("\t\t\t\tvalidazione: CASO RICORSIVO: tracce con stato finale uguale a "+((ElementoStato)transCorr.getElementFrom()).getName()+":");//DEBUG
			
			
//			ezio 2006 - bug fixed
			idTracceVisitate.addElement(this.getIdTraccia());
			////////////////
			
			tracceDaStatoFinale = sml.getTracceDaStatoFinale((ElementoStato)transCorr.getElementFrom());
			scorriTracce = tracceDaStatoFinale.iterator();
			while(scorriTracce.hasNext()){
				// esaminiamo ogni traccia che ha lo stato finale uguale allo stato iniziale di t
				tracciaDaStatoFinale = (Traccia)scorriTracce.next();
				//System.out.println("\t\t\t\t\t traccia "+tracciaDaStatoFinale+":");//DEBUG
				if(!((ElementoMessaggio)tracciaDaStatoFinale.getLast()).getName().equals(m) && !idTracceVisitate.contains(tracciaDaStatoFinale.getIdTraccia())){
					// la traccia non contiene il messaggio m in ultima posizione e non è stata validata
					if(tracciaDaStatoFinale.isCappio()){// è un cappio!
					    /* Invece di escludere le tracce che sono cicli (che potrebbero contenere
					     * lo stato is!) escludiamo a priori solo i cappi (ovviamente)
					     * Se la traccia ciclica (non cappio) contiene lo stato is
					     * va tenuta come valida, altrimenti è esclusa (vedi sù)
					     */
						//System.out.println("\t\t\t\tvalidazione: la traccia è un inutile ciclo");//DEBUG
					}else{
						//System.out.println("\t\t\t\tvalidazione: la traccia non contiene il messaggio m e non è stata ancora visitata, lanciamo la RICORSIONE");//DEBUG
						// aggiunge l'id della traccia da visitare tra quelli delle tracce visitate
						
						
						//ezio 2006 - idTracceVisitate.addElement(tracciaDaStatoFinale.getIdTraccia());
						
						
						// RICORSIONE
						
						
						
						tracceRitornate = tracciaDaStatoFinale.validazione(is,tracciaDaStatoFinale.listIterator(tracciaDaStatoFinale.size()-1),m,idTracceVisitate);
						if(tracceRitornate.size()>0){// c'è almeno una traccia valida
							scorriTracceRitornate = tracceRitornate.iterator();
							//System.out.println("\t\t\tvalidazione traccia "+this+": m = "+m+", is = "+is.getName());//DEBUG
							/* ad ogni traccia ritornata accodiamo la sotto-traccia di t
							 * dal suo stato iniziale a cs, quindi l'aggiungiamo alle tracce da ritornare
							 */
							while(scorriTracceRitornate.hasNext()){
								tracciaRitornata = (Traccia)scorriTracceRitornate.next();
								tracciaRitornata.aggiungiInCoda(tracciaTemp);
								if(!tracciaRitornata.contieneCicliNonTerminali()/* && !tracceDaRitornare.contiene(tracciaRitornata)*/){
									// la traccia ritornata è da ritornare perché non contiene cicli non terminali					 */
									tracceDaRitornare.add(tracciaRitornata);
									//System.out.println("\t\t\t\tvalidazione: ACCODAMENTO, ottenuta TRACCIA "+tracciaRitornata);//DEBUG
								}else{
									//System.out.println("\t\t\t\tvalidazione: la traccia ritornata "+tracciaRitornata+" contiene cicli o è gia stata esaminata: ESCLUSA");//DEBUG
								}
							}
						}else{// nessuna traccia valida da ricorsione
							//System.out.println("\t\t\t\tvalidazione: nessuna traccia valida ritornata");
						}
					}
				}else{
					//System.out.println("\t\t\t\tvalidazione: la traccia contiene il messaggio m oppure è stata già visitata");//DEBUG
				}
			}
			//System.out.println("\t\t\tvalidazione traccia "+this+": RETURN\n");//DEBUG
			return tracceDaRitornare; 
		}
		/**
		 * Restituisce le sup-trace individuate da questa traccia, dallo stato iniziale
		 * specificato e dalla transizione in questa traccia alla posizione specificata.
		 * 
		 * @param is uno stato della SM linearizzata a cui appartiene questa traccia
		 * @param posizione la posizione nella traccia della transizione d'interesse
		 * @return le tracce valide individuate da questa traccia per i parametri specificati
		 * @throws ArrayIndexOutOfBoundsException se la posizione specificata non è valida
		 * @throws WrongStateException se lo stato specificato non appartiene alla SM di questa traccia
		 * @see AlgoritmoTeStor.Traccia#validazione(ElementoStato, ListIterator, String, ListaIndiciInteriLunghi)
		 */
		protected Tracce validazione(ElementoStato is, int posizione)
							throws 	IndexOutOfBoundsException,
									WrongStateException{
			// next() su quest'iteratore dà la trans. indicata dalla posizione
			ListIterator iter = listIterator(posizione);
			// la trans. obiettivo della validazione
			ElementoMessaggio trans = (ElementoMessaggio)iter.next();
			// così la prossima transizione sarà ancora quella obiettivo
			iter.previous();
			return validazione(is, iter, trans.getName(), new ListaIndiciInteriLunghi());
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		/**
		 * Rappresenta questa traccia con il suo identificatore, la sequenza di
		 * stati (racchiusi tra parentesi quadre) e transizioni che la compongono.
		 * 
		 * @return una stringa rappresentante questa traccia
		 */
		public String toString(){
			return toString(-1);
		}
		/**
		 * Simile a toString, evidenzia la transizione alla posizione specificata
		 * (tra parentesi acute).
		 * 
		 * @param posizione la posizione di una transizione all'interno di questa traccia
		 * @return una stringa rappresentante questa traccia
		 * @see AlgoritmoTeStor.Traccia#toString()
		 */
		public String toString(int posizione){
			String temp="ID = "+getIdTraccia()+":  ";
			Iterator scorriTrans = iterator();
			if(!isEmpty()){
				ElementoMessaggio transCorr = null;
				int i = -1;
				while(scorriTrans.hasNext()){
					transCorr = (ElementoMessaggio)scorriTrans.next();
					i++;
					temp += "[" + transCorr.getElementFrom().getName() + "] "+ 
						(i==posizione?
							"<"+transCorr.getName()+"> ":
							transCorr.getName()+" "
						);
				}
				temp += "[" + transCorr.getElementTo().getName() + "]";
			}else{
				temp += "<vuota>";
			}
			return temp;

		}
	}
	/**
	 * Struttura fondamentale usata nella generazione dei casi di test.
	 * É una traccia in una SM linearizzata, con annotato uno stato iniziale (is).
	 * 
	 * @author Fabrizio Facchini
	 */
	private class TracciaTeStor extends Traccia{
		/**
		 * Lo stato iniziale di questa traccia.
		 * Poiché una traccia di questo tipo può non contenere transizioni,
		 * questo stato rappresenta una traccia di cui è specificato il solo stato iniziale.
		 */
		private ElementoStato is;
		/**
		 * Costruisce un nuovo oggetto TracciaTeStor sui parametri specificati.
		 * Costruisce una sup-trace, appartenente alla SM specificata,
		 * contenente il solo stato specificato.
		 * 
		 * @param sml una SM linearizzata
		 * @param is uno stato della SM linearizzata
		 * @throws WrongStateException se lo stato specificato non appartiene alla SM specificata
		 */
		private TracciaTeStor(SMLineare sml, ElementoStato is) throws WrongStateException{
			super(sml);
			if(!sml.getStateMachine().getListaStato().giaPresente(is.getName())){
				throw new WrongStateException("Lo stato specificato \""+is.getName()+"\" non appartiene alla StateMachine specificata \""+sml.getNomeSM()+"\".");
			}
			this.is = is;
		}
		/**
		 * Costruisce un nuovo oggetto TracciaTeStor.
		 * Costruisce una sup-trace, appartenente alla SM specificata,
		 * contenente solo il suo stato iniziale (start state).
		 * 
		 * @param sml una SM linearizzata
		 */
		private TracciaTeStor(SMLineare sml){
			this(sml,sml.getStartState());
		}
		/**
		 * Costruisce un nuovo oggetto TracciaTeStor sui parametri specificati.
		 * Costruisce una sup-trace impostata sulla traccia passata,
		 * assegnando come is lo stato iniziale della traccia, o lo StartState se vuota.
		 * 
		 * @param traccia una traccia lineare
		 */
		private TracciaTeStor(Traccia traccia) {
			this(traccia.getSmLineare(),
					traccia.isEmpty()?
						traccia.getSmLineare().getStartState():
						traccia.getStatoIniziale());
			super.aggiungiInCoda(traccia);
		}
		/**
		 * Copia questa sup-trace.
		 * Non sono clonate le transizioni della traccia, né lo stato iniziale is.
		 * 
		 * @return una copia di questa traccia
		 */
		private TracciaTeStor copia(){
			TracciaTeStor copia = new TracciaTeStor(getSmLineare(),getStatoIniziale());
			copia.aggiungiInCoda(this);
			return copia;
		}
		/* (non-Javadoc)
		 * @see plugin.TeStor.condivisi.AlgoritmoTeStor.Traccia#getStatoIniziale()
		 */
		/**
		 * Restituisce lo stato iniziale di questa sup-trace.
		 * Se risulta vuota allora restituisce lo stato associato is.
		 */
		protected ElementoStato getStatoIniziale() {
			return is;
		}
		/* (non-Javadoc)
		 * @see plugin.TeStor.condivisi.AlgoritmoTeStor.Traccia#getStatoFinale()
		 */
		/**
		 * Restituisce lo stato finale di questa sup-trace.
		 * Se risulta vuota allora restituisce lo stato associato is.
		 */
		protected ElementoStato getStatoFinale() {
			return isEmpty()?is:super.getStatoFinale();
		}
		/* (non-Javadoc)
		 * @see plugin.TeStor.condivisi.AlgoritmoTeStor.Traccia#isTransAddizionabile(core.a02thread.graph.ElementoMessaggio, boolean)
		 */
		/**
		 * Stabilisce se la transizione specificata è addizionabile a questa sup-trace.
		 * La transizione è <i>addizionabile</i> se valgono le seguenti condizioni:<br>
		 * - la transizione è presente nella state machine a cui appartiene questa sup-trace,<br>
		 * - lo stato terminale di questa sup-trace coincide con quello terminale della transizione.<br>
		 * Gli stati terminali di cui sopra dipendono dal valore di <code>inCoda</code>.
		 * 
		 * @param trans una transizione
		 * @param inCoda indica che si vuole stabilire se la transizione specificata può
		 * essere aggiunta in coda a questa traccia, piuttosto che in testa
		 * @return <code>true</code> se la transizione è addizionabile, <code>false</code> altrimenti
		 */
		protected boolean isTransAddizionabile(ElementoMessaggio trans, boolean inCoda) {
			return
				getSmLineare().getStateMachine().getListaMessaggio().giaPresente(trans.getName()) &&
				(inCoda?
					getStatoFinale().equals(trans.getElementFrom()):
					getStatoIniziale().equals(trans.getElementTo())
				);
		}
		/**
		 * Stabilisce se la sup-trace specificata è addizionabile a questa sup-trace.
		 * La sup-trace specificata è <i>addizionabile</i>:<br>
		 * - in coda, se il suo stato iniziale coincide con quello finale di questa sup-trace;<br>
		 * - in testa, se il suo stato finale coincide con quello iniziale di questa sup-trace.<br>
		 * 
		 * @param tracciaTeStor una sup-trace
		 * @param inCoda indica che si vuole stabilire se la sup-trace specificata può
		 * essere aggiunta in coda a questa sup-trace, piuttosto che in testa
		 * @return <code>true</code> se la sup-trace specificata è addizionabile, <code>false</code> altrimenti
		 */
		protected boolean isTracciaAddizionabile(TracciaTeStor tracciaTeStor, boolean inCoda){
			return 	inCoda?
						getStatoFinale().equals(tracciaTeStor.getStatoIniziale()):
						getStatoIniziale().equals(tracciaTeStor.getTransFinale());
		}
		/**
		 * Aggiunge in coda a questa sup-trace la sup-trace specificata.
		 * L'aggiunta in coda non avviene se la sup-trace non è addizionabile in coda a questa sup-trace.
		 * 
		 * @param tracciaTeStor una sup-trace
		 * @return <code>true</code> se l'aggiunta è avvenuta, <code>false</code> altrimenti
		 * @see AlgoritmoTeStor.TracciaTeStor#isTracciaAddizionabile(TracciaTeStor, boolean)
		 */
		protected boolean aggiungiInCoda(TracciaTeStor tracciaTeStor){
			if(isTracciaAddizionabile(tracciaTeStor,true)){
				return addAll(tracciaTeStor);
			}
			return false;
		}
		/**
		 * Aggiunge in testa a questa sup-trace la sup-trace specificata.
		 * L'aggiunta in testa non avviene se la sup-trace non è addizionabile in testa a questa sup-trace.
		 * 
		 * @param tracciaTeStor una sup-trace
		 * @return <code>true</code> se l'aggiunta è avvenuta, <code>false</code> altrimenti
		 * @see AlgoritmoTeStor.TracciaTeStor#isTracciaAddizionabile(TracciaTeStor, boolean)
		 */
		protected boolean aggiungiInTesta(TracciaTeStor tracciaTeStor){
			if(isTracciaAddizionabile(tracciaTeStor,false)){
				boolean aggiunta = addAll(0,tracciaTeStor);
				if(aggiunta){
					//is = tracciaTeStor.getStatoIniziale();
					aggiornaIs();
				}
				return aggiunta;
			}
			return false;
		}
		/* (non-Javadoc)
		 * @see plugin.TeStor.condivisi.AlgoritmoTeStor.Traccia#addFirst(core.a02thread.graph.ElementoMessaggio)
		 */
		protected boolean addFirst(ElementoMessaggio trans) {
			boolean aggiunta = super.addFirst(trans);
			if(aggiunta){
				//is = (ElementoStato)trans.getElementFrom();
				aggiornaIs();
			}
			return aggiunta;
		}
		/**
		 * Aggiorna lo stato is di questa sup-trace.
		 * Da utilizzare dopo aver alterato la testa di questa sup-trace.
		 */
		protected void aggiornaIs(){
			is = ((Traccia)this).getStatoIniziale();
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return isEmpty()?
					"ID = "+getIdTraccia()+":  ["+getStatoIniziale().getName()+"]":
					super.toString();
		}
	}
	/**
	 * Lista di elementi utilizzata durante la generazione dei casi di test
	 * per raccogliere le sup-trace di una stessa SM.
	 * É una lista (concatenata) di oggetti di tipo {@link TracciaTeStor}.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class TracceTeStor extends LinkedList{
		/**
		 * La SM lineare a cui appartengono queste tracce
		 */
		private final SMLineare sml;
		/**
		 * Costruisce un nuovo oggetto TracceTeStor.
		 * Imposta la SM lineare a cui appartengono tutte queste tracce.
		 * 
		 * @param sml una SM lineare
		 */
		private TracceTeStor(SMLineare sml) {
			super();
			this.sml = sml;
		}
		/**
		 * Aggiunge la traccia specificata a questa lista.
		 * L'aggiunta non avviene se la SM della traccia da aggiungere non
		 * coincide con quella associata.
		 * 
		 * @param tracciaTeStor una traccia
		 * @return <code>true</code> se l'aggiunta è avvenuta, <code>false</code> altrimenti
		 */
		private boolean add(TracciaTeStor tracciaTeStor) {
			return tracciaTeStor.getSmLineare().equals(getSml())?
					super.add(tracciaTeStor):
					false;
		}
		/**
		 * Aggiunge a queste tracce le tracce specificate.
		 * 
		 * @param tracceTeStor le tracce che si intendono aggiungere
		 * @return ???
		 */
		private boolean addAll(TracceTeStor tracceTeStor) {
			boolean modificato = false;
			Iterator scorriTracce = tracceTeStor.iterator();
			while(scorriTracce.hasNext()){
				if(add((TracciaTeStor)scorriTracce.next())){
					modificato = true;
				}
			}
			return modificato;
		}
		/**
		 * @return il campo sml.
		 */
		private SMLineare getSml() {
			return sml;
		}
	}
	/**
	 * Struttura fondamentale utilizzata durante la generazione dei casi di test.
	 * Associa ad ogni SM linearizzata dell'architettura la lista delle sue sup-trace
	 * e degli stati da queste raggiunti.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class TracceSM {
		/**
		 * Una SM linearizzata
		 */
		private final SMLineare sml;
		/**
		 * La lista delle sup-trace generate dalla SM
		 */
		private TracceTeStor tracceTeStor;
		/**
		 * Gli stati raggiunti in parallelo dalla SM associata durante la generazione dei casi di test.
		 * Uso: la chiave è la stringa rappresentante il nome dello stato, il valore è lo stato stesso.
		 */
		private final TreeMap statiRaggiunti = new TreeMap();
		/**
		 * Costruisce un nuovo oggetto TracceSM sulla SM linearizzata specificata.
		 * Associa quest'oggetto alla SM e crea una nuova {@link AlgoritmoTeStor.TracciaTeStor}
		 * impostata sullo stato iniziale della SM; imposta come unico stato raggiunto lo
		 * stato iniziale della SM.
		 * 
		 * @param sml una SM linearizzata
		 */
		private TracceSM(SMLineare sml) {
			super();
			this.sml = sml;
			ElementoStato statoIniziale = sml.getStartState();
			this.tracceTeStor = new TracceTeStor(sml);
			this.tracceTeStor.add(new TracciaTeStor(sml));
			this.statiRaggiunti.put(statoIniziale.getName(),sml.getStartState());
		}

		/**
		 * Restituisce la SM linearizzata a cui si riferiscono le sup-trace associate in quest'oggetto.
		 * 
		 * @return la SM linearizzata a cui si riferiscono le sup-trace associate in quest'oggetto
		 */
		private SMLineare getSml() {
			return sml;
		}

		/**
		 * Ritorna le sup-trace di quest'oggetto.
		 * 
		 * @return le sup-trace di quest'oggetto
		 */
		private TracceTeStor getTracceTeStor() {
			return tracceTeStor;
		}
		
		/**
		 * Restituisce gli stati raggiunti dalla SM associata.
		 * 
		 * @return il campo statiRaggiunti.
		 */
		private TreeMap getStatiRaggiunti() {
			return statiRaggiunti;
		}
		/**
		 * Imposta il valore del campo specificato.
		 * 
		 * @param tracceTeStor il valore per tracceTeStor da assegnare.
		 */
		private void setTracceTeStor(TracceTeStor tracceTeStor) {
			this.tracceTeStor = tracceTeStor;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			String out = "\n\tSM "+getSml().getNomeSM()+". Sup-trace generate ("+tracceTeStor.size()+"):";
			Iterator scorriTracceTeStor = getTracceTeStor().iterator();
			while(scorriTracceTeStor.hasNext()){
				out += "\n\t\t"+scorriTracceTeStor.next();
			}
			return out;
		}
	}
	/**
	 * Lista (concatenata) di elementi di tipo {@link AlgoritmoTeStor.TracceSM}
	 * utilizzata durante la generazione dei casi di test per memorizzare le sup-trace di ogni SM.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class SMtge extends LinkedList{
		/**
		 * Costruisce un nuovo oggetto SMtge sull'output della linearizzazione.
		 * Per ogni SM linearizzata crea una nuovo oggetto {@link AlgoritmoTeStor.TracceSM}
		 * sulla medesima SM linearizzata, inizialmente vuoto.
		 * 
		 * @param outputLinea l'output della linearizzazione
		 */
		private SMtge(SMLinearizzate outputLinea) {
			super();
			Iterator scorriSML = outputLinea.smLinearizzate.iterator();
			SMLineare smlCorr;
			while(scorriSML.hasNext()){
				smlCorr = (SMLineare)scorriSML.next();
				this.add(new TracceSM(smlCorr));	
			}
		}
		/**
		 * Aggiunge a quest'oggetto le tracce specificate
		 * 
		 * @param tracceSM le sup-trace di una stessa SM
		 * @return <code>true</code>
		 */
		private boolean add(TracceSM tracceSM) {
			return super.add(tracceSM);
		}
		/**
		 * Genera le sup-trace per le SM coinvolte dal messaggio specificato.
		 * Dato il messaggio corrente, ottiene tutte le tracce lineari che lo contengono (cioè che
		 * contengono una transizione con lo stesso nome) e di ognuna di esse stabilisce la validità.
		 * La validità di ognuna di queste tracce è stabilita dal metodo
		 * {@link AlgoritmoTeStor.Traccia#validazione(ElementoStato is, int posizione)},
		 * assumendo uno stato iniziale is (ottenuto da quest'oggetto)
		 * ed uno stato corrente (il secondo estremo dal messaggio corrente in ciascuna traccia lineare).
		 * 
		 * @param m un messaggio scambiato tra due processi dell'architettura
		 * @param outputLinea l'output della linearizzazione
		 * @throws TestCaseGenerationExceptionsList se non è stata prodotta alcuna sup-trace per il messaggio
		 */
		private void generaSupTraceDaMess(ElementoCanaleMessaggio m, SMLinearizzate outputLinea) throws TestCaseGenerationExceptionsList{
			// System.out.println("Messaggio "+m.getName());// DEBUG
			
			// lista per eventuali errori di generazione
			TestCaseGenerationExceptionsList listaEccezioni = new TestCaseGenerationExceptionsList();
			
			// sup-trace d'appoggio
			SMtge smtgeTemp = new SMtge(outputLinea);
			// iteriamo sulle SM per creare le sup-trace
			for(int i=0; i<outputLinea.getSmLinearizzate().size(); i++){
				TracceSM smCorr = (TracceSM)get(i);
				
				TracceSM smTemp = (TracceSM)smtgeTemp.get(i);
				smTemp.getTracceTeStor().clear();
				smTemp.getStatiRaggiunti().clear();
				// la SM lineare corrente
				SMLineare sml = smCorr.getSml();
				//System.out.println("\tAnalisi stati raggiunti nella SM "+sml.getNomeSM());// DEBUG
				if(sml.getNomeSM().equals(m.getElementFrom().getName()) || sml.getNomeSM().equals(m.getElementTo().getName())){
				// la SM corrente è una delle due coinvolte da m	
					// num. tracce valide
					int numTV = 0;
					// iteratore per gli stati precedentemente raggiunti da questa SM
					Iterator scorriStati = smCorr.getStatiRaggiunti().values().iterator();
					// scorriamo gli stati
					while(scorriStati.hasNext()){
						// assegnamo lo stato da raggiungere per la validazione
						ElementoStato statoCorr = (ElementoStato)scorriStati.next();
						//System.out.println("\t\tstato "+statoCorr.getName());// DEBUG
						// tracce valide per m da statoCorr
						Tracce tracceValide = new Tracce();
						try {
							tracceValide = sml.getTracceValide(m,statoCorr);
							numTV += tracceValide.size();
							// iteratore per tracce valide
							Iterator scorriTracceValide = tracceValide.iterator();
							// scorriamo le tracce valide
							while(scorriTracceValide.hasNext()){
								// traccia valida corrente
								Traccia tracciaValidacorr = (Traccia)scorriTracceValide.next();
								//System.out.println("\t\t\t\t"+tracciaValidacorr);// DEBUG
								// stato raggiunto dalla traccia
								ElementoStato statoRaggiunto = tracciaValidacorr.getStatoFinale();
								// creiamo una sup-trace corrispondente alla traccia e settiamo lo stato raggiunto
								TracciaTeStor tracciaTeStorValida = new TracciaTeStor(tracciaValidacorr);
								// aggiorniamo la mappa degli stati raggiunti
								smTemp.getStatiRaggiunti().put(statoRaggiunto.getName(),statoRaggiunto);
								// aggiungiamo la sup-trace alle altre già prodotte
								smTemp.getTracceTeStor().add(tracciaTeStorValida);
							}
						} catch (NoValidTracesFoundException e) {
							listaEccezioni.add(e);
						}
					}
					// controlliamo quante tracce valide abbiamo prodotto
					if(numTV <= 0){
						// solleviamo l'eccezione poiché non possiamo proseguire la generazione
						throw listaEccezioni;
					}
				}else{// la SM NON è coinvolta da m
					// ricopiamo le sup-trace per questa SM...
					// iteratore per gli stati precedentemente raggiunti da questa SM
					Iterator scorriStati = smCorr.getStatiRaggiunti().values().iterator();
					// scorriamo gli stati
					while(scorriStati.hasNext()){
						// stato raggiunto dalla traccia
						ElementoStato statoRaggiunto = (ElementoStato)scorriStati.next();
						smTemp.getTracceTeStor().add(new TracciaTeStor(sml,statoRaggiunto));
						smTemp.getStatiRaggiunti().put(statoRaggiunto.getName(),statoRaggiunto);
					}
				}
			}
			// assegnamo quanto prodotto...
			clear();
			addAll(smtgeTemp);
		}

		/**
		 * Genera i casi di test sui messaggi specificati.
		 * Il metodo cicla su ogni messaggio dell'iteratore, a partire dal primo, chiamando su
		 * questo il metodo {@link AlgoritmoTeStor.SMtge#generaSupTraceDaMess(ElementoCanaleMessaggio, SMLinearizzate)},
		 * che genera le <i>sup-trace</i> per le due SM coinvolte dal messaggio;
		 * quindi chiama {@link AlgoritmoTeStor.SMtge#generaOutSDdaSupTrace(ElementoMessaggio, SMLinearizzate)}
		 * che integra le <i>sup-trace</i> generando i relativi casi di test (parziali).
		 * I casi di test vengono collegati a quelli delle iterazioni precedenti.
		 * All'ultima iterazione i casi di test saranno quelli generati dal SD specificato.
		 * 
		 * @param scorriMess un iteratore contenente la sequenza di messaggi d'interesse
		 * @param outputLinea l'output della linearizzazione delle SM
		 * @return una lista contenente i casi di test ({@link SequenceElement}) per la sequenza messaggi specificata
		 * @throws TestCaseGenerationExceptionsList se la generazione di alcun caso di test è possibile
		 */
		private DefaultListModel generaCasiDiTestDaSeqMess(Iterator scorriMess, SMLinearizzate outputLinea) throws TestCaseGenerationExceptionsList{
			// la lista degli outSD per questa seq. mess.
			DefaultListModel casiDiTest = new DefaultListModel();
			// i casi di test ritornati ad ogni iterazione, e quelli progressivi
			LinkedList 	casiDiTestRitornati = /*new LinkedList()*/null, 
						casiDiTestProgressivi = /*new LinkedList()*/null;
			// il messaggio corrente
			ElementoCanaleMessaggio m;
			// contatore dei messaggi
			int contaMess = 0;
			// lista dei messaggi che è stato possibile generare (per eccezioni)
			LinkedList messaggiGenerati = new LinkedList();
			
			// inizializziamo la lista con un caso di test vuoto
			casiDiTestProgressivi = new LinkedList();
			casiDiTestProgressivi.add(new CasoDiTest("",plugData,outputLinea));
			
			// iteriamo sui messaggi nel SD
			while(scorriMess.hasNext()){
				// assegnamo il messaggio corrente
				m = (ElementoCanaleMessaggio)scorriMess.next();
				contaMess++;
				
				try {
					// GENERAZIONE DELLE SUP-TRACE GUIDA DAL MESSAGGIO CORRENTE
					generaSupTraceDaMess(m,outputLinea);
					
					// DEBUG - stampa sup-trace parziali ********************
					Iterator scorriSMtge = iterator();
					System.out.println("\n\nSup-trace progressive per il messaggio "+contaMess+" (da "+outputLinea.getTutteLeTrans().get(m.getName()).getSmProv().getNomeSM()+" verso "+outputLinea.getTutteLeTrans().get(m.getName()).getSmDest().getNomeSM()+") "+m.getName());
					while(scorriSMtge.hasNext()){
						System.out.println((TracceSM)scorriSMtge.next());// DEBUG
					}
					// ******************************************************
					
					// INTEGRAZIONE SUP-TRACE e GENERAZIONE OUTsd ***********
					casiDiTestRitornati = generaOutSDdaSupTrace(m,outputLinea);
					
					
				} catch (TestCaseGenerationExceptionsList e) {
					// impostiamo i campi per questa sequenza di messaggi (SD)
					e.setMessaggi(messaggiGenerati,m);
					throw e;
				}
				
				// integrazione casi di test parziali con i precedenti...
				Iterator scorriCasiPrecedenti = casiDiTestProgressivi.iterator();
				// lista d'appoggio per i casi di test progressivi
				LinkedList casiTemp = new LinkedList();
				// scorriamo i casi di test generati fino ad adesso per ampliarli con quelli ritornati
				while(scorriCasiPrecedenti.hasNext()){
					CasoDiTest casoPrecCorr = (CasoDiTest)scorriCasiPrecedenti.next();
					Iterator scorriCasiRitornati = casiDiTestRitornati.iterator();
					/* scorriamo i casi ritornati cercando quelli che hanno gli stati iniziali
					 * uguali a quelli raggiunti dal caso progressivo precedente corrente
					 */
					while(scorriCasiRitornati.hasNext()){
						CasoDiTest casoRitCorr = ((TracceSD)scorriCasiRitornati.next()).getCasoDiTest();
						if(casoPrecCorr.getStatiRaggiunti().equals(casoRitCorr.getStatiIniziali())){
						/* gli stati raggiunti per il caso precedente corrente coincidono
						 * con quelli iniziali del caso ritornato corrente
						 */
							// copiamo il caso di test precedente corrente
							CasoDiTest casoNuovo = casoPrecCorr.copia();
							// impostiamo gli stati raggiunti con quelli del caso ritornato corrente
							casoNuovo.setStatiRaggiunti(casoRitCorr.getStatiRaggiunti());
							// gli aggiungiamo i messaggi del caso ritornato corrente
							casoNuovo.aggiungiMessaggi(casoRitCorr.getSequenceElement().getListaSeqLink().iteratorTemporal());
							// aggiungiamo il nuovo caso alla lista d'appoggio
							casiTemp.add(casoNuovo);
						}
					}					
				}
				// impostiamo i casi progressivi con quelli appena generati (nella lista d'appoggio)
				casiDiTestProgressivi = casiTemp;
				//*******************************************************
				messaggiGenerati.add(m);// per le eccezioni...
			
			}
			
			// costruzione della lista di SequenceElement (i casi di test) da ritornare
			for(int i = 0; i<casiDiTestProgressivi.size(); i++){
				casiDiTest.addElement(((CasoDiTest)casiDiTestProgressivi.get(i)).getSequenceElement());
			}
			return casiDiTest;
		}

		/**
		 * La funzione genera i casi di test a partire dalle sup-trace in quest'oggetto,
		 * generate dal messaggio specificato.
		 * Essa produce tutte le combinazioni di n-uple (se n sono le SM dell'architettura)
		 * di <i>sup-trace</i>, rappresentanti le possibili evoluzioni dell'interazione delle SM.
		 * Per ogni n-upla chiama {@link AlgoritmoTeStor#integra(String, TracceSD, SMLinearizzate)}
		 * sulla <i>sup-trace</i> più lunga, integrando i risultati delle diverse chiamate.
		 * Al termine della sua esecuzione restituisce tutti i casi di test (parziali)
		 * implicati, con annotati gli stati raggiunti nelle SM.
		 * 
		 * @param messaggioGuida un messaggio nel SD in input
		 * @param outputLinea l'output della linearizzazione
		 * @return una lista di casi ti test (oggetti di tipo {@link TracceSD})
		 * @throws TestCaseGenerationExceptionsList se nessuna combinazione di stati/sup-trace dà luogo ad un caso di test parziale
		 */
		private LinkedList generaOutSDdaSupTrace(ElementoCanaleMessaggio messaggioGuida, SMLinearizzate outputLinea) throws TestCaseGenerationExceptionsList{
			// lista per eventuali eccezioni
			TestCaseGenerationExceptionsList listaEccezioni = new TestCaseGenerationExceptionsList();
			// una lista di casi di test...
			LinkedList casiDiTestParziali = new LinkedList();
			// il numero di SM dell'architettura
			int numSM = size();
			// contatore delle combinazioni
			int numComb = -1;
			// contatore dei casi generati (cioè senza eccezioni)
			int numOutSD = 0;
			// contiene una combinazione che può dare origine ad un caso di test
			// la chiave è il nome della SM, il valore una TracciaSD
			TracceSD tracceSD = new TracceSD(outputLinea);
			
			String prov = messaggioGuida.getElementFrom().getName();
			String dest = messaggioGuida.getElementTo().getName();
			
			// array di iteratori, uno per ogni SM
			Iterator[] iterSupTrace = new Iterator[numSM];
			// traccia d'appoggio
			TracciaTeStor tracciaTemp;
			
			
			System.out.println("\nGenerazione casi di test: combinazione delle possibilità");// DEBUG
			// realizziamo le combinazioni di sup-trace guida e su queste eseguiamo l'integrazione
			// indice dell'iteratore attivo
			int indiceIter = -1;
			// ciclo principale: ad ogni iterazione si produce una nuova combinazione di sup-trace guida
			while(true){
				// ciclo di azzeramento degli iteratori
				while(true){
					if(indiceIter+1 < numSM){
						indiceIter++;
						//System.out.println("indiceIter = "+indiceIter+", azzeramento iteratore corrente");// DEBUG
						iterSupTrace[indiceIter] = ((TracceSM)get(indiceIter)).getTracceTeStor().iterator();
						tracciaTemp = (TracciaTeStor)(iterSupTrace[indiceIter]).next();
						tracceSD.sotituisciTraccia(tracciaTemp,prov,dest);
					}else{
						break;
					}
				}
				
				numComb++;
				// aggiornamento
				numOutSD++;
				// DEBUG - Stampa combinazione corrente di sup-trace guida *********
				System.out.println("\nCombinazione "+numComb+":");
				for(int i=0; i < numSM; i++){
					System.out.println("\t"+(TracciaSD)tracceSD.get(((TracceSM)get(i)).getSml().getNomeSM()));
				}
				// *****************************************************************
				// CODICE INTEGRAZIONE SUP-TRACE E GENERAZIONE CASI DI TEST	********
				// individuiamo le 2 sup-trace guida
				TracciaSD tracciaProv = ((TracciaSD)tracceSD.get(prov))/*.getSupTrace()*/;
				TracciaSD tracciaDest = ((TracciaSD)tracceSD.get(dest))/*.getSupTrace()*/;

				// assegnamo la traccia più lunga (ma è ininfluente)
				TracciaSD tracciaSDCorr = tracciaProv.size()>=tracciaDest.size()?tracciaProv:tracciaDest;
				
				try {
					// integriamo...*****************************************************
					casiDiTestParziali.addAll(tracceSD.copia().integra(tracciaSDCorr.getNomeProcesso(),tracciaSDCorr.size(), outputLinea));
					// ******************************************************************
				} catch (TestCaseGenerationExceptionsList el) {
					listaEccezioni.addAll(el);
					// decrementiamo numOutSD
					numOutSD--;
				}
				
				// ciclo di assegnamento minima variazione (cerca di effettuare next() sull'iteratore con indice maggiore)
				while(true){
					if(iterSupTrace[indiceIter].hasNext()){
						tracciaTemp = (TracciaTeStor)(iterSupTrace[indiceIter]).next();
						tracceSD.sotituisciTraccia(tracciaTemp,prov,dest);
						break;
					}else{
						indiceIter--;
						//System.out.println("indiceIter = "+indiceIter);// DEBUG
						if(indiceIter <= -1){
							break;
						}
					}
				}
				if(indiceIter <= -1){
					break;
				}
			}
			
			// controllo sul numero dei casi di test parziali effettivamente generati
			if(numOutSD <= 0){
				// nessun caso di test parziale
				throw listaEccezioni;
			}
			
			// AGGIORNAMENTO...
			// aggiornamento di questo oggetto per tener conto dell'avanzamento nella generazione...
			TreeMap tutteLeSM = new TreeMap();
			Iterator scorriSM = outputLinea.getSmLinearizzate().iterator();
			// inizializzazione mappa degli stati raggiunti nelle varie SM
			while(scorriSM.hasNext()){
				// per ogni SM creiamo una mappa (vuota) degli stati in essa raggiunti
				tutteLeSM.put(((SMLineare)scorriSM.next()).getNomeSM(),new TreeMap());
			}
			// iteratore per i casi parziali prodotti
			Iterator scorriCasi = casiDiTestParziali.iterator();
			/* per ogni caso di test aggiungiamo gli stati da esso raggiunti in
			 * ciascuna SM nella mappa tutteLeSM al posto giusto
			 */ 
			while(scorriCasi.hasNext()){
				TreeMap statiRaggiuntiInCaso = ((TracceSD)scorriCasi.next()).getCasoDiTest().getStatiRaggiunti();
				scorriSM = outputLinea.getSmLinearizzate().iterator();
				while(scorriSM.hasNext()){
					SMLineare smCorr = (SMLineare)scorriSM.next();
					ElementoStato statoRaggiuntoInSMcorr = (ElementoStato)statiRaggiuntiInCaso.get(smCorr.getNomeSM());
					TreeMap statiRaggiuntiInSMcorr = (TreeMap)tutteLeSM.get(smCorr.getNomeSM());
					statiRaggiuntiInSMcorr.put(statoRaggiuntoInSMcorr.getName(),statoRaggiuntoInSMcorr);
				}
			}
			// trasformiamo la mappa tutteLeSM  in un nuovo oggetto SMtge
			SMtge nuovoSMtge = new SMtge(outputLinea);
			Iterator scorriSMtge = nuovoSMtge.iterator();
			// per ogni SM creiamo le sup-trace contenenti gli stati raggiunti in quella SM
			while(scorriSMtge.hasNext()){
				TracceSM tracceSMcorr = (TracceSM)scorriSMtge.next();
				// cancelliamo la sup-trace di default
				tracceSMcorr.setTracceTeStor(new TracceTeStor(tracceSMcorr.getSml()));
				// cancelliamo gli stati raggiunti di default
				tracceSMcorr.getStatiRaggiunti().clear();
				Iterator scorriStatiRaggiunti = ((TreeMap)tutteLeSM.get(tracceSMcorr.getSml().getNomeSM())).values().iterator();
				while(scorriStatiRaggiunti.hasNext()){
					ElementoStato statoRaggiunto = (ElementoStato)scorriStatiRaggiunti.next();
					tracceSMcorr.getTracceTeStor().add(new TracciaTeStor(tracceSMcorr.getSml(),statoRaggiunto));
					tracceSMcorr.getStatiRaggiunti().put(statoRaggiunto.getName(),statoRaggiunto);
				}
			}
			// rimuoviamo le vecchie sup-trace e aggiungiamo le nuove
			removeRange(0,nuovoSMtge.size());
			addAll(nuovoSMtge);
			
			
			return casiDiTestParziali;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			String out = "\n\nSup-trace per le SM dell'architettura:";
			Iterator scorriSMtge = iterator();
			while(scorriSMtge.hasNext()){
				out += (TracceSM)scorriSMtge.next();
			}
			
			return out+"\n";
		}
	}
	/**
	 * La struttura che lega una SM alla sue tracce lineari.
	 * Associa una SM dell'architettura ad un oggetto di tipo {@link Tracce}.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class SMLineare {
		/**
		 * Il processo il cui primo thread (indice 0) è la SM d'interesse.
		 */
		private final ListaThread processo;
		/**
		 * Una SM dell'architettura (thread 0 di un qualche processo).
		 */
		private final ThreadElement sm;
		/**
		 * La lista delle tracce lineari della SM.
		 */
		private final Tracce tracce;
		/**
		 * Un riferimento alle informazioni di linearizzazione delle transizioni
		 */
		private final InfoTransizioni rifInfoTrans;
		/**
		 * Costruisce un nuovo oggetto SMLineare sul processo specificato.
		 * Viene creata una nuova SM lineare sulla macchina a stati identificata
		 * dal thread 0 del processo.
		 * Richiede un riferimento alle informazioni sulla linearizzazione (in corso)
		 * delle transizioni dei processi presenti nell'architettura.
		 * 
		 * @param processo una SM dell'architettura.
		 * @param rifInfoTrans riferimento alle informazioni sulle transizioni
		 * @throws LinearizationException se non è stato possibile linearizzare il processo
		 */
		private SMLineare(ListaThread processo, InfoTransizioni rifInfoTrans)
					throws LinearizationException {
			this.processo = processo;
			try {
				this.sm = processo.get(0);
			} catch (IndexOutOfBoundsException e) {
				throw new NoThreadDefinedException(processo);
			}
			this.tracce = new Tracce();
			this.rifInfoTrans = rifInfoTrans;
			//*******************************
			linearizzaSM();
			//*******************************
		}
		/**
		 * Ritorna la SM relativa a questo oggetto
		 * 
		 * @return la SM relativa a questo oggetto
		 */
		private ThreadElement getStateMachine() {
			return sm;
		}
		/**
		 * Restituisce il processo associato a questa SM linearizzata.
		 * 
		 * @return il processo associato a questa SM linearizzata
		 */
		private ListaThread getProcesso() {
			return processo;
		}
		/**
		 * Ritorna le tracce lineari di questo oggetto
		 * 
		 * @return le tracce lineari di questo oggetto
		 */
		private Tracce getTracce(){
			return tracce;
		}
		
		
		///ezio 2006 - il metodo 
		/**
		 * Ritorna la traccia lineare con identificatore passato come argomento.
		 */
		public Traccia getTraccia(long idTraccia){
			
//			 iteratore per le tracce
			Iterator scorriTracce = tracce.iterator();
			// traccia corrente
			Traccia tracciaCorr;
			while(scorriTracce.hasNext()){
				tracciaCorr = (Traccia)scorriTracce.next();
				if (tracciaCorr.getIdTraccia()==idTraccia)
					return tracciaCorr;
			}	
			return null;
				
		}
		
		
		/**
		 * Restituisce il nome della SM, cioè del processo che contiene come thread 0 questa SM.
		 * 
		 * @return il nome della SM (linearizzata)
		 */
		private String getNomeSM(){
			if (getProcesso()==null) return null;
			return getProcesso().getNameProcesso();
		}
		
		/**
		 * Restituisce il riferimento alle informazioni di linearizzazione delle transizioni
		 * 
		 * @return il campo rifInfoTrans.
		 */
		private InfoTransizioni getRifInfoTrans() {
			return rifInfoTrans;
		}
		/**
		 * Restituisce tutte le tracce di questa SM linearizzata che hanno per stato finale lo stato specificato.
		 * 
		 * @param stato uno stato di questa SM
		 * @return le tracce il cui stato finale è quello specificato
		 */
		private Tracce getTracceDaStatoFinale(ElementoStato stato){
			Tracce tracceTrovate = new Tracce();
			Traccia tracciaCorr;
			Iterator scorriTracce = getTracce().iterator();
			while(scorriTracce.hasNext()){
				tracciaCorr = (Traccia)scorriTracce.next();
				if(tracciaCorr.getStatoFinale()==stato){
					tracceTrovate.add(tracciaCorr);
				}
			}
			return tracceTrovate;
		}
		/**
		 * Restituisce lo stato iniziale (start state) della SM associata.
		 * 
		 * @return lo stato iniziale della SM associata
		 */
		private ElementoStato getStartState(){
			return getStateMachine().getListaStato().getStartState();
		}

		/**
		 * Aggiunge le tracce lineari specificate, e le informazioni sulle transizioni
		 * contenute, a questa SM.
		 * Da usare in corso di linearizzazione.
		 * 
		 * @param tracce delle tracce lineari prodotte in questa SM linearizzata
		 */
		private void aggiungiTracce(Tracce tracce){
			// contatore per la posizione nella traccia corrente
			int i;
			// iteratore per le tracce
			Iterator scorriTracce = tracce.iterator();
			// traccia corrente
			Traccia tracciaCorr;
			// iteratore per le trans. nella traccia corrente
			Iterator scorriTrans;
			// trans. corrente
			ElementoMessaggio transCorr;
			// scorriamo le tracce da aggiungere
			while(scorriTracce.hasNext()){
				tracciaCorr = (Traccia)scorriTracce.next();
				// aggiungiamo la traccia corrente alle tracce di questa SM linearizzata
				getTracce().add(tracciaCorr);
				scorriTrans = tracciaCorr.iterator();
				// posizione iniziale
				i=0;
				// scorriamo le trans. della traccia corrente
				while(scorriTrans.hasNext()){
					transCorr = (ElementoMessaggio)scorriTrans.next();
					// aggiorniamo le informazioni della trans. corrente
					rifInfoTrans.aggiornaInfoTrans(transCorr,tracciaCorr,i);
					i++;
				}
			}
		}
		/**
		 * Esegue la linearizzazione di questa SM.
		 * Chiama {@link AlgoritmoTeStor.SMLineare#linearizzaSMdaStato(Elementostato,MappaStati)}
		 * sullo stato iniziale della SM, passando una nuova MappaStati.
		 * 
		 * @throws StartStateNotDefinedException se questa SM non ha uno StartState
		 */
		private void linearizzaSM() throws StartStateNotDefinedException {
			ElementoStato startState = getStartState();
			if(startState != null){
				aggiungiTracce(linearizzaSMdaStato(startState,new MappaStati()));
			}else{
				throw new StartStateNotDefinedException(getProcesso());
			}
		}
		/**
		 * Esegue la linearizzazione di questa SM a partire dallo stato specificato.
		 * Se lo stato si rivela un Head of Loop o un pozzo non restituisce nulla,
		 * altrimenti restituisce tutte le tracce lineari che hanno lo stato come primo estremo (provvisorio),
		 * chiamando se stessa sullo stato secondo estremo di ciascuna transizione uscente dallo stato.
		 * Una <i>traccia lineare</i> di una SM rappresenta:<br>
		 * un cammino diretto non contenente cicli (ma esso stesso può essere un ciclo)<br>
		 * - dallo stato iniziale della SM, o da uno stato <i>Head of a Loop</i> (HoaL)<br>
		 * - verso lo stato iniziale della SM, uno stato HoaL oppure un pozzo.
		 * <p>
		 * Le tracce complete ottenute da questa funzione sono lineari <i>per costruzione</i>.
		 * 
		 * @param stato uno stato di questa SM lineare
		 * @param statiVisitati la mappa contenente informazioni sugli stati visitati
		 * @return 	le tracce lineari provvisorie che hanno lo stato specificato in testa,
		 * 			oppure nulla
		 */
		private Tracce linearizzaSMdaStato(ElementoStato stato, MappaStati statiVisitati){
		    // informazioni sullo stato
			InfoStato infoStato;
			// stato secondo estremo della trans. corrente
			ElementoStato secondoEstremo;
			// informazioni sullo stato secondo estremo della trans. corrente
			InfoStato infoSecondoEstremo;
			// traccia d'appoggio
			Traccia traccia = null;
			// tutte le transizioni entranti e uscenti dallo stato
			LinkedList trans = getStateMachine().getListaMessaggio().getListaMessaggio(stato);
			// iteratore delle transizioni
			Iterator scorriTrans = trans.iterator();
			// la trans. corrente
			ElementoMessaggio transCorr;
			// lista di tracce da restituire
			Tracce tracceDaRestituire = new Tracce();
			// lista di tracce restituite da una chiamata della funzione su uno stato secondo estremo
			Tracce tracceRestituite;
			// iteratore per le tracce restituite
			Iterator scorriTracce;
			// traccia corrente
			Traccia tracciaCorr;
			
			//System.out.println("\nLinearizzaSMdaStato: linearizzazione stato "+stato.getName());//DEBUG
			// aggiorna le informazioni sullo stato
			statiVisitati.put(stato);
			//System.out.println("LinearizzaSMdaStato "+stato.getName()+": aggiunto stato a stati visitati");//DEBUG
			
			while(scorriTrans.hasNext()){
				transCorr = (ElementoMessaggio)scorriTrans.next();
				//System.out.println("LinearizzaSMdaStato "+stato.getName()+": analisi trans. "+transCorr.getName());//DEBUG
				if(transCorr.getElementFrom()!=stato){// trans. entrante
					//System.out.println("			Trans. "+transCorr.getName()+" entrante");//DEBUG
				}else{// trans. USCENTE
					//System.out.println("			Trans. "+transCorr.getName()+" uscente");//DEBUG
					secondoEstremo = (ElementoStato)transCorr.getElementTo();
					//System.out.println("LinearizzaSMdaStato "+stato.getName()+": stato secondo estremo "+secondoEstremo.getName()+" già incontrato????");//DEBUG
					if(statiVisitati.contieneChiave(secondoEstremo)){// secondo estremo già incontrato
						//System.out.println("LinearizzaSMdaStato "+stato.getName()+": stato secondo estremo "+secondoEstremo.getName()+" già incontrato");//DEBUG
						// controlliamo lo stato secondo estremo
						infoSecondoEstremo = statiVisitati.get(secondoEstremo);
						// controlliamo la natura del secondo estremo
						if(!infoSecondoEstremo.isIndeterm && !infoSecondoEstremo.isHoaL){// secondo estremo stato intermedio *******
							//System.out.println("\nLinearizzaSMdaStato "+stato.getName()+": stato secondo estremo "+secondoEstremo.getName()+" NON HoaL e nemmeno pozzo");//DEBUG
							// recuperiamo le tracce prodotte a partire dallo stato secondo estremo
							tracceRestituite = infoSecondoEstremo.tracce;
							scorriTracce = tracceRestituite.iterator();
							/* per ogni traccia prodotta dallo stato secondo estremo
							 * creiamo una traccia uguale alla traccia restituita con in testa la trans.
							 */
							while(scorriTracce.hasNext()){
								tracciaCorr = (Traccia)scorriTracce.next();
								//System.out.println("LinearizzaSMdaStato "+stato.getName()+": restituita dalla ricorsione traccia "+tracciaCorr);//DEBUG
								traccia = new Traccia(this);
								traccia.add(transCorr);
								traccia.aggiungiInCoda(tracciaCorr);
								tracceDaRestituire.add(traccia);
								//System.out.println("LinearizzaSMdaStato "+stato.getName()+": creata traccia "+traccia);//DEBUG
							}
						}else{// secondo estremo HoaL
							// aggiorniamo le informazioni sul secondo estremo
							infoSecondoEstremo.isIndeterm = false;
							infoSecondoEstremo.isHoaL = true;
							//System.out.println("LinearizzaSMdaStato "+stato.getName()+": stato secondo estremo "+secondoEstremo.getName()+" diviene HoaL");//DEBUG
							// creo una nuova traccia per la trans.
							traccia = new Traccia(this);
							traccia.add(transCorr);
							//System.out.println("			Trans. "+transCorr.getName()+": creata nuova traccia "+traccia);//DEBUG
							tracceDaRestituire.add(traccia);
							//System.out.println("LinearizzaSMdaStato "+stato.getName()+": creata traccia "+traccia);//DEBUG
						}
					}else{// secondo estremo NUOVO
						//System.out.println("LinearizzaSMdaStato "+stato.getName()+": stato secondo estremo "+secondoEstremo.getName()+" NUOVO, si lancia la ricorsione");//DEBUG
						// RICORSIONE
						tracceRestituite = linearizzaSMdaStato(secondoEstremo,statiVisitati);
						infoSecondoEstremo = statiVisitati.get(secondoEstremo);
						if(tracceRestituite.size()>0){// bisogna accodarle
							//System.out.println("\nLinearizzaSMdaStato "+stato.getName()+": stato secondo estremo "+secondoEstremo.getName()+" NON HoaL e nemmeno pozzo");//DEBUG
							scorriTracce = tracceRestituite.iterator();
							/* per ogni traccia restituita dalla chiamata sullo stato secondo estremo
							 * creiamo una traccia uguale alla traccia restituita con in testa la trans.
							 */
							while(scorriTracce.hasNext()){
								tracciaCorr = (Traccia)scorriTracce.next();
								//System.out.println("LinearizzaSMdaStato "+stato.getName()+": restituita dalla ricorsione traccia "+tracciaCorr);//DEBUG
								traccia = new Traccia(this);
								traccia.add(transCorr);
								traccia.aggiungiInCoda(tracciaCorr);
								tracceDaRestituire.add(traccia);
								//System.out.println("LinearizzaSMdaStato "+stato.getName()+": creata traccia "+traccia);//DEBUG
							}
						}else{// secondo estremo HoaL o pozzo: niente accodamento
							//System.out.println("\nLinearizzaSMdaStato "+stato.getName()+": stato secondo estremo "+secondoEstremo.getName()+" HoaL oppure pozzo");//DEBUG
							// creo una nuova traccia per la trans.
							traccia = new Traccia(this);
							traccia.add(transCorr);
							//System.out.println("			Trans. "+transCorr.getName()+": creata nuova traccia "+traccia);//DEBUG
							tracceDaRestituire.add(traccia);
							//System.out.println("LinearizzaSMdaStato "+stato.getName()+": creata traccia "+traccia);//DEBUG
						}
					}
				}
			}
			infoStato = statiVisitati.get(stato);
			// impostiamo la natura dello lo stato
			if(infoStato.isIndeterm){// non è stato possibile ritornarci da cammini inesplorati
				infoStato.isIndeterm = false;
				infoStato.isHoaL = false;
				//System.out.println("LinearizzaSMdaStato "+stato.getName()+": questo stato è HoaL? "+infoStato.isHoaL);//DEBUG
			}
			if(infoStato.isHoaL){// stato estremo
				/* bisogna aggiungere le tracce da restituire alla lista con tutte le
				 * tracce prodotte, e non restituire nulla;
				 */
				aggiungiTracce(tracceDaRestituire);
				tracceDaRestituire.clear();
			}
			infoStato.tracce = tracceDaRestituire;// aggiornamento ****************
			return tracceDaRestituire;
		}
		/**
		 * Genera tutte le tracce valide per la transizione specificata all'indietro verso
		 * lo stato specificato.
		 * Prima cerca tutte le tracce lineari di questa SM contenenti la transizione,
		 * quindi, per ognuna di esse, chiama il metodo {@link Traccia#validazione(ElementoStato, int)}
		 * sullo stato specificato e la posizione identificante la transizione all'interno della traccia.
		 * 
		 * @param transizione una transizione in questa SM lineare
		 * @param stato uno stato in questa SM lineare
		 * @return le tracce valide dallo stato alla transizione
		 * @throws NoValidTracesFoundException se non è stato possibile generare alcuna traccia valida
		 */
		private Tracce getTracceValide(ElementoCanaleMessaggio transizione, ElementoStato stato) throws NoValidTracesFoundException{
			// tutte le tracce valide sulla trans.
			Tracce tracceValide = new Tracce();
			// iteratore per le informazioni su tracce e posizioni
			Iterator scorriInfo = rifInfoTrans.get(transizione.getName()).iterator();
			while(scorriInfo.hasNext()){
				// informazione corrente
				InfoTracciaTrans infoTraccia = (InfoTracciaTrans)scorriInfo.next();
				
				
				if(infoTraccia.getTraccia().getSmLineare().equals(this)){// la SM è giusta
					// validazione traccia e aggiunta tracce ritornate
					tracceValide.addAll(infoTraccia.getTraccia().validazione(stato,infoTraccia.getPosizione()));
				}
				
				///ezio 2006 - fixed bug - introduciamo controllo se è un ciclo terminale sullo stato iniziale.
				/*if((infoTraccia.getTraccia().getSmLineare().equals(this))&&
						(transizione instanceof ElementoSeqLink)&&
						(infoTraccia.getTraccia().getTransFinale().getName().compareTo(transizione.getName())==0)&&
						infoTraccia.getTraccia().getStatoFinale().getId()==stato.getId()){
					
					tracceValide.addAll(infoTraccia.getTraccia().validazione(stato,infoTraccia.getPosizione(),true));
				}*/
				//////////////////
				
				
			}
			if(tracceValide.isEmpty()){
				throw new NoValidTracesFoundException(this,stato,transizione);
			}
			return tracceValide;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			String out = "\nTracce lineari della SM "+getNomeSM()+" ("+getTracce().size()+" tracce):";
			Iterator scorriTraccia = getTracce().iterator();
			while(scorriTraccia.hasNext()){
				out += "\n\t"+(Traccia)scorriTraccia.next();
			}
			return out;
		}
	}
	/**
	 * La struttura prodotta dalla linearizzazione.
	 * Contiene la versione linearizzata delle SM dell'architettura,
	 * ed una struttura efficiente per il reperimento delle informazioni necessarie
	 * alla generazione dei casi di test.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class SMLinearizzate {
		/**
		 * Informazioni di linearizzazione sulle transizioni dell'architettura.
		 */
		private final InfoTransizioni tutteLeTrans = new InfoTransizioni();
		/**
		 * La lista di tutte le SM nella versione lineare.
		 */
		private final LinkedList smLinearizzate = new LinkedList();
		/**
		 * Costruisce un nuovo oggetto SMLinearizzate.
		 * Genera le SM linearizzate sui processi specificati.
		 * 
		 * @param processi i processi dell'architettura
		 * @throws LinearizationExceptionsList se almeno una delle SM non può essere linearizzata
		 */
		private SMLinearizzate(ListaDP processi)
					throws LinearizationExceptionsList {
			// lista per le eventuali eccezioni di linearizzazione
			LinearizationExceptionsList listaEccezioni = new LinearizationExceptionsList();
			// indica che c'è stata almeno un'eccezione di linearizzazione
			boolean sollevataEccezione = false;
			// l'iteratore per la lista di SM
			Iterator scorriProcessi = processi.iterator();
			// linearizza ciascuna SM
			while(scorriProcessi.hasNext()){
				try {
					smLinearizzate.add(new SMLineare((ListaThread)scorriProcessi.next(),tutteLeTrans));
				} catch (LinearizationException e) {
					// aggiungiamo l'eccezione alle altre
					listaEccezioni.add(e);
					sollevataEccezione = true;
				}
			}
			if(sollevataEccezione){
				throw listaEccezioni;
			}
		}
		/**
		 * Restituisce le SM linearizzate contenute in quest'oggetto.
		 * 
		 * @return le SM linearizzate contenute in quest'oggetto
		 */
		private LinkedList getSmLinearizzate() {
			return smLinearizzate;
		}

		/**
		 * Restituisce le informazioni di linearizzazione delle transizioni dell'architettura.
		 * 
		 * @return le informazioni di linearizzazione delle transizioni dell'architettura
		 */
		private InfoTransizioni getTutteLeTrans() {
			return tutteLeTrans;
		}
		/**
		 * Restituisce la SM lineare indicata dal nome specificato.
		 * 
		 * @param nomeSM il nome di una SM dell'architettura
		 * @return la SM lineare con quel nome, null altrimenti
		 */
		private SMLineare getSMdaNome(String nomeSM){
			for(int i = 0; i < smLinearizzate.size(); i++){
				SMLineare smCorr = (SMLineare)smLinearizzate.get(i);
				if(smCorr.getNomeSM() == nomeSM){
					return smCorr;
				}
			}
			return null;
		}

	}
	/**  
	 * Informazioni associate ad una transizione al fine di reperire una traccia linearizzata
	 * (e la posizione al suo interno) in cui la transizione si trova.
	 * Un nuovo oggetto di questo tipo è creato durante ogni aggiunta di una transizione
	 * ad una traccia lineare in fase di linearizzazione.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class InfoTracciaTrans {
		/**
		 * Una traccia lineare contenuta in una SM in corso di linearizzazione.
		 */
		private final Traccia traccia;
		/**
		 * Una posizione all'interno della traccia.
		 */
		private final int posizione;
		/**
		 * Costruisce un nuovo oggetto InfoTracciaTrans sui parametri specificati.
		 * Data una transizione in una certa traccia lineare,
		 * memorizza la traccia e la posizione nella traccia
		 * in cui essa si trova.
		 * 
		 * @param traccia una traccia lineare di una SM
		 * @param pos una posizione nella traccia
		 */
		private InfoTracciaTrans(Traccia traccia,int pos){
			this.traccia = traccia;
			this.posizione = pos;
		}
		/**
		 * @return il campo posizione.
		 */
		private int getPosizione() {
			return posizione;
		}
		/**
		 * @return il campo traccia.
		 */
		private Traccia getTraccia() {
			return traccia;
		}
	}
	/**
	 * Struttura che associa ad ogni transizione dell'architettura
	 * (la <i>chiave</i> è rappresentata dalla stringa che è il nome della transizione)
	 * l'informazione sulle tracce (lineari) in cui essa appare
	 * (il <i>valore</i> è un oggetto di tipo {@link InfoTrans}).
	 * Questa struttura è costruita nella fase di linearizzazione.
	 */
	private class InfoTransizioni extends TreeMap {
		/**
		 * Valore da associare alla chiave rappresentante una transizione
		 * nella struttura di tipo {@link SMLinearizzate#tutteLeTrans}.
		 * É una lista (concatenata) di oggetti di tipo {@link InfoTracciaTrans}.
		 * Utilizzata durante la generazione dei casi di test,
		 * data una transizione dell'architettura
		 * fornisce tutte le informazioni circa le SM e le tracce lineari
		 * (e la posizione nelle tracce) in cui la transizione appare.
		 * 
		 * @author Fabrizio Facchini
		 */
		private class InfoTrans extends LinkedList{
			/**
			 * Indica che il messaggio etichettante la transizione associata è interno.
			 * CONVENZIONE: se il messaggio scambiato è INTERNO (tau)
			 * impostiamo la SM di PROVENIENZA uguale a quella di DESTINAZIONE
			 */
			private final boolean isInterno;
			/**
			 * La SM linearizzata di provenienza del messaggio etichettante la transizione associata.
			 */
		    private SMLineare smProv = null;
		    /**
			 * La SM linearizzata di destinazione del messaggio etichettante la transizione associata
			 */
		    private SMLineare smDest = null;
		    /**
		     * Costruisce un nuovo oggetto InfoTrans specificando se
		     * il messaggio etichettante la transizione associata è interno
		     * 
		     * @param isInterno specifica un messaggio interno
		     */
		    private InfoTrans(boolean isInterno) {
		    	this.isInterno = isInterno;
		    }
			/**
			 * Accoda a quest'oggetto una nuova informazione sulla localizzazione
			 * della transizione associata.
			 * Crea un nuovo oggetto di tipo {@link AlgoritmoTeStor.InfoTracciaTrans}
			 * e lo aggiunge a quest'oggetto.
			 * 
			 * @param traccia una traccia lineare
			 * @param pos una posizione nella traccia
			 */
			private boolean add(Traccia traccia,int pos) {		
				return super.add(new InfoTracciaTrans(traccia,pos));
			}
			
            /**
             * @return Ritorna la SM destinazione del messaggio etichettante la transizione.
             */
            private SMLineare getSmDest() {
                return smDest;
            }
            /**
             * @param smDest la SM da assegnare come destinazione del messaggio etichettante la transizione.
             */
            private void setSmDest(SMLineare smDest) {
                this.smDest = smDest;
            }
            /**
             * @return Ritorna la SM di provenienza del messaggio etichettante la transizione.
             */
            private SMLineare getSmProv() {
                return smProv;
            }
            /**
             * @param smProv la SM da assegnare come provenienza del messaggio etichettante la transizione.
             */
            private void setSmProv(SMLineare smProv) {
                this.smProv = smProv;
            }
			/**
			 * @return il campo isInterno.
			 */
			private boolean isInterno() {
				return isInterno;
			}
		}
		/* (non-Javadoc)
		 * @see java.util.Map#get(java.lang.Object)
		 */
		/**
		 * Restituisce le informazioni di linearizzazione della transizione
		 * specificata dalla chiave.
		 * 
		 * @param nomeTrans la stringa rappresentante il nome di una transizione
		 * @return le informazioni relative alla transizione specificata
		 */
		private InfoTrans get(String nomeTrans) {
			return (InfoTrans)super.get(nomeTrans);
		}

		/* (non-Javadoc)
		 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
		 */
		/**
		 * Imposta le informazioni di linearizzazione, della transizione specificata come chiave,
		 * all'oggetto infoTrans specificato.
		 * 
		 * @param nomeTrans la stringa rappresentante il nome di una transizione
		 * @param infoTrans le informazioni da associare alla transizione
		 */
		private InfoTrans put(String nomeTrans, InfoTrans infoTrans) {
			return (InfoTrans)super.put(nomeTrans, infoTrans);
		}

		/**
		 * Aggiorna le informazioni sulla transizione specificata, come appartenente
		 * alla traccia specificata, alla posizione specificata.
		 * 
		 * @param transCorr la transizione di cui si desiderano aggiornare le informazioni
		 * @param traccia la traccia lineare in cui appare la transizione
		 * @param posizione la posizione della transizione nella traccia
		 */
		private void aggiornaInfoTrans(ElementoMessaggio transCorr, Traccia traccia, int posizione){
			// reperimento informazioni per la transizione corrente
			InfoTrans info = (InfoTrans)get(transCorr.getName());
			if(info != null){// transizione già incontrata
				// si aggiorna l'informazione con la traccia corrente
				info.add(traccia,posizione);
				if(!info.isInterno()){
					if(transCorr.getSendReceive()==ElementoMessaggio.SEND){
						info.setSmProv(traccia.getSmLineare());
					}else{
						info.setSmDest(traccia.getSmLineare());
					}
				}
			}else{// nessuna informazione: transizione mai incontrata
				// si creano le informazioni necessarie e si aggiungono alla struttura
				boolean isInterno = transCorr.getSendReceive() == ElementoMessaggio.TAU;
				info = new InfoTrans(isInterno);
				if(isInterno){
					/* CONVENZIONE: se il messaggio scambiato è INTERNO (tau)
					 * impostiamo la SM di PROVENIENZA uguale a quella di DESTINAZIONE
					 * (smProv == sm Dest)
					 */
					info.setSmProv(traccia.getSmLineare());
					info.setSmDest(traccia.getSmLineare());
				}else if(transCorr.getSendReceive()==ElementoMessaggio.SEND){
					info.setSmProv(traccia.getSmLineare());
				}else{
					info.setSmDest(traccia.getSmLineare());
				}
				info.add(traccia,posizione);
				put(transCorr.getName(),info);
			}
		}
		/**
		 * Restituisce l'altra SM coinvolta dal messaggio con lo stesso nome
		 * della transizione specificata.
		 * Nota: ha senso se il messaggio NON è interno.
		 * 
		 * @param trans una transizione
		 * @return l'altra SM coinvolta dal messaggio con lo stesso nome della transizione
		 */
		private SMLineare getAltraSM(ElementoMessaggio trans){
			return trans.getSendReceive()==ElementoMessaggio.SEND?
				get(trans.getName()).getSmDest():
				get(trans.getName()).getSmProv();
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			String out = "\n\nLinearizza: LOCALIZZAZIONE TRANSIZIONI in tracce lineari";
			out += "\n\ntrovate "+size()+" transizioni:";
			Object[] chiavi = keySet().toArray();
			InfoTrans infoTrans;
			Iterator scorriTracce;
			InfoTracciaTrans infoTraccia;
			for(int i=0;i<chiavi.length;i++){
				infoTrans = (InfoTrans)get(chiavi[i]);
				out += "\n\n\ttransizione "+chiavi[i]+" ("+infoTrans.getSmProv().getNomeSM()+" -> "+infoTrans.getSmDest().getNomeSM()+") presente in "+infoTrans.size()+" tracce lineari:";
				String nomeSM = "";
				scorriTracce = infoTrans.iterator();
				while(scorriTracce.hasNext()){
					infoTraccia = (InfoTracciaTrans)scorriTracce.next();
					if(nomeSM != infoTraccia.getTraccia().getNomeProcesso()){
						nomeSM = infoTraccia.getTraccia().getNomeProcesso();
						out += "\n\tnella SM "+nomeSM;
					}
					out += "\n\t\tin "+infoTraccia.traccia.toString(infoTraccia.getPosizione());
				}
			}
			return out;
		}
	}
	/**
	 * Struttura atta a contenere le informazioni sugli stati visitati durante
	 * la linearizzazione.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class MappaStati extends TreeMap {
		/**
		 * Stabilisce se questa mappa contiene lo stato passato come chiave.
		 * 
		 * @param chiave uno stato in una SM dell'architettura
		 * @return <code>true</code> se se questa mappa contiene lo stato specificato, <code>false</code> altrimenti
		 */
		private boolean contieneChiave(ElementoStato chiave) {
			return super.containsKey(new Long(chiave.getId()));
		}
		/**
		 * Aggiunge a quest'oggetto l'informazione di default per lo stato specificato.
		 * Utilizzare questo metodo durante la linearizzazione a partire da uno stato
		 * mai incontrato prima.
		 * 
		 * @param stato uno stato in una SM dell'architettura
		 * @return l'informazione presente prima di questa chiamata (null)
		 */
		private Object put(ElementoStato stato) {
			return super.put(new Long(stato.getId()), new InfoStato(stato));
		}
		/**
		 * Aggiunge a quest'oggetto l'informazione specificata.
		 * Utilizzare questo metodo durante la linearizzazione quando si è
		 * stabilita la natura dello stato a cui l'informazione si riferisce.
		 * 
		 * @param infoStato informazione contenente l'identificatore di uno stato usato come chiave
		 * @return l'informazione presente prima di questa chiamata
		 */
		private Object put(InfoStato infoStato){
			return super.put(new Long(infoStato.stato.getId()), infoStato);
		}
		/**
		 * Restituisce l'informazione associata allo stato specificato.
		 * 
		 * @param stato uno stato di una SM dell'architettura
		 * @return l'informazione associata allo stato, se presente
		 */
		private InfoStato get(ElementoStato stato){
			return (InfoStato)super.get(new Long(stato.getId()));
		}
	}
	/**
	 * Struttura che associa ad uno stato le informazioni utilizzate durante la linearizzazione.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class InfoStato {
		/**
		 * Uno stato
		 */
		private ElementoStato stato;
		/**
		 * Flag indicante la condizione di indeterminatezza dello stato associato.
		 * Uno stato è indeterminato finché non si sia stabilito se è HoaL oppure no.
		 */
		private boolean isIndeterm = true;
		/**
		 * Flag indicante che lo stato associato è un <i>Head of a Loop</i>.
		 * L'informazione è sensata se lo stato non è indeterminato.
		 * Uno stato s è HoaL se<br>
		 * è possibile raggiungere s dallo stato iniziale della SM con un cammino a aciclico
		 * e da s è possibile tornarvi con un ciclo non contenente alcuna transizione in a.
		 */
		private boolean isHoaL;
		/**
		 * Le tracce (incomplete) generate durante la linearizzazione
		 * a partire da questo stato
		 */
		private Tracce tracce = new Tracce();
		/**
		 * Costruisce un nuovo oggetto InfoStato, il cui stato è inizialmente indeterminato.
		 * 
		 * @param stato uno stato
		 */
		private InfoStato(ElementoStato stato){
			this.stato = stato;
		}
	}
	/**
	 * Struttura utilizzata per la generazione di un SD in output al TeStor (caso di test).
	 * 
	 * @author Fabrizio Facchini
	 */
	private class TracciaSD {
		/**
		 * La parte di questa traccia da leggere
		 */
		private final TracciaTeStor supTrace;
		/**
		 * La parte di questa traccia letta
		 */
		private final TracciaTeStor supTraceLetta;
		/**
		 * Indica se questa traccia è vincolata
		 */
		private final boolean isVincolata;
		/**
		 * Lo stato corrente la cui transizione uscente (se esiste) è la prima in questa
		 * traccia non ancora letta, e la entrante (se esiste) è l'ultima letta.
		 */
		private ElementoStato statoCorr;
		/**
		 * Costruisce un nuovo oggetto TracciaSD sulla sup-trace specificata.
		 * Si specifica se la traccia creata debba essere vincolata.
		 * 
		 * @param supTrace una sup-trace
		 * @param isVincolata indica se la traccia creata debba essere vincolata
		 */
		private TracciaSD(TracciaTeStor supTrace, boolean isVincolata){
			this.supTrace = supTrace;
			this.supTraceLetta = new TracciaTeStor(supTrace.getSmLineare(),supTrace.getStatoIniziale());
			this.isVincolata = isVincolata;
			this.statoCorr = supTrace.getStatoIniziale();
		}
		/**
		 * Costruisce un nuovo oggetto TracciaSD.
		 * Per uso interno: utilizzata per copia!
		 * 
		 * @param supTrace
		 * @param supTraceLetta
		 * @param isVincolata
		 */
		private TracciaSD(TracciaTeStor supTrace, TracciaTeStor supTraceLetta, boolean isVincolata){
			this.supTrace = supTrace;
			this.supTraceLetta = supTraceLetta;
			this.isVincolata = isVincolata;
			this.statoCorr = supTrace.getStatoIniziale();
		}
		/**
		 * Restituisce la sotto-traccia <i>da leggere</i> dall'inizio fino
		 * alla transizione specificata (se esiste).
		 * 
		 * @param trans una transizione nella stessa SM di questa traccia
		 * @return la sotto-traccia di cui sopra se la transizione esiste, <code>null</code> altrimenti
		 */
		private Traccia sottoTraccia(ElementoMessaggio trans){
			// la traccia da restituire
			Traccia traccia = new Traccia(supTrace.getSmLineare());
			
			Iterator scorriSupTrace = supTrace.iterator();
			// la trans. corrente
			ElementoMessaggio transCorr;
			while(scorriSupTrace.hasNext()){
				transCorr = (ElementoMessaggio)scorriSupTrace.next();
				traccia.add(transCorr);
				if(transCorr.getName().equals(trans.getName())){
					return traccia;
				}
			}
			return null;
		}
		/**
		 * Restituisce la sup-trace associata.
		 * 
		 * @return supTrace.
		 */
		/*private TracciaTeStor getSupTrace() {
			return supTrace;
		}*/
		/**
		 * Restituisce una copia di quest'oggetto.
		 * Non sono clonate le transizioni della sup-trace associata in nessun caso,
		 * né la transizione corrente e neppure lo stato corrente.
		 * 
		 * @return una copia di quest'oggetto
		 */
		private TracciaSD copia() {
			TracciaSD copia = new TracciaSD(supTrace.copia(),supTraceLetta.copia(),isVincolata);
			return copia;
		}
		/**
		 * @return il campo isVincolata
		 */
		private boolean isVincolata() {
			return isVincolata;
		}
		/**
		 * @return il campo statoCorr.
		 */
		private ElementoStato getStatoCorr() {
			return statoCorr;
		}
		/**
		 * Restituisce il nome del processo per questa traccia.
		 * 
		 * @return il nome del processo per questa traccia
		 */
		private String getNomeProcesso(){
			return supTrace.getNomeProcesso();
		}
		/**
		 * <i>Legge</i> (e consuma) la prossima transizione in questa traccia e la restituisce.
		 * Da utilizzarsi per simulare l'esecuzione di questa transizione nella sua SM
		 * cosicché lo stato corrente diventi il secondo estremo della transizione.
		 * 
		 * @return la transizione correntemente letta in questa traccia
		 */
		private ElementoMessaggio leggiTrans(){
			ElementoMessaggio trans = (ElementoMessaggio)supTrace.removeFirst();
			supTrace.is = (ElementoStato)trans.getElementTo();
			supTraceLetta.add(trans);
			statoCorr = (ElementoStato)trans.getElementTo();
			return trans;
		}
		/**
		 * <i>Individua</i> la prossima transizione non letta in questa traccia.
		 * 
		 * @return la prossima transizione non letta
		 */
		private ElementoMessaggio prossimaTrans(){
			return supTrace.getTransIniziale();
		}
		/**
		 * Restituisce la dimensione di questa traccia: la parte letta + la parte da leggere
		 * @return la dimensione di questa traccia
		 */
		private int size(){
			return supTrace.size()+supTraceLetta.size();
		}
		/**
		 * Restituisce una stringa della sup-trace che evidenzia lo stato corrente
		 * e tutte le transizioni già lette, cioè prodotte in qualche caso di test
		 */
		public String toString(){
			String temp = supTrace.getNomeProcesso()+" - ID = "+supTrace.getIdTraccia()+":  ";
			Iterator scorri = supTraceLetta.iterator();
			while(scorri.hasNext()){
				ElementoMessaggio transCorr = (ElementoMessaggio)scorri.next();
				temp += "["+transCorr.getElementFrom().getName()+"] ¶"+transCorr.getName()+"¶ ";
			}
			temp += "<["+supTraceLetta.getStatoFinale().getName()+"]>";
			scorri = supTrace.iterator();
			while(scorri.hasNext()){
				ElementoMessaggio transCorr = (ElementoMessaggio)scorri.next();
				temp += " "+transCorr.getName()+" ["+transCorr.getElementTo().getName()+"]";
			}
			return temp;
		}
	}
	/**
	 * Associazione tra una mappa indicante l'avanzamento dell'integrazione
	 * di una certa combinazione di sup-trace e il caso di test finora generato.
	 * La mappa associa i nomi delle SM dell'architettura
	 * (<i>chiavi</i>) ad oggetti di tipo {@link TracciaSD} (<i>valori</i>).
	 * 
	 * @author Fabrizio Facchini
	 */
	private class TracceSD extends TreeMap{
		/**
		 * Il caso di test generato per questa combinazione di sup-trace
		 */
		private CasoDiTest casoDiTest;
		/**
		 * Mappa contenente le transizioni da leggere,
		 * ma non ancora prodotte nei casi di test.
		 * Utilizzata per riconoscere ricorsioni cicliche
		 * nella integrazione delle sup-trace.
		 * Uso: chiave = un oggetto {@link Long} creato sul valore getId() della trans.,
		 * valore = la trans. stessa.
		 */
		private TreeMap transDaLeggere = new TreeMap();
		/**
		 * Costruisce un nuovo oggetto TracceSD.
		 * Il caso di test associato è impostato sugli stati iniziali delle SM.
		 * 
		 * @see CasoDiTest#CasoDiTest(String, IPlugData, SMLinearizzate)
		 */
		private TracceSD(SMLinearizzate outputLinea) {
			super();
			this.casoDiTest = new CasoDiTest("",plugData,outputLinea);
		}
		/**
		 * @return il campo casoDiTest.
		 */
		private CasoDiTest getCasoDiTest() {
			return casoDiTest;
		}
		/**
		 * Imposta il valore del campo specificato.
		 * 
		 * @param casoDiTest il valore per casoDiTest da assegnare.
		 */
		private void setCasoDiTest(CasoDiTest casoDiTest) {
			this.casoDiTest = casoDiTest;
		}
		/**
		 * Sostituisce la traccia specificata a quella presente in quest'oggetto,
		 * relativamente alla stessa SM; aggiorna il caso di test associato
		 * relativamente allo stato iniziale e raggiunto dalla stessa SM.
		 * Utilizzata durante la generazione dei casi di test parziali per
		 * il messaggio le cui SM di provenienza e destinazione sono rappresentate
		 * dalle due stringhe specificate.
		 * 
		 * @param traccia la traccia da sostituire in quest'oggetto
		 * @param smProvMess il nome della SM di provenienza del messaggio corrente
		 * @param smDestMess il nome della SM di destinazione del messaggio corrente
		 */
		private void sotituisciTraccia(TracciaTeStor traccia, String smProvMess, String smDestMess){
			// il nome della SM a cui appartiene la traccia da sostituire
			String nomeSM = traccia.getSmLineare().getNomeSM();
			// poniamo una nuova tracciaSD al posto giusto; se la SM è coinvolta dal mess. allora è vincolata
			put(nomeSM, new TracciaSD(traccia, traccia.getNomeProcesso()==smProvMess || traccia.getNomeProcesso()==smDestMess));
			// poniamo lo stato iniziale della traccia tra gli stati inizali del caso di test associato
			getCasoDiTest().getStatiIniziali().put(nomeSM, traccia.getStatoIniziale());
			// poniamo lo stato iniziale della traccia tra gli stati finali del caso di test associato
			getCasoDiTest().getStatiRaggiunti().put(nomeSM,traccia.getStatoIniziale());
		}
		/**
		 * Restituisce una copia di quest'oggetto.
		 * 
		 * @return una copia di quest'oggetto
		 * @see AlgoritmoTeStor.CasoDiTest#copia()
		 * @see TracciaSD#copia()
		 */
		private TracceSD copia(){
			TracceSD copia = new TracceSD(getCasoDiTest().outputLinea);
			copia.setCasoDiTest(getCasoDiTest().copia());
			// copiamo ogni TracciaSD e la mettiamo al posto giusto
			Iterator scorriChiavi = keySet().iterator();
			while(scorriChiavi.hasNext()){
				String chiaveCorr = (String)scorriChiavi.next();
				copia.put(chiaveCorr,((TracciaSD)get(chiaveCorr)).copia());
			}
			copia.transDaLeggere = copiaTreeMap(this.transDaLeggere);

			return copia;
		}
		/**
		 * Realizza l'integrazione di queste sup-trace a partire da quella specificata
		 * dalla SM passata come nome, per il numero di transizioni specificate.
		 * 
		 * @param sm il nome di una SM dell'architettura
		 * @param numLetture il numero di transizioni che si vogliono integrare
		 * @param outputLinea l'output della linearizzazione
		 * @return la lista di {@link AlgoritmoTeStor.TracceSD} prodotte dall'integrazione
		 * @throws TestCaseGenerationExceptionsList se l'integrazione non è possibile per qualche motivo
		 */
		private LinkedList integra(String sm, int numLetture, SMLinearizzate outputLinea) throws TestCaseGenerationExceptionsList{
			
			// lista eccezioni eventualmente sollevate
			TestCaseGenerationExceptionsList listaEccezioni = new TestCaseGenerationExceptionsList();
			
			// i casi di test (lista di TracceSD) da restituire
			LinkedList listaCasiDiTest = new LinkedList();
			// la tracciaSD corrente
			TracciaSD tracciaSD = (TracciaSD)get(sm);
			// la sup-trace corrente
			System.out.println("\nIntegrazione traccia: "+tracciaSD+", numero di letture da effettuare: "+numLetture);// DEBUG
			
			// condizione di TERMINAZIONE *********
			if(numLetture <= 0){
				listaCasiDiTest.add(this);
				System.out.println("\t\tTraccia terminata: RETURN\n");// DEBUG
				return listaCasiDiTest;
			}
			//*************************************
			
			// trans. corrente
			ElementoMessaggio transCorr = tracciaSD.prossimaTrans();
			System.out.println("\n\tMessaggio corrente: "+transCorr.getName()+" (sup-trace "+(tracciaSD.isVincolata()?"VINCOLATA":"NON vincolata")+": "+tracciaSD+")");// DEBUG
			
			// MESSAGGIO INTERNO (tipo TAU)
			if(transCorr.getSendReceive() == ElementoMessaggio.TAU){// mess. interno
				System.out.println("\n\t\tIl messaggio corrente è INTERNO.");// DEBUG
				// consumiamo la trans.
				tracciaSD.leggiTrans();//**********************
				
				// PRODUZIONE NUOVO LINK nell'outSD
				// aggiungiamo il link
				getCasoDiTest().aggiungiMessaggio(transCorr);
				SMLineare smProv = outputLinea.getTutteLeTrans().get(transCorr.getName()).getSmProv();// DEBUG
				System.out.println("\n\t\tPRODOTTO NUOVO MESSAGGIO: ["+smProv.getNomeSM()+"] "+transCorr.getName()+" ["+smProv.getNomeSM()+"]");// DEBUG
				// impostiamo gli stati finali raggiunti
				getCasoDiTest().getStatiRaggiunti().put(sm,transCorr.getElementTo());
				// decrementiamo numLetture
				numLetture--;
				System.out.println("\nSITUAZIONE CORRENTE:\n"+this);// DEBUG
				
				try {
					// ricorsione
					return integra(sm,numLetture, outputLinea);
				} catch (TestCaseGenerationExceptionsList e) {
					// aggiorniamo le eventuali eccezioni di looping non concluse
					// specificando solo la sotto-traccia d'interesse
					e.aggiornaLooping(tracciaSD, numLetture);
					listaEccezioni = e;
					throw listaEccezioni;
				}
			}
			
			// messaggio NON INTERNO ********

			// RICONOSCIMENTO LOOP NELLA RICORSIONE
			/* La transizione corrente, se già presente nella mappa delle transizioni da leggere
			 * (put(...) != null), indica che già si era chiamata la ricorsione su questa traccia,
			 * e quindi si è creato un loop nel tentativo di leggerla.
			 */
			if(transDaLeggere.put(new Long(transCorr.getId()),transCorr) != null) {
				// LOOP!!!
				System.out.println("\n\t\tTransizione già incontrata: LOOP.\nIMPOSSIBILE INTEGRARE LA SUP-TRACE\n");// DEBUG
				// creiamo e aggiungiamo una nuova eccezione di looping
				listaEccezioni.add(new LoopingTransitionsException(tracciaSD,numLetture,transCorr));
				throw listaEccezioni;
			}
			
			// INDIVIDUAZIONE EVENTUALI INTEGRAZIONI
			// l'altra SM coinvolta dal messaggio
			SMLineare altraSM = outputLinea.getTutteLeTrans().getAltraSM(transCorr);
			// l'altra tracciaSD coinvolta
			TracciaSD altraTracciaSD = (TracciaSD)get(altraSM.getNomeSM());
			// l'altra traccia coinvolta
			//Traccia altraTraccia = altraTracciaSD.supTrace;//*********************
			System.out.println("\n\t\tCoinvolta la sup-trace "+(altraTracciaSD.isVincolata()?"VINCOLATA":"NON vincolata")+": "+altraTracciaSD);// DEBUG
			// cerchiamo la trans. nell'altra sup-trace
			Traccia sottoTraccia = altraTracciaSD.sottoTraccia(transCorr);
			// controlliamo il risultato...
			if(sottoTraccia == null){// la traccia è vuota o non contiene la trans.
				System.out.println("\t\tLa sup-trace NON contiene il messaggio.");// DEBUG
				// tutte le tracce valide vanno bene purchè integrabili
			}else{// contiene la trans.: è quella giusta?
				System.out.println("\t\tLa SM contiene il messaggio nella sotto-traccia: "+sottoTraccia);// DEBUG
				
				if(sottoTraccia.size() == 1){// CASO BASE ******
					ElementoMessaggio altraTransCorr = sottoTraccia.getTransIniziale();
					// consumiamo le due trans.
					tracciaSD.leggiTrans();//************
					altraTracciaSD.leggiTrans();//**********
					
					// PRODUZIONE NUOVO LINK nell'outSD
					// aggiungiamo il link
					getCasoDiTest().aggiungiMessaggio(transCorr);
					SMLineare smProv = outputLinea.getTutteLeTrans().get(transCorr.getName()).getSmProv();// DEBUG
					SMLineare smDest = outputLinea.getTutteLeTrans().get(transCorr.getName()).getSmDest();// DEBUG
					System.out.println("\n\t\tPRODOTTO NUOVO MESSAGGIO: ["+smProv.getNomeSM()+"] "+transCorr.getName()+" ["+smDest.getNomeSM()+"]");// DEBUG
					// impostiamo gli stati finali raggiunti
					getCasoDiTest().getStatiRaggiunti().put(sm,transCorr.getElementTo());
					getCasoDiTest().getStatiRaggiunti().put(altraSM.getNomeSM(),altraTransCorr.getElementTo());
					// rimuoviamo le due transizioni perché lette (generate)
					transDaLeggere.remove(new Long(transCorr.getId()));
					transDaLeggere.remove(new Long(altraTransCorr.getId()));
					// decrementiamo numLetture
					numLetture--;
					System.out.println("\nSITUAZIONE CORRENTE:\n"+this);// DEBUG
					
					try {
						// RICORSIONE sulla SM sm
						return integra(sm, numLetture, outputLinea);
					} catch (TestCaseGenerationExceptionsList e) {
						e.aggiornaLooping(tracciaSD, numLetture);
						listaEccezioni = e;
						throw listaEccezioni;
					}
				}	
			}
			// cerchiamo le tracce valide, in altraSM, per transCorr e lo stato corrente
			Tracce tracceValidePerTrans = new Tracce();
			try {
				tracceValidePerTrans = altraSM.getTracceValide(transCorr,altraTracciaSD.getStatoCorr());
			} catch (NoValidTracesFoundException e2) {
				listaEccezioni.add(e2);
				throw listaEccezioni;
			}
			// num. tracce valide
			int numTV = tracceValidePerTrans.size();
			System.out.println("\t\tTrovata/e "+tracceValidePerTrans.size()+" traccia/e valida/e.");// DEBUG
			
			if(!altraTracciaSD.isVincolata()){// l'altra traccia non è vincolata
				System.out.println("\t\tLa sup-trace è NON vincolata: tutte le tracce valide possono essere integrate");// DEBUG
			}else{
				System.out.println("\t\tLa sup-trace è VINCOLATA: le tracce valide devono essere integrabili.");// DEBUG
			}
			
			// INTEGRAZIONE
			Iterator scorriTracceValide = tracceValidePerTrans.iterator();
			// contatore
			int i = 0;
			// contatore tracce integrazione
			int numItegr = numTV;
			/* Per ognuna delle tracce valide, stabiliamo l'integrabilità (a seconda che
			 * l'altra traccia sia vincolata o meno), realizziamo la traccia da integrare
			 * ed eseguiamo la ricorsione.
			 */
			while(scorriTracceValide.hasNext()){
				Traccia tracciaValidaCorr = (Traccia)scorriTracceValide.next();
				i++;
				// numero di letture da effettuare sulla ricorsione
				int numLettureRic = tracciaValidaCorr.size();
				// indica che tracciaValidaCorr è integrabile
				boolean isIntegrabile = true;
				// analisi candidata
				if(!altraTracciaSD.supTrace.isEmpty()){// altraTraccia è NON VUOTA
					// dobbiamo costruire la corrisp. traccia integrazione
					// indica che la trans. è presente in altraTraccia
					boolean transPresente = sottoTraccia != null;
					/* La traccia di riferimento è quella la cui ultima trans. è necessaria.
					 * Se altraTraccia contiene la transCorr allora non dobbiamo superarla,
					 * e quindi la traccia di riferimento è sottoTraccia, altrimenti è altraTraccia
					 */
					Traccia altraTraccia = altraTracciaSD.supTrace;//**************
					Traccia tracciaRif = transPresente? sottoTraccia: altraTraccia;
					
					System.out.println("\t\t\ttraccia valida "+i+" di "+numTV+": "+tracciaValidaCorr);// DEBUG
					// analizziamo la candidata rispetto a tracciaRif
					Traccia[] comRest = tracciaRif.comuneRestante(tracciaValidaCorr);
					// sotto-traccia comune alle due
					Traccia comune = comRest[0];
					// sotto-tracia restante della candidata
					Traccia restante = comRest[1];
					// sotto-traccia restante di tracciaRif
					Traccia restRif = comRest[2];
					if(transPresente){// trans. presente
						/* L'integrazione o deve avere una parte comune più breve di sottoTraccia
						 * e terminare ciclicamente, oppure deve coincidere con essa
						 */
						if((comune.size() < tracciaRif.size() && !restante.isCiclo())
								|| !restante.isEmpty()){
							System.out.println("\t\t\tLa traccia NON è integrabile\n");// DEBUG
							isIntegrabile = false;
						}
					}else{// trans. NON presente
						/* L'integrazione, non essendo presente la trans., può avere una
						 * parte comune lunga a piacere, purché la parte restante sia ciclica
						 */
						if(!restante.isCiclo()){
							System.out.println("\t\t\tLa traccia NON è integrabile\n");// DEBUG
							isIntegrabile = false;
						}
					}
					
					/* Se tracciaValidaCorr è integrabile dobbiamo creare la nuova traccia
					 * contenente l'integrazione.
					 */
					if(isIntegrabile){
						Traccia nuova = new Traccia(altraSM);
						// la nuova traccia è inizialmente uguale a parte comune + integrazione
						nuova.aggiungiInCoda(tracciaValidaCorr);
						// quindi aggiungiamo il resto di altraTraccia
						ListIterator scorriRestAltraTr = altraTraccia.listIterator(comune.size());
						while(scorriRestAltraTr.hasNext()){
							nuova.add((ElementoMessaggio)scorriRestAltraTr.next());
						}
						// riassegnamo per coerenze dei nomi
						tracciaValidaCorr = nuova;
						System.out.println("\t\t\tLa traccia è INTEGRABILE. Costruita traccia da integrare.");// DEBUG
					}else{
						// decrementiamo il num. tracce integrazione
						numItegr--;
					}
				}
				
				if(isIntegrabile){
					System.out.println("\t\t\tIntegrazione candidata: "+tracciaValidaCorr);// DEBUG
					// INTEGRAZIONE traccia...
					TracciaSD tracciaSDdaIntegrare = new TracciaSD(new TracciaTeStor(tracciaValidaCorr), altraTracciaSD.isVincolata);
					TracceSD tracceSDric = copia();
					// sostituiamo la traccia con quella con l'integrazione
					tracceSDric.put(altraSM.getNomeSM(),tracciaSDdaIntegrare);
					// RICORSIONE sulla sotto-traccia...
					System.out.println("\t\t\tRICORSIONE su traccia valida candidata:\n");// DEBUG
					LinkedList listaTracceSD = new LinkedList();
					try {
						listaTracceSD = tracceSDric.integra(altraSM.getNomeSM(),numLettureRic, outputLinea);
						// contatore delle tracceSD
						int contaTracceSD = listaTracceSD.size();
						// scorriamo le tracceSD ritornate...
						Iterator scorri = listaTracceSD.iterator();
						while(scorri.hasNext()){
							TracceSD tracceSDrit = (TracceSD)scorri.next();
							try {
								// ricorriamo sulle trans. eventualmente rimanenti
								listaCasiDiTest.addAll(tracceSDrit.integra(sm,numLetture-1, outputLinea));
							} catch (TestCaseGenerationExceptionsList e) {
								// decrementiamo il contatore
								contaTracceSD--;
								listaEccezioni.addAll(e);
							}
						}
						if(contaTracceSD <= 0){
							// nessun oggetto tracceSD integrato per tracciaSDdaIntegrare 
							numItegr--;
						}
					} catch (TestCaseGenerationExceptionsList e1) {
						numItegr--;
						e1.aggiornaLooping(tracciaSD, numLetture);
						listaEccezioni.addAll(e1);
					}
				}
			}
			if(numItegr <= 0){
				System.out.println("\n\t\tNessuna traccia valida risulta integrabile.\nIMPOSSIBILE INTEGRARE LA SUP-TRACE\n");// DEBUG
				listaEccezioni.add(new NoIntegrationsFoundException(tracciaSD.supTrace.primeNtrans(numLetture)));
				throw listaEccezioni;
			}
			
			return listaCasiDiTest;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString(){
			String stringa = "";
			Iterator scorriSM = values().iterator();
			while(scorriSM.hasNext()){
				stringa += "\n"+(TracciaSD)scorriSM.next();
			}
			stringa += "\n\ncaso di test generato:\n"+getCasoDiTest()+"\n";
			return stringa;
		}
	}
	/**
	 * Associazione tra un caso di test ({@link SequenceElement})
	 * e, per ciascuna SM dell'architettura, lo stato iniziale e quello raggiunto.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class CasoDiTest{
		/**
		 * Mappa che associa al nome di ogni SM (chiave) lo stato da cui essa partiva (valore)
		 * nel caso di test associato.
		 */
		private TreeMap statiIniziali = new TreeMap();
		/**
		 * Mappa che associa al nome di ogni SM (chiave) lo stato che essa ha raggiunto (valore)
		 * nel caso di test associato.
		 */
		private TreeMap statiRaggiunti = new TreeMap();
		/**
		 * Un caso di test. Gli stati iniziali e raggiunti dal caso di test
		 * sono contenuti nelle due mappe associate.
		 */
		private SequenceElement sequenceElement;
		/**
		 * L'indice del prossimo istante temporale libero nel caso di test associato
		 */
		private long tempo = 0;
		/**
		 * L'output della linearizzazione
		 */
		private SMLinearizzate outputLinea;
		/**
		 * Costruisce un nuovo oggetto CasoDiTest.
		 * Crea un SequenceElement vuoto e gli stati iniziali e finali
		 * sono costituiti dallo stato iniziale di ciascuna SM.
		 * 
		 * @param nomeCasoDiTest il nome da dare al caso di test
		 * @param plugData contenitore dei dati
		 * @param outputLinea l'output della linearizzazione
		 */
		private CasoDiTest(String nomeCasoDiTest, IPlugData plugData, SMLinearizzate outputLinea) {
			super();
			this.sequenceElement = new SequenceElement(nomeCasoDiTest,plugData);
			this.outputLinea = outputLinea;
			// aggiunge al caso di test vuoto i processi dell'architettura
			impostaCasoDaSM(outputLinea);
			// itera tra le SM alla ricerca dello stato iniziale
			for(int i=0; i<outputLinea.getSmLinearizzate().size(); i++){
				SMLineare sml = (SMLineare)outputLinea.getSmLinearizzate().get(i);
				this.statiIniziali.put(sml.getNomeSM(),sml.getStartState());
				this.statiRaggiunti.put(sml.getNomeSM(),sml.getStartState());
			}
		}
		/**
		 * Aggiunge al caso di test i processi dell'architettura (come oggetti di tipo
		 * {@link plugin.sequencediagram.data.ElementoClasse}), ricavati dal parametro specificato.
		 * 
		 * @param outputLinea l'output della linearizzazione delle SM
		 */
		private void impostaCasoDaSM(SMLinearizzate outputLinea){
			// ciclo preso a prestito dal costruttore principale di plugin.sequencediagram.SequenceEditor
			for(int i=0; i<outputLinea.getSmLinearizzate().size(); i++){
				sequenceElement.getListaClasse().addElement(
					new ElementoClasse(
						i*SequenceEditor.STEP_CLASSE+SequenceEditor.MARGINE_CLASSE,
						SequenceEditor.MARGINE_CLASSE,
						((SMLineare)outputLinea.getSmLinearizzate().get(i)).getNomeSM(),
						null
					)
				);
			}
		}
		/**
		 * Aggiunge il messaggio specificato al caso di test in quest'oggetto.
		 * 
		 * @param messaggio il messaggio da aggiungere
		 * @return il messaggio aggiunto
		 */
		private ElementoSeqLink aggiungiMessaggio(ElementoCanaleMessaggio messaggio){
			// SM di provenienza e destinazione del messaggio
			SMLineare smProv = outputLinea.getTutteLeTrans().get(messaggio.getName()).getSmProv();
			SMLineare smDest = outputLinea.getTutteLeTrans().get(messaggio.getName()).getSmDest();
			// il SequenceElement associato
			SequenceElement casoDiTest = getSequenceElement();
			casoDiTest.getListaTime().addLast(tempo);
			tempo++;
			ElementoSeqLink nuovoMessaggio = new ElementoSeqLink(
					messaggio.getName(),
					casoDiTest.getListaClasse().getElement(smProv.getNomeSM()),
					casoDiTest.getListaClasse().getElement(smDest.getNomeSM()),
					casoDiTest.getListaTime().getLast(),
					casoDiTest.getListaTime().getLast(),
					ElementoSeqLink.SYNCHRONOUS,
					messaggio.getName(),
					null);
			casoDiTest.getListaSeqLink().addElement(
				//new ElementoSeqLink(messaggio.getName(),casoDiTest.getListaClasse().getElement(smProv.getNomeSM()),casoDiTest.getListaClasse().getElement(smDest.getNomeSM()),casoDiTest.getListaTime().getLast(),casoDiTest.getListaTime().getLast(),ElementoSeqLink.SYNCHRONOUS,messaggio.getName(),null)
				nuovoMessaggio
			);
			return nuovoMessaggio;
		}
		/**
		 * @return il campo sequenceElement.
		 */
		private SequenceElement getSequenceElement() {
			return sequenceElement;
		}
		/**
		 * @return il campo statiRaggiunti.
		 */
		private TreeMap getStatiRaggiunti() {
			return statiRaggiunti;
		}
		/**
		 * @return il campo statiIniziali.
		 */
		private TreeMap getStatiIniziali() {
			return statiIniziali;
		}
		/**
		 * Imposta il valore del campo specificato.
		 * 
		 * @param casoDiTest il valore per sequenceElement da assegnare.
		 */
		private void setSequenceElement(SequenceElement casoDiTest) {
			this.sequenceElement = casoDiTest;
		}
		/**
		 * Imposta il valore del campo specificato.
		 * 
		 * @param statiRaggiunti il valore per statiRaggiunti da assegnare.
		 */
		private void setStatiRaggiunti(TreeMap statiRaggiunti) {
			this.statiRaggiunti = statiRaggiunti;
		}
		/**
		 * Imposta il valore del campo specificato.
		 * 
		 * @param statiIniziali il valore per statiIniziali da assegnare.
		 */
		private void setStatiIniziali(TreeMap statiIniziali) {
			this.statiIniziali = statiIniziali;
		}
		/**
		 * Accoda al caso di test associato la lista messaggi identificata dall'iteratore specificato.
		 * 
		 * @param scorriMessaggi un iteratore di elementi di tipo {@link ElementoCanaleMessaggio}
		 */
		private void aggiungiMessaggi(Iterator scorriMessaggi){
			while(scorriMessaggi.hasNext()){
				aggiungiMessaggio((ElementoCanaleMessaggio)scorriMessaggi.next());
			}
		}
		/**
		 * Restituisce una copia di quest'oggetto.
		 * Non vengono clonati i messaggi nel {@link SequenceElement} associato.
		 * 
		 * @return una copia di quest'oggetto
		 * @see AlgoritmoTeStor#copiaTreeMap(TreeMap)
		 */
		private CasoDiTest copia(){
			CasoDiTest copia = new CasoDiTest(getSequenceElement().getName(),plugData,outputLinea);
			copia.setStatiIniziali(copiaTreeMap(getStatiIniziali()));
			copia.setStatiRaggiunti(copiaTreeMap(getStatiRaggiunti()));
			copia.aggiungiMessaggi(getSequenceElement().getListaSeqLink().iteratorTemporal());
			copia.tempo = tempo;
			return copia;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			String caso = "";
			if(getSequenceElement().getListaSeqLink().size()<=0){
				caso = "\t<vuoto>";
			}else{
				Iterator scorriCaso = getSequenceElement().getListaSeqLink().iteratorTemporal();
				while(scorriCaso.hasNext()){
					ElementoSeqLink mess = (ElementoSeqLink)scorriCaso.next();
					caso += "\n\t["+mess.getElementFrom().getName()+"] "+mess.getName()+" ["+mess.getElementTo().getName()+"]";
				}
			}
			return caso;
		}
	}
	/**
	 * Una lista di eccezioni. Classe base per la gestione dettagliata degli errori
	 * incontrati durante l'esecuzione dell'algoritmo.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class ExceptionsList extends Exception{
		/**
		 * Messaggio atto a descrivere la situazione rappresentata da questa eccezione
		 */
		private final static String DESCRIZIONE_SITUAZIONE = "Durante l'esecuzione dell'algoritmo si sono verificati degli errori.";
		/**
		 * La lista contenente le eccezioni
		 */
		private final LinkedList lista = new LinkedList();
		/**
		 * Costruisce un nuovo oggetto ExceptionsList.
		 */
		protected ExceptionsList() {
			super(DESCRIZIONE_SITUAZIONE);
		}
		/**
		 * Costruisce un nuovo oggetto ExceptionsList.
		 * Viene specificato il messaggio da visualizzare.
		 * 
		 * @param message il messaggio da visualizzare
		 */
		protected ExceptionsList(String message) {
			super(message);
		}
		/**
		 * Aggiunge l'eccezione specificata alle altre.
		 * 
		 * @param e un'eccezione
		 */
		protected void add(Exception e){
			lista.add(e);
		}
		/**
		 * Aggiunge la lista di eccezioni specificata alle altre
		 * 
		 * @param el una lista di eccezioni
		 */
		protected void addAll(ExceptionsList el){
			lista.addAll(el.lista);
		}
		/**
		 * Restituisce l'eccezione all'indice specificato.
		 * 
		 * @param i l'indice di una eccezione
		 * @return l'eccezione all'indice specificato
		 */
		protected Exception get(int i){
			return (Exception)lista.get(i);
		}
		/**
		 * @return il numero delle eccezioni nella lista
		 */
		protected int getNumEccezioni() {
			return lista.size();
		}
	}
	/**
	 * Eccezione che rappresenta l'impossibilità di eseguire la linearizzazione
	 * sulle SM dell'architettura.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class LinearizationException extends Exception {
		/**
		 * Messaggio atto a descrivere la situazione rappresentata da questa eccezione
		 */
		private final static String DESCRIZIONE_SITUAZIONE = "Durante la linearizzazione delle SM si è verificato un errore.";
		/**
		 * Costruisce un nuovo oggetto LinearizationException.
		 * 
		 * @param message il messaggio d'errore che descrive la situazione
		 */
		private LinearizationException(String message) {
			super(message);
		}
		/**
		 * Costruisce un nuovo oggetto LinearizationException.
		 */
		private LinearizationException() {
			super(DESCRIZIONE_SITUAZIONE);
		}
		/**
		 * Costruisce un nuovo oggetto LinearizationException.
		 * 
		 * @param message il messaggio d'errore che descrive la situazione
		 * @param cause la casusa dell'errore
		 */
		private LinearizationException(String message, Throwable cause) {
			super(message, cause);
		}
		/**
		 * Costruisce un nuovo oggetto LinearizationException.
		 * 
		 * @param cause la casusa dell'errore
		 */
		private LinearizationException(Throwable cause) {
			super(DESCRIZIONE_SITUAZIONE, cause);
		}
	}
	/**
	 * Eccezione che rappresenta la totale assenza di definizione di una qualche Thread
	 * in un processo dell'architettura.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class NoThreadDefinedException extends LinearizationException{
		/**
		 * Messaggio atto a descrivere la situazione rappresentata da questa eccezione
		 */
		private final static String DESCRIZIONE_SITUAZIONE = "Un processo dell'architettura non ha definito alcun thread.";
		/**
		 * Un processo dell'architettura
		 */
		protected ListaThread processo = null;
		/**
		 * Costruisce un nuovo oggetto NoThreadDefinedException.
		 * 
		 * @param message il messaggio d'errore che descrive la situazione
		 */
		private NoThreadDefinedException(String message) {
			super(message);
		}
		/**
		 * Costruisce un nuovo oggetto NoThreadDefinedException.
		 */
		private NoThreadDefinedException() {
			super(DESCRIZIONE_SITUAZIONE);
		}
		/**
		 * Costruisce un nuovo oggetto NoThreadDefinedException.
		 * Viene specificato il processo interessato dall'eccezione.
		 * 
		 * @param processo un processo dell'architettura
		 */
		private NoThreadDefinedException(ListaThread processo){
			super("Il processo "+processo.getNameProcesso()+" non ha definito alcun thread.");
			this.processo = processo;
		}
		/**
		 * @return il campo processo.
		 */
		protected ListaThread getProcesso() {
			return processo;
		}
	}
	/**
	 * Eccezione che rappresenta l'assenza di StartState in un Thread.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class StartStateNotDefinedException extends NoThreadDefinedException{
		/**
		 * Messaggio atto a descrivere la situazione rappresentata da questa eccezione
		 */
		private final static String DESCRIZIONE_SITUAZIONE = "Il thread 0 di un processo non ha definito il suo StartState";
		/**
		 * Costruisce un nuovo oggetto StartStateNotDefinedException.
		 * 
		 * @param message il messaggio d'errore che descrive la situazione
		 */
		private StartStateNotDefinedException(String message) {
			super(message);
		}
		/**
		 * Costruisce un nuovo oggetto StartStateNotDefinedException.
		 */
		private StartStateNotDefinedException() {
			super(DESCRIZIONE_SITUAZIONE);
		}
		/**
		 * Costruisce un nuovo oggetto StartStateNotDefinedException.
		 * Viene specificato un processo dell'architettura.
		 * 
		 * @param processo un processo dell'architettura
		 */
		private StartStateNotDefinedException(ListaThread processo){
			super("Il thread 0 ("+processo.get(0).getNomeThread()+") del processo "+processo.getNameProcesso()+" non ha definito il suo StartState.");
			this.processo = processo;
		}
}
	/**
	 * Lista per raccogliere tutte le eccezioni di linearizzazione.
	 * 
	 * @author Fabrizio Facchini
	 * @see LinearizationException
	 */
	private class LinearizationExceptionsList extends ExceptionsList {
		/**
		 * Messaggio atto a descrivere la situazione rappresentata da questa eccezione
		 */
		private final static String DESCRIZIONE_SITUAZIONE = "Durante la linearizzazione delle SM si sono verificati degli errori.";
		/**
		 * Costruisce un nuovo oggetto LinearizationExceptionsList.
		 */
		private LinearizationExceptionsList() {
			super(DESCRIZIONE_SITUAZIONE);
		}
	}
	/**
	 * Eccezione indicante uno stato, supposto appartenente ad una data SM,
	 * che in realtà non le appartiene.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class WrongStateException extends RuntimeException {
		/**
		 * Messaggio atto a descrivere la situazione rappresentata da questa eccezione
		 */
		private final static String DESCRIZIONE_SITUAZIONE = "Uno stato non appartiene alla StateMachine che si suppone le appartenga.";
		/**
		 * Uno stato in una SM
		 */
		private ElementoStato stato = null;
		/**
		 * Un thread in un qualche processo dell'architettura
		 */
		private ThreadElement sm = null;
		/**
		 * Costruisce un nuovo oggetto WrongStateException.
		 * Vengono specificati il messaggio da visualizzare, lo stato e la SM
		 * che hanno generato l'eccezione.
		 * 
		 * @param message il messaggio che descrive l'eccezione
		 * @param stato uno stato di una SM
		 * @param sm una SM (non contenente lo stato d'interesse)
		 */
		private WrongStateException(String message, ElementoStato stato, ThreadElement sm){
			this(message);
			this.stato = stato;
			this.sm = sm;
		}
		/**
		 * Costruisce un nuovo oggetto WrongStateException.
		 * Vengono specificati lo stato e la SM che hanno generato l'eccezione.
		 * 
		 * @param stato uno stato di una SM
		 * @param sm una SM (non contenente lo stato d'interesse)
		 */
		private WrongStateException(ElementoStato stato, ThreadElement sm){
			this("Lo stato "+stato.getName()+" non appartiene alla SM "+sm.getNomeThread()+".",
				stato, sm);
		}
		/**
		 * Costruisce un nuovo oggetto WrongStateException.
		 * Viene specificato il messaggio da visualizzare.
		 * 
		 * @param message il messaggio d'errore che descrive la situazione
		 */
		private WrongStateException(String message) {
			super(message);
		}
		/**
		 * Costruisce un nuovo oggetto WrongStateException.
		 */
		private WrongStateException() {
			super(DESCRIZIONE_SITUAZIONE);
		}
		/**
		 * @return il campo sm.
		 */
		private ThreadElement getSm() {
			return sm;
		}
		/**
		 * @return il campo stato.
		 */
		private ElementoStato getStato() {
			return stato;
		}
	}
	/**
	 * Eccezione indicante l'impossibilità di generare casi di test per
	 * un dato SD (SequenceElement) dell'architettura.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class TestCaseGenerationException extends Exception{
		/**
		 * Messaggio atto a descrivere la situazione rappresentata da questa eccezione
		 */
		private final static String DESCRIZIONE_SITUAZIONE ="Durante la generazione dei casi di test di un SD si è verificato un errore.";
		/**
		 * Il SD che ha generato l'eccezione
		 */
		protected SequenceElement sequenceElement = null;
		/**
		 * Costruisce un nuovo oggetto TestCaseGenerationException.
		 */
		private TestCaseGenerationException() {
			super(DESCRIZIONE_SITUAZIONE);
		}
		/**
		 * Costruisce un nuovo oggetto TestCaseGenerationException.
		 * Viene specificato il messaggio da visualizzare.
		 * 
		 * @param message il messaggio da visualizzare
		 */
		private TestCaseGenerationException(String message) {
			super(message);
		}
		/**
		 * Costruisce un nuovo oggetto TestCaseGenerationException.
		 * Viene specificato il SD che ha generato l'eccezione.
		 * 
		 * @param sequenceElement il SD che ha generato l'eccezione
		 */
		private TestCaseGenerationException(SequenceElement sequenceElement) {
			this("Durante la generazione dei casi di test il SD "+sequenceElement.getName()+" ha generato un errore.");
			this.sequenceElement = sequenceElement;
		}
		/**
		 * @return il campo sequenceElement.
		 */
		protected SequenceElement getSequenceElement() {
			return sequenceElement;
		}
	}
	/**
	 * Eccezione indicante l'impossibilità di generare tracce valide
	 * per una data transizione ed un dato stato di una SM dell'architettura.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class NoValidTracesFoundException extends TestCaseGenerationException{
		/**
		 * Messaggio atto a descrivere la situazione rappresentata da questa eccezione
		 */
		private final static String DESCRIZIONE_SITUAZIONE = "In una SM dell'architettura da uno stato è impossibile arrivare ad eseguire lo scambio di un messaggio.";
		/**
		 * Una SM dell'architettura
		 */
		private SMLineare sml = null;
		/**
		 * Un messaggio della SM
		 */
		private ElementoCanaleMessaggio messaggio = null;
		/**
		 * Uno stato della SM
		 */
		private ElementoStato stato = null;
		/**
		 * Costruisce un nuovo oggetto NoValidTracesFoundException.
		 */
		private NoValidTracesFoundException(){
			super(DESCRIZIONE_SITUAZIONE);
		}
		/**
		 * Costruisce un nuovo oggetto NoValidTracesFoundException.
		 * Viene specificato il messaggio da visualizzare.
		 * 
		 * @param message il messaggio da visualizzare
		 */
		private NoValidTracesFoundException(String message){
			super(message);
		}
		/**
		 * Costruisce un nuovo oggetto NoValidTracesFoundException.
		 * Vengono specificati una SM, uno stato ed un messaggio della stessa SM.
		 * 
		 * @param sml una SM dell'architettura
		 * @param stato uno stato ella SM
		 * @param messaggio un messaggio scambiato dalla SM
		 */
		private NoValidTracesFoundException(SMLineare sml, ElementoStato stato, ElementoCanaleMessaggio messaggio){
			this("Nella SM "+sml.getNomeSM()+", dallo stato "+stato.getName()+" è impossibile arrivare ad eseguire lo scambio del messaggio "+messaggio.getName()+".");
			this.sml = sml;
			this.stato = stato;
			this.messaggio = messaggio;
		}
		/**
		 * @return il campo messaggio.
		 */
		private ElementoCanaleMessaggio getMessaggio() {
			return messaggio;
		}
		/**
		 * @return il campo sml.
		 */
		private SMLineare getSml() {
			return sml;
		}
		/**
		 * @return il campo stato.
		 */
		private ElementoStato getStato() {
			return stato;
		}
	}
	/**
	 * Eccezione sollevata ad una chiamata della funzione {@link TracceSD#integra(String, int, SMLinearizzate)}
	 * quando non è possibile produrre tracce integrazione per la sup-trace specificata nella chiamata.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class NoIntegrationsFoundException extends TestCaseGenerationException{
		/**
		 * Messaggio atto a descrivere la situazione rappresentata da questa eccezione
		 */
		private final static String DESCRIZIONE_SITUAZIONE = "Le sup-trace del SD corrente non possiedono integrazioni.";
		/**
		 * La traccia che non si riesce ad integrare
		 */
		private final Traccia traccia;
		/**
		 * Costruisce un nuovo oggetto NoIntegrationsFoundException.
		 * Viene specificata la traccia che non si riesce ad integrare.
		 * 
		 * @param traccia una traccia prodotta durante la generazione dei casi di test
		 */
		private NoIntegrationsFoundException(Traccia traccia){
			super("La sup-trace "+traccia+", nella SM "+traccia.getNomeProcesso()+", non può essere riprodotta rispetto alle altre SM.");
			this.traccia = traccia;
		}
	}
	/**
	 * Eccezione che rappresenta la situazione in cui una data transizione t<sub>1</sub> in una SM sm<sub>1</sub>
	 * può essere integrata solo se prodotta dopo di una transizione t<sub>2</sub> in una SM sm<sub>2</sub> ecc.
	 * finché l'ultima transizione t<sub>n</sub> di una SM sm<sub>n</sub> può essere prodotta solo dopo la nostra
	 * transizione t<sub>1</sub> nella SM sm<sub>1</sub>.
	 * Ovviamente tutte le altre transizioni da 2 ad n godono della stessa proprietà, ma a noi
	 * interessa averne una qualunque.<br>
	 * La classe ha due costruttori:<br>
	 * 1. il primo è utilizzato nel momento in cui è stata rilevata la ciclicità,<br>
	 * 2. il secondo, che ha come parametro un'eccezione di questo stesso tipo, è utilizzato per costruire il
	 * quadro completo delle sup-trace coinvolte dal ciclo, durante la propagazione dell'eccezione.<br>
	 * Un flag nell'eccezione corrente ({@link LoopingTransitionsException#conclusa}) indica se il quadro
	 * è concluso oppure no. Se il quadro è ancora in corso d'opera deve essere arricchito utilizzando
	 * il costruttore 2, che si fa carico di riconoscere se così il quadro si conclude oppure no. 
	 * 
	 * @author Fabrizio Facchini
	 */
	private class LoopingTransitionsException extends TestCaseGenerationException{
		/**
		 * Messaggio atto a descrivere la situazione rappresentata da questa eccezione
		 */
		private final static String DESCRIZIONE_SITUAZIONE = "Le transizioni implicate da un SD formano un ciclo di precedenze.";
		/**
		 * Collezione di tracce, una per ogni SM, interessate da un ciclo di precedenze.<br>
		 * Uso: <i>chiave</i> = nome di una SM, <i>valore</i> = traccia della SM.
		 */
		private TreeMap tracceLoop;
		/**
		 * Indica che questa eccezione ha collezionato tutte le tracce interessate
		 * dal ciclo di precedenze.
		 */
		private boolean conclusa = false;
		/**
		 * La transizione interessata dal ciclo di precedenze
		 */
		private ElementoMessaggio trans;
		/**
		 * Costruisce un nuovo oggetto LoopingTransitionsException.
		 * Viene specificata la traccia contenente la transizione coinvolta nel ciclo,
		 * il numero di transizioni significative e la transizione stessa.<br>
		 * Uso: utilizzare questo costruttore nel metodo
		 * {@link AlgoritmoTeStor.TracceSD#integra(String, int, SMLinearizzate)}
		 * quando è stato rilevato un ciclo di precedenze nelle tracce.
		 * Questo costruttore crea la prima, non conclusa eccezione
		 * nella pila della propagazione delle stesse.
		 * 
		 * @param tracciaSD una traccia prodotta durante la generazione dei casi di test
		 * @param primeNtrans il numero delle prime transizioni significative della traccia
		 * @param trans una transizione della traccia già visitata
		 * @see AlgoritmoTeStor.LoopingTransitionsException#conlusa
		 */
		private LoopingTransitionsException(TracciaSD tracciaSD, int primeNtrans, ElementoMessaggio trans){
			super(DESCRIZIONE_SITUAZIONE);
			this.trans = trans;
			this.tracceLoop = new TreeMap();
			this.tracceLoop.put(tracciaSD.getNomeProcesso(),tracciaSD.supTrace.primeNtrans(primeNtrans));
		}
		/**
		 * Costruisce un nuovo oggetto LoopingTransitionsException.
		 * Viene specificata una traccia coinvolta dall'eccezione, il numero di transizioni
		 * da considerare ed una eccezione dello stesso tipo.<br>
		 * Uso: utilizzare questo costruttore per creare una nuova eccezione sulla
		 * base di una non conclusa e catturata da una clausola catch.
		 * Il costruttore provvede da solo a controllare se è possibile concludere
		 * l'eccezione.
		 * 
		 * @param tracciaSD una traccia prodotta durante la generazione dei casi di test
		 * @param primeNtrans il numero delle prime transizioni significative della traccia
		 * @param lte una eccezione non conlusa dello stesso tipo catturata in una clausola catch
		 * @see AlgoritmoTeStor.LoopingTransitionsException#conlusa
		 */
		private LoopingTransitionsException(TracciaSD tracciaSD, int primeNtrans, LoopingTransitionsException lte){
			super();
			this.trans = lte.trans;
			this.tracceLoop = lte.tracceLoop;
			if(this.tracceLoop.get(tracciaSD.getNomeProcesso()) != null){
				// è già presente in tracceLoop una traccia per la SM della traccia specificata
				this.conclusa = true;
			}else{
				// la SM della traccia specificata è inclusa nel ciclo dell'eccezione
				this.tracceLoop.put(tracciaSD.getNomeProcesso(),tracciaSD.supTrace.primeNtrans(primeNtrans));
			}
		}
		/**
		 * @return il campo conclusa.
		 */
		private boolean isConclusa() {
			return conclusa;
		}
		/**
		 * @return il campo tracceLoop.
		 */
		private TreeMap getTracceLoop() {
			return tracceLoop;
		}
		/**
		 * @return il campo trans.
		 */
		private ElementoMessaggio getTrans() {
			return trans;
		}
		/* (non-Javadoc)
		 * @see java.lang.Throwable#getMessage()
		 */
		public String getMessage() {
			if(conclusa){
				String errore = "Nelle SM dell'architettura esiste il seguente ciclo di precedenze sulla transizione "+trans.getName()+":";
				Iterator scorri = tracceLoop.values().iterator();
				while(scorri.hasNext()){
					Traccia tracciaCorr = (Traccia)scorri.next();
					errore += "\n        SM "+tracciaCorr.getNomeProcesso()+": "+tracciaCorr;
				}
				return errore;
			}else{
				return DESCRIZIONE_SITUAZIONE;
			}
		}
	}
	/**
	 * Una lista di eccezioni sollevate durante la generazione dei casi di test.
	 * Poiché esiste una sola lista di questo tipo ad ogni esecuzione dell'algoritmo,
	 * <i>a livello di una esecuzione</i> essa contiene eccezioni di questo stesso tipo.
	 * <i>A livello di singolo SD</i> le eccezioni contenute sono di tipo
	 * {@link TestCaseGenerationException}.
	 * 
	 * @author Fabrizio Facchini
	 */
	private class TestCaseGenerationExceptionsList extends ExceptionsList{
		/**
		 * Messaggio atto a descrivere la situazione rappresentata da questa eccezione
		 */
		private final static String DESCRIZIONE_SITUAZIONE = "Durante la generazione dei casi di test per i SD selezionati si sono verificati degli errori.";
		/**
		 * Il SD a cui si riferisce questa lista di eccezioni
		 */
		private SequenceElement sequenceElement = null;
		/**
		 * I messaggi del SD che è stato possibile produrre in qualche caso di test
		 */
		private LinkedList messaggiGenerati;
		/**
		 * Il messaggio che non è stato possibile riprodurre
		 */
		private ElementoCanaleMessaggio messaggioCritico;
		/**
		 * Costruisce un nuovo oggetto TestCaseGenerationExceptionsList.
		 */
		private TestCaseGenerationExceptionsList(){
			super(DESCRIZIONE_SITUAZIONE);
		}
		/**
		 * @return il campo sequenceElement.
		 */
		private SequenceElement getSequenceElement() {
			return sequenceElement;
		}
		/**
		 * Imposta il valore del campo specificato.
		 * Utilizzare per impostare il SD per cui non è stato possibile
		 * generare alcun caso di test.
		 * 
		 * @param sequenceElement il valore per sequenceElement da assegnare.
		 */
		private void setSequenceElement(SequenceElement sequenceElement) {
			this.sequenceElement = sequenceElement;
		}
		/**
		 * Imposta lo stato della generazione dei casi di test per il SD corrente.
		 * Vengono specificati i messaggi prodotti (elementi di tipo {@link ElementoCanaleMessaggio},
		 * ed il messaggio che ha generato le eccezioni contenute in questa lista.
		 * 
		 * @param messaggiGenerati i messaggi che è stato possibile produrre in qualche caso di test
		 * @param messaggioCritico il messaggio che ha genrato le eccezioni in questa lista
		 */
		private void setMessaggi(LinkedList messaggiGenerati, ElementoCanaleMessaggio messaggioCritico){
			this.messaggiGenerati = messaggiGenerati;
			this.messaggioCritico = messaggioCritico;
		}
		/**
		 * @return il campo messaggioCritico.
		 */
		private ElementoCanaleMessaggio getMessaggioCritico() {
			return messaggioCritico;
		}
		/**
		 * Produce una stringa rappresentante i messaggi che è stato possibile riprodurre
		 * in qualche caso di test.
		 * 
		 * @return una rappresentazione dei messaggi
		 */
		private String stampaMessaggi(){
			String out = "";
			Iterator scorri = messaggiGenerati.iterator();
			while(scorri.hasNext()){
				out += " "+((ElementoCanaleMessaggio)scorri.next()).getName()+",";
			}
			return out;
		}
		/**
		 * Esegue l'aggiornamento delle eccezioni {@link LoopingTransitionsException}
		 * in funzione della traccia e del numero di transizioni specificate.
		 * Per ognuna di queste eccezioni che sia ancora non conclusa, confronta la SM
		 * della traccia specificata con tutte quelle nell'eccezione. Se ne trova,
		 * per ognuna aggiorna l'eccezione, arricchendo il quadro delle tracce coinvolte.
		 * 
		 * @param tracciaSD una traccia generata durante la generazione dei casi di test
		 * @param primeNtrans il numero delle prime transizioni della traccia da considerare
		 * @see AlgoritmoTeStor.LoopingTransitionsException#LoopingTransitionsException(TracciaSD, int, LoopingTransitionsException)
		 */
		private void aggiornaLooping(TracciaSD tracciaSD, int primeNtrans){
			ListIterator scorri = super.lista.listIterator();
			while(scorri.hasNext()){
				TestCaseGenerationException eCorr = (TestCaseGenerationException)scorri.next();
				if(eCorr instanceof LoopingTransitionsException){
					if(!((LoopingTransitionsException)eCorr).conclusa){
						scorri.remove();
						scorri.add(new LoopingTransitionsException(tracciaSD,primeNtrans,(LoopingTransitionsException)eCorr));
					}
				}
			}
		}
	}
	/**
	 * Messaggio esprimente l'impossibilità di proseguire con la generazione
	 */
	private final static String DESCRIZIONE_CONSEGUENZA_ERRORE = "Impossibile procedere con la generazione dei casi di test!";
	/**
	 * Titolo per la finestra di errore dell'algoritmo
	 */
	private final static String TITOLO_FINESTRA_ERRORE = "TeStor - Errore";
	/**
	 * Costruisce un nuovo oggetto AlgoritmoTeStor.
	 * 
	 * @param plugData il contenitore dei dati del plugin TeStor
	 */
	public AlgoritmoTeStor(IPlugData plugData) {
		super();
		this.plugData = plugData;
	}
	/**
	 * Il contenitore dei dati del plugin
	 */
	private final IPlugData plugData;
	/**
	 * Contatore per le tracce lineari prodotte dalla linearizzazione delle SM.
	 */
	private static long contaTracce = 0;
	/**
	 * Restituisce una copia della mappa specificata.
	 * Crea una mappa contenente le stesse associazioni chiave-valore della mappa originaria
	 * ma <i>senza</i> creare un clone né delle chiavi né dei valori.
	 * 
	 * @param mappa la mappa che si intende copiare
	 * @return una copia della mappa specificata
	 */
	private static TreeMap copiaTreeMap(TreeMap mappa){
		TreeMap copia = new TreeMap();
		Iterator scorriChiavi = mappa.keySet().iterator();
		while(scorriChiavi.hasNext()){
			Object chiaveCorr = scorriChiavi.next();
			copia.put(chiaveCorr,mappa.get(chiaveCorr));
		}
		return copia;
	}

	/**
	 * Esegue la linearizzazione delle SM presenti nell'architettura.
	 * Cicla sulle SM chiamando, per ciascuna di esse, {@link SMLinearizzate#linearizzaSM(ListaThread)}.
	 * Ottiene le SM da processare dalle Dinamiche di Processo specificate.
	 * 
	 * @param dinProc la lista delle Dinamiche di Processo (cioè i processi) dell'architettura
	 * @throws LinearizationExceptionsList se almeno una delle SM non può essere linearizzata
	 */
	private SMLinearizzate linearizza(ListaDP dinProc)
							throws LinearizationExceptionsList{

		// genera le SM linearizzate per l'architettura
		SMLinearizzate outputLinea = new SMLinearizzate(dinProc);
		
		//*******************************************
		// STAMPA tracce  -  DEBUG
		/*Iterator scorriSML = outputLinea.smLinearizzate.iterator();
		System.out.println("\n\nLinearizza: TRACCE GENERATE dalla linearizzazione delle SM");
		while(scorriSML.hasNext()){
			System.out.println((SMLineare)scorriSML.next());
		}*/
		//*******************************************
		
		//System.out.println(outputLinea.getTutteLeTrans());
		
		return outputLinea;
	}	
	/**
	 * Itera sui vari SD in input, chiamando di volta in volta
	 * il metodo {@link AlgoritmoTeStor#generaCasiDiTestDaSeqMess(Iterator, SMLinearizzate)},
	 * il quale genera i casi di test per la sequenza messaggi sul SD corrente.
	 * Restituisce una lista ({@link DefaultListModel}) di liste
	 * ({@link DefaultListModel}) di casi di test ({@link SequenceElement}).
	 * 
	 * @param inSD i SD in input al TeStor
	 * @param outputLinea l'output della linearizzazione delle SM
	 * @return la lista contenente, per ogni SD in input, i casi di test da esso implicati
	 */
	private DefaultListModel generaCasiDiTest(DefaultListModel inSD, SMLinearizzate outputLinea){
		// i casi di test per i SD in input
		/* Ogni elemento della lista è relativo al corrisponedente elemento
		 * (per posizione) della lista di SD in input. Esso contiene una lista
		 * (DefaultListModel) i cui elementi sono i casi di test (SequenceElement)
		 * implicati dal corrispondente SD in input
		 */
	    DefaultListModel casiDiTest = new DefaultListModel();
	    // lista per le eventuali eccezioni
	    TestCaseGenerationExceptionsList listaEccezioni = new TestCaseGenerationExceptionsList();
		
		System.out.println("\n\ngeneraCasiDiTest: GENERAZIONE CASI DI TEST per i SD dell'architettura");//DEBUG
		// iteriamo sui diversi SD in input al TeStor
		for (int i = 0; i < inSD.size(); i++) {
			System.out.println("\n\n*********************************************************************");//DEBUG
			System.out.println("\ngeneraCasiDiTest: SD "+((SequenceElement)inSD.get(i)).getName()+" ("+((SequenceElement)inSD.get(i)).getListaSeqLink().size()+" messaggio/i):");//DEBUG
			// le sup-trace parziali (vuote) prodotte da questo SD
			SMtge smtge = new SMtge(outputLinea);
			// il SD corrente
			SequenceElement seCorr = (SequenceElement)inSD.get(i);
			try {
				// aggiungiamo i casi di test per il SD corrente
				
				//ezio 2006 - fixed bug - ho introdotto iteratorTemporal() per avere la giusta sequenza temporale dei messaggi, in generale la lista dei messaggi non è ordinata temporalmente
				casiDiTest.addElement(smtge.generaCasiDiTestDaSeqMess(seCorr.getListaSeqLink().iteratorTemporal(),outputLinea));
				
			} catch (TestCaseGenerationExceptionsList e) {
				// impostiamo il SD che ha generato le eccezioni
				e.setSequenceElement(seCorr);
				// aggiungiamo la lista di eccezioni come elemento della lista di liste
				listaEccezioni.add(e);
				// dobbiamo mantenere l'associazione SD - casi di test (anche se non ce ne sono)
				casiDiTest.addElement(new DefaultListModel());
			}
		}
		
		// costruzione finestra d'errore
		String errore = listaEccezioni.getLocalizedMessage()+"\n\nLista errori per SD ("+listaEccezioni.getNumEccezioni()+"):\n";
		for (int i = 0; i < listaEccezioni.getNumEccezioni(); i++) {
			TestCaseGenerationExceptionsList listaDiLista = (TestCaseGenerationExceptionsList)listaEccezioni.get(i);
			//errore += " > SD "+listaDiLista.sequenceElement.getName()+" ("+listaDiLista.getNumEccezioni()+" errore/i):\n";
			errore += " > SD "+listaDiLista.getSequenceElement().getName()+" - messaggi riprodotti:"+listaDiLista.stampaMessaggi()+" - messaggio critico: "+listaDiLista.getMessaggioCritico().getName()+"\n";
			errore += "      "+listaDiLista.getNumEccezioni()+" errore/i:\n";
			
			//for(int j = 0; j < listaDiLista.getNumEccezioni(); j++){
			for(int j = listaDiLista.getNumEccezioni()-1; j >= 0; j--){
				errore += "    - "+((TestCaseGenerationException)listaDiLista.get(j)).getLocalizedMessage()+"\n";
			}
		}
		// visualizza la finestra d'errore se ci sono eccezioni
		if(listaEccezioni.getNumEccezioni() > 0){
			JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),errore,TITOLO_FINESTRA_ERRORE,JOptionPane.ERROR_MESSAGE);
		}
		
		return casiDiTest;
	}

	/**
	 * Esegue l'algoritmo di generazione dei casi di test sulla lista di SD (oggetti di tipo
	 * {@link plugin.sequencediagram.data.SequenceElement}) specificata.
	 * Le SM per l'algoritmo sono specificate in processi.
	 * Dapprima linearizza le SM (tramite il metodo {@link AlgoritmoTeStor#linearizza(ListaDP)}),
	 * poi genera i casi di test per i SD specificati e le SM ottenute dalla linearizzazione
	 * (metodo {@link AlgoritmoTeStor#generaCasiDiTest(DefaultListModel, SMLinearizzate)}).
	 * 
	 * @param listaSD i {@link plugin.sequencediagram.data.SequenceElement} su cui deve essere eseguita la generazione
	 * @param processi le SM dell'architettura
	 * @param rifComp la finestra di Charmy
	 * @return i casi di test generati
	 */
	public synchronized DefaultListModel esegui(DefaultListModel listaSD, ListaDP processi, Container rifComp){
		// i casi di test per i SD dell'architettura
		DefaultListModel casiDiTest = new DefaultListModel();
		// azzeriamo il contatore delle tracce
		contaTracce = 0;	
		
		// prima di lanciare l'algoritmo controlliamo che le SM non abbiano errori*************
		ThreadCheck tc = new ThreadCheck((plugin.statediagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.state"));
		// esegue il controllo e raccoglie la stringa rappresentante gli errori
		ThreadCheck.Error errorCheck;
		String risultato = "";
		
		errorCheck = tc.messagesCheck();
		if(errorCheck.isError()){
			risultato += errorCheck.printError();
		}
		errorCheck = tc.checkNumPar();
		if(errorCheck.isError()){
			risultato += errorCheck.printError();
		}
		errorCheck = tc.threadMessagesCheck();
		if(errorCheck.isError()){
			risultato += errorCheck.printError();
		}

		if(risultato!=""){// ci sono errori
			JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),"Le StateMachine dell'architettura presentano degli errori.\n"+DESCRIZIONE_CONSEGUENZA_ERRORE,TITOLO_FINESTRA_ERRORE,JOptionPane.ERROR_MESSAGE);
			new ThreadCheckWindow(null,risultato);
		}else{// nessun errore
			// prende il cursore attuale
			Cursor curs = rifComp.getCursor();
			// lo cambia con la clessidra
			rifComp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
			FileOutputStream out = null;
			try {
				try {// ridirigiamo l'output su file per questioni di spazio
					out = new FileOutputStream("TeStor.log");
					System.setOut(new PrintStream(out));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				
				
				// genera i casi di test sui SD dalle SM linearizzate *******
				casiDiTest = generaCasiDiTest(listaSD, linearizza(processi));
				// **********************************************************
				
				
				
	        } catch (LinearizationExceptionsList e) {
	        	String errore = e.getLocalizedMessage()+"\n\nLista errori ("+e.getNumEccezioni()+"):\n";
				for (int i = 0; i < e.getNumEccezioni(); i++) {
					errore += " > "+e.get(i).getLocalizedMessage()+"\n";
				}
				errore += "\n"+DESCRIZIONE_CONSEGUENZA_ERRORE;
				JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),errore,TITOLO_FINESTRA_ERRORE,JOptionPane.ERROR_MESSAGE);
	        } finally {
	        	try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	        	System.setOut(System.out);
	        	// cambia il cursore con quello precedente
				rifComp.setCursor(curs);
	        }
		}
			
		return casiDiTest;
	}
}
