package agent.behaviour;

import agent.Jogador;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;;

public class JogarBehaviour extends FSMBehaviour {
	public JogarBehaviour() {
		ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
		msg.addUserDefinedParameter("time", getJogador().getTime());
		myAgent.send(msg);
	}
	private Jogador getJogador(){
		return (Jogador) myAgent;
	}
}
