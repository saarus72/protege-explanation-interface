package not.org.saa.protege.explanation.joint;

import javax.swing.JList;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.framelist.OWLFrameListRenderer;
import org.semanticweb.owlapi.model.OWLObject;

public class AxiomsFrameListRenderer extends OWLFrameListRenderer {
	public AxiomsFrameListRenderer(OWLEditorKit owlEditorKit) {
		super(owlEditorKit);
		setHighlightUnsatisfiableClasses(false);
		setHighlightUnsatisfiableProperties(false);
	}

	@Override
	protected OWLObject getIconObject(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return null;
	}
}
