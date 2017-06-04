package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.Map;

import grafico.Bola;
import grafico.Gol;
import grafico.InfoAreasCampo;
import grafico.ObjetoJogo;
import grafico.StatusJogo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Campo extends Canvas {
	
	private static final Color COR_CAMPO = new Color(51, 204, 51);
	
	private Map<String, ObjetoJogo> objetosJogo;
	
	public Graphics2D g;

	private Gol golEsquerda;
	private Gol golDireita;
	private StatusJogo status;
	private InfoAreasCampo infoAreasCampo;
	
	public Campo() {
		setBackground(COR_CAMPO);
		objetosJogo = new HashMap<>();
		Bola bola = new Bola();
		bola.setCampo(this);
		golEsquerda = new Gol();
		golDireita = new Gol();
		infoAreasCampo = new InfoAreasCampo();
		objetosJogo.put("BOLA", bola);
	}
	
	public void start(){
		
		this.setIgnoreRepaint(true);
		this.createBufferStrategy(2);
		status = StatusJogo.JOGANDO;
		infoAreasCampo.inicializa(this);
		
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
		AffineTransform move = AffineTransform.getTranslateInstance(getWidth()/2, infoAreasCampo.getYBordaCima());
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
		g.fill(infoAreasCampo.getLimites());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.drawLine(0, infoAreasCampo.getYBordaBaixo(), 0, infoAreasCampo.getYBordaCima());
		g2.fillOval(infoAreasCampo.getXMeio()-5, infoAreasCampo.getYMeio()-5, 10, 10);
		g2.drawOval(infoAreasCampo.getXMeio()-30, infoAreasCampo.getYMeio()-30, 60, 60);
				
		g2.draw(infoAreasCampo.getLimitesGolEsquerda());		
		g2.draw(infoAreasCampo.getLimitesGolDireita());		
		g2.draw(infoAreasCampo.getLimitesDentroQuatroLinhas());
	}

	public void moverBola() {
		ObjetoJogo bola = objetosJogo.get("BOLA");
		bola.setX(0);
		bola.setY(0);
		bola.setVelocidade(1);
		bola.setDirecao((float) (Math.random()*360));
		bola.setAceleracao(10);
	}

	public void gooolTimeEsquerda() {
		System.out.println("Goool esquerda");
		status = StatusJogo.GOOOL;
	}

	public void gooolTimeDireita() {
		System.out.println("Goool direita");
		status = StatusJogo.GOOOL;
	}	
}
