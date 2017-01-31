package not.org.saa.protege.explanation.joint.service;

import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;

public abstract class JustificationComputationService implements ProtegePluginInstance {

	public abstract JustificationComputation creareComputation(OWLAxiom entailment);
	
	public abstract boolean hasAxioms(OWLAxiom entailment);

	private OWLEditorKit kit;

	public OWLEditorKit getOWLEditorKit() {
		return kit;
	}
	
	protected JustificationComputationService setup(OWLEditorKit kit) {
		this.kit = kit;
		return this;
	}

	@Override
	public abstract void dispose();
}