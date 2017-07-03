package jogo.behaviour;

import jade.core.Agent;

public class EsperandoState extends JogadorTickerBehavior {

		public EsperandoState(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void executaEstado() {
			if (bolaEmJogo())
				finalizaCom(JogadorBehaviour.BOLA_EM_JOGO);
		}

		private boolean bolaEmJogo() {
			return getJogador().getCampo().isBolaEmJogo();
		}

	}