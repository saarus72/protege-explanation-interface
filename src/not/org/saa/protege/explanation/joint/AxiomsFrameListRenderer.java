package not.org.saa.protege.explanation.joint;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.protege.editor.core.ui.list.MList;
import org.protege.editor.core.ui.list.MListItem;
import org.protege.editor.core.ui.list.MListSectionHeader;
import org.protege.editor.core.ui.list.RendererWithInsets;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.framelist.OWLFrameListRenderer;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * Author: Matthew Horridge
 * Stanford University
 * Bio-Medical Informatics Research Group
 * Date: 19/03/2012
 */

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