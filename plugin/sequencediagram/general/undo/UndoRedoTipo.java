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
    

package plugin.sequencediagram.general.undo;

/**
 * @author Michele Stoduto
 * Progetto Charmy
 * Tipo di undo redo da gestire
 */
public interface UndoRedoTipo {
	
	
	//eventi generati da SequenceElement 
	static final int ADD_TIME				= 22;
	static final int REMOVE_TIME			= 23;
	static final int UPDATE_TIME			= 24;
	
	//eventi per una classe
	static final int ADD_CLASSE				= 25;
	static final int REMOVE_CLASSE		= 26;
	static final int UPDATE_CLASSE			= 27;

	
	
	
	//	eventi per una link
	  static final int ADD_LINK				= 28;
	  static final int REMOVE_LINK		= 29;
	  static final int UPDATE_LINK		= 30;
	  
	  
	  
	  //eventi per una classe
	  static final int ADD_CONSTRAINT			= 31;
	  static final int REMOVE_CONSTRAINT	= 32;
	  static final int UPDATE_CONSTRAINT	= 33;
	  
	   //eventi per un'operatore parallelo
	  static final int ADD_PARALLEL			= 34;
	  static final int REMOVE_PARALLEL	= 35;
	  static final int UPDATE_PARALLEL	= 36;
          
          //eventi per un'operatore simultaneo
	  static final int ADD_SIM			= 37;
	  static final int REMOVE_SIM	= 38;
	  static final int UPDATE_SIM	= 39;
          
          //eventi per un'operatore loop
	  static final int ADD_LOOP			= 40;
	  static final int REMOVE_LOOP	= 41;
	  static final int UPDATE_LOOP	= 42;


}
