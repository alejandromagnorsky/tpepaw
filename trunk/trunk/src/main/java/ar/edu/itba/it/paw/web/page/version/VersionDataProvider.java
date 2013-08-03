package ar.edu.itba.it.paw.web.page.version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class VersionDataProvider extends SortableDataProvider<Version> {
	
	private IModel<Project> projectModel;
	private boolean nonReleasedOnly;
	
	public VersionDataProvider(IModel<Project> projectModel) {
		setSort("name", true);
		this.projectModel = projectModel;
		this.nonReleasedOnly = false;
	}
	
	public void setNonReleasedOnly(boolean nonReleasedOnly){
		this.nonReleasedOnly = nonReleasedOnly;
	}
	
	private List<Version> getVersions(){
		if (nonReleasedOnly)
			return getNonReleasedVersions();
		return getAllVersions();
	}
	
	private List<Version> getNonReleasedVersions(){
		List<Version> versions = projectModel.getObject().getNonReleasedVersions();
		if (versions == null)
			return new ArrayList<Version>();
		List<Version> list = new ArrayList<Version>(versions);
		sort(list);
		return list;
	}
	
	private List<Version> getAllVersions(){
		List<Version> versions = projectModel.getObject().getVersions();
		if (versions == null)
			return new ArrayList<Version>();
		List<Version> list = new ArrayList<Version>(versions);
		sort(list);
		return list;
	}

	public Iterator<? extends Version> iterator(int first, int count) {
		return getVersions().subList(first, first + count).iterator();
	}

	public int size() {
		return getVersions().size();
	}

	public IModel<Version> model(Version version) {
		return new EntityModel<Version>(Version.class, version);
	}
	
	private void sort(List<Version> versions){
		final boolean isAsc = getSort().isAscending();
		if (getSort().getProperty().equals("name")){
			if (isAsc)
				Collections.sort(versions);
			else
				Collections.sort(versions, Collections.reverseOrder());
		} else if (getSort().getProperty().equals("releaseDate")){
			Collections.sort(versions, new Comparator<Version>(){
				public int compare(Version arg0, Version arg1) {
					if(isAsc)
						return arg0.getReleaseDate().compareTo(arg1.getReleaseDate());
					return arg1.getReleaseDate().compareTo(arg0.getReleaseDate());
				}					
			});
		} else if (getSort().getProperty().equals("state")){
			Collections.sort(versions, new Comparator<Version>(){
				public int compare(Version arg0, Version arg1) {
					if(isAsc)
						return arg0.getState().getCaption().compareTo(arg1.getState().getCaption());
					return arg1.getState().getCaption().compareTo(arg0.getState().getCaption());
				}					
			});
		}
	}
}
