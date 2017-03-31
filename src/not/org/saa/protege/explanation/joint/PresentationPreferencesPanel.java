package not.org.saa.protege.explanation.joint;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.protege.editor.core.ui.preferences.PreferencesLayoutPanel;
import org.protege.editor.owl.ui.preferences.OWLPreferencesPanel;

public class PresentationPreferencesPanel extends OWLPreferencesPanel {

	private SpinnerNumberModel initialAmountM, incrementM;

	static private int increment = 10;
	static private int initialAmount = 20;

	static private ArrayList<PreferencesListener> listeners = new ArrayList<PreferencesListener>();

	@Override
	public void initialise() throws Exception {
		setLayout(new BorderLayout());
		PreferencesLayoutPanel panel = new PreferencesLayoutPanel();
		add(panel, BorderLayout.NORTH);
		
		panel.addGroup("Initial justifications");
		initialAmountM = new SpinnerNumberModel(getInitialAmount(), 1, 999, 1);
		JComponent spinnerIA = new JSpinner(initialAmountM);
		spinnerIA.setMaximumSize(spinnerIA.getPreferredSize());
		panel.addGroupComponent(spinnerIA);
		spinnerIA.setToolTipText("Amount of justifications displayed at the beginning");
		
		panel.addGroup("Increment value");
		incrementM = new SpinnerNumberModel(getIncrement(), 1, 999, 1);
		JComponent spinnerI = new JSpinner(incrementM);
		spinnerI.setMaximumSize(spinnerI.getPreferredSize());
		panel.addGroupComponent(spinnerI);
		spinnerI.setToolTipText("Amount of additional justifications displayed after click on “Show next” button");
	}

	@Override
	public void dispose() throws Exception {
	}

	@Override
	public void applyChanges() {
		setInitialAmount(initialAmountM.getNumber().intValue());
		setIncrement(incrementM.getNumber().intValue());
		for (PreferencesListener listener : listeners)
			listener.valueChanged();
	}
	
	static public int getIncrement() {
		return increment;
	}
	
	static private void setIncrement(int value) {
		increment = value;
	}
	
	static public int getInitialAmount() {
		return initialAmount;
	}
	
	static public void setInitialAmount(int value) {
		initialAmount = value;
	}
	
	public static void addListener(PreferencesListener listener) {
		listeners.add(listener);
	}

	public static void removeListener(PreferencesListener listener) {
		listeners.remove(listener);
	}
	
	public interface PreferencesListener {
		public void valueChanged();
	}
}