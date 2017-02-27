package not.org.saa.protege.explanation.joint.service;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

/**
 * A skeleton for a plugin that can provide justification based explanation for
 * OWL axioms and inconsistent ontologies
 */

public abstract class ComputationService implements ProtegePluginInstance {

	/**
     * Creates computation object designed to compute everything regarding the service.
     *
     * @param entailment	An axiom to compute justification for.
     *
     * @return	JustificationComputation for the service. Null if plugin is not able to manage the entailment.
     */
	public abstract JustificationComputation createJustificationComputation(OWLAxiom entailment);
	
	public JustificationComputation createInconsistentOntologyJustificationComputation(){
		OWLDataFactory df = getOWLEditorKit().getOWLModelManager().getOWLDataFactory();
		OWLSubClassOfAxiom entailment = df.getOWLSubClassOfAxiom(df.getOWLThing(), df.getOWLNothing());
		return createJustificationComputation(entailment);
	}
	
	/**
	 * 
	 * @param entailment
	 * @return if returns true createJustificationComputation should not return null
	 */
	public boolean canComputeJustification(OWLAxiom entailment) {
		return createJustificationComputation(entailment) == null;
	}
	
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
     * Should be called before computation object creation.
     *
     * @param kit	OWLEditorKit to store in the service instance.
     */
	ComputationService stp(OWLEditorKit kit) {
		this.kit = kit;
		return this;
	}
	
	private List<ComputationServiceListener> listeners = new ArrayList<ComputationServiceListener>();
	
	public void addListener(ComputationServiceListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ComputationServiceListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Could Return visual control pane which will be displayed while the plugin is chosen
	 * 
	 * @return the panel to be displayed. Null if panel is not provided.
	 */
	public JPanel getSettingsPanel() {
		return null;
	}
	
	/**
	 * Allows plugin to ask to re-display justification by calling this method.
	 * When called, JustificationComputation will be requested again.
	 */
	public void settingsChanged() {
		for (ComputationServiceListener listener : listeners)
			listener.redrawingCalled();
	}
}