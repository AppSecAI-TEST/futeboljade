package jogo.behaviour;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jogo.JogadorListenerAdapter;;

public class JogarBehaviour extends FSMBehaviour {
	private static final String ESPERANDO = "esperando";
	private static final String COM_BOLA = "comBola";
	private static final String TIME_COM_BOLA = "timeComBola";
	private static final String SEM_BOLA = "semBola";
	private static final byte COLEGA_PEGOU_BOLA = 1;
	private static final byte PEGOU_BOLA = 2;
	private static final byte RECEBEU_PASSE = 3;
	private static final byte PASSOU_BOLA = 4;
	private static final byte PERDEU_OU_CHUTOU_BOLA = 5;
	public static final byte BOLA_EM_JOGO = 7;
	public static final byte TEMPO_ACAO = 30;

	public JogarBehaviour(Agent agent) {
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
		registerTransition(COM_BOLA, SEM_BOLA, PERDEU_OU_CHUTOU_BOLA);
		registerTransition(TIME_COM_BOLA, SEM_BOLA, PERDEU_OU_CHUTOU_BOLA);
	}

	public int onEnd() {
		System.out.println("FSM behaviour completed.");
		myAgent.doDelete();
		return super.onEnd();
	}

	class SemBolaState extends JogoTickerBehavior {

		private final class FinalizadorAoPegarBola extends JogadorListenerAdapter {
			@Override
			public void pegouBola() {
				propaga(Mensagens.PEGUEI_BOLA);
				finalizaCom(PEGOU_BOLA);
			}
		}

		public SemBolaState(Agent a, long period) {
			super(a, period);
			getJogador().addListener(new FinalizadorAoPegarBola());
		}

		@Override
		protected void executaPassoJogo() {
			if (pegouBola() && !mensagemMesmoJogador()) {
				getJogador().reiniciaContagemColisoesAtePegarBola();
			}
			if (pegouBola() && mensagemMesmoTime()) {
				finalizaCom(COLEGA_PEGOU_BOLA);
			}
			if (colidiuComBola()) {
				getJogador().setColidiuComBola();
			}
			correAtrasDaBola();
		}

		private void correAtrasDaBola() {
			propaga(Mensagens.CORRENDO_ATRAS_DA_BOLA);
			getJogador().correAtrasDaBola();
		}

	}

	class TimeComBolaState extends JogoTickerBehavior {

		public TimeComBolaState(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void executaPassoJogo() {
			getJogador().para();
			if(chutouBola()){
				finalizaCom(PERDEU_OU_CHUTOU_BOLA);
			}
		}
	}

	class ComBolaState extends JogoTickerBehavior {
		public ComBolaState(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void executaPassoJogo() {
			propaga(Mensagens.TENHO_A_BOLA);
			getJogador().getCampo().notificaJogadorPegouBola(getJogador().getNome());
			getJogador().jogaComBola();
			if( getJogador().chutou() ) {
				getJogador().chutou(false);
				propaga(Mensagens.CHUTEI);
				finalizaCom(PERDEU_OU_CHUTOU_BOLA);
			}
		}
	}
}
