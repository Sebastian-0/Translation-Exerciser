/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButtonMenuItem;

import gui.control.ProgramUI;

public abstract class AbstractRadioButtonMenuItem extends JRadioButtonMenuItem
{
  private ProgramUI window;
  
  public AbstractRadioButtonMenuItem(String text, ProgramUI window)
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
