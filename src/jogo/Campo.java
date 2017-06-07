package jogo;

import java.util.HashSet;
import java.util.Set;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import lombok.Getter;
import view.CampoGraficoListener;

public class Campo implements CampoGraficoListener {

	private final AgentContainer mainContainer;
	@Getter
	private Set<Jogador> jogadores;
	private Set<CampoAgentesListener> listeners;
	@Getter
	private boolean bolaEmJogo;

	public Campo() {
		Runtime jade = Runtime.instance();
		jade.setCloseVM(true);
		Profile profile = new ProfileImpl("127.0.0.1", 1999, "linux");
		mainContainer = jade.createMainContainer(profile);
		jogadores = new HashSet<>();
		listeners = new HashSet<>();
	}

	public void setBolaEmJogo(boolean bolaEmJogo) {
		this.bolaEmJogo = bolaEmJogo;
		listeners.forEach(CampoAgentesListener::bolaEmJogo);
	}
	
	public void adicionaJogador(String nome) {
		adicionaJogador(nome, "Sem Time");
	}

	public void adicionaJogador(String nome, String time) {
		Object[] args = new Object[] { nome, time, this };
		Jogador jogador = new Jogador(nome);
		jogadores.add(jogador);
		try {
			AgentController controller = mainContainer.createNewAgent(nome, Jogador.class.getName(), args);
			controller.start();
		} catch (StaleProxyException e) {
			throw new RuntimeException(e);
		}
		listeners.forEach(listener -> listener.jogadorAdicionado(jogador));
	}

	public void addListener(CampoAgentesListener jogoListener) {
		this.listeners.add(jogoListener);
	}

	public void mostraJogadorCorrendoAtrasDaBolaIgualUmTanso(Jogador jogador) {
		this.listeners.forEach(listener -> {
			listener.jogadorIndoNaDirecaoDaBolaBemLoko(jogador);
		});
	}

	public Jogador getJogador(String localName) {
		return jogadores.stream().filter(jogador -> {
			System.out.println(localName + " " + jogador.getNome());
			return jogador.getNome().equals(localName);
		}).findFirst().orElse(new Jogador("Não encontrado"));
	}

	
	@Override
	public void jogadorColidiuComBola(String nome) {
		getJogador(nome).setColidiuComBola();
	}
	
}
