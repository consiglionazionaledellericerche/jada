package it.cnr.jada.criterion;


import net.bzdyl.ejb3.criteria.Criterion;
import net.bzdyl.ejb3.criteria.QueryContext;


public class ExistsSubqueryExpression implements Criterion {
	private static final long serialVersionUID = 1L;
	private final String subQuery;
	
	public ExistsSubqueryExpression(String subQuery) {
		super();
		this.subQuery = subQuery;
	}

	public String toQueryFragment(QueryContext queryContext) {
        StringBuilder buffer = new StringBuilder(" EXISTS (");
        buffer.append(subQuery);
        buffer.append( ")" );        
        return buffer.toString();
	}

}
