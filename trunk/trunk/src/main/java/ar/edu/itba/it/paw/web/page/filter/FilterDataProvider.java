package ar.edu.itba.it.paw.web.page.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class FilterDataProvider extends SortableDataProvider<Filter> {
	
	private IModel<Project> projectModel;
	
	public FilterDataProvider(IModel<Project> projectModel) {
		setSort("name", true);
		this.projectModel = projectModel;
	}
	
	private List<Filter> getFilters(){
		SortedSet<Filter> filters = projectModel.getObject().getFilters();
		if (filters == null)
			return new ArrayList<Filter>();
		List<Filter> list = new ArrayList<Filter>(filters);
		sort(list);
		return list;
	}

	public Iterator<? extends Filter> iterator(int first, int count) {
		return getFilters().subList(first, first + count).iterator();
	}

	public int size() {
		return getFilters().size();
	}

	public IModel<Filter> model(Filter filter) {
		return new EntityModel<Filter>(Filter.class, filter);
	}
	
	private void sort(List<Filter> filters){
		if (getSort().isAscending())
			Collections.sort(filters);
		else
			Collections.sort(filters, Collections.reverseOrder());
	}
}
