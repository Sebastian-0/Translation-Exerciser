package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import backend.Session;
import config.Table;
import gui.model.WordEngine;

public class ExercisingPanel extends JPanel {
	private static final float MINIMUM_SCALE_SIZE = 1 / 2f;
	private static final float MAXIMUM_SCALE_SIZE = 2f;

	private boolean isMovingViewPort;
	private float viewportX;
	private float viewportY;

	private int mouseX;
	private int mouseY;

	private Font labelFont;

	private float scale;

	private WordEngine engine;

	public ExercisingPanel() {
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(400, 400));

		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addMouseWheelListener(mouseListener);

		labelFont = getFont().deriveFont(Font.BOLD, 12f);

		scale = 1;

		engine = new WordEngine();
	}

	public void resetViewport() {
		viewportX = 0;
		viewportY = 0;
	}

	public void zoomIn() {
		if (scale < MAXIMUM_SCALE_SIZE) {
			float oldScale = scale;
			if (scale < 1)
				scale += 1 / 4f;
			else
				scale += 1 / 2f;
			updateViewportAfterZooming(oldScale, scale);
			repaint();
		}
	}

	public void zoomOut() {
		if (scale > MINIMUM_SCALE_SIZE) {
			float oldScale = scale;
			if (scale <= 1)
				scale -= 1 / 4f;
			else
				scale -= 1 / 2f;
			updateViewportAfterZooming(oldScale, scale);
			repaint();
		}
	}

	private void updateViewportAfterZooming(float oldScale, float newScale) {
		int halfWidth = getWidth() / 2;
		int halfHeight = getHeight() / 2;

		float offsetX = halfWidth / oldScale - halfWidth / newScale;
		float offsetY = halfHeight / oldScale - halfHeight / newScale;

		viewportX += offsetX;
		viewportY += offsetY;
	}

	public void start(Session session) {
		resetViewport();
		engine.start(session);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		g2d.scale(scale, scale);

		// renderGrids(g2d);

		g2d.translate(-(int) viewportX, -(int) viewportY);
		// renderCircuit(g2d);
		// if (!isReadOnlyMode)
		// renderTool(g2d);
		// renderUserCursors(g2d);

		engine.render(g2d);

		g2d.translate((int) viewportX, (int) viewportY);

		g2d.scale(1 / scale, 1 / scale);

		renderZoomLabel(g2d);
	}

	private void renderZoomLabel(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.setFont(labelFont);
		FontMetrics metrics = g2d.getFontMetrics();
		String text = Table.get("surface_zoom") + ": " + getZoomPercent() + "%";
		g2d.drawString(text,
				getWidth() - metrics.stringWidth(text),
				getHeight() - metrics.getHeight() + metrics.getAscent());
	}

	private String getZoomPercent() {
		return "" + (int) Math.round(scale * 100);
	}

	private float scaleValue(int value) {
		return value / scale;
	}

	private MouseAdapter mouseListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			scaleMouseEvent(e);

			boolean usedInput = engine.mousePressed(e, (int) viewportX, (int) viewportY);
			if (usedInput) {
				repaint();
			} else if (e.getButton() == MouseEvent.BUTTON2) {
				isMovingViewPort = true;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			scaleMouseEvent(e);

			if (isMovingViewPort) {
				isMovingViewPort = false;
				viewportX = (int) viewportX; // Remove any half-pixels from the viewport
				viewportY = (int) viewportY;
			} else if (engine.mouseReleased(e, (int) viewportX, (int) viewportY)) {
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseMoved(e, false);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			mouseMoved(e, true);
		}

		private void mouseMoved(MouseEvent e, boolean wasDragged) {
			int oldX = mouseX;
			int oldY = mouseY;

			mouseX = e.getX();
			mouseY = e.getY();

			if (isMovingViewPort) {
				viewportX += scaleValue(oldX - mouseX);
				viewportY += scaleValue(oldY - mouseY);
			} else {
				scaleMouseEvent(e);

				engine.mouseMoved(e, wasDragged, (int) viewportX, (int) viewportY);
			}
			repaint();
		}

		private void scaleMouseEvent(MouseEvent e) {
			int xd = (int) scaleValue(e.getX()) - e.getX();
			int yd = (int) scaleValue(e.getY()) - e.getY();
			e.translatePoint(xd, yd);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.isControlDown()) {
				if (e.getWheelRotation() < 0)
					zoomIn();
				else
					zoomOut();
			}
		}
	};
}
