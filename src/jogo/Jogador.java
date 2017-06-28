package jogo;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import jade.core.Agent;
import jade.util.leap.Serializable;
import jogo.behaviour.Constants;
import jogo.behaviour.JogarBehaviour;
import jogo.estilojogo.EstiloDeJogo;
import jogo.estilojogo.EstiloDeJogoFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class Jogador extends Agent implements Serializable {
	private static final short COLISOES_ATE_PEGAR_BOLA = 5;
	private static final int QUANTIDADE_MENSAGENS_FILA = 1;
	public static final Jogador NULL = new Jogador("nulo");
	@Getter
	@Setter @Accessors(chain=true)
	private String nome;
	@Getter @Accessors(chain=true)
	private ListaJogadores listaJogadores = new ListaJogadores();
	@Getter
	@Setter @Accessors(chain=true)
	private Campo campo;
	private EstiloDeJogo estiloDeJogo;
	private Set<JogadorListener> listeners;
	private int colisoesAtePegarBola = COLISOES_ATE_PEGAR_BOLA;
	
	@Getter @Setter @Accessors(chain=true)
	private String time;

	@Getter
	@Setter
	@Accessors(fluent = true)
	private boolean chutou, passou;

	@Override
	protected void setup() {
		Object[] arguments = getArguments();
		setNome((String) arguments[0]);
		setOutrosJogadores(new ListaJogadores());
		setTime((String) arguments[1]);
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
		this.estiloDeJogo = EstiloDeJogoFactory.criaEstiloDeJogo();
	}

	public Jogador setOutrosJogadores(ListaJogadores listaJogadores) {
		listaJogadores.addJogador(this);
		this.listaJogadores = listaJogadores;
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

	public void debuga(String mensagem) {
		if(Constants.DEBUG)
			System.out.println(getNome() + ": " + mensagem);
	}

	public void vaiProAtaque() {
		getCampo().notificaQueJogadorDeveIrProAtaque(nome);
	}

	public void jogaComBola() {
		estiloDeJogo.movimentaComBola(this);
		if (estiloDeJogo.deveChutar()) {
			int erro = estiloDeJogo.calculaErroDirecaoChute();
			int aceleracao = estiloDeJogo.calculaAceleracaoChute();
			int velocidade = estiloDeJogo.calculaVelocidadeChute();
			getCampo().notificaJogadorDeveChutar(nome, MovimentoBola.instance(erro, aceleracao, velocidade));
			reiniciaColisoesPraPegarBola();
			chutou(true);
		} else if (estiloDeJogo.devePassar()) {
			String recebedor = estiloDeJogo.selecionaColegaPassarBola(getParceiros()).getNome();
			getCampo().notificaJogadorDevePassar(nome, recebedor);
			passou(true);
		}
	}

	public Stream<Jogador> getParceiros() {
		return getListaJogadores().getParceirosDe(this);
	}
	
	public Stream<Jogador> getOutrosJogadores() {
		return getListaJogadores().getJogadores().stream().filter(j->!j.getNome().equals(nome));
	}

	public void reiniciaColisoesPraPegarBola() {
		colisoesAtePegarBola = COLISOES_ATE_PEGAR_BOLA;
	}
	
	public void preparaPegarBola() {
		colisoesAtePegarBola = 1;
	}

}
