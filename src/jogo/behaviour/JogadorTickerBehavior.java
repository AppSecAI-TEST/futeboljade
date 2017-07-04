package jogo.behaviour;

import jade.core.Agent;
import jade.core.behaviours.CompositeBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jogo.Jogador;

import java.util.Set;

abstract class JogadorTickerBehavior extends TickerBehaviour {
	private int transicao;
	private ACLMessage message;
	private String mensagemVindaDaInterface;
	public JogadorTickerBehavior(Agent a, long period) {
		super(a, period);
	}

	protected void finalizaCom(int transicao) {
		this.transicao = transicao;
		stop();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onTick() {
		Object objetoMensagemVindaInterface = getJogador().getO2AObject();
		if (objetoMensagemVindaInterface instanceof Set) {
			Set<Jogador> jogadores = (Set<Jogador>) objetoMensagemVindaInterface;
			getJogador().getListaJogadores().setJogadores(jogadores);
		}
		if (objetoMensagemVindaInterface instanceof String) {
			mensagemVindaDaInterface = (String) objetoMensagemVindaInterface;
		} else {
			mensagemVindaDaInterface = null;
		}
		message = myAgent.receive();
		log();
		getJogador().informaEstado( getJogador().getNome(), getBehaviourName() );
		executaPassoJogo();
		executaEstado();
	}

	private void executaPassoJogo() {
		CompositeBehaviour parent = getParent();
		if(parent instanceof JogadorBehaviour){
			((JogadorBehaviour)parent).executaPassoJogo( new InformacoesPassoJogo(message, mensagemVindaDaInterface));
		}
	}

	private void log() {
		if(mensagemVindaDaInterface != null)
			getJogador().debugaSeAtivo("MI: " + mensagemVindaDaInterface);
		if (message != null) {
			String string = "ouvi " + message.getContent() + " de " + message.getSender().getLocalName();
			getJogador().debugaSeAtivo(string);
		}
	}

	abstract void executaEstado();

	@Override
	public int onEnd() {
		reset(JogadorBehaviour.TEMPO_ACAO);
		return transicao;
	}

	protected Jogador getJogador() {
		return ((Jogador) getAgent());
	}

}