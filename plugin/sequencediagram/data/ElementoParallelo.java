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
import plugin.sequencediagram.data.interfacce.IListaParallelNotify;
import plugin.sequencediagram.graph.GraficoParallelo;
import plugin.topologydiagram.TopologyWindow;
import plugin.topologydiagram.resource.data.ImpUpdate;

/**
 *
 * @author  FLAMEL 2005
 */


/** classe che implementa l'operatore parallelo*/

public class ElementoParallelo extends ImpUpdate implements Serializable
{

       	/**Memorizza l'oggetto che gestisce la rappresentazione grafica dell'operatore. */
	private GraficoParallelo grafico;
        
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
	private int parall_width;
        
        /**memorizza l'altezza dell'operatore*/
	private int parall_height;
        
        /**memorizza la cardinalità degli operatori bottom*/
	private int op_bot;
        
       /**memorizza la cardinalità degli operatori up*/
	private int op_up;
        
        /**memorizza la larghezza del link più ampio tra quelli appetibili**/
        private int widthmax = 0;
        
         /**memorizza la larghezza del link più ampio tra quelli appetibili**/
        private int heightmax = 0;
        
         /**il punto da dove inizierà la linea discontinua  dell'operatore parallelo*/
	private int parall_point_start_med ;
        
       //notifica degli update
	private IListaParallelNotify  updateEp = null;
        
        private long instanceIde=0;
        
        //**Memorizza le istanze**//
        private static int parallelInstanceNumber = 0;
        
        private Graphics2D g2D;
        
        /** Estremo superiore incluso/escluso */
	private boolean estremoSuperiore;
	
	/** Estremo inferiore incluso/escluso */
	private boolean estremoInferiore;
        
        /** Variabile usata per gestire due diversi tipi di operazione_
        ctrlOP = true: aggiunta di un nuovo parallelo
        ctrlOP = false: modifica proprietï¿½ del parallelo corrente. */
	private boolean ctrlOP;
        
        
       /**memorizano i valori prima che l'operatore venga modificato**/
        private int height_back;
        private int width_back;
        private int rettanY_back;
        private int rettanX_back;
        
        private String label;
        
        /** Costruttore. */  
    public ElementoParallelo(LinkedList list,
                             SequenceElement seq,
                             long id,
			     IListaParallelNotify  updateEp,
                             int bot,
                             int up,
                             boolean forClone) 
    {
       if (forClone)
            instanceIde = id;   	
       else
            instanceIde = incParallelInstanceNumber();	
       
      seqEle = seq; 
      list_link = new LinkedList();
      list_link = seqEle.getListaSeqLink().getListLinkSequence();
      list_mess = new LinkedList();
      list_mess = list;
      op_bot = bot;
      op_up = up;
      link_last =(ElementoSeqLink) list_mess.getLast();
      link_first =(ElementoSeqLink) list_mess.getFirst();
      
      
      
      
      setPar_Height(); 
      setParallel_point_start_lin_med();
      setParallel_point_width();
      setXYRet();
      grafico = null;
      // core.internal.plugin.file.FileManager.setModificata(true);
    
     // ezio 2006 SequenceEditor.incParallelInstanceNumber();
    //  ezio 2006 instanceIde = incParallelInstanceNumber();  
      this.setLabel("par"+instanceIde);
      
      ///ezio 2006
      this.updateEp = updateEp;
      /////
      
      height_back = parall_height;
      width_back = parall_width;
      rettanY_back = rettangoloY;
      rettanX_back = rettangoloX;
      setIdLink(); 
    }
    
    /** Crea l'oggetto grafico associato al parallelo. */
    public void creaGraficoParallel()
    {
        boolean bo = this.testAndSet();

       grafico = new GraficoParallelo( rettangoloX,
                                       rettangoloY,
                                       parall_height,
                                       this.getParallel_point_start_lin_med(),
                                       this.getParallel_point_width(), 
                                       this);
    }
    
    /** Disegna il grafico associato all'operatore parallelo. */
    public void paintParallel(Graphics2D g2D)
    {   boolean bo = this.testAndSet();
        if(grafico!=null) 
            grafico.paintGraficoParallel(g2D);
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
           ElementoSeqLink link =  (ElementoSeqLink) list_mess.get(i);
           link.setIdPar(this.instanceIde);
           if(link.getPointStart().y > this.parall_point_start_med)
               link.setOpPar(true);
           else
               link.setOpPar(false);
        }
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
        link.setParallel(true,this.getId()); 
        if(link.getPointStart().y > this.parall_point_start_med)
           link.setOpPar(true);
        else
           link.setOpPar(false);        
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
 	    	String s = "Indice fuori dai limiti ammessi \ndentro la classe ElementoParallelo&removeElem.\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return false;
        }
       link.setParallel(false,0);       
       list_mess.remove(i);
       testAndReset(bo);
       return true;
       
    }
    
    //**aggiorna la lista dei link del sequence mantenuta dall'operatore**//
    public void addElem_link(ElementoSeqLink link){
       boolean bo = this.testAndSet();
       list_link.add(link);
       testAndReset(bo);
    }
    
    //**Rimuove un link dalla lista mantenuta dall'operatore**//
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
    public int getPar_Height(){
        
        return parall_height ;
    }
    
    /**setta l'altezza della figura**/
    public void setPar_Height(){      
      boolean bo = this.testAndSet();
      parall_height = link_last.getPointEnd().y - link_first.getPointStart().y ;
      height_back = parall_height;
      testAndReset(bo);
    }
    
    /** setta la variabile point_start della linea trattegiata */
	public void setParallel_point_start_lin_med() {
            boolean bo = this.testAndSet();    
                if(link_last.getFlussoDiretto()){
                    parall_point_start_med = link_last.getPointStart().y -40;   
                }
                else{
                    parall_point_start_med = link_last.getPointEnd().y - 40;                       
                }
            setIdLink();
            this.testAndReset(bo);
        }
    
    /** ritorna la variabile point_start della linea trattegiata */
	public int getParallel_point_start_lin_med() {
            return parall_point_start_med;
        }
        
        /** setta la variabile point_width */
	public void setParallel_point_width() {
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
                parall_width = end - start;
                width_back = parall_width;
		this.testAndReset(bo);          
	}
    
        /** ritorna la variabile point_width */
	public int getParallel_point_width() {
		
                return  parall_width;
	}
        
        /** Aggiorna la posizione dopo la modofica della posizione di una classe. */    
        public void updateParallelPosizione()
        {           
            setXYRet();
            setParallel_point_width();
            setPar_Height();
            grafico.updateParallelPosizione(rettangoloX,
                                            rettangoloY,
                                            parall_height,
                                            parall_point_start_med,
                                            parall_width);	
        }
        
        
         /** Aggiorna la posizione dopo la modofica della posizione dell'operatore. */     
        public void updateParallelPosizione_ao()
        {
            setBound_med();            
            grafico.updateParallelPosizione(rettangoloX,
                                            rettangoloY,
                                            parall_height,
                                            parall_point_start_med,
                                            parall_width);
            setIdLink();
        }
        
        /** Aggiorna la posizione dopo la modofica della posizione della figura. */     
        public void updateParallelPosizione_am()
        {

            grafico.updateParallelPosizione(rettangoloX,
                                            rettangoloY,
                                            parall_height,
                                            parall_point_start_med,
                                            parall_width);
            setIdLink();
        }
        
        /**ritorna l'ordinata di partenza della linea centrale dell'operatore**/
        public int getBound_med(){
             return parall_point_start_med;
        }
        
        /**setta l'ordinata di partenza della linea centrale dell'operatore**/
        public void setBound_med(){
            boolean bo = this.testAndSet();
            ElementoSeqLink link;
             int med = getY_med()-10;
             int bound = med - link_first.getPointStart().y; 
             int med_now = link_first.getPointStart().y;
             int k = 0;
            for(int i = 0;i<list_mess.size();i++){
                link =(ElementoSeqLink) this.list_mess.get(i);
                k = med - link.getPointStart().y;
                if( k < 0)
                  k = -k;
                if( k < bound){
                    bound = k;
                    med_now = link.getPointStart().y ;                    
                }                                 
            }
             parall_point_start_med = med_now;
             this.testAndReset(bo); 
       
        }
        
        /**Controlla se dopo lo spostamento sia stato selezionato o deselezionato qualche link **/
        public void CntrlElem(){
            boolean bo = this.testAndSet();
            ElementoSeqLink link;
            ElementoSeqLink link1;
            for(int i=0;i<list_link.size();i++){
                if(!list_mess.contains(list_link.get(i))){
                    link =(ElementoSeqLink) list_link.get(i);                
                    if (grafico.getParallelRectangle().contains(link.getPointStart()) 
                        || grafico.getParallelRectangle().contains(link.getPointEnd())){ 
                           if(link.isParallel()
                              ||link.isSimultaneous()){
                               setLast_link();
                               setFirst_link();
                               setParallel_point_start_lin_med();
                               testAndReset(bo); 
                               return;
                           }
                           if(link.hasConstraint())
                           {
                               if(link.isConstraintPre())
                                   addElem(link);
                               else
                               {
                                   setLast_link();
                                   setFirst_link();
                                   setParallel_point_start_lin_med();
                                   testAndReset(bo); 
                                   return;
                                   
                               }
                           }
                           else                                 
                                addElem(link);                     
                    }  
                }
            }
            
            for(int j=0;j<list_link.size();j++){
                if(list_mess.contains(list_link.get(j))){
                    link1 =(ElementoSeqLink) list_link.get(j);
                    if(!(grafico.getParallelRectangle().contains(link1.getPointStart())
                        || grafico.getParallelRectangle().contains(link1.getPointEnd()))){
                         removeElem(link1);

                    }
                }
            }
           setLast_link();
           setFirst_link();
           setParallel_point_start_lin_med();
           testAndReset(bo);
        }
        
        /**Ritorna l'ordinata della linea mediana dell'operatore**/
        public int getY_med()
	{
		return grafico.getY_med();
	}
    
        public static int getParallelInstanceNumber()
	{
		return parallelInstanceNumber;
	}
	
	public static int incParallelInstanceNumber()
	{
		return parallelInstanceNumber++;
	}
       
	///ezio 2006 - nuovo metodo, per reset apertura file
	public static void setInstanceNumber(int instance)
	{
		parallelInstanceNumber = instance;
	}
	

	//////
        /** Restituisce il punto della linea centrale. */
        public int getMed()
        {
            return parall_point_start_med;
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


        /** Imposta il punto della linea centrale. */
        public void setMed(int nuova_pos)
        {
            boolean bo = testAndSet();
            parall_point_start_med = nuova_pos;
            testAndReset(bo);
        }
        
        /** Restituisce la larghezza della forma rettangolare. */
        public int getWidth()
        {
            return grafico.getWidth();
        }


        /** Imposta la larghezza della forma rettangolare. */
        public void setWidth(int nuova_larghezza)
        {
            boolean bo = testAndSet();
            parall_width = nuova_larghezza;
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
            parall_height = nuova_altezza;
            testAndReset(bo);
        }
        
        /**
         * crea un clone dell'elemento Parallel compreso l'identificatore
        * @author flamel
        */
    
        public ElementoParallelo cloneParallel()
        {
            ElementoParallelo cloned;
            cloned = new ElementoParallelo(list_mess,seqEle,this.getId(),null,op_bot,op_up,true);
            cloned.creaGraficoParallel();
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
    
         /** Seleziona/deseleziona il link. */
        public void setSelected(boolean ctrlSelection)
        {
            grafico.setSelected(ctrlSelection);
        }
        
        /** Verifica se il link ï¿½ selezionato. */
        public boolean isSelected()
        {
            return grafico.isSelected();
        }
   
    
        /** Inverte lo stato selezionato/deselezionato del link. */
        public void invSelected()
        {
            grafico.invSelected();
        } 
        
        /** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
	Serve per verificare se il punto 'p' ï¿½ sufficientemente 
        vicino al parallelo (vicino dal punto di vista grafico). */
        public boolean isSelezionato(Point p)
        {
            return grafico.isSelezionato(p);
        }
        
        /** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
	Serve per verificare se il punto 'p' ï¿½ sufficientemente 
        vicino alla linea media (vicino dal punto di vista grafico). */
        public boolean isSelected_med(Point p)
        {
            return grafico.isSelected_med(p);
        }
        
        /** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
	Serve per verificare se il punto 'p' ï¿½ sufficientemente 
        vicino alla linea media (vicino dal punto di vista grafico). */
        public boolean isSelezionato_med(Point p)
        {
          return grafico.isSelezionato_med(p);
        }
        
        /** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
	Serve per verificare se il punto 'p' ï¿½ sufficientemente 
        vicino agli angoli dell'operatore (vicino dal punto di vista grafico). */
        public boolean isSelected_par(Point p)
        {
            return grafico.isSelected_par(p);
        }
        
        /** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
	Serve per verificare se il punto 'p' ï¿½ sufficientemente 
        vicino agli angoli dell'operatore (vicino dal punto di vista grafico). */
        public boolean isSelezionato_par(Point p)
        {
            return grafico.isSelezionato_par(p);
        }
        
        public void Mod_med(Point p){
            grafico.Mod_med(p);
        }
        
         public void Mod_par(Point p){
            grafico.Mod_par(p);
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
        public GraficoParallelo getGrafico()    
        {
            return grafico;
        }
        
        /** set della variabile estremoSuperiore. */
        public void setEstremoSuperiore(boolean value)
        {
            estremoSuperiore=value;
        }
        
        /** Imposta la posizione della linea centrale dell'operatore
         * selezionato. */
	public void setPoint_lincen(Point p) {       
            if( list_mess.size() > 2
               && p.y < link_last.getPointStart().y -7)
            {      
                boolean bo = testAndSet(); 
                Point pp = new Point();
                pp.x = grafico.getX();
                pp.y = p.y; 
                grafico.setPoint_lincen(pp); 
                testAndReset(bo);
            }
	}
            
        
        /** Imposta la posizione del grafico dell'elemento. */
	public void setPoint_mov(Point p) {
                boolean bo = testAndSet(); 

                if(grafico.isSelezionato_par_bot_right()){ 
                    if( p.y > link_first.getPointStart().y + 40)
                    { if(p.x <= rettangoloX + width_back)                        
                          parall_height = p.y - rettangoloY-5;
                      else
                          parall_width =  p.x - rettangoloX-5;
                          parall_height = p.y - rettangoloY-5;
                    }     
                }
                else if(grafico.isSelezionato_par_bot_left()){ 
                        if( p.y > link_first.getPointStart().y + 40)
                        { 
                            if(p.x >= rettanX_back)                                                    
                                parall_height = p.y - rettangoloY-5;  
                            else{
                                parall_height = p.y - rettangoloY-5;
                                parall_width =  width_back -10 + rettanX_back - p.x  ;
                                rettangoloX = p.x+10 ;
                            }

                        }     
                }
                else if(grafico.isSelezionato_par_up_left()){
                        if( p.y < link_last.getPointStart().y - 50)
                            {         
                                if(p.x >= rettanX_back){
                                    parall_height =  height_back - 25 + rettanY_back - p.y;
                                    rettangoloY = p.y+25;
                                }
                                else{
                                    parall_width =  width_back -10 + rettanX_back - p.x  ;                    
                                    parall_height =  height_back - 25 + rettanY_back - p.y;
                                    rettangoloX = p.x+10 ;
                                    rettangoloY = p.y+25; 
                                }
                            }
                }
                else if(grafico.isSelezionato_par_up_right()){
                        if( p.y < link_last.getPointStart().y - 50)
                            {    
                                if(p.x <=rettangoloX + width_back ){
                                   parall_height =  height_back - 25 + rettanY_back - p.y; 
                                   rettangoloY = p.y + 25;
                                }
                                    
                                else{                                    
                                parall_width =  p.x - rettangoloX-5;
                                parall_height =  height_back - 25 + rettanY_back - p.y;
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
				updateEp.informaPreUpdate(this.cloneParallel());
			}
		}
	}

	/* (non-Javadoc)
	 * @see data.ImpUpdate#informPostUpdate()
	 */
	public void informPostUpdate() {
		if(sendMsg){  //posso inviare il messaggio
			if(updateEp != null){
				updateEp.informaPostUpdate(this.cloneParallel());
			}
		}	
	}
        
        /**
     * ritorna la classe di ascolto update
     * @return
     */
    public IListaParallelNotify getUpdateEp() {
    	return updateEp;
    }
    
    /**
     * setta la classe di ascolto update
     * @param updateEP
     */    
       public void setUpdateEp(IListaParallelNotify  updateEP) {
    	updateEp = updateEP;
    }

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	} 
}













