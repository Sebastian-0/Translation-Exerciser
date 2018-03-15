
package gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import gui.control.ProgramUI;

public abstract class AbstractMenuItem extends JMenuItem
{
  private ProgramUI window;
  
  public AbstractMenuItem(String text, ProgramUI window)
  {
    super (text);
    
    this.window = window;
    
    addActionListener(actionListener);
  }
  
  protected abstract void doAction(ProgramUI program);
  
  
  private ActionListener actionListener = new ActionListener()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      doAction(window);
    }
  };
}
