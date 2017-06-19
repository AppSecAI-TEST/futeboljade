package jogo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import lombok.Getter;
import lombok.SneakyThrows;

public class Campo {

	private final AgentContainer mainContainer;
	private Map<String, AgentController> jogadores;
	private Set<CampoAgentesListener> listeners;
	@Getter
	private boolean bolaEmJogo;

	public Campo() {
		Runtime jade = Runtime.instance();
		jade.setCloseVM(true);
		Profile profile = new ProfileImpl("127.0.0.1", 1999, "linux");
		mainContainer = jade.createMainContainer(profile);
		jogadores = new HashMap<>();
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
		try {
			AgentController controller = mainContainer.createNewAgent(nome, Jogador.class.getName(), args);
			controller.start();
			jogadores.put(nome, controller);
		} catch (StaleProxyException e) {
			throw new RuntimeException(e);
		}
		notificaJogadorAdicionado(nome, time);
	}

	public void addListener(CampoAgentesListener jogoListener) {
		this.listeners.add(jogoListener);
	}

	public void mostraJogadorCorrendoAtrasDaBolaIgualUmTanso(Jogador jogador) {
		this.listeners.forEach(listener -> {
			listener.jogadorIndoNaDirecaoDaBolaBemLoko(jogador);
		});
	}

	@SneakyThrows
	public void jogadorColidiuComBola(String nome) {
		try{
			AgentController agentController = jogadores.get(nome);
			agentController.putO2AObject("colidiu_com_bola", false);
		}catch (Exception e) {
			System.out.println("cala a boca: " + e.getMessage());
		}
	}

	public Set<String> getJogadores() {
		return jogadores.keySet();
	}

	private void notificaJogadorAdicionado(String nome, String time) {
		listeners.forEach(listener->listener.jogadorAdicionado(nome, time));
	}

	public void notificaJogadorPegouBola(String nome){
		listeners.forEach(listener->listener.jogadorPegouBola(nome));
	}

	public void notificaQueJogadorDeveParar(String nome) {
		listeners.forEach(listener->listener.jogadorDeveParar(nome));
	}

	public void notificaJogadorDeveChutar(String nome, MovimentoBola movimentoBola) {
		listeners.forEach(listener->listener.jogadorDeveChutar(nome, movimentoBola));
	}

}
