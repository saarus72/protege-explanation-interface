package not.org.saa.protege.explanation.joint.service;

import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Computes justifications for the specified axiom
 */

public abstract class ComputationService implements ProtegePluginInstance {

	/**
     * Creates computation object designed to compute everything regarding the service.
     *
     * @param entailment	An axiom to compute justification for.
     *
     * @return	OWLEditorKit for the service.
     */
	public abstract JustificationComputation createComputation(OWLAxiom entailment);
	
	private OWLEditorKit kit;
	
	/**
	 * Should return a name for the plugin
	 * 
	 * @return	the name to be displayed in available plugins list
	 */
	public abstract String getName();
	
	@Override
	public String toString() {
		return getName();
	}

	/**
     * Returns OWLEditorKit for the service.
     *
     * @return	OWLEditorKit for the service.
     */
	public OWLEditorKit getOWLEditorKit() {
		return kit;
	}
	
	/**
     * Setup function for service.
     *
     * @param kit	OWLEditorKit to store in the service instance.
     */
	ComputationService stp(OWLEditorKit kit) {
		this.kit = kit;
		return this;
	}
}