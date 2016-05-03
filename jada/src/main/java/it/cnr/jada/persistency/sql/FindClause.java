package it.cnr.jada.persistency.sql;


public interface FindClause
{

    public abstract String getLogicalOperator();

    public abstract void setLogicalOperator(String s);

    public static final String AND = "AND";
    public static final String AND_NOT = "AND NOT";

    public static final String OR = "OR";
    public static final String OR_NOT = "OR NOT";
}