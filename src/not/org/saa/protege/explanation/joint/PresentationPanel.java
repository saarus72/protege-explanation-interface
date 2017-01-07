package not.org.saa.protege.explanation.joint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

import org.protege.editor.core.Disposable;
import org.protege.editor.core.ProtegeManager;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationException;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

import not.org.saa.protege.explanation.joint.service.LogicService;

public class PresentationPanel extends JPanel implements Disposable/*, OWLModelManagerListener,
		EntailmentSelectionListener, AxiomSelectionModel, ExplanationManagerListener*/ {

	private final OWLEditorKit kit;
	private final PresentationManager manager;
	private JComponent explanationDisplayHolder;
	private JScrollPane scrollPane;
	private Collection<AxiomsDisplay> panels;
//	private AxiomSelectionModelImpl selectionModel;

	public PresentationPanel(LogicServiceManager manager, OWLAxiom entailment) {
		this(new PresentationManager(manager, entailment));
	}
	
	public PresentationPanel(PresentationManager manager) {


		this.manager = manager;
		this.kit = this.manager.getOWLEditorKit();
		setLayout(new BorderLayout());

//		JFrame workspaceFrame = ProtegeManager.getInstance().getFrame(ek.getWorkspace());
//		JustificationManager justificationManager = JustificationManager.getExplanationManager(workspaceFrame, ek.getOWLModelManager());
//		this.workbenchManager = new WorkbenchManager(justificationManager, entailment);

//		selectionModel = new AxiomSelectionModelImpl();

		panels = new ArrayList<>();

//		editorKit.getModelManager().addListener(this);
		explanationDisplayHolder = new Box(BoxLayout.Y_AXIS);

		JPanel pan = new HolderPanel(new BorderLayout());
		pan.add(explanationDisplayHolder, BorderLayout.NORTH);
		scrollPane = new JScrollPane(pan);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.getViewport().setBackground(null);
		scrollPane.setOpaque(false);

		JPanel rhsPanel = new JPanel(new BorderLayout(7, 7));
		JPanel explanationListPanel = new JPanel(new BorderLayout());
		explanationListPanel.add(scrollPane);
		explanationListPanel.setMinimumSize(new Dimension(10, 10));
		rhsPanel.add(explanationListPanel);
		add(rhsPanel);

		refill();
	}

	public Dimension getMinimumSize() {
		return new Dimension(10, 10);
	}

//	public void explanationLimitChanged(JustificationManager explanationManager) {
//		selectionChanged();
//	}

//	public void explanationsComputed(OWLAxiom entailment) {
//	}

	private class HolderPanel extends JPanel implements Scrollable {

		public HolderPanel(LayoutManager layout) {
			super(layout);
			setOpaque(false);
		}

		public Dimension getPreferredScrollableViewportSize() {
			return super.getPreferredSize();
		}

		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 30;
		}

		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 30;
		}

		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
	}

//	public void selectionChanged() {
//		refill();
//	}

	private void refill() {
		panels.forEach(AxiomsDisplay::dispose);
		explanationDisplayHolder.removeAll();
		explanationDisplayHolder.validate();

//		ArrayList<ArrayList<OWLAxiom>> axioms = logic.getAxioms(this.entailment);
//		for (int i = 0; i < axioms.size(); i++)
//		{
//			output += "[";
//			for (int j = 0; j < axioms.get(i).size(); j++)
//				output += axioms.get(i).get(j).toString() + ", ";
//			output += "] ";
//		}

		List<Explanation<OWLAxiom>> lists = manager.getAxioms();
		
		int count = 1;
		for (Explanation<OWLAxiom> explanation : lists) {
			final AxiomsDisplay display = new AxiomsDisplay(/*this,*/ manager, explanation);
			AxiomsDisplayList displayList = new AxiomsDisplayList(manager, display, count);
			displayList.setBorder(BorderFactory.createEmptyBorder(2, 0, 10, 0));
			explanationDisplayHolder.add(displayList);
			panels.add(display);
			count++;
		}

		explanationDisplayHolder.add(Box.createVerticalStrut(10));
		scrollPane.validate();
	}

//	protected List<Explanation<OWLAxiom>> getOrderedExplanations(Set<Explanation<OWLAxiom>> explanations) {
//		List<Explanation<OWLAxiom>> orderedExplanations = new ArrayList<>();
//		orderedExplanations.addAll(explanations);
//		Collections.sort(orderedExplanations, new Comparator<Explanation<OWLAxiom>>() {
//			public int compare(Explanation<OWLAxiom> o1, Explanation<OWLAxiom> o2) {
//				int diff = getAxiomTypes(o1).size() - getAxiomTypes(o2).size();
//				if (diff != 0) {
//					return diff;
//				}
//				diff = getClassExpressionTypes(o1).size() - getClassExpressionTypes(o2).size();
//				if (diff != 0) {
//					return diff;
//				}
//				return o1.getSize() - o2.getSize();
//			}
//		});
//		return orderedExplanations;
//	}

//	private Set<AxiomType<?>> getAxiomTypes(Explanation<OWLAxiom> explanation) {
//		Set<AxiomType<?>> result = new HashSet<>();
//		for (OWLAxiom ax : explanation.getAxioms()) {
//			result.add(ax.getAxiomType());
//		}
//		return result;
//	}

//	private Set<ClassExpressionType> getClassExpressionTypes(Explanation<OWLAxiom> explanation) {
//		Set<ClassExpressionType> result = new HashSet<>();
//		for (OWLAxiom ax : explanation.getAxioms()) {
//			result.addAll(ax.getNestedClassExpressions().stream().map(OWLClassExpression::getClassExpressionType)
//					.collect(Collectors.toList()));
//		}
//		return result;
//	}

	public void dispose() {
//		kit.getModelManager().removeListener(this);
		for (AxiomsDisplay panel : panels) {
			panel.dispose();
		}
	}

//	public void handleChange(OWLModelManagerChangeEvent event) {
//
//	}

//	public void addAxiomSelectionListener(AxiomSelectionListener lsnr) {
//		selectionModel.addAxiomSelectionListener(lsnr);
//	}

//	public void removeAxiomSelectionListener(AxiomSelectionListener lsnr) {
//		selectionModel.removeAxiomSelectionListener(lsnr);
//	}

//	public void setAxiomSelected(OWLAxiom axiom, boolean b) {
//		selectionModel.setAxiomSelected(axiom, b);
//	}

//	public Set<OWLAxiom> getSelectedAxioms() {
//		return selectionModel.getSelectedAxioms();
//	}

//	@Override
//	public Dimension getPreferredSize() {
//		Dimension workspaceSize = editorKit.getWorkspace().getSize();
//		int width = (int) (workspaceSize.getWidth() * 0.8);
//		int height = (int) (workspaceSize.getHeight() * 0.7);
//		return new Dimension(width, height);
//	}
}
