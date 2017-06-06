package jogo.agent.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;;

public class JogarBehaviour extends FSMBehaviour {
	private static final String ESPERANDO = "esperando";
	private static final String COM_BOLA = "comBola";
	private static final String TIME_COM_BOLA = "timeComBola";
	private static final String SEM_BOLA = "semBola";
	private static final int COLEGA_PEGOU_BOLA = 0;
	private static final int PEGOU_BOLA_DO_ADVERSARIO = 1;
	private static final int RECEBEU_PASSE = 2;
	private static final int PASSOU_BOLA = 3;
	private static final int PERDEU_BOLA = 4;
	public static final int BOLA_EM_JOGO = 5;
	private static final int TEMPO_ACAO = 1000;

	public JogarBehaviour(Agent agent) {
		super(agent);
		registerFirstState(new EsperandoState(myAgent, TEMPO_ACAO), ESPERANDO);
		registerLastState(new SemBolaState(myAgent, TEMPO_ACAO), SEM_BOLA);
		registerState(new TimeComBolaState(), TIME_COM_BOLA);
		registerState(new ComBolaState(), COM_BOLA);
		
		registerTransition(ESPERANDO, SEM_BOLA, BOLA_EM_JOGO);
		registerTransition(SEM_BOLA, TIME_COM_BOLA, COLEGA_PEGOU_BOLA);
		registerTransition(SEM_BOLA, COM_BOLA, PEGOU_BOLA_DO_ADVERSARIO);
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

		public SemBolaState(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void onTick() {
			System.out.println("Sem bola state");
			correAtrasDaBola();
			if (colegaPegouBola())
				finalizaCom(COLEGA_PEGOU_BOLA);
			if (pegouBolaDoAdversario())
				finalizaCom(PEGOU_BOLA_DO_ADVERSARIO);
		}

		private void correAtrasDaBola(){
			getAgent().send(vouCorrerAtrasDaBolaIgualUmRetardado());
			getJogador().correAtrasDaBola();
		}
		
		private ACLMessage vouCorrerAtrasDaBolaIgualUmRetardado() {
			ACLMessage message = new ACLMessage(ACLMessage.PROPAGATE);
			getJogador().getTime().getJogadores().forEach(jogadorColega->{
				message.addReceiver(new AID(jogadorColega.getNome(), AID.ISLOCALNAME)); 
			});
			message.setLanguage("futebol");
			message.setContent("vou_correr_atras_da_bola");
			message.setOntology("Futebol-ontologia");
			return message;
		}

		private boolean colegaPegouBola() {
			// TODO Implementar
			return false;
		}

		private boolean pegouBolaDoAdversario() {
			// TODO Implementar
			return false;
		}
	}

	class TimeComBolaState extends FSMBehaviour {
	}

	class ComBolaState extends FSMBehaviour {
	}
}
