package ar.edu.itba.it.paw.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectStatus {
	
	private Map<Issue.State, Node> map;

	private class Node {
		private Time time;
		private int quant;

		public Node() {
			this.quant = 0;
			this.time = new Time(0);
		}

		public Node(Time time) {
			this.quant = 0;
			this.time = time;
		}

		public void add(Node node) {
			this.quant++;
			this.time.add(node.time);
		}
	}
	
	public ProjectStatus(List<Issue> issues) {
		map = new HashMap<Issue.State, Node>();
		for (Issue.State state : Issue.State.values())
			map.put(state, new Node());
		for (Issue issue : issues)
			map.get(issue.getState()).add(new Node(issue.getEstimatedTime()));
	}

	public Time getTime(Issue.State state) {
		return map.get(state).time;
	}

	public int getQuant(Issue.State state) {
		return map.get(state).quant;
	}

}
