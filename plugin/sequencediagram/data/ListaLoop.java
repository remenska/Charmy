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

import plugin.sequencediagram.data.delegate.DelegateListaLoopListener;
import plugin.sequencediagram.graph.GraficoLoop;
import plugin.sequencediagram.pluglistener.IListaLoopListener;
import core.internal.runtime.data.IPlugData;

/**
 *
 * @author  FLAMEL 2005
 */




/** Classe che implementa una lista per memorizzare gli operatori loop inseriti nel Sequence Diagram. */

public class ListaLoop implements Serializable
{
     /** Memorizza una lista collegata di oggetti della classe ElementoLoop. */
     protected LinkedList lista;
    
     private IPlugData plugData = null;
    
     private DelegateListaLoopListener listener;
     
    /**
     * variabile che indica il fatto che una serie di 
     * modifiche appartengono ad una stessa transazione
     * la variabile viene utilizzata prevalentemente in <code><i>removeListSelected()</i></code>
     */
    protected boolean isTransaction = false;
    
      /** Costruttore. */
    public ListaLoop(IPlugData pl) 
    {
  		lista = new LinkedList();
  		plugData = pl;
  		listener = new DelegateListaLoopListener(this, pl);
    }
    
    /** Restituisce il numero di elementi memorizzati nella lista. */
    public int size() 
    {
        return lista.size();
    }
    
   /** Aggiunge un nuovo elemento alla lista. */
    public boolean addElement(ElementoLoop Loop)
    {
    	if (Loop==null) return false;
    	if (lista==null) return false;
        if (Loop==null) 
        	return false;
        if (lista==null) 
        	return false;
        
        if(lista.contains(Loop)){
        	return true; //gia c'è
        }
        
        Loop.creaGraficoLoop();	    	
        Loop.setUpdateEp(listener);
        listener.notifyAdd(Loop);
        return lista.add(Loop);
        
    }
    /** 
     * Inserito ezio 2006
     * Restituisce l'elemento con l'identificatore specificato, 
	  *  'null' se non e' presente nella lista. 
	  * @param long id identificatore dell'arco
	  * @author ezio
     */
	 public ElementoLoop getElementById(long id) 
	  {
		  Iterator ite = lista.iterator();
		  while(ite.hasNext()){
			  ElementoLoop loop = (ElementoLoop) ite.next();
		  	if(loop.getId() == id){ //elemento trovato
		  		return loop;
		  	}
		  }
		return null;
	  }
	 
    /** Clonazione dell'oggetto. */    
    public ListaLoop cloneListaLoop()
    {
    	ElementoLoop tmpElementoCon = null;
    	ElementoLoop clonedElementoCon = null;
    	ListaLoop cloned = new ListaLoop(plugData);
    	int j=0;
    	
    	while (j<lista.size())
    	{
    		tmpElementoCon = (ElementoLoop)(lista.get(j));
    		clonedElementoCon = tmpElementoCon.cloneLoop();
    		(cloned.lista).add(clonedElementoCon);
    		j++;	
    	}
    	return cloned;     	
    }
    
    /** Restituisce l'elemento all'indice specificato, ma
    	genera un errore se l'indice non appartiene alla lista. */
    protected ElementoLoop get(int i)
    {
        return (ElementoLoop)lista.get(i);
    }
    
    /**
     * aggiunta di un listener per la lista
     * @param ilpl
     */
    public void addListener(IListaLoopListener ilLoop) {
    	listener.add(ilLoop);
    }

    /** Stampa tutti gli oggetti della lista. */
    public void paintLista(Graphics2D g2D)
    {
        for (int i=0; i<lista.size(); i++){
            ((ElementoLoop)lista.get(i)).paintLoop(g2D);
        }
    }
    
    /** Deseleziona tutti gli elementi della lista. */
    public void noSelected()
    {
        ElementoLoop Loop;        
        if ((lista!=null)&&(!lista.isEmpty()))
	        for (int i=0;i<lista.size();i++){
	 	    	Loop = (ElementoLoop)lista.get(i);
	            Loop.setSelected(false);
			}        
    }
    
    /** Restituisce l'elemento all'indice specificato,
        'null' se non e' presente nella lista. */
    public ElementoLoop getElement(int i) 
    {
        if (lista==null) 
        	return null;
        if (lista.isEmpty()==true) 
        	return null;
        try{ 
            return (ElementoLoop)lista.get(i);
        }
        catch (IndexOutOfBoundsException e){ 
            return null;
        } 
    }
    
    /** Restituisce l'elemento con l'id specificato,
        'null' se non e' presente nella lista. */
    public ElementoLoop getElementId(long id) 
    {
        if (lista==null) 
        	return null;
        if (lista.isEmpty()==true) 
        	return null;
        try{ 
           for(int i=0;i<lista.size();i++)
           {
               ElementoLoop loop =(ElementoLoop) lista.get(i);
               if(loop.getId() == id)
                   return (ElementoLoop) lista.get(i);
           }                       
        }
        catch (IndexOutOfBoundsException e){ 
            return null;
        } 
        return null;
    }
    
    
    /** Scorre la lista dei canali e restituisce, se esiste, il Loop 
        tale che il punto p, preso come parametro di input, si trova
        in un intorno della linea che rappresenta il Loop stesso. */
    public ElementoLoop getElementSelected(Point p) 
    {
        int j = 0;
        
        if (lista==null) return null;
        while ((j<lista.size()) && (!((ElementoLoop)lista.get(j)).isSelezionato(p))){
            j++;
        }
        if (j<lista.size()){
            return (ElementoLoop)lista.get(j);
        }
        else{
            return null;
        }
    }
    
    /** Scorre la lista dei canali e restituisce, se esiste, il Loop 
        tale che il punto p, preso come parametro di input, si trova
        in un intorno della linea che rappresenta il Loop stesso. */
    public ElementoLoop getElementSelect() 
    {
        int j = 0;
        
        if (lista==null) return null;
        while ((j<lista.size()) && (!((ElementoLoop)lista.get(j)).isSelected())){
            j++;
        }
        if (j<lista.size()){
            return (ElementoLoop)lista.get(j);
        }
        else{
            return null;
        }
    }
    
    /** Aggiorna la posizione di tutti i canali collegati
    	all'oggetto passato come parametro d'ingresso. */
    public void updateListaLoopPosizione(ElementoSeqLink prc)
    {
        ElementoLoop tmpCon;        
        for (int i=0; i<lista.size(); i++){
            tmpCon = (ElementoLoop)lista.get(i);
            tmpCon.updateLoopPosizione();
            
        }
    }
    
    /** Rimuove l'operatore specificato come parametro. */
    public boolean removeElement(ElementoLoop Loop) 
    {
        int i;
        if (lista==null) 
        	return false; 
        if (lista.isEmpty() == true) 
        	return false;
        try{ 
            i = lista.indexOf(Loop); 
        } 
        catch (IndexOutOfBoundsException e){
 	    	String s = "Indice fuori dai limiti ammessi \ndentro la classe ListaLoop$removeElement.\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        for(int j=0;j<Loop.size();j++){
            ElementoSeqLink link= Loop.get(j);
            link.setLoop(false,0,0,0);            
        }
        lista.remove(i); 
        return true;	   
    }

    /**
     * rimozione di   un listener per la lista
     * @param ilpl
     */
    public void removeListener(IListaLoopListener ilLoop){
    	listener.removeElement(ilLoop);
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
			this.removeElement((ElementoLoop)ite.next());
			ite = lista.iterator();
		}
    }
    
    /** Rimuove l'elemento con indice i . */
    public void removeElement(int i) 
    {
        if (lista==null) 
        	return;
        
        lista.remove(i);      
    }


	/**
	 * @return iteratore della lista dei Loop
	 */
	public Iterator iterator() {
		return lista.iterator();
	}
        
        /** Metodo per ricostruire la struttura delle classi a partire
        dalle informazioni memorizzate sul file. */    
        public void restoreFromFile()
        {
            GraficoLoop grafico1;
            ElementoLoop Loop;

            if (lista!=null){
                for (int i=0; i<lista.size(); i++){
                        Loop = (ElementoLoop)(lista.get(i));
                        grafico1 = (GraficoLoop)(Loop.getGrafico());
                        Loop.updateLoopPosizione();
                        grafico1.restoreFromFile();
                }
            } 
        }
        
        /**
	 * @author  Flamel
	 * rimuove l'operatore se almeno uno dei processi inclusi in esso è selezionato
	 * 
	 */
	public void removeLoopInLink() {
		boolean ctrl;
		ElementoLoop Loop;
                LinkedList list;
                ElementoSeqLink link;
		if (this.size() > 0) {
			for (int i = 0; i < size(); i++) {
				Loop = (ElementoLoop) getElement(i);
                                list = Loop.getList_mess();
                              for (int k = 0; k <list.size(); k++) {
                                   link = (ElementoSeqLink) list.get(k); 
                                    if (link.isSelected()) {
					isTransaction = true;
                                        for (int j = 0; j <list.size(); j++) {
                                            link = (ElementoSeqLink) list.get(j);
                                            link.setLoop(false,0,0,0);
                                        }
					removeElement(Loop);                                        
				}
                              }                               
			}
			isTransaction = false;
		}
	}
        
        








}