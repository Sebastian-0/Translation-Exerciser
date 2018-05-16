/*
 * Copyright (C) 2018 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package gui.dialogs.newsession;

import javax.swing.JCheckBox;

import config.Table;

public class IncludeAllCheckbox extends JCheckBox {
	public IncludeAllCheckbox() {
		super(Table.get("new_session_include_all"));
	}
}
