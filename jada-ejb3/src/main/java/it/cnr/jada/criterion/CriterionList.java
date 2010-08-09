package it.cnr.jada.criterion;

import java.util.ArrayList;
import java.util.List;

import net.bzdyl.ejb3.criteria.Criterion;
import net.bzdyl.ejb3.criteria.QueryContext;

public class CriterionList implements Criterion {
	private static final long serialVersionUID = 1L;
	private List<Criterion> criterions;
	
	public CriterionList() {
		super();
		criterions = new ArrayList<Criterion>();
	}
	
	public CriterionList(Criterion... criterion) {
		this();
		add(criterion);
	}

	public String toQueryFragment(QueryContext queryContext) {
		if ( criterions.isEmpty() ){
            return "";
        }else{		
			StringBuffer buffer = new StringBuffer(" ");
			boolean firstRun = true;
			for (Criterion criterion : criterions) {
				if ( !firstRun )
                    buffer.append( " AND " );
                buffer.append( criterion.toQueryFragment( queryContext ) );
                firstRun = false;
			}
			return buffer.toString();
        }
	}

	public void add(Criterion... criterions){
		for (Criterion criterion : criterions) {
			this.criterions.add(criterion);
		}
	}
}
