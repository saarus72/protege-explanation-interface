package not.org.saa.protege.explanation.joint;

import java.util.ArrayList;
import java.util.Collection;

import org.protege.editor.core.Disposable;
import org.protege.editor.owl.OWLEditorKit;

import not.org.saa.protege.explanation.joint.service.InconsistentOntologyJustificationComputationPlugin;
import not.org.saa.protege.explanation.joint.service.InconsistentOntologyJustificationComputationPluginLoader;
import not.org.saa.protege.explanation.joint.service.InconsistentOntologyJustificationComputationService;

public class InconsistentOntologyJustificationComputationServiceManager implements Disposable {

	private final OWLEditorKit kit;

	private final Collection<InconsistentOntologyJustificationComputationService> services;

	public InconsistentOntologyJustificationComputationServiceManager(OWLEditorKit kit) throws Exception {
		this.kit = kit;
		this.services = new ArrayList<InconsistentOntologyJustificationComputationService>();
		InconsistentOntologyJustificationComputationPluginLoader loader = new InconsistentOntologyJustificationComputationPluginLoader(this.kit);
		for (InconsistentOntologyJustificationComputationPlugin plugin : loader.getPlugins())
			services.add(plugin.newInstance());
	}

	@Override
	public void dispose() throws Exception {
		for (InconsistentOntologyJustificationComputationService service : services) {
			service.dispose();
		}
	}

	public OWLEditorKit getOWLEditorKit() {
		return kit;
	}

	public Collection<InconsistentOntologyJustificationComputationService> getServices() {
		return services;
	}
}