package yeti.environments.kermeta;

import java.util.ArrayList;

import kyeti.util.KermetaSimplifiedInterpreter;
import yeti.YetiType;
import fr.irisa.triskell.kermeta.language.structure.TypeDefinition;
import fr.irisa.triskell.kermeta.modelhelper.NamedElementHelper;

/**
 * 
 * Class that represents a Kermeta type.
 * Mostly a kermeta class, but can be a Kermeta Enumeration as well.
 * 
 * @author Erwan Bousse (erwan.bousse@gmail.com)
 * @date 28 juil. 2011
 *
 */
public class YetiKermetaType extends YetiType {

	/**
	 * Some kermeta class properties to ignore.
	 */
	private static ArrayList<String> _propertiesToAvoid = null;
	
	/**
	 * Checks if a class property has to be ignored.
	 * @param name The name of the property
	 * @return True if it shoulf be ignored, otherwise false.
	 */
	public static boolean isPropertyToAvoid(String name) {
		if (_propertiesToAvoid ==null) {
			_propertiesToAvoid = new ArrayList<String>();
			// Theses are sime "Object" properties that aren't always defined, so we don't want to consider them in tests.
			_propertiesToAvoid.add("tag");
			_propertiesToAvoid.add("ownedTags");
		}
		return _propertiesToAvoid.contains(name);
	}

	
	/**
	 * Constructor using a simple Type name.
	 * @param name The name of the type (like a Kermeta full class name).
	 */
	public YetiKermetaType(String name) {
		super(name);
	}

	
	/**
	 * Constructor using a Kermeta typedefinition.
	 * @param clazz The Kermeta typedefinition to use.
	 */
	public YetiKermetaType(TypeDefinition clazz) {
		this(NamedElementHelper.getQualifiedName(clazz));
	}


	/**
	 * Initialize important types and their interesting values.
	 */
	public static void initImportantTypes(){
		
		// the unique loader
		YetiKermetaLoader loader =  YetiKermetaLoader.yetiLoader;
		
		// loading some important kermeta classes
		loader.addDefinitionFromFrameworkClasses("kermeta::standard::String");
		loader.addDefinitionFromFrameworkClasses("kermeta::language::structure::Object");
		loader.addDefinitionFromFrameworkClasses("kermeta::standard::Integer");
		loader.addDefinitionFromFrameworkClasses("kermeta::standard::Boolean");

		// we need the kermeta environnement in order to create kermeta values
		KermetaSimplifiedInterpreter interpreter =loader.getInterpreter();

		// boolean interesting values
		YetiType tBoolean = YetiType.allTypes.get("kermeta::standard::Boolean");
		if (tBoolean!=null) {
			tBoolean.addInterestingValues(interpreter.getKermetaBoolean(true));
			tBoolean.addInterestingValues(interpreter.getKermetaBoolean(false));
		}

		// integer interesting values
		YetiType tInt = YetiType.allTypes.get("kermeta::standard::Integer");
		if (tBoolean!=null) {
			for (int j = -10; j<11;j++)
				tInt.addInterestingValues(interpreter.constructKermetaInteger(j));
			tInt.addInterestingValues(interpreter.constructKermetaInteger(Integer.MAX_VALUE));
			tInt.addInterestingValues(interpreter.constructKermetaInteger(Integer.MAX_VALUE-1));
			tInt.addInterestingValues(interpreter.constructKermetaInteger(Integer.MAX_VALUE-2));
			tInt.addInterestingValues(interpreter.constructKermetaInteger(Integer.MIN_VALUE+2));
			tInt.addInterestingValues(interpreter.constructKermetaInteger(Integer.MIN_VALUE+1));
			tInt.addInterestingValues(interpreter.constructKermetaInteger(Integer.MIN_VALUE));
		}
	}
}
