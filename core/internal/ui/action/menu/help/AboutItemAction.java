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
    

package core.internal.ui.action.menu.help;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * @author Patrizio
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
/**	La classe risponde alla pressione dell'item "Help"
	nel menu "Help". */
	
public class AboutItemAction implements ActionListener
{
    
    public void actionPerformed(ActionEvent p1) 
    {
		String titolo="About...";
		String messaggio=core.internal.datistatici.CharmyFile.DESCRIZIONE+"\r\n(c) Copyright SEA Group, University of L'Aquila."+
			 "\r\nComputer Science Department. All right reserved."+
			 "\r\n\r\nProject Advisor:\r\n" +
			 "                 Paola Inverardi - inverard@di.univaq.it"+
			 "\r\nProject Members:\r\n"+
			 "                 Patrizio Pelliccione - pellicci@di.univaq.it"+
			 "\r\n                 Henry Muccini - muccini@di.univaq.it"+
			 "\r\n                 Mauro Caporuscio - caporusc@di.univaq.it" +			 "\r\n\r\n      http://www.di.univaq.it/charmy";

		Toolkit SystemKit = Toolkit.getDefaultToolkit();
		Image img = SystemKit.getImage(core.internal.datistatici.CharmyFile.createURLPath("core/internal/icon/"+core.internal.datistatici.CharmyFile.ABOUT_IMAGE,"/Charmy.jar"));
		JOptionPane.showMessageDialog(null, messaggio,titolo,JOptionPane.INFORMATION_MESSAGE,new ImageIcon(img));
    }
    
    

}