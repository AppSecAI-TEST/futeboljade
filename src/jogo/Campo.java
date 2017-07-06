package jogo;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import jogo.behaviour.Mensagens;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;

public class Campo {

    private final AgentContainer mainContainer;
    private final Map<String, AgentController> jogadores;
    private final Set<Jogador> jogadoresInformar;
    private final Set<CampoAgentesListener> listeners;
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

        new Thread(() -> {
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

    private void adicionaJogador(String nome, String time, String className) {
        Object[] args = new Object[]{nome, time, this};
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

    void mostraJogadorCorrendoAtrasDaBolaIgualUmTanso(Jogador jogador) {
        this.listeners.forEach(listener -> listener.jogadorIndoNaDirecaoDaBolaBemLoko(jogador));
    }

    @SneakyThrows
    void jogadorColidiuComBola(String nome) {
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

    void notificaQueJogadorDeveAtacar(String nome) {
        listeners.forEach(listener -> listener.jogadorDeveAtacar(nome));
    }

    void notificaQueJogadorDeveDefender(String nome) {
        listeners.forEach(listener -> listener.jogadorDeveDefender(nome));
    }

    void notificaJogadorDeveChutar(String nome, MovimentoBola movimentoBola) {
        listeners.forEach(listener -> listener.jogadorDeveChutar(nome, movimentoBola));
    }

    void notificaJogadorDevePassar(String passador, String recebedor) {
        listeners.forEach(listener -> listener.jogadorDevePassar(passador, recebedor));
    }

    @SneakyThrows
    void jogadorEstaAXDistancia(String nome, double distanciaX) {
        AgentController agentController = jogadores.get(nome);
        agentController.putO2AObject(Mensagens.Gui.DISTANCIA_BOLA + ":" + (int) distanciaX, BLOCKING);
    }

    @SneakyThrows
    void bolaEstaNaGrandeAreaDoTime(String time) {
        notificaJogadores(Mensagens.Gui.BOLA_NA_AREA_DO_TIME + ":" + time);
    }

    void bolaNaoEstaNaGrandeAreaDoTime(String time) {
        String mensagem = "saiu_da_area:" + time;
        notificaJogadores(mensagem);
    }

    private void notificaJogadores(String mensagem) {
        if (!jogadores.isEmpty()) {
            jogadores.forEach((j, ac) -> {
                try {
                    ac.putO2AObject(mensagem, BLOCKING);
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    void informaEstado(String nome, String estado) {
        listeners.forEach(l -> l.informaEstado(nome, estado));
    }

    void debuga(String nome, String mensagem) {
        listeners.forEach(l -> l.debuga(nome, mensagem));
    }

    @SneakyThrows
    void jogadorEstaNaPosicao(String nome, Jogador.PosicaoCampo posicaoCampo) {
        jogadores.get(nome).putO2AObject(posicaoCampo.toString(), BLOCKING);
    }

    void jogadoresAFrente(String nome, List<String> nomes) {
    }

    void bolaNoCentroDepoisDoGol() {
        this.setBolaEmJogo(true);
    }

    @SneakyThrows
    void golTime(String nomeTime) {
        notificaJogadores(Mensagens.Gui.GOL+":"+nomeTime);
    }

    void informaPosicaoCampo(String nome, String posicao) {
        listeners.forEach(l -> l.informaPosicaoCampo(nome, posicao));
    }

	public void bolaFoiProCentro() {
		listeners.forEach(l->l.bolaFoiProCentro());
	}

}
