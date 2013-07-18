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
    

package core.internal.ui.statusbar;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Label;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/** Classe per costruire e gestire una barra di stato. */

public class StatusBar extends JPanel //JComponent
{
    
    /** Label per visualizzare le informazioni sulla barra di stato. */
    private Label Informazioni;

    
    /** Costruttore con stringa d'inizializzazione per la barra di stato. */
    public StatusBar(String stringa)
    {
        super();
        Informazioni = new Label(stringa);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));     
        add(Informazioni);
        setVisible(true);
    }

    
    /** Metodo per definire le caratteristiche del bordo della barra di stato. */ 
    public Insets getInsets()
    {
        return new Insets(0,5,0,0);
    }


	/** Metodo per la "stampa" della barra di stato. */
    public void paint(Graphics g)
    {
        super.paint(g);
    }


	/** Metodo per modificare il testo della barra di stato. */
    public void setText(String stringa)
    {
		Informazioni.setText(stringa);
	}
	
}