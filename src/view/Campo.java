package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.Map;

import grafico.Bola;
import grafico.Gol;
import grafico.ObjetoJogo;
import grafico.StatusJogo;

public class Campo extends Canvas {
	
	private static final Color COR_CAMPO = new Color(51, 204, 51);
	
	private Map<String, ObjetoJogo> objetosJogo;
	
	public Graphics2D g;

	private Gol golEsquerda;
	private Gol golDireita;
	private StatusJogo status;
	
	public Campo() {
		setBackground(COR_CAMPO);
		objetosJogo = new HashMap<>();
		Bola bola = new Bola();
		bola.setCampo(this);
		golEsquerda = new Gol();
		golDireita = new Gol();
		objetosJogo.put("BOLA", bola);
	}
	
	public void start(){
		
		this.setIgnoreRepaint(true);
		this.createBufferStrategy(2);
		status = StatusJogo.JOGANDO;
		
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
		AffineTransform move = AffineTransform.getTranslateInstance(getWidth()/2, getYBordaCima());
		g.transform(yFlip);
		g.transform(move);
		
		desenhaCampo(g2);
		
		objetosJogo.values().forEach(o->o.atualiza());
		
		g.dispose();
		
		BufferStrategy strategy = bufferStrategy;
		if (!strategy.contentsLost()) {
			strategy.show();
		}
		Toolkit.getDefaultToolkit().sync();
	}

	private void desenhaCampo(Graphics2D g2) {
		g2.setColor(COR_CAMPO);
		g.fill(getLimites());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.drawLine(0, getYBordaBaixo(), 0, getYBordaCima());
		g2.fillOval(getXMeio()-5, getYMeio()-5, 10, 10);
		
		Rectangle limitesGolEsquerda = golEsquerda.getLimites();
		limitesGolEsquerda.setLocation(
				getXBordaEsquerda(),
				(int) (getYMeio() - limitesGolEsquerda.getHeight()/2));
		g2.fill(limitesGolEsquerda);
		
		Rectangle limitesGolDireita = golDireita.getLimites();
		limitesGolDireita.setLocation(
				(int) (getXBordaDireita()-limitesGolDireita.getWidth()),
				(int) (getYMeio() - limitesGolEsquerda.getHeight()/2));		
		g2.fill(limitesGolDireita);
	}

	public int getYBordaBaixo() { return getHeight()/2; }
	public int getYBordaCima() { return -getHeight()/2; }
	public int getXBordaEsquerda() { return -getWidth()/2; }
	public int getXBordaDireita() {	return getWidth()/2; }

	public int getXMeio() {	return 0; }
	public int getYMeio() {	return 0; }

	public void moverBola() {
		ObjetoJogo bola = objetosJogo.get("BOLA");
		bola.setX(0);
		bola.setY(0);
		bola.setVelocidade(1);
		bola.setDirecao(180);
		bola.setAceleracao(10);
	}

	public Rectangle2D getLimites() {
		return new Rectangle2D.Double(getXBordaEsquerda(), getYBordaCima(), getWidth(), getHeight());
	}

	public Gol getGolEsquerda() {
		return golEsquerda;
	}

	public Gol getGolDireita() {
		return golDireita;
	}

	public void gooolTimeEsquerda() {
		System.out.println("Goool esquerda");
		status = StatusJogo.GOOOL;
	}

	public void gooolTimeDireita() {
		System.out.println("Goool direita");
		status = StatusJogo.GOOOL;
	}

	public StatusJogo getStatus() {
		return status;
	}
	
}
