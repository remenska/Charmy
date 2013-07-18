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
    

package core.internal.plugin;

/**
 * � una classe astratta per poter estendere la funzionalit� del
 * plugin relativo all'editor e al file gestendone le parti comuni
 * @author michele
 * Charmy plug-in
 **/
public class PlugExtension {

	/**
	 * stringa rappresentante la classe di ingresso al plug-in
	 * classPoint pu� avere solo due valori:
	 * editor oppure file, se � editor il plugin si installa come
	 * nuovo strumento visuale oppure nuovo algoritmo
	 * file invece dice che il plugin riguada esportazione o importazione di files
	 */
	private String classPoint;
	
	public PlugExtension(){
		this(null);
	}
	/**
	 * Costruttore
	 * @param cPoint stringa rappresentante il point
	 */
	public PlugExtension(String cPoint){
		classPoint = cPoint;
	}
	/**
	 * restituisce il tipo di plugin
	 * @return
	 */
	public String getClassPoint() {
		return classPoint;
	}

	/**
	 * setta il tipo di plugin
	 * @param string
	 */
	public void setClassPoint(String string) {
		classPoint = string;
	}



}
