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
    
package plugin.topologychannels.resource.data;


/**
 * @author michele
 *
 * Classe base per la simulazione di una eredit? multipla relativa alle
 * classi IElementoId e IUpdate
 */
public abstract class ElementoBase 
	extends ImpUpdate 
	implements IElementoId {
	
	
	
	/**
	 * elementoId per eredita' multipla
	 */
	private IElementoId elementoId = new ImpElementoId();


	/* (non-Javadoc)
	 * @see data.IElementoId#getId()
	 */
	public long getId() {
		return elementoId.getId();
	}

	/* (non-Javadoc)
	 * @see data.IElementoId#setId(long)
	 */
	public void setId(long id) {
		boolean bo = this.testAndSet();
		elementoId.setId(id);
		this.testAndReset(bo);
	}

	/* (non-Javadoc)
	 * @see data.IElementoId#getName()
	 */
	public String getName() {
		return elementoId.getName();
	}

	/* (non-Javadoc)
	 * @see data.IElementoId#setName(java.lang.String)
	 */
	public void setName(String name) {
		boolean bo = this.testAndSet();
		elementoId.setName(name);
		this.testAndReset(bo);
	}

	/**
	 * ritorna la descrizione da visualizzare
	 * viene ridefinita delle classi discendenti 
	 * @return Stringa rappresentante la descrizione da visualizzare
	 */
	public  String getViewName(){
		return elementoId.getName();
	}
	
		
	/**
	 * ritorna il tipo di elemento
	 */
	public int getTipo() {
		return elementoId.getTipo();
	}

		
	/**
	 * setta il tipo dell'oggetto
	 * @param tipo
	 */
	public boolean setTipo(int tipo) {
		boolean bo = this.testAndSet();
  	    elementoId.setTipo(tipo);
		this.testAndReset(bo);
		return true;
	}

}
