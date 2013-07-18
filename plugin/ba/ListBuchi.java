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

import javax.swing.JOptionPane;

/**
 * @author flamel 2005
 * Charmy plug-in
 * Lista per memorizzare i buchi 
 **/

public class ListBuchi {
    
    /** Memorizza una lista collegata di oggetti della classe Buchi. */
     protected LinkedList lista;
     
       /** Costruttore. */
    public ListBuchi() 
    {
        lista = new LinkedList();
    }
    
    
    /** Aggiunge un nuovo elemento alla lista. */
    public boolean addElement(Buchi nd)
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
    public Buchi get(int i)
    {
        return (Buchi) lista.get(i);
    }
    
    public LinkedList getAll()
    {
        return  lista;
    }
    
    
    /** Rimuove l'operatore specificato come parametro. */
    public boolean removeElement(Buchi nd) 
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
    
    /** Rimuove l'operatore specificato come parametro. */
    public boolean removeAllElement() 
    {
        if (lista==null) 
        	return false; 
        if (lista.isEmpty() == true) 
        	return false;
       lista = new LinkedList();	
       return true;
    }
    
    /** Restituisce il numero di elementi memorizzati nella lista. */
    public int size() 
    {
        return lista.size();
    }
}
