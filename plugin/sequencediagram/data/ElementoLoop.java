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

import plugin.sequencediagram.data.interfacce.IListaLoopNotify;
import plugin.sequencediagram.graph.GraficoLoop;
import plugin.topologydiagram.TopologyWindow;
import plugin.topologydiagram.resource.data.ImpUpdate;


/**
 *
 * @author  FLAMEL 2005
 */


/** classe che implementa l'operatore loop*/

public class ElementoLoop extends ImpUpdate implements Serializable
{

	
        /**Memorizza l'oggetto che gestisce la rappresentazione grafica dell'operatore. */
	private GraficoLoop grafico;
        
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
	private int Loop_width;
        
        /**memorizza l'altezza dell'operatore*/
	private int Loop_height;
        
        /**memorizza il valore minimo dell'operatore */
	private int min;
        
        /**memorizza il valore massimo dell'operatore */
	private int max;        
        
        /**memorizza la larghezza del link più ampio tra quelli appetibili**/
        private int widthmax = 0;
        
         /**memorizza la larghezza del link più ampio tra quelli appetibili**/
        private int heightmax = 0;
        
         /**il punto da dove inizierà la linea discontinua  dell'operatore Loopo*/
	private int Loop_point_start_med ;
        
       //notifica degli update
	private IListaLoopNotify  updateEp = null;
        
        private long instanceIde=0;
        
        //**Memorizza le istanze**//
        private static int LoopInstanceNumber = 0;
        
        private Graphics2D g2D;
        
        /** Estremo superiore incluso/escluso */
	private boolean estremoSuperiore;
	
	/** Estremo inferiore incluso/escluso */
	private boolean estremoInferiore;
        
        /** Variabile usata per gestire due diversi tipi di operazione_
        ctrlOP = true: aggiunta di un nuovo Loopo
        ctrlOP = false: modifica proprietï¿½ del Loopo corrente. */
	private boolean ctrlOP;
        
       /**memorizano i valori prima che l'operatore venga modificato**/
        private int height_back;
        private int width_back;
        private int rettanY_back;
        private int rettanX_back;
    
         /** Costruttore. */ 
    public ElementoLoop(LinkedList list,
                             SequenceElement seq,
                             long id,
                             int _min,
                             int _max,   
			     IListaLoopNotify  updateEp,
                             boolean forClone) 
    {
        
        if (forClone)
            instanceIde = id;   	
       else
            instanceIde = incLoopInstanceNumber();      
      seqEle = seq; 
      list_link = new LinkedList();
      list_link = seqEle.getListaSeqLink().getListLinkSequence();
      list_mess = new LinkedList();
      list_mess = list;
      
      link_last =(ElementoSeqLink) list_mess.getLast();
      link_first =(ElementoSeqLink) list_mess.getFirst();
      
      setLoop_Height(); 
      setLoop_point_width();
      setXYRet();
      min = _min;
      max = _max;
      grafico = null;
     // core.internal.plugin.file.FileManager.setModificata(true);
      
   //ezio 2006   SequenceEditor.incLoopInstanceNumber();
      //ezio 2006     instanceIde = incLoopInstanceNumber();
      
      //ezio 2006
      this.updateEp = updateEp;
      ////
      
      height_back = Loop_height;
      width_back = Loop_width;
      rettanY_back = rettangoloY;
      rettanX_back = rettangoloX;
      setIdLink();
    }

    
    /** Crea l'oggetto grafico associato al Loopultaneo. */
    public void creaGraficoLoop()
    {
        boolean bo = this.testAndSet();

        grafico = new GraficoLoop( rettangoloX,
                                       rettangoloY,
                                       Loop_height,
                                       this.getLoop_point_width(), 
                                       this);
    }
    
    /** Disegna il grafico associato all'operatore Loopo. */
    public void paintLoop(Graphics2D g2D)
    {   boolean bo = this.testAndSet();
        if(grafico!=null) 
            grafico.paintGraficoLoop(g2D);
        testAndReset(bo);
    }
    
    /** Aggiorna la lista dei link presenti nel Sequence**/
    public void setlist_link(LinkedList list){
        boolean bo = this.testAndSet();
        list_link = list ;
        testAndReset(bo);
    }
    
    /** Aggiorna il valore minimo**/
    public void setMin(int j){
        boolean bo = this.testAndSet();
        min = j ;
        for(int i=0;i<list_mess.size();i++)
        {
            ((ElementoSeqLink) list_mess.get(i)).setMinLoop(j);            
        }
        testAndReset(bo);
    }
    
    /** Aggiorna il valore massimo**/
    public void setMax(int j){
        boolean bo = this.testAndSet();
        max = j ;
        for(int i=0;i<list_mess.size();i++)
        {
            ((ElementoSeqLink) list_mess.get(i)).setMaxLoop(j);
        }
        testAndReset(bo);
    }
    
    /**setta il suo id e il valore di min e max, su tutti i suoi link **/
    public void setIdLink()
    {
        for(int i=0;i<list_mess.size();i++)
        {
           ((ElementoSeqLink) list_mess.get(i)).setIdLoop(this.instanceIde);
           ((ElementoSeqLink) list_mess.get(i)).setMinLoop(this.min);
           ((ElementoSeqLink) list_mess.get(i)).setMaxLoop(this.max);
        }
    }
    
    /** Ritorna il valore minimo **/
    public int getMin (){
        return min;
        
    }
    
    /** Ritorna il valore massimo **/
    public int getMax (){
        return max;
        
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
        link.setLoop(true,min,max,instanceIde);        
        testAndReset(bo);
        return true;
    }
    
    /**Rimuove un link all'operatore**/
    public boolean removeElem(ElementoSeqLink link){ 
        boolean bo = this.testAndSet();
        int i;
        if(list_mess.size()==1)
            return false;
        if (list_mess == null) 
        	return false; 
        try{ 
            i = list_mess.indexOf(link); 
        }
        catch (IndexOutOfBoundsException e){
 	    	String s = "Indice fuori dai limiti ammessi \ndentro la classe ElementoLoopo&removeElem.\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return false;
        }
       link.setLoop(false,0,0,0);       
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
    public int getLoop_Height(){
        
        return Loop_height ;
    }
    
    /**setta l'altezza della figura**/
    public void setLoop_Height(){      
      boolean bo = this.testAndSet();
      Loop_height = link_last.getPointEnd().y - link_first.getPointStart().y ;
      height_back = Loop_height;
      testAndReset(bo);
    }
        
        /** setta la variabile point_width */
	public void setLoop_point_width() {
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
                Loop_width = end - start;
                width_back = Loop_width;
		this.testAndReset(bo);          
	}
    
        /** ritorna la variabile point_width */
	public int getLoop_point_width() {
		
                return  Loop_width;
	}
        
        /** Aggiorna la posizione dopo la modofica della posizione di una classe. */    
        public void updateLoopPosizione()
        {           
            setXYRet();
            setLoop_point_width();
            setLoop_Height();
            grafico.updateLoopPosizione(rettangoloX,
                                            rettangoloY,
                                            Loop_height,
                                            Loop_point_start_med,
                                            Loop_width);	
        }
        
        
        
        /** Aggiorna la posizione dopo la modofica della posizione della figura. */     
        public void updateLoopPosizione_am()
        {

            grafico.updateLoopPosizione(rettangoloX,
                                            rettangoloY,
                                            Loop_height,
                                            Loop_point_start_med,
                                            Loop_width);
            
        }
        
        
        /**Controlla se dopo lo spostamento sia stato selezionato o deselezionato qualche link **/
        public void CntrlElem(){
            boolean bo = this.testAndSet();
            ElementoSeqLink link;
            ElementoSeqLink link1;
            for(int i=0;i<list_link.size();i++){
                if(!list_mess.contains(list_link.get(i))){
                    link =(ElementoSeqLink) list_link.get(i);                
                    if (grafico.getLoopRectangle().contains(link.getPointStart()) 
                        || grafico.getLoopRectangle().contains(link.getPointEnd())){ 
                           if(link.isLoop_op()){
                               setLast_link();
                               setFirst_link();
                               testAndReset(bo); 
                               return;
                           }
                           addElem(link);                                                  
                    }
                }
            }
            
            for(int j=0;j<list_link.size();j++){
                if(list_mess.contains(list_link.get(j))){
                    link1 =(ElementoSeqLink) list_link.get(j);
                    if(!(grafico.getLoopRectangle().contains(link1.getPointStart())
                        || grafico.getLoopRectangle().contains(link1.getPointEnd()))){
                         removeElem(link1);                        
                    }
                }
            }
           setLast_link();
           setFirst_link();
           boolean ok=true;
           boolean ok2=true;
           while(ok)
           {
               if(this.getfirst_link().hasConstraint())
                {
                    if(this.getfirst_link().isConstraintPastClo() 
                        || this.getfirst_link().isConstraintPastOp()
                        || this.getfirst_link().isConstraintChCloPast()
                        || this.getfirst_link().isConstraintChOpPast()
                        || this.getfirst_link().isConstraintUnCloPast()
                        || this.getfirst_link().isConstraintUnOpPast())
                    {
                        if(this.getfirst_link().equals(this.getlast_link().getPrec()))
                        {
                           this.getfirst_link().setConstraintType(1); 
                           this.getfirst_link().updateAllConstraintPosizione(); 
                           ok2=false;
                           return;
                        }
                        else
                        {
                            removeElem(this.getfirst_link());
                            setFirst_link(); 
                        }
                        
                    }
                else
                    ok=false;
               }
               else
                   ok=false;
           }
           
         while(ok2)
         {
            if(this.getlast_link().hasConstraint())
            {
                if(this.getlast_link().isConstraintFutClo() 
                    || this.getlast_link().isConstraintFutOp()
                    || this.getlast_link().isConstraintChCloFut()
                    || this.getlast_link().isConstraintChOpFut()
                    || this.getlast_link().isConstraintUnCloFut()
                    || this.getlast_link().isConstraintUnOpFut())
                {
                    if(this.getfirst_link().equals(this.getlast_link().getPrec()))
                    {
                       this.getlast_link().setConstraintType(-1);
                       this.getlast_link().updateAllConstraintPosizione(); 
                       return;
                    }
                    else
                    {
                        removeElem(this.getlast_link());
                        setLast_link();
                    }
                }
                else
                    ok2=false;
            }
            else
                ok2=false;
         }
             
           
           testAndReset(bo);
        }

        public static int getLoopInstanceNumber()
	{
		return LoopInstanceNumber;
	}
	
	public static int incLoopInstanceNumber()
	{
		return LoopInstanceNumber++;
	}
	
	///ezio 2006 - nuovo metodo, per reset apertura file
	public static void setInstanceNumber(int instance)
	{
		LoopInstanceNumber = instance;
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
            Loop_width = nuova_larghezza;
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
            Loop_height = nuova_altezza;
            testAndReset(bo);
        }
        
        /**
         * crea un clone dell'elemento Loop compreso l'identificatore
        * @author flamel
        */
    
        public ElementoLoop cloneLoop()
        {
            ElementoLoop cloned;
            cloned = new ElementoLoop(list_mess,seqEle,this.getId(),min,max,null,true);
            cloned.creaGraficoLoop();
            return cloned;
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
        vicino al Loop (vicino dal punto di vista grafico). */
        public boolean isSelezionato(Point p)
        {
            return grafico.isSelezionato(p);
        }
        
        /** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
	Serve per verificare se il punto 'p' ï¿½ sufficientemente 
        vicino agli angoli dell'operatore (vicino dal punto di vista grafico). */
        public boolean isSelected_Loop(Point p)
        {
            return grafico.isSelected_Loop(p);
        }
        
        /** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
	Serve per verificare se il punto 'p' ï¿½ sufficientemente 
        vicino agli angoli dell'operatore (vicino dal punto di vista grafico). */
        public boolean isSelezionato_Loop(Point p)
        {
            return grafico.isSelezionato_Loop(p);
        }
        
        
         public void Mod_Loop(Point p){
            grafico.Mod_Loop(p);
        }
         
       
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
        public GraficoLoop getGrafico()    
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

                if(grafico.isSelezionato_Loop_bot_right()){ 
                    if( p.y > link_first.getPointStart().y + 20)
                    { 
                        if(p.x <= rettangoloX + width_back)                        
                          Loop_height = p.y - rettangoloY-5;
                        else
                          Loop_width =  p.x - rettangoloX-5;
                          Loop_height = p.y - rettangoloY-5;
                    }     
                }
                else if(grafico.isSelezionato_Loop_bot_left()){ 
                        if( p.y > link_first.getPointStart().y + 20)
                        { 
                            if(p.x >= rettanX_back)                                                    
                                Loop_height = p.y - rettangoloY-5;  
                            else{
                                Loop_height = p.y - rettangoloY-5;
                                Loop_width =  width_back -10 + rettanX_back - p.x  ;
                                rettangoloX = p.x+10 ;
                            }

                        }     
                }
                else if(grafico.isSelezionato_Loop_up_left()){
                        if( p.y < link_last.getPointStart().y - 30)
                            {         
                                if(p.x >= rettanX_back){
                                    Loop_height =  height_back - 25 + rettanY_back - p.y;
                                    rettangoloY = p.y+25;
                                }
                                else{
                                    Loop_width =  width_back -10 + rettanX_back - p.x  ;                    
                                    Loop_height =  height_back - 25 + rettanY_back - p.y;
                                    rettangoloX = p.x+10 ;
                                    rettangoloY = p.y+25; 
                                }
                            }
                }
                else if(grafico.isSelezionato_Loop_up_right()){
                        if( p.y < link_last.getPointStart().y - 30)
                            {    
                                if(p.x <=rettangoloX + width_back ){
                                   Loop_height =  height_back - 25 + rettanY_back - p.y; 
                                   rettangoloY = p.y + 25;
                                }
                                    
                                else{                                    
                                Loop_width =  p.x - rettangoloX-5;
                                Loop_height =  height_back - 25 + rettanY_back - p.y;
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
				updateEp.informaPreUpdate(this.cloneLoop());
			}
		}
	}

	/* (non-Javadoc)
	 * @see data.ImpUpdate#informPostUpdate()
	 */
	public void informPostUpdate() {
		if(sendMsg){  //posso inviare il messaggio
			if(updateEp != null){
				updateEp.informaPostUpdate(this.cloneLoop());
			}
		}	
	}
        
        /**
     * ritorna la classe di ascolto update
     * @return
     */
    public IListaLoopNotify getUpdateEp() {
    	return updateEp;
    }
    
    /**
     * setta la classe di ascolto update
     * @param updateEP
     */    
       public void setUpdateEp(IListaLoopNotify  updateEP) {
    	updateEp = updateEP;
    }     
    
}