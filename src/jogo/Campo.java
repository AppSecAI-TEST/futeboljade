package jogo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

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
	private Set<Jogador> jogadoresInformar;
	private Set<CampoAgentesListener> listeners;
	@Getter
	private boolean bolaEmJogo;

	public Campo() {
		Runtime jade = Runtime.instance();
		jade.setCloseVM(true);
		Profile profile = new ProfileImpl("127.0.0.1", 1999, "linux");
		mainContainer = jade.createMainContainer(profile);
		jogadores = new HashMap<>();
		jogadoresInformar = new HashSet<>();
		listeners = new HashSet<>();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					jogadores.values().forEach(j -> {
						try {
							j.putO2AObject(jogadoresInformar, false);
						} catch (StaleProxyException e) {
							e.printStackTrace();
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
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
			jogadoresInformar.add(new Jogador(nome).setTime(new Time(time)));
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
		AgentController agentController = jogadores.get(nome);
		agentController.putO2AObject("colidiu_com_bola", false);
	}

	public Set<String> getJogadores() {
		return jogadores.keySet();
	}

	private void notificaJogadorAdicionado(String nome, String time) {
		listeners.forEach(listener -> listener.jogadorAdicionado(nome, time));
	}

	public void notificaJogadorPegouBola(String nome) {
		listeners.forEach(listener -> listener.jogadorPegouBola(nome));
	}

	public void notificaQueJogadorDeveParar(String nome) {
		listeners.forEach(listener -> listener.jogadorDeveParar(nome));
	}

	public void notificaJogadorDeveChutar(String nome, MovimentoBola movimentoBola) {
		listeners.forEach(listener -> listener.jogadorDeveChutar(nome, movimentoBola));
	}

	public void notificaJogadorDevePassar(String passador, String recebedor) {
		listeners.forEach(listener -> listener.jogadorDevePassar(passador, recebedor));

	}

	@SneakyThrows
	public void jogadorEstaAXDistancia(String nome, double distanciaX) {
		AgentController agentController = jogadores.get(nome);
		agentController.putO2AObject("distancia_bola:"+distanciaX, false);
	}

	@SneakyThrows
	public void jogadorEstaNaGrandeAreaAlvo(String nome) {
		AgentController agentController = jogadores.get(nome);
		agentController.putO2AObject("chegou_na_grande_area_alvo:"+nome, false);
	}

}
