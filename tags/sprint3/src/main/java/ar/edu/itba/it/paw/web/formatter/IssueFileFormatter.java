package ar.edu.itba.it.paw.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.IssueFile;
import ar.edu.itba.it.paw.repo.IssueRepo;

@Component
public class IssueFileFormatter implements Formatter<IssueFile> {

	private IssueRepo issueRepo;

	@Autowired
	public IssueFileFormatter(IssueRepo issueRepo) {
		this.issueRepo = issueRepo;
	}

	public String print(IssueFile issueFile, Locale l) {
		return issueFile.getId() + "";
	}

	public IssueFile parse(String id, Locale l) throws ParseException {
		return issueRepo.getIssueFile(Integer.valueOf(id));
	}

}
