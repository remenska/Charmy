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
    

package plugin.sequencediagram.data;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import plugin.sequencediagram.data.delegate.DelegateListaConstraintListener;
import plugin.sequencediagram.graph.GraficoConstraint;
import plugin.sequencediagram.pluglistener.IListaConstraintListener;
import plugin.topologydiagram.resource.data.ElementoCanaleMessaggio;
import core.internal.runtime.data.IPlugData;

/** Classe per memorizzare i constraints inseriti nel Sequence Diagram. */

public class ListaConstraint implements Serializable
{
    /** Memorizza una lista collegata di oggetti della classe ElementoConstraint. */
    protected LinkedList lista;
   
    private DelegateListaConstraintListener listener;
    
    private IPlugData plugData = null;
    
    /**
	 * variabile che indica il fatto che una serie di 
	 * modifiche appartengono ad una stessa transazione
	 * la variabile viene utilizzata prevalentemente in <code><i>removeListSelected()</i></code>
	 */
	protected boolean isTransaction = false;
    
    /** Costruttore. */
    public ListaConstraint(IPlugData pl) 
    {
  		lista = new LinkedList();
  		plugData = pl;
  		listener = new DelegateListaConstraintListener(this, pl);
    }
	
 
    /** Restituisce il numero di elementi memorizzati nella lista. */
    public int size() 
    {
        return lista.size();
    }
 

    /** Aggiunge un nuovo elemento alla lista. */
    public boolean addElement(ElementoConstraint con)
    {
    	if (con==null) return false;
        if (lista==null) 
        	return false;
        
        if(lista.contains(con)){
        	return true; //gia c'Ã¨
        }
        
        con.creaGraficoConstraint();	    	
        con.setUpdateEp(listener);
        listener.notifyAdd(con);
        return lista.add(con);
        
    }
    
    /** Clonazione dell'oggetto. */    
    public ListaConstraint cloneListaConstraint()
    {
    	ElementoConstraint tmpElementoCon = null;
    	ElementoConstraint clonedElementoCon = null;
    	ListaConstraint cloned = new ListaConstraint(plugData);
    	int j=0;
    	
    	while (j<lista.size())
    	{
    		tmpElementoCon = (ElementoConstraint)(lista.get(j));
    		clonedElementoCon = tmpElementoCon.cloneConstraint();
    		(cloned.lista).add(clonedElementoCon);
    		j++;	
    	}
    	return cloned;     	
    }
    
 
    /** Restituisce true nel caso il constraint esiste già sul link*/
 /*   public boolean isInsertableConstraint(ElementoSeqLink link)
    {
        if (lista==null) 
        	return true;
        if (lista.isEmpty()==true) 
        	return true;
        for(int i=0 ;i<lista.size();i++)
        {
            ElementoConstraint con =(ElementoConstraint) lista.get(i);
             if(con.getLink().equals(link))
                return false;
        }
       return true;

    }*/
    
    /** 
     * Inserito ezio 2006
     * Restituisce l'elemento con l'identificatore specificato, 
	  *  'null' se non e' presente nella lista. 
	  * @param long id identificatore dell'arco
	  * @author ezio
     */
	 public ElementoConstraint getElementById(long id) 
	  {
		  Iterator ite = lista.iterator();
		  while(ite.hasNext()){
			  ElementoConstraint ec = (ElementoConstraint) ite.next();
		  	if(ec.getId() == id){ //elemento trovato
		  		return ec;
		  	}
		  }
		return null;
	  }
    
    
    /** Restituisce l'elemento all'indice specificato,
        'null' se non e' presente nella lista. */
    public ElementoConstraint getElement(int i) 
    {
        if (lista==null) 
        	return null;
        if (lista.isEmpty()==true) 
        	return null;
        try{ 
            return (ElementoConstraint)lista.get(i);
        }
        catch (IndexOutOfBoundsException e){ 
            return null;
        } 
    } 
    
    /** Restituisce la lista dei constraint attivi al tempo passato come parametro */
    
    public LinkedList getConstraintAtTime(long time)
    {        
        if (lista==null) 
        	return null;
        if (lista.isEmpty()) 
        	return null;        
        LinkedList conList=new LinkedList();
        for (int i=0;i<lista.size();i++)
            if ((((ElementoConstraint)lista.get(i)).getTimeFrom().getTime()<=time)&
                (((ElementoConstraint)lista.get(i)).getTimeTo().getTime()>=time))
                conList.add((ElementoConstraint)lista.get(i));
        return conList;
    }
    
    /** Restituisce l'elemento all'indice specificato, ma
    	genera un errore se l'indice non appartiene alla lista. */
    protected ElementoConstraint get(int i)
    {
        return (ElementoConstraint)lista.get(i);
    }  
	

    /** Scorre la lista dei canali e restituisce, se esiste, il constraint 
        tale che il punto p, preso come parametro di input, si trova
        in un intorno della linea che rappresenta il constraint stesso. */
    public ElementoConstraint getElementSelected(Point p) 
    {
        int j = 0;
        
        if (lista==null) return null;
        while ((j<lista.size()) && (!((ElementoConstraint)lista.get(j)).isSelezionato(p))){
            j++;
        }
        if (j<lista.size()){
            return (ElementoConstraint)lista.get(j);
        }
        else{
            return null;
        }
    } 
 
 
 
    /** Rimuove tutti gli elementi della lista. */
    public void removeAll() 
    {
        if (lista==null) 
        	return;
        
		Iterator ite = lista.iterator();
		while (ite.hasNext()){
			this.removeElement((ElementoConstraint)ite.next());
			ite = lista.iterator();
		}
    } 


    /** Rimuove tutti i link collegati all'oggetto preso in input. */
    public  void removeAllLink(ElementoSeqLink seq) 
    {
        ElementoConstraint con;
        int i=0;

        if (lista==null) 
        	return;
        try{
            while (i<lista.size()){
                con = (ElementoConstraint)lista.get(i);
                if (con != null){
                    if ((ElementoSeqLink)con.getLink()==seq){
	        			lista.remove(i);
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

 
    /** Rimuove il Constraint specificato come parametro. */
    public boolean removeElement(ElementoConstraint con) 
    {
        int i;
        if (lista==null) 
        	return false; 
        if (lista.isEmpty() == true) 
        	return false;
        try{ 
            i = lista.indexOf(con); 
        } 
        catch (IndexOutOfBoundsException e){
 	    	String s = "Indice fuori dai limiti ammessi \ndentro la classe ListaCanale$removeChannel.\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        ElementoSeqLink link = con.getLink();
        link.removeConstraint(con);
        lista.remove(i); 
        return true;	   
    }

    
    /** Rimuove dalla lista tutti gli elementi presenti nella
    	lista passato come parametro d'ingresso. */
    public void removeListeSelected(LinkedList delCon)
    {
    	boolean ctrl;
    	ElementoConstraint tmpCon;
    	
    	if ((delCon != null)&&(!delCon.isEmpty())){
    		for (int i=0; i<delCon.size(); i++){
    			tmpCon = (ElementoConstraint)delCon.get(i);
    			ctrl = removeElement(tmpCon);
    		}
    	}
    }


    /** Rimuove dalla lista tutti gli elementi presenti nella
    	lista passato come parametro d'ingresso. */
    public void removeListeSelected(ListaConstraint delCon)
    {
    	boolean ctrl;
    	ElementoConstraint tmpCon;
    	
    	if ((delCon != null)&&(!delCon.isEmpty())){
    		for (int i=0; i<delCon.size(); i++){
    			tmpCon = (ElementoConstraint)delCon.get(i);
    			ctrl = removeElement(tmpCon);
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
        
        if (l==null) 
        	return false;
        for (int i=0;i<l.size();i++){
	    	n = (String) l.get(i);
	    	if (n.equalsIgnoreCase(nome)) 
	    		return true;
        }
        return false;
    }	

        
    /** Stampa tutti gli oggetti della lista. */
    public void paintLista(Graphics2D g2D)
    {
        for (int i=0; i<lista.size(); i++){
            ((ElementoConstraint)lista.get(i)).paintConstraint(g2D);
        }
    }
    
    
    /** Aggiorna la posizione di tutti i canali collegati
    	all'oggetto passato come parametro d'ingresso. */
    public void updateListaConstraintPosizione(ElementoSeqLink prc)
    {
        ElementoConstraint tmpCon;        
        for (int i=0; i<lista.size(); i++){
            tmpCon = (ElementoConstraint)lista.get(i);
            if (prc.equals(tmpCon.getLink())){
                tmpCon.updateConstraintPosizione();
            }
        }
    }

    
    /** Deseleziona tutti gli elementi della lista. */
    public void noSelected()
    {
        ElementoConstraint con;        
        if ((lista!=null)&&(!lista.isEmpty()))
	        for (int i=0;i<lista.size();i++){
	 	    	con = (ElementoConstraint)lista.get(i);
	            con.setSelected(false);
			}        
    }

    
    /** Restituisce la lista di tutti gli elementi selezionati. */
    public LinkedList listSelectedChannel()
    {
        LinkedList clista = new LinkedList();
        ElementoConstraint con;        
        if ((lista != null)&&(!lista.isEmpty()))
	        for (int i=0; i<lista.size(); i++){
		    	con = (ElementoConstraint)lista.get(i);
	            if (con.isSelected()){
	                clista.add(con);
	            }
	        }
        return clista;
    }


	/** Metodo astratto per eseguire l'operazione di paste di un elemento. */
	public boolean addPasteElement(ElementoConstraint con, ListaSeqLink lsl)
    {
        return true;
    }


    /** Seleziona un link se contenuto nel rettangolo passato in ingresso_
    	Probabilmente non funziona e pu? essere eliminato. */
    public void setSelectedIfInRectangle(Rectangle2D rectExternal)
    {
      	ElementoConstraint con;      	
    	if ((lista != null) && (!lista.isEmpty())){
        	for (int i=0; i<lista.size(); i++){
	    		con = (ElementoConstraint)lista.get(i);
                	con.setSelected(true);
        	}    		
    	}
    	
    }
 
 
 	/** Seleziona un link se l'elemento di partenza
 		e di arrivo sono stati selezionati. */
    public void setSelectedIfInRectangle()
    {
      	ElementoConstraint con;      	
    	if ((lista != null) && (!lista.isEmpty())){
        	for (int i=0; i<lista.size(); i++){
                con = (ElementoConstraint) lista.get(i);
                if (con.getLink().isSelected()){
                    con.setSelected(true);
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
 		}
 		return localStr;	
 	} 


	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
    public void restoreFromFile()
    {	
		ElementoConstraint con;
		GraficoConstraint grafico1;
		boolean flusso;		
		if (lista!=null){
			for (int i=0; i<lista.size(); i++){
				con = (ElementoConstraint)(lista.get(i));
				grafico1 = (GraficoConstraint)(con.getGrafico());                                                
				con.updateConstraintPosizione();
				grafico1.restoreFromFile();
			}
		}
	}  

            
    
    /**
     * aggiunta di un listener per la lista
     * @param ilpl
     */
    public void addListener(IListaConstraintListener ilpl) {
    	listener.add(ilpl);
    }


    /**
     * rimozione di   un listener per la lista
     * @param ilpl
     */
    public void removeListener(IListaConstraintListener ilpl){
    	listener.removeElement(ilpl);
    }


    /**
     * rimuove tutti i listener registrati
     *
     */
    public void removeAllListener(){
    	listener.removeAllElements();
    }
    
    /**
	 * @author  Michele Stoduto
	 * rimuove tutti i processi selezionati (attributo selected = true)
	 * 
	 */
	public void removeAllSelected() {
		boolean ctrl;
		ElementoConstraint con;
		if (this.size() > 0) {
                    Iterator ite = lista.iterator();
			while (ite.hasNext()) {
				con = (ElementoConstraint) ite.next();
				if (con.getLink().isSelected()) 
                                {
                                    isTransaction = true;
                                    removeElement(con);
                                    ite = lista.iterator();
				}
			}
			isTransaction = false;
		}
	}


	/**
	 * @return iteratore della lista dei Constraint
	 */
	public Iterator iterator() {
		return lista.iterator();
	}	 
     	      	
}