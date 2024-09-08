package ch.interlis.testbed;

import ch.ehi.basics.settings.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.interlis2.validator.Validator;


public class Testbed {

    private static final String TEST_IN = "src/main/data/";
    private static final String XTF_LOGFILE_NAME = "ilivalidator.log.xtf";

    public void run(String dataDirectory) {

        // Alle XTF-Dateien, die nicht eine expected Log-Datei ist.
        List<Path> transferFiles = new ArrayList<Path>();
        try (Stream<Path> walk = Files.walk(Paths.get(dataDirectory))) {
            transferFiles = walk
                    .filter(p -> !Files.isDirectory(p))   
                    .filter(f -> {
                        if (f.toString().toLowerCase().endsWith(".xtf") && !f.toString().toLowerCase().endsWith(".log.xtf")) {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println(transferFiles);

        // Alle gefundenen XTF-Dateien prüfen, die im gleichen Verzeichnis
        // auch eine (expected) Logdatei zum Vergleich liegen haben.
        
               
        
//      Settings settings = new Settings();
//      settings.setValue(Validator.SETTING_XTFLOG, XTF_LOGFILE_NAME); // TODO
//      settings.setValue(Validator.SETTING_ILIDIRS,
//              TEST_IN + "models/;" + TEST_IN + "models/CH;" + TEST_IN + "models/V_D;" + TEST_IN + "models/refhb24");
//      settings.setValue(Validator.SETTING_CONFIGFILE, TEST_IN + "models/DMAV_V1_0_Validierung.ini");
//
//      boolean valid = Validator.runValidation(TEST_IN+"FixpunkteAVKategorie3_LFP3/CH031151.xtf", settings);

        
        
        
        
    }
}
