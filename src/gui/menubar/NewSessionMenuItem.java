/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package gui.menubar;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import config.Table;
import gui.control.ProgramUI;
import gui.dialogs.newsession.NewSessionDialog;

public class NewSessionMenuItem extends AbstractMenuItem
{
  public NewSessionMenuItem(ProgramUI window)
  {
    super(Table.get("menubar_new"), window);
    setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
  }

  @Override
  protected void doAction(ProgramUI program)
  {
	  new NewSessionDialog(program).setVisible(true);
  }
}
