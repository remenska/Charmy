/*
 * Created on 15-ott-2004
 */
package plugin.TeStor.condivisi.interfaccia;

import javax.swing.JScrollPane;

import plugin.TeStor.data.PlugData;

import plugin.sequencediagram.SequenceEditor;
import plugin.sequencediagram.data.SequenceElement;
//import core.internal.runtime.data.PlugData;
import core.internal.ui.PlugDataWin;
/**
 * La visuale di un SD nel TeStor.
 * Questa visuale è identica a quella nel Sequence Editor, ma <i>sono inibite
 * tutte le funzioni di editing</i> sui SD: l'unica toolbar connessa è quella di zoom,
 * e click singoli o doppi dentro la visuale non hanno alcun effetto.
 * 
 * @author Fabrizio Facchini
 */
public class ScrollSequence extends JScrollPane {
	/**
	 * Il SequenceEditor del SequenceElement visualizzato in questo oggetto
	 */
	private SequenceEditor sEd;
	
	/**
	 * Costruisce un nuovo oggetto ScrollSequence sul SequenceElement specificato,
	 * in riferimento alle parti grafica e dati di Charmy.
	 * 
	 * @param sEl il SequenceElement da visualizzare
	 * @param pd i dati di Charmy
	 * @param pdw la grafica di Charmy
	 */
	public ScrollSequence(SequenceElement sEl, PlugData pd, PlugDataWin pdw) {
		super();
		// crea il SequenceEditor relativo al SD da visualizzare
		this.sEd = new SequenceEditor(sEl,pd.getPlugDataManager().getPlugData("charmy.plugin.sequence"),pdw);
		inizializza();
	}
	/**
	 * Restituisce il SequenceElement contenuto in questo oggetto
	 * 
	 * @return il SequenceElement visualizzato da questo oggetto
	 */
	public SequenceElement getSequence(){
		return this.sEd.getSequenceElement();
	}
	/**
	 * Restituisce il SequenceEditor contenuto in questo oggetto
	 * 
	 * @return il SequenceEditor utilizzato da questo oggetto per la visualizzazione
	 */
	public SequenceEditor getSeqEd(){
		return this.sEd;
	}
	/**
	 * Disattiva tutti i listener del mouse per evitare di poter modificare il SD
	 * e inserisce l'editor nella viewPort dello ScrollPane.
	 */
	private void inizializza(){
		// ...per i click
		for (int i = 0; i < sEd.getMouseListeners().length; i++) {
			sEd.removeMouseListener(sEd.getMouseListeners()[i]);
		}
		// ...e per gli spostamenti
		for (int i = 0; i < sEd.getMouseMotionListeners().length; i++) {
			sEd.removeMouseMotionListener(sEd.getMouseMotionListeners()[i]);
		}
		// inserisce l'editor nello scrollPane
		super.setViewportView(sEd);
	}
}
