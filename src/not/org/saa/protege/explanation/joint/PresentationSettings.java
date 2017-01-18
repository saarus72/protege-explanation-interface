package not.org.saa.protege.explanation.joint;

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
