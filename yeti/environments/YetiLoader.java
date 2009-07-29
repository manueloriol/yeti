package yeti.environments;

/**
 * Class that represents a custom class loader. 
 * 
 * @author Vasileios Dimitriadis (vd508@cs.york.ac.uk, vdimitr@hotmail.com)
 * @date 20 Jul 2009
 *
 */
public abstract class YetiLoader extends ClassLoader {
	
	/**
	 * The classpath of classes to load.
	 */
	protected String []classpaths;


	/**
	 * The general loader.
	 */
	public static YetiLoader yetiLoader;
	
	/**
	 * Constructor that creates a new loader.
	 * 
	 * @param path the classpath to load classes.
	 */
	public YetiLoader(String path) {
		super();
		
		this.classpaths = path.split(System.getProperty("path.separator"));
		yetiLoader = this;
	}
	
	/**
	 * We load all the classes in the classpath.
	 */
	public abstract void loadAllClassesInPath();
	
	/**
	 * We load all classes in a directory.
	 * 
	 * @param directoryName the name of the directory.
	 * @param prefix the prefix for the class.
	 */
	public abstract void loadAllClassesIn(String directoryName, String prefix);
	
	/**
	 * We add the definition of the parameter class to the Yeti structures.
	 * 
	 * @param clazz the class to add.
	 * @return the class that was added.
	 */
	@SuppressWarnings("unchecked")
	public abstract Class addDefinition(Class clazz);
}
