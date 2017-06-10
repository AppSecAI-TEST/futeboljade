package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import grafico.Bola;
import grafico.GeometriaUtil;
import grafico.Gol;
import grafico.InfoAreasCampo;
import grafico.Jogador;
import grafico.ObjetoJogo;
import grafico.OuvinteAgentes;
import grafico.PosicionadorJogador;
import grafico.StatusJogo;
import grafico.Time;
import jogo.CampoAgentesListener;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Campo extends Canvas {
	
	private static final Color COR_CAMPO = new Color(86, 188, 96);
	private static final Color COR_CASA = new Color(255, 69, 28);
	private static final Color COR_VISITANTE = new Color(28, 205, 255);
	
	private Map<String, ObjetoJogo> objetosJogo;
	private Time casa, visitante;
	
	public Graphics2D g;

	private Gol golEsquerda;
	private Gol golDireita;
	private StatusJogo status;
	private InfoAreasCampo infoAreasCampo;
	private PosicionadorJogador posicionador;
	private CampoAgentesListener ouvinteAgentes;
	private Set<CampoGraficoListener> listeners;
	
	public Campo() {
		infoAreasCampo = new InfoAreasCampo();
		setBackground(COR_CAMPO);
		objetosJogo = new HashMap<>();
		
		golEsquerda = new Gol();
		golDireita = new Gol();
		objetosJogo.put("BOLA", new Bola());
		ouvinteAgentes = new OuvinteAgentes(this);
		listeners = new HashSet<>();
	}
	
	public void start(){		
		System.out.println("start");
		this.setIgnoreRepaint(true);
		this.createBufferStrategy(2);
		status = StatusJogo.JOGANDO;
		infoAreasCampo.inicializa(this);
		posicionador = new PosicionadorJogador(this);
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
		
		Iterator<ObjetoJogo> objetos = objetosJogo.values().iterator();
		while(objetos.hasNext()){
			ObjetoJogo objeto = objetos.next();
			objeto.atualiza();
		}
		
		g.dispose();
		
		BufferStrategy strategy = bufferStrategy;
		if (!strategy.contentsLost()) {
			strategy.show();
		}
		Toolkit.getDefaultToolkit().sync();
	}

	private void desenhaCampo(Graphics2D g2) {
		g2.setColor(COR_CAMPO);
		g.fill(infoAreasCampo.getLimitesTotais());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		
		g2.drawLine(
				infoAreasCampo.getXMeio(),
				(int)infoAreasCampo.getLimitesDentroQuatroLinhas().getMinY(),
				infoAreasCampo.getXMeio(),
				(int)infoAreasCampo.getLimitesDentroQuatroLinhas().getMaxY());
		
		g2.fillOval(infoAreasCampo.getXMeio()-5, infoAreasCampo.getYMeio()-5, 10, 10);
		g2.drawOval(infoAreasCampo.getXMeio()-30, infoAreasCampo.getYMeio()-30, 60, 60);
				
		g2.draw(infoAreasCampo.getLimitesGolEsquerda());		
		g2.draw(infoAreasCampo.getLimitesGolDireita());		
		g2.draw(infoAreasCampo.getLimitesDentroQuatroLinhas());		
	}

	public void moverBola() {
		ObjetoJogo bola = getBola();
		bola.setVelocidade(1);
		bola.setDirecao(0);
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

	public void addJogadorCasa(Jogador jogador) {
		if(casa == null)
			casa = new Time(COR_CASA);
		objetosJogo.put(jogador.getNome(), jogador);
		posicionador.posicionaJogadorCasa(jogador);
		jogador.setColor(casa.getCor());
		casa.addJogador(jogador);
		jogador.setCampo(this);
	}
	
	public void addJogadorVisitante(Jogador jogador) {
		if(visitante == null) {
			visitante = new Time(COR_VISITANTE);
		}
		objetosJogo.put(jogador.getNome(), jogador);
		posicionador.posicionaJogadorVisitante(jogador);
		jogador.setColor(visitante.getCor());
		visitante.addJogador(jogador);
		jogador.setCampo(this);
	}
	
	public void addBola() {
		Bola bola = new Bola();
		bola.setX(infoAreasCampo.getXMeio());
		bola.setY(infoAreasCampo.getYMeio());
		objetosJogo.put("BOLA", bola);
		bola.setCampo(this);
	}

	public void jogadorSeguirBola(String nome) {
		ObjetoJogo jogador = objetosJogo.get(nome);
		ObjetoJogo bola = getBola();
		jogador.setVelocidade(3);
		jogador.setAceleracao(1);
		jogador.setDirecao(GeometriaUtil
				.getDirecaoPara(jogador.getX(), jogador.getY(), bola.getX(), bola.getY()));
	}

	public ObjetoJogo getBola() {
		return objetosJogo.get("BOLA");
	}

	public void addListener(CampoGraficoListener listener){
		this.listeners.add(listener);
	}
	
	public void jogadorColidiuComBola(Jogador jogador) {
		listeners.forEach(listener->listener.jogadorColidiuComBola(jogador.getNome()));
	}
}
