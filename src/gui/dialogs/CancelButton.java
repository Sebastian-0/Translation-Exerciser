
package gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import config.Table;

public class CancelButton extends JButton
{
  private JDialog dialog;
  
  public CancelButton(JDialog dialog)
  {
    super(Table.get("button_cancel"));
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
