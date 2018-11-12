import org.apache.log4j.Logger;
import org.junit.Test;

public class logTest {
    private static Logger logger = Logger.getLogger(logTest.class);

    @Test
    public void logTest() {
        logger.debug("this is a debug");
        logger.info("this is a info");
        logger.warn("this is a warn");
        logger.error("this is an error");
    }
}
