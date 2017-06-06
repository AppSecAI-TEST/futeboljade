package jogo.agent;

import java.awt.Point;

import jade.core.Agent;
import jogo.Campo;
import jogo.Time;
import jogo.agent.behaviour.JogarBehaviour;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class JogadorAgent extends Agent {

	@Getter
	private jogo.Jogador jogador;

	public JogadorAgent() {
		jogador = new jogo.Jogador();
	}

	@Override
	protected void setup() {
		Object[] arguments = getArguments();
		jogador.setNome((String) arguments[0]);
		jogador.setTime(new Time((String) arguments[1]));
		jogador.setPosicao((Point) arguments[2]);
		jogador.setCampo((Campo) arguments[3]);
		addBehaviour(new JogarBehaviour(this));
	}

}
