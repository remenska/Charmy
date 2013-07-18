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

import java.util.LinkedList;

import plugin.ba.Node;
import plugin.ba.Transition;
import plugin.ba.Buchi;
import plugin.ba.ListTransition;
import plugin.ba.ListNode;
import plugin.ba.PSC2BA;

import java.util.Iterator;

import javax.swing.JOptionPane;

/**
 *
 * @author  FLAMEL 2005
 */

/**
 *
 * implementa una lista di archi mantenuta dall'automa 
 */

public class ListTransition {
    
    /** Memorizza una lista collegata di oggetti della classe Transition. */
     public LinkedList lista;
     
       /** Costruttore. */
    public ListTransition() 
    {
        lista = new LinkedList();
    }
    
    /** Aggiunge un nuovo elemento alla lista. */
    public boolean addElement(Transition nd)
    {
    	if (nd==null) return false;
    	if (lista==null) return false;
        if (nd==null) 
        	return false;
        if (lista==null) 
        	return false;
        
        if(lista.contains(nd)){
        	return true; //gia c'è
        }    
        return lista.add(nd);
        
    }
    
    /** Restituisce l'elemento all'indice specificato, ma
    	genera un errore se l'indice non appartiene alla lista. */
    public Transition get(int i)
    {
        return (Transition) lista.get(i);
    }
    
    public Transition getFromId(long id)
    {
        for(int i=0;i<lista.size();i++)
        {
            if(((Transition)lista.get(i)).getId() == id)
                return (Transition) lista.get(i);
        }
        return null;
    }
    
    
    public ListTransition getTransNode(Node sender)
    {
        ListTransition ls = new ListTransition();
        for(int i=0;i<lista.size();i++)
        {
            if(((Transition)lista.get(i)).getNodeIndex().getId() == sender.getId())
                ls.addElement((Transition)lista.get(i));
        }
        return ls;
    }
    
    public boolean hasSelfTrans(Node nd)
    {
        for(int i=0;i<lista.size();i++)
        {
            Transition tr =(Transition) lista.get(i);
            if(tr.getNodeIndex().equals(nd) && tr.getNodeTarget().equals(nd))
                return true;
        }
        return false;
    }
    
    public Transition getSelfTrans(Node nd)
    {
        for(int i=0;i<lista.size();i++)
        {
            Transition tr =(Transition) lista.get(i);
            if(tr.getNodeIndex().equals(nd) && tr.getNodeTarget().equals(nd))
                return tr;
        }
        return null;
    }
    
    public boolean contain(Transition tr)
    {
        return lista.contains(tr);        
    }
    
    /** Rimuove l'operatore specificato come parametro. */
    public boolean removeElement(Transition nd) 
    {
        int i;
        if (lista==null) 
        	return false; 
        if (lista.isEmpty() == true) 
        	return false;
        try{ 
            i = lista.indexOf(nd); 
        } 
        catch (IndexOutOfBoundsException e){
 	    	String s = "Indice fuori dai limiti ammessi \ndentro la classe ListaCnale$removeChannel.\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        lista.remove(i); 
        return true;	   
    }
    
    
    /** Rimuove l'operatore specificato come parametro. */
    public boolean removeElement(int i) 
    {
        if (lista==null) 
        	return false; 
        if (lista.isEmpty() == true) 
        	return false;
       lista.remove(i);	
       return true;
    }
    
    /** Restituisce il numero di elementi memorizzati nella lista. */
    public int size() 
    {
        return lista.size();
    }
    
    
    public void deleteTransitions(Node Index,Node Target,String label)
    {
        for(int i=0;i<lista.size();i++)
        {
            if(((Transition)lista.get(i)).getNodeIndex().equals(Index)
                && ((Transition)lista.get(i)).getNodeTarget().equals(Target) 
                && ((Transition)lista.get(i)).getLabel().equals(label))
                    lista.remove(i); 
        }   
    }
}

