package jogo;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import jade.core.Agent;
import jade.util.leap.Serializable;
import jogo.behaviour.JogarBehaviour;
import jogo.estilojogo.EstiloDeJogo;
import jogo.estilojogo.EstiloDeJogoFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class Jogador extends Agent implements Serializable {
	private static final short COLISOES_ATE_PEGAR_BOLA = 4;
	private static final int QUANTIDADE_MENSAGENS_FILA = 10;
	public static final Jogador NULL = new Jogador("nulo");
	@Getter
	@Setter @Accessors(chain=true)
	private String nome;
	@Getter @Accessors(chain=true)
	private Time time = new Time("Sem time");
	@Getter
	@Setter @Accessors(chain=true)
	private Campo campo;
	private EstiloDeJogo estiloDeJogo;
	private Set<JogadorListener> listeners;
	private int colisoesAtePegarBola = COLISOES_ATE_PEGAR_BOLA;

	@Getter
	@Setter
	@Accessors(fluent = true)
	private boolean chutou, passou;

	@Override
	protected void setup() {
		Object[] arguments = getArguments();
		setNome((String) arguments[0]);
		setTime(new Time((String) arguments[1]));
		setCampo((Campo) arguments[2]);
		addBehaviour(new JogarBehaviour(this));
		setEnabledO2ACommunication(true, QUANTIDADE_MENSAGENS_FILA);
	}

	public Jogador() {
		this("Sem nome");
	}

	public Jogador(String nome) {
		this.nome = nome;
		listeners = new HashSet<>();
		this.estiloDeJogo = EstiloDeJogoFactory.estiloAleatorio();
	}

	public Jogador setTime(Time time) {
		time.addJogador(this);
		this.time = time;
		return this;
	}

	public void correAtrasDaBola() {
		getCampo().mostraJogadorCorrendoAtrasDaBolaIgualUmTanso(this);
	}

	public void setColidiuComBola() {
		colisoesAtePegarBola--;
		if (colisoesAtePegarBola == 0) {
			colisoesAtePegarBola = COLISOES_ATE_PEGAR_BOLA;
			listeners.forEach(listener -> listener.pegouBola());
		}
	}

	public void addListener(JogadorListener jogadorListener) {
		this.listeners.add(jogadorListener);
	}

	public void reiniciaContagemColisoesAtePegarBola() {
		colisoesAtePegarBola = COLISOES_ATE_PEGAR_BOLA;
	}

	public void fala(String mensagem) {
		System.out.println(getNome() + ": " + mensagem);
	}

	public void vaiProAtaque() {
		getCampo().notificaQueJogadorDeveIrProAtaque(nome);
	}

	public void jogaComBola() {
		if (estiloDeJogo.deveChutar()) {
			int erro = estiloDeJogo.calculaErroDirecaoChute();
			int aceleracao = estiloDeJogo.calculaAceleracaoChute();
			int velocidade = estiloDeJogo.calculaVelocidadeChute();
			getCampo().notificaJogadorDeveChutar(nome, MovimentoBola.instance(erro, aceleracao, velocidade));
			chutou(true);
		} else if (estiloDeJogo.devePassar()) {
			String recebedor = estiloDeJogo.selecionaColegaPassarBola(getParceiros()).getNome();
			getCampo().notificaJogadorDevePassar(nome, recebedor);
			passou(true);
		}
	}

	public Stream<Jogador> getParceiros() {
		return getTime().getParceirosDe(this);
	}

	public void preparaParaReceberPasse() {
		colisoesAtePegarBola = 1;
	}

}
