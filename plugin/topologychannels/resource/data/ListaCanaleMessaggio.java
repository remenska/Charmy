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
    

package plugin.topologychannels.resource.data;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import plugin.topologychannels.resource.data.interfacce.IListaCanMessNotify;

import core.internal.runtime.data.IPlugData;


/** Superclasse astratta di ListaCanale e ListaMessaggio_
	La prima implementa la lista dei canali inseriti nel S_A_ Topology
	Diagram, la seconda quella dei link di uno State Diagram. */

public abstract class ListaCanaleMessaggio 
				implements Serializable
{
    /** Memorizza una lista collegata di oggetti della classe ElementoCanaleMessaggio. */
    protected LinkedList lista;
   
	/** delega per la gestione dei listener */
	private IListaCanMessNotify canaleNotify = null;
	
	protected IPlugData plugData;
	
    /** Costruttore. */
    public ListaCanaleMessaggio(IPlugData plugData) 
    {
  		lista = new LinkedList();
  		this.plugData = plugData; 
    }
	
 
 	/**
 	 * notifica l'aggiunta di un canale
 	 * @param cm
 	 */
 	private void canaleAddNotify(ElementoCanaleMessaggio cm){
 		if(canaleNotify!=null){
 			canaleNotify.notifyAdd(cm);
 		}
 	}
 	
	/**
	 * notifica la cancellazione di un canale
	 * @param cm
	 */
	private void canaleRemoveNotify(ElementoCanaleMessaggio cm){
		if(canaleNotify!=null){
			canaleNotify.notifyRemove(cm);
		}
	}
 	
	/**
	 * notifica la cancellazione di un canale
	 * @param cm
	 */
	public void canaleRefresh(){
		if(canaleNotify!=null){
			canaleNotify.refreshCanale();
		}
	}
 	
 
 
    /** Restituisce il numero di elementi memorizzati nella lista. */
    public int size() 
    {
        return lista.size();
    }
 

    /** Aggiunge un nuovo elemento alla lista, ma solo se 
     *	risulta libero almeno un collegamento. 
     * @param Elemento da aggiungere
     * @return true se l'elemento ? stato aggiunto, false altrimenti
     **/
    public boolean addElement(ElementoCanaleMessaggio cnl)
    {
        boolean bool; 
        if (cnl==null) 
        	return false;
        if (lista==null) 
        	return false;
        int pos = trovaCollegamentoLibero(cnl.getElement_one(),cnl.getElement_two());
        if (pos>=0){
            cnl.setPosizione(pos);	    	
            bool = lista.add(cnl);
            if(bool){
				cnl.setUpdateEp(canaleNotify);
				if(canaleNotify!= null){
					canaleNotify.notifyAdd(cnl);
				}	
            }
            return bool;
        }
        else{
            String s = "Errore: \nnon e' possibile aggiungere altri\n canali tra la copia di processi.";
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }
    
    
	/** Aggiunge un nuovo elemento alla lista senza effettuare al controllo
		sull'esistenza o meno di un collegamento libero. */
    public void add(ElementoCanaleMessaggio cnl)
    {
        if ((cnl!=null) && (lista!=null)){
    		cnl.setUpdateEp(canaleNotify);
			lista.add(cnl);
			if(canaleNotify!= null){
				canaleNotify.notifyAdd(cnl);
			}
    	}
    }


    /** Restituisce l'elemento con il nome specificato, 
        'null' se non e' presente nella lista. */
    public ElementoCanaleMessaggio getElement(String nome) 
    {
        int j = 0;
        if ((lista==null)||(nome==null)) 
        	return null;
		for(j = 0; j< lista.size(); j++){
			if(nome.equals(((ElementoCanaleMessaggio)lista.get(j)).getName())){
				return (ElementoCanaleMessaggio)lista.get(j);
			}
		}
		return null;
    }   

	/** Restituisce l'ultimo elemento. */
	public ElementoCanaleMessaggio getLastElement() 
	{
		if (lista==null) 
			return null;
		return (ElementoCanaleMessaggio)lista.getLast();
	}   


 
	/** Restituisce l'elemento con l'identificatore specificato, 
	  *  'null' se non e' presente nella lista. 
	  * @param long id identificatore dell'arco
	  * @author Stoduto Michele
      */
	 public ElementoCanaleMessaggio getElementById(long id) 
	  {
		  Iterator ite = lista.iterator();
		  while(ite.hasNext()){
		  	ElementoCanaleMessaggio ecm = (ElementoCanaleMessaggio) ite.next();
		  	if(ecm.getId() == id){ //elemento trovato
		  		return ecm;
		  	}
		  }
		return null;
	  }


 
 
 
 
    /** Restituisce l'elemento all'indice specificato,
        'null' se non e' presente nella lista. */
    public ElementoCanaleMessaggio getElement(int i) 
    {
        if (lista==null) 
        	return null;
        if (lista.isEmpty()==true) 
        	return null;
        try{ 
            return (ElementoCanaleMessaggio)lista.get(i);
        }
        catch (IndexOutOfBoundsException e){ 
            return null;
        } 
    } 
    
    
    /** Restituisce l'elemento all'indice specificato, ma
    	genera un errore se l'indice non appartiene alla lista. */
    protected ElementoCanaleMessaggio get(int i)
    {
        return (ElementoCanaleMessaggio)lista.get(i);
    }  
	

    /** Scorre la lista dei canali e restituisce, se esiste, il link 
        tale che il punto p, preso come parametro di input, si trova
        in un intorno della linea che rappresenta il link stesso. */
    public ElementoCanaleMessaggio getElementSelected(Point p) 
    {
        int j = 0;
        if (lista==null) 
        	return null;
        while ((j<lista.size()) && (!((ElementoCanaleMessaggio)lista.get(j)).isSelezionato(p))){
            j++;
        }
        if (j<lista.size()){
            return (ElementoCanaleMessaggio)lista.get(j);
        }
        else{
            return null;
        }
    } 
 
 
    /** Dati in ingresso i due oggetti tra cui si vuole creare un nuovo link,
        restituisce un numero indicante il primo collegamento libero disponibile_ 
        In altre parole, il metodo determina il tipo di linea da tracciare tra i due
        oggetti interessati; se non vi sono collegamenti liberi, restituisce '-1'. */
    private int trovaCollegamentoLibero(ElementoProcessoStato proc1, ElementoProcessoStato proc2)
    {
        /** Massimo numero di collegamenti tra ogni coppia di processi. */
        final int MaxCollegamenti = 20;
        ElementoCanaleMessaggio canaleTemp;

        boolean occupato[] = new boolean[MaxCollegamenti];
        // Inizializza tutte le posizioni a libero.
        for(int i=0; i<MaxCollegamenti; i++) 
        	occupato[i]=false;
        for(int i=0; i<lista.size(); i++){
            canaleTemp = (ElementoCanaleMessaggio)lista.get(i);
            if (((ElementoProcessoStato)canaleTemp.getElement_one() == proc1) &&
                ((ElementoProcessoStato)canaleTemp.getElement_two() == proc2)){
                occupato[canaleTemp.getPosizione()]=true;
            }
        }		
        // Ricerca un valore disponibile.
        int j = 0;
        while ((occupato[j]) && (j<MaxCollegamenti)){
            j++;
        }
        if (j < MaxCollegamenti){
            return j;
        }
        else{
        // Non vi sono canali disponibili.
            return -1;
        }
    }
 
 
    /** Rimuove tutti gli elementi della lista. */
    public void removeAll() 
    {
        if (lista==null) 
        	return;
		Iterator ite = lista.iterator();
		while (ite.hasNext()){
			this.removeElement((ElementoCanaleMessaggio)ite.next());
			ite = lista.iterator();
		}
    } 


    /** Rimuove tutti i link collegati all'oggetto preso in input. */
    public  void removeAllLink(ElementoProcessoStato proc) 
    {
        ElementoCanaleMessaggio canale;
        int i=0;
        if (lista==null) 
        	return;
        try{
            while (i<lista.size()){
                canale = (ElementoCanaleMessaggio)lista.get(i);
                if (canale != null){
		    		if ( (((ElementoProcessoStato)canale.getElement_one()).getId() == proc.getId()) ||
                        (((ElementoProcessoStato)canale.getElement_two()).getId() == proc.getId())){
	        			lista.remove(i);
						if(canaleNotify!= null){
							canaleNotify.notifyRemove(canale);
						}
						i = 0;
		        		continue;
		    		}
	        	}    
        	i++;
	    	}
        } 
        catch (IndexOutOfBoundsException e){
 	    	String s = "Indice fuori dai limiti ammessi \n dentro la classe ListaCanale$removeAllLink().\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

	/**
	 * rimuove l'elemento mediante identificatore
	 * @param id identificatore dell'elementoCanaleMessaggio
	 * @return true se l'elemeto ? rimosso, false altrimenti
	 */
	public boolean removeById(long id){
		Iterator ite = lista.iterator();
		 while(ite.hasNext()){
			 ElementoCanaleMessaggio ecm = (ElementoCanaleMessaggio)ite.next();
			 if(ecm.getId() == id){
				 ecm.setUpdateEp(null);
				 lista.remove(ecm);
				 if(canaleNotify!= null){
					 canaleNotify.notifyRemove(ecm);
				 }    		
				 return true;	   
			 }
		 }
		 return false;
	}

 
    /** Rimuove il link specificato come parametro. 
     * @return true se l'elemento ? stato rimosso, false altrimenti
     * */
    public boolean removeElement(ElementoCanaleMessaggio ec) 
    {
        if (lista==null) 
        	return false; 
        if (lista.isEmpty() == true) 
        	return false;
        /*
         * rimuove mediante identificatore
         */
       return removeById(ec.getId());
	}       
 
	public boolean removeAllElements(String linkName){
		if (lista==null) 
			return false; 
		if (lista.isEmpty() == true) 
			return false;
		boolean del=false;
		for(int i=0;i<lista.size();i++){
			if (((ElementoCanaleMessaggio)lista.get(i)).getName().equals(linkName)){
				lista.remove(i);
				i--;
				del=true;	
			}
		}
		return del;
	}

	/**
	 * rimuove tutti i canali selezionati dalla lista
	 * gestendo la condizione di stessa transazione
	 * per la generazione dei messaggi
	 * Stoduto Michele
	 */
	public void removeAllSelected(){
		boolean ctrl;
		ElementoCanaleMessaggio tmpCanMes;

		if (this.size() > 0) {
			boolean inCorso = plugData.getPlugDataManager().startSessione();
			Iterator ite = lista.iterator();
			while (ite.hasNext()) {
				tmpCanMes = (ElementoCanaleMessaggio) ite.next();
				if (tmpCanMes.isSelected()) {
					this.removeElement(tmpCanMes);
					ite = lista.iterator(); //azzera l'iterazione altrimenti non funziona
				}

			}
			plugData.getPlugDataManager().stopSessione(inCorso);
		}
		
	}

    
    /** Rimuove dalla lista tutti gli elementi presenti nella
    	lista passato come parametro d'ingresso. 
      */
    public void removeListeSelected(LinkedList delCanali)
    {
    	boolean ctrl;
    	ElementoCanaleMessaggio tmpCanale;
    	if ((delCanali != null)&&(!delCanali.isEmpty())){
    		for (int i=0; i<delCanali.size(); i++){
    			tmpCanale = (ElementoCanaleMessaggio)delCanali.get(i);
    			ctrl = removeElement(tmpCanale);
    		}
    	}
    }


    /** Rimuove dalla lista tutti gli elementi presenti nella
    	lista passato come parametro d'ingresso. */
    public void removeListeSelected(ListaCanaleMessaggio delCanali)
    {
    	boolean ctrl;
    	ElementoCanaleMessaggio tmpCanale;
    	if ((delCanali != null)&&(!delCanali.isEmpty())){
    		for (int i=0; i<delCanali.size(); i++){
    			tmpCanale = (ElementoCanaleMessaggio)delCanali.get(i);
    			ctrl = removeElement(tmpCanale);
    		}
    	}
    }    
  

    /** Controlla se il nome, nel primo parametro, ? gi? stato inserito 
        nella lista, nel secondo parametro_ Restituisce 'true' se il nome ? 
        gi? presente nela lista, 'false' altrimenti_ 
        Viene lasciato temporaneamente perch? presente nel prototipo. */
    public boolean giaPresente(String nome, LinkedList l)
    {
        String n;
        
        if(l==null) 
        	return false;
        for (int i=0;i<l.size();i++){
	    	n = (String) l.get(i);
	    	if (n.equalsIgnoreCase(nome)) 
	    		return true;
        }
        return false;
    }	


    /** Verifica se nella lista esiste un link con il nome
    	passato come parametro d'ingresso. */
    public boolean giaPresente(String nome)
    {
        ElementoCanaleMessaggio chn;
        String n;
        
        if (lista == null) 
        	return false;
        for (int i=0;i<lista.size();i++){
	    	chn = (ElementoCanaleMessaggio) lista.get(i);
            n = (String) chn.getName();
	    	if (n.equalsIgnoreCase(nome)) 
	    		return true;
        }
        return false;
    }

     
    /** Restituisce tutti i nomi dei canali presenti nel class diagram_
        La lista restituita e' composta da oggetti di tipo String
        in cui sono stati eliminati i nomi di messaggi duplicati_ 
        Viene lasciato temporaneamente perch? presente nel prototipo. */
    public LinkedList getAllMessageName()
    {
        ElementoCanaleMessaggio canale;
        LinkedList risultato = new LinkedList();
        if (lista==null) 
        	return risultato;
        for (int i=0;i<lista.size();i++) {
	    	canale = (ElementoCanaleMessaggio)lista.get(i);
	    	if (canale==null) 
	    		continue;
	    	String nome = canale.getName();
	    	if (!giaPresente(nome, risultato)) 
	    		risultato.add(nome); 
        }  		
        return risultato;
    }
    
    
    /** Stampa tutti gli oggetti della lista. */
    public void paintLista(Graphics2D g2D)
    {
        for (int i=0; i<lista.size(); i++){
            ((ElementoCanaleMessaggio)lista.get(i)).paintCanale(g2D);
        }
    }
    
    
    /** Aggiorna la posizione di tutti i canali collegati
    	all'oggetto passato come parametro d'ingresso. */
    public void updateListaCanalePosizione(ElementoProcessoStato prc)
    {
        ElementoCanaleMessaggio tmpCanale;
        for (int i=0; i<lista.size(); i++){
            tmpCanale = (ElementoCanaleMessaggio)lista.get(i);
            if ((prc.equals(tmpCanale.getElement_one())) || (prc.equals(tmpCanale.getElement_two()))){
                tmpCanale.updateCanalePosizione();
            }
        }
    }

    
    /** Deseleziona tutti gli elementi della lista. */
    public void noSelected()
    {
        ElementoCanaleMessaggio ec;
        if ((lista!=null)&&(!lista.isEmpty()))
	        for (int i=0;i<lista.size();i++){
	 	    	ec = (ElementoCanaleMessaggio)lista.get(i);
	            ec.setSelected(false);
			}        
    }

	/**
	 * restituisce un iteratore della lista
	 * @return iterator
	 */
	public Iterator iterator(){
		return lista.iterator();
	}
    
    /** Restituisce la lista di tutti gli elementi selezionati. */
    public LinkedList listSelectedChannel()
    {
        LinkedList clista = new LinkedList();
        ElementoCanaleMessaggio canale;
        if ((lista != null)&&(!lista.isEmpty()))
	        for (int i=0; i<lista.size(); i++){
		    	canale = (ElementoCanaleMessaggio)lista.get(i);
	            if (canale.isSelected()){
	                clista.add(canale);
	            }
	        }
        return clista;
    }



    /** Seleziona un link se contenuto nel rettangolo passato in ingresso_
    	Probabilmente non funziona e pu? essere eliminato. */
    public void setSelectedIfInRectangle(Rectangle2D rectExternal)
    {
      	ElementoCanaleMessaggio canale;
    	if ((lista != null) && (!lista.isEmpty())){
        	for (int i=0; i<lista.size(); i++){
	    		canale = (ElementoCanaleMessaggio)lista.get(i);
            	if (canale.isInRect(rectExternal)){
                	canale.setSelected(true);
            	}
        	}    		
    	}
    }
 
 
 	/** Seleziona un link se l'elemento di partenza
 		e di arrivo sono stati selezionati. */
    public void setSelectedIfInRectangle()
    {
      	ElementoCanaleMessaggio canale;
    	if ((lista != null) && (!lista.isEmpty())){
        	for (int i=0; i<lista.size(); i++){
	    		canale = (ElementoCanaleMessaggio) lista.get(i);
            	if ((canale.getElement_one()).isSelected() && (canale.getElement_two()).isSelected()){
                	canale.setSelected(true);
            	}
        	}    		
    	}
    }
    
    
    /** Verifica se la lista ? vuota. */
    public boolean isEmpty()
    {
    	return lista.isEmpty();
    } 
    

 	/** Modifica il nome del link in ingresso se questo inizia con '*'_
 		Molto utile per aggiustare il nome dopo un'operazione di paste. */ 
 	public String adjustNameChannel(String str)
 	{
 		String localStr;
 		localStr = str;
 		if (localStr.startsWith("*")){
 			localStr = localStr.substring(1,localStr.length());
 			localStr = "cp_" + localStr;
 			int j = 2;
 			int i = localStr.length();
    		while (this.giaPresente(localStr)){
    			localStr = localStr.substring(0,i);
    			localStr = localStr + "_" + j;
    			j++;
    		} 				
 		}
 		return localStr;	
 	} 


	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
    public abstract void restoreFromFile();
     	      	
	/**
	 * ritorna la classe responsabile delle notifiche
	 * @return null se non vi ? nessuna classe registrata
	 */
	public IListaCanMessNotify getNotify() {
		return canaleNotify;
	}

	/**
	 * setta la classe responsabile delle notifiche
	 * @param notify   null per eliminate la classe di notifica
	 */
	public void setNotify(IListaCanMessNotify notify) {
		canaleNotify = notify;
	}

}