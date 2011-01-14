package it.cnr.jada.blobs.servlet;

import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.blobs.bulk.*;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.*;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.ejb.EJBException;
import javax.servlet.*;
import javax.servlet.http.*;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

public class DownloadBlobServlet extends HttpServlet
{
	public DownloadBlobServlet()
	{
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		HttpActionContext context = new HttpActionContext(this, request, response);
		try
		{
			Connection conn = EJBCommonServices.getConnection(context);
			try
			{
				InputStream is;
				int bufferSize;				
				String fullpath = request.getPathInfo();
				HomeCache homeCache = new HomeCache(conn);
				String tipo = fullpath.substring(1, fullpath.indexOf('/', 1));				
				if(tipo.equals("fileExcel")){
					String user = fullpath.substring(fullpath.indexOf('/', 1) + 1, fullpath.lastIndexOf('/'));
					String filename = fullpath.substring(fullpath.lastIndexOf('/') + 1);
					BulkHome home = (BulkHome)homeCache.getHome(Excel_blobBulk.class);
					Excel_blobBulk excelBlobBulk = (Excel_blobBulk)homeCache.getHome(Excel_blobBulk.class).findByPrimaryKey(new Excel_blobBulk(user,filename));
					if(excelBlobBulk == null)
					{
						response.sendError(404, fullpath);
						return;
					}
					if(excelBlobBulk.getStato() != null && !"S".equals(excelBlobBulk.getStato()))
					{
						response.sendError(404, fullpath);
						return;
					}
					BLOB blob = (BLOB)home.getSQLBlob(excelBlobBulk, "BDATA");
					bufferSize = blob.getBufferSize();
					response.setBufferSize(bufferSize);
					String contentType = getServletContext().getMimeType(filename);
					if(contentType != null)
					  response.setContentType(contentType);
					else
					  response.setContentType("application/vnd.ms-excel");  
					response.addHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
 						
					long length = blob.length();
					if(length > 0L && length < 0x7fffffffL)
						response.setContentLength((int)length);
					is = blob.getBinaryStream();
					response.setDateHeader("Last-Modified", excelBlobBulk.getDuva().getTime());					
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
				conn.commit();
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