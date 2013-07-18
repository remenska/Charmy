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
    


package plugin.statediagram.data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import core.internal.runtime.data.IPlugData;

import plugin.statediagram.data.delegate.DelegateListaMessaggioListener;
import plugin.statediagram.graph.*;
import plugin.statediagram.pluglistener.IListaMessaggioListener;
import plugin.statediagram.pluglistener.IThreadElementListener;

import plugin.statediagram.eventi.thread.AddThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.AddThreadStatoEvento;
import plugin.statediagram.eventi.thread.RemoveThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.RemoveThreadStatoEvento;
import plugin.statediagram.eventi.thread.UpdateThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.UpdateThreadStatoEvento;
import plugin.topologydiagram.resource.data.ListaCanaleMessaggio;
import plugin.topologydiagram.resource.data.ListaProcessoStato;
import plugin.topologydiagram.resource.graph.GraficoCollegamento;
import plugin.topologydiagram.resource.graph.GraficoLoop;

/** Questa classe e' utilizzata per memorizzare la lista 
    dei link tra gli stati di uno State Diagram. */

public class ListaMessaggio 
	extends ListaCanaleMessaggio 
	implements Serializable,  IThreadElementListener
{
    /**
	 * delega per la gestione degli eventi
	 */
	DelegateListaMessaggioListener delegateListener = null;
	
	  
    /** Costruttore. 
     * @param Contenitore dei dati
     * */
    public ListaMessaggio(IPlugData plugData) 
    {
  		super(plugData);
  		this.plugData=plugData;
  		/*
  		 * cambiamento per gestione msg
  		 */
		setDelegateListener(new DelegateListaMessaggioListener(plugData,this));
    } 
 

	/** Clonazione dell'oggetto. 
	  */    
    public ListaMessaggio cloneListaMessaggio(ListaProcessoStato lprc)
    {
    	ElementoMessaggio tmpElementoMessaggio = null;
    	ElementoMessaggio clonedElementoMessaggio = null;
    	ListaMessaggio cloned = new ListaMessaggio(plugData);
    	int j=0;    	
    	while (j<lista.size()){
    		tmpElementoMessaggio = (ElementoMessaggio)(lista.get(j));
    		clonedElementoMessaggio = tmpElementoMessaggio.cloneMessaggio();
    		(cloned.lista).add(clonedElementoMessaggio);
    		j++;	
    	}
    	return cloned;     	
    }	


	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
    public void restoreFromFile()
    {	
    	GraficoCollegamento grafico1;
    	GraficoLoop grafico2;
    	ElementoMessaggio msg;
    	int tipomessaggio = 0;
    	boolean flusso;
    	if (lista!=null){
    		for (int i=0; i<lista.size(); i++){
    			msg = (ElementoMessaggio)(lista.get(i));    		
    			tipomessaggio = msg.getPosizioneCanMess();
    			flusso = msg.getFlussoDiretto();		
    			if (msg.ctrlIfLoop()){
    				grafico2 = (GraficoLoop)(msg.getGrafico());
    				grafico2.restoreFromFile(tipomessaggio,flusso);
    			}
    			else{				
    				grafico1 = (GraficoCollegamento)(msg.getGrafico());
    				grafico1.restoreFromFile(tipomessaggio,flusso);
    			}
    		}
    	}  
	}
	
	public LinkedList getListaNomeSendMessaggio(){
		LinkedList risultato= new LinkedList();
		ElementoMessaggio canale;
		for(int i=0;i<lista.size();i++){
			canale=(ElementoMessaggio)lista.get(i);
			if(canale.getSendReceive()==ElementoMessaggio.SEND){
				risultato.add(canale.getName());
			}			
		}		
		return risultato;
	}

	public LinkedList getTauMessages(){
		LinkedList risultato= new LinkedList();
		ElementoMessaggio canale;
		for(int i=0;i<lista.size();i++){
			canale=(ElementoMessaggio)lista.get(i);
			if(canale.getSendReceive()==ElementoMessaggio.TAU){
				risultato.add(canale.getName());
			}			
		}		
		return risultato;
	}

	
	public LinkedList getListaNomeReceiveMessaggio(){
		LinkedList risultato= new LinkedList();
		ElementoMessaggio canale;
		for(int i=0;i<lista.size();i++){
			canale=(ElementoMessaggio)lista.get(i);
			if(canale.getSendReceive()==ElementoMessaggio.RECEIVE){
				risultato.add(canale.getName());
			}
		}		
		return risultato;
	}
	
	/** Restituisce la lista di tutti i messaggi collegati
		allo stato passato in ingresso. */
	public LinkedList getListaMessaggio(ElementoStato istStato)
	{
        LinkedList risultato = new LinkedList();
        ElementoMessaggio canale;
        int i=0;
        if (lista==null) return risultato;
        try {
            while (i<lista.size()){
                canale = (ElementoMessaggio)(lista.get(i));
                if (canale != null){
		    		if ((((ElementoStato)(canale.getElement_one())) == istStato) ||
                        (((ElementoStato)(canale.getElement_two())) == istStato)){
	        			risultato.add(canale);
		    		}
	        	}    
        		i++;
	    	}
        } 
        catch (IndexOutOfBoundsException e) {
 	    	String s = "Indice fuori dai limiti ammessi \n dentro la classe ListaCanale$removeAllLink().\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return risultato;
        }		
		return risultato;
	}

	public void setUnselected(){
		Iterator ite = iterator();
		while (ite.hasNext()) {
			((ElementoMessaggio) ite.next()).setSelected(false);
		}
	}
	
	/**
	 * Prende La lista Dei messaggi
	 * @return
	 */
	public LinkedList getAllMessages(){			
		return lista;
	}	


	/**
	 * aggiunta di un listener per la lista
	 * @param ilpl
	 */
	public void addListener(IListaMessaggioListener ilpl){
		this.delegateListener.add(ilpl);
	}

	/**
	 * rimozione di   un listener per la lista
	 * @param ilpl
	 */
	public void removeListener(IListaMessaggioListener ilpl){
		this.delegateListener.removeElement(ilpl);
	}
	/**
	 * rimuove tutti i listener registrati
	 *
	 */
	public void removeAllListener(){
		this.delegateListener.removeAllElements();
	}


	/**
	 * inserisce un delegate listener, il metodo ? atomicp
	 * @param listener
	 */
	public void  setDelegateListener(DelegateListaMessaggioListener listener) {
		delegateListener = listener;
		delegateListener.setListaMessaggio(this);
		setNotify(delegateListener);
	}

	/**
	 * restituisce il Delegate Listener
	 * @return
	 */
	public DelegateListaMessaggioListener getDelegateListener() {
		return delegateListener;
	}


	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoAdded(eventi.thread.AddThreadStatoEvento)
	 */
	public void statoAdded(AddThreadStatoEvento event) {

	}


	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoRemoved(eventi.thread.RemoveThreadStatoEvento)
	 */
	/**
	 * unico evento interessante per poter togliere eventuali canali 
	 * collegati allo stato
	 */
	public void statoRemoved(RemoveThreadStatoEvento event) {
		removeAllLink(event.getElementoStato());
	}


	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoUpdate(eventi.thread.UpdateThreadStatoEvento)
	 */
	public void statoUpdate(UpdateThreadStatoEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioAdded(eventi.thread.AddThreadMessaggioEvento)
	 */
	public void messaggioAdded(AddThreadMessaggioEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioRemoved(eventi.thread.RemoveThreadMessaggioEvento)
	 */
	public void messaggioRemoved(RemoveThreadMessaggioEvento event) {

	}


	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioUpdate(eventi.thread.UpdateThreadMessaggioEvento)
	 */
	public void messaggioUpdate(UpdateThreadMessaggioEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#threadRefresh()
	 */
	public void threadRefresh() {
	}

}