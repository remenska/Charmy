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
    
package plugin.topologydiagram.resource.data;

/**
 * @author michele
 *
 * implementazione di IElementoId, tale classe è un passaggio per 
 * simulare l'eredità multipla da parte degli elementi. 
 */
public class ImpElementoId implements IElementoId {

	
	/**
	 * identificativo dell'elemento
	 */
	private long id;

	/**
	 * nome dell'elemento
	 */
	private String nome;

	/**
	 * tipo di elemento
	 */
	private int tipo;
	
	/* (non-Javadoc)
	 * @see data.IElementoId#getId()
	 */
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see data.IElementoId#setId(long)
	 */
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see data.IElementoId#getName()
	 */
	public String getName() {
		return nome;
	}

	/* (non-Javadoc)
	 * @see data.IElementoId#setName(java.lang.String)
	 */
	public void setName(String name) {
		nome = name;
	}

	/* (non-Javadoc)
	 * @see data.IElementoId#getTipo()
	 */
	public int getTipo() {
		return tipo;
	}

	/* (non-Javadoc)
	 * @see data.IElementoId#setTipo(int)
	 */
	public boolean setTipo(int tipo) {
		this.tipo = tipo;	
		return true;
	}

}
