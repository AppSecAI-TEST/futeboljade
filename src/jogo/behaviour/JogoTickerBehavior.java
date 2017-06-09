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

	protected boolean jogadorPegouBola() {
		if (message != null) {
			System.out.println(getJogador().getO2AObject());
		}
		return false;
	}

	protected boolean colidiuComBola() {
		if (message != null) {
			// o comunicador me diz que colidi
			return message.getContent().equals("voce_colidiu_com_a_bola");
		}
		return false;
	}

	protected boolean mesmoTime() {
		if (message != null) {
			String parametroTime = message.getUserDefinedParameter("time");
			if (parametroTime != null) {
				String timeJogador = getJogador().getTime().getNome();
				boolean estaNoMesmoTime = parametroTime.equals(timeJogador);
				// não é o próprio jogador e está no mesmo time
				return !mensagemMesmoJogador() && estaNoMesmoTime;
			}
		}
		return false;
	}

	protected boolean mensagemMesmoJogador() {
		if (message != null) {
			return getJogador().getLocalName().equals(message.getSender().getLocalName());
		}
		return false;
	}
}