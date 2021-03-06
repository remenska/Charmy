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

package plugin.sequencediagram.graph;


import java.io.Serializable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import plugin.sequencediagram.data.ElementoSim;
import plugin.sequencediagram.data.ElementoSeqLink;

/**
 * @author flamel 2005
 * Charmy plug-in
 * classe per disegnare l'operatore simultaneo
 **/

public class GraficoSim 	extends JPanel 
implements Serializable
               
{

        /** True quando il Sim viene selezionato, false altrimenti. */ 
        protected boolean isSelected;

        /** Rappresentazione grafica dell'operatore simultaneo . */
        transient private Rectangle2D.Double SimRectangle;
        transient private Line2D.Double SimGraph1;
        transient private Line2D.Double SimGraph2;
        transient private Line2D.Double SimGraph3;
        transient private Line2D.Double SimGraph_tratt;


        /** Linea continua */
        public static final float[] NOT_DASHED = {10.0f, 0.0f, 10.0f, 0.0f};
        /** linea trattegiata**/
        public static final float[] DASHED = {5.0f, 3.0f, 5.0f, 3.0f};

    
        transient private Rectangle2D rectSelTopLeft;
    
        transient private Rectangle2D rectSelBottomLeft;
    
        transient private Rectangle2D rectSelTopRight;
    
        transient private Rectangle2D rectSelBottomRight;
        
        transient private Rectangle2D rectForSelection = null;
        
        
       
        /** Memorizza l'ascissa dell'estremo in alto a sinistra. */
      private int rettangoloX;
        
        /** Memorizza l'ordinata dell'estremo in alto a sinistra. */
     private int rettangoloY;
    
        /** Memorizza la larghezza della forma rettangolare. */
     private int larghezza;
    
        /** Memorizza l'altezza della forma rettangolare. */
     private int altezza;        

     
        
        /** Vera se viene selezionato uno dei rettangolini agli estremi della figura. */
        private boolean rett_ang = false;
        
        /** Vera se viene selezionato uno dei rettangolini agli estremi della figura. */
        private boolean Bot_right = false;
        
        /** Vera se viene selezionato uno dei rettangolini agli estremi della figura. */
        private boolean Up_left = false;
        
        /** Vera se viene selezionato uno dei rettangolini agli estremi della figura. */
        private boolean Bot_left = false;
        
        /** Vera se viene selezionato uno dei rettangolini agli estremi della figura. */
        private boolean Up_right = false;
        
        /** Riferimento al constraint */
        private ElementoSim SimCorrente;
        
    
    /**
     x     : l'ascissa del punto in alto a sinistra del rettangolo
     y     : l'ordinata
     h     : l'ordinata dell'ultimo link selezionato 
     w     : l'ascissa del link pi� profondo che f� parte dell'operatore
     loop  : l'elemento parallelo
     **/        
    public GraficoSim(int x,int y,int h,int w, ElementoSim sim)
    {
    	
    	
     SimCorrente = sim;         
     rettangoloX = x-10; 
     rettangoloY = y-25;
     larghezza = w+15;
     altezza = h+35;
     rectForSelection = new Rectangle2D.Double(rettangoloX,rettangoloY,larghezza,altezza);
     rectSelTopLeft = new Rectangle2D.Double(rettangoloX-3,rettangoloY-3,6,6);
     rectSelBottomLeft = new Rectangle2D.Double(rettangoloX-3,rettangoloY+altezza-3,6,6);
     rectSelTopRight = new Rectangle2D.Double(rettangoloX+larghezza-3,rettangoloY-3,6,6);
     rectSelBottomRight = new Rectangle2D.Double(rettangoloX+larghezza-3,rettangoloY+altezza-3,6,6);     
     updateSelection();
        
    }
   
    /** Disegna l'operatore simultaneo. */
    public void paintGraficoSim(Graphics2D g2D)
    {
                Stroke tmpStroke;
                Paint tmpPaint;
		AffineTransform tmpTransform;
		tmpStroke = g2D.getStroke();
		tmpPaint = g2D.getPaint();
		tmpTransform = g2D.getTransform();
		g2D.setColor(Color.GRAY);
                SimRectangle = new Rectangle2D.Double(rettangoloX,rettangoloY,larghezza,altezza);
                SimGraph1 = new Line2D.Double(rettangoloX, rettangoloY+20,rettangoloX+50,rettangoloY+20);
                SimGraph2 = new Line2D.Double(rettangoloX+60, rettangoloY,rettangoloX+60,rettangoloY+10);
                SimGraph3 = new Line2D.Double(rettangoloX+60, rettangoloY+10,rettangoloX+50,rettangoloY+20);
                g2D.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 10.0f, DASHED, 0));
                for(int i = 0; i < SimCorrente.getList_mess().size();i++){
                    if(!(SimCorrente.getList_mess().get(i).equals(SimCorrente.getlast_link()))){
                        ElementoSeqLink link =(ElementoSeqLink) SimCorrente.getList_mess().get(i);
                            if(link.getFlussoDiretto()){
                                SimGraph_tratt = new Line2D.Double(rettangoloX,link.getPointStart().y+10,(rettangoloX)+(larghezza),link.getPointStart().y+10);
                                g2D.draw(SimGraph_tratt);
                            }
                            else{
                                SimGraph_tratt = new Line2D.Double(rettangoloX,link.getPointEnd().y+10,(rettangoloX)+(larghezza),link.getPointEnd().y+10);
                                g2D.draw(SimGraph_tratt);
                            }
                    }
                    
                }
                g2D.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND, 10.0f, NOT_DASHED, 0));
                g2D.drawString("Sim "+SimCorrente.getId(),rettangoloX+15,rettangoloY+15);               
		g2D.draw(SimGraph1);
                g2D.draw(SimGraph2);
                g2D.draw(SimGraph3);              
                g2D.draw(SimRectangle);  
                g2D.setStroke(tmpStroke);
                paintSelected(g2D);
		g2D.setPaint(tmpPaint);
		g2D.setTransform(tmpTransform);
        
        
    } 
    
    /** Metodo per aggiornare la posizione del simultaneo*/
    public void updateSimPosizione(int x,int y,int h,int w)
    {         
       
        rettangoloX = x-10;
        rettangoloY = y-25;
        larghezza = w+15;
        altezza = h+35;       
        rectForSelection.setRect(rettangoloX,rettangoloY,larghezza,altezza);
        rectSelTopLeft.setRect(rettangoloX-3,rettangoloY-3,6,6);
        rectSelBottomLeft.setRect(rettangoloX-3,rettangoloY+altezza-3,6,6);
        rectSelTopRight.setRect(rettangoloX+larghezza-3,rettangoloY-3,6,6);
        rectSelBottomRight.setRect(rettangoloX+larghezza-3,rettangoloY+altezza-3,6,6); 
        
        
    }
    
    /** Se l'oggetto � stato selezionato, il metodo aggiorna la 
    	posizione dei rettangoli che evidenziano la selezione. */
        public void updateIfSelected()
        {
            if (isSelected)
            {
        	rectForSelection.setRect(rettangoloX,rettangoloY,larghezza,altezza);
        	rectSelTopLeft.setRect(rettangoloX-3,rettangoloY-3,6,6);
        	rectSelBottomLeft.setRect(rettangoloX-3,rettangoloY+altezza-3,6,6);
        	rectSelTopRight.setRect(rettangoloX+larghezza-3,rettangoloY-3,6,6);
        	rectSelBottomRight.setRect(rettangoloX+larghezza-3,rettangoloY+altezza-3,6,6);
            }
        }
        
        /** Il metodo aggiorna la posizione dei rettangoli che evidenziano la selezione. */
        public void updateSelection()
        {
            rectForSelection.setRect(rettangoloX,rettangoloY,larghezza,altezza);
            rectSelTopLeft.setRect(rettangoloX-3,rettangoloY-3,6,6);
            rectSelBottomLeft.setRect(rettangoloX-3,rettangoloY+altezza-3,6,6);
            rectSelTopRight.setRect(rettangoloX+larghezza-3,rettangoloY-3,6,6);
            rectSelBottomRight.setRect(rettangoloX+larghezza-3,rettangoloY+altezza-3,6,6);
        }
        
        /** "Stampa" i rettangoli che evidenziano la selezione. */
        public void paintSelected(Graphics2D g2d)
        {
            if (isSelected)
            {
                 if(rett_ang){
                
                    g2d.setColor(Color.gray);
                    g2d.fill(rectSelTopLeft);
                    g2d.fill(rectSelBottomLeft);
                    g2d.fill(rectSelTopRight);
                    g2d.fill(rectSelBottomRight);
                    g2d.setColor(Color.white);
                    g2d.setColor(Color.black);
                    g2d.draw(rectSelTopLeft);
                    g2d.draw(rectSelBottomLeft);
                    g2d.draw(rectSelTopRight);
                    g2d.draw(rectSelBottomRight);
                
                }
                else {
                    g2d.setColor(Color.white);                                  
                    g2d.fill(rectSelTopLeft);
                    g2d.fill(rectSelBottomLeft);
                    g2d.fill(rectSelTopRight);
                    g2d.fill(rectSelBottomRight);                  
                    g2d.setColor(Color.black);
                    g2d.draw(rectSelTopLeft);
                    g2d.draw(rectSelBottomLeft);
                    g2d.draw(rectSelTopRight);
                    g2d.draw(rectSelBottomRight);
                }
                        	
            }        
        }
    
    /** Imposta il link come selezionato/deselezionato. */
    public void setSelected(boolean ctrlIfSelected)
    {
        isSelected = ctrlIfSelected;
    }


	/** Restituisce true se il link ? stato selezionato, false altrimenti. */
    public boolean isSelected()
    {
        return isSelected;
    }
    
    /** Seleziona/deseleziona il link se deselezionato/selezionato. */
    public void invSelected()
    {
    	isSelected = !isSelected;
    }
    
    /** Verifica se il punto 'pnt' ? sufficientemente vicino al 'centro' del collegamento.
        La costante 'delta' determina quanto il punto 'pnt' deve stare vicino perch? il
        metodo restituisca 'true'. */
    public boolean isSelezionato(Point pnt)
    {    
        if(rectSelBottomRight.contains(pnt) || rectSelBottomLeft.contains(pnt) 
           || rectSelTopRight.contains(pnt) 
           || rectSelTopLeft.contains(pnt))            
            rett_ang = true;
        else
            rett_ang = false;       
        return rectForSelection.contains(pnt);  
    }
    
    /** Verifica se il punto 'pnt' ? sufficientemente vicino al 'centro' del collegamento.
        La costante 'delta' determina quanto il punto 'pnt' deve stare vicino perch? il
        metodo restituisca 'true'. */
    public boolean isSelected_sim(Point pnt)
    {           
        return rett_ang;  
    }
    
    /** Verifica se il punto 'pnt' ? sufficientemente vicino al 'centro' del collegamento.
        La costante 'delta' determina quanto il punto 'pnt' deve stare vicino perch? il
        metodo restituisca 'true'. */
    public boolean isSelezionato_sim(Point pnt)
    {   
        if(rectSelBottomLeft.contains(pnt)){
            Bot_left = true;
            Bot_right = false;
            Up_left = false;
            Up_right = false;
            return true;
        } 
        else if(rectSelBottomRight.contains(pnt)){
            Bot_left = false;
            Bot_right = true;
            Up_left = false;
            Up_right = false;
            return true;
        }
        else  if(rectSelTopRight.contains(pnt)){
                Bot_left = false;
                Bot_right = false;
                Up_left = false;
                Up_right = true;
                return true;
        }                   
        else  if(rectSelTopLeft.contains(pnt)){
                Bot_left = false;
                Bot_right = false;
                Up_left = true;
                Up_right = false;
                return true;
        }                          
        else{
            Up_left = false;
            Bot_right = false;
            Bot_left = false;
            Up_right= false;
            return false;
        }
    }
    
    
    /**  **/
    public boolean isSelezionato_sim_up_left()
    {        
        return Up_left;
    }
    
    /** Verifica se il punto � vicino al rettangolo in basso a sinistra */
    public boolean isSelezionato_sim_bot_right()
    {        
        return Bot_right;
    }
    
    /**  **/
    public boolean isSelezionato_sim_up_right()
    {        
        return Up_right;
    }
    
    /** Verifica se il punto � vicino al rettangolo in basso a sinistra */
    public boolean isSelezionato_sim_bot_left()
    {        
        return Bot_left;
    }
    
    
    public void Mod_sim(Point p){
        
        if(Bot_right)
            rectSelBottomRight.setRect(rettangoloX+larghezza-3,rettangoloY+altezza-3,10,10);     
        else  if(Up_left)
            rectSelTopLeft.setRect(rettangoloX-3,rettangoloY-3,10,10);
        else  if(Bot_left)       
            rectSelBottomLeft.setRect(rettangoloX-3,rettangoloY+altezza-3,10,10);
        else  if(Up_right)   
            rectSelTopRight.setRect(rettangoloX+larghezza-3,rettangoloY-3,10,10);
    }
    
    /** Restituisce l'ascissa dell'estremo in alto a sinistra. 
     * @return posizione X attuale
     * */
    public int getX()
    {
        return rettangoloX;     
    }
    
    /** Restituisce l'ordinata dell'estremo in alto a sinistra. 
     * @return posizione Y attuale
     * */
    public int getY()
    {
        return rettangoloY;     
    }
    
    /** Restituisce l'ordinata della linea media. 
     * @return posizione Y attuale
     * */
    public int getWidth()
    {
        return larghezza;     
    }
    
    public Rectangle2D.Double getSimRectangle(){
        
        return SimRectangle;
    }
    
    /** Restituisce l'ordinata della linea media. 
     * @return posizione Y attuale
     * */
    public int getHeight()
    {
        return altezza;     
    }
    
    
   /** Metodo per ricostruire la struttura dell'operatore a partire
		dalle informazioni memorizzate sul file. */
	public void restoreFromFile()
	{
             rectForSelection = new Rectangle2D.Double(rettangoloX,rettangoloY,larghezza,altezza);
             rectSelTopLeft = new Rectangle2D.Double(rettangoloX-3,rettangoloY-3,6,6);
             rectSelBottomLeft = new Rectangle2D.Double(rettangoloX-3,rettangoloY+altezza-3,6,6);
             rectSelTopRight = new Rectangle2D.Double(rettangoloX+larghezza-3,rettangoloY-3,6,6);
             rectSelBottomRight = new Rectangle2D.Double(rettangoloX+larghezza-3,rettangoloY+altezza-3,6,6);
             SimCorrente.updateSimPosizione();

            validate();
            repaint();
	}
        

    
    
    
}