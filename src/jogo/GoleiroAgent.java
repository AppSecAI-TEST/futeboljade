package jogo;

import jade.core.Agent;
import jade.util.leap.Serializable;
import lombok.Data;

@Data
public class GoleiroAgent extends Agent implements Serializable {

	private String time;

	public GoleiroAgent() {
		
	}
}
