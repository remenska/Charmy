/*
 * Created on 30-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.plugin.feature;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import core.internal.plugin.InternalError;


/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XMLParseFeature extends DefaultHandler {

	
	///DEFINIZIONE TAG DI SITEMA PER LE FEATURE - spostare in una nterfaccia?
	
	public static final String FEATURE = "feature";
	public static final String ID = "id";
	public static final String VERSION = "version";
	public static final String PLUGIN = "plugin";
	public static final String DEFAULT = "default";	
	public static final String VALUE = "value";
	
	
	/////////////////////////////////////////
	
	private boolean fatalErrorOccured=false;
	
	private FeatureDescriptor featureDescriptor	=null;
	private InternalError internalError; 
	
	private boolean parseFeature=false;
	private boolean parsePlugin=false;
	
	/**
	 * 
	 */
	public XMLParseFeature(InternalError internalError) {
		super();
		this.internalError=internalError;
		// TODO Auto-generated constructor stub
	}

	public FeatureDescriptor parseXmlFeature(File XmlFileFeature) {
		// Use an instance of ourselves as the SAX event handler
		
		
		
		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			// Set up output stream
	
			// Parse the input
			SAXParser saxParser = factory.newSAXParser();
			
		
				
				saxParser.parse(XmlFileFeature, this);
				
			
			
			if (this.fatalErrorOccured)
				return null;
			else
				return featureDescriptor;
	
		} catch (Throwable t) {
			
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String namespaceURI, String lName, // local name
			String qName, // qualified name
			Attributes attrs) throws SAXException {
		
		String eName = lName; // element name

		if ("".equals(eName))
			eName = qName; // namespaceAware = false

		// Gestisco gli attributi (Se ce ne sono)
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				String aName = attrs.getLocalName(i); // Attr name
				if ("".equals(aName))
					aName = attrs.getQName(i);
			}
		}

		/*
		 * feature
		 */
		if (eName.equals(FEATURE)) {
			
			String idFeature= attrs.getValue(ID);
			String versionFeature= attrs.getValue(VERSION);
			if ((idFeature!=null)&&(versionFeature!=null)){
				this.featureDescriptor= new FeatureDescriptor(idFeature,versionFeature);
				this.parseFeature=true;
			}
			
		}
		
		if (parseFeature){			
			if (eName.equals(DEFAULT)){
				
				String value = attrs.getValue(VALUE);
				
				if ((value!=null)&&
						(value.compareTo("true")==0)){
					this.featureDescriptor.setFeatureDefault(true);
				}
										
			}
			
		}
		
		if (parseFeature){			
			if (eName.equals(PLUGIN)){
				
				String idPlugin= attrs.getValue(ID);
				String versionPlugin= attrs.getValue(VERSION);
				if ((idPlugin!=null)&&(versionPlugin!=null)){
					
					this.featureDescriptor.addPluginFeature(idPlugin,versionPlugin);
				}
			}
			
		}
		
	}
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException {
		
		if (arg2.equals(FEATURE)) {

			parseFeature = false;

		}
	}
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}
	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	public void fatalError(SAXParseException arg0) throws SAXException {
		// TODO Auto-generated method stub
		super.fatalError(arg0);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	public void error(SAXParseException arg0) throws SAXException {
		// TODO Auto-generated method stub
		super.error(arg0);
	}
}
