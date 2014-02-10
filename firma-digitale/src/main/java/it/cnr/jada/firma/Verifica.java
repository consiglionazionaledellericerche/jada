package it.cnr.jada.firma;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import it.actalis.ellips.capi.*;

public class Verifica {

	private final static int BUFFSIZE = 1024;
	private static byte buff1[] = new byte[BUFFSIZE];
	private static byte buff2[] = new byte[BUFFSIZE];


	public static void verificaBustaFirmata(File fInput) throws FileNotFoundException, CapiException {
		StreamSignedEnvelope sse=null;
		sse = new StreamSignedEnvelope(new FileInputStream(fInput), new FileOutputStream(new File(fInput.getPath()+".data"))); 	//parses the enveloper from file "in" and save the contained data in "data"
		String[] certs = sse.listCertificates();  //gets a list of contained certificates
		String[] signersDN = sse.listSigners();   //gets a list of signers
		for (int i=0;i<signersDN.length;i++) {
			sse.verify(0);
			//sse.getSignerCertificate(i);
			//sse.getSigningTime(i, null);
		}
	}

	public static File estraiFile(File fInput) throws FileNotFoundException, CapiException, NotSignedEnvelopeException {
		StreamSignedEnvelope sse=null;
		File fOutput = new File(fInput.getPath()+".data");
		FileOutputStream fos = new FileOutputStream(fOutput);
		try {
			sse = new StreamSignedEnvelope(new FileInputStream(fInput), fos);
		}
		catch (CapiException ex) {
			throw new NotSignedEnvelopeException();
		}
		return fOutput;
	}
	
	public static boolean inputStreamEquals(InputStream is1, InputStream is2) {
		if(is1 == is2) return true;
		if(is1 == null && is2 == null) return true;
		if(is1 == null || is2 == null) return false;
		try {
			int read1 = -1;
			int read2 = -1;

			do {
				int offset1 = 0;
				while (offset1 < BUFFSIZE
               				&& (read1 = is1.read(buff1, offset1, BUFFSIZE-offset1)) >= 0) {
            				offset1 += read1;
        			}

				int offset2 = 0;
				while (offset2 < BUFFSIZE
               				&& (read2 = is2.read(buff2, offset2, BUFFSIZE-offset2)) >= 0) {
            				offset2 += read2;
        			}
				if(offset1 != offset2) return false;
				if(offset1 != BUFFSIZE) {
					Arrays.fill(buff1, offset1, BUFFSIZE, (byte)0);
					Arrays.fill(buff2, offset2, BUFFSIZE, (byte)0);
				}
				if(!Arrays.equals(buff1, buff2)) return false;
			} while(read1 >= 0 && read2 >= 0);
			if(read1 < 0 && read2 < 0) return true;	// both at EOF
			return false;

		} catch (Exception ei) {
			return false;
		}
	}

	public static boolean fileContentsEquals(File file1, File file2) {
		InputStream is1 = null;
		InputStream is2 = null;
		if(file1.length() != file2.length()) return false;

		try {
			is1 = new FileInputStream(file1);
			is2 = new FileInputStream(file2);

			return inputStreamEquals(is1, is2);

		} catch (Exception ei) {
			return false;
		} finally {
			try {
				if(is1 != null) is1.close();
				if(is2 != null) is2.close();
			} catch (Exception ei2) {}
		}
	}

	public static boolean fileContentsEquals(String fn1, String fn2) {
		return fileContentsEquals(new File(fn1), new File(fn2));
	}
	
	public static void fileCopy(File inputFile, File outputFile) throws IOException {
		FileInputStream in = new FileInputStream(inputFile);
		FileOutputStream out = new FileOutputStream(outputFile);
		int c;

	    while ((c = in.read()) != -1)
	      out.write(c);
	
	    in.close();
	    out.flush();
	    out.close();
	}
	
	/* in lavorazione
	public static void creaPdfFirmato(File fInput) throws FileNotFoundException, CapiException {
		StreamSignedEnvelope sse=null;
		File fdoc = new File(fInput.getPath()+".data");
		sse = new StreamSignedEnvelope(new FileInputStream(fInput), new FileOutputStream(fdoc));
		      //parses the enveloper from file "in" and save the contained data in "data"
		SignedPDF spdf = new SignedPDF(new FileInputStream(fdoc));
		String[] certs = sse.listCertificates();  //gets a list of contained certificates
		String[] signersDN = sse.listSigners();   //gets a list of signers
		//File fpdf = new File(fInput.getPath()+"-signed.pdf");
		for (int i=0;i<signersDN.length;i++) {
			spdf.addSignatureCertificate(sse.getSignerCertificate(i).getBytes());
			//spdf.addSignatureRevision((sse.listSigners())[0],new FileOutputStream(fpdf),"test");
			spdf.addSignatureRevision((sse.listSigners())[0],new FileOutputStream(fpdf),"test");
			//sse.getSignerCertificate(i);
			//sse.getSigningTime(i, null);
		}
		spdf.verify();
	}
*/
}
