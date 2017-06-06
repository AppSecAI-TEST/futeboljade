package jogo.agent.behaviour;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jogo.Jogador;
import jogo.agent.JogadorAgent;

abstract class JogoTickerBehavior extends TickerBehaviour {
	public JogoTickerBehavior(Agent a, long period) {
		super(a, period);
	}

	private int transicao;

	protected void finalizaCom(int transicao) {
		this.transicao = transicao;
		stop();
	}

	@Override
	public int onEnd() {
		return transicao;
	}

	public Jogador getJogador() {
		return ((JogadorAgent) getAgent()).getJogador();
	}
}