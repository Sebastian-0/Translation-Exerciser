/*
 * Copyright (C) 2018 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */


package gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;

import gui.control.ProgramUI;

public abstract class AbstractCheckBoxMenuItem extends JCheckBoxMenuItem
{
  private ProgramUI window;
  
  public AbstractCheckBoxMenuItem(String text, ProgramUI window)
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
