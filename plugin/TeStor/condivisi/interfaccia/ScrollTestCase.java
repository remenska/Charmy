/*
 * Created on 24-feb-2005
 */
package plugin.TeStor.condivisi.interfaccia;

import javax.swing.DefaultListModel;

import plugin.TeStor.Principale;
import plugin.TeStor.interfaccia.TestCaseTab;
import plugin.sequencediagram.data.SequenceElement;

/**
 * 
 * @author Fabrizio Facchini
 */
public class ScrollTestCase extends ScrollSequence {
	/**
	 * I casi di test implicati dal SequenceElement associato.
	 * Contiene una lista di oggetti di tipo {@link ScrollSequence}.
	 */
	private DefaultListModel casiDiTest = new DefaultListModel();
	/**
	 * L'interfaccia per la visualizzazione dei casi di test.
	 */
	private TestCaseTab tabCasiDiTest;
	/**
	 * Formatta un numero intero.
	 * Dato il numero intero specificato lo converte in una stringa
	 * di lunghezza il numero specificato, anteponendo, se possibile, tante volte
	 * il carattere specificato quante sono necessarie a raggiungere la lughezza voluta.
	 * La stringa restituita è la normale rappresentazione decimale del numero
	 * se esso risulta negativo o se la lunghezza della rappresentazione stessa
	 * eccede la quantità di caratteri (cifre) specificata.
	 * 
	 * @param numero un numero
	 * @param numCaratteri la lunghezza desiderata per la rappresentazione formattata
	 * @param carattere il carattere da anteporre alla rappresentazione decimale del numero
	 * @return la stringa rappresentante il numero formattato
	 */
	public static String nCaratteri(int numero, int numCaratteri, char carattere){
		String stringaNumero = ""+numero;
		int differenza = numCaratteri-stringaNumero.length();
		if(numero>=0){
			for(int i=0; i<differenza; i++){
				stringaNumero=carattere+stringaNumero;
			}
		}
		return stringaNumero;	
	}
	/**
	 * Formatta il numero specificato anteponendo degli zeri.
	 * 
	 * @param numero un numero
	 * @param numCaratteri la lunghezza finale del numero formattato
	 * @return il numero specificato con degli zeri davanti
	 * @see ScrollTestCase#nCaratteri(int, int, char)
	 */
	private static String formattaNumero(int numero, int numCaratteri){
		return nCaratteri(numero,numCaratteri,'0');
	}
	/**
	 * Costruisce un nuovo oggetto ScrollTestCase sul SD e sui dati del plugin specificati.
	 * Associa al SD la lista dei suoi casi di test
	 * specificati come elementi di tipo {@link plugin.sequencediagram.data.SequenceElement},
	 * creando un tab {@link TestCaseTab} che visualizza la lista dei casi di test.
	 * 
	 * @param sEl un SD dell'architettura
	 * @param casiDiTest la lista dei casi di test implicati dal SD
	 * @param tabPrincipale il tab principale del TeStor
	 */
	public ScrollTestCase(SequenceElement sEl, DefaultListModel casiDiTest, Principale tabPrincipale) {
		super(sEl, tabPrincipale.getPD(), tabPrincipale.getPlugDataWin());
		for(int i=0; i<casiDiTest.getSize(); i++){
			ScrollSequence ssCorr = new ScrollSequence((SequenceElement)casiDiTest.get(i),tabPrincipale.getPD(),tabPrincipale.getPlugDataWin());
			// diamo un nome al caso di test corrente
			ssCorr.getSeqEd().getSequenceElement().setName("TeStor["+sEl.getNomeSequence()+"]."+formattaNumero(i,String.valueOf(casiDiTest.size()-1).length()));
			// aggiungiamo il caso di test agli altri
			this.casiDiTest.addElement(ssCorr);
		}
		tabCasiDiTest = new TestCaseTab(this.casiDiTest,tabPrincipale);
	}
	/**
	 * Restituisce i casi di test (lista di oggetti di tipo {@link ScrollSequence})
	 * implicati dal SD associato.
	 * 
	 * @return il campo casiDiTest.
	 */
	public DefaultListModel getCasiDiTest() {
		return casiDiTest;
	}
	/**
	 * Restituisce il tab per la visualizzazione dei casi di test.
	 * 
	 * @return il campo tabCasiDiTest.
	 */
	public TestCaseTab getTabCasiDiTest() {
		return tabCasiDiTest;
	}
}
