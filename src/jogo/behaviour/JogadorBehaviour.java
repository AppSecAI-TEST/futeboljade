package jogo.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import jogo.Jogador;
import jogo.JogadorListenerAdapter;

import java.util.function.Predicate;

public class JogadorBehaviour extends FSMBehaviour {
	private static final String ESPERANDO = "esperando";
	private static final String COM_BOLA = "comBola";
	private static final String TIME_COM_BOLA = "timeComBola";
	private static final String SEM_BOLA = "semBola";
	private static final byte COLEGA_PEGOU_BOLA = 1;
	private static final byte PEGOU_BOLA = 2;
	private static final byte PASSOU_BOLA = 4;
	private static final byte RECEBEU_PASSE = 3;
	private static final byte PERDEU_OU_PASSOU_OU_CHUTOU_BOLA = 5;
	static final byte BOLA_EM_JOGO = 7;
	static final short TEMPO_ACAO = Constants.GAME_LOOP_SLEEP;

	protected ACLMessage message;
	private String mensagemVindaDaInterface = "";

	public JogadorBehaviour(Agent agent) {
		super(agent);
		registerFirstState(new EsperandoState(agent, TEMPO_ACAO), ESPERANDO);
		registerState(new SemBolaState(agent, TEMPO_ACAO), SEM_BOLA);
		registerState(new TimeComBolaState(agent, TEMPO_ACAO), TIME_COM_BOLA);
		registerState(new ComBolaState(agent, TEMPO_ACAO), COM_BOLA);

		registerTransition(ESPERANDO, SEM_BOLA, BOLA_EM_JOGO);

		registerTransition(SEM_BOLA, TIME_COM_BOLA, COLEGA_PEGOU_BOLA);
		registerTransition(SEM_BOLA, COM_BOLA, PEGOU_BOLA);
		// passe
		registerTransition(COM_BOLA, TIME_COM_BOLA, PASSOU_BOLA);
		registerTransition(TIME_COM_BOLA, COM_BOLA, RECEBEU_PASSE);
		// quando chuta ou perde bola
		registerTransition(COM_BOLA, SEM_BOLA, PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
		registerTransition(TIME_COM_BOLA, SEM_BOLA, PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
	}

	public int onEnd() {
		myAgent.doDelete();
		return super.onEnd();
	}

	protected void jogaComBola() {
		getJogador().jogaComBola();
	}

	protected void jogaTimeComBola() {
		getJogador().atacar();
	}

	protected void jogaSemBola() {
		getJogador().jogaSemBola();
	}

	protected Jogador getJogador() {
		return (Jogador) myAgent;
	}

	public void executaPassoJogo(InformacoesPassoJogo informacoesPassoJogo) {
		this.mensagemVindaDaInterface = informacoesPassoJogo.getMensagemDaInterface();
		this.message = informacoesPassoJogo.getMessage();
		if (pegouBola() && mensagemMesmoJogador()) {
			getJogador().reiniciaColisoesPraPegarBola();
		}
		if( mensagemVindaDaInterface != null && mensagemVindaDaInterface.contains("distancia_bola") ){
			float distancia = Float.parseFloat(mensagemVindaDaInterface.split(":")[1]);
			getJogador().setDistanciaBola(distancia);
		}
	}

	class SemBolaState extends JogadorTickerBehavior {

		private final class FinalizadorAoPegarBola extends JogadorListenerAdapter {
			@Override
			public void pegouBola() {
				propagaAoTime(Mensagens.PEGUEI_BOLA);
				finalizaCom(PEGOU_BOLA);
			}
		}

		SemBolaState(Agent a, long period) {
			super(a, period);
			getJogador().addListener(new FinalizadorAoPegarBola());
		}

		@Override
		protected void executaEstado() {
			if (pegouBola() && mensagemMesmoTime()) {
				finalizaCom(COLEGA_PEGOU_BOLA);
			}
			if (colidiuComBola()) {
				getJogador().setColidiuComBola();
			}
			jogaSemBola();
		}

	}

	class TimeComBolaState extends JogadorTickerBehavior {

		TimeComBolaState(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void executaEstado() {
			seOutroTimePegouBolaFinaliza();
			jogaTimeComBola();
			if(disse(Mensagens.PASSEI) && mensagemMesmoTime())
				getJogador().preparaPegarBola();
			if (disse(Mensagens.CHUTEI) || disse(Mensagens.PASSEI)) {
				finalizaCom(PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
			}
		}

		private void seOutroTimePegouBolaFinaliza() {
			if (pegouBola() && mensagemOutroTime()) {
				getJogador().reiniciaColisoesPraPegarBola();
				finalizaCom(PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
			}
		}
	}

	class ComBolaState extends JogadorTickerBehavior {

		ComBolaState(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void executaEstado() {
			seOutroPegouBolaFinaliza();
			avisaQueTemBola();
			jogaComBola();
			if (getJogador().chutou()) {
				getJogador().chutou(false);
				propaga(Mensagens.CHUTEI);
				finalizaCom(PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
			}
			if (getJogador().passou()) {
				getJogador().passou(false);
				propagaAoTime(Mensagens.PASSEI);
				finalizaCom(PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
			}
		}

		private void avisaQueTemBola() {
			propaga(Mensagens.TENHO_A_BOLA);
			getJogador().getCampo().notificaJogadorPegouBola(getJogador().getNome());
		}

		private void seOutroPegouBolaFinaliza() {
			if (pegouBola() && mensagemOutroJogador()) {
				getJogador().debugaSeAtivo("Perdi a bola :(");
				getJogador().reiniciaColisoesPraPegarBola();
				finalizaCom(PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
			}
		}

	}


	private boolean pegouBola() {
		return message != null && (Mensagens.PEGUEI_BOLA.equals(message.getContent()) || Mensagens.TENHO_A_BOLA.equals(message.getContent()));
	}

	private boolean disse(String evento) {
		return message != null && evento.equals(message.getContent());
	}

	private boolean colidiuComBola() {
		if (mensagemVindaDaInterface != null) {
			boolean colidiu = "colidiu_com_bola".equals(mensagemVindaDaInterface);
			mensagemVindaDaInterface = null;
			return colidiu;
		}
		return false;
	}

	private boolean mensagemMesmoTime() {
		if (message != null) {
			String parametroTime = message.getUserDefinedParameter("time");
			if (parametroTime != null) {
				boolean estaNoMesmoTime = parametroTime.equals(getJogador().getTime());
				return mensagemOutroJogador() && estaNoMesmoTime;
			}
		}
		return false;
	}

	private boolean mensagemOutroTime(){
		return !mensagemMesmoTime();
	}

	private boolean mensagemMesmoJogador() {
		return message != null && getJogador().getLocalName().equals(message.getSender().getLocalName());
	}

	private boolean mensagemOutroJogador() {
		return !mensagemMesmoJogador();
	}

	private void propaga(String conteudo) {
		getAgent().send(mensagemPropagacao(conteudo, this::outro));
	}

	private void propagaAoTime(String conteudo) {
		getAgent().send(mensagemPropagacao(conteudo, this::mesmoTime));
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
			getJogador().debugaSeAtivo("vou notificar " + jogador.getNome() + " que " + conteudo);
			message.addReceiver(new AID(jogador.getNome(), AID.ISLOCALNAME));
		});
		message.addUserDefinedParameter("time", getJogador().getTime());
		message.setContent(conteudo);
		return message;
	}



}
