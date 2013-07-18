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

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import plugin.sequencediagram.data.delegate.DelegateListaClasseListener;
import plugin.sequencediagram.graph.GraficoTipoSequence;
import plugin.sequencediagram.pluglistener.IListaClasseListener;
import plugin.topologydiagram.resource.data.ElementoProcessoStato;
import plugin.topologydiagram.resource.data.ListaProcessoStato;
import core.internal.runtime.data.IPlugData;

/** Lista delle classi inserite in un Sequence Diagram. */

public class ListaClasse 
	extends ListaProcessoStato 
	implements Serializable
{
 
 
	/**
	 * delega per la gestione degli eventi
	 */
	private DelegateListaClasseListener delegateListener = null;
	   
	   
    /** Costruttore. 
     *  @param contenitore dei dati
     * */
    public  ListaClasse(IPlugData plugData) {
   		super(null, plugData);
		setDelegateListener(new DelegateListaClasseListener(plugData,this));
    }
 
	
	/** Metodo vuoto_ Necessario perch� definito
		come astratto in 'ListaProcessoStato'. */
	public boolean addPasteElement(ElementoProcessoStato eps){
		return true;
	}
	
	/** Clonazione dell'oggetto. 
	  */    
	public ListaClasse cloneListaClasse()
	{
		ElementoClasse tmpClassElement = null;
		ElementoClasse clonedElementoClasse = null;
		ListaClasse cloned = new ListaClasse(plugData);
		int j=0;    	
		while (j<lista.size()){
			tmpClassElement = (ElementoClasse)(lista.get(j));
			clonedElementoClasse = tmpClassElement.cloneClasse();
			(cloned.lista).add(clonedElementoClasse);
			j++;	
		}
		return cloned;     	
	}	

	 
    /** Controlla se il punto specificato si trova all'interno di uno dei
     *	rettangoli rappresentanti gli elementi_ In caso affermativo viene 
     *	restituito in output l'elemento, altrimenti viene restituito null.
     *	@param Punto da controllare
     * */
    public ElementoClasse getLineElement(Point p) 
    {
        ElementoClasse eps = null;
        if (lista == null) 
        	return null;
        if (p == null) 
        	return null;
        try{
            for (int i=0;i<lista.size();i++){
                eps = (ElementoClasse)lista.get(i);
                if (eps != null)
	            	if (eps.isInLine(p.x, p.y)) 
                		return eps;
            }
        } 
        catch (IndexOutOfBoundsException e) {
 	    	String s = "Indice fuori dai limiti ammessi \n dentro la classe ListaClasse$getProcess. \n" + e.toString()+"\n Il programma sar� terminato!";
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }    
        return null;
    }
    
    
    /** Restituisce il vettore contenente i nomi di tutti i processi definiti_  
     *  Nel vettore non sono inclusi i nomi dei processi 'dummy'. 
     * @return LinkedList of String else LinkedList with size = 0  
     */
    public LinkedList getListaAllClassName()
    {
        ElementoClasse ep;
        String nome;
        LinkedList vettore;

        vettore = new LinkedList();
        if (lista==null) 
        	return vettore;
        if (lista.isEmpty() == true) 
        	return vettore;
        try{ 
            for (int i=0;i<lista.size();i++){
	        	ep = (ElementoClasse)(lista.get(i));
            	nome = ep.getName();
	            vettore.add(nome);
	    	} 
        } 
        catch (NullPointerException e) {
 	    	String s = "Puntatore nullo dentro \n la classe ListaClasse$getAllProcessName().\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return vettore;
    } 
    

	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */    
    public void restoreFromFile()
    {
    	GraficoTipoSequence grafico1;
		ElementoClasse proc;
		
		if (lista!=null){
			for (int i=0; i<lista.size(); i++){
				proc = (ElementoClasse)(lista.get(i));
				grafico1 = (GraficoTipoSequence)(proc.getGrafico());
				grafico1.restoreFromFile();
			}
    	} 
    }       


	public void setLine(int altezza)
	{
		ElementoClasse ec;
		for(int i=0; i<lista.size(); i++){
			ec = (ElementoClasse)(lista.get(i));
			ec.setLine(altezza);
		}
	}
			 	 
			 	 
			 	 
	/**
	 * ritorna la classe di delega listener
	 * @return
	 */
	public DelegateListaClasseListener getDelegateListener() {
		return delegateListener;
	}

	/**
	 * setta la gestione dei listener per i cambiamenti dei dati
	 * attenzione, � una operazione che pu� disabilitare 
	 * gli ascoltatori precedenti
	 * @param listener
	 */
	public void setDelegateListener(DelegateListaClasseListener listener) {
		delegateListener = listener;
		listener.setListaClasse(this);
		setListaNotify(delegateListener);
	}		 	 
			 	 

	/**
	 * aggiunta di un listener per la lista
	 * @param ilpl
	 */
	public void addListener(IListaClasseListener ilpl) {
		this.delegateListener.add(ilpl);
	}


	/**
	 * rimozione di   un listener per la lista
	 * @param ilpl
	 */
	public void removeListener(IListaClasseListener ilpl){
		this.delegateListener.removeElement(ilpl);
	}


	/**
	 * rimuove tutti i listener registrati
	 *
	 */
	public void removeAllListener(){
		this.delegateListener.removeAllElements();
	}	 	 
			 	    
}