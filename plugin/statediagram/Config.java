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
    

package plugin.statediagram;

import java.awt.event.KeyEvent;

import core.internal.datistatici.CharmyFile;


/**
 *   Classe Config.java che contiene la parte di inizializzazione
 *   variabili e delle costanti per il plugIn.
  *  @author Patrizio Pelliccione
 */
public class Config {

    public static final String SM_SUB_DIR = "statediagram";
    public static final String ICONS_SUB_DIR = "icon";
 	private static String dirSMPlug = "";
 	private static String dirSMPlugIcons = dirSMPlug;

    public static String PathGif="plugin/statediagram/icon/";
	public static final String CheckIMG 	= "check.png";
	public static final String  Check     	= "Check";
	public static final int    CheckMN  	= KeyEvent.VK_C;
	
	public static final String NewThreadIMG 	= "newthread.gif";
	public static final String  NewThread     	= "New Thread";

	public static final String CopyThreadIMG 	= "copythread.gif";
	public static final String  CopyThread     	= "Copy Thread";

	public static final String DelThreadIMG 	= "delthread.gif";
	public static final String  DelThread     	= "Delete Thread";

	public static final String RenameThreadIMG 	= "propstate.gif";
	public static final String  RenameThread     	= "Thread properties";
	
	public static final String ReadySequenceIMG 	= "ready.gif";
	public static final String  ReadySequence     	= "Ready";

	public static final String StartStateIMG 	= "start.gif";
	public static final String  StartState	= "Start State";

	public static final String BuildStateIMG 	= "build.gif";
	public static final String  BuildState	= "Build State";
                
	public static final String SynMessageIMG 	= "synstate.gif";
	public static final String  SynMessage	= "Synchronous Message";

	public static final String AsynMessageIMG 	= "asynstate.gif";
	public static final String  AsynMessage	= "Asynchronous Message";

	public static final String LoopMessageIMG 	= "loopstate.gif";
	public static final String  LoopMessage	= "Loop Message";

	public static final String ImgStateIMG 	= "imgjpeg.gif";
	public static final String  ImgState	= "Save Jpeg Image";

	public static final String HelpStateIMG 	= "helpstate.gif";
	public static final String  HelpState	= "State Panel Help";
	

    /**
     * Metodo init per inizializzare le variabili
     *
     * @return VOID
     *
     * @author Patrizio Pelliccione
     */
    public static void init(){
  /*    dirSMPlug = CharmyFile.dirPlug();
      dirSMPlug = CharmyFile.addFileSeparator(dirSMPlug);
      dirSMPlug = dirSMPlug.concat(SM_SUB_DIR);
      dirSMPlug = CharmyFile.addFileSeparator(dirSMPlug);

      dirSMPlugIcons = dirSMPlug.concat(ICONS_SUB_DIR);
      dirSMPlugIcons = CharmyFile.addFileSeparator(dirSMPlugIcons);

      PathGif=dirSMPlugIcons;*/
    }
}
