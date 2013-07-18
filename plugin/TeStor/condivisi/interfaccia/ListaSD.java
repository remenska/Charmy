/*
 * Created on 9-ott-2004
 */
package plugin.TeStor.condivisi.interfaccia;

import javax.swing.JList;
import javax.swing.ListModel;

import plugin.TeStor.condivisi.interfaccia.ListaSDCellRenderer;

/**
 * Una lista di SD (rappresentati come {@link plugin.TeStor.condivisi.interfaccia.ScrollSequence}).
 * E' una banale JList col settaggio del CellRenderer su {@link plugin.TeStor.condivisi.interfaccia.ListaSDCellRenderer}
 * 
 * @author Fabrizio Facchini
 */
public class ListaSD extends JList {
	/**
	 * Costruisce un nuovo oggetto ListaSD sulla base del modello di SD specificato.
	 * Perché funzioni è necessario che gli oggetti nel modello siano
	 * esclusivamente di tipo {@link ScrollSequence} (o di una sua sottoclasse).
	 * Questa condizione è sempre soddisfatta nel TeStor dall'uso che si fa di questa classe.
	 * 
	 * @param listModel il modello su cui costruire la lista
	 */
	public ListaSD(ListModel listModel){
		super(listModel);
		this.setCellRenderer(new ListaSDCellRenderer());
	}
	
}
