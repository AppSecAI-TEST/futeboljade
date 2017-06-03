package view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import grafico.Bola;
import grafico.ObjetoJogo;

public class Campo extends JPanel {
	
	private static final Color COR_CAMPO = new Color(51, 204, 51);
	
	private Map<String, ObjetoJogo> objetosJogo;
	
	public Graphics g;
	
	public Campo() {
		setBackground(COR_CAMPO);
		objetosJogo = new HashMap<>();
		objetosJogo.put("BOLA", new Bola());
	}
	
	public void gameLoop(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					repaint();
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.g = g;
		g.setColor(Color.WHITE);
		g.drawLine(getMeioX(), getHeight(), getMeioX(), 0);
		g.fillOval(getMeioX()-5, getMeioY()-5, 10, 10);
		objetosJogo.values().forEach(o->o.atualiza(this));
	}

	public int getMeioX() {
		return getWidth()/2;
	}
	public int getMeioY() {
		return getHeight()/2;
	}

	public void moverBola() {
		ObjetoJogo bola = objetosJogo.get("BOLA");
		bola.setVelocidade(7);
		bola.setDirecao(90);
	}
	
}
