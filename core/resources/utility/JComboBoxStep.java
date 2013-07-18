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

import javax.swing.JComboBox;

/** La classe estende JComboBox introducendo l'inizializzazione con i valori
	compresi nell'intervallo [inizio,fine] secondo il passo stabilito e
	selezionando il valore passato come parametro. */

public class JComboBoxStep extends JComboBox
{

    /** Memorizza il valore iniziale dell'intervallo. */
    private int start;

    /** Memorizza lo step di risoluzione sull'intervallo. */
    private int step;


    /* Costruttore_ Prende in ingresso l'elemento da selezionare nella 'ComboBox'. */
    public JComboBoxStep(int inizio, int fine, int passo, int valoreattuale)
    {
        super();

        int index = 0;
        int j = 0;

        start = inizio;
        step = passo;
        for (int i=inizio; i<=fine; i=i+passo)
        {
            addItem(String.valueOf(i));
            if (i == valoreattuale)
            {
                index = j;
            }
            j++;
        }
        setSelectedIndex(index);
    }


    /** Restituisce il valore selezionato. */
    public int getSelectedStep()
    {
            return (start + step * getSelectedIndex());
    }

}