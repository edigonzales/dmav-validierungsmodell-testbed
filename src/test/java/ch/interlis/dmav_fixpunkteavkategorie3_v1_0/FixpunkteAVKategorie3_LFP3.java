package ch.interlis.dmav_fixpunkteavkategorie3_v1_0;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.interlis2.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ch.ehi.basics.settings.Settings;
import ch.interlis.validator.LogEvent;
import ch.interlis.validator.LogEventType;
import ch.interlis.validator.ValidationTestHelper;
import ch.interlis.validator.ValidationTestHelperFOO;

public class FixpunkteAVKategorie3_LFP3 {
    private static final String TEST_IN = "src/test/data/";
    private static final String LOGFILE_NAME = "ilivalidator.log";
    private static final String XTF_LOGFILE_NAME = "ilivalidator.log.xtf";

    // Nachteil: Mit der high level api kann ich schlecht herausfinden, wie viele Fehler gefunden wurden (?)
    
//    ValidationTestHelperFOO vh = null;
    ValidationTestHelper vh = null;

    @BeforeEach
    void setUp() {
//        vh = new ValidationTestHelperFOO();
        vh = new ValidationTestHelper();
        //vh.addFunction(new GetAreaIoxPlugin());
    }

//    @Test
//    public void CH031151(@TempDir Path tempDir) throws Exception {
//
////        vh.runValidation(new String[]{"FixpunkteAVKategorie3/CH031151.xtf"}, new String[]{});
////        
////        System.out.println(vh.getErrs().size());
////        System.out.println(vh.getErrs().get(0));
//
//        vh.runValidation("FixpunkteAVKategorie3/CH031151.xtf", tempDir);
//        
//        
////        String logFileName = Paths.get(tempDir.toFile().getAbsolutePath(), LOGFILE_NAME).toFile().getAbsolutePath();
////        String xtfLogFileName = Paths.get(tempDir.toFile().getAbsolutePath(), XTF_LOGFILE_NAME).toFile().getAbsolutePath();
////        
////        Settings settings = new Settings();
//////        settings.setValue(Validator.SETTING_LOGFILE, logFileName);
////        settings.setValue(Validator.SETTING_XTFLOG, xtfLogFileName);
////        settings.setValue(Validator.SETTING_ILIDIRS, TEST_IN+"models/;"+TEST_IN+"models/CH;"+TEST_IN+"models/V_D;"+TEST_IN+"models/refhb24");
////        settings.setValue(Validator.SETTING_CONFIGFILE, TEST_IN+"models/DMAV_V1_0_Validierung.ini");
//////        
////        boolean valid = Validator.runValidation(TEST_IN+"FixpunkteAVKategorie3/CH031151.xtf", settings);
////        assertFalse(valid);
//////  
////        String content = new String(Files.readAllBytes(Paths.get(xtfLogFileName)));
////        System.out.println(content);
//////        assertTrue(content.contains("Error: line 51: DMAV_FixpunkteAVKategorie3_V1_0.FixpunkteAVKategorie3.LFP3: tid ddfda0fa-c828-4dc9-804c-31b185eebd05: Hoehengometrie darf nicht gleich 0.0 sein"));
//    }
    
    //@ParameterizedTest(name = "{0} is expected to output {1}")
    @ParameterizedTest(name = "{0}")
    @MethodSource("testCases")
    public void fizzBuzz(String inputFile, boolean expectedValidationResult, List<LogEvent> expectedLogEvents, @TempDir Path tempDir) {
//        assertEquals(expected, underTest.fizzBuzz(input));
        System.out.println("input " + inputFile);
        System.out.println("exp: " + expectedValidationResult);
        
        boolean valid = vh.runValidation(inputFile, tempDir);
        
        assertEquals(expectedValidationResult, valid);
        
        List<LogEvent> logEvents = vh.getLogEvents();
//        System.out.println("logEvent: " + logEvent);
        
        {
            List<LogEvent> differences = logEvents.stream()
                    .filter(element -> !expectedLogEvents.contains(element))
                    .collect(Collectors.toList());

            System.out.println("Test result does not correspond to the expected result (expected minus test): " + differences);
            
        }
        {
            List<LogEvent> differences = expectedLogEvents.stream()
                    .filter(element -> !logEvents.contains(element))
                    .collect(Collectors.toList());

            System.out.println("Expected result does not correspond to the test result (test minus expected): " + differences);
            
        }
        
        
    }

    static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(
                        "FixpunkteAVKategorie3/CH031151.xtf", 
                        false, 
                        List.of(
                                new LogEvent(LogEventType.ERROR, "Hoehengeometrie darf nicht gleich 0.0 sein")
                                )
                        ), 
                Arguments.of(
                        "FixpunkteAVKategorie3/CH033551.xtf", 
                        false, 
                        List.of(
                                new LogEvent(LogEventType.ERROR, "Hoehengeometrie darf nicht gleich 0.0 sein")
                                )
                        )
                );
    }

    
//    private static Stream<Arguments> fizzBuzzTestCases() {
//        return Stream.of(
//          Arguments.of(named.of(1, "1 - not divisible by 3 or 5"), "1"),
//          Arguments.of(named.of(2, "2 - not divisible by 3 or 5"), "2"),
//          Arguments.of(named.of(3, "3 - divisible by 3"), "Fizz"),
//          Arguments.of(named.of(4, "4 - not divisible by 3 or 5"), "4"),
//          Arguments.of(named.of(5, "5 - divisible by 5"), "Buzz"),
//          Arguments.of(named.of(6, "6 - divisible by 3"), "Fizz"),
//          Arguments.of(named.of(10, "10 - divisible by 5"), "Buzz"),
//          Arguments.of(named.of(11, "11 - not divisible by 3 or 5"), "11"),
//          Arguments.of(named.of(15, "15 - divisible by both 3 and 5"), "FizzBuzz")
//        );
//    }

}
