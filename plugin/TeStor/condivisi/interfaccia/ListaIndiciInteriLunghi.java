/*
 * Created on 23-ott-2004
 */
package plugin.TeStor.condivisi.interfaccia;

import javax.swing.DefaultListModel;

/**
 * Un modello di lista di interi lunghi.
 * La classe sfrutta i metodi di DefaultListModel ma su numeri interi lunghi.
 * 
 * @author Fabrizio Facchini
 */
public class ListaIndiciInteriLunghi extends DefaultListModel {

	/**
	 * Inserisce un numero intero lungo alla posizione specificata
	 * 
	 * @param pos la posizione dell'inserimento
	 * @param n il numero intero lungo da inserire
	 */
	public void add(int pos,long n) {
		Long oLong = new Long(n);
		super.add(pos,oLong);
	}
	/**
	 * Inserisce un numero intero lungo
	 * 
	 * @param n il numero intero da inserire
	 */
	public void addElement(long n) {
		Long oLong = new Long(n);
		super.addElement(oLong);
	}
	/**
	 * Restituisce il numero intero lungo alla posizione indicata
	 * 
	 * @param pos la posizione d'interesse
	 * @return il numero intero lungo alla posizione indicata
	 */
	public long getLong(int pos) {
		return ((Long)super.get(pos)).longValue();
	}
	/**
	 * Controlla la presenza del numero intero lungo specificato
	 * 
	 * @param n un numero intero lungo
	 * @return <code>true</code> se il numero è presente, <code>false</code> altrimenti
	 */
	public boolean contains(long n) {
		return indexOfLong(n)!=-1;
	}
	/**
	 * Ritorna il numero intero alla prima posizione
	 * 
	 * @return il primo numero intero presente
	 */
	public long firstLongElement() {
		return getLong(0);
	}
	/**
	 * Ritorna il numero intero lungo all'ultima posizione
	 * 
	 * @return l'ultimo numero intero lungo presente
	 */
	public long lastLongElement() {
		return getLong(this.getSize()-1);
	}
	/**
	 * Ritorna la posizione della prima occorrenza del numero intero lungo specificato
	 * 
	 * @param n un numero intero lungo
	 * @return la posizione del numero se presente, -1 altrimenti
	 */
	public int indexOfLong(long n) {
		// esegue una banale ricerca lineare
		for(int i=0;i<this.getSize();i++){
			if(this.getLong(i)==n){
				return i;
			}
		}
		return -1;
	}
	/**
	 * Rimuove il numero intero lungo specificato
	 * 
	 * @param n un numero intero lungo
	 * @return <code>true</code> se il numero è presente, <code>false</code> altrimenti
	 */
	public boolean removeElement(long n) {
		int pos;
		
		pos = indexOfLong(n);
		removeElementAt(pos);
		return pos!=-1;
	}
}