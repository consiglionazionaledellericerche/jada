package it.cnr.jada.criterion;

import net.bzdyl.ejb3.criteria.Criterion;

public class Subqueries {
	private static final long serialVersionUID = 1L;

	public static Criterion exists(String subquery) {
		return new ExistsSubqueryExpression(Boolean.TRUE, subquery);
	}

	public static Criterion notExists(String subquery) {
		return new ExistsSubqueryExpression(Boolean.FALSE, subquery);
	}

}
