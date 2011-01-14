package it.cnr.jada.util.upload;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class MailMessage
{

    public MailMessage()
        throws IOException
    {
        this("localhost");
    }

    public MailMessage(String host)
        throws IOException
    {
        this.host = host;
        to = new Vector();
        cc = new Vector();
        headers = new Hashtable();
        setHeader("X-Mailer", "it.cnr.mail");
        connect();
        sendHelo();
    }

    public void bcc(String bcc)
        throws IOException
    {
        sendRcpt(bcc);
    }

    public void cc(String cc)
        throws IOException
    {
        sendRcpt(cc);
        this.cc.addElement(cc);
    }

    void connect()
        throws IOException
    {
        socket = new Socket(host, 25);
        out = new MailPrintStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        getReady();
    }

    void disconnect()
        throws IOException
    {
        if(out != null)
            out.close();
        if(in != null)
            in.close();
        if(socket != null)
            socket.close();
    }

    void flushHeaders()
        throws IOException
    {
        String name;
        String value;
        for(Enumeration e = headers.keys(); e.hasMoreElements(); out.println(name + ": " + value))
        {
            name = (String)e.nextElement();
            value = (String)headers.get(name);
        }

        out.println();
        out.flush();
    }

    public void from(String from)
        throws IOException
    {
        sendFrom(from);
        this.from = from;
    }

    public PrintStream getPrintStream()
        throws IOException
    {
        setFromHeader();
        setToHeader();
        setCcHeader();
        sendData();
        flushHeaders();
        return out;
    }

    void getReady()
        throws IOException
    {
        String response = in.readLine();
        int ok[] = {
            220
        };
        if(!isResponseOK(response, ok))
            throw new IOException("Didn't get introduction from server: " + response);
        else
            return;
    }

    boolean isResponseOK(String response, int ok[])
    {
        for(int i = 0; i < ok.length; i++)
            if(response.startsWith(String.valueOf(ok[i])))
                return true;

        return false;
    }

    static String sanitizeAddress(String s)
    {
        int paramDepth = 0;
        int start = 0;
        int end = 0;
        int len = s.length();
        for(int i = 0; i < len; i++)
        {
            char c = s.charAt(i);
            if(c == '(')
            {
                paramDepth++;
                if(start == 0)
                    end = i;
            } else
            if(c == ')')
            {
                paramDepth--;
                if(end == 0)
                    start = i + 1;
            } else
            if(paramDepth == 0 && c == '<')
                start = i + 1;
            else
            if(paramDepth == 0 && c == '>')
                end = i;
        }

        if(end == 0)
            end = len;
        return s.substring(start, end);
    }

    void send(String msg, int ok[])
        throws IOException
    {
        out.rawPrint(msg + "\r\n");
        String response = in.readLine();
        if(!isResponseOK(response, ok))
            throw new IOException("Unexpected reply to command: " + msg + ": " + response);
        else
            return;
    }

    public void sendAndClose()
        throws IOException
    {
        sendDot();
        disconnect();
    }

    void sendData()
        throws IOException
    {
        int ok[] = {
            354
        };
        send("DATA", ok);
    }

    void sendDot()
        throws IOException
    {
        int ok[] = {
            250
        };
        send("\r\n.", ok);
    }

    void sendFrom(String from)
        throws IOException
    {
        int ok[] = {
            250
        };
        send("MAIL FROM: <" + sanitizeAddress(from) + ">", ok);
    }

    void sendHelo()
        throws IOException
    {
        String local = InetAddress.getLocalHost().getHostName();
        int ok[] = {
            250
        };
        send("HELO " + local, ok);
    }

    void sendQuit()
        throws IOException
    {
        int ok[] = {
            221
        };
        send("QUIT", ok);
    }

    void sendRcpt(String rcpt)
        throws IOException
    {
        int ok[] = {
            250, 251
        };
        send("RCPT TO: <" + sanitizeAddress(rcpt) + ">", ok);
    }

    void setCcHeader()
    {
        setHeader("Cc", vectorToList(cc));
    }

    void setFromHeader()
    {
        setHeader("From", from);
    }

    public void setHeader(String name, String value)
    {
        headers.put(name, value);
    }

    public void setSubject(String subj)
    {
        headers.put("Subject", subj);
    }

    void setToHeader()
    {
        setHeader("To", vectorToList(to));
    }

    public void to(String to)
        throws IOException
    {
        sendRcpt(to);
        this.to.addElement(to);
    }

    String vectorToList(Vector v)
    {
        StringBuffer buf = new StringBuffer();
        for(Enumeration e = v.elements(); e.hasMoreElements();)
        {
            buf.append(e.nextElement());
            if(e.hasMoreElements())
                buf.append(", ");
        }

        return buf.toString();
    }

    String host;
    String from;
    Vector to;
    Vector cc;
    Hashtable headers;
    MailPrintStream out;
    BufferedReader in;
    Socket socket;
}