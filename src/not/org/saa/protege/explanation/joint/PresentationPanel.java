package not.org.saa.protege.explanation.joint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.Scrollable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

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

import not.org.saa.protege.explanation.joint.service.ComputationService;
import not.org.saa.protege.explanation.joint.service.ComputationServiceListener;

/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Information Management Group
 * Date: 04-Oct-2008
 * 
 * The component that displays a set of justification
 */

public class PresentationPanel extends JPanel implements Disposable, OWLModelManagerListener,
		EntailmentSelectionListener, AxiomSelectionModel, ExplanationManagerListener, ComputationServiceListener {

	private final OWLEditorKit kit;
	private final PresentationManager manager;
	private JComponent explanationDisplayHolder;
	private JComponent serviceSettingsDisplayHolder;
	private JScrollPane scrollPane;
	private Collection<AxiomsDisplay> panels;
	
	private JPanel explanationListPanel;

	private JSpinner maxExplanationsSelector = new JSpinner();
	private AxiomSelectionModelImpl selectionModel;
	private static final Logger logger = LoggerFactory.getLogger(PresentationPanel.class);

	public PresentationPanel(JustificationComputationServiceManager manager, OWLAxiom entailment) {
		this(new PresentationManager(ProtegeManager.getInstance().getFrame(manager.getOWLEditorKit().getWorkspace()),
				manager, entailment));
	}

	public PresentationPanel(PresentationManager manager) {
		this.manager = manager;
		manager.setComputationServiceListener(this);
		this.kit = this.manager.getOWLEditorKit();
		setLayout(new BorderLayout());
		
		Collection<ComputationService> services = manager.getServices();
		switch (services.size()) {
		case 0:
			break;
		case 1:
			manager.selectService(services.iterator().next());
			JLabel label = new JLabel("Using " + manager.getSelectedService() + " as a computation service");
			add(label, BorderLayout.NORTH);
			break;
		default:
			JComboBox<ComputationService> selector = new JComboBox<ComputationService>();
			ComputationService serviceToSelect = services.iterator().next();
			for (ComputationService service : services)
				if (service.canComputeJustification(manager.getEntailment()))
				{
					selector.addItem(service);
					if (JustificationComputationServiceManager.lastChoosenServiceId == manager.getIdForService(service))
						serviceToSelect = service;
				}
			selector.setSelectedItem(serviceToSelect);
			manager.selectService(serviceToSelect);
			selector.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					manager.selectService((ComputationService) selector.getSelectedItem());
					updateSettingsPanel();
					
					manager.clearJustificationsCache();
					refill();
				}
			});
			add(selector, BorderLayout.NORTH);
		}

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
		explanationListPanel = new JPanel(new BorderLayout());
		explanationListPanel.add(scrollPane);
		explanationListPanel.setMinimumSize(new Dimension(10, 10));
		JComponent headerPanel = createHeaderPanel();
		JPanel headerPanelHolder = new JPanel(new BorderLayout());
		headerPanelHolder.add(headerPanel, BorderLayout.WEST);
		JPanel sSDHPanel = new JPanel(new BorderLayout());
		JPanel p = new JPanel(new BorderLayout());
		JSeparator sep = new JSeparator();
		sep.setBorder(new EmptyBorder(2, 2, 2, 2));
		p.add(sep, BorderLayout.NORTH);
		p.add(headerPanelHolder);
		sSDHPanel.add(p);
		serviceSettingsDisplayHolder = new JPanel(new BorderLayout());
		sSDHPanel.add(serviceSettingsDisplayHolder, BorderLayout.NORTH);
		explanationListPanel.add(sSDHPanel, BorderLayout.NORTH);
		rhsPanel.add(explanationListPanel);
		add(rhsPanel);

		updateSettingsPanel();
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

		final Timer spinnerUpdateTimer = new Timer(800, e -> {
			presentationSettings.setLimit((Integer) maxExplanationsSelector.getValue());
			refill();
		});
		spinnerUpdateTimer.setRepeats(false);
		maxExplanationsSelector.addChangeListener(e -> spinnerUpdateTimer.restart());

//		JSeparator sep = new JSeparator();
//		
//		headerPanel.add(sep, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
//				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		headerPanel.add(computeAllExplanationsRadioButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		headerPanel.add(computeMaxExplanationsRadioButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(2, 10, 2, 2), 0, 0));
		headerPanel.add(maxExplanationsSelector, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		maxExplanationsSelector.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

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
	

	@Override
	public void redrawingCalled() {
		manager.clearJustificationsCache();
		refill();
	}
	
	private void updateSettingsPanel() {
		JPanel settingsPanel = manager.getSelectedService().getSettingsPanel();
		serviceSettingsDisplayHolder.removeAll();
		if (settingsPanel != null)
			serviceSettingsDisplayHolder.add(settingsPanel, BorderLayout.WEST);
		explanationListPanel.validate();
	}

	private void refill() {
		try {
			panels.forEach(AxiomsDisplay::dispose);
			explanationDisplayHolder.removeAll();
			explanationDisplayHolder.validate();

			List<Explanation<OWLAxiom>> lists = getOrderedExplanations(manager.getJustifications());
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
		selectionModel.dispose();
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