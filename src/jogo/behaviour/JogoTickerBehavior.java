package jogo.behaviour;

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
		// getJogador().fala(getBehaviourName());
		message = myAgent.receive();
		// if (message != null)
		// getJogador().fala("eu ouvi " + message.getContent() + " vinda de " +
		// message.getSender().getLocalName());
		Object men = getJogador().getO2AObject();
		if( men != null ){
			getJogador().fala("Mensagem: " + men);
		}
//		if (men instanceof Set) { // recebe os jogadores
//			Set<Jogador> jogadores = (Set<Jogador>) men;
//			getJogador().getTime().setJogadores(jogadores);
//		}
//		mensagemVindaDaInterface = (String) myAgent.getO2AObject();
		executaPassoJogo();
	}

	abstract void executaPassoJogo();

	@Override
	public int onEnd() {
		// getJogador().fala("Finaliza com " + transicao);
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

	protected boolean chutouBola() {
		if (message != null) {
			return Mensagens.CHUTEI.equals(message.getContent());
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

	protected void propaga(String conteudo) {
		getAgent().send(mensagemPropagacao(conteudo));
	}

	private ACLMessage mensagemPropagacao(String conteudo) {
		ACLMessage message = new ACLMessage(ACLMessage.PROPAGATE);
		getJogador().getCampo().getJogadores().forEach(nomeJogador -> {
			if (!nomeJogador.equals(getJogador().getNome())) {
				// getJogador().fala("Vou dizer para " + nomeJogador + " que " +
				// conteudo);
				message.addReceiver(new AID(nomeJogador, AID.ISLOCALNAME));
			}
		});
		message.addUserDefinedParameter("time", getJogador().getTime().getNome());
		message.setContent(conteudo);
		return message;
	}
}