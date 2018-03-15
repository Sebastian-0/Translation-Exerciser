
package gui.menubar;

import config.Table;
import gui.control.ProgramUI;

public class ExitMenuItem extends AbstractMenuItem
{
  public ExitMenuItem(ProgramUI window)
  {
    super (Table.get("menubar_quit"), window);
  }
  
  @Override
  protected void doAction(ProgramUI program)
  {
    program.shutdown();
  }
}

