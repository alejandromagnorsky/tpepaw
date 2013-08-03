package ar.edu.itba.it.paw.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.Work;
import ar.edu.itba.it.paw.service.IssueService;

@Component
public class WorkFormatter implements Formatter<Work> {
	
	private IssueService issueService;
	
	@Autowired
	public WorkFormatter(IssueService issueService){
		this.issueService = issueService;
	}

	public String print(Work work, Locale l) {
		return String.valueOf(work.getId());
	}

	public Work parse(String id, Locale l) throws ParseException {
		return issueService.getWork(Integer.valueOf(id));
	}

}
