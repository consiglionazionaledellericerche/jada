package it.spasia.generator.properties;
import org.eclipse.osgi.util.NLS;
/**
 * Gestione propriet� di default
 * 
 * @author Marco Spasiano
 * @version 1.0 
 * [22-Aug-2006] creazione
 * [24-Aug-2006] classe rinominata (ex BulkGeneratorDefaults)
 */

	public final class Generation extends NLS {

		private static final String BUNDLE_NAME= "it.spasia.generator.properties.Generation";//$NON-NLS-1$

		private Generation() {
			// Do not instantiate
		}

		static {
			NLS.initializeMessages(BUNDLE_NAME, Generation.class);
		}

		// ----- java names -----
		public static String java_ext;		
		public static String java_base;
		public static String java_key;
		public static String java_home;
		public static String java_bulk;
		
		// ----- xml names -----
		public static String bixml_ext;
		public static String bpixml_ext;
		public static String xml_base;
		public static String xml_key;
		public static String xml_info;
		public static String xml_bulk;
		
		// ------ imports ------
		public static String base_Keyed;
		public static String bulk_ActionContext;
		public static String bulk_OggettoBulk;
		public static String bulk_CRUDBP;
		public static String home_BulkHome;
		public static String home_PersistentCache;
		public static String home_Connection;
		public static String key_KeyedPersistent;
		
		
}
