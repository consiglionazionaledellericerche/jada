package it.cnr.jada.ejb;

import it.cnr.jada.util.ejb.EJBCommonServices;

import java.rmi.RemoteException;
import java.sql.*;
import javax.ejb.*;

@Stateless(name="JADAEJB_ServerDate")
public class ServerDateBean implements ServerDate{
    private static final long serialVersionUID = 0x2c7e5503d9bf9553L;

    public Timestamp getServerDate() throws EJBException{
        try{
        	Connection conn = EJBCommonServices.getConnection();
            Statement statement = conn.createStatement();
            try{
                ResultSet resultset = statement.executeQuery("SELECT TRUNC(SYSDATE) FROM DUAL");
                try{
                    if(!resultset.next())
                        throw new EJBException("Errore interno: SELECT SYSDATE FROM DUAL ha restituito 0 records");
                    Timestamp timestamp = resultset.getTimestamp(1);
                    return timestamp;
                }finally{
					try{resultset.close();}catch( java.sql.SQLException e ){};
                }
            }finally{
				try{
					statement.close();
					conn.close();
				}catch( java.sql.SQLException e ){
				};
            }
        }catch(SQLException sqlexception){
            throw new EJBException(sqlexception);
        }
    }

    public Timestamp getServerTimestamp() throws EJBException{
        try{
        	Connection conn = EJBCommonServices.getConnection();
            Statement statement = conn.createStatement();
            try{
                ResultSet resultset = statement.executeQuery("SELECT SYSDATE FROM DUAL");
                try{
                    if(!resultset.next())
                        throw new EJBException("Errore interno: SELECT SYSDATE FROM DUAL ha restituito 0 records");
                    Timestamp timestamp = resultset.getTimestamp(1);
                    return timestamp;
                }finally{
					try{resultset.close();}catch( java.sql.SQLException e ){};
                }
            }finally{
				try{
					statement.close();
					conn.close();
				}catch( java.sql.SQLException e ){
				};
            }
        }catch(SQLException sqlexception){
            throw new EJBException(sqlexception);
        }
    }
}