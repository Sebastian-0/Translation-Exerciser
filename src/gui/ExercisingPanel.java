package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import backend.Session;
import backend.Statistics;
import config.Config;
import config.Table;
import database.io.StatisticsIO;
import gui.model.WordEngine;

public class ExercisingPanel extends JPanel {
	private static final float MINIMUM_SCALE_SIZE = 1 / 2f;
	private static final float MAXIMUM_SCALE_SIZE = 2f;

//	private boolean isMovingViewPort;
	private float viewportX;
	private float viewportY;

	private int mouseX;
	private int mouseY;

	private Font labelFont;

	private float scale;

	private WordEngine engine;
	private boolean sessionRunning; 

	public ExercisingPanel() {
		setBorder(new LineBorder(Color.BLACK));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(400, 400));

		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addMouseWheelListener(mouseListener);
		addComponentListener(componentListener);

		labelFont = getFont().deriveFont(Font.BOLD, 12f);

		scale = 1;

		engine = new WordEngine();
		
		resetViewport();
	}

	public void resetViewport() {
		viewportX = -getWidth()/2;
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
		sessionRunning = true;
		repaint();
	}

	public Statistics stop() {
		int result = JOptionPane.YES_OPTION;
		if (!engine.areAllAnswered()) {
			result = JOptionPane.showConfirmDialog(this,
					Table.get("session_end_missing_translations"),
					Table.get("popup_title_are_you_sure"),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
		}
		if (result == JOptionPane.YES_OPTION) {
			Statistics stats = engine.stop();
			sessionRunning = false;
			repaint();
			try {
				engine.saveFaults();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this,
						MessageFormat.format(Table.get("session_end_save_faults_failed"), e.getMessage()),
						Table.get("popup_title_error"),
						JOptionPane.ERROR_MESSAGE);
			}
			try {
				new StatisticsIO().write(new File(Config.get(Config.DATA_FOLDER_PATH) + File.separator + Config.STATS_FILE), stats);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this,
						MessageFormat.format(Table.get("session_end_save_stats_failed"), e.getMessage()),
						Table.get("popup_title_error"),
						JOptionPane.ERROR_MESSAGE);
			}
			return stats;
		}
		return null;
	}
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		g2d.setColor(Color.BLACK);
		g2d.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());

		g2d.scale(scale, scale);
		g2d.translate(-(int) viewportX, -(int) viewportY);
		// renderCircuit(g2d);
		// if (!isReadOnlyMode)
		// renderTool(g2d);
		// renderUserCursors(g2d);

		engine.render(g2d);

		g2d.translate((int) viewportX, (int) viewportY);
		g2d.scale(1 / scale, 1 / scale);
		
		if (!sessionRunning) {
			renderDisabledStripes(g2d);
		}

		renderZoomLabel(g2d);
	}

	private void renderDisabledStripes(Graphics2D g2d) {
		g2d.setColor(new Color(200, 200, 200, 50));
		g2d.rotate(Math.PI/4, getWidth()/2, getHeight()/2);
		
		final int stripeWidth = 50;
		
		int distance = (int) Math.sqrt(getWidth()*getWidth() + getHeight()*getHeight());
		int pos = getHeight()/2-distance/2 + stripeWidth;
		while (pos < getHeight()/2 + distance/2) {
			g2d.fillRect(getWidth()/2 - distance/2, pos, distance, stripeWidth);
			pos += stripeWidth*2;
		}
		
		g2d.rotate(-Math.PI/4, getWidth()/2, getHeight()/2);
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
			if (sessionRunning && engine.mousePressed(e, (int) viewportX, (int) viewportY)) {
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			scaleMouseEvent(e);
			if (sessionRunning && engine.mouseReleased(e, (int) viewportX, (int) viewportY)) {
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
			mouseX = e.getX();
			mouseY = e.getY();
			
			scaleMouseEvent(e);
			engine.mouseMoved(e, wasDragged, (int) viewportX, (int) viewportY);
			repaint();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.isControlDown()) {
				if (e.getWheelRotation() < 0)
					zoomIn();
				else
					zoomOut();
			} else {
				scaleMouseEvent(e);
				engine.mouseWheelMoved(e, scale, (int) viewportX, (int) viewportY);
				repaint();
			}
		}

		private void scaleMouseEvent(MouseEvent e) {
			int xd = (int) scaleValue(e.getX()) - e.getX();
			int yd = (int) scaleValue(e.getY()) - e.getY();
			e.translatePoint(xd, yd);
		}
	};
	
	
	private ComponentListener componentListener = new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
			resetViewport();
		}
	}; 
}
