package agent.behaviour;

import agent.Jogador;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;;

public class JogarBehaviour extends FSMBehaviour {
	private static final String COM_BOLA = "comBola";
	private static final String TIME_COM_BOLA = "timeComBola";
	private static final String SEM_BOLA = "semBola";
	private final int COLEGA_PEGOU_BOLA = 0;
	private final int PEGOU_BOLA_DO_ADVERSARIO = 1;
	private final int RECEBEU_PASSE = 2;
	private final int PASSOU_BOLA = 3;
	private final int PERDEU_BOLA = 4;
	private final int TEMPO_ACAO = 100;

	public JogarBehaviour() {
		registerFirstState(new SemBolaState(myAgent, TEMPO_ACAO), SEM_BOLA);
		registerState(new TimeComBolaState(), TIME_COM_BOLA);
		registerState(new ComBolaState(), COM_BOLA);

		registerTransition(SEM_BOLA, TIME_COM_BOLA, COLEGA_PEGOU_BOLA);
		registerTransition(SEM_BOLA, COM_BOLA, PEGOU_BOLA_DO_ADVERSARIO);
		registerTransition(COM_BOLA, TIME_COM_BOLA, PASSOU_BOLA);
		registerTransition(TIME_COM_BOLA, COM_BOLA, RECEBEU_PASSE);

	}

	class SemBolaState extends TickerBehaviour {
		private int transicao;

		public SemBolaState(Agent a, long period) {
			super(a, period);
		}

		@Override
		public int onEnd() {
			return transicao;
		}

		@Override
		protected void onTick() {
			// Verifica se colega pegou bola, se sim, finaliza
			if (colegaPegouBola()) {
				transicao = COLEGA_PEGOU_BOLA;
				stop();
			}
			if (pegouBolaDoAdversario()){
				transicao = PEGOU_BOLA_DO_ADVERSARIO;
				stop();
			}
		}

		private boolean colegaPegouBola() {
			return false;
		}
	}

	class TimeComBolaState extends FSMBehaviour {
	}

	class ComBolaState extends FSMBehaviour {
	}
}
