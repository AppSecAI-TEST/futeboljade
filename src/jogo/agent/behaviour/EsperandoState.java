package jogo.agent.behaviour;

import jade.core.Agent;

public class EsperandoState extends JogoTickerBehavior {

		public EsperandoState(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void onTick() {
			System.out.println("EsperandoState");
			if (bolaEmJogo())
				finalizaCom(JogarBehaviour.BOLA_EM_JOGO);
		}

		private boolean bolaEmJogo() {
			return getJogador().getCampo().isBolaEmJogo();
		}

	}