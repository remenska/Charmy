/*
 * Created on 9-ott-2004
 */
package plugin.TeStor.condivisi.interfaccia;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.DefaultListCellRenderer;

import plugin.TeStor.condivisi.interfaccia.ScrollSequence;

/**
 * La classe renderizza le celle delle liste di SD del TeStor.
 * <br>La classe nasce dall'esigenza derivante dal fatto di avere liste di SD, i cui
 * elementi sono {@link ScrollSequence}, e dal dover visualizzare i loro nomi e NON i loro
 * identificatori univoci (cosa che avverrebbe con il DefaultListCellRenderer).
 * 
 * @author Fabrizio Facchini
 */
public class ListaSDCellRenderer extends DefaultListCellRenderer {

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	/**
	 * Restituisce il CellRenderer per il SD alla posizione specificata (int index)
	 * nella la lista di SD specificata (JList list).
	 * <br>Qui si sovrascrive il metodo della classe DefaultListCellRenderer,
	 * sostituendo al parametro <code>value</code> la stringa ottenuta chiamando
	 * {@link plugin.sequencediagram.data.SequenceElement#getNomeSequence()}
	 * sull'elemento della lista, cioè il nome del SD.
	 * 
	 * @return il CellRenderer per uno ScrollSequence
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		return super.getListCellRendererComponent(list,
			((ScrollSequence)list.getModel().getElementAt(index)).getSequence().getNomeSequence(),
			index,isSelected,cellHasFocus);
	}

}
