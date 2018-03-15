
package gui.dialogs.sessionresults;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import config.Table;

public class CloseButton extends JButton
{
  private SessionResultsDialog dialog;
  
  public CloseButton(SessionResultsDialog dialog)
  {
    super(Table.get("button_close"));
    
    this.dialog = dialog;
    addActionListener(actionListener);
  }
  
  
  private ActionListener actionListener = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      dialog.dispose();
    }
  };
}
