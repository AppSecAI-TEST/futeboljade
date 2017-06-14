package jogo;

import java.util.HashSet;
import java.util.Set;

import jade.core.Agent;
import jogo.behaviour.JogarBehaviour;
import lombok.Getter;
import lombok.Setter;

public class Jogador extends Agent {
	private static final short COLISOES_ATE_PEGAR_BOLA = 1;
	private static final int QUANTIDADE_MENSAGENS_FILA = 10;
	@Getter
	@Setter
	private String nome;
	@Getter
	private Time time = new Time("Sem time");

	@Getter
	@Setter
	private Campo campo;

	private Set<JogadorListener> listeners;
	private int colisoesAtePegarBola = COLISOES_ATE_PEGAR_BOLA;

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
	}

	public void setTime(Time time) {
		time.addJogador(this);
		this.time = time;
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

}
