package not.org.saa.protege.explanation.joint;

import java.awt.*;
import org.protege.editor.owl.ui.explanation.ExplanationResult;

public class PresentationPanelResult extends ExplanationResult {

	private PresentationPanel presentationPanel;

	public PresentationPanelResult(PresentationPanel panel) {
		this.presentationPanel = panel;
		setLayout(new BorderLayout());
		add(this.presentationPanel);
	}

	@Override
	public void dispose() {
		presentationPanel.dispose();
	}

}
