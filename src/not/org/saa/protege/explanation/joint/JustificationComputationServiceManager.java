package not.org.saa.protege.explanation.joint;

import java.util.ArrayList;
import java.util.Collection;

import org.protege.editor.core.Disposable;
import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;

import not.org.saa.protege.explanation.joint.service.ComputationService;
import not.org.saa.protege.explanation.joint.service.JustificationComputationPlugin;
import not.org.saa.protege.explanation.joint.service.JustificationComputationPluginLoader;

/**
 * Keeps track of the available specified {@link ComputationService} plugins.
 * 
 * @author Pavel Klinov pavel.klinov@uni-ulm.de
 * 
 * @author Yevgeny Kazakov
 */

public class JustificationComputationServiceManager implements Disposable {

	private final OWLEditorKit kit;

	private final Collection<ComputationService> services;
	private ComputationService selectedService = null;

	public JustificationComputationServiceManager(OWLEditorKit kit, String KEY, String ID) throws Exception {
		this.kit = kit;
		this.services = new ArrayList<ComputationService>();
		JustificationComputationPluginLoader loader = new JustificationComputationPluginLoader(this.kit, KEY, ID);
		for (JustificationComputationPlugin plugin : loader.getPlugins()) {
			ComputationService service = plugin.newInstance();
			service.initialise();
			services.add(service);
		}
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

	public Collection<ComputationService> getServices() {
		return services;
	}
	
	public ComputationService getSelectedService() {
		return selectedService;
	}
	
	public void selectService(ComputationService service) {
		selectedService = service;
	}
}