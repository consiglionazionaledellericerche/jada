/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Created by Generator 1.0
 * Date 23/01/2006
 */
package it.cnr.jada.excel.bulk;

import it.cnr.jada.persistency.Keyed;

import java.sql.Timestamp;

public class Excel_spoolerBase extends Excel_spoolerKey implements Keyed {
    //    SHEET_NAME VARCHAR(300)
    private java.lang.String sheet_name;

    //    STATO CHAR(1)
    private java.lang.String stato;

    //    SERVER VARCHAR(200)
    private java.lang.String server;

    //    NOME_FILE VARCHAR(300)
    private java.lang.String nome_file;

    //    QUERY VARCHAR(4000)
    private java.lang.String query;

    //    ERRORE VARCHAR(4000)
    private java.lang.String errore;
    //  FL_EMAIL CHAR(1)
    private java.lang.Boolean fl_email;

    //    EMAIL_A VARCHAR(250)
    private java.lang.String email_a;

    //    EMAIL_CC VARCHAR(250)
    private java.lang.String email_cc;

    //    EMAIL_CCN VARCHAR(250)
    private java.lang.String email_ccn;

    //    EMAIL_SUBJECT VARCHAR(250)
    private java.lang.String email_subject;

    //    EMAIL_BODY VARCHAR(4000)
    private java.lang.String email_body;
    //	  DT_PARTENZA DATE
    private Timestamp dt_partenza;
    //INTERVALLO NUMBER(10)
    private Long intervallo;
    //	  TI_INTERVALLO CHAR(1)
    private String ti_intervallo;
    //	  DT_PROSSIMA_ESECUZIONE DATE
    private Timestamp dt_prossima_esecuzione;

    private String before_statement;

    private java.lang.String ds_estrazione;

    public Excel_spoolerBase() {
        super();
    }

    public Excel_spoolerBase(java.lang.Long pg_estrazione) {
        super(pg_estrazione);
    }

    public java.lang.Boolean getFl_email() {
        return fl_email;
    }

    public void setFl_email(java.lang.Boolean fl_email) {
        this.fl_email = fl_email;
    }

    public java.lang.String getEmail_a() {
        return email_a;
    }

    public void setEmail_a(java.lang.String email_a) {
        this.email_a = email_a;
    }

    public java.lang.String getEmail_cc() {
        return email_cc;
    }

    public void setEmail_cc(java.lang.String email_cc) {
        this.email_cc = email_cc;
    }

    public java.lang.String getEmail_ccn() {
        return email_ccn;
    }

    public void setEmail_ccn(java.lang.String email_ccn) {
        this.email_ccn = email_ccn;
    }

    public java.lang.String getEmail_subject() {
        return email_subject;
    }

    public void setEmail_subject(java.lang.String email_subject) {
        this.email_subject = email_subject;
    }

    public java.lang.String getEmail_body() {
        return email_body;
    }

    public void setEmail_body(java.lang.String email_body) {
        this.email_body = email_body;
    }

    public Timestamp getDt_partenza() {
        return dt_partenza;
    }

    public void setDt_partenza(Timestamp dt_partenza) {
        this.dt_partenza = dt_partenza;
    }

    public Long getIntervallo() {
        return intervallo;
    }

    public void setIntervallo(Long intervallo) {
        this.intervallo = intervallo;
    }

    public String getTi_intervallo() {
        return ti_intervallo;
    }

    public void setTi_intervallo(String ti_intervallo) {
        this.ti_intervallo = ti_intervallo;
    }

    public Timestamp getDt_prossima_esecuzione() {
        return dt_prossima_esecuzione;
    }

    public void setDt_prossima_esecuzione(Timestamp dt_prossima_esecuzione) {
        this.dt_prossima_esecuzione = dt_prossima_esecuzione;
    }

    public java.lang.String getDs_estrazione() {
        return ds_estrazione;
    }

    public void setDs_estrazione(java.lang.String ds_estrazione) {
        this.ds_estrazione = ds_estrazione;
    }

    public java.lang.String getSheet_name() {
        return sheet_name;
    }

    public void setSheet_name(java.lang.String sheet_name) {
        this.sheet_name = sheet_name;
    }

    public java.lang.String getStato() {
        return stato;
    }

    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }

    public java.lang.String getServer() {
        return server;
    }

    public void setServer(java.lang.String server) {
        this.server = server;
    }

    public java.lang.String getNome_file() {
        return nome_file;
    }

    public void setNome_file(java.lang.String nome_file) {
        this.nome_file = nome_file;
    }

    public java.lang.String getQuery() {
        return query;
    }

    public void setQuery(java.lang.String query) {
        this.query = query;
    }

    public java.lang.String getErrore() {
        return errore;
    }

    public void setErrore(java.lang.String errore) {
        this.errore = errore;
    }

    public String getBefore_statement() {
        return before_statement;
    }

    public void setBefore_statement(String before_statement) {
        this.before_statement = before_statement;
    }
}