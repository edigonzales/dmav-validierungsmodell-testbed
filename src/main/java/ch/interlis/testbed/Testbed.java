package ch.interlis.testbed;

import ch.ehi.basics.settings.Settings;
import org.interlis2.validator.Validator;


public class Testbed {

    private static final String TEST_IN = "src/main/data/";
    private static final String XTF_LOGFILE_NAME = "ilivalidator.log.xtf";

    public void run() {
        
        DynamicDependencyLoader dependencyLoader = new DynamicDependencyLoader();
        try {
            dependencyLoader.loadDependency("ch.interlis", "ilivalidator", "1.14.2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
      Settings settings = new Settings();
      settings.setValue(Validator.SETTING_XTFLOG, XTF_LOGFILE_NAME); // TODO
      settings.setValue(Validator.SETTING_ILIDIRS,
              TEST_IN + "models/;" + TEST_IN + "models/CH;" + TEST_IN + "models/V_D;" + TEST_IN + "models/refhb24");
      settings.setValue(Validator.SETTING_CONFIGFILE, TEST_IN + "models/DMAV_V1_0_Validierung.ini");

      boolean valid = Validator.runValidation(TEST_IN+"FixpunkteAVKategorie3_LFP3/CH031151.xtf", settings);

        
        
        
        
    }
    
    
    private void resolveIlivalidatorDependencies() {
    }
 
}
