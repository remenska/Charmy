/*
 * Created on 23-ott-2004
 */
package plugin.TeStor.condivisi.interfaccia;

import javax.swing.DefaultListModel;

/**
 * Un modello di lista di interi.
 * La classe sfrutta i metodi di DefaultListModel ma su numeri interi.
 * E' nata dall'esigenza di creare DefaultListModel contenente numeri interi e non oggetti.
 * 
 * @author Fabrizio Facchini
 */
public class ListaIndiciInteri extends DefaultListModel {

	/**
	 * Inserisce un numero intero alla posizione specificata
	 * 
	 * @param pos la posizione dell'inserimento
	 * @param n il numero intero da inserire
	 */
	public void add(int pos,int n) {
		Integer oInt = new Integer(n);
		super.add(pos,oInt);
	}
	/**
	 * Inserisce un numero intero
	 * 
	 * @param n il numero intero da inserire
	 */
	public void addElement(int n) {
		Integer oInt = new Integer(n);
		super.addElement(oInt);
	}
	/**
	 * Restituisce il numero intero alla posizione indicata
	 * 
	 * @param pos la posizione d'interesse
	 * @return il numero intero alla posizione indicata
	 */
	public int getInt(int pos) {
		return ((Integer)super.get(pos)).intValue();
	}
	/**
	 * Controlla la presenza del numero intero specificato
	 * 
	 * @param n un numero intero
	 * @return <code>true</code> se il numero intero è presente, <code>false</code> altrimenti
	 */
	public boolean contains(int n) {
		return indexOfInt(n)!=-1;
	}
	/**
	 * Ritorna il numero intero alla prima posizione
	 * 
	 * @return il primo numero intero presente
	 */
	public int firstIntElement() {
		return getInt(0);
	}
	/**
	 * Ritorna il numero intero all'ultima posizione
	 * 
	 * @return l'ultimo numero intero presente
	 */
	public int lastIntElement() {
		return getInt(this.getSize()-1);
	}
	/**
	 * Ritorna la posizione della prima occorrenza del numero intero specificato
	 * 
	 * @param n un numero intero
	 * @return la posizione del numero se presente, -1 altrimenti
	 */
	public int indexOfInt(int n) {
		// esegue una banale ricerca lineare
		for(int i=0;i<this.getSize();i++){
			if(this.getInt(i)==n){
				return i;
			}
		}
		return -1;
	}
	/**
	 * Rimuove il numero intero specificato
	 * 
	 * @param n un numero intero
	 * @return <code>true</code> se il numero intero è presente, <code>false</code> altrimenti
	 */
	public boolean removeElement(int n) {
		int pos;
		
		pos = indexOfInt(n);
		removeElementAt(pos);
		return pos!=-1;
	}
	/**
	 * Sincronizza questo oggetto con l'array specificato.
	 * Copia l'array in questo oggetto, ma lasciando inalterato l'ordine
	 * (in questo oggetto) dei numeri interi contenuti nell'array. Il metodo raggiunge il suo
	 * scopo quando i numeri interi nell'array passato al metodo sono tutti diversi tra loro.
	 * <br>Questa condizione è sempre soddisfatta nel TeStor dall'uso che si fa del metodo,
	 * in quanto esso è chiamato a gestire la possibilità di una evidenziazione qualunque
	 * dei SD nelle liste, mantenendo però l'informazione sulla successione cronologica dei
	 * SD evidenziati, in modo da conoscere sempre l'unico SD da visualizzare, secondo quanto
	 * si vede alla dichiarazione di {@link plugin.TeStor.interfaccia.TeStorTab#indiciCronologici}.
	 * 
	 * @param intArray un array di numeri interi
	 */
	public void sincr(int[] intArray) {
		// lista di numeri interi d'appoggio
		ListaIndiciInteri temp = new ListaIndiciInteri();
		
		// inizializza temp: copia questo oggetto in temp
		for(int i=0;i<this.getSize();i++){
			temp.addElement(this.getInt(i));
		}		
		// aggiunge a questo oggetto i numeri interi evidenziati che non conteneva
		for(int i=0;i<intArray.length;i++){
			if(!this.contains(intArray[i])){
				this.addElement(intArray[i]);
			}else{// aggiornamento di temp...
				temp.removeElement(intArray[i]);
			}
		}
		// rimuove da questo oggetto i numeri interi in più rispetto a quelli nell'array,
		// che, per costruzione, sono quelli contenuti in temp
		for(int i=0;i<temp.getSize();i++){
			this.removeElement(temp.getInt(i));
		}
	}
}