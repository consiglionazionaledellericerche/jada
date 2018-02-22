package it.cnr.jada.blobs.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.blobs.bulk.*;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.Component;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.RicercaComponent;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.TransactionalBulkLoaderIterator;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.ColumnMapping;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLQuery;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.RemoteIteratorEnumeration;
import org.apache.poi.hssf.usermodel.*;

import java.io.*;
import java.math.BigDecimal;
import java.rmi.NoSuchObjectException;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;

// Referenced classes of package it.cnr.jada.blobs.comp:
//			  IBframeBlobMgr

public class BframeBlobComponent extends RicercaComponent
        implements IBframeBlobMgr, Cloneable, Serializable, Component {
    private static final Integer NUMERO_MAX_RIGHE = new Integer(25000);

    public BframeBlobComponent() {
    }

    public void elimina(UserContext userContext, Bframe_blob_pathBulk blob_path[])
            throws ComponentException {
        try {
            Bframe_blobHome home = (Bframe_blobHome) getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blobBulk.class);
            for (int i = 0; i < blob_path.length; i++) {
                if (blob_path[i].isDirectory())
                    throw new ApplicationException("Non \350 possibile eliminare le cartelle.");
                home.delete(new Bframe_blobKey(blob_path[i].getCd_tipo(), blob_path[i].getFilename(), blob_path[i].getRelativepath()), userContext);
            }

        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public RemoteIterator getBlobChildren(UserContext userContext, Bframe_blob_pathBulk blob_path, String ti_visibilita)
            throws ComponentException {
        getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class);
        String cd_tipo = blob_path.getCd_tipo();
        String abspath = blob_path.getPath() + blob_path.getFilename();
        String relativepath = blob_path.getFilename() != null ? blob_path.getRelativepath() != null ? blob_path.getRelativepath() + blob_path.getFilename() + "/" : blob_path.getFilename() + "/" : "";
        abspath.length();
        int relativepath_length = relativepath.length();
        SQLBuilder sql_dirs = getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class, "V_BFRAME_BLOB_DIR").createSQLBuilder();
        if (relativepath_length > 0)
            sql_dirs.addClause("AND", "relativepath", 40968, relativepath + "_");
        sql_dirs.addClause("AND", "cd_tipo", 8192, cd_tipo);
        sql_dirs.setDistinctClause(true);
        SQLBuilder sql_files = getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class, "V_BFRAME_BLOB_FILE").createSQLBuilder();
        if (relativepath_length == 0)
            sql_files.addClause("AND", "relativepath", 8201, null);
        else
            sql_files.addClause("AND", "relativepath", 8192, relativepath);
        if ("U".equals(ti_visibilita)) {
            sql_files.addClause("AND", "ti_visibilita", 8192, ti_visibilita);
            sql_files.addClause("AND", "utcr", 8192, userContext.getUser());
        } else if ("P".equals(ti_visibilita)) {
            sql_files.openParenthesis("AND");
            sql_files.addClause("AND", "ti_visibilita", 8192, ti_visibilita);
            sql_files.addClause("OR", "ti_visibilita", 8201, null);
            sql_files.closeParenthesis();
        }
        sql_files.addClause("AND", "cd_tipo", 8192, cd_tipo);
        sql_files.resetColumns();
        sql_dirs.resetColumns();
        ColumnMapping mapping;
        for (Iterator i = getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class, "V_BFRAME_BLOB_FILE").getColumnMap().getColumnMappings().iterator(); i.hasNext(); sql_files.addColumn(mapping.getColumnName())) {
            mapping = (ColumnMapping) i.next();
            if (mapping.getPropertyName().equals("relativepath"))
                sql_dirs.addColumn("'" + relativepath + "'", mapping.getColumnName());
            else if (mapping.getPropertyName().equals("path"))
                sql_dirs.addColumn("'" + abspath + "'", mapping.getColumnName());
            else if (mapping.getPropertyName().equals("filename"))
                sql_dirs.addColumn("substr(relativepath," + (relativepath_length + 1) + ",instr(relativepath,'/'," + (relativepath_length + 1) + ")-" + (relativepath_length + 1) + ")", mapping.getColumnName());
            else
                sql_dirs.addColumn(mapping.getColumnName());
        }

        SQLQuery sql = sql_dirs.union(sql_files, false);
        return iterator(userContext, sql, it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class, null);
    }

    public Selezione_blob_tipoVBulk getSelezione_blob_tipo(UserContext userContext)
            throws ComponentException {
        Selezione_blob_tipoVBulk selezione = new Selezione_blob_tipoVBulk();
        try {
            selezione.setBlob_tipo((Bframe_blob_tipoBulk) getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk.class).findByPrimaryKey(new Bframe_blob_tipoKey("tipo1")));
            selezione.setBlob_tipoList(getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk.class).findAll());
            selezione.setTi_visibilita("U");
        } catch (PersistencyException e) {
            throw handleException(e);
        }
        return selezione;
    }

    public void insertBlob(UserContext context) throws ComponentException, Exception {
        Bframe_blobBulk bframe_blob = new Bframe_blobBulk("tipo", "filename", "percorso");
        BulkHome home = (BulkHome) getHome(context, Bframe_blobBulk.class);
        bframe_blob.setUser(context.getUser());
        home.insert(bframe_blob, context);
        Blob blob = getHome(context, Bframe_blobBulk.class).getSQLBlob(bframe_blob, "BDATA");
        java.io.OutputStream os = blob.setBinaryStream(1L);
        os.write("PROVA PROVA".getBytes());
        os.close();
    }
}