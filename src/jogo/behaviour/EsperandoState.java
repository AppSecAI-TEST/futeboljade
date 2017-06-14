package jogo.behaviour;

import jade.core.Agent;

public class EsperandoState extends JogoTickerBehavior {

		public EsperandoState(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void executaPassoJogo() {
			getJogador().fala("Estou esperando");
			if (bolaEmJogo())
				finalizaCom(JogarBehaviour.BOLA_EM_JOGO);
		}

		private boolean bolaEmJogo() {
			return getJogador().getCampo().isBolaEmJogo();
		}

	}