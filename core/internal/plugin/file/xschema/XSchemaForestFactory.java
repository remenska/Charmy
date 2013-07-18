package core.internal.plugin.file.xschema;

import java.io.File;
import java.lang.reflect.Method;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
import com.sun.org.apache.xerces.internal.jaxp.validation.XercesConstants;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSConstants;
import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSModelGroup;
import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
import com.sun.org.apache.xerces.internal.xs.XSObject;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTerm;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

import core.internal.plugin.PlugInDescriptor;
import core.internal.plugin.file.SerializableCharmyFile;

public class XSchemaForestFactory {

	private PlugInDescriptor[] pluginsDescriptor = null;
	private SchemaForest schemaForest=null;
	private String idXmlModel=null;
	SerializableCharmyFile[] pluginsMenagementFile;
	private SerializableCharmyFile currentMenagementFile = null;
	
	public XSchemaForestFactory(PlugInDescriptor[] pluginsDescriptor,String idXmlModel) {
		
		this.pluginsDescriptor=pluginsDescriptor;
		this.idXmlModel = idXmlModel;
		
	}

	public SchemaForest buildSchemaForest() {
		
		if (this.schemaForest==null){
			this.parseSchemaFile();
		}
		
		return this.schemaForest;		
	}
	
	/**
	 * 
	 * @param idPlugFileSave
	 * @return
	 */
	/*public SchemaTree getSchemaTree(String idPlugFileSave){
	
	 // attivazione: costruiamo l'albero relativo ai file schema dei plug che vogliono salvare sul filtro associato al plugin di tipo file passato come argomento
	  if (this.schemaTree==null){			
	
	  //prima ricaviamo gli URL dei file schema
	   Vector result = new Vector();
	   IMainTabPanel[] plugins = this.getPluginToSave(idPlugFileSave);
	
	   for (int i = 0; i < plugins.length; i++) {
	
	
	
	   String dirPlugin = new File(this.plugManager.getPlugEditDescriptor(plugins[i]).getDirectory()).getName();
	   String relPathSchema = this.plugManager.getPlugEditDescriptor(plugins[i]).getXSchemaSave();
	   result.add("plugin"+"/"+dirPlugin+"/"+relPathSchema);
	   }
	
	   String[]  absolutePathSchema= new String[result.size()];
	
	   for (int i = 0; i < result.size(); i++) {
	   absolutePathSchema[i]= (String)result.get(i);
	   }
	
	   this.parseSchemaFile(absolutePathSchema);// qui vengono parsati gli schema e viene costruito l'albero
	   }
	
	   return this.schemaTree;
	   }*/
	
	/**
	 * Parsa i file xschema per il salvataggio-apertura file.
	 * xsd dichiarati dai plugin che intendono salvare/aprire dal plugin di tipo file passato come argomento.
	 * 
	 * @author Ezio Di Nisio
	 * @param pluginFile
	 */
	private void parseSchemaFile(){ 
		
		
		if (this.schemaForest==null){	
			
			this.schemaForest = new SchemaForest();
						
				
			for (int i = 0; i < pluginsDescriptor.length; i++) {
				
				String dirPlugin = new File(pluginsDescriptor[i].getDirectory()).getName();
				String relPathSchema = pluginsDescriptor[i].getXSchemaFile(this.idXmlModel);
				String pathSchemaFile = "plugin"+"/"+dirPlugin+"/"+relPathSchema;
				
				this.currentMenagementFile=pluginsDescriptor[i].getMenagementFile(this.idXmlModel);
				//facciamo dei controlli ed istanziamo la classe del plugin che deve gestire il salvataggio/apertura dei file - querry al plugin
				//si dovrebbe spostare nel FileManager
				//SerializableCharmyFile menagementFile = null;
				
				
				
				
				SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Source schemaFile = new StreamSource(new File(pathSchemaFile));				
				Schema schema = null;				
				XMLGrammarPool xmlSchemaGrammarPool=null;
				
				try {
					
					schema = factory.newSchema(schemaFile);					
					Method newXNIValidator = schema.getClass().getMethod("newXNIValidator", new Class[] {});
					newXNIValidator.setAccessible(true);		    	   
					
					Object insulatedSchemaValidator = newXNIValidator.invoke(schema, new Object[] {});
					
					Method getProperty = insulatedSchemaValidator.getClass().getMethod("getProperty", new Class[] {String.class});
					getProperty.setAccessible(true);	
					
					xmlSchemaGrammarPool = (XMLGrammarPool) getProperty.invoke(insulatedSchemaValidator, new Object[] {XercesConstants.XMLGRAMMAR_POOL});
					
				} catch (Exception e) {e.printStackTrace();}
				
				
				/* SerializableCharmyFile pluginSupportFile=null;
				try {
					pluginSupportFile = (SerializableCharmyFile)plugins[i];
				}
				catch (Exception e) {System.out.println("il plugin non implementa l'interfaccia SerializableCharmyFile " + e);}
				*/
				
				Grammar[] grammars = xmlSchemaGrammarPool.retrieveInitialGrammarSet(XMLGrammarDescription.XML_SCHEMA);				
				SchemaGrammar schemaGrammar = (SchemaGrammar) grammars[0];			   
				
				this.buildSchemaTree(schemaGrammar);
				
			}			
			
		}		
	}

	/**
	 * 
	 * 
	 * @author Ezio
	 * @param schemaGrammar 
	 */
	private void buildSchemaTree(SchemaGrammar schemaGrammar){
		
		//mapGlobalElementsRoot: root elements - radice dell'albero, se pi� di uno � una foresta
		
		XSNamedMap mapGlobalElementsRoot = schemaGrammar.getComponents(XSConstants.ELEMENT_DECLARATION);
		
		//in realt� qui abbiamo sempre un unico elemento root, l'elemento rappresenta la radice dell'albero dello schema relativo ai dati che un plugin vuole salvare
		for (int i = 0; i < mapGlobalElementsRoot.getLength(); i++) {
			
			XSElementDeclaration xsElementRoot= (XSElementDeclaration) mapGlobalElementsRoot.item(i);
			
			SchemaEntry schemaEntry = schemaForest.insert(currentMenagementFile,xsElementRoot,null);
			
			this.handleXSElement(xsElementRoot,schemaEntry,null);			
		}				
	}

	private void handleXSElement (XSElementDeclaration xsElement,SchemaEntry schemaEntry,SchemaEntry schemaEntryParent){
		
		short xsElementRootType = xsElement.getTypeDefinition().getTypeCategory();
		
		
		//gestione complex type (element-only  o mixed)
		if (xsElementRootType==XSTypeDefinition.COMPLEX_TYPE){			
			
			this.handleComplexType(xsElement,schemaEntry);					
			
		}
		
		if (xsElementRootType==XSTypeDefinition.SIMPLE_TYPE){
			
			if (schemaEntry!=null)
				if (schemaEntry.getElementSource().equals(xsElement))
					return;
			
			this.handleSimpleType(xsElement,schemaEntryParent);
			
		}
		
	}

	private void handleComplexType(XSElementDeclaration xsElement,SchemaEntry schemaEntry){
		
		XSComplexTypeDefinition complexTypeElement = (XSComplexTypeDefinition)xsElement.getTypeDefinition();
		
		if ((complexTypeElement.getContentType()==XSComplexTypeDefinition.CONTENTTYPE_ELEMENT) ||
				(complexTypeElement.getContentType()==XSComplexTypeDefinition.CONTENTTYPE_MIXED)){
			
			XSParticleDecl particella = (XSParticleDecl)complexTypeElement.getParticle();
			this.handleParticle(particella,schemaEntry);
			
		}
		
	}

	private void handleSimpleType(XSElementDeclaration xsElement,SchemaEntry schemaEntryParent){
		
		this.schemaForest.insert(this.currentMenagementFile,xsElement,schemaEntryParent);
		
	}

	private void handleParticle(XSParticleDecl particle,SchemaEntry schemaEntryParent){
		
		XSTerm XSchemaTerminal = particle.getTerm(); 
		
		switch (particle.fType) {
		
		case XSParticleDecl.PARTICLE_ELEMENT :
			
			SchemaEntry schemaEntry2 = schemaForest.insert(this.currentMenagementFile,(XSElementDeclaration)particle.fValue,schemaEntryParent);
			
			this.handleXSElement((XSElementDeclaration)particle.fValue,schemaEntry2,schemaEntryParent);
			
			break;
			
		case XSParticleDecl.PARTICLE_MODELGROUP :
			
			XSObjectList listChild = ((XSModelGroup)XSchemaTerminal).getParticles();
			for (int i = 0; i < listChild.getLength(); i++) {
				
				XSObject ob =  listChild.item(i);
				if (ob instanceof XSParticleDecl ){ // controllo forse superfluo
					
					this.handleParticle((XSParticleDecl)ob,schemaEntryParent);
				}			
				
			}
			
			break;
		case XSParticleDecl.PARTICLE_WILDCARD :
			
			break;
		default :
			
			break;
		}
		
	}

}
