package not.org.saa.protege.explanation.joint;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import org.protege.editor.core.log.LogBanner;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorInterruptedException;

import not.org.saa.protege.explanation.joint.service.JustificationComputationListener;
import not.org.saa.protege.explanation.joint.service.JustificationComputation;
import not.org.saa.protege.explanation.joint.service.JustificationComputationService;

public class PresentationManager {

	private static final Logger logger = LoggerFactory.getLogger(PresentationManager.class);
	public static final Marker MARKER = MarkerFactory.getMarker("Explanation");

	private final OWLAxiom entailment;
	private final JustificationComputationServiceManager manager;
	private final Collection<JustificationComputationService> services;
	private final PresentationSettings presentationSettings;
	private AxiomsCache axiomsCache;
	private ExecutorService executorService;
	private JFrame parentWindow;

	public PresentationManager(JFrame parentWindow, JustificationComputationServiceManager manager,
			OWLAxiom entailment) {
		this.entailment = entailment;
		this.manager = manager;
		this.parentWindow = parentWindow;
		services = manager.getServices();
		presentationSettings = new PresentationSettings();
		axiomsCache = new AxiomsCache();
		executorService = Executors.newSingleThreadExecutor();
	}

	public PresentationSettings getPresentationSettings() {
		return presentationSettings;
	}

	public OWLAxiom getEntailment() {
		return entailment;
	}

	public Set<Explanation<OWLAxiom>> getAxioms() {
		return getAxioms(entailment);
	}

	public Set<Explanation<OWLAxiom>> getAxioms(OWLAxiom entailment) {
		if (!axiomsCache.contains(entailment)) {
			Set<Explanation<OWLAxiom>> expls = computeAxioms(entailment);
			axiomsCache.put(expls);
		}
		return axiomsCache.get(entailment);
	}

	private Set<Explanation<OWLAxiom>> computeAxioms(OWLAxiom entailment) {
		if (services.size() == 0)
			return null;
		logger.info(LogBanner.start("Computing Explanations"));
		logger.info(MARKER, "Computing explanations for {}", entailment);
		JustificationComputationService logic = services.iterator().next();
		JustificationComputation computation = logic.creareComputation(entailment);
		ExplanationGeneratorProgressDialog progressDialog = new ExplanationGeneratorProgressDialog(parentWindow,
				computation);

		ExplanationGeneratorCallable callable = new ExplanationGeneratorCallable(entailment, computation,
				progressDialog);
		try {
			executorService.submit(callable);
		} catch (ExplanationGeneratorInterruptedException e) {
			logger.info(MARKER, "Explanation computation terminated early by user");
		}
		progressDialog.reset();
		progressDialog.setVisible(true);
		HashSet<Explanation<OWLAxiom>> explanations = new HashSet<>(callable.found);
		logger.info(MARKER, "A total of {} explanations have been computed", explanations.size());
		logger.info(LogBanner.end());
		return explanations;
	}

	public int getComputedExplanationCount(OWLAxiom entailment) {
		if (axiomsCache.contains(entailment)) {
			return axiomsCache.get(entailment).size();
		} else {
			return -1;
		}
	}

	public int getPopularity(OWLAxiom axiom) {
		int count = 0;
		Set<Explanation<OWLAxiom>> justifications = getAxioms(entailment);
		for (Explanation<OWLAxiom> justification : justifications) {
			if (justification.contains(axiom)) {
				count++;
			}
		}
		return count;
	}

	public OWLEditorKit getOWLEditorKit() {
		return manager.getOWLEditorKit();
	}

	private class ExplanationGeneratorCallable
			implements Callable<Set<Explanation<OWLAxiom>>>, JustificationComputationListener {

		private final OWLAxiom entailment;

		private Set<Explanation<OWLAxiom>> found = new HashSet<>();

		private final ExplanationGeneratorProgressDialog progressDialog;

		private final JustificationComputation computation;

		private ExplanationGeneratorCallable(OWLAxiom entailment, JustificationComputation computation,
				ExplanationGeneratorProgressDialog progressDialog) {
			this.entailment = entailment;
			this.progressDialog = progressDialog;
			this.computation = computation;
			computation.addComputationListener(this);
		}

		/**
		 * Computes a result, or throws an exception if unable to do so.
		 * 
		 * @return computed result
		 * @throws Exception
		 *             if unable to compute a result
		 */
		public Set<Explanation<OWLAxiom>> call() throws Exception {
			found = new HashSet<>();
			progressDialog.reset();
			try {
				computation.startComputation();
			} finally {
				SwingUtilities.invokeLater(() -> progressDialog.setVisible(false));
			}
			return found;
		}

		@Override
		public void foundJustification(List<OWLAxiom> justification) {
			found.add(new Explanation<OWLAxiom>(entailment, new HashSet<>(justification)));
			progressDialog.setExplanationCount(found.size());
		}
	}
}
