package not.org.saa.protege.explanation.joint;

/**
 * Date: 23/03/2017
 */

public class PresentationSettings {
	private int explanationsDisplayedCnt = 0;
	
	public int getIncrement() {
		return PresentationPreferencesPanel.getIncrement();
	}
	
	public int getInitialAmount() {
		return PresentationPreferencesPanel.getInitialAmount();
	}
	
	public void setCurrentCount(int count) {
		explanationsDisplayedCnt = count;
	}

	public int getCurrentCount() {
		return explanationsDisplayedCnt;
	}
}