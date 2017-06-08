package jogo;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;
import lombok.SneakyThrows;

public class AgenteComunicador extends Agent {

	@Override
	protected void setup() {
		addBehaviour(new TickerBehaviour(this, 100) {
			@Override
			protected void onTick() {
			}
		});
	}
	
	@SneakyThrows
	public AID pesquisa(String nome) {
		SearchConstraints c = new SearchConstraints();
		c.setMaxResults (new Long(-1));
		AMSAgentDescription[] agentes = AMSService.search(this, new AMSAgentDescription(), c);
		for (AMSAgentDescription agente : agentes) {
			if(agente.getName().getLocalName().equals(nome))
				return agente.getName();
		}
		return new AID();
	}

	public void jogadorColidiuComBola(String nome) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setOntology("Futebol");
		message.addReceiver(pesquisa(nome));
		message.setContent("voce_colidiu_com_a_bola");
		send(message);
	}

}
