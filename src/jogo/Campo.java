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
	private Set<Jogador> jogadoresInformar;
	private Set<CampoAgentesListener> listeners;
	private static final boolean BLOCKING = false;

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
				while (true) {
					jogadores.values().forEach(j -> {
						try {
							j.putO2AObject(jogadoresInformar, BLOCKING);
						} catch (StaleProxyException e) {
							e.printStackTrace();
						}
					});
					try {
						Thread.sleep(2000);
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

	public void adicionaJogador(String nome, String time) {
		adicionaJogador(nome, time, Jogador.class.getName());
	}

	public void adicionaGoleiro(String nome, String time) {
		adicionaJogador(nome, time, Goleiro.class.getName());
	}

	public void adicionaJogador(String nome, String time, String className) {
		Object[] args = new Object[] { nome, time, this };
		try {
			AgentController controller = mainContainer.createNewAgent(nome, className, args);
			controller.start();
			jogadores.put(nome, controller);
			jogadoresInformar.add(new Jogador(nome).setTime(time).setOutrosJogadores(new ListaJogadores()));
		} catch (StaleProxyException e) {
			throw new RuntimeException(e);
		}
		if (className.equals(Goleiro.class.getName()))
			notificaGoleiroAdicionado(nome, time);
		else
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
		agentController.putO2AObject("colidiu_com_bola", BLOCKING);
	}

	private void notificaJogadorAdicionado(String nome, String time) {
		listeners.forEach(listener -> listener.jogadorAdicionado(nome, time));
	}

	private void notificaGoleiroAdicionado(String nome, String time) {
		listeners.forEach(listener -> listener.goleiroAdicionado(nome, time));
	}

	public void notificaJogadorPegouBola(String nome) {
		listeners.forEach(listener -> listener.jogadorPegouBola(nome));
	}

	public void notificaQueJogadorDeveAtacar(String nome) {
		listeners.forEach(listener -> listener.jogadorDeveAtacar(nome));
	}
	
	public void notificaQueJogadorDeveDefender(String nome) {
		listeners.forEach(listener -> listener.jogadorDeveDefender(nome));
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
		agentController.putO2AObject("distancia_bola:" + distanciaX, BLOCKING);
	}

	@SneakyThrows
	public void jogadorEstaNaGrandeAreaAlvo(String nome) {
		AgentController agentController = jogadores.get(nome);
		agentController.putO2AObject("chegou_na_grande_area_alvo:" + nome, BLOCKING);
	}

	@SneakyThrows
	public void bolaEstaNaGrandeAreaDoTime(String time) {
		if(!jogadores.isEmpty()){
			jogadores.forEach((j, ac) -> {
				try {
					ac.putO2AObject("chegou_na_grande_area_alvo:" + time, BLOCKING);
				} catch (StaleProxyException e) {
					e.printStackTrace();
				}
			});
		}
	}

	public void bolaNaoEstaNaGrandeAreaDoTime(String time) {
		if(!jogadores.isEmpty()){
			jogadores.forEach((j, ac) -> {
				try {
					ac.putO2AObject("saiu_da_area:" + time, BLOCKING);
				} catch (StaleProxyException e) {
					e.printStackTrace();
				}
			});
		}
	}

	public void informaEstado(String nome, String estado) {
		listeners.forEach(l->l.informaEstado(nome, estado));
	}
}
