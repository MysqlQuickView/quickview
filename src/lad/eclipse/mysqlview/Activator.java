package lad.eclipse.mysqlview;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import lad.eclipse.mysqlview.templates.DocumentDll;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "lad.eclipse.mysqlview";
	private static Activator plugin;
	public static final String IMAGE_DATABASE = "icons/database.gif";
	public static final String IMAGE_ACCESS = "icons/sample.gif";
	public static final String IMAGE_MYSQL = "icons/mysql.gif";
	public static final String IMAGE_DEFAULT = "icons/hierarchicalLayout.png";
	public static final String IMAGE_TABLE = "icons/prop_ps.png";
	public static final String IMAGE_COLUMN = "icons/artifact.gif";
	private HashMap<String, Image> imageMap = new HashMap<String, Image>();
	private File dbFile;

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		getDbFile();
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public Image getImage(String path) {
		Image image = (Image) this.imageMap.get(path);
		if (image == null) {
			image = getImageDescriptor(path).createImage();
			this.imageMap.put(path, image);
		}
		return image;
	}
	
	public File getDbFile(){
		if(dbFile==null){
			File file = new File(getDefault().getStateLocation().toFile(), "template.db");
		    if(!file.exists()){
		    	try {
					file.createNewFile();
					DocumentDll.initSqlite(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		    dbFile = file;
		}
		return dbFile;
		
	}
}