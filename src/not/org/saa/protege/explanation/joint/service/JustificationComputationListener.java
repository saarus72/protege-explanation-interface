package not.org.saa.protege.explanation.joint.service;

import java.util.List;

import org.semanticweb.owlapi.model.OWLAxiom;

public interface JustificationComputationListener {
	/**
     * Common words.
     *
     * @param explanation          More specific words.
     */
	void foundJustification(List<OWLAxiom> justification);
}
