package jogo.behaviour;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jogo.Jogador;

abstract class JogoTickerBehavior extends TickerBehaviour {
	protected ACLMessage message;

	public JogoTickerBehavior(Agent a, long period) {
		super(a, period);
	}

	private int transicao;

	protected void finalizaCom(int transicao) {
		this.transicao = transicao;
		stop();
	}

	@Override
	protected void onTick() {
		message = myAgent.receive();
	}

	@Override
	public int onEnd() {
		return transicao;
	}

	public Jogador getJogador() {
		return ((Jogador) getAgent());
	}

	public Jogador getJogador(String localName) {
		return getJogador().getCampo().getJogador(localName);
	}

	protected boolean jogadorPegouBola() {
		if (message != null) {
			return message.getContent().equals("peguei_bola");
		}
		return false;
	}

	protected boolean mesmoTime() {
		if (message != null) {
			Jogador jogadorMensagem = getJogador(message.getSender().getLocalName());
			boolean naoEhOMesmo = !getJogador().equals(jogadorMensagem);
			boolean estaNoMesmoTime = getJogador().getTime().equals(jogadorMensagem.getTime());
			return naoEhOMesmo && estaNoMesmoTime;
		}
		return false;
	}
}