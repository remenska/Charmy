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
    

package plugin.charmyfile.info;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import plugin.charmyfile.Tag;

/**
 * @author michele
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Informazioni {

	
	
	/**
	 * crea le intestazioni per il file
	 *
	 */
	public Element creaInformazioni(Document doc){
		Element intesta =  doc.createElement(Tag.info);
		
		Element plug_nome = doc.createElement(Tag.plug_name);///plug_nome viene persa poi...a che serve??
		intesta.appendChild(plug_nome);
		
		Text testo = doc.createTextNode("Charmy save");
		plug_nome.appendChild(testo);
		return intesta;
	}
	
	
}
