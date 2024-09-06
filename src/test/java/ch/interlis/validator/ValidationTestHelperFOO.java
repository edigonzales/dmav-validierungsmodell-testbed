package ch.interlis.validator;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxLogEvent;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox_j.IoxIliReader;
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.utility.ReaderFactory;
import ch.interlis.iox_j.validator.InterlisFunction;
import ch.interlis.iox_j.validator.ValidationConfig;
//import ch.interlis.iox_j.validator.Validator;
import org.interlis2.validator.Validator;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ValidationTestHelperFOO {
    private final HashMap<String, Class<InterlisFunction>> userFunctions = new HashMap<>();
    private LogCollector logCollector;

    public void runValidation(String[] dataFiles, String[] modelFiles) throws IoxException, Ili2cFailure {
        dataFiles = addLeadingTestDataDirectory(dataFiles);
        modelFiles = addLeadingTestDataDirectory(modelFiles);
//        modelFiles = appendGeoWFunctionsExtIli(modelFiles);

        logCollector = new LogCollector();
        LogEventFactory errFactory = new LogEventFactory();
        errFactory.setLogger(logCollector);

        Settings settings = new Settings();
        settings.setValue(Validator.SETTING_CONFIGFILE, "src/test/data/models/DMAV_V1_0_Validierung.ini");
        //settings.setTransientObject(ch.interlis.iox_j.validator.Validator.CONFIG_CUSTOM_FUNCTIONS, userFunctions);

        //TransferDescription td = ch.interlis.ili2c.Ili2c.compileIliFiles(new ArrayList<>(Arrays.asList(modelFiles)), new ArrayList<String>());
        
        ArrayList<String> models = new ArrayList<String>() {{
            add("DMAVTYM_Alles_V1_0"); 
//            add("DMAV_V1_0_Validierung"); 
        }};
        ArrayList<String> modeldir = new ArrayList<String>() {{
            add("src/test/data/models/");
            add("src/test/data/models/V_D");
            add("src/test/data/models/CH");
            add("src/test/data/models/refhb24");
        }};
        TransferDescription td = ch.interlis.ili2c.Ili2c.compileIliModels(models, modeldir);
        
        ValidationConfig modelConfig = new ValidationConfig();
        modelConfig.mergeIliMetaAttrs(td);

        PipelinePool pool = new PipelinePool();
        ch.interlis.iox_j.validator.Validator validator = new ch.interlis.iox_j.validator.Validator(td, modelConfig, logCollector, errFactory, pool, settings);

        for (String filename : dataFiles) {
            System.out.println("filename: " + filename);
            IoxReader ioxReader = new ReaderFactory().createReader(new java.io.File(filename), errFactory, settings);
            if (ioxReader instanceof IoxIliReader) {
                ((IoxIliReader) ioxReader).setModel(td);

                errFactory.setDataSource(filename);
                td.setActualRuntimeParameter(ch.interlis.ili2c.metamodel.RuntimeParameters.MINIMAL_RUNTIME_SYSTEM01_CURRENT_TRANSFERFILE, filename);
                try {
                    IoxEvent event;
                    do {
                        event = ioxReader.read();
                        validator.validate(event);
                    } while (!(event instanceof EndTransferEvent));
                } finally {
                    ioxReader.close();
                }
            }
        }
    }
    
    private String[] appendGeoWFunctionsExtIli(String[] modelDirs) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String functionsExtIliPath = "src/model/GeoW_FunctionsExt.ili";
        ArrayList<String> result = new ArrayList<>();
        result.add(functionsExtIliPath);
        result.addAll(Arrays.asList(modelDirs));
        return result.toArray(new String[0]);
    }

    @SuppressWarnings("unchecked")
    public void addFunction(InterlisFunction function) {
        userFunctions.put(function.getQualifiedIliName(), (Class<InterlisFunction>) function.getClass());
    }

    public String[] addLeadingTestDataDirectory(String[] files) {
        return Arrays
                .stream(files).map(file -> Paths.get("src/test/data", file).toString())
                .distinct()
                .toArray(String[]::new);
    }

    public ArrayList<IoxLogEvent> getErrs() {
        return logCollector.getErrs();
    }

    public ArrayList<IoxLogEvent> getWarn() {
        return logCollector.getWarn();
    }

}
