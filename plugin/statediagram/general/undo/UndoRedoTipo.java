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
    

package plugin.statediagram.general.undo;

/**
 * @author Michele Stoduto
 * Progetto Charmy
 * Tipo di undo redo da gestire
 */
public interface UndoRedoTipo {
	
	static final int ADD_PROCESSO    		= 1;
	static final int REMOVE_PROCESSO 	= 2;
	static final int UPDATE_PROCESSO 	= 3;
	
	static final int ADD_CANALE    				= 4;
	static final int REMOVE_CANALE 		= 5;
	static final int UPDATE_CANALE 			= 6;
	
	
	static final int ADD_LISTATHREAD        = 7;
	static final int REMOVE_LISTATHREAD = 8;
	static final int UPDATE_LISTATHREAD  = 9;
	
	
	//static final int ADD_PROCESSO = 1;

	//nuovo tread creato in listaThrea
	static final int ADD_THREADELEMENT			= 10;
	static final int REMOVE_THREADELEMENT	= 11;
	static final int UPDATE_THREADELEMENT	= 12;


	static final int ADD_THREAD							= 13;
	static final int REMOVE_THREAD					= 14;
	static final int UPDATE_THREAD					= 15;


	//nuovo tread creato in listaThrea
	static final int ADD_STATO							= 16;
	static final int REMOVE_STATO					= 17;
	static final int UPDATE_STATO						= 18;
	
   //	nuovo tread creato in listaThrea
	static final int ADD_MESSAGGIO					= 19;
	static final int REMOVE_MESSAGGIO			= 20;
	static final int UPDATE_MESSAGGIO			= 21;
	
	

}
