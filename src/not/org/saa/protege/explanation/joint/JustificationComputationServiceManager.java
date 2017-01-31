package not.org.saa.protege.explanation.joint;

import java.util.ArrayList;
import java.util.Collection;

import org.protege.editor.core.Disposable;
import org.protege.editor.owl.OWLEditorKit;

import not.org.saa.protege.explanation.joint.service.LogicPlugin;
import not.org.saa.protege.explanation.joint.service.LogicPluginLoader;
import not.org.saa.protege.explanation.joint.service.JustificationComputationService;

public class JustificationComputationServiceManager implements Disposable {

	private final OWLEditorKit kit;

	private final Collection<JustificationComputationService> services;

	public JustificationComputationServiceManager(OWLEditorKit kit) throws Exception {
		this.kit = kit;
		this.services = new ArrayList<JustificationComputationService>();
		LogicPluginLoader loader = new LogicPluginLoader(this.kit);
		for (LogicPlugin plugin : loader.getPlugins())
			services.add(plugin.newInstance());
	}

	@Override
	public void dispose() throws Exception {
		for (JustificationComputationService service : services) {
			service.dispose();
		}
	}

	public OWLEditorKit getOWLEditorKit() {
		return kit;
	}

	public Collection<JustificationComputationService> getServices() {
		return services;
	}
}