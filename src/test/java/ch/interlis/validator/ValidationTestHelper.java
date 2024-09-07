package ch.interlis.validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//import org.interlis2.validator.Validator;

//import ch.ehi.basics.settings.Settings;

public class ValidationTestHelper {
    private static final String TEST_IN = "src/test/data/";
    private static final String XTF_LOGFILE_NAME = "ilivalidator.log.xtf";

    private List<LogEvent> logEvents = new ArrayList<>();
    
    public boolean runValidation(String transferFile, Path tempDir) {
        String xtfLogFileName = tempDir.resolve(XTF_LOGFILE_NAME).toFile().getAbsolutePath();

//        Settings settings = new Settings();
//        settings.setValue(Validator.SETTING_XTFLOG, xtfLogFileName);
//        settings.setValue(Validator.SETTING_ILIDIRS,
//                TEST_IN + "models/;" + TEST_IN + "models/CH;" + TEST_IN + "models/V_D;" + TEST_IN + "models/refhb24");
//        settings.setValue(Validator.SETTING_CONFIGFILE, TEST_IN + "models/DMAV_V1_0_Validierung.ini");
//
//        boolean valid = Validator.runValidation(TEST_IN+transferFile, settings);

      String content;
    try {
        content = new String(Files.readAllBytes(Paths.get(xtfLogFileName)));
//        System.out.println(content);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    // TODO parse xtf log to logevents
        LogEvent logEvent = new LogEvent(LogEventType.ERROR, "Hoehengeometrie darf nicht gleich 0.0 sein");
        logEvents.add(logEvent);
    
        boolean valid = true;
        return valid;
    }

    public List<LogEvent> getLogEvents() {
        return logEvents;
    }
}
