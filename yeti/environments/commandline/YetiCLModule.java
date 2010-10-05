package yeti.environments.commandline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import yeti.Yeti;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Class that represents a module for the command-line.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Jul 28, 2010
 *
 */
public class YetiCLModule extends YetiModule {

	/**
	 * Class that represents an XML parser for parsing the description.
	 * 
	 * @author Manuel Oriol (manuel@cs.york.ac.uk)
	 * @date Aug 3, 2010
	 *
	 */
	public class YetiXMLContentHandler extends DefaultHandler2{


		String currentCommand = "";
		private boolean nextIsName=false;
		private boolean nextIsArgument=false;
		private boolean nextIsReturnType = false;
		private Vector<YetiType> args = new Vector<YetiType>();
		private YetiModule module;

		public YetiXMLContentHandler(YetiModule module) {
			super();
			this.module = module;
		}


		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (length>0&&nextIsName) {
				currentCommand=new String(ch).substring(start,start+length);	
				nextIsName = false;
				System.out.println("Name is: "+new String(ch).substring(start,start+length));
			}
			if (length>0&&nextIsArgument) {
				nextIsArgument = false;
				String name = new String(ch).substring(start,start+length);
				System.out.println("Argument is: \""+name+"\"");
				args.add(YetiType.allTypes.get(name));
			}
			if (length>0&&nextIsReturnType) {
				nextIsReturnType = false;
				// do not need return type in command line (process always returns a integer value)
				/*
				String typeName = new String(ch).substring(start,start+length);
				System.out.println("Return Type is: "+ typeName);
				if (YetiType.allTypes.containsKey(typeName))
					args.add(new YetiType(typeName));
				else 
					args.add(YetiType.allTypes.get(typeName));
				*/
			}
		}

		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			if (qName.equals("NAME"))
				nextIsName=true;
			if (qName.equals("ARGUMENT"))
				nextIsArgument=true;
			if (qName.equals("RETURN_TYPE"))
				nextIsReturnType=true;
//			System.out.println(qName);
		}

		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equals("COMMAND")) {
				YetiCLRoutine yclr = new YetiCLRoutine(new YetiName(currentCommand), currentCommand, args.toArray(new YetiType[0]), module);
				args.clear();
				System.out.println("generated command: "+yclr);
				module.addRoutineInModule(yclr);

			}
		}

		
	}
	
	/**
	 * A creation procedure for the module.
	 * 
	 * 
	 * @param moduleName the module to load and create.
	 */
	public YetiCLModule(String moduleName) {	
		super(moduleName);
		String []ypath = Yeti.yetiPath.split(":");
		for (String path: ypath) {
			File f = new File(path+System.getProperty("file.separator")+moduleName);
			yeti.YetiLog.printDebugLog("Trying file: "+f.getName(), this);

			if (f.exists()&&f.isFile()&&moduleName.endsWith(".ycl")) {
				String fileName = path + "/" + moduleName;
				try {
					SAXParser in = SAXParserFactory.newInstance().newSAXParser();
					in.parse(new InputSource (new FileInputStream(fileName)),new YetiXMLContentHandler(this));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * A creation procedure for the module.
	 * 
	 * 
	 * @param moduleName the module to load and create.
	 */
	public YetiCLModule(String moduleName, String path) {	
		super(moduleName);

		String fileName = path + System.getProperty("file.separator") + moduleName;
		try {
			SAXParser in = SAXParserFactory.newInstance().newSAXParser();
			in.parse(new InputSource (new FileInputStream(fileName)),new YetiXMLContentHandler(this));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public static void main(String []args) {
		new YetiCLModule(args[0],".");
		
	}
}
