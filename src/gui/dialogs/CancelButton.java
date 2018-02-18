/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

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