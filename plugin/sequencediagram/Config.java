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
    

package plugin.sequencediagram;

import core.internal.datistatici.CharmyFile;

/**
 *   Classe Config.java che contiene la parte di inizializzazione
 *   variabili e delle costanti per il plugIn.
  *  @author Patrizio Pelliccione
 */
public class Config {

   public static final String SM_SUB_DIR = "a03sequence";
    public static final String ICONS_SUB_DIR = "icon";
 	private static String dirSMPlug = "";
 	private static String dirSMPlugIcons = dirSMPlug;

 	//ezio 2006
    public static String PathGif="/home/daniela/sw/charmy2.0Beta_source/plugin/sequencediagram/icon/";
    /////
	public static final String NewSequenceIMG 	= "newsequence.gif";
	public static final String  NewSequence	= "New Sequence";
	
	public static final String DelSequenceIMG 	= "delsequence.gif";
	public static final String DelSequence	= "Delete Sequence";

	public static final String CopySequenceIMG 	= "copysequence.gif";
	public static final String  CopySequence     	= "Copy Sequence";

	public static final String SynSequenceIMG 	= "synsequence.gif";
	public static final String  SynSequence    	= "Synchronous Message";

	public static final String AsynSequenceIMG 	= "asynsequence.gif";
	public static final String AsynSequence	= "Asynchronous Message";
	
	public static final String ReadySequenceIMG 	= "ready.gif";
	public static final String  ReadySequence     	= "Ready";

	public static final String LoopSequenceIMG 	= "loopsequence.gif";
	public static final String LoopSequence	= "Loop Message";

	public static final String ConstraintIMG 	= "constraint.gif";
	public static final String Constraint	= "Constraint";
                
	public static final String TimeIMG 	= "time.gif";
	public static final String Time	= "Time's Line";
        
        ///////////NEW Flamel/////////////////////////////
        
        public static final String SimIMG 	= "simsequence.gif";
	public static final String Sim	= "Simultaneous operator";
        
        public static final String LoopIMG 	= "loop2sequence.gif";
	public static final String Loop	= "Loop operator";
        
        public static final String ParallelIMG 	= "parsequence.gif";
	public static final String Parallel	= "Parallel operator";
        
        public static final String CoregionIMG 	= "corsequence.gif";
	public static final String Coregion	= "Coregion area operator";
        
       /////////////////////////////////////////////////// 

	public static final String PropSequenceIMG 	= "propsequence.gif";
	public static final String PropSequence	= "Sequence's Properties";

	public static final String ImgSequenceIMG 	= "imgjpeg.gif";
	public static final String  ImgSequence	= "Save Jpeg Image";

	public static final String HelpSequenceIMG 	= "helpsequence.gif";
	public static final String  HelpSequence	= "Sequence Panel Help";
	

    /**
     * Metodo init per inizializzare le variabili
     *
     * @return VOID
     *
     * @author Patrizio Pelliccione
     */
    public static void init(){
    	//ezio 2006
    /*  dirSMPlug = CharmyFile.dirPlug();
      dirSMPlug = CharmyFile.addFileSeparator(dirSMPlug);
      dirSMPlug = dirSMPlug.concat(SM_SUB_DIR);
      dirSMPlug = CharmyFile.addFileSeparator(dirSMPlug);

      dirSMPlugIcons = dirSMPlug.concat(ICONS_SUB_DIR);
      dirSMPlugIcons = CharmyFile.addFileSeparator(dirSMPlugIcons);

      PathGif=dirSMPlugIcons;*/
    }
}
