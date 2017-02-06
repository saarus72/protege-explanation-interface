package not.org.saa.protege.explanation.joint;

import java.util.ArrayList;
import java.util.Collection;

import org.protege.editor.core.Disposable;
import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;

import not.org.saa.protege.explanation.joint.service.ComputationService;
import not.org.saa.protege.explanation.joint.service.JustificationComputationPlugin;
import not.org.saa.protege.explanation.joint.service.JustificationComputationPluginLoader;
import not.org.saa.protege.explanation.joint.service.JustificationComputationService;

public class JustificationComputationServiceManager<T extends ComputationService> implements Disposable {

	private final OWLEditorKit kit;

	private final Collection<T> services;

	public JustificationComputationServiceManager(OWLEditorKit kit, String KEY, String ID) throws Exception {
		this.kit = kit;
		this.services = new ArrayList<T>();
		JustificationComputationPluginLoader<T> loader = new JustificationComputationPluginLoader<T>(this.kit, KEY, ID);
		for (JustificationComputationPlugin<T> plugin : loader.getPlugins())
			services.add(plugin.newInstance());
	}

	@Override
	public void dispose() throws Exception {
		for (ComputationService service : services) {
			service.dispose();
		}
	}

	public OWLEditorKit getOWLEditorKit() {
		return kit;
	}

	public Collection<T> getServices() {
		return services;
	}
}