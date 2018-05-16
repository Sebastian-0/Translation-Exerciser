/*
 * Copyright (C) 2018 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
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

