package core.internal.plugin.file.xschema;

import java.util.ArrayList;
import java.util.Vector;

import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;

import core.internal.plugin.file.SerializableCharmyFile;

public class SchemaForest {
	
	
	//private Hashtable forest = new Hashtable();
	
	private Vector forest = null;
	public SchemaForest() {
		
		this.forest = new Vector();
	}
	
	public SchemaEntry insert (SerializableCharmyFile plugin,XSElementDeclaration xsElement, SchemaEntry schemaEntryParent ){
		
		if ((plugin==null)||(xsElement==null))
			return null;
		
	/*	SchemaEntry schemaTemp = this.getSchemaEntry(xsElement);
		if (schemaTemp!=null)
			return schemaTemp;*/
					
		SchemaEntry schemaEntry = new SchemaEntry (xsElement);
		//SchemaEntry schemaEntryParent = this.getSchemaEntry(xsElementParent);
		
		if (schemaEntryParent!=null)
		schemaEntryParent.appendChild(schemaEntry);
		
		schemaEntry.setParent(schemaEntryParent);
		schemaEntry.setPlugin(plugin);
		
		this.insert(schemaEntry,plugin);
		
		//Vector tree = (Vector)this.forest.get(plugin);
		/*Vector tree = this.getTree(plugin);
		if (tree ==null){
			
			tree = new Vector();
			tree.add(schemaEntry);
			Vector data = new Vector(2);
			data.add(0,plugin);
			data.add(1,tree);
			//this.forest.put(plugin,tree);
		}
		else
			tree.add(schemaEntry);*/
		
		
		return schemaEntry;
	}
	
	
	/*public SchemaEntry[] getSchemaTree(SerializableCharmyFile plugin){
		
		if (plugin==null)
			return new SchemaEntry[0];
		
		Vector tree = (Vector)this.forest.get(plugin);
		if (tree == null)
			return new SchemaEntry[0];
		
		SchemaEntry[] schemaEntrys = new SchemaEntry[tree.size()];
		
		for (int i = 0; i <tree.size(); i++) {
			
			schemaEntrys[i]= (SchemaEntry)tree.get(i);			
		}
		
		return schemaEntrys;		
		
	}*/
	
	
	public SchemaEntry[] getRoot (SerializableCharmyFile plugin){
		
		if (plugin==null)
			return null;
		
		//Vector tree = (Vector)this.forest.get(plugin);
		Vector tree = this.getTree(plugin);
		if (tree ==null)
			return null;
		
		ArrayList result = new ArrayList();
		for (int j = 0; j <tree.size(); j++) {
			
			if (((SchemaEntry)tree.get(j)).getParent()==null)
				result.add(tree.get(j));
		}		
		
		return (SchemaEntry[]) result.toArray(
				new SchemaEntry[result.size()]);  	
		
	}
	
	/*public Iterator iterator(){
		Set set = this.forest.entrySet();
		return set.iterator();
	}*/
	
	public Vector getTree(SerializableCharmyFile plugin){
		
		for (int j = 0; j <this.forest.size(); j++) {
			
			if (((SerializableCharmyFile)((Vector)forest.get(j)).get(0)).equals(plugin))
				return (Vector)((Vector)forest.get(j)).get(1);
		}
		return null;
	}
	
	private void insert (SchemaEntry schemaEntry,SerializableCharmyFile plugin){
		
		if ((schemaEntry==null)||(plugin==null))
			return;
		
		boolean bo = false;
		for (int j = 0; j <this.forest.size(); j++) {
			
			if (((SerializableCharmyFile)((Vector)forest.get(j)).get(0)).equals(plugin)){
				
				((Vector)((Vector)forest.get(j)).get(1)).add(schemaEntry);
				bo=true;
			}
			
		}
		
		if (!bo){
			Vector data = new Vector(2);
			data.add(0,plugin);
			Vector tree = new Vector();
			tree.add(schemaEntry);
			data.add(1,tree);
			this.forest.add(data);
			
		}
			
	}
	
	public SchemaEntry getSchemaEntry(XSElementDeclaration xsElement){
		
		if (xsElement==null)
			return null;
		
		for (int i = 0; i <this.forest.size(); i++) {
			Vector tree = (Vector)((Vector)forest.get(i)).get(1);
			for (int j = 0; j <tree.size(); j++) {
				
				SchemaEntry currentEntry=(SchemaEntry)tree.get(j);
				if (currentEntry.getElementSource().equals(xsElement))
					return currentEntry;
				
			}			
		}
		
			
		/*Iterator ite = this.iterator();
		while(ite.hasNext()){
			
			Entry entry = (Entry)ite.next();
			Vector tree = (Vector)entry.getValue();
			
			for (int i = 0; i <tree.size(); i++) {
				
				SchemaEntry schemaEntry = (SchemaEntry)tree.get(i);
				if (schemaEntry.getElementSource().equals(xsElement))
					return schemaEntry;
			}
			
		}*/
		
		return null;
	}

	public Vector getForest() {
		return forest;
	}
	
	public SerializableCharmyFile[] getPlugins(){
		
		ArrayList result = new ArrayList();
		
		for (int j = 0; j <this.forest.size(); j++) {
			result.add((SerializableCharmyFile)((Vector)forest.get(j)).get(0));
			
		}
		
		return (SerializableCharmyFile[]) result.toArray(
				new SerializableCharmyFile[result.size()]);  	
		
	}
	
}
