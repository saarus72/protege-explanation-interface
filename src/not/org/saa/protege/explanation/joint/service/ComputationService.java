package not.org.saa.protege.explanation.joint.service;

import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;

public interface ComputationService extends ProtegePluginInstance {

	public JustificationComputation creareComputation(OWLAxiom entailment);
		
	public ComputationService stp(OWLEditorKit kit);
}
