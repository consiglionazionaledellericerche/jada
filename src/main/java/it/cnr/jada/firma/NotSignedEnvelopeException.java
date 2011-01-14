package it.cnr.jada.firma;

public class NotSignedEnvelopeException extends it.cnr.jada.comp.ApplicationException {

	public NotSignedEnvelopeException() {
		super();
	}

	public NotSignedEnvelopeException(String s) {
		super(s);
	}

	public NotSignedEnvelopeException(String s, Throwable detail) {
		super(s, detail);
	}

	public NotSignedEnvelopeException(Throwable detail) {
		super(detail);
	}
}
