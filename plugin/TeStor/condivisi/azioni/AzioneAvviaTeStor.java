/*
 * Created on 28-ott-2004
 */
package plugin.TeStor.condivisi.azioni;

//import java.awt.Container;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;

import plugin.TeStor.Principale;
import plugin.TeStor.condivisi.interfaccia.ScrollSequence;
import plugin.TeStor.condivisi.interfaccia.ScrollTestCase;
import plugin.TeStor.data.PlugData;
import plugin.statediagram.data.ListaDP;
//import plugin.TeStor.data.PlugData;
//import core.a02thread.data.ListaDP;


/**
 * Gestisce l'avvio dell'algoritmo TeStor.
 * 
 * @author Fabrizio Facchini
 */
public class AzioneAvviaTeStor implements ActionListener{
	/**
	 * La lista dei SD (nella forma di ScrollSequence) dell'architettura. 
	 */
	private DefaultListModel listaSD;
	/**
	 * Il tab principale del TeStor.
	 */
	private Principale tabPrincipale;
	/**
	 * Costruisce un nuovo oggetto AzioneAvviaTeStor sui parametri specificati.
	 * 
	 * @param listaSD la lista (modello) di SD nel sotto-tab dei SD selezionati
	 * @param tabPrincipale il tab principale del TeStor
	 */
	public AzioneAvviaTeStor(DefaultListModel listaSD, Principale tabPrincipale) {
		super();
		this.listaSD = listaSD;
		this.tabPrincipale = tabPrincipale;
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	/**
	 * Lancia il TeStor sui SD selezionati.
	 * Chiama il metodo {@link plugin.TeStor.condivisi.AlgoritmoTeStor#Esegui(DefaultListModel, PlugData, Container)}
	 * sui SD di quest'oggetto e sulle StateMachine ottenute dal riferimento al tab principale del TeStor.
	 */
	public void actionPerformed(ActionEvent e) {
		// la lista di SequenceElement per il TeStor
		DefaultListModel SDselezionati = new DefaultListModel();
		// la lista dei casi di test
		DefaultListModel casiDiTest;
		// copia in SDselezionati i SequenceElement selezionati per il TeStor
		// Nota: listaSD è una lista di ScrollSequence (dati + grafica)
		// mentre SDselezionati è una lista di SequenceElement (SOLO DATI)
		for (int i = 0; i < listaSD.size(); i++) {
			SDselezionati.addElement(((ScrollSequence)listaSD.get(i)).getSequence());
		}
		casiDiTest = tabPrincipale.algoritmo.esegui(SDselezionati,
		((plugin.statediagram.data.PlugData)tabPrincipale.getPlugData().
			getPlugDataManager().getPlugData("charmy.plugin.state")).getListaDP(),tabPrincipale.getTopLevelAncestor());
		// gestione casi di test a scopo visualizzazione ed interazione con l'utente
		gestioneCasiDiTest(casiDiTest);
	}
	/**
	 * Questo metodo tratta la lista ritornata dall'esecuzione del metodo
	 * {@link plugin.TeStor.condivisi.AlgoritmoTeStor#esegui(DefaultListModel, ListaDP, Container)}.
	 * Per ogni SD in input sostituisce il vecchio {@link ScrollSequence} che lo conteneva
	 * con un nuovo {@link ScrollTestCase} contenente una lista i cui elementi sono
	 * ScrollSequence, ciascuno dei quali contiene un caso di test per quel SD.
	 * 
	 * @param listaCasiDiTest una lista dei casi di test
	 */
	private void gestioneCasiDiTest(DefaultListModel listaCasiDiTest){
		ScrollSequence scrollCorr;
		// scorriamo i vari SD in input al TeStor
		for(int i=0; i<listaSD.size(); i++){
			scrollCorr = (ScrollSequence)listaSD.get(i);
			// inseriamo il SD con associati i suoi casi di test
			
			////ezio 2006 - fixed bug:  java.lang.ArrayIndexOutOfBoundsException: 0 >= 0
			if (listaCasiDiTest.size()>0){
				ScrollTestCase scrollTestCase= new ScrollTestCase(scrollCorr.getSequence(),(DefaultListModel)listaCasiDiTest.get(i),tabPrincipale);			
				listaSD.setElementAt(scrollTestCase,i);
			}
			
		//	listaSD.set(i,new ScrollTestCase(scrollCorr.getSequence(),(DefaultListModel)listaCasiDiTest.get(i),tabPrincipale));
			///////
			
		}
		// aggiorniamo la visualizzazione...
		tabPrincipale.getTabAttivo().aggiornaVisualizzazione();
		// disabilita tutti i pulsanti di spostamento
		tabPrincipale.abilitaPulsSpost(false);		
		tabPrincipale.setGenerazioneAvvenuta(true);
		
		//ezio 2006 - salviamo sul plugData i casi di test generati
		((PlugData)this.tabPrincipale.getPlugData()).setListaCasiDiTest(listaCasiDiTest);
	}
}
