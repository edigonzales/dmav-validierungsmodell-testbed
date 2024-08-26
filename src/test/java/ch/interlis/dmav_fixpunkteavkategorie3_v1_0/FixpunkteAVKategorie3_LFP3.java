package ch.interlis.dmav_fixpunkteavkategorie3_v1_0;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.interlis2.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ch.ehi.basics.settings.Settings;
import ch.interlis.validator.ValidationTestHelper;

public class FixpunkteAVKategorie3_LFP3 {
    private static final String TEST_IN = "src/test/data/";
    private static final String LOGFILE_NAME = "ilivalidator.log";

    // Nachteil: Mit der high level api kann ich schlecht herausfinden, wie viele Fehler gefunden wurden (?)
    
    ValidationTestHelper vh = null;

    @BeforeEach
    void setUp() {
        vh = new ValidationTestHelper();
        //vh.addFunction(new GetAreaIoxPlugin());
    }

    @Test
    public void CH031151(@TempDir Path tempDir) throws Exception {

        vh.runValidation(new String[]{TEST_IN+"FixpunkteAVKategorie3_LFP3/CH031151.xtf"}, new String[]{});

        
//        String logFileName = Paths.get(tempDir.toFile().getAbsolutePath(), LOGFILE_NAME).toFile().getAbsolutePath();
//        
//        Settings settings = new Settings();
//        settings.setValue(Validator.SETTING_LOGFILE, logFileName);
//        settings.setValue(Validator.SETTING_ILIDIRS, "https://models.geo.admin.ch;"+TEST_IN+"models/;");
//        settings.setValue(Validator.SETTING_CONFIGFILE, TEST_IN+"models/DMAV_V1_0_Validierung.ini");
//        
//        boolean valid = Validator.runValidation(TEST_IN+"FixpunkteAVKategorie3_LFP3/CH031151.xtf", settings);
//        assertFalse(valid);
//  
//        String content = new String(Files.readAllBytes(Paths.get(logFileName)));        
//        assertTrue(content.contains("Error: line 51: DMAV_FixpunkteAVKategorie3_V1_0.FixpunkteAVKategorie3.LFP3: tid ddfda0fa-c828-4dc9-804c-31b185eebd05: Hoehengometrie darf nicht gleich 0.0 sein"));
    }
}
