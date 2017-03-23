package echo.Tests;
import echo.Tests.ComputationalTests.*;
import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({   BackgroundWorkerTest.class, DetectiveTest.class, SpeechToTextTest.class,
        TextToSpeechTest.class, WolframQueryTest.class, GUITest.class, SoundsTest.class
        })
public class TestSuite {}
