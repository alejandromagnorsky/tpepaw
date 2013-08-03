package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.sort.AjaxFallbackOrderByLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.data.DataView;

@SuppressWarnings("serial")
public class AjaxUtils {
	
	public static <T>  AjaxPagingNavigator getPager(String id, final DataView<T> dataView,
					final WebMarkupContainer container){
		return new AjaxPagingNavigator(id, dataView) {
        	@Override
        	protected void onAjaxEvent(AjaxRequestTarget target) {
        		target.addComponent(container);
        	}
        };
	}
	
	public static <T> AjaxFallbackOrderByLink getOrderLink(String id,
					String property, final DataView<T> dataView,
					final WebMarkupContainer container, SortableDataProvider<T> provider){
		return new AjaxFallbackOrderByLink(id, property, provider) {
			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
					
			@Override
			protected void onAjaxClick(AjaxRequestTarget target) {
				target.addComponent(container);
			}
		};
	}
}
