
package gui.dialogs.newsession;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import config.Table;

public class StartButton extends JButton
{
  private NewSessionDialog dialog;
  
  public StartButton(NewSessionDialog dialog)
  {
    super(Table.get("new_session_start"));
    
    this.dialog = dialog;
    addActionListener(actionListener);
  }
  
  
  private ActionListener actionListener = new ActionListener() {
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
      dialog.createSession();
    }
  };
}
