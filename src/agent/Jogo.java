package agent;

import java.util.ArrayList;
import java.util.List;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Jogo {

	private final AgentContainer mainContainer;
	private List<String> jogadores;

	public Jogo() {
		Runtime jade = Runtime.instance();
		jade.setCloseVM(true);
		Profile profile = new ProfileImpl("127.0.0.1", 1999, "linux");
		mainContainer = jade.createMainContainer(profile);
	}

	public void inicia() {
		jogadores = new ArrayList<>();
		adicionaJogador("Julio Batista");
		adicionaJogador("Roberto Carlos");
	}
	
	public void adicionaJogador(String nome) {
		adicionaJogador(nome, 0, 0);
	}

	public void adicionaJogador(String nome, int x, int y) {
		Object[] args = new Object[] { nome, x, y };
		jogadores.add(nome);
		try {
			AgentController controller = mainContainer.createNewAgent(nome, "agent.Jogador", args);
			controller.start();
		} catch (StaleProxyException e) {
			throw new RuntimeException(e);
		}
	}
}
