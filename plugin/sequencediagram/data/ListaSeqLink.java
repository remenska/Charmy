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
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import plugin.sequencediagram.data.delegate.DelegateListaSeqLinkListener;
import plugin.sequencediagram.graph.GraficoCollegamentoSeqLink;
import plugin.sequencediagram.graph.GraficoLoopSeqLink;
import plugin.sequencediagram.pluglistener.IListaSeqLinkListener;
import plugin.topologydiagram.resource.data.ElementoCanaleMessaggio;
import plugin.topologydiagram.resource.data.ListaCanaleMessaggio;
import plugin.topologydiagram.resource.data.ListaProcessoStato;
import core.internal.runtime.data.IPlugData;

/** Questa classe e' utilizzata per memorizzare la lista 
    dei link tra le classi di un Sequence Diagram. */

public class ListaSeqLink extends ListaCanaleMessaggio implements Serializable
{  
    
	/**
	 * delega per la gestione degli eventi
	 */
	DelegateListaSeqLinkListener delegateListener = null;
	
    /** Costruttore. 
     * @param plugData riferimento al contenitore dei dati
     * */
    public ListaSeqLink(IPlugData plugData) 
    {
  		super(plugData);
		setDelegateListener(new DelegateListaSeqLinkListener(this, plugData));
    } 
 

	/**	Metodo vuoto_ Necessario perch? dichiarato
		come astratto in 'ListaCanaleMessaggio'. */
	public boolean addPasteElement(ElementoCanaleMessaggio chn, ListaProcessoStato lprc)
	{
		return true;
	}
	

    /** Aggiunge un nuovo elemento alla lista.
     * ridefinisce il metodo addElement di listaCanaleMessaggio
     *  */
    public boolean addElement(ElementoCanaleMessaggio cnl)
    {
        if (cnl==null) return false;
        if (lista==null) return false;
        
        if(lista.contains(cnl)){
        	return true;
        }
        
        cnl.setPosizione(0);	    	
        boolean bo = lista.add(cnl);
        if(bo){  //modifica per listener
        	delegateListener.notifyAdd(cnl);
        	cnl.setUpdateEp(delegateListener);
        }
        return bo;
    }
    
    
    /** Aggiorna la posizione di tutti i canali interessati
    	dall'oggetto passato come parametro d'ingresso. */
    public void updateListaCanalePosizione(ElementoTime et)
    {
        ElementoSeqLink tmpCanale;
        
        for (int i=0; i<lista.size(); i++)
        {
            tmpCanale = (ElementoSeqLink)lista.get(i);
            if ((et.equals(tmpCanale.getTime_one())) || (et.equals(tmpCanale.getTime_two())))
            {
                tmpCanale.updateCanalePosizione();
            }
        }
    }    
    
    	
    /** Aggiorna la posizione di tutti i canali interessati
    	dall'oggetto passato come parametro d'ingresso. */
    public void updateListaSeqLinkPosizione()
    {
        ElementoSeqLink tmpCanale;
        
        for (int i=0; i<lista.size(); i++)
        {
            tmpCanale = (ElementoSeqLink)lista.get(i);
            tmpCanale.updateCanalePosizione();
        }
    } 
    
        
	/** Clonazione dell'oggetto. */    
    public ListaSeqLink cloneListaLink(ListaClasse lc, ListaTime lt)
    {
    	ElementoSeqLink tmpElementoSeqLink = null;
    	ElementoSeqLink clonedElementoSeqLink = null;
    	ListaSeqLink cloned = new ListaSeqLink(plugData);
    	int j=0;
    	
    	while (j<lista.size())
    	{
    		tmpElementoSeqLink = (ElementoSeqLink)(lista.get(j));
    		clonedElementoSeqLink = tmpElementoSeqLink.cloneSeqLink(lc,lt);
    		(cloned.lista).add(clonedElementoSeqLink);
    		j++;	
    	}
    	return cloned;     	
    }   

    

    /** Stampa tutti gli oggetti della lista. */
    public void paintLista(Graphics2D g2D)
    {
    	for (int i=0; i<lista.size(); i++)
    	{
    		ElementoSeqLink esl=(ElementoSeqLink)lista.get(i);
    		esl.paintCanale(g2D);
    		if(esl.getPrec()!=null)
    		{
    			if(esl.isStrict())
    			{       
    				if(esl.getFlussoDiretto())
    					esl.creaGraficoStrict(esl.getPointStart().x, esl.getPointStart().y,esl.getPointStart().x,esl.getPrec().getPointEnd().y,g2D);
    				
    				else
				        esl.creaGraficoStrict(esl.getPointEnd().x, esl.getPointEnd().y,esl.getPointEnd().x,esl.getPrec().getPointEnd().y,g2D);
    				
    			}  
    
                        
    		}
    		else if(esl.isStrict()){
    				if(esl.getFlussoDiretto())
    					esl.creaGraficoStrict(esl.getPointStart().x, esl.getPointStart().y,esl.getPointStart().x,esl.getProcY(),g2D);
    				
    				else
					esl.creaGraficoStrict(esl.getPointEnd().x, esl.getPointEnd().y,esl.getPointEnd().x,esl.getProcY(),g2D);
    				
    			}
    	}
    }
    
    

    /** Rimuove tutti i link collegati all'oggetto preso in input. */
    public  void removeAllLink(ElementoTime proc) 
    {
        ElementoSeqLink canale;
        ElementoTime time_one;
        ElementoTime time_two;
        long tempo;
        int offset;
        int i=0;

        if (lista==null) return;
        if (proc==null) return;
        tempo = proc.getTime();
        offset = proc.getMaxY() - proc.getMinY();
        try 
        {
            while (i<lista.size())
            {
                canale = (ElementoSeqLink)lista.get(i);
                if (canale != null)
	        	{
		    		time_one = canale.getTime_one();
		    		time_two = canale.getTime_two();
		    		if ((time_one == proc) ||
                                    (time_two == proc))
		    		{
                                      
	        			lista.remove(i);
						i = 0;
		        		continue;
		    		}
		    		else
		    		{
		    			if ((time_one.getTime()>tempo)||(time_two.getTime()>tempo))
		    			{
		    				canale.updateCanalePosizione();
		    			}
		    		}
	        	}    
        	i++;
	    	}
        } 
        catch (IndexOutOfBoundsException e) 
        {
 	    	String s = "Indice fuori dai limiti ammessi \n dentro la classe ListaSeqLink$removeAllLink().\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return;
        }
    }


	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
    public void restoreFromFile()
    {	
    	GraficoCollegamentoSeqLink grafico1;
    	GraficoLoopSeqLink grafico2;
		ElementoSeqLink msg;
		int tipomessaggio = 0;
		boolean flusso;
		
		if (lista!=null)
		{
			for (int i=0; i<lista.size(); i++)
			{
				msg = (ElementoSeqLink)(lista.get(i));
				//tipomessaggio = msg.getTipo();
							
				
				tipomessaggio = msg.getPosizione();
				
				flusso = msg.getFlussoDiretto();				
				if (msg.ctrlIfLoop())
				{
					grafico2 = (GraficoLoopSeqLink)(msg.getGrafico());
					grafico2.restoreFromFile(tipomessaggio,flusso);
				}
				else
				{				
					grafico1 = (GraficoCollegamentoSeqLink)(msg.getGrafico());
					grafico1.restoreFromFile(tipomessaggio,flusso);
				}
//			
			}
    	}  
	}


	/** Restituisce true se il messaggio nomeC ? asincrono. */
	public boolean isAsynchronous(String nomeC)
	{
		boolean async = false;
		for (int i=0; i<lista.size(); i++)
    	{
	 		ElementoSeqLink link = (ElementoSeqLink)(lista.get(i));
	 		if (nomeC.equalsIgnoreCase(link.getName()))
	 		{
	    		if (link.getTipo()==ElementoSeqLink.ASYNCHRONOUS) 
		   		{
		    		async = true;
					break;
		   		}
		   	}
    	}
		return async;		
	}


	/** Restituisce true se il messaggio nomeC ? sincrono. */
	public boolean isSynchronous(String nomeC)
	{
		boolean sync = false;
		for (int i=0; i<lista.size(); i++)
    	{
	 		ElementoSeqLink link = (ElementoSeqLink)(lista.get(i));
	 		if (nomeC.equalsIgnoreCase(link.getName()))
	 		{
	    		if (link.getTipo()==ElementoSeqLink.SYNCHRONOUS) 
		   		{
		    		sync = true;
					break;
		   		}
		   	}
    	}
		return sync;
	}
	


	public LinkedList getListLinkSequence()
	{
		return lista;		
	}
			 
			 
	/**
	 * aggiunta di un listener per la lista
	 * @param ilpl
	 */
	public void addListener(IListaSeqLinkListener ilpl){
		this.delegateListener.add(ilpl);
	}

	/**
	 * rimozione di   un listener per la lista
	 * @param ilpl
	 */
	public void removeListener(IListaSeqLinkListener ilpl){
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
	 * ritorna la classe di delega per la gestione degli eventi
	 * @return
	 */
	public DelegateListaSeqLinkListener getDelegateListener() {
		return delegateListener;
	}

	/**
	 * setta la delega per la gestione degli eventi
	 * @param listener
	 */
	public void setDelegateListener(DelegateListaSeqLinkListener listener) {
		delegateListener = listener;
		listener.setListaSeqLink(this);
		setNotify(delegateListener);  //super
	}	      
	
	
	//ezio 2006 - serve al testor - tutto il metodo
	/**
	 * Restituisce un iterator sulla lista dei messaggi, l'ordinamento è dato dalla consecutività temporale dei messaggi.
	 * @return
	 */
	public Iterator iteratorTemporal(){
		
		
	LinkedList listTemp = (LinkedList)lista.clone();
	LinkedList listReturn = new LinkedList();
	
	while(listTemp.size()!=0){
		int pos = 0;
		long controllo = ((ElementoSeqLink)(listTemp.get(0))).getTimeFrom().getTime();
		for (int i=0; i<listTemp.size(); i++)
		{
			long lineNumberCorr =  ((ElementoSeqLink)(listTemp.get(i))).getTimeFrom().getTime();
			
	 		if(lineNumberCorr<controllo){
	 			pos=i;
	 			controllo=lineNumberCorr;
	 		}
	 		
		}
		
		listReturn.add(listTemp.get(pos));
		listTemp.remove(pos);
		
	}
	
		return listReturn.iterator();
		
	}
	
	
	//ezio 2006 - serve per correggere un bug nell'editor
	/**
	 * 
	 * @param time
	 * @return
	 */
	public ElementoSeqLink getLinkAtTime(long time){
		
		for (int i=0; i<lista.size(); i++)
		{
			ElementoSeqLink linkCorr = (ElementoSeqLink)lista.get(i);
			if (linkCorr.getTimeFrom().getTime()==time)
				return linkCorr;
			
		}
		
		return null;
	}
	
}