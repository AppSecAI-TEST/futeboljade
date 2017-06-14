package jogo.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jogo.Jogador;

abstract class JogoTickerBehavior extends TickerBehaviour {
	protected ACLMessage message;
	protected String mensagemVindaDaInterface = "";

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
		if (message != null)
			getJogador().fala("eu ouvi " + message.getContent() + " vinda de " + message.getSender().getLocalName());
		mensagemVindaDaInterface = (String) myAgent.getO2AObject();
		executaPassoJogo();
	}
	
	abstract void executaPassoJogo();

	@Override
	public int onEnd() {
		return transicao;
	}

	public Jogador getJogador() {
		return ((Jogador) getAgent());
	}

	protected boolean jogadorPegouBola() {
		if (message != null) {
			return Mensagens.PEGUEI_BOLA.equals(message.getContent());
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
	
	protected ACLMessage mensagemPropagacao(String conteudo) {
		ACLMessage message = new ACLMessage(ACLMessage.PROPAGATE);
		getJogador().getCampo().getJogadores().forEach(nomeJogador -> {
			if(!nomeJogador.equals(getJogador().getNome())){					
				getJogador().fala("Vou dizer para " + nomeJogador + " que " + conteudo);
				message.addReceiver(new AID(nomeJogador, AID.ISLOCALNAME));
			}
		});
		message.setContent(conteudo);
		return message;
	}
}