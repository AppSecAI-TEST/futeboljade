package view;

import grafico.*;
import jogo.CampoAgentesListener;
import jogo.MovimentoBola;
import jogo.behaviour.Constants;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
@Setter
public class Campo extends Canvas {

	public static final int GAME_LOOP_SLEEP = Constants.GAME_LOOP_SLEEP;
	private static final Color COR_CAMPO = new Color(86, 188, 96);
	private static final Color COR_LATERAIS = new Color(56, 158, 66);
	private static final Color COR_CASA = new Color(255, 69, 28);
	private static final Color COR_VISITANTE = new Color(28, 205, 255);

	private Map<String, ObjetoJogo> objetosJogo;
	private Time casa, visitante;

	public Graphics2D g;

	private Gol golEsquerda;
	private Gol golDireita;
	private GrandeArea grandeAreaEsquerda;
	private GrandeArea grandeAreaDireita;
	private StatusJogo status;
	private InfoAreasCampo infoAreasCampo;
	private PosicionadorJogador posicionador;
	private CampoAgentesListener ouvinteAgentes;
	private Set<CampoGraficoListener> listeners;
	private Jogador jogadorComBola;
	private Goleiro goleiroCasa;
	private Goleiro goleiroVisitante;

	public Campo() {
		infoAreasCampo = new InfoAreasCampo();
		setBackground(COR_CAMPO);
		objetosJogo = new ConcurrentHashMap<>();

		golEsquerda = new Gol();
		golDireita = new Gol();
		grandeAreaEsquerda = new GrandeArea();
		grandeAreaDireita = new GrandeArea();
		
		addObjetoJogo("BOLA", new Bola());
		ouvinteAgentes = new OuvinteAgentes(this);
		listeners = new HashSet<>();
	}

	public void start() {
		setIgnoreRepaint(true);
		createBufferStrategy(2);
		status = StatusJogo.JOGANDO;
		infoAreasCampo.inicializa(this);
		posicionador = new PosicionadorJogador(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					gameLoop();
					try {
						Thread.sleep(GAME_LOOP_SLEEP);
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

		desenhaCampo(g2);

		Iterator<ObjetoJogo> objetos = objetosJogo.values().iterator();
		synchronized (objetos) {
			while (objetos.hasNext()) {
				ObjetoJogo objeto = objetos.next();
				objeto.atualiza();
			}
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
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);

		g2.drawLine(infoAreasCampo.getXMeio(), (int) infoAreasCampo.getLimitesDentroQuatroLinhas().getMinY(),
				infoAreasCampo.getXMeio(), (int) infoAreasCampo.getLimitesDentroQuatroLinhas().getMaxY());

		g2.fillOval(infoAreasCampo.getXMeio() - 5, infoAreasCampo.getYMeio() - 5, 10, 10);
		g2.drawOval(infoAreasCampo.getXMeio() - 60, infoAreasCampo.getYMeio() - 60, 120, 120);

		g2.draw(infoAreasCampo.getLimitesGolEsquerda());
		g2.draw(infoAreasCampo.getLimitesGolDireita());
		g2.draw(infoAreasCampo.getLimitesGrandeAreaEsquerda());
		g2.draw(infoAreasCampo.getLimitesGrandeAreaDireita());
		
		g2.draw(infoAreasCampo.getLimitesDentroQuatroLinhas());

		g2.setColor(COR_LATERAIS);
		g2.fill(infoAreasCampo.getCampoNaoJogavel());
		g2.setColor(Color.white);
		g2.draw(infoAreasCampo.getCampoNaoJogavel());
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

	public void addJogador(String nome, String nomeTime) {
		addTime(nomeTime);
		Time time;
		Jogador jogador = new Jogador().setNome(nome);
		addObjetoJogo(jogador.getNome(), jogador);
		if (nomeTime.equals(casa.getNome())) {
			posicionador.posicionaJogadorCasa(jogador);
			time = casa;
		} else {
			posicionador.posicionaJogadorVisitante(jogador);
			time = visitante;
		}
		jogador.setColor(time.getCor());
		time.addJogador(jogador);
	}

	private void addTime(String nomeTime) {
		if (casa == null){
			casa = new Time(nomeTime).setCor(COR_CASA).setGolAlvo(golDireita);
			casa.setGrandeAreaAlvo(grandeAreaDireita);
			casa.setCampoAtaque(new CampoAtaque(infoAreasCampo.getCampoDireita()));
			casa.setTipoTime(TipoTime.CASA);
		}
		else if (visitante == null){
			visitante = new Time(nomeTime).setCor(COR_VISITANTE).setGolAlvo(golEsquerda);
			visitante.setGrandeAreaAlvo(grandeAreaEsquerda);
			visitante.setCampoAtaque(new CampoAtaque(infoAreasCampo.getCampoEsquerda()));
			visitante.setTipoTime(TipoTime.VISITANTE);
		}
	}

	public void addBola() {
		Bola bola = new Bola();
		bola.setX(infoAreasCampo.getXMeio());
		bola.setY(infoAreasCampo.getYMeio());
		addObjetoJogo("BOLA", bola);		
	}

	public void jogadorSeguirBola(String nome) {
		ObjetoJogo jogador = objetosJogo.get(nome);
		ObjetoJogo bola = getBola();
		if (jogador != null) {
			jogador.setVelocidade(3);
			jogador.setAceleracao(1);
			jogador.setDirecao(GeometriaUtil.getDirecaoPara(jogador.getX(), jogador.getY(), bola.getX(), bola.getY()));
		}
	}

	public void jogadorColidiuComBola(Jogador jogador) {
		listeners.forEach(listener -> listener.jogadorColidiuComBola(jogador.getNome()));
	}

	public ObjetoJogo getBola() {
		return objetosJogo.get("BOLA");
	}

	public void addListener(CampoGraficoListener listener) {
		this.listeners.add(listener);
	}

	public Jogador getJogadorComBola() {
		return jogadorComBola;
	}

	public Jogador getJogador(String nomeJogador) {
		return (Jogador) objetosJogo.get(nomeJogador);
	}

	public void jogadorComBolaChutarGol(MovimentoBola movimentoBola) {
		if (jogadorComBola != null) {
			jogadorComBola.chutarGol(movimentoBola);
		}
	}

	public void jogadorComBolaChutarGol() {
		jogadorComBolaChutarGol(MovimentoBola.instance());
	}

	public void jogadorComBolaPassarPara(String parceiro) {
		if(jogadorComBola != null){
			jogadorComBola.passarPara(parceiro);
		}
	}

	public List<Jogador> getJogadores() {
		return getObjetosJogo()
				.values()
				.stream()
				.filter(o->o instanceof Jogador)
				.map(o->(Jogador) o)
				.collect(Collectors.toList());
	}

	public void timeVisitanteAvancarAtaque() {
		getJogadores()
		.stream()
		.filter(j->j.getTime() == visitante)
		.forEach(j->{
			j.atacar();
		});
	}
	public void timeCasaAvancarAtaque() {
		getJogadores()
		.stream()
		.filter(j->j.getTime() == casa)
		.forEach(j->{
			j.atacar();
		});
	}
	
	public void addGoleiro(String nome, String time) {
		addTime(time);
		if(time.equals("CASA"))
			addGoleiroCasa(nome);
		if(time.equals("VISITANTE"))
			addGoleiroVisitante(nome);
	}

	public void addGoleiroCasa(String nome) {
		goleiroCasa = new Goleiro(casa);
		goleiroCasa.setNome(nome);
		posicionador.posicionaGoleiroCasa(goleiroCasa);
		goleiroCasa.setGrandeAreaDefender(grandeAreaEsquerda);
		addObjetoJogo(goleiroCasa.getNome(), goleiroCasa);
	}
	
	public void addGoleiroVisitante(String nome) {
		goleiroVisitante = new Goleiro(visitante);
		goleiroVisitante.setNome(nome);
		goleiroVisitante.setGrandeAreaDefender(grandeAreaDireita);
		posicionador.posicionaGoleiroVisitante(goleiroVisitante);
		addObjetoJogo(goleiroVisitante.getNome(), goleiroVisitante);
	}
	
	private void addObjetoJogo(String nome, ObjetoJogo o){
		o.setCampo(this);
		getObjetosJogo().put(nome, o);
	}

	public void avisaQueBolaSaiu() {
		listeners.forEach(CampoGraficoListener::bolaSaiu);
	}

	public void avisaJogadorEstaNoAtaque(Jogador jogador) {
		listeners.forEach(l->l.jogadorEstaNoAtaque(jogador.getNome()));
	}

	public void avisaJogadoresAFrente(Jogador jogador, List<String> names) {
		listeners.forEach(l->l.jogadoresAFrente(jogador.getNome(), names));
	}
}
