package it.cnr.jada.util.upload;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class MultipartResponse
{

    public MultipartResponse(HttpServletResponse response)
        throws IOException
    {
        endedLastResponse = true;
        res = response;
        out = res.getOutputStream();
        res.setContentType("multipart/x-mixed-replace;boundary=End");
        out.println();
        out.println("--End");
    }

    public void endResponse()
        throws IOException
    {
        out.println();
        out.println("--End");
        out.flush();
        endedLastResponse = true;
    }

    public void finish()
        throws IOException
    {
        out.println("--End--");
        out.flush();
    }

    public void startResponse(String contentType)
        throws IOException
    {
        if(!endedLastResponse)
            endResponse();
        out.println("Content-type: " + contentType);
        out.println();
        endedLastResponse = false;
    }

    HttpServletResponse res;
    ServletOutputStream out;
    boolean endedLastResponse;
}