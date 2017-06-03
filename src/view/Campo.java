package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import grafico.Bola;
import grafico.ObjetoJogo;

public class Campo extends Canvas {
	
	private static final Color COR_CAMPO = new Color(51, 204, 51);
	
	private Map<String, ObjetoJogo> objetosJogo;
	
	public Graphics2D g;
	
	public Campo() {
		setBackground(COR_CAMPO);
		objetosJogo = new HashMap<>();
		objetosJogo.put("BOLA", new Bola());
	}
	
	public void start(){
		
		this.setIgnoreRepaint(true);
		this.createBufferStrategy(2);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					gameLoop();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private void gameLoop() {
		BufferStrategy bufferStrategy = this.getBufferStrategy();
		Graphics2D g2 = (Graphics2D) bufferStrategy.getDrawGraphics();
		this.g = g2;
		
		AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
		AffineTransform move = AffineTransform.getTranslateInstance(getWidth()/2, -getHeight()/2);
		g.transform(yFlip);
		g.transform(move);
		
		desenhaCampo(g2);
		
		objetosJogo.values().forEach(o->o.atualiza(this));
		
		g.dispose();
		
		BufferStrategy strategy = bufferStrategy;
		if (!strategy.contentsLost()) {
			strategy.show();
		}
		Toolkit.getDefaultToolkit().sync();
	}

	private void desenhaCampo(Graphics2D g2) {
		g2.setColor(COR_CAMPO);
		g.fillRect(0, 0, getWidth(), getHeight());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.drawLine(0, -getHeight()/2, 0, getHeight()/2);
		g2.fillOval(getMeioX()-5, getMeioY()-5, 10, 10);
	}

	public int getMeioX() {
		return 0;
	}
	public int getMeioY() {
		return 0;
	}

	public void moverBola() {
		ObjetoJogo bola = objetosJogo.get("BOLA");
		bola.setVelocidade(2);
		bola.setDirecao(90);
	}
	
}
