package jogo.behaviour;

import java.util.Set;
import java.util.function.Predicate;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jogo.Jogador;

abstract class JogoTickerBehavior extends TickerBehaviour {
	protected ACLMessage message;
	protected String mensagemVindaDaInterface = "";
	private int transicao;

	public JogoTickerBehavior(Agent a, long period) {
		super(a, period);
	}

	protected void finalizaCom(int transicao) {
		this.transicao = transicao;
		stop();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onTick() {
		message = myAgent.receive();
		Object objetoMensagemVindaInterface = getJogador().getO2AObject();
		if (objetoMensagemVindaInterface instanceof Set) {
			Set<Jogador> jogadores = (Set<Jogador>) objetoMensagemVindaInterface;
			getJogador().getListaJogadores().setJogadores(jogadores);
		} else if (objetoMensagemVindaInterface instanceof String) {
			mensagemVindaDaInterface = (String) objetoMensagemVindaInterface;
		} else {
			mensagemVindaDaInterface = null;
		}
		log();
		executaPassoJogo();
		if (pegouBola() && mensagemMesmoJogador()) {
			getJogador().reiniciaColisoesPraPegarBola();
		}
	}

	private void log() {
		getJogador().debuga(getBehaviourName());
		getJogador().debuga("Mensagem interface: " + mensagemVindaDaInterface);
//		if (message != null)
//		getJogador().fala("eu ouvi " + message.getContent() + " vinda de " +
		// message.getSender().getLocalName());
	}

	abstract void executaPassoJogo();

	@Override
	public int onEnd() {
		getJogador().debuga("Finaliza com " + transicao);
		reset(JogarBehaviour.TEMPO_ACAO);
		return transicao;
	}

	protected Jogador getJogador() {
		return ((Jogador) getAgent());
	}

	protected boolean pegouBola() {
		if (message != null) {
			return Mensagens.PEGUEI_BOLA.equals(message.getContent())
					|| Mensagens.TENHO_A_BOLA.equals(message.getContent());
		}
		return false;
	}

	protected boolean disse(String evento) {
		if (message != null) {
			return evento.equals(message.getContent());
		}
		return false;
	}

	protected boolean colidiuComBola() {
		if (mensagemVindaDaInterface != null) {
			boolean colidiu = "colidiu_com_bola".equals(mensagemVindaDaInterface);
			mensagemVindaDaInterface = null;
			return colidiu;
		}
		return false;
	}

	protected boolean mensagemMesmoTime() {
		if (message != null) {
			String parametroTime = message.getUserDefinedParameter("time");
			if (parametroTime != null) {
				boolean estaNoMesmoTime = parametroTime.equals(getJogador().getTime());
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

	protected boolean mensagemOutroJogador() {
		return !mensagemMesmoJogador();
	}

	protected void propaga(String conteudo) {
		getAgent().send(mensagemPropagacao(conteudo, jogador -> outro(jogador)));
	}

	protected void propagaAoTime(String conteudo) {
		getAgent().send(mensagemPropagacao(conteudo, jogador -> mesmoTime(jogador)));
	}

	private boolean mesmoTime(Jogador jogador) {
		return outro(jogador) && jogador.getTime().equals(getJogador().getTime());
	}

	private boolean outro(Jogador jogador) {
		return !jogador.getNome().equals(getJogador().getNome());
	}


	private ACLMessage mensagemPropagacao(String conteudo, Predicate<? super Jogador> predicate) {
		ACLMessage message = new ACLMessage(ACLMessage.PROPAGATE);
		getJogador().getListaJogadores().getJogadores().stream().filter(predicate).forEach(jogador -> {
			getJogador().debuga("vou notificar " + jogador.getNome() + " que " + conteudo);
			message.addReceiver(new AID(jogador.getNome(), AID.ISLOCALNAME));
		});
		message.addUserDefinedParameter("time", getJogador().getTime());
		message.setContent(conteudo);
		return message;
	}
}