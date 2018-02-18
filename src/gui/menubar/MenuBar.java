/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package gui.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import config.Table;
import gui.control.ProgramUI;

public class MenuBar extends JMenuBar
{
  public MenuBar(ProgramUI window)
  {
    JMenu fileMenu = new JMenu(Table.get("menubar_file"));
    fileMenu.add(new NewSessionMenuItem(window));
    fileMenu.addSeparator();
    fileMenu.add(new ExitMenuItem(window));
    add(fileMenu);
  }
}
