package it.cnr.jada.firma.arss;

public class ArubaSignServiceException extends Exception {

  private static final long serialVersionUID = 8826060283776292375L;

  public ArubaSignServiceException() {
  }

  public ArubaSignServiceException(String message) {
    super(message);
  }

  public ArubaSignServiceException(Throwable cause) {
    super(cause);
  }

  public ArubaSignServiceException(String message, Throwable cause) {
    super(message, cause);
  }

}
