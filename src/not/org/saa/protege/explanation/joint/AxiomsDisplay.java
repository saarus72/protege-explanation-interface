package not.org.saa.protege.explanation.joint;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.protege.editor.core.Disposable;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

public class AxiomsDisplay extends JPanel implements AxiomSelectionListener, Disposable {

	private Explanation<OWLAxiom> explanation;
	private AxiomsFrame frame;
	private final AxiomsFrameList frameList;
	private PresentationManager manager;
	private AxiomSelectionModel axiomSelectionModel;
	private boolean transmittingSelectionToModel = false;

	public AxiomsDisplay(PresentationManager manager, AxiomSelectionModel selectionModel, Explanation<OWLAxiom> explanation) {
		this.manager = manager;
		OWLEditorKit editorKit = manager.getOWLEditorKit();
		this.axiomSelectionModel = selectionModel;
        this.explanation = explanation;
		frame = new AxiomsFrame(editorKit);
		setLayout(new BorderLayout());
		frameList = new AxiomsFrameList(selectionModel, manager, frame);
		add(frameList, BorderLayout.NORTH);
		frame.setRootObject(explanation);
		frameList.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));

		frameList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				transmitSelectionToModel();
			}
		});

		axiomSelectionModel.addAxiomSelectionListener(new AxiomSelectionListener() {
			public void axiomAdded(AxiomSelectionModel source, OWLAxiom axiom) {
				respondToAxiomSelectionChange();
			}

			public void axiomRemoved(AxiomSelectionModel source, OWLAxiom axiom) {
				respondToAxiomSelectionChange();
			}
		});
	}

	private void respondToAxiomSelectionChange() {
		if (!transmittingSelectionToModel) {
			frameList.clearSelection();
			frameList.repaint(frameList.getVisibleRect());
		}
		frameList.repaint(frameList.getVisibleRect());
	}

	private void transmitSelectionToModel() {
		try {
			transmittingSelectionToModel = true;
			for (int i = 1; i < frameList.getModel().getSize(); i++) {
				Object element = frameList.getModel().getElementAt(i);
				if (element instanceof AxiomsFrameSectionRow) {
					AxiomsFrameSectionRow row = (AxiomsFrameSectionRow) element;
					OWLAxiom ax = row.getAxiom();
					axiomSelectionModel.setAxiomSelected(ax, frameList.isSelectedIndex(i));
				}
			}
		} finally {
			transmittingSelectionToModel = false;
		}
	}

	public Explanation<OWLAxiom> getExplanation() {
		return explanation;
	}

	public void dispose() {
		frame.dispose();
		frameList.dispose();
	}

	@Override
	public void axiomAdded(AxiomSelectionModel source, OWLAxiom axiom) {
		System.out.println("SEL: " + axiom);
	}

	@Override
	public void axiomRemoved(AxiomSelectionModel source, OWLAxiom axiom) {
	}
}
