package not.org.saa.protege.explanation.joint;

import java.util.ArrayList;
import java.util.Collection;

import org.protege.editor.core.Disposable;
import org.protege.editor.owl.OWLEditorKit;

import not.org.saa.protege.explanation.joint.service.LogicPlugin;
import not.org.saa.protege.explanation.joint.service.LogicPluginLoader;
import not.org.saa.protege.explanation.joint.service.LogicService;

public class LogicServiceManager implements Disposable {

	private final OWLEditorKit kit;

	private final Collection<LogicService> services_;

	public LogicServiceManager(OWLEditorKit kit) throws Exception {
		this.kit = kit;
		this.services_ = new ArrayList<LogicService>();
		LogicPluginLoader loader = new LogicPluginLoader(this.kit);
		for (LogicPlugin plugin : loader.getPlugins()) {
			LogicService service = plugin.newInstance();
			services_.add(service);
		}
	}

	@Override
	public void dispose() throws Exception {
		for (LogicService service : services_) {
			service.dispose();
		}
	}
	
	public OWLEditorKit getOWLEditorKit() {
		return kit;
	}

	public Collection<LogicService> getServices() {
		return services_;
	}
}
