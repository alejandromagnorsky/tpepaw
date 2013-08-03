package ar.edu.itba.it.paw.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.Work;
import ar.edu.itba.it.paw.repo.IssueRepo;

@Component
public class WorkFormatter implements Formatter<Work> {
	
	private IssueRepo issueRepo;
	
	@Autowired
	public WorkFormatter(IssueRepo issueRepo){
		this.issueRepo = issueRepo;
	}

	public String print(Work work, Locale l) {
		return String.valueOf(work.getId());
	}

	public Work parse(String id, Locale l) throws ParseException {
		return issueRepo.getWork(Integer.valueOf(id));
	}

}
