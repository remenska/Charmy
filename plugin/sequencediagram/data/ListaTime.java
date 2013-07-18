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

import plugin.sequencediagram.data.delegate.DelegateListaTimeListener;
import plugin.sequencediagram.pluglistener.IListaTimeListener;
import core.internal.runtime.data.IPlugData;

/** La classe implementa la lista degli istanti di tempo 
	(ElementoTime) utilizzati nel Sequence Diagram. */

public class ListaTime implements Serializable
{
    
    /** Memorizza una lista di oggetti 
    	della classe ElementoTime. */
    protected LinkedList lista;
    
	/** Variabile per abilitare/disabilitare la
		visualizzazione della stringa del time. */
	private boolean stringIsVisible;

	/** Variabile per abilitare/disabilitare la
		visualizzazione delle linee nella fascia. */	
	private boolean lineIsVisible;    
    
    /**
     * delega per la gestione dell'invio dei messaggi
     */
    private DelegateListaTimeListener listener;
    
    
    /** Costruttore. */
    /**
     * costruttore
     * @param plugData, riferimento al contenitore dei dati
     */
    public  ListaTime(IPlugData pd) 
    {
   		lista  = new LinkedList();
   		stringIsVisible = true;
   		lineIsVisible = true;
   		
   		listener = new DelegateListaTimeListener(this, pd);
    }
 

	/** Imposta la visualizzazione delle stringhe. 
	 * @param ctrl true visualizzo la stringa
	 * */
	public void setStringVisible(boolean ctrl)
	{
		ElementoTime et;
		stringIsVisible = ctrl;
		if (lista!=null)
		{
			for(int i=0; i<lista.size(); i++)
			{
				et = (ElementoTime)(lista.get(i));
				et.setStringVisible(ctrl);
			}
		}
	}
	
	
	/** Imposta la visualizzazione delle linee. */
	public void setLineVisible(boolean ctrl)
	{
		ElementoTime et;
		
		lineIsVisible = ctrl;
		if (lista!=null)
		{
			for(int i=0; i<lista.size(); i++)
			{
				et = (ElementoTime)(lista.get(i));
				et.setLineVisible(ctrl);
			}
		}		
	}
	
	
    /** Restituisce il numero degli elementi 
    	attualmente inseriti nella lista. */
    public int size() 
    {
        if (lista.isEmpty())
        {
            return 0;
        }
        else
        { 
            return lista.size();
        }
    }
    
    
    /** Inserisce il primo elemento della lista_ 
    	Restituisce 'false' se la lista non ? vuota
    	oppure non ? stata inizializzata. */
    public boolean addFirst(ElementoTime et)
    {
    	if (lista==null)
    		return false;
    	if (et==null)
    		return false;
    	if (lista.size() > 0)
    	{
    		return false;
    	}
    	else
    	{	
    		
    		et.setLineVisible(lineIsVisible);
    		et.setStringVisible(stringIsVisible);
    		boolean bo = lista.add(et);
    		if(bo){
    			et.setUpdateEp(listener);
    			listener.notifyAdd(et);
    		}
    		return bo;
    	}
    }
    
    
    /** Inserisce il primo elemento della lista_ 
    	Restituisce 'false' se la lista non ? vuota
    	oppure non ? stata inizializzata. */
    public boolean addFirst(long tempo)
    {
    	ElementoTime et;
    	
    	if (lista==null)
    		return false;
    	if (tempo<0)
    		return false;
    	if (lista.size() > 0)
    	{
    		return false;
    	}
    	else
    	{	//SequenceEditor.MARGINE_SUPERIORE
    		et = new ElementoTime(tempo,140,stringIsVisible,lineIsVisible, null);
			boolean bo = lista.add(et);
			if(bo){
				et.setUpdateEp(listener);
				listener.notifyAdd(et);
			}
    		return bo;
    	}
    }
     

    /** Inserisce il primo elemento della lista_ 
    	Restituisce 'false' se la lista non ? vuota
    	oppure non ? stata inizializzata. */
    public boolean addFirst()
    {
    	
    	return addFirst(0);

    }
     
          
    /** Inserisce un nuovo elemento in coda alla lista_ 
    	Restituisce 'true' se l'elemento ? stato inserito, 
    	'false' altrimenti. */
    public boolean addLast(ElementoTime et) 
    {
        ElementoTime proc;
        
        if (lista==null) 
            return false;
        if (et==null) 
            return false;
        // Controlla se la lista possiede almeno un elemento.
        if (lista.size()>0)
        {
        	// Verifica che l'elemento da inserire rispetti l'ordinamento.
        	proc = (ElementoTime)lista.getLast();
        	if (proc.getTime() < et.getTime())
        	{
    			et.setLineVisible(lineIsVisible);
    			et.setStringVisible(stringIsVisible);
				et.setUpdateEp(listener);
				listener.notifyAdd(et);
        		lista.addLast(et);	
	        	return true;
        	}
        }
        else
        {
    		et.setLineVisible(lineIsVisible);
    		et.setStringVisible(stringIsVisible);       
			et.setUpdateEp(listener);
			listener.notifyAdd(et); 	
        	lista.add(et);
	        return true;        	
        }
        return false;
    }
    

    /** Inserisce un nuovo elemento in coda alla lista_ 
    	Restituisce 'true' se l'elemento ? stato inserito, 
    	'false' altrimenti. */    
    public boolean addLast(long tempo)
    {
    	ElementoTime proc;
    	ElementoTime et;
    	
    	if (lista==null)
    		return false;
    	if (tempo<0)
    		return false;
        // Controlla se la lista possiede almeno un elemento.
        if (lista.size()>0)
        {
        	// Verifica che l'elemento da inserire rispetti l'ordinamento.
        	proc = (ElementoTime)lista.getLast();
        	if (proc.getTime() < tempo)
        	{
        		et = new ElementoTime(tempo,proc.getMaxY(),stringIsVisible,lineIsVisible, null);
				et.setUpdateEp(listener);
				listener.notifyAdd(et);
        		lista.addLast(et);
	        	return true;
        	}        	        	
        }
        else
        {
//			SequenceEditor.MARGINE_SUPERIORE = 140
        	et = new ElementoTime(tempo,140,stringIsVisible,lineIsVisible, null); 
			et.setUpdateEp(listener);
			listener.notifyAdd(et); 
        	lista.addLast(et);
	        return true;        	
        }
        return false;    	
    }
    

    /** Inserisce un nuovo elemento in coda alla lista_ 
    	Restituisce 'true' se l'elemento ? stato inserito, 
    	'false' altrimenti. */    
    public boolean addLast()
    {
    	ElementoTime proc;
    	ElementoTime et;
    	
    	if (lista==null)
    		return false;
        // Controlla se la lista possiede almeno un elemento.
        if (lista.size()>0)
        {
        	proc = (ElementoTime)lista.getLast();
        	et = new ElementoTime((proc.getTime())+1,proc.getMaxY(),stringIsVisible,lineIsVisible, null);
			et.setUpdateEp(listener);
			listener.notifyAdd(et);
        	lista.addLast(et);
	       	return true;        	        	
        }
        else
        {  //SequenceEditor.MARGINE_SUPERIORE
        	et = new ElementoTime(0,140,stringIsVisible,lineIsVisible,null);
			et.setUpdateEp(listener);
			listener.notifyAdd(et);
        	lista.add(et);
	       	return true;        	
        }    	  	
    }
    
    
    /** Restituisce l'ultimo elemento della lista. */
    public ElementoTime getLast()
    {
    	if (lista==null)
    	{
    		return null;
    	}
    	else
    	{
    		return (ElementoTime)(lista.getLast());
    	}
    }


    /**  Aggiorna il numero di linee dell'elemento nella lista coincidente con quello 
    	passato come parametro_ Ritorna 'false' se l'aggiornamento non ? stato possibile. */ 
    public boolean updateElement(int numlinee, ElementoTime et)
    {
        ElementoTime proc;
        ElementoTime aux;
        int indice;
        int oldHeight;
        int newHeight;
        int offset;
        long tempo;
        
        if (lista == null)
        	return false;
        if (et == null) 
        	return false;
		if ((numlinee<1) || (numlinee>ElementoTime.maxlinee))
			return false;        
        indice = lista.indexOf(et);
        if (indice >= 0)
        {
//        	listener.informPreUpdate(this);
	    	proc = (ElementoTime)(lista.get(indice));
	    	oldHeight = proc.getMaxY()-proc.getMinY();
	    	proc.setLineNumber(numlinee);
	    	newHeight = proc.getMaxY()-proc.getMinY();
	    	if (newHeight != oldHeight)
	    	{
	    		offset = newHeight - oldHeight; 
	    		for (int i=indice; i<lista.size(); i++)
	    		{
	    			aux = (ElementoTime)(lista.get(i));
	    			aux.addGraphOffset(offset);	
	    		}
	    	}
//			listener.informaPostUpdate(this);	    		 
			return true; 
        }
        return false;
    }
	

    /** Aggiorna l'attributo time (con tempo) dell'elemento nella lista coincidente 
    	con il parametro et_ Ritorna false se l'aggiornamento non ? possibile. */
    public boolean updateElementTime(long tempo, ElementoTime et)
    {
        int indice;
        
        if (lista == null) return false;
        if (et == null) return false;
        if (tempo < 0) return false;
        indice = lista.indexOf(et);
        if (indice >= 0)
        {
        	// Il nuovo valore deve essere minore del 'time' dell'elemento che segue.
        	if (indice != ((lista.size())-1))
        	{
        		if (tempo >= ((ElementoTime)(lista.get(indice+1))).getTime())
        		{
        			return false;
        		}
        	}
        	// Il nuovo valore deve essere maggiore del 'time' dell'elemento che precede.
        	if (indice != 0)
        	{
        		if (tempo <= ((ElementoTime)(lista.get(indice-1))).getTime())
        		{
        			return false;
        		}
        	}
        	((ElementoTime)(lista.get(indice))).setTime(tempo);
        	return true;
        }
        return false;
    }
    	

    /** Aggiorna l'attributo time (con tempo) dell'elemento nella lista coincidente 
    	con il parametro et, quindi aggiorna l'attributo time di tutti gli elementi
    	che seguono sommandoci la differenza "tempo-(et.getTime())".
    	Ritorna false se l'aggiornamento non ? possibile. */
    public boolean updateElementTimeTwo(long tempo, ElementoTime et)
    {
        ElementoTime proc;
        int indice;
        long differenza;
        long oldTime;
        
        if (lista == null) return false;
        if (et == null) return false;
        if (tempo < 0) return false;
        indice = lista.indexOf(et);
        if (indice >= 0)
        {
        	// Il nuovo valore deve essere maggiore del 'time' dell'elemento che precede.
        	if (indice != 0)
        	{
        		if (tempo <= ((ElementoTime)(lista.get(indice-1))).getTime())
        		{
        			return false;
        		}
        	}
        	differenza = tempo - (et.getTime());
        	((ElementoTime)(lista.get(indice))).setTime(tempo);
        	// Aggiornamento degli elementi che seguono.
        	for (int i=(indice+1); i<lista.size(); i++)
        	{
        		proc = (ElementoTime)(lista.get(i));
        		oldTime = proc.getTime();
        		proc.setTime(oldTime+differenza);	
        	}
        	return true;
        }
        return false;
    }
    

    /** Inserisce nella lista un nuovo elemento subito prima di quello
    	passato come parametro, assegnando a time il valore tempo. */
    public boolean addElementTimeFirst(long tempo, ElementoTime et)
    {
        ElementoTime proc;
        int indice;
        
        if (lista == null) return false;
        if (et == null) return false;
        if (tempo < 0) return false;
        indice = lista.indexOf(et);
        if (indice >= 0)
        {
        	// Il valore di tempo deve essere minore del time di et.        	
        	if (tempo >= (et.getTime()))
        	{
        		return false;
        	}
        	// Il nuovo valore deve essere maggiore del time dell'elemento che precede.
        	if (indice != 0)
        	{
        		if (tempo <= ((ElementoTime)(lista.get(indice-1))).getTime())
        		{
        			return false;
        		}
        	}
        	// Creazione ed aggiunta del nuovo elemento.
        	proc = new ElementoTime(tempo,et.getMinY()+40,et.getMinY(),stringIsVisible,lineIsVisible, listener);
    		// proc.setLineVisible(lineIsVisible);
    		// proc.setStringVisible(stringIsVisible);        	
        	lista.add(indice,proc);
        	// Aggiornamento delle caratteristiche grafiche degli elementi che seguono.
        	for (int i = indice+1; i<lista.size(); i++)
        	{
        		proc = (ElementoTime)(lista.get(i));
        		proc.addGraphOffset(40);
        	}
        	return true;
        }
        return false;
    }

     
    /** Restituisce l'elemento con il time specificato, null se non e' presente. */
    public ElementoTime getElement(long tempo) 
    {
        ElementoTime et;
        
        if (lista==null) return null;
        if (tempo < 0) return null;
        for (int i=0;i<lista.size();i++)
        {
 	    	et = (ElementoTime)(lista.get(i));
	    	if (tempo == et.getTime()) 
	    	{
	    		return et;
			}
    	}
    	return null;
    }     
 

    /** Rimuove dalla lista l'elemento con il time specificato. */
    public boolean removeElement(long tempo) 
    {
        ElementoTime proc;
        ElementoTime aux;
        int offset;
        
        if (lista == null) return false;
        if (tempo < 0) return false;         
        for (int i=0;i<lista.size();i++) 
		{
	    	proc = (ElementoTime)(lista.get(i));
	    	if (tempo == proc.getTime()) 
        	{
                // Aggiornamento delle propriet? grafiche
                // degli elementi che seguono.                
                offset = proc.getMinY()-proc.getMaxY();
                for (int j=i+1; j<lista.size(); j++)
                {
                	aux = (ElementoTime)(lista.get(j));
                	aux.addGraphOffset(offset);	
                }
                
                lista.remove(i);
				listener.notifyRemove(proc);
				return true;
	    	}
        }
        return false;
    }


    /** Rimuove dalla lista l'elemento passato come parametro. */
    public boolean removeElement(ElementoTime et) 
    {
        ElementoTime proc;
        ElementoTime aux;
        int offset;
                
        if (lista == null) return false;
        if (et == null) return false;         
        for (int i=0;i<lista.size();i++) 
		{
	    	proc = (ElementoTime)(lista.get(i));
	    	if (et.getTime() == proc.getTime()) 
        	{
                // Aggiornamento delle propriet? grafiche
                // degli elementi che seguono.
                offset = proc.getMinY()-proc.getMaxY();
                for (int j=i+1; j<lista.size(); j++)
                {
                	aux = (ElementoTime)(lista.get(j));
                	aux.addGraphOffset(offset);	
                }        		
                lista.remove(i);
				listener.notifyRemove(proc);
				return true;
	    	}
        }
        return false;
    }

 
    /** Rimuove tutti gli elementi inseriti nella lista. */
    public void removeAll() 
    {
        if (lista == null) return;
		Iterator ite = lista.iterator();
		while (ite.hasNext()){
			//lista.remove(ite.next());
			this.removeElement((ElementoTime)ite.next());
			ite = lista.iterator();
		}
        
        //lista.removeAll(lista);
    } 

    
    /** Controlla se il punto specificato si trova all'interno di uno dei
    	rettangoli rappresentanti gli elementi_ In caso affermativo viene 
    	restituito in output l'elemento, altrimenti viene restituito null. */
    public ElementoTime getElement(Point p) 
    {
        ElementoTime et = null;
        
        if (lista == null) return null;
        if (p == null) return null;
        try 
        {
            for (int i=0;i<lista.size();i++) 
            {
                et = (ElementoTime)(lista.get(i));
                if (et != null)
	            	if (et.isInTime(p)) 
                		return et;
            }
        } 
        catch (IndexOutOfBoundsException e) 
        {
 	    	String s = "Indice fuori dai limiti ammessi \n dentro la classe ListaTime$getElement. \n" + e.toString()+"\n Il programma sar? terminato!";
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }    
        return null;
    }
 
     
    /** Stampa (visualizza) l'intera lista. */
    public void paintLista(Graphics2D g2D)
    {
        if (lista != null)
        {
        	for (int i=0; i<lista.size(); i++)
        	{
            	((ElementoTime)lista.get(i)).paintElementoTime(g2D);
        	}
    	}
    }
    
    
    /** Verifica se la lista ? vuota. */
    public boolean isEmpty()
    {
    	return lista.isEmpty();
    }   
    

    /** Clonazione dell'oggetto. 

     * */
    public ListaTime cloneListaTime()
    {
    	ElementoTime tmpElementoTime = null;
    	ElementoTime clonedElementoTime = null;
    	ListaTime cloned = new ListaTime(listener.getPlugData());
    	int j=0;
    	
    	cloned.setLineVisible(lineIsVisible);
    	cloned.setStringVisible(stringIsVisible);
    	while (j<lista.size())
    	{
    		tmpElementoTime = (ElementoTime)(lista.get(j));
    		clonedElementoTime = tmpElementoTime.cloneTime();
    		(cloned.lista).add(clonedElementoTime);
    		j++;	
    	}
    	return cloned;   	
    }


	/** Restituisce l'elemento della lista avente
		come indice il valore passato in ingresso. */
	public ElementoTime getAtIndex(int i)
	{
		return (ElementoTime)(lista.get(i));
	}        


	/** Metodo per verificare se la stringa con il 'time'
		della fascia temporale viene visualizzata. */	
	public boolean isStringTimeVisible()
	{
		return stringIsVisible;
	}


	/** Metodo per verificare se le linee all'interno 
		della fascia temporale sono visualizzate. */
	public boolean isLineTimeVisible()
	{
		return lineIsVisible;
	}


	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
	public void restoreFromFile()
	{
		for (int i=0; i<lista.size(); i++)
		{
			((ElementoTime)(lista.get(i))).restoreFromFile();
		}
	}		


	public void setLine(int larghezza)
	{
		for (int i=0; i<lista.size(); i++)
		{
			((ElementoTime)(lista.get(i))).setLine(larghezza);
		}		
	}
	
	
	/**
	 * aggiunta di un time listener  
	 */
	public void addListener(IListaTimeListener list){
		listener.add(list);
	}
	
	
	/**
	 * rimozione di un time listener  
	 */
	public void removeListener(IListaTimeListener list){
		listener.remove(list);
	}
	
	/**
	 * rimozione di tutti i listener  
	 */
	public void removeAllListener(){
		listener.removeAllElements();
	}


	/**
	 * @return iteratore della lista
	 */
	public Iterator iterator() {

		return lista.iterator();
	}
	
	
	/**
	 * restituisce l'elemento identificato dal codice id
	 * @param id codice elemento
	 * @return ElementoTime se esiste, oppure null
	 */
	public ElementoTime getElementoById(long id){
		
		for(int i = 0; i<lista.size(); i++){
			ElementoTime et = (ElementoTime )lista.get(i);
			if(et.getId()==id){
				return et;
			}
			
		}
		return null;
	}	
}