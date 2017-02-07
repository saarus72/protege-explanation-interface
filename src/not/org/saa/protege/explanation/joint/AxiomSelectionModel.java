package not.org.saa.protege.explanation.joint;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Information Management Group
 * Date: 09-Oct-2008
 */

public interface AxiomSelectionModel {

	void addAxiomSelectionListener(AxiomSelectionListener lsnr);

	void removeAxiomSelectionListener(AxiomSelectionListener lsnr);

	void setAxiomSelected(OWLAxiom axiom, boolean b);

	Set<OWLAxiom> getSelectedAxioms();
}
