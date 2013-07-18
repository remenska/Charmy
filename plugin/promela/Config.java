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
    

package plugin.promela;

import core.internal.datistatici.CharmyFile;

/**
 *   Classe Config.java che contiene la parte di inizializzazione
 *   variabili e delle costanti per il plugIn.
  *  @author Patrizio Pelliccione
 */
public class Config {

    public static final String SM_SUB_DIR = "promela";
    public static final String ICONS_SUB_DIR = "icon";
 	private static String dirSMPlug = "";
 	private static String dirSMPlugIcons = dirSMPlug;
 	 

    public static String PathGif="plugin/promela/icon/";
    
	public static final String PromelaIMG 	= "uptext.gif";
	public static final String  Promela     	= "Promela Code Generation";
		

    /**
     * Metodo init per inizializzare le variabili
     *
     * @return VOID
     *
     * @author Patrizio Pelliccione
     */
    public static void init(){
   /*   dirSMPlug = CharmyFile.dirPlug();
      dirSMPlug = CharmyFile.addFileSeparator(dirSMPlug);
      dirSMPlug = dirSMPlug.concat(SM_SUB_DIR);
      dirSMPlug = CharmyFile.addFileSeparator(dirSMPlug);

      dirSMPlugIcons = dirSMPlug.concat(ICONS_SUB_DIR);
      dirSMPlugIcons = CharmyFile.addFileSeparator(dirSMPlugIcons);

      PathGif=dirSMPlugIcons;*/
    }
}
