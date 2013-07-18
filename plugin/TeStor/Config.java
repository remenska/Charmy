/*   Charmy (CHecking Architectural Model consistencY)
 *   Copyright (C) 2004 Patrizio Pelliccione <pellicci@di.univaq.it>. 
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


package plugin.TeStor;

import core.internal.datistatici.CharmyFile;

/**
 *   Classe Config.java che contiene la parte di inizializzazione
 *   variabili e delle costanti per il plugIn.
 * 
  *  @author Patrizio Pelliccione
 */
public class Config {

    public static final String SM_SUB_DIR = "TeStor";
    public static final String ICONS_SUB_DIR = "icon";
 	private static String dirSMPlug = "";
 	private static String dirSMPlugIcons = dirSMPlug;

    public static String PathGif="plugin/TeStor/icon/";
    
	public static final String strHorizIMG 	= "stretchHorizontal.gif";
	public static final String  strHoriz     	= "Espande orizzontalmente";

	public static final String compHorizIMG 	= "compressHorizontal.gif";
	public static final String  compHoriz     	= "Comprime orizzontalmente";

	public static final String strVertIMG 	= "stretchVertical.gif";
	public static final String  strVert     	= "Espande verticalmente";

	public static final String compVertIMG 	= "compressVertical.gif";
	public static final String  compVert     	= "Comprime verticalmente";


    /**
     * Metodo init per inizializzare le variabili 
     *
     * @return VOID
     */
    public static void init(){
     /* dirSMPlug = CharmyFile.dirPlug();
      dirSMPlug = CharmyFile.addFileSeparator(dirSMPlug);
      dirSMPlug = dirSMPlug.concat(SM_SUB_DIR);
      dirSMPlug = CharmyFile.addFileSeparator(dirSMPlug);

      dirSMPlugIcons = dirSMPlug.concat(ICONS_SUB_DIR);
      dirSMPlugIcons = CharmyFile.addFileSeparator(dirSMPlugIcons);

      PathGif=dirSMPlugIcons;*/
    }
}
