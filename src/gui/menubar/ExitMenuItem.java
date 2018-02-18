/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

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

