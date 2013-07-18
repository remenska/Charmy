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
import java.util.LinkedList;

import javax.swing.DefaultListModel;

import plugin.sequencediagram.data.interfacce.IListaConstraintNotify;
import plugin.sequencediagram.graph.GraficoConstraint;
import plugin.statediagram.data.ListaDP;
import plugin.topologydiagram.resource.data.ImpUpdate;

/**
 *
 * @author  FLAMEL 2005
 */


/** Questa classe implementa i constraints. */

public class ElementoConstraint extends ImpUpdate implements Serializable
{	   
	
	
    /** Tempo riferito a element_one */
    private ElementoTime time;

    /** Variabile usata per indicare il link a cui viene applicato il constraint*/
    private ElementoSeqLink Link;

    /** Memorizza l'oggetto che gestisce la rappresentazione grafica del link. */
    private GraficoConstraint grafico;

    /** Variabile usata per memorizzare la label associata al constraint*/
    private String label="";

    private long instanceIde=0;

    /** Lista usata per memorizzare i messaggi inseriti nel constraint*/
    private LinkedList msg;
    
    /** Lista usata per memorizzare i messaggi inseriti nel constraint qualora siano catene*/
    private LinkedList msgPrintForChain;

    /** Variabile utilizzata per salvare se il link va inserito oppure no, fatto
        tramite la finestra di dialogo FinestraNuovoConstraint*/
    private boolean inserisciConstraint=false;

    //notifica degli update
    private IListaConstraintNotify  updateEp = null;

    private static int constraintInstanceNumber=0;

    private String constraintExpression = "";

    /**Memorizza il tipo di constraint
     -2 past open
     -1 past closed
     0 present
     1 future closed
     2 future open
     3 chain closed past
     4 chain open past
     5 chain closed fut
     6 chain open fut
     7 unwanted closed past
     8 unwanted open past
     9 unwanted closed fut
     10 unwanted open fut**/
    private int Type = 11;
    
    
    
    
          
    /** Costruttore. */ 
    public ElementoConstraint(
    		        ElementoSeqLink link, 
			String label,  
			String expression,
			boolean forClone,
			long id,
                        int type,
			IListaConstraintNotify  updateEp) 
    {
    	super();	

    	if (forClone){
    		instanceIde = id;
    	}
    	else{
    		instanceIde = incConstraintInstanceNumber();	
    	}
     	Link = link;
    	time = Link.getTimeFrom();
    	grafico = null;
    	this.label=label;
    	this.constraintExpression = expression;
    	this.updateEp = updateEp;
        Type=type;
     
        
    } 
    
    public static int getConstraintInstanceNumber()
    {
            return constraintInstanceNumber;
    }

    public static int incConstraintInstanceNumber()
    {
            return constraintInstanceNumber++;
    }

	///ezio 2006 - nuovo metodo, per reset apertura file
	public static void setInstanceNumber(int instance)
	{
		constraintInstanceNumber = instance;
	}
	//////
    
    public String getConstraintExpression(){
    	return constraintExpression;
    }
    
    public void setConstraintExpression(String expression){
    	this.constraintExpression = expression;
    }
    
    
    /**Setta il tipo di constraint**/
    public void setType(int i){
        Type = i;
    }
    
    /**Ritorna il tipo di constraint**/
    public int getType(){
        return Type;
    }
    
	/**
     * resitutisce l'identificatore
     * @return l'id
     */
    public long getId(){
    	return instanceIde;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isPastOp(){
        if(Type==-2)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isPastClo(){
        if(Type==-1)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isPre(){
        if(Type==0)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isFutClo(){
        if(Type==1)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isFutOp(){
        if(Type==2)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isChCloPast(){
        if(Type==3)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isChOpPast(){
        if(Type==4)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isChCloFut(){
        if(Type==5)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isChOpFut(){
        if(Type==6)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isUnCloPast(){
        if(Type==7)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isUnOpPast(){
        if(Type==8)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isUnCloFut(){
        if(Type==9)
            return true;
        else
            return false;
    }
    
    /**
     * resitutisce l'identificatore
     * @return l'id
     */
    public boolean isUnOpFut(){
        if(Type==10)
            return true;
        else
            return false;
    }
    
    
    /** Metodo per introdurre i messaggi nella LinkedList msg*/
    public void setMsg(DefaultListModel msg)
    {
        this.msg=null;
        this.msg=new LinkedList();
        for(int i=0;i<msg.size();i++)
            this.msg.add(msg.get(i));
    }
    
    /** Metodo per introdurre i messaggi nella LinkedList msgPrintForChain*/
    public void setMsgForChain(DefaultListModel msg)
    {
        this.msgPrintForChain = null;
        this.msgPrintForChain=new LinkedList();
        for(int i=0;i<msg.size();i++)
            this.msgPrintForChain.add(msg.get(i));
    }
    
    /** Metodo per recuparare i messaggi nella LinkedList msgPrintForChain*/
    public LinkedList getMsgForChain()
    {
        return msgPrintForChain;
    }
    
    /** Metodo per recuparare i messaggi nella LinkedList msg*/
    public LinkedList getMsg()
    {
        return msg;
    }
    
    
    /** Metodo inserito per inizializzare l-oggetto nel caso si usi il costruttore 
        senza paramentri.*/    
    public void start(ElementoSeqLink link, String label)
    {
        this.Link = link;
        time = Link.getTimeFrom();
        grafico = null;
        this.label=label;
        
    }
    
    
    /** set della variabile inserisciConstraint */
    public void setInserisciConstraint(boolean value)
    {
        inserisciConstraint=value;
    }

    /** get della variabile inserisciConstraint */
    public boolean getInserisciConstraint()
    {
        return inserisciConstraint;
    }
	
    /** Restituisce il tempo di inizio del constraint*/
    public ElementoTime getTimeFrom()
    {
            return time;
    }
     
    /** Restituisce il tempo di fine del constraint*/
    public ElementoTime getTimeTo()
    {
            return time;

    }

    /** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
    Serve per verificare se il punto 'p' � sufficientemente 
    vicino al constraint (vicino dal punto di vista grafico). */
    public boolean isSelezionato(Point p)
    {
        return grafico.isSelezionato(p);
    }
    
    
    /** Disegna il grafico associato al constraint. */
    public void paintConstraint(Graphics2D g2D)
    {
        if (grafico != null)
            grafico.paintGraficoConstraint(g2D);
    }
    
    /** Restituisce un riferimento al grafico del link. */
    public GraficoConstraint getGrafico()    
    {
        return grafico;
    }
  
        /** Seleziona/deseleziona il link. */
    public void setSelected(boolean ctrlSelection)
    {
        grafico.setSelected(ctrlSelection);
    }
    
     /** Restituisce il link. */
    public ElementoSeqLink getLink()    
    {
        return Link;
    }
   
    
    /** Verifica se il link � selezionato. */
    public boolean isSelected()
    {
        return grafico.isSelected();
    }
    
   
    
    /** Inverte lo stato selezionato/deselezionato del link. */
    public void invSelected()
    {
    	grafico.invSelected();
    } 
   
    
    /** Restituisce il nome del constraint. */
    public String getLabel()
    {
        return label;
    }
    
    /** Imposta il nome del constraint. */
    public void setLabel(String str)
    {
        label = str;    

   }  
    
    /** Imposta il nome del constraint. */
    public void setLink(ElementoSeqLink link)
    {
        Link = link;    
    }
   
    
    /** Crea l'oggetto grafico associato al constraint. */
    public void creaGraficoConstraint()
    {
        Point p1,p2;
        p1 = Link.getPointStart();
        p2 = Link.getPointEnd();
        
        if(Link.getFlussoDiretto())        
             grafico = new GraficoConstraint(p1.x,p1.y,p2.x,p2.y,"C"+instanceIde,this,Type,true);   
        else
             grafico = new GraficoConstraint(p1.x,p1.y,p2.x,p2.y,"C"+instanceIde,this,Type,false);
    }
    

    /** Aggiorna la posizione del link in funzione di
    	quella dell'elemento di arrivo e di partenza. */    
    public void updateConstraintPosizione()
    {
        Point p1,p2;
        p1 = Link.getPointStart();
        p2 = Link.getPointEnd();
        if(Link.getFlussoDiretto())
            grafico.updateConstraintPosizione(p1.x,p1.y,p2.x,p2.y,Type,true);
        else
            grafico.updateConstraintPosizione(p1.x,p1.y,p2.x,p2.y,Type,false);	
    }
    
    /**
     * crea un clone dell'elementoConstraint compreso l'identificatore
     * @author michele
     */
    
    public ElementoConstraint cloneConstraint()
    {
    	ElementoSeqLink Link = this.getLink();
        ElementoSeqLink Linkclon = Link.cloneSeqLink();
    	ElementoConstraint cloned;
    	cloned = new ElementoConstraint(Linkclon,this.getLabel(),this.getConstraintExpression(), true, getId(),Type, null);
    	cloned.creaGraficoConstraint();
    	return cloned;
    }         	   

    
    /**
     * ritorna la classe di ascolto update
     * @return
     */
    public IListaConstraintNotify getUpdateEp() {
    	return updateEp;
    }

    /**
     * setta la classe di ascolto update
     * @param updateEP
     */
    public void setUpdateEp(IListaConstraintNotify  updateEP) {
    	updateEp = updateEP;
    }

	/* (non-Javadoc)
	 * @see data.ImpUpdate#informPreUpdate()
	 */
	public void informPreUpdate() {
		if(sendMsg){  //posso inviare il messaggio
			if(updateEp != null){
				updateEp.informaPreUpdate(this.cloneConstraint());
			}
		}
	}

	/* (non-Javadoc)
	 * @see data.ImpUpdate#informPostUpdate()
	 */
	public void informPostUpdate() {
		if(sendMsg){  //posso inviare il messaggio
			if(updateEp != null){
				updateEp.informaPostUpdate(this.cloneConstraint());
			}
		}	
	}

	public String labelConstraintOr(ListaDP lDP)
	{
	    String lab="";
	    if(msg!=null)
	        if(msg.size()==1){
	        	if(lDP.getMessage((String)msg.get(0)).ctrlIfLoop())
	            	lab="(_lstTau==channelTau["+msg.get(0)+"])";
	        	else
					lab="(_lst==channel"+lDP.getNumParametersMessage((String)msg.get(0))+"["+msg.get(0)+"])";
	        }else{
	            for(int i=0;i<msg.size();i++){
	                if(i==0)
						if(lDP.getMessage((String)msg.get(i)).ctrlIfLoop())
							lab+="((_lstTau==channelTau["+msg.get(i)+"])";
						else
							lab+="((_lst==channel"+lDP.getNumParametersMessage((String)msg.get(i))+"["+msg.get(i)+"])";
	                    //lab+="((_lst==channel"+lDP.getNumParametersMessage((String)msg.get(i))+"["+msg.get(i)+"])";
	                else
						if(lDP.getMessage((String)msg.get(i)).ctrlIfLoop())
							lab+="||(_lstTau==channelTau["+msg.get(i)+"])";
						else
							lab+="||(_lst==channel"+lDP.getNumParametersMessage((String)msg.get(i))+"["+msg.get(i)+"])";
	                    //lab+="||(_lst==channel"+lDP.getNumParametersMessage((String)msg.get(i))+"["+msg.get(i)+"])";
	                if(i==msg.size()-1)
	                    lab+=")";
	            }
	        }
	    return lab;        
	}

	public String labelConstraint(ListaDP lDP)
	    {
	        String lab="";
	//      if((label!=null)&&(!label.equals(""))){
	//          lab=label;
	//          if(msg!=null)
	//              if(msg.size()!=0)
	//                  lab+="&&";
	//      }
	      if(msg!=null)
	          for(int i=0;i<msg.size();i++){
	              if(i==0){
						if(lDP.getMessage((String)msg.get(i)).ctrlIfLoop())
							lab+="(!(_lstTau==channelTau["+msg.get(i)+"])";
						else
							lab+="(!(_lst==channel"+lDP.getNumParametersMessage((String)msg.get(i))+"["+msg.get(i)+"])";
	                  //lab+="(!(_lst==channel"+lDP.getNumParametersMessage((String)msg.get(i))+"["+msg.get(i)+"])";
	              }else
						if(lDP.getMessage((String)msg.get(i)).ctrlIfLoop())
	//				TODO v2.0 modificato 18 settembre nome thread con spazi					
							lab+="&&(!(_lstTau==channelTau["+msg.get(i)+"]))";
						else
	//				TODO v2.0 modificato 18 settembre nome thread con spazi
							lab+="&&(!(_lst==channel"+lDP.getNumParametersMessage((String)msg.get(i))+"["+msg.get(i)+"]))";                
	                  //lab+="&&(!(_lst==channel"+lDP.getNumParametersMessage((String)msg.get(i))+"["+msg.get(i)+"])";
	              if(i==msg.size()-1)
	                  lab+=")";
	          }
	      return lab;
	    }
    
    
}