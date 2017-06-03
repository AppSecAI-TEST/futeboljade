package agent;

import java.awt.Point;
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
	private Point bola;
	
	public Jogo() {
		Runtime jade = Runtime.instance();
		jade.setCloseVM(true);
		Profile profile = new ProfileImpl("127.0.0.1", 1999, "linux");
		mainContainer = jade.createMainContainer(profile);
	}

	public void inicia() {
		jogadores = new ArrayList<>();
		bola = new Point(10, 10);
		adicionaJogador("Julio Batista", "Brasil", 10, 10);
		adicionaJogador("Roberto Carlos", "Brasil", 100, 100);
	}
	
	public void adicionaJogador(String nome) {
		adicionaJogador(nome, "Sem Time", 0, 0);
	}

	public void adicionaJogador(String nome, String time, int x, int y) {
		Object[] args = new Object[] { nome, time , x, y };
		jogadores.add(nome);
		try {
			AgentController controller = mainContainer.createNewAgent(nome, "agent.Jogador", args);
			controller.start();
		} catch (StaleProxyException e) {
			throw new RuntimeException(e);
		}
	}
}
