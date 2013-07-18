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

import plugin.sequencediagram.data.delegate.DelegateListaSimListener;
import plugin.sequencediagram.graph.GraficoSim;
import plugin.sequencediagram.pluglistener.IListaSimListener;
import core.internal.runtime.data.IPlugData;

/**
 *
 * @author  FLAMEL 2005
 */

/** Classe che implementa l'operatore simultaneo. */

public class ListaSim implements Serializable
{
     /** Memorizza una lista collegata di oggetti della classe ElementoSim. */
     protected LinkedList lista;
    
     private IPlugData plugData = null;
    
     private DelegateListaSimListener listener;
     
    /**
     * variabile che indica il fatto che una serie di 
     * modifiche appartengono ad una stessa transazione
     * la variabile viene utilizzata prevalentemente in <code><i>removeListSelected()</i></code>
     */
    protected boolean isTransaction = false;
    
      /** Costruttore. */
    public ListaSim(IPlugData pl) 
    {
  		lista = new LinkedList();
  		plugData = pl;
  		listener = new DelegateListaSimListener(this, pl);
    }
    
    /** Restituisce il numero di elementi memorizzati nella lista. */
    public int size() 
    {
        return lista.size();
    }
    
   /** Aggiunge un nuovo elemento alla lista. */
    public boolean addElement(ElementoSim sim)
    {
    	if (sim==null) return false;
    	if (lista==null) return false;
        if (sim==null) 
        	return false;
        if (lista==null) 
        	return false;
        
        if(lista.contains(sim)){
        	return true; //gia c'è
        }
        
        sim.creaGraficoSim();	    	
        sim.setUpdateEp(listener);
        listener.notifyAdd(sim);
        return lista.add(sim);
        
    }
    
    /** Clonazione dell'oggetto. */    
    public ListaSim cloneListaSim()
    {
    	ElementoSim tmpElementoPar = null;
    	ElementoSim clonedElementoPar = null;
    	ListaSim cloned = new ListaSim(plugData);
    	int j=0;
    	
    	while (j<lista.size())
    	{
    		tmpElementoPar = (ElementoSim)(lista.get(j));
    		clonedElementoPar = tmpElementoPar.cloneSim();
    		(cloned.lista).add(clonedElementoPar);
    		j++;	
    	}
    	return cloned;     	
    }
    
    /** Restituisce l'elemento all'indice specificato, ma
    	genera un errore se l'indice non appartiene alla lista. */
    protected ElementoSim get(int i)
    {
        return (ElementoSim)lista.get(i);
    }
    
    /**
     * aggiunta di un listener per la lista
     * @param ilpl
     */
    public void addListener(IListaSimListener ilsim) {
    	listener.add(ilsim);
    }

    /** Stampa tutti gli oggetti della lista. */
    public void paintLista(Graphics2D g2D)
    {
        for (int i=0; i<lista.size(); i++){
            ((ElementoSim)lista.get(i)).paintSim(g2D);
        }
    }
    
    /** Deseleziona tutti gli elementi della lista. */
    public void noSelected()
    {
        ElementoSim sim;        
        if ((lista!=null)&&(!lista.isEmpty()))
	        for (int i=0;i<lista.size();i++){
	 	    	sim = (ElementoSim)lista.get(i);
	            sim.setSelected(false);
			}        
    }
    
    /** 
     * Inserito ezio 2006
     * Restituisce l'elemento con l'identificatore specificato, 
	  *  'null' se non e' presente nella lista. 
	  * @param long id identificatore dell'arco
	  * @author ezio
     */
	 public ElementoSim getElementById(long id) 
	  {
		  Iterator ite = lista.iterator();
		  while(ite.hasNext()){
			  ElementoSim sim = (ElementoSim) ite.next();
		  	if(sim.getId() == id){ //elemento trovato
		  		return sim;
		  	}
		  }
		return null;
	  }
    
    
    /** Restituisce l'elemento con l'id specificato,
        'null' se non e' presente nella lista. */
    public ElementoSim getElementId(int id) 
    {
        if (lista==null) 
        	return null;
        if (lista.isEmpty()==true) 
        	return null;
        try{ 
           for(int i=0;i<lista.size();i++)
           {
               ElementoSim sim =(ElementoSim) lista.get(i);
               if(sim.getId() == id)
                   return (ElementoSim) lista.get(i);
           }                       
        }
        catch (IndexOutOfBoundsException e){ 
            return null;
        } 
        return null;
    }
    
    
    /** Restituisce l'elemento all'indice specificato,
        'null' se non e' presente nella lista. */
    public ElementoSim getElement(int i) 
    {
        if (lista==null) 
        	return null;
        if (lista.isEmpty()==true) 
        	return null;
        try{ 
            return (ElementoSim)lista.get(i);
        }
        catch (IndexOutOfBoundsException e){ 
            return null;
        } 
    }
    
    /** Scorre la lista dei canali e restituisce, se esiste, il Sim 
        tale che il punto p, preso come parametro di input, si trova
        in un intorno della linea che rappresenta il Sim stesso. */
    public ElementoSim getElementSelected(Point p) 
    {
        int j = 0;
        
        if (lista==null) return null;
        while ((j<lista.size()) && (!((ElementoSim)lista.get(j)).isSelezionato(p))){
            j++;
        }
        if (j<lista.size()){
            return (ElementoSim)lista.get(j);
        }
        else{
            return null;
        }
    }
    
    /** Scorre la lista dei canali e restituisce, se esiste, il Sim 
        tale che il punto p, preso come parametro di input, si trova
        in un intorno della linea che rappresenta il Sim stesso. */
    public ElementoSim getElementSelect() 
    {
        int j = 0;
        
        if (lista==null) return null;
        while ((j<lista.size()) && (!((ElementoSim)lista.get(j)).isSelected())){
            j++;
        }
        if (j<lista.size()){
            return (ElementoSim)lista.get(j);
        }
        else{
            return null;
        }
    }
    
    /** Aggiorna la posizione di tutti i canali collegati
    	all'oggetto passato come parametro d'ingresso. */
    public void updateListaSimPosizione(ElementoSeqLink prc)
    {
        ElementoSim tmpCon;        
        for (int i=0; i<lista.size(); i++){
            tmpCon = (ElementoSim)lista.get(i);
            tmpCon.updateSimPosizione();
            
        }
    }
    
    /** Rimuove l'operatore specificato come parametro. */
    public boolean removeElement(ElementoSim sim) 
    {
        int i;
        if (lista==null) 
        	return false; 
        if (lista.isEmpty() == true) 
        	return false;
        try{ 
            i = lista.indexOf(sim); 
        } 
        catch (IndexOutOfBoundsException e){
 	    	String s = "Indice fuori dai limiti ammessi \ndentro la classe ListaSim$removeElement.\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        for(int j=0;j<sim.size();j++){
            ElementoSeqLink link= sim.get(j);
            link.setSimultaneous(false,0);            
        }
        lista.remove(i); 
        return true;	   
    }

    /**
     * rimozione di   un listener per la lista
     * @param ilpl
     */
    public void removeListener(IListaSimListener ilsim){
    	listener.removeElement(ilsim);
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
			this.removeElement((ElementoSim)ite.next());
			ite = lista.iterator();
		}
    }


	/**
	 * @return iteratore della lista dei Sim
	 */
	public Iterator iterator() {
		return lista.iterator();
	}
        
        /** Metodo per ricostruire la struttura delle classi a partire
        dalle informazioni memorizzate sul file. */    
        public void restoreFromFile()
        {
            GraficoSim grafico1;
            ElementoSim sim;

            if (lista!=null){
                for (int i=0; i<lista.size(); i++){
                        sim = (ElementoSim)(lista.get(i));
                        grafico1 = (GraficoSim)(sim.getGrafico());
                        sim.updateSimPosizione();
                        grafico1.restoreFromFile();
                }
            } 
        }
        
        /**
	 * @author  Flamel
	 * rimuove l'operatore se almeno uno dei processi inclusi in esso è selezionato
	 * 
	 */
	public void removeSimInLink() {
		boolean ctrl;
		ElementoSim sim;
                LinkedList list;
                ElementoSeqLink link;
		if (this.size() > 0) {
			for (int i = 0; i < size(); i++) {
				sim = (ElementoSim) getElement(i);
                                list = sim.getList_mess();
                              for (int k = 0; k <list.size(); k++) {
                                   link = (ElementoSeqLink) list.get(k); 
                                    if (link.isSelected()) {
					isTransaction = true;
                                        for (int j = 0; j <list.size(); j++) {
                                            link = (ElementoSeqLink) list.get(j);
                                            link.setSimultaneous(false,0);
                                        }
					removeElement(sim);                                        
				}
                              }                               
			}
			isTransaction = false;
		}
	}








}