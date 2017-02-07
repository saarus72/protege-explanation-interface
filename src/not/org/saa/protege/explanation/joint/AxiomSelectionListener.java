package not.org.saa.protege.explanation.joint;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Information Management Group
 * Date: 09-Oct-2008
 */

public interface AxiomSelectionListener {

	void axiomAdded(AxiomSelectionModel source, OWLAxiom axiom);

	void axiomRemoved(AxiomSelectionModel source, OWLAxiom axiom);
}
