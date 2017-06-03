package agent;

import java.awt.Point;

import agent.behaviour.JogarBehaviour;
import jade.core.Agent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
public class Jogador extends Agent {
	@Getter
	@Setter
	private Point posicao;
	@Getter
	private String nome;
	@Getter
	@Setter
	private Time time;
	
	public Jogador() {
	}

	@Override
	protected void setup() {
		Object[] arguments = getArguments();	
		this.nome = (String) arguments[0];
		this.time = new Time((String) arguments[1]);
		this.posicao = (Point) arguments[2];
		
		addBehaviour(new JogarBehaviour());
	}

}
