package not.org.saa.protege.explanation.joint;

import java.util.ArrayList;
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
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorInterruptedException;

import not.org.saa.protege.explanation.joint.service.AxiomsProgressMonitor;
import not.org.saa.protege.explanation.joint.service.LogicService;

public class PresentationManager {

	private static final Logger logger = LoggerFactory.getLogger(PresentationManager.class);
	public static final Marker MARKER = MarkerFactory.getMarker("Explanation");

	private final OWLAxiom entailment;
	private final LogicServiceManager manager;
	private final Collection<LogicService> services;
	private final PresentationSettings presentationSettings;
	private AxiomsCache axiomsCache;
	private ExplanationGeneratorProgressDialog progressDialog;
	private ExecutorService executorService;

	public PresentationManager(JFrame parentWindow, LogicServiceManager manager, OWLAxiom entailment) {
		this.entailment = entailment;
		this.manager = manager;
		services = manager.getServices();
		presentationSettings = new PresentationSettings();
		axiomsCache = new AxiomsCache();
		progressDialog = new ExplanationGeneratorProgressDialog(parentWindow);
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
		ExplanationGeneratorCallable callable = new ExplanationGeneratorCallable(entailment, services.iterator().next(),
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

	private static class ExplanationGeneratorCallable implements Callable<Set<Explanation<OWLAxiom>>>, AxiomsProgressMonitor<OWLAxiom> {

		private final OWLAxiom entailment;

		private Set<Explanation<OWLAxiom>> found = new HashSet<>();

		private final ExplanationGeneratorProgressDialog progressDialog;

		private final LogicService logic;

		private ExplanationGeneratorCallable(OWLAxiom entailment, LogicService logic,
				ExplanationGeneratorProgressDialog progressDialog) {
			this.entailment = entailment;
			this.progressDialog = progressDialog;
			this.logic = logic;
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
				/*found = */logic.startComputation(entailment, this);
			} finally {
				SwingUtilities.invokeLater(() -> progressDialog.setVisible(false));
			}
			return found;
		}

		public void foundExplanation(List<OWLAxiom> explanation) {
			progressDialog.setExplanationCount(found.size());
			found.add(new Explanation<OWLAxiom>(entailment, new HashSet<>(explanation)));
		}

		public boolean isCancelled() {
			return progressDialog.isCancelled();
		}
	}

}
