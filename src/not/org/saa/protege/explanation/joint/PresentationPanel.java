package not.org.saa.protege.explanation.joint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.Scrollable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PresentationPanel extends JPanel implements Disposable, OWLModelManagerListener,
		EntailmentSelectionListener, AxiomSelectionModel, ExplanationManagerListener {

	private final OWLEditorKit kit;
	private final PresentationManager manager;
	private JComponent explanationDisplayHolder;
	private JScrollPane scrollPane;
	private Collection<AxiomsDisplay> panels;

	private JSpinner maxExplanationsSelector = new JSpinner();
	private AxiomSelectionModelImpl selectionModel;
	private static final Logger logger = LoggerFactory.getLogger(PresentationPanel.class);

	public PresentationPanel(JustificationComputationServiceManager manager, OWLAxiom entailment) {
		this(new PresentationManager(ProtegeManager.getInstance().getFrame(manager.getOWLEditorKit().getWorkspace()),
				manager, entailment));
	}

	public PresentationPanel(PresentationManager manager) {
		this.manager = manager;
		this.kit = this.manager.getOWLEditorKit();
		setLayout(new BorderLayout());

		selectionModel = new AxiomSelectionModelImpl();

		panels = new ArrayList<>();

		kit.getModelManager().addListener(this);
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

		JComponent headerPanel = createHeaderPanel();
		JPanel headerPanelHolder = new JPanel(new BorderLayout());
		headerPanelHolder.add(headerPanel, BorderLayout.WEST);
		explanationListPanel.add(headerPanelHolder, BorderLayout.NORTH);

		rhsPanel.add(explanationListPanel);
		add(rhsPanel);

		refill();
	}

	private JComponent createHeaderPanel() {

		GridBagLayout layout = new GridBagLayout();

		JComponent headerPanel = new JPanel(layout);

		final PresentationSettings presentationSettings = manager.getPresentationSettings();

		SpinnerModel spinnerModel = new SpinnerNumberModel(presentationSettings.getLimit(), 1, 900, 1);
		maxExplanationsSelector.setModel(spinnerModel);
		maxExplanationsSelector.setEnabled(!presentationSettings.isFindAllExplanations());

		final JRadioButton computeAllExplanationsRadioButton = new JRadioButton();
		computeAllExplanationsRadioButton.setAction(new AbstractAction("All explanations") {
			public void actionPerformed(ActionEvent e) {
				presentationSettings.setFindAllExplanations(computeAllExplanationsRadioButton.isSelected());
				maxExplanationsSelector.setEnabled(!presentationSettings.isFindAllExplanations());
				refill();
			}
		});
		final JRadioButton computeMaxExplanationsRadioButton = new JRadioButton();
		computeMaxExplanationsRadioButton.setAction(new AbstractAction("Limit explanations to") {
			public void actionPerformed(ActionEvent e) {
				presentationSettings.setFindAllExplanations(false);
				maxExplanationsSelector.setEnabled(!presentationSettings.isFindAllExplanations());
				refill();
			}
		});
		ButtonGroup limitButtonGroup = new ButtonGroup();
		limitButtonGroup.add(computeAllExplanationsRadioButton);
		limitButtonGroup.add(computeMaxExplanationsRadioButton);

		if (presentationSettings.isFindAllExplanations()) {
			computeAllExplanationsRadioButton.setSelected(true);
		} else {
			computeMaxExplanationsRadioButton.setSelected(true);
		}

		headerPanel.add(computeAllExplanationsRadioButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		headerPanel.add(computeMaxExplanationsRadioButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		final Timer spinnerUpdateTimer = new Timer(800, e -> {
			presentationSettings.setLimit((Integer) maxExplanationsSelector.getValue());
			refill();
		});

		spinnerUpdateTimer.setRepeats(false);

		headerPanel.add(maxExplanationsSelector, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		maxExplanationsSelector.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
		maxExplanationsSelector.addChangeListener(e -> spinnerUpdateTimer.restart());

		return headerPanel;
	}

	public Dimension getMinimumSize() {
		return new Dimension(10, 10);
	}

	public void explanationLimitChanged(PresentationManager presentationManager) {
		maxExplanationsSelector.setEnabled(!presentationManager.getPresentationSettings().isFindAllExplanations());
		selectionChanged();
	}

	public void explanationsComputed(OWLAxiom entailment) {
	}

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

	public void selectionChanged() {
		refill();
	}

	private void refill() {
		try {
			panels.forEach(AxiomsDisplay::dispose);
			explanationDisplayHolder.removeAll();
			explanationDisplayHolder.validate();

			List<Explanation<OWLAxiom>> lists = getOrderedExplanations(manager.getAxioms());
			PresentationSettings settings = manager.getPresentationSettings();

			for (int i = 0; i < lists.size() && (settings.isFindAllExplanations() || i < settings.getLimit()); i++) {
				Explanation<OWLAxiom> explanation = lists.get(i);
				final AxiomsDisplay display = new AxiomsDisplay(manager, this, explanation);
				AxiomsDisplayList displayList = new AxiomsDisplayList(display, i);
				displayList.setBorder(BorderFactory.createEmptyBorder(2, 0, 10, 0));
				explanationDisplayHolder.add(displayList);
				panels.add(display);
			}
			explanationDisplayHolder.add(Box.createVerticalStrut(10));
			scrollPane.validate();
		} catch (ExplanationException e) {
			logger.error("An error occurred whilst computing explanations: {}", e.getMessage(), e);
		}
	}

	protected List<Explanation<OWLAxiom>> getOrderedExplanations(Set<Explanation<OWLAxiom>> explanations) {
		List<Explanation<OWLAxiom>> orderedExplanations = new ArrayList<>();
		orderedExplanations.addAll(explanations);
		Collections.sort(orderedExplanations, new Comparator<Explanation<OWLAxiom>>() {
			public int compare(Explanation<OWLAxiom> o1, Explanation<OWLAxiom> o2) {
				int diff = getAxiomTypes(o1).size() - getAxiomTypes(o2).size();
				if (diff != 0) {
					return diff;
				}
				diff = getClassExpressionTypes(o1).size() - getClassExpressionTypes(o2).size();
				if (diff != 0) {
					return diff;
				}
				return o1.getSize() - o2.getSize();
			}
		});
		return orderedExplanations;
	}

	private Set<AxiomType<?>> getAxiomTypes(Explanation<OWLAxiom> explanation) {
		Set<AxiomType<?>> result = new HashSet<>();
		for (OWLAxiom ax : explanation.getAxioms()) {
			result.add(ax.getAxiomType());
		}
		return result;
	}

	private Set<ClassExpressionType> getClassExpressionTypes(Explanation<OWLAxiom> explanation) {
		Set<ClassExpressionType> result = new HashSet<>();
		for (OWLAxiom ax : explanation.getAxioms()) {
			result.addAll(ax.getNestedClassExpressions().stream().map(OWLClassExpression::getClassExpressionType)
					.collect(Collectors.toList()));
		}
		return result;
	}

	public void dispose() {
		kit.getModelManager().removeListener(this);
		for (AxiomsDisplay panel : panels) {
			panel.dispose();
		}
	}

	public void handleChange(OWLModelManagerChangeEvent event) {

	}

	public void addAxiomSelectionListener(AxiomSelectionListener lsnr) {
		selectionModel.addAxiomSelectionListener(lsnr);
	}

	public void removeAxiomSelectionListener(AxiomSelectionListener lsnr) {
		selectionModel.removeAxiomSelectionListener(lsnr);
	}

	public void setAxiomSelected(OWLAxiom axiom, boolean b) {
		selectionModel.setAxiomSelected(axiom, b);
	}

	public Set<OWLAxiom> getSelectedAxioms() {
		return selectionModel.getSelectedAxioms();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension workspaceSize = kit.getWorkspace().getSize();
		int width = (int) (workspaceSize.getWidth() * 0.8);
		int height = (int) (workspaceSize.getHeight() * 0.7);
		return new Dimension(width, height);
	}
}
