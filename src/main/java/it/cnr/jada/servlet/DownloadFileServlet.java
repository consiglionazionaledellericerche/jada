package it.cnr.jada.servlet;

import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.blobs.bulk.*;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.firma.bulk.Doc_firma_digitaleBulk;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.*;
import java.net.SocketException;
import java.sql.Connection;
import javax.servlet.*;
import javax.servlet.http.*;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

public class DownloadFileServlet extends HttpServlet
{
	public DownloadFileServlet()
	{
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		HttpActionContext context = new HttpActionContext(this, request, response);
		String user=null;
		if (context.getUserContext()!=null)
			user = context.getUserContext().getUser();
		if(user == null) {
			response.sendError(401, "Utente non autorizzato ad accedere al file");
			return;
		}
		try
		{
			Connection conn = EJBCommonServices.getConnection();
			conn.setAutoCommit(false);
			try
			{
				InputStream is;
				int bufferSize;				
				String fullpath = request.getPathInfo();
				HomeCache homeCache = new HomeCache(conn);
				
				String tipo = fullpath.substring(1, fullpath.indexOf('/', 1));				
				if(tipo.equals("download_doc_firma_digitale")){
				    int ind_init = fullpath.indexOf('/') + 1;
				    int ind_end  = fullpath.indexOf('/', ind_init);
				    ind_init = ind_end + 1;
				    ind_end  = fullpath.indexOf('/', ind_init);
					String filename = fullpath.substring(fullpath.lastIndexOf('/') + 1);
				    String progressivo = fullpath.substring(ind_init,ind_end);	
					   
				    BulkHome home = (BulkHome)homeCache.getHome(Doc_firma_digitaleBulk.class);
				    Doc_firma_digitaleBulk docBulk = (Doc_firma_digitaleBulk)homeCache.getHome(Doc_firma_digitaleBulk.class).findByPrimaryKey(new Doc_firma_digitaleBulk(new Long(progressivo)));
					if(docBulk == null) {
						response.sendError(404, fullpath);
						return;
					}
					if(!docBulk.getCd_utente().equals(user)) {
						response.sendError(401, "Utente non autorizzato ad accedere al file");
						return;
					}
					BLOB blob = (BLOB)home.getSQLBlob(docBulk, "BDATA");
					bufferSize = blob.getBufferSize();
					response.setBufferSize(bufferSize);
					String contentType = getServletContext().getMimeType(filename);
					if(contentType != null)
					  response.setContentType(contentType);
					else
					  response.setContentType("application/x-pkcs7");  
					response.addHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
	 						
					long length = blob.length();
					if(length > 0L && length < 0x7fffffffL)
						response.setContentLength((int)length);
					is = blob.getBinaryStream();
					response.setDateHeader("Last-Modified", docBulk.getDuva().getTime());
				}else{
					String filename = fullpath.substring(fullpath.lastIndexOf('/') + 1);
					String path = fullpath.substring(fullpath.indexOf('/', 1) + 1, fullpath.lastIndexOf('/') + 1);
					BulkHome home = (BulkHome)homeCache.getHome(it.cnr.jada.blobs.bulk.Bframe_blobBulk.class);
					Bframe_blob_tipoBulk blob_tipo = (Bframe_blob_tipoBulk)homeCache.getHome(it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk.class).findByPrimaryKey(new Bframe_blob_tipoKey(tipo));
					if(blob_tipo == null)
					{
						response.sendError(404, fullpath);
						return;
					}
					Bframe_blobBulk blob_bulk = (Bframe_blobBulk)home.findByPrimaryKey(new Bframe_blobKey(tipo, filename, path));
					if(blob_bulk == null)
					{
						response.sendError(404, fullpath);
						return;
					}
					if(blob_bulk.getStato() != null && !"S".equals(blob_bulk.getStato()))
					{
						response.sendError(404, fullpath);
						return;
					}
					if(blob_tipo.getFl_binario().booleanValue())
					{
						BLOB blob = (BLOB)home.getSQLBlob(blob_bulk, "BDATA");
						bufferSize = blob.getBufferSize();
						response.setBufferSize(bufferSize);
						String contentType = getServletContext().getMimeType(filename);
						if(contentType != null)
							response.setContentType(contentType);
						else
							response.setContentType("www/unknown");													
						long length = blob.length();
						if(length > 0L && length < 0x7fffffffL)
							response.setContentLength((int)length);
						is = blob.getBinaryStream();
					} else
					{
						CLOB clob = (CLOB)home.getSQLClob(blob_bulk, "CDATA");
						bufferSize = clob.getBufferSize();
						response.setBufferSize(bufferSize);
						String contentType = getServletContext().getMimeType(filename);
						if(contentType != null)
							response.setContentType(contentType);
						else
						    response.setContentType("www/unknown");	
						long length = clob.length();
						//if(length > 0L && length < 0x7fffffffL)
							response.setContentLength((int)length);
						is = clob.getAsciiStream();
					}
				    response.setDateHeader("Last-Modified", blob_bulk.getDuva().getTime());
				}									    
				OutputStream os = response.getOutputStream();
				try
				{
					byte buffer[] = new byte[bufferSize];
					int size;
					while((size = is.read(buffer)) > 0) 
						os.write(buffer, 0, size);
				}
				catch(SocketException ex){}				
				finally
				{
					is.close();
				}
			}
			finally
			{
				//conn.commit();
				conn.close();
				conn = null;
			}
		}
		catch(Throwable _ex)
		{
			response.sendError(500);
		}
	}
}
