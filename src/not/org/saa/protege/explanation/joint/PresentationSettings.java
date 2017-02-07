package not.org.saa.protege.explanation.joint;

/**
 * Author: Matthew Horridge
 * Stanford University
 * Bio-Medical Informatics Research Group
 * Date: 20/03/2012
 */

public class PresentationSettings {

	private int limit = 2;
	private boolean findAll = true;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isFindAllExplanations() {
		return findAll;
	}

	public void setFindAllExplanations(boolean findAllExplanations) {
		findAll = findAllExplanations;
	}
}
