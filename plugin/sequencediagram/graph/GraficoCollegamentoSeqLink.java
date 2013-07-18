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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import plugin.sequencediagram.data.ElementoTime;
import plugin.topologydiagram.resource.data.ElementoBase;
import plugin.topologydiagram.resource.graph.ElementoBox;
import plugin.topologydiagram.resource.graph.Freccia;
import plugin.topologydiagram.resource.graph.GraficoLink;

/** La classe serve per disegnare un collegamento tra due processi
	oppure un link per scambio di messaggi tra due stati. */

public class GraficoCollegamentoSeqLink extends GraficoLink implements Serializable {

	/** La prima parte del link ? rappresentata con un arco. */
	transient private Arc2D.Double inizio;

	/** La parte centrale del link ? rappresentata con una linea. */
	transient private Line2D.Double centro;

	/** La parte finale del link ? rappresentata con un arco. */
	transient private Arc2D.Double fine;

	/** Ascissa del punto finale del link. */
	private double xend;

	/** Ordinata del punto finale del link. */
	private double yend;

	/** Memorizza un valore pari ad 1/3 della distanza tra i punti
	    di coordinate (xstart,ystart) e (xend,yend). */
	private double p;

	/**
	 * elementi box di partenza ed arrivo del collegamento
	 */
	private ElementoBox elementoBox_start;
	private ElementoBox elementoBox_end;
	private ElementoTime elementoTime_start;
	private ElementoTime elementoTime_end;
	
	
	
	/** Costruttore_
	 * @param ElementoBox box1	: Elemento iniziale di collegamento
	 * @param ElementoBox box1	: Elemento finale di collegamento
	 * @param	i		: parametro che determina la distanza e la posizione del
				          collegamento rispetto alla congiungente tra i punti (x1,y1) e (x2,y2)_
	 * @param flusso	: parametro per determinare l'orientamento della freccia. 
	 * @param ElementBase contenete le informazioni da visualizzare
	 **/
	
	public GraficoCollegamentoSeqLink(
			ElementoBox box1, 
			ElementoBox box2, 
			ElementoTime time1, 
			ElementoTime time2, 	
			int i,
			boolean flusso,
			ElementoBase update)
	{
		
		super(update);
		elementoBox_start	= box1;
		elementoBox_end		= box2;
		elementoTime_start	= time1;
		elementoTime_end	= time2;

		int deltaangle = 0;

		update.disable();
		calcolaStartEnd(i);        
		InitVariables();
		calcolaValoriGrafici(flusso);
		
		update.enabled();
		
	}
	
	
	/**
	 * calcola i punti di partenza e di arrivo
	 * per il canale
	 * @param posizione da riempire con l'arco
	 */
	private void calcolaStartEnd(int posizione){
						
		int x_two = (elementoBox_end.getPointMiddle()).x;
		int y_two = elementoTime_end.getMinY() + ElementoTime.hfascia;
		xstart = elementoBox_start.getPointMiddle().x;
		ystart = elementoTime_start.getMinY() + ElementoTime.hfascia;

		if ((elementoBox_start.getPointMiddle().x == elementoBox_end.getPointMiddle().x)
		     &&(elementoBox_start.getPointMiddle().y == elementoBox_end.getPointMiddle().y)){
			xend = x_two  + 5;
			yend = y_two + 5;
                        x_end=xend;
                        y_end=yend;
		}
		else{
			xend = x_two ;
			yend = y_two ;
                        x_end=xend;
                        y_end=yend;
		}
		if ((posizione%2)==0){
			posCollegamento = -posizione/2;
		}
		else{
			posCollegamento = (posizione+1)/2;
		}
		
	}
	
	
	/**
	 * funzione per il calcolo dei valori 
	 * relativi al disegno
	 *
	 */
	private void calcolaValoriGrafici(boolean flusso){
		int deltaangle = 0;
		
		p = (Math.sqrt(Math.pow((xend - xstart),2) + Math.pow((yend - ystart),2)))/3;
		if (posCollegamento < 0){
			deltaangle = 90;
		}
		inizio = new Arc2D.Double(xstart,ystart-Math.abs(posCollegamento)*DEFAULT_LINK_DISTANCE,2*p,
				2*Math.abs(posCollegamento)*DEFAULT_LINK_DISTANCE,90+deltaangle,90,0);
		centro = new Line2D.Double(xstart+p,ystart-posCollegamento*DEFAULT_LINK_DISTANCE,
				xstart+2*p,ystart-posCollegamento*DEFAULT_LINK_DISTANCE);
		fine = new Arc2D.Double(xstart+p,ystart-Math.abs(posCollegamento)*DEFAULT_LINK_DISTANCE,2*p,
				2*Math.abs(posCollegamento)*DEFAULT_LINK_DISTANCE,0-deltaangle,90,0);
		flex = new Freccia(Math.round((float)(xstart+1.5*p)), 
				Math.round((float)(ystart-posCollegamento*DEFAULT_LINK_DISTANCE)),update.getTipo(),((flusso)? 0:1));
		testolayout = new TextLayout(update.getViewName(), new Font(testofont,fontstile,fontdimensione),
				DEFAULT_FONTRENDERCONTEXT);
		testoX = (float)(xstart+1.5*p-0.5*(testolayout.getBounds().getWidth()));
		testoY = (float)(ystart-posCollegamento*DEFAULT_LINK_DISTANCE-fontdimensione);
		rectSelFirst = new Rectangle2D.Double(xstart+p-3,ystart-posCollegamento*DEFAULT_LINK_DISTANCE-3,6,6);
		rectSelSecond = new Rectangle2D.Double(xstart+2*p-3,ystart-posCollegamento*DEFAULT_LINK_DISTANCE-3,6,6);        
	}
	
	
	/** Permette di modificare l'orientamento della freccia e
	    di cambiarne la forma in funzione del tipo di messaggio. */

	public synchronized void updateFreccia(int TipoMessaggio, boolean flusso) {
		boolean bo = update.getStato();
		if(bo){
			update.informPreUpdate();
			update.disable();
		}  
		flex =
			new Freccia(
				Math.round((float) (xstart + 1.5 * p)),
				Math.round(
					(float) (ystart
						- posCollegamento * DEFAULT_LINK_DISTANCE)),
				TipoMessaggio,
				((flusso) ? 0 : 1));
		if (bo) {
			update.enabled();
			update.informPostUpdate();
		}

	}

	/** Permette di aggiornare le propriet? grafiche del testo. 
	 * */
	public void updateTestoProprieta(
		Color nuovo_testocolore,
		String nuovo_testofont,
		int nuovo_fontstile,
		int nuova_fontdimensione) {
		boolean bo = update.getStato();
		if (bo) {
			update.informPreUpdate();
			update.disable();
		}
		testocolore = nuovo_testocolore;
		testofont = nuovo_testofont;
		fontstile = nuovo_fontstile;
		fontdimensione = nuova_fontdimensione;
		testolayout = new TextLayout(update.getViewName(), new Font(testofont,fontstile,fontdimensione),
				DEFAULT_FONTRENDERCONTEXT);
		testoX = (float)(xstart+1.5*p-0.5*(testolayout.getBounds().getWidth()));
		testoY = (float)(ystart-posCollegamento*DEFAULT_LINK_DISTANCE-fontdimensione);
		if (bo) {
			update.enabled();
			update.informPostUpdate();
		}
	}

	/** Permette l'aggiornamento della posizione del collegamento a seguito della variazione
	    delle coordinate dei punti centrali dei processi interessati. */
	public void updateCollegamentoPosizione(
		int TipoMessaggio,
		boolean flusso) {
		boolean bo = update.getStato();
		if (bo) {
			update.disable();
		}
		
		calcolaStartEnd(TipoMessaggio);	
		calcolaValoriGrafici(flusso);

		if (bo) {
			update.enabled();
		}

	}


	/** Verifica se il punto 'pnt' ? sufficientemente vicino al 'centro' del collegamento_
	    La costante 'delta' determina quanto il punto 'pnt' deve stare vicino perch? il
	    metodo restituisca 'true'. */
	public boolean isSelezionato(Point pnt) {
		final int delta = 5;
		double angolo;
		double seno;
		double coseno;
		int[] ascisse = new int[4];
		int[] ordinate = new int[4];
		Polygon poly;
		boolean risultato;

		angolo = Math.atan((yend-ystart)/(xend-xstart));
		if (xend < xstart){
			angolo = angolo + Math.PI;
		}
		seno = Math.sin(angolo);
		coseno = Math.cos(angolo);
		ascisse[0] = Math.round((float)(xstart + p*coseno+(posCollegamento*DEFAULT_LINK_DISTANCE-delta)*seno));
		ascisse[1] = Math.round((float)(xstart + p*coseno+(posCollegamento*DEFAULT_LINK_DISTANCE+delta)*seno));
		ascisse[3] = Math.round((float)(xstart + 2*p*coseno+(posCollegamento*DEFAULT_LINK_DISTANCE-delta)*seno));
		ascisse[2] = Math.round((float)(xstart + 2*p*coseno+(posCollegamento*DEFAULT_LINK_DISTANCE+delta)*seno));
		ordinate[0] = Math.round((float)(ystart + p*seno-(posCollegamento*DEFAULT_LINK_DISTANCE-delta)*coseno));
		ordinate[1] = Math.round((float)(ystart + p*seno-(posCollegamento*DEFAULT_LINK_DISTANCE+delta)*coseno));
		ordinate[3] = Math.round((float)(ystart + 2*p*seno-(posCollegamento*DEFAULT_LINK_DISTANCE-delta)*coseno));
		ordinate[2] = Math.round((float)(ystart + 2*p*seno-(posCollegamento*DEFAULT_LINK_DISTANCE+delta)*coseno));
		poly = new Polygon(ascisse,ordinate,4);
		risultato = poly.contains(pnt);
		// Nel prototipo ? scritto che le due istruzioni seguenti sono state aggiunte
		// per consentire al 'Garbage Collection' di rimuovere l'oggetto temporaneo 'poly'.
		ascisse = null;
		ordinate = null;
		return risultato;
	}

	/** Disegna il collegamento. */
	public void paintGraficoLink(Graphics2D g2D) {
		Stroke tmpStroke;
		Paint tmpPaint;
		AffineTransform tmpTransform;
		double angolo;

		tmpStroke = g2D.getStroke();
		tmpPaint = g2D.getPaint();
		tmpTransform = g2D.getTransform();
		angolo = Math.atan((yend-ystart)/(xend-xstart));
		if (xend < xstart){
			angolo = angolo + Math.PI;
		}
		g2D.rotate(angolo,xstart,ystart);
		g2D.setStroke(new BasicStroke(collegamentospessore,BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND,10.0f,collegamentotratteggio,0));
		g2D.setColor(collegamentocolore);
		g2D.draw(inizio);
		g2D.draw(centro);
		g2D.draw(fine);
		flex.paintFreccia(g2D);
		if (xend < xstart){
			g2D.rotate(Math.PI,xstart+1.5*p,ystart-posCollegamento*DEFAULT_LINK_DISTANCE);
		}
		g2D.setColor(testocolore);
		g2D.setFont(new Font(testofont,fontstile,fontdimensione));
		testolayout.draw(g2D,testoX,testoY);
		g2D.setStroke(tmpStroke);
		paintSelected(g2D);        
		g2D.setPaint(tmpPaint);
		g2D.setTransform(tmpTransform);
	}

	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
	public void restoreFromFile(int TipoMessaggio, boolean flusso) {
		calcolaStartEnd(TipoMessaggio);	
		calcolaValoriGrafici(flusso);
				
	}
	
}