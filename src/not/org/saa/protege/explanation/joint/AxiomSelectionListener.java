package not.org.saa.protege.explanation.joint;

import org.semanticweb.owlapi.model.OWLAxiom;

public interface AxiomSelectionListener {

	void axiomAdded(AxiomSelectionModel source, OWLAxiom axiom);

	void axiomRemoved(AxiomSelectionModel source, OWLAxiom axiom);
}
