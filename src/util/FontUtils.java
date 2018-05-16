/*
 * Copyright (C) 2018 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public final class FontUtils {

	private static BufferedImage fontMetricImage;
	private static Graphics2D fontMetricGraphics;
	
	static {
		fontMetricImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		fontMetricGraphics = fontMetricImage.createGraphics();
	}
	
	private FontUtils() {
	}
	
	public static Font getDefaultFont() {
		return fontMetricGraphics.getFont();
	}
	
	public static FontMetrics getFontMetrics(Font f) {
		return fontMetricGraphics.getFontMetrics(f);
	}
}
