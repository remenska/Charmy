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

import plugin.sequencediagram.data.ElementoLoop;
import plugin.sequencediagram.data.ElementoSeqLink;

/**
 * @author flamel 2005
 * Charmy plug-in
 * classe che disegna l'operatore loop
 **/

public class GraficoLoop 	extends JPanel 
implements Serializable
               
{

        /** True quando il Loop viene selezionato, false altrimenti. */ 
        protected boolean isSelected;

        /** Rappresentazione grafica dell'operatore Loopultaneo . */
        transient private Rectangle2D.Double LoopRectangle;
        transient private Line2D.Double LoopGraph1;
        transient private Line2D.Double LoopGraph2;
        transient private Line2D.Double LoopGraph3;


        /** Linea continua */
        public static final float[] NOT_DASHED = {10.0f, 0.0f, 10.0f, 0.0f};

    
        /** Rettangoli di selezione **/
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
        private ElementoLoop LoopCorrente;
        
    
    /**
     x     : l'ascissa del punto in alto a sinistra del rettangolo
     y     : l'ordinata
     h     : l'ordinata dell'ultimo link selezionato 
     w     : l'ascissa del link più profondo che fà parte dell'operatore
     loop  : l'elemento loop**/
    public GraficoLoop(int x,int y,int h,int w, ElementoLoop Loop)
    {
    	
    	
     LoopCorrente = Loop;         
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
   
    /** Disegna l'operatore Loopultaneo. */
    public void paintGraficoLoop(Graphics2D g2D)
    {
        Stroke tmpStroke;
        Paint tmpPaint;
        AffineTransform tmpTransform;
        tmpStroke = g2D.getStroke();
        tmpPaint = g2D.getPaint();
        tmpTransform = g2D.getTransform();
        g2D.setColor(Color.GRAY);
        LoopRectangle = new Rectangle2D.Double(rettangoloX,rettangoloY,larghezza,altezza);
        LoopGraph1 = new Line2D.Double(rettangoloX, rettangoloY+20,rettangoloX+70,rettangoloY+20);
        LoopGraph2 = new Line2D.Double(rettangoloX+80, rettangoloY,rettangoloX+80,rettangoloY+10);
        LoopGraph3 = new Line2D.Double(rettangoloX+80, rettangoloY+10,rettangoloX+70,rettangoloY+20);
        g2D.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND, 10.0f, NOT_DASHED, 0));
        g2D.drawString("Loop"+LoopCorrente.getId()+" ("+LoopCorrente.getMin()+","+LoopCorrente.getMax()+")",rettangoloX+15,rettangoloY+15);               
        g2D.draw(LoopGraph1);
        g2D.draw(LoopGraph2);
        g2D.draw(LoopGraph3);              
        g2D.draw(LoopRectangle);  
        g2D.setStroke(tmpStroke);
        paintSelected(g2D);
        g2D.setPaint(tmpPaint);
        g2D.setTransform(tmpTransform);
        
        
    } 
    
    /** Metodo per aggiornare la posizione del Loopultaneo*/
    public void updateLoopPosizione(int x,int y,int h, int med,int w)
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
    
    /** Se l'oggetto ï¿½ stato selezionato, il metodo aggiorna la 
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
    public boolean isSelected_Loop(Point pnt)
    {           
        return rett_ang;  
    }
    
    /** Verifica se il punto 'pnt' ? sufficientemente vicino al 'centro' del collegamento.
        La costante 'delta' determina quanto il punto 'pnt' deve stare vicino perch? il
        metodo restituisca 'true'. */
    public boolean isSelezionato_Loop(Point pnt)
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
    public boolean isSelezionato_Loop_up_left()
    {        
        return Up_left;
    }
    
    /** Verifica se il punto è vicino al rettangolo in basso a sinistra */
    public boolean isSelezionato_Loop_bot_right()
    {        
        return Bot_right;
    }
    
    /**  **/
    public boolean isSelezionato_Loop_up_right()
    {        
        return Up_right;
    }
    
    /** Verifica se il punto è vicino al rettangolo in basso a sinistra */
    public boolean isSelezionato_Loop_bot_left()
    {        
        return Bot_left;
    }
    
    
    public void Mod_Loop(Point p){
        
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
    
    /** Restituisce l'ordinata della linea media. 
     * @return posizione Y attuale
     * */
    public int getHeight()
    {
        return altezza;     
    }
    
    public Rectangle2D.Double getLoopRectangle(){
        
        return LoopRectangle;
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
             LoopCorrente.updateLoopPosizione();

            validate();
            repaint();
	}
        

    
    
    
}