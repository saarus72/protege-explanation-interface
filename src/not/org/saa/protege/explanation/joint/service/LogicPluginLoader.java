package not.org.saa.protege.explanation.joint.service;

import org.eclipse.core.runtime.IExtension;
import org.protege.editor.core.plugin.AbstractPluginLoader;
import org.protege.editor.owl.OWLEditorKit;

public class LogicPluginLoader extends AbstractPluginLoader<LogicPlugin> {
	private final OWLEditorKit kit;

	public LogicPluginLoader(OWLEditorKit kit) {
		super(LogicPlugin.KEY, LogicPlugin.ID);
		this.kit = kit;
	}

	@Override
	protected LogicPlugin createInstance(IExtension extension) {
		return new LogicPlugin(kit, extension);
	}
}
