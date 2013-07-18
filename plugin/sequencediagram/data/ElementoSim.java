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

import javax.swing.JOptionPane;

import core.internal.plugin.file.FileManager;

import plugin.sequencediagram.SequenceEditor;
import plugin.sequencediagram.data.interfacce.IListaSimNotify;
import plugin.sequencediagram.graph.GraficoSim;
import plugin.topologydiagram.TopologyWindow;
import plugin.topologydiagram.resource.data.ImpUpdate;

/**
 *
 * @author  FLAMEL 2005
 */


/** classe che implementa l'operatore simultaneo*/

public class ElementoSim extends ImpUpdate implements Serializable
{

	
        /**Memorizza l'oggetto che gestisce la rappresentazione grafica dell'operatore. */
	private GraficoSim grafico;
        
        /**Memorizza l'oggetto sequence element di cui l'opertore fà parte**/
        private SequenceElement seqEle;
        
        /**Memorizza i messaggi dell'operatore**/
        private LinkedList list_mess;
        
        /**Memorizza i link del sequence di cui l'operatore fà parte**/
        private LinkedList list_link;
        
        
        /** Memorizza l'ascissa dell'estremo in alto a sinistra. */
        private int rettangoloX;
        
        /** Memorizza l'ordinata dell'estremo in alto a sinistra. */
        private int rettangoloY;
        
        private ElementoSeqLink link_first;        
        private ElementoSeqLink link_last;
        
        /**memorizza l'ampiezza dell'operatore*/
	private int sim_width;
        
        /**memorizza l'altezza dell'operatore*/
	private int sim_height;
        
        /**memorizza la cardinalità degli operatori bottom*/
	private int op_bot;
        
       /**memorizza la cardinalità degli operatori up*/
	private int op_up;
        
        /**memorizza la larghezza del link più ampio tra quelli appetibili**/
        private int widthmax = 0;
        
         /**memorizza la larghezza del link più ampio tra quelli appetibili**/
        private int heightmax = 0;
        
       //notifica degli update
	private IListaSimNotify  updateEp = null;
        
        private long instanceIde=0;
        
        //**Memorizza le istanze**//
        private static int SimInstanceNumber = 0;
        
        private Graphics2D g2D;
        
        /** Estremo superiore incluso/escluso */
	private boolean estremoSuperiore;
	
	/** Estremo inferiore incluso/escluso */
	private boolean estremoInferiore;
        
        /** Variabile usata per gestire due diversi tipi di operazione_
        ctrlOP = true: aggiunta di un nuovo Simo
        ctrlOP = false: modifica proprietï¿½ del Simo corrente. */
	private boolean ctrlOP;
        
       /**memorizano i valori prima che l'operatore venga modificato**/
        private int height_back;
        private int width_back;
        private int rettanY_back;
        private int rettanX_back;
        
        private String label;
    
         /** Costruttore. */ 
    public ElementoSim(LinkedList list,
                             SequenceElement seq,
                             long id,
			     IListaSimNotify  updateEp,
                             boolean forClone) 
    {
        
        if (forClone)
            instanceIde = id;   	
       else
            instanceIde = incSimInstanceNumber(); 
      seqEle = seq; 
      list_link = new LinkedList();
      list_link = seqEle.getListaSeqLink().getListLinkSequence();
      list_mess = new LinkedList();
      list_mess = list;
      link_last =(ElementoSeqLink) list_mess.getLast();
      link_first =(ElementoSeqLink) list_mess.getFirst();
      setSim_Height(); 
      setSim_point_width();
      setXYRet();
      grafico = null;
      // core.internal.plugin.file.FileManager.setModificata(true);
     //ezio 2006  SequenceEditor.incSimInstanceNumber();
      //ezio 2006  instanceIde = incSimInstanceNumber();  
      this.setLabel("sim"+instanceIde);
      
     // ezio 2006
      this.updateEp = updateEp;
      //
      
      height_back = sim_height;
      width_back = sim_width;
      rettanY_back = rettangoloY;
      rettanX_back = rettangoloX;
      setIdLink(); 
    }

    
    /** Crea l'oggetto grafico associato al Simultaneo. */
    public void creaGraficoSim()
    {
        boolean bo = this.testAndSet();

        grafico = new GraficoSim( rettangoloX,
                                       rettangoloY,
                                       sim_height,
                                       this.getSim_point_width(), 
                                       this);
    }
    
    /** Disegna il grafico associato all'operatore Simo. */
    public void paintSim(Graphics2D g2D)
    {   boolean bo = this.testAndSet();
        if(grafico!=null) 
            grafico.paintGraficoSim(g2D);
        testAndReset(bo);
    }
    
    /** Aggiorna la lista dei link presenti nel Sequence**/
    public void setlist_link(LinkedList list){
        boolean bo = this.testAndSet();
        list_link = list ;
        testAndReset(bo);
    }
    
    /**setta il suo id su tutti i suoi link **/
    public void setIdLink()
    {
        for(int i=0;i<list_mess.size();i++)
        {
           ((ElementoSeqLink) list_mess.get(i)).setIdSim(this.instanceIde);
        }
    }
    
    /** Restituisce il link nella posizione data. */
    public ElementoSeqLink get(int i)
    {
        return (ElementoSeqLink) list_mess.get(i);
    }

    /** Restituisce la dimensione dell'operatore. */
    public int size()
    {
        return  list_mess.size();
    }
        
    /** Ritorna l'ultimo messaggio dell'operatore **/
    public ElementoSeqLink getlast_link (){
        return link_last;
        
    }
    
    /** Ritorna il primo messaggio dell'operatore **/
    public ElementoSeqLink getfirst_link (){
        return link_first;
        
    }
    
    /** Setta il primo messaggio dell'operatore **/
    public void setFirst_link (){
        boolean bo = this.testAndSet();
        ElementoSeqLink app = (ElementoSeqLink) list_mess.getFirst();
        int i= app.getPointStart().y;
        for(int j=0;j<list_mess.size();j++){
            ElementoSeqLink link=(ElementoSeqLink) list_mess.get(j);
            if(link.getPointStart().y <= i)
                link_first = link;
            
        }
        testAndReset(bo);
    }
    
    /** Setta l'ultimo messaggio dell'operatore **/
    public void setLast_link (){
        boolean bo = this.testAndSet();
        ElementoSeqLink app = (ElementoSeqLink) list_mess.get(0);
        int i= app.getPointStart().y;
        for(int j=0;j<list_mess.size();j++){
            ElementoSeqLink link=(ElementoSeqLink) list_mess.get(j);
            if(link.getPointStart().y >= i)
                link_last = link;
        }
        testAndReset(bo);
    }
    
    /**Aggiunge un nuovo link all'operatore**/
    public boolean addElem(ElementoSeqLink link){
        boolean bo = this.testAndSet();        
        if(list_mess.contains(link)){
        	return true; //gia c'è
        }
        list_mess.add(link);
        link.setSimultaneous(true,this.getId());        
        testAndReset(bo);
        return true;
    }
    
    /**Rimuove un link all'operatore**/
    public boolean removeElem(ElementoSeqLink link){ 
        boolean bo = this.testAndSet();
        int i;
        if(list_mess.size()==2)
            return false;
        if (list_mess == null) 
        	return false; 
        try{ 
            i = list_mess.indexOf(link); 
        }
        catch (IndexOutOfBoundsException e){
 	    	String s = "Indice fuori dai limiti ammessi \ndentro la classe ElementoSimo&removeElem.\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return false;
        }
       link.setSimultaneous(false,0);       
       list_mess.remove(i);
       testAndReset(bo);
       return true;
       
    }
    
    /**Aggiunge un nuovo link alla lista mantenuta dall'operatore**/
    public void addElem_link(ElementoSeqLink link){
       boolean bo = this.testAndSet();
       list_link.add(link);
       testAndReset(bo);
    }
    
    /**Rimuove un link alla lista mantenuta dall'operatore**/
    public void removeElem_link(ElementoSeqLink link){  
       boolean bo = this.testAndSet();
       list_link.remove(link);
       testAndReset(bo);
       
    }
    
    public void setXYRet(){
        boolean bo = this.testAndSet(); 
        int retX=0;
        int retY=0;
        if(link_first.getFlussoDiretto()){
           retX = link_first.getPointStart().x;
           retY = link_first.getPointStart().y;
        }
        else{
           retX = link_first.getPointEnd().x;
           retY = link_first.getPointEnd().y;
        }            
        for(int i=0; i<list_mess.size();i++){
            ElementoSeqLink link = (ElementoSeqLink) list_mess.get(i);
            if(link.getFlussoDiretto()){
              if(link.getPointStart().x < retX)
                  retX = link.getPointStart().x;                 
            }
            else{
                if(link.getPointEnd().x < retX)
                  retX = link.getPointEnd().x;  
            }            
        }
        rettangoloX = retX;
        rettangoloY = retY;
        rettanY_back = rettangoloY;
        rettanX_back = rettangoloX;
        this.testAndReset(bo);
    }
    
    /**ritorna l'altezza della figura**/
    public int getSim_Height(){
        
        return sim_height ;
    }
    
    /**setta l'altezza della figura**/
    public void setSim_Height(){      
      boolean bo = this.testAndSet();
      sim_height = link_last.getPointEnd().y - link_first.getPointStart().y ;
      height_back = sim_height;
      testAndReset(bo);
    }
        
        /** setta la variabile point_width */
	public void setSim_point_width() {
		boolean bo = this.testAndSet();
                int start;
                int end;
                ElementoSeqLink prec_lev ;
                if(link_last.getFlussoDiretto()){
                    start =link_last.getPointStart().x;  
                    end = link_last.getPointEnd().x ;
                }
                else{
                    start =link_last.getPointEnd().x;  
                    end = link_last.getPointStart().x ; 
                }
		for(int i=0; i<list_mess.size() ;i++){
                    prec_lev =(ElementoSeqLink) list_mess.get(i);
                    if(prec_lev.getFlussoDiretto()){
                        if(prec_lev.getPointStart().x < start)
                            start = prec_lev.getPointStart().x; 
                        if(prec_lev.getPointEnd().x > end)
                            end = prec_lev.getPointEnd().x;
                    }
                    else{
                        if(prec_lev.getPointEnd().x < start)     
                           start = prec_lev.getPointEnd().x; 
                        if(prec_lev.getPointStart().x > end)
                            end = prec_lev.getPointStart().x;
                    }
               }
                sim_width = end - start;
                width_back = sim_width;
		this.testAndReset(bo);          
	}
    
        /** ritorna la variabile point_width */
	public int getSim_point_width() {
		
                return  sim_width;
	}
        
        /** Aggiorna la posizione dopo la modofica della posizione di una classe. */    
        public void updateSimPosizione()
        {           
            setXYRet();
            setSim_point_width();
            setSim_Height();
            grafico.updateSimPosizione(rettangoloX,
                                            rettangoloY,
                                            sim_height,
                                            sim_width);	
        }
        
        
        
        /** Aggiorna la posizione dopo la modofica della posizione della figura. */     
        public void updateSimPosizione_am()
        {

            grafico.updateSimPosizione(rettangoloX,
                                            rettangoloY,
                                            sim_height,
                                            sim_width);	
        }
        
        
        /**Controlla se dopo lo spostamento sia stato selezionato o deselezionato qualche link **/
        public void CntrlElem(){
            boolean bo = this.testAndSet();
            ElementoSeqLink link;
            ElementoSeqLink link1;
            for(int i=0;i<list_link.size();i++){
                if(!list_mess.contains(list_link.get(i))){
                    link =(ElementoSeqLink) list_link.get(i);                
                    if (grafico.getSimRectangle().contains(link.getPointStart()) 
                        || grafico.getSimRectangle().contains(link.getPointEnd())){ 
                           if(link.isSimultaneous()
                              || link.isParallel()){
                               setLast_link();
                               setFirst_link(); 
                               testAndReset(bo);                                
                               return;
                           }
                           if(link.hasConstraint()){
                               if(link.isConstraintChCloFut() ||
                                    link.isConstraintChOpFut() ||
                                    link.isConstraintChCloPast() ||
                                    link.isConstraintChOpPast() ||
                                    link.isConstraintUnCloFut() ||
                                    link.isConstraintUnOpFut() ||
                                    link.isConstraintUnCloPast() ||
                                    link.isConstraintUnOpPast())
                            {
                                    setLast_link();
                                    setFirst_link();
                                    testAndReset(bo);                                    
                                    return;       
                                }
                               else
                                   addElem(link);
                           }
                           else
                           addElem(link);                     
                    }  
                }
            }
            
            for(int j=0;j<list_link.size();j++){
                if(list_mess.contains(list_link.get(j))){
                    link1 =(ElementoSeqLink) list_link.get(j);
                    if(!(grafico.getSimRectangle().contains(link1.getPointStart())
                        || grafico.getSimRectangle().contains(link1.getPointEnd()))){
                         removeElem(link1);

                    }
                }
            }
           setLast_link();
           setFirst_link();           
           testAndReset(bo);
        }
        
    
        public static int getSimInstanceNumber()
	{
		return SimInstanceNumber;
	}
	
	public static int incSimInstanceNumber()
	{
		return SimInstanceNumber++;
	}
	
	///ezio 2006 - nuovo metodo, per reset apertura file
	public static void setInstanceNumber(int instance)
	{
		SimInstanceNumber = instance;
	}
	//////
	
        /** Restituisce la larghezza della forma rettangolare. */
        public int getWidth()
        {
            return grafico.getWidth();
        }


        /** Imposta la larghezza della forma rettangolare. */
        public void setWidth(int nuova_larghezza)
        {
            boolean bo = testAndSet();
            sim_width = nuova_larghezza;
            testAndReset(bo);
        }


        /** Restituisce la lista dei messaggi contenuti nell'operatore. */
        public LinkedList getList_mess()
        {
            return list_mess;
        }


        /** Restituisce l'altezza della forma rettangolare. */
        public int getHeight()
        {
            return grafico.getHeight();
        }


        /** Imposta l'altezza della forma rettangolare. */
        public void setHeight(int nuova_altezza)
        {
            boolean bo = testAndSet();
            sim_height = nuova_altezza;
            testAndReset(bo);
        }
        
        /**
         * crea un clone dell'elemento Sim compreso l'identificatore
        * @author flamel
        */
    
        public ElementoSim cloneSim()
        {
            ElementoSim cloned;
            cloned = new ElementoSim(list_mess,seqEle,this.getId(),null,true);
            cloned.creaGraficoSim();
            return cloned;
        }
        
        
        
        /**
        * resitutisce l'identificatore
        * @return l'id
        */
        public long getId(){
            return instanceIde;
        }
        
        /** get della variabile estremoSuperiore. */
        public boolean getEstremoSuperiore()
        {
            return estremoSuperiore;
        }

        /** get della variabile estremoInferiore. */
        public boolean getEstremoInferiore()
        {
            return estremoInferiore;
        }
    
         /** Seleziona/deseleziona l'operatore. */
        public void setSelected(boolean ctrlSelection)
        {
            grafico.setSelected(ctrlSelection);
        }
        
        /** Verifica se l'operatore ï¿½ selezionato. */
        public boolean isSelected()
        {
            return grafico.isSelected();
        }
   
    
        /** Inverte lo stato selezionato/deselezionato dell'operatore. */
        public void invSelected()
        {
            grafico.invSelected();
        } 
        
        /** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
	Serve per verificare se il punto 'p' ï¿½ sufficientemente 
        vicino al Sim (vicino dal punto di vista grafico). */
        public boolean isSelezionato(Point p)
        {
            return grafico.isSelezionato(p);
        }
        
        /** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
	Serve per verificare se il punto 'p' ï¿½ sufficientemente 
        vicino agli angoli dell'operatore (vicino dal punto di vista grafico). */
        public boolean isSelected_sim(Point p)
        {
            return grafico.isSelected_sim(p);
        }
        
        /** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
	Serve per verificare se il punto 'p' ï¿½ sufficientemente 
        vicino agli angoli dell'operatore (vicino dal punto di vista grafico). */
        public boolean isSelezionato_sim(Point p)
        {
            return grafico.isSelezionato_sim(p);
        }
        
        
         public void Mod_sim(Point p){
            grafico.Mod_sim(p);
        }
        
         /**  **/
         
       
        /** Restituisce l'ascissa dell'estremo in alto a sinistra. */
        public int getTopX()
        {
            return grafico.getX();
        }
        
        /** Restituisce l'ordinata dell'estremo in alto a sinistra. */
        public int getTopY()
        {
            return grafico.getY();
        }
        
        /** Restituisce un riferimento al grafico del link. */
        public GraficoSim getGrafico()    
        {
            return grafico;
        }
        
        /** set della variabile estremoSuperiore. */
        public void setEstremoSuperiore(boolean value)
        {
            estremoSuperiore=value;
        }           
        
        /** Imposta la posizione del grafico dell'elemento. */
	public void setPoint_mov(Point p) {
                boolean bo = testAndSet(); 

                if(grafico.isSelezionato_sim_bot_right()){ 
                    if( p.y > link_first.getPointStart().y + 40)
                    { if(p.x <= rettangoloX + width_back)                        
                          sim_height = p.y - rettangoloY-5;
                      else
                          sim_width =  p.x - rettangoloX-5;
                          sim_height = p.y - rettangoloY-5;
                    }     
                }
                else if(grafico.isSelezionato_sim_bot_left()){ 
                        if( p.y > link_first.getPointStart().y + 40)
                        { 
                            if(p.x >= rettanX_back)                                                    
                                sim_height = p.y - rettangoloY-5;  
                            else{
                                sim_height = p.y - rettangoloY-5;
                                sim_width =  width_back -10 + rettanX_back - p.x  ;
                                rettangoloX = p.x+10 ;
                            }

                        }     
                }
                else if(grafico.isSelezionato_sim_up_left()){
                        if( p.y < link_last.getPointStart().y - 50)
                            {         
                                if(p.x >= rettanX_back){
                                    sim_height =  height_back - 25 + rettanY_back - p.y;
                                    rettangoloY = p.y+25;
                                }
                                else{
                                    sim_width =  width_back -10 + rettanX_back - p.x  ;                    
                                    sim_height =  height_back - 25 + rettanY_back - p.y;
                                    rettangoloX = p.x+10 ;
                                    rettangoloY = p.y+25; 
                                }
                            }
                }
                else if(grafico.isSelezionato_sim_up_right()){
                        if( p.y < link_last.getPointStart().y - 50)
                            {    
                                if(p.x <=rettangoloX + width_back ){
                                   sim_height =  height_back - 25 + rettanY_back - p.y; 
                                   rettangoloY = p.y + 25;
                                }
                                    
                                else{                                    
                                sim_width =  p.x - rettangoloX-5;
                                sim_height =  height_back - 25 + rettanY_back - p.y;
                                rettangoloY = p.y + 25;  
                                }
                            }
                }
                testAndReset(bo);
                    
        }
        
        /* (non-Javadoc)
        * @see data.ImpUpdate#informPreUpdate()
	*/
	public void informPreUpdate() {
		if(sendMsg){  //posso inviare il messaggio
			if(updateEp != null){
				updateEp.informaPreUpdate(this.cloneSim());
			}
		}
	}

	/* (non-Javadoc)
	 * @see data.ImpUpdate#informPostUpdate()
	 */
	public void informPostUpdate() {
		if(sendMsg){  //posso inviare il messaggio
			if(updateEp != null){
				updateEp.informaPostUpdate(this.cloneSim());
			}
		}	
	}
        
        /**
     * ritorna la classe di ascolto update
     * @return
     */
    public IListaSimNotify getUpdateEp() {
    	return updateEp;
    }
    
    /**
     * setta la classe di ascolto update
     * @param updateEP
     */    
       public void setUpdateEp(IListaSimNotify  updateEP) {
    	updateEp = updateEP;
    }


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}     
    
}