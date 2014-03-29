package utils;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class DisableablePanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = -941958557706854375L;
  
  private Map<Component, Boolean> previousStates = null; 
  
  public DisableablePanel() {
    super();
  }
  
  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    
    if(enabled) {
      if(previousStates == null) previousStates = new HashMap<Component, Boolean>();
      for(Component component: getComponents()) {
        if(previousStates.containsKey(component)) {
          component.setEnabled(previousStates.get(component));
        } else {
          component.setEnabled(true);
        }
      }
      previousStates = null;
    } else {
      previousStates = new HashMap<Component, Boolean>();
      
      for(Component component: getComponents()) {
        previousStates.put(component, component.isEnabled());
        component.setEnabled(false);
      }
    }
  }
}
