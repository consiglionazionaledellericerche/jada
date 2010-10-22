package it.cnr.jada.criterion;


import net.bzdyl.ejb3.criteria.Criterion;
import net.bzdyl.ejb3.criteria.QueryContext;


public class ExistsSubqueryExpression implements Criterion {
	private static final long serialVersionUID = 1L;
	private final Boolean exists;
	private final String subQuery;
	
	public ExistsSubqueryExpression(Boolean exists, String subQuery) {
		super();
		this.subQuery = subQuery;
		this.exists = exists;
	}

	public String toQueryFragment(QueryContext queryContext) {
		StringBuilder buffer = new StringBuilder();
		if (!exists)
			buffer.append(" NOT ");
        buffer.append(" EXISTS (");
        buffer.append(subQuery);
        buffer.append( ")" );        
        return buffer.toString();
	}

}
