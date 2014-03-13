package it.cnr.jada.firma.arss;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

public class ArubaSignServiceClientTest {


  private static final String INPUT_FILE = "contenuto.txt";

  private static final Logger LOGGER = Logger.getLogger(ArubaSignServiceClientTest.class);

  private static final String USERNAME = "mfraticelli";
  private static final String PASSWORD = "massimo72frat";
  private static final String OTP = "539863";

  @Test
  @Ignore
  public void testPkcs7SignV2() throws IOException, ArubaSignServiceException {
    ArubaSignServiceClient client = new ArubaSignServiceClient();

    InputStream is = ArubaSignServiceClientTest.class.getClassLoader()
        .getResourceAsStream("aruba.properties");
    Properties props = new Properties();
    props.load(is);
    client.setProps(props);

    InputStream iss = ArubaSignServiceClientTest.class.getClassLoader()
        .getResourceAsStream(INPUT_FILE);

    byte[] bytes = IOUtils.toByteArray(iss);

    byte[] content = client.pkcs7SignV2(USERNAME, PASSWORD, OTP, bytes);

    assertTrue(content != null && content.length > 0);
    LOGGER.info(new String(content));

    byte[] out = client.verify(content);
    LOGGER.info("messaggio decriptato");
    LOGGER.info(new String(out));
  }

}
