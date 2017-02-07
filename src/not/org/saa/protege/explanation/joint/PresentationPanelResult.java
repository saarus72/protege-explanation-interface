package not.org.saa.protege.explanation.joint;

import java.awt.*;
import org.protege.editor.owl.ui.explanation.ExplanationResult;

/**
 * Author: Matthew Horridge
 * Stanford University
 * Bio-Medical Informatics Research Group
 * Date: 18/03/2012
 */

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
