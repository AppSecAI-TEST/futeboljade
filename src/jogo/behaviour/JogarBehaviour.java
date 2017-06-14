package jogo.behaviour;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jogo.JogadorListenerAdapter;;

public class JogarBehaviour extends FSMBehaviour {
	private static final String ESPERANDO = "esperando";
	private static final String COM_BOLA = "comBola";
	private static final String TIME_COM_BOLA = "timeComBola";
	private static final String SEM_BOLA = "semBola";
	private static final int COLEGA_PEGOU_BOLA = 1;
	private static final int PEGOU_BOLA = 2;
	private static final int RECEBEU_PASSE = 3;
	private static final int PASSOU_BOLA = 4;
	private static final int PERDEU_BOLA = 5;
	public static final int BOLA_EM_JOGO = 6;
	private static final int TEMPO_ACAO = 5000;

	public JogarBehaviour(Agent agent) {
		super(agent);
		registerFirstState(new EsperandoState(agent, TEMPO_ACAO), ESPERANDO);
		registerState(new SemBolaState(agent, TEMPO_ACAO), SEM_BOLA);
		registerState(new TimeComBolaState(agent, TEMPO_ACAO), TIME_COM_BOLA);
		registerState(new ComBolaState(agent, TEMPO_ACAO), COM_BOLA);

		registerTransition(ESPERANDO, SEM_BOLA, BOLA_EM_JOGO);
		registerTransition(SEM_BOLA, TIME_COM_BOLA, COLEGA_PEGOU_BOLA);
		registerTransition(SEM_BOLA, COM_BOLA, PEGOU_BOLA);
		registerTransition(COM_BOLA, SEM_BOLA, PERDEU_BOLA);
		registerTransition(COM_BOLA, TIME_COM_BOLA, PASSOU_BOLA);
		registerTransition(TIME_COM_BOLA, COM_BOLA, RECEBEU_PASSE);
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
				getJogador().send(mensagemPropagacao(Mensagens.PEGUEI_BOLA));
				finalizaCom(PEGOU_BOLA);
			}
		}

		public SemBolaState(Agent a, long period) {
			super(a, period);
			getJogador().addListener(new FinalizadorAoPegarBola());
		}

		@Override
		protected void executaPassoJogo() {
			correAtrasDaBola();
			if (jogadorPegouBola()) {
				getJogador().fala("Alguém pegou a bola");
			}
			if (jogadorPegouBola() && !mensagemMesmoJogador()) {
				getJogador().reiniciaContagemColisoesAtePegarBola();
			}
			if (jogadorPegouBola() && mesmoTime()) {
				getJogador().fala("Meu colega pegou a bola");
				finalizaCom(COLEGA_PEGOU_BOLA);
			}
			if (colidiuComBola()) {
				getJogador().setColidiuComBola();
			}
		}

		private void correAtrasDaBola() {
			getAgent().send(mensagemPropagacao(Mensagens.CORRENDO_ATRAS_DA_BOLA));
			getJogador().correAtrasDaBola();
		}

	}

	class TimeComBolaState extends JogoTickerBehavior {

		public TimeComBolaState(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void executaPassoJogo() {
			getJogador().fala("Time com bola galera!");
		}
	}

	class ComBolaState extends JogoTickerBehavior {
		public ComBolaState(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void executaPassoJogo() {
			getJogador().fala("Tenho a bola");
		}
	}
}
