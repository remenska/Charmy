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
    

package core.resources.utility;

import java.awt.Color;

import javax.swing.JComboBox;

/** La classe estende JComboBox introducendo l'inizializzazione con le stringhe
	corrispondenti alle "costanti colore" definite in Java e selezionando quella
	relativa al colore passato come parametro. */

public class JComboBoxColor extends JComboBox
{

    /** Vettore dei colori disponibili. */
    static final Color[] coloridisponibili = {Color.black, Color.blue, Color.cyan,
        Color.darkGray, Color.gray, Color.green, Color.lightGray, Color.magenta,
        Color.orange, Color.pink, Color.red, Color.white, Color.yellow};


    /* Costruttore_ Prende in ingresso l'elemento da selezionare nella 'ComboBox'. */
    public JComboBoxColor(Color colore)
    {        
        super();

        int index = 0;

        addItem("Black");
        addItem("Blue");
        addItem("Cyan");
        addItem("DarkGray");
        addItem("Gray");
        addItem("Green");
        addItem("LightGray");
        addItem("Magenta");
        addItem("Orange");
        addItem("Pink");
        addItem("Red");
        addItem("White");
        addItem("Yellow");
        for (int i=0; i < coloridisponibili.length; i++)
        {
            if (colore.equals(coloridisponibili[i]))
            {
                index = i;
            }
        }
        setSelectedIndex(index);
    }


    /** Restituisce l'oggetto colore corrispondente all'item selezionato. */
    public Color getSelectedColor()
    {
       return(coloridisponibili[getSelectedIndex()]);
    }

}