import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileWriter;

//--Eadgyth--/
import utils.FileUtils;
import utils.SystemParams;
import ui.ViewSettingWin;

/**
 * The preferences for the editor and for projects. The editor
 * properties correspond to the keys defined as constants in this
 * class. Other key/property pairs can be added.
 */
public final class Prefs {
   /**
    * Key for the indent unit which is a certain number of spaces */
   public final static String INDENT_UNIT_KEY = "IndentUnit";
   /**
    * Key for the font name */
   public final static String FONT_KEY = "Font";
   /**
    * Key for the font size */
   public final static String FONT_SIZE_KEY = "FontSize";
   /**
    * Key for showing line numbers; a 'Yes-No' property */
   public final static String LINE_NR_KEY = "LineNumbers";
   /**
    * Key for enabling wordwrap; a 'Yes-No' property */
   public final static String WORDWRAP_KEY = "Wordwrap";
   /**
    * Key for showing the toolbar; a 'Yes-No' property */
   public final static String TOOLBAR_KEY = "Toolbar";
   /**
    * Key for showing the status bar; a 'Yes-No' property */
   public final static String STATUSBAR_KEY = "Statusbar";
   /**
    * Key for showing the tabbar; a 'Yes-No' property */
   public final static String TABBAR_KEY = "Tabbar";
   /**
    * Key for showing the file view; a 'Yes-No' property */
   public final static String FILE_VIEW_KEY = "FileView";
   /**
    * Key for the icon size which is 'Small' or 'Large' */
   public final static String ICON_SIZE_KEY = "IconSize";
   /**
    * Key for the background theme which is 'White' or 'Dark' */
   public final static String THEME_KEY = "Theme";
   /**
    * Key for the LaF which is 'System' or 'Java default' */
   public final static String LAF_KEY = "LaF";
   /**
    * Key for the language which is a constant in {@link Languages}
    * as String */
   public final static String LANG_KEY = "Language";
   /**
    * Key for the last used directory to save or open a file */
   public final static String RECENT_DIR_KEY = "RecentDir";
   /**
    * Prefix for keys of properties of the exchange editor */
   public final static String EXCHG_PREFIX = "Exchg";
   /**
    * The name of the properties file to store the configuration
    * of a project */
   public final static String PROJ_CONFIG_FILE = "ProjConfig.properties";

   private final static String PREFS_FILE
         = SystemParams.EADGYTH_DATA_DIR + "/Prefs.properties";

   private final static Properties PREF_PROP = new Properties();

   private final Properties projConfigProp = new Properties();
   private final Properties prop;
   private final File file;

  /**
   * Creates a <code>Prefs</code> that reads from and writes to the
   * Prefs.properties file.
   * <p>
   * The Prefs file is stored in the .eadgyth folder (see
   * {@link SystemParams EADGYTH_DATA_DIR}) if the folder exists.
   * <p>
   * Every new <code>Prefs</code> object accesses the same set of
   * properties which are loaded upon first creation. If the Prefs
   * file does not (yet) exist the editor properties are pre-set.
   */
   public Prefs() {
      prop = PREF_PROP;
      file = new File(PREFS_FILE);
      if (file.exists()) {
         if (prop.isEmpty()) {
            load();
         }
      }
      else {
         setProperty(INDENT_UNIT_KEY, "   ");
         setProperty(FONT_KEY, "Monospaced");
         setProperty(EXCHG_PREFIX + FONT_KEY, "Monospaced");
         setProperty(FONT_SIZE_KEY, "9");
         setProperty(EXCHG_PREFIX + FONT_SIZE_KEY, "9");
         setProperty(LINE_NR_KEY, "Yes");
         setProperty(WORDWRAP_KEY, "No");
         setProperty(TOOLBAR_KEY, "Yes");
         setProperty(STATUSBAR_KEY, "Yes");
         setProperty(TABBAR_KEY, "Yes");
         setProperty(FILE_VIEW_KEY, "No");
         setProperty(ICON_SIZE_KEY, ViewSettingWin.ICON_SIZES[1]);
         setProperty(THEME_KEY, ViewSettingWin.THEME_OPT[0]);
         setProperty(LAF_KEY, ViewSettingWin.LAF_OPT[1]);
         setProperty(LANG_KEY,  String.valueOf(Languages.NORMAL_TEXT));
         setProperty(EXCHG_PREFIX + LANG_KEY, String.valueOf(Languages.NORMAL_TEXT));
         setProperty(RECENT_DIR_KEY, "");
      }
   }

   /**
    * Creates a <code>Prefs</code> that reads from and writes to a
    * ProjConfig.properties file in the specified project directory.
    * The properties are loaded if the file exists. Every new
    * <code>Prefs</code> object accesses an own set of properties.
    *
    * @param projectDir  the directory
    */
   public Prefs(String projectDir) {
      prop = projConfigProp;
      file = new File(projectDir + "/" + PROJ_CONFIG_FILE);
      load();
   }

   /**
    * Sets a new value for the property that corresponds to the
    * specified key or adds a new key/value pair if the key is
    * not contained in the property list
    *
    * @param key  the property key
    * @param value  the new value
    */
   public void setProperty(String key, String value) {
      prop.setProperty(key, value);
   }

   /**
    * Sets a new value for the 'Yes-No' property that corresponds
    * to the specified key or adds a new key/value pair if the key
    * is not contained in the property list
    *
    * @param key  the property key
    * @param state  true for 'Yes', false for 'No'
    */
   public void setYesNoProperty(String key, boolean state) {
      String value = state ? "Yes" : "No";
      prop.setProperty(key, value);
   }

   /**
    * Returns the value of the property that corresponds to the
    * specified key
    *
    * @param key  the property key
    * @return  the value; the empty String if the property could not be found
    */
   public String property(String key) {
      return prop.getProperty(key, "");
   }

   /**
    * Returns if the value of the 'Yes-No' property that corresponds
    * to the specified key is 'Yes'
    *
    * @param key  the property key
    * @return  true if the value is 'Yes', false otherwise
    */
   public boolean yesNoProperty(String key) {
      return "Yes".equals(property(key));
   }

   /**
    * Loads the properties from this properties file if it exists
    */
   public void load() {
      if (!file.exists()) {
         return;
      }
      try (FileInputStream reader = new FileInputStream(file)) {
         prop.load(reader);
      }
      catch (IOException e) {
         FileUtils.log(e);
      }
   }

   /**
    * Writes the properties to this properties file if the parent
    * directory of the file exists
    */
   public void store() {
      if (!file.getParentFile().exists()) {
         return;
      }
      try (FileWriter writer = new FileWriter(file)) {
         prop.store(writer, null);
      }
      catch (IOException | NullPointerException | ClassCastException e) {
         FileUtils.log(e);
      }
   }
}
