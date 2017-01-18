package not.org.saa.protege.explanation.joint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owl.explanation.api.Explanation;

import not.org.saa.protege.explanation.joint.service.LogicService;

public class PresentationManager {
	private final OWLAxiom entailment;
	private final LogicServiceManager manager;
	private final Collection<LogicService> services;
	private final PresentationSettings presentationSettings;

	public PresentationManager(LogicServiceManager manager, OWLAxiom entailment) {
		this.entailment = entailment;
		this.manager = manager;
		services = manager.getServices();
		presentationSettings = new PresentationSettings();
	}
	
    public PresentationSettings getPresentationSettings() {
        return presentationSettings;
    }

	public OWLAxiom getEntailment() {
		return entailment;
	}

	public List<Explanation<OWLAxiom>> getAxioms() {
		return getAxioms(entailment);
	}

	public List<Explanation<OWLAxiom>> getAxioms(OWLAxiom entailment) {
		if (services.size() == 0)
			return null;
		LogicService logic = services.iterator().next();
		List<? extends List<OWLAxiom>> lists = logic.getAxioms(entailment);
		List<Explanation<OWLAxiom>> explanations = new ArrayList<Explanation<OWLAxiom>>();
		for (List<OWLAxiom> axioms : lists) {
			Set<OWLAxiom> s = new HashSet<OWLAxiom>(axioms);
			explanations.add(new Explanation<OWLAxiom>(entailment, s));
		}
		return explanations;
	}

	public OWLEditorKit getOWLEditorKit() {
		return manager.getOWLEditorKit();
	}
}
