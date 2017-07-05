package jogo;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Serializable;
import jogo.behaviour.JogadorBehaviour;
import jogo.behaviour.Mensagens;
import jogo.estilojogo.EstiloDeJogo;
import jogo.estilojogo.EstiloDeJogoFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@EqualsAndHashCode(of = "nome")
public class Jogador extends Agent implements Serializable {
    private static final int QUANTIDADE_MENSAGENS_FILA = 3;
    public static final Jogador NULL = new Jogador("nulo");
    @Getter
    @Setter
    @Accessors(chain = true)
    private String nome;
    @Getter
    @Accessors(chain = true)
    private ListaJogadores listaJogadores = new ListaJogadores();
    @Getter
    @Setter
    @Accessors(chain = true)
    private Campo campo;
    @Setter
    private EstiloDeJogo estiloDeJogo;
    private Set<JogadorListener> listeners;
    private int colisoesAtePegarBola;
    @Getter
    @Setter
    @Accessors(chain = true)
    private float distanciaBola = Float.MAX_VALUE;
    @Getter
    @Setter
    @Accessors(chain = true)
    private String time;
    @Getter
    @Setter
    @Accessors(fluent = true)
    private boolean chutou, passou;
    private boolean decidiuQueDevePassar;

    @Override
    protected void setup() {
        Object[] arguments = getArguments();
        setNome((String) arguments[0]);
        setOutrosJogadores(new ListaJogadores());
        setTime((String) arguments[1]);
        setCampo((Campo) arguments[2]);
        setEnabledO2ACommunication(true, QUANTIDADE_MENSAGENS_FILA);
        reiniciaColisoesPraPegarBola();
        configuraComportamento();
    }

    protected void configuraComportamento() {
        addBehaviour(new JogadorBehaviour(this));
    }

    public Jogador() {
        this("Sem nome");
    }

    public Jogador(String nome) {
        this.nome = nome;
        listeners = new HashSet<>();
        this.estiloDeJogo = EstiloDeJogoFactory.criaEstiloDeJogo();
    }

    public Jogador setOutrosJogadores(ListaJogadores listaJogadores) {
        listaJogadores.addJogador(this);
        this.listaJogadores = listaJogadores;
        return this;
    }

    public void jogaSemBola() {
        if (estiloDeJogo.deveCorrerAtrasDaBola(this)) {
            getCampo().mostraJogadorCorrendoAtrasDaBolaIgualUmTanso(this);
        } else {
            defender();
        }
    }

    public void setColidiuComBola() {
        colisoesAtePegarBola--;
        if (colisoesAtePegarBola == 0) {
            colisoesAtePegarBola = getColisoesAtePegarBola();
            listeners.forEach(listener -> listener.pegouBola());
        }
    }

    protected int getColisoesAtePegarBola() {
        return 5;
    }

    public void addListener(JogadorListener jogadorListener) {
        this.listeners.add(jogadorListener);
    }

    public void debugaSeAtivo(String mensagem) {
        getCampo().debuga(getNome(), mensagem);
    }

    public void atacar() {
        getCampo().notificaQueJogadorDeveAtacar(nome);
    }

    public void defender() {
        getCampo().notificaQueJogadorDeveDefender(nome);
    }

    public void jogaComBola() {
        // TODO extrair ifs para enum de estados do jogador: CHUTANDO, VAI_PASSAR, PASSANDO
        estiloDeJogo.movimentaComBola(this);
        if (estiloDeJogo.deveChutar()) {
            int erro = estiloDeJogo.calculaErroDirecaoChute();
            int aceleracao = estiloDeJogo.calculaAceleracaoChute();
            int velocidade = estiloDeJogo.calculaVelocidadeChute();
            getCampo().notificaJogadorDeveChutar(nome, MovimentoBola.instance(erro, aceleracao, velocidade));
            reiniciaColisoesPraPegarBola();
            chutou(true);
        } else if ( !decidiuQueDevePassar && estiloDeJogo.devePassar()) {
            send(criaMensagem(Mensagens.QUAL_SUA_DISTANCIA_DA_BOLA,
                    this::isDoMesmoTime,
                    ACLMessage.QUERY_REF,
                    Mensagens.QUAL_SUA_DISTANCIA_DA_BOLA));
            decidiuQueDevePassar = true;
            informaEstado(nome,"Devo passar a bola");
        }
        if( decidiuQueDevePassar ){
            String recebedor = estiloDeJogo.selecionaColegaPassarBola(getParceiros()).getNome();
            if(!recebedor.equals(Jogador.NULL.getNome())){
                getCampo().notificaJogadorDevePassar(nome, recebedor);
                reiniciaColisoesPraPegarBola();
                passou(true);
                decidiuQueDevePassar = false;
                estiloDeJogo.resetaNumeroRespostasDistanciaBola();
                getParceiros().forEach(j->j.setDistanciaBola(Float.MAX_VALUE));
            }
        }

    }

    public List<Jogador> getParceiros() {
        return getListaJogadores().getParceirosDe(this);
    }

    public void reiniciaColisoesPraPegarBola() {
        colisoesAtePegarBola = getColisoesAtePegarBola() * 2;
    }

    public void preparaPegarBola() {
        colisoesAtePegarBola = 1;
    }

    public void informaEstado(String nome, String estado) {
        getCampo().informaEstado(nome, estado);
    }

    public void setPosicaoCampo(PosicaoCampo posicaoCampo) {
        this.estiloDeJogo.setPosicaoCampo(posicaoCampo);
    }

    public ACLMessage criaMensagem(String conteudo, Predicate<? super Jogador> predicate) {
        int perf = ACLMessage.PROPAGATE;
        return criaMensagem(conteudo, predicate, perf, "");
    }

    private ACLMessage criaMensagem(String conteudo, Predicate<? super Jogador> predicate, int perf, String ontology) {
        ACLMessage message = new ACLMessage(perf);
        getListaJogadores().getJogadores().stream().filter(predicate).forEach(jogador -> {
            debugaSeAtivo("vou notificar " + jogador.getNome() + " que " + conteudo);
            message.addReceiver(new AID(jogador.getNome(), AID.ISLOCALNAME));
        });
        message.addUserDefinedParameter("time", getTime());
        message.setContent(conteudo);
        message.setOntology(ontology);
        return message;
    }

    public boolean isDoMesmoTime(Jogador jogador) {
        return isOutro(jogador) && jogador.getTime().equals(getTime());
    }

    public boolean isOutro(Jogador jogador) {
        return !jogador.getNome().equals(getNome());
    }

    public void propagaAoTime(String conteudo) {
        send(criaMensagem(conteudo, this::isDoMesmoTime));
    }

    public void propaga(String conteudo) {
        send(criaMensagem(conteudo, this::isOutro));
    }

    public Jogador getParceiro(String name) {
        return getParceiros().stream().filter(j -> j.getNome().equals(name)).findFirst().orElse(Jogador.NULL);
    }

    public void addDistanciaParceiroBola(String nomeParceiro, Float distancia) {
        getParceiro( nomeParceiro ).setDistanciaBola(distancia);
        estiloDeJogo.incrementaNumeroRespostasDistanciaBola();
    }

    public enum PosicaoCampo {
        INDETERMINADA {
            @Override
            public int getChanceChutar() {
                return 0;
            }

            @Override
            public int getChancePassar() {
                return 3;
            }
        }, ATAQUE {
            @Override
            public int getChanceChutar() {
                return 100;
            }

            @Override
            public int getChancePassar() {
                return 1;
            }
        };

        public abstract int getChanceChutar();

        public abstract int getChancePassar();
    }
}
