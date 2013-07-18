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
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import plugin.sequencediagram.data.delegate.DelegateListaParallelListener;
import plugin.sequencediagram.graph.GraficoParallelo;
import plugin.sequencediagram.pluglistener.IListaParallelListener;
import core.internal.runtime.data.IPlugData;

/**
 *
 * @author  FLAMEL 2005
 */

/** Classe che implementa l'operatore parallelo. */

public class ListaParallel implements Serializable
{
     /** Memorizza una lista collegata di oggetti della classe ElementoParallelo. */
     protected LinkedList lista;
    
     private IPlugData plugData = null;
    
     private DelegateListaParallelListener listener;
     
    /**
     * variabile che indica il fatto che una serie di 
     * modifiche appartengono ad una stessa transazione
     * la variabile viene utilizzata prevalentemente in <code><i>removeListSelected()</i></code>
     */
    protected boolean isTransaction = false;
    
      /** Costruttore. */
    public ListaParallel(IPlugData pl) 
    {
  		lista = new LinkedList();
  		plugData = pl;
  		listener = new DelegateListaParallelListener(this, pl);
    }
    
    /** Restituisce il numero di elementi memorizzati nella lista. */
    public int size() 
    {
        return lista.size();
    }
    
   /** Aggiunge un nuovo elemento alla lista. */
    public boolean addElement(ElementoParallelo par)
    {
    	if (par==null) return false;
    	if (lista==null) return false;
        if (par==null) 
        	return false;
        if (lista==null) 
        	return false;
        
        if(lista.contains(par)){
        	return true; //gia c'è
        }
        
        par.creaGraficoParallel();	    	
        par.setUpdateEp(listener);
        listener.notifyAdd(par);
        return lista.add(par);
        
    }
    /** 
     * Inserito ezio 2006
     * Restituisce l'elemento con l'identificatore specificato, 
	  *  'null' se non e' presente nella lista. 
	  * @param long id identificatore dell'arco
	  * @author ezio
     */
	 public ElementoParallelo getElementById(long id) 
	  {
		  Iterator ite = lista.iterator();
		  while(ite.hasNext()){
			  ElementoParallelo parallelo = (ElementoParallelo) ite.next();
		  	if(parallelo.getId() == id){ //elemento trovato
		  		return parallelo;
		  	}
		  }
		return null;
	  }
    
    /** Clonazione dell'oggetto. */    
    public ListaParallel cloneListaParallel()
    {
    	ElementoParallelo tmpElementoPar = null;
    	ElementoParallelo clonedElementoPar = null;
    	ListaParallel cloned = new ListaParallel(plugData);
    	int j=0;
    	
    	while (j<lista.size())
    	{
    		tmpElementoPar = (ElementoParallelo)(lista.get(j));
    		clonedElementoPar = tmpElementoPar.cloneParallel();
    		(cloned.lista).add(clonedElementoPar);
    		j++;	
    	}
    	return cloned;     	
    }
    
    /** Restituisce l'elemento all'indice specificato, ma
    	genera un errore se l'indice non appartiene alla lista. */
    protected ElementoParallelo get(int i)
    {
        return (ElementoParallelo)lista.get(i);
    }
    
    /**
     * aggiunta di un listener per la lista
     * @param ilpl
     */
    public void addListener(IListaParallelListener ilpl) {
    	listener.add(ilpl);
    }

    /** Stampa tutti gli oggetti della lista. */
    public void paintLista(Graphics2D g2D)
    {
        for (int i=0; i<lista.size(); i++){
            ((ElementoParallelo)lista.get(i)).paintParallel(g2D);
        }
    }
    
    /** Deseleziona tutti gli elementi della lista. */
    public void noSelected()
    {
        ElementoParallelo par;        
        if ((lista!=null)&&(!lista.isEmpty()))
	        for (int i=0;i<lista.size();i++){
	 	    	par = (ElementoParallelo)lista.get(i);
	            par.setSelected(false);
			}        
    }
    
    /** Restituisce l'elemento con l'id specificato,
        'null' se non e' presente nella lista. */
    public ElementoParallelo getElementId(int id) 
    {
        if (lista==null) 
        	return null;
        if (lista.isEmpty()==true) 
        	return null;
        try{ 
           for(int i=0;i<lista.size();i++)
           {
               ElementoParallelo par =(ElementoParallelo) lista.get(i);
               if(par.getId() == id)
                   return (ElementoParallelo) lista.get(i);
           }                       
        }
        catch (IndexOutOfBoundsException e){ 
            return null;
        } 
        return null;
    }
    
    
    /** Restituisce l'elemento all'indice specificato,
        'null' se non e' presente nella lista. */
    public ElementoParallelo getElement(int i) 
    {
        if (lista==null) 
        	return null;
        if (lista.isEmpty()==true) 
        	return null;
        try{ 
            return (ElementoParallelo)lista.get(i);
        }
        catch (IndexOutOfBoundsException e){ 
            return null;
        } 
    }
    
    /** Scorre la lista dei canali e restituisce, se esiste, il parallel 
        tale che il punto p, preso come parametro di input, si trova
        in un intorno della linea che rappresenta il parallel stesso. */
    public ElementoParallelo getElementSelected(Point p) 
    {
        int j = 0;
        
        if (lista==null) return null;
        while ((j<lista.size()) && (!((ElementoParallelo)lista.get(j)).isSelezionato(p))){
            j++;
        }
        if (j<lista.size()){
            return (ElementoParallelo)lista.get(j);
        }
        else{
            return null;
        }
    }
    
    /** Scorre la lista dei canali e restituisce, se esiste, il parallel 
        tale che il punto p, preso come parametro di input, si trova
        in un intorno della linea che rappresenta il parallel stesso. */
    public ElementoParallelo getElementSelect() 
    {
        int j = 0;
        
        if (lista==null) return null;
        while ((j<lista.size()) && (!((ElementoParallelo)lista.get(j)).isSelected())){
            j++;
        }
        if (j<lista.size()){
            return (ElementoParallelo)lista.get(j);
        }
        else{
            return null;
        }
    }
    
    /** Aggiorna la posizione di tutti i canali collegati
    	all'oggetto passato come parametro d'ingresso. */
    public void updateListaParallelPosizione(ElementoSeqLink prc)
    {
        ElementoParallelo tmpCon;        
        for (int i=0; i<lista.size(); i++){
            tmpCon = (ElementoParallelo)lista.get(i);
            tmpCon.updateParallelPosizione();
            
        }
    }
    
    /** Rimuove l'operatore specificato come parametro. */
    public boolean removeElement(ElementoParallelo par) 
    {
        int i;
        if (lista==null) 
        	return false; 
        if (lista.isEmpty() == true) 
        	return false;
        try{ 
            i = lista.indexOf(par); 
        } 
        catch (IndexOutOfBoundsException e){
 	    	String s = "Indice fuori dai limiti ammessi \ndentro la classe ListaParallel$removeElement.\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        for(int j=0;j<par.size();j++){
            ElementoSeqLink link= par.get(j);
            link.setParallel(false,0);            
        }
        lista.remove(i); 
        return true;	   
    }

    /**
     * rimozione di   un listener per la lista
     * @param ilpl
     */
    public void removeListener(IListaParallelListener ilpl){
    	listener.removeElement(ilpl);
    }


    /**
     * rimuove tutti i listener registrati
     *
     */
    public void removeAllListener(){
    	listener.removeAllElements();
    }
    
    /** Rimuove tutti gli elementi della lista. */
    public void removeAll() 
    {
        if (lista==null) 
        	return;
        
		Iterator ite = lista.iterator();
		while (ite.hasNext()){
			this.removeElement((ElementoParallelo)ite.next());
			ite = lista.iterator();
		}
    }


	/**
	 * @return iteratore della lista dei Parallel
	 */
	public Iterator iterator() {
		return lista.iterator();
	}
        
        /** Metodo per ricostruire la struttura delle classi a partire
        dalle informazioni memorizzate sul file. */    
        public void restoreFromFile()
        {
            GraficoParallelo grafico1;
            ElementoParallelo par;

            if (lista!=null){
                for (int i=0; i<lista.size(); i++){
                        par = (ElementoParallelo)(lista.get(i));
                        grafico1 = (GraficoParallelo)(par.getGrafico());
                        par.updateParallelPosizione();
                        grafico1.restoreFromFile();
                }
            } 
        }
        
        /**
	 * @author  Flamel
	 * rimuove l'operatore se almeno uno dei processi inclusi in esso è selezionato
	 * 
	 */
	public void removeParallelInLink() {
		boolean ctrl;
		ElementoParallelo par;
                LinkedList list;
                ElementoSeqLink link;
		if (this.size() > 0) {
			for (int i = 0; i < size(); i++) {
				par = (ElementoParallelo) getElement(i);
                                list = par.getList_mess();
                              for (int k = 0; k <list.size(); k++) {
                                   link = (ElementoSeqLink) list.get(k); 
                                    if (link.isSelected()) {
					isTransaction = true;
                                        for (int j = 0; j <list.size(); j++) {
                                            link = (ElementoSeqLink) list.get(j);
                                            link.setParallel(false,0);
                                        }
					removeElement(par);                                        
				}
                              }                               
			}
			isTransaction = false;
		}
	}
        








}