package not.org.saa.protege.explanation.joint.service;

public class ComputationServiceListenerManager {

	private ComputationServiceListener listener;
	private ComputationService service = null;
	
	public ComputationServiceListenerManager(ComputationServiceListener listener, ComputationService service) {
		this.listener = listener;
		changeService(service);
	}
	
	public void changeService(ComputationService service) {
//		if (this.service != null)
//			service.removeListener(listener);
		this.service = service;
		if (service != null)
			service.setListener(listener);
	}
}