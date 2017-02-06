package not.org.saa.protege.explanation.joint.service;

import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;

public abstract class InconsistentOntologyJustificationComputationService implements ComputationService {

	//public abstract JustificationComputation creareComputation(OWLAxiom entailment);

	private OWLEditorKit kit;

	public OWLEditorKit getOWLEditorKit() {
		return kit;
	}

	public InconsistentOntologyJustificationComputationService stp(OWLEditorKit kit) {
		this.kit = kit;
		return this;
	}
}
