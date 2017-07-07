package jogo.behaviour;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import jogo.Jogador;
import jogo.JogadorListenerAdapter;

public class JogadorBehaviour extends FSMBehaviour {
    private static final String ESPERANDO = "esperando";
    private static final String COM_BOLA = "comBola";
    private static final String TIME_COM_BOLA = "timeComBola";
    private static final String SEM_BOLA = "semBola";
    private static final String PEGANDO_BOLA_DEPOIS_DO_GOL = "colocandoBolaNoCentro";
    private static final String INDO_PARA_O_CENTRO = "indoParaCentro";
    private static final byte COLEGA_PEGOU_BOLA = 1;
    private static final byte PEGOU_BOLA = 2;
    private static final byte PASSOU_BOLA = 4;
    private static final byte RECEBEU_PASSE = 3;
    private static final byte PERDEU_OU_PASSOU_OU_CHUTOU_BOLA = 5;
    static final byte BOLA_EM_JOGO = 7;
    static final byte GOL = 8;
    static final int SELECIONADO_BUSCAR_BOLA = 9;
    private static final int PEGOU_BOLA_PRA_COLOCAR_NO_CENTRO = 10;
    static final short TEMPO_ACAO = Constants.GAME_LOOP_SLEEP;

    protected ACLMessage message;
    private String mensagemVindaDaInterface = "";

    public JogadorBehaviour(Agent agent) {
        super(agent);
        registerFirstState(new EsperandoState(agent, TEMPO_ACAO), ESPERANDO);
        registerState(new SemBolaState(agent, TEMPO_ACAO), SEM_BOLA);
        registerState(new TimeComBolaState(agent, TEMPO_ACAO), TIME_COM_BOLA);
        registerState(new ComBolaState(agent, TEMPO_ACAO), COM_BOLA);
        registerState(new PegandoBolaDepoisDoGolState(agent, TEMPO_ACAO), PEGANDO_BOLA_DEPOIS_DO_GOL);
        registerState(new IndoParaOCentroComABolaState(agent, TEMPO_ACAO), INDO_PARA_O_CENTRO);

        registerTransition(ESPERANDO, SEM_BOLA, BOLA_EM_JOGO);

        registerTransition(SEM_BOLA, TIME_COM_BOLA, COLEGA_PEGOU_BOLA);
        registerTransition(SEM_BOLA, COM_BOLA, PEGOU_BOLA);
        // passe
        registerTransition(COM_BOLA, TIME_COM_BOLA, PASSOU_BOLA);
        registerTransition(TIME_COM_BOLA, COM_BOLA, RECEBEU_PASSE);
        // quando chuta ou perde bola
        registerTransition(COM_BOLA, SEM_BOLA, PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
        registerTransition(TIME_COM_BOLA, SEM_BOLA, PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);

        registerTransition(TIME_COM_BOLA, ESPERANDO, GOL);
        registerTransition(SEM_BOLA, ESPERANDO, GOL);
        registerTransition(COM_BOLA, ESPERANDO, GOL);

        registerTransition(SEM_BOLA, PEGANDO_BOLA_DEPOIS_DO_GOL, SELECIONADO_BUSCAR_BOLA);
        registerTransition(ESPERANDO, PEGANDO_BOLA_DEPOIS_DO_GOL, SELECIONADO_BUSCAR_BOLA);
        registerTransition(PEGANDO_BOLA_DEPOIS_DO_GOL, INDO_PARA_O_CENTRO, PEGOU_BOLA_PRA_COLOCAR_NO_CENTRO);
        registerTransition(INDO_PARA_O_CENTRO, SEM_BOLA, BOLA_EM_JOGO);

    }

    public int onEnd() {
        myAgent.doDelete();
        return super.onEnd();
    }

    protected void jogaComBola() {
        atualizaPosicaoCampo();
        getJogador().jogaComBola();
    }

    protected void jogaTimeComBola() {
        getJogador().atacar();
    }

    private void jogaSemBola() {
        getJogador().jogaSemBola();
    }

    protected Jogador getJogador() {
        return (Jogador) myAgent;
    }

    private void atualizaPosicaoCampo() {
        if (Jogador.PosicaoCampo.ATAQUE.toString().equals(mensagemVindaDaInterface))
            getJogador().setPosicaoCampo(Jogador.PosicaoCampo.ATAQUE);
        if (Jogador.PosicaoCampo.NA_AREA.toString().equals(mensagemVindaDaInterface))
            getJogador().setPosicaoCampo(Jogador.PosicaoCampo.NA_AREA);
        if (Jogador.PosicaoCampo.DEFESA.toString().equals(mensagemVindaDaInterface))
            getJogador().setPosicaoCampo(Jogador.PosicaoCampo.DEFESA);
    }

    public void executaPassoJogo(InformacoesPassoJogo informacoesPassoJogo) {
        this.mensagemVindaDaInterface = informacoesPassoJogo.getMensagemDaInterface();
        this.message = informacoesPassoJogo.getMessage();
        if (pegouBola() && mensagemMesmoJogador()) {
            getJogador().reiniciaColisoesPraPegarBola();
        }
        if (mensagemDaInterface(Mensagens.Gui.DISTANCIA_BOLA)) {
            float distancia = Float.parseFloat(mensagemVindaDaInterface.split(":")[1]);
            getJogador().setDistanciaBola(distancia);
        }
        if (perguntandoDistanciaBola()) {
            respondeDistanciaBola();
        }
        if (recebendoRespostaDistanciaBola()) {
            guardaRespostaDistanciaBola();
        }
    }

    private boolean mensagemDaInterface(String texto) {
        return mensagemVindaDaInterface != null && mensagemVindaDaInterface.contains(texto);
    }

    private boolean perguntandoDistanciaBola() {
        return message != null &&
                message.getPerformative() == ACLMessage.QUERY_REF &&
                message.getOntology().equals(Mensagens.QUAL_SUA_DISTANCIA_DA_BOLA);
    }

    private void respondeDistanciaBola() {
        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        reply.setOntology(Mensagens.MINHA_DISTANCIA_BOLA);
        reply.setContent(String.valueOf(getJogador().getDistanciaBola()));
        getJogador().send(reply);
    }

    private boolean recebendoRespostaDistanciaBola() {
        return message != null &&
                message.getPerformative() == ACLMessage.INFORM &&
                message.getOntology().equals(Mensagens.MINHA_DISTANCIA_BOLA);
    }

    private void guardaRespostaDistanciaBola() {
        Float distancia = Float.valueOf(message.getContent());
        getJogador().addDistanciaParceiroBola(message.getSender().getLocalName(), distancia);
    }

    class SemBolaState extends JogadorTickerBehavior {

        private final class FinalizadorAoPegarBola extends JogadorListenerAdapter {
            @Override
            public void pegouBola() {
                getJogador().propagaAoTime(Mensagens.PEGUEI_BOLA);
                finalizaCom(PEGOU_BOLA);
            }
        }

        SemBolaState(Agent a, long period) {
            super(a, period);
            getJogador().addListener(new FinalizadorAoPegarBola());
        }

        @Override
        protected void executaEstado() {
            escutaGol();
            if (pegouBola() && mensagemMesmoTime()) {
                finalizaCom(COLEGA_PEGOU_BOLA);
            }
            if (colidiuComBola()) {
                getJogador().setColidiuComBola();
            }
            jogaSemBola();
        }

    }

    class TimeComBolaState extends JogadorTickerBehavior {

        TimeComBolaState(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void executaEstado() {
            escutaGol();
            seOutroTimePegouBolaFinaliza();
            jogaTimeComBola();
            if (disse(Mensagens.PASSEI) && mensagemMesmoTime())
                getJogador().preparaPegarBola();
            if (disse(Mensagens.CHUTEI) || disse(Mensagens.PASSEI)) {
                finalizaCom(PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
            }
        }

        private void seOutroTimePegouBolaFinaliza() {
            if (pegouBola() && mensagemOutroTime()) {
                getJogador().reiniciaColisoesPraPegarBola();
                finalizaCom(PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
            }
        }
    }

    class ComBolaState extends JogadorTickerBehavior {

        ComBolaState(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void executaEstado() {
            escutaGol();
            seOutroPegouBolaFinaliza();
            avisaQueTemBola();
            jogaComBola();
            if (getJogador().chutou()) {
                getJogador().chutou(false);
                getJogador().propaga(Mensagens.CHUTEI);
                finalizaCom(PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
            }
            if (getJogador().passou()) {
                getJogador().passou(false);
                getJogador().propagaAoTime(Mensagens.PASSEI);
                finalizaCom(PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
            }
        }

        private void avisaQueTemBola() {
            getJogador().propaga(Mensagens.TENHO_A_BOLA);
            getJogador().getCampo().notificaJogadorPegouBola(getJogador().getNome());
        }

        private void seOutroPegouBolaFinaliza() {
            if (pegouBola() && mensagemOutroJogador()) {
                getJogador().debuga("Perdi a bola :(");
                getJogador().reiniciaColisoesPraPegarBola();
                finalizaCom(PERDEU_OU_PASSOU_OU_CHUTOU_BOLA);
            }
        }

    }

    class PegandoBolaDepoisDoGolState extends JogadorTickerBehavior {

        PegandoBolaDepoisDoGolState(Agent a, long period) {
            super(a, period);
        }

        @Override
        void executaEstado() {
            getJogador().propagaAoTime(Mensagens.VOU_BOTAR_BOLA_NO_CENTRO);
            getJogador().seguirBola();
            if(colidiuComBola()){
                finalizaCom(PEGOU_BOLA_PRA_COLOCAR_NO_CENTRO);
            }
        }

    }

    class IndoParaOCentroComABolaState extends JogadorTickerBehavior {

        IndoParaOCentroComABolaState(Agent a, long period) {
            super(a, period);
        }

        @Override
        void executaEstado() {
            reiniciaJogoSeBolaChegouNoCentro();
            getJogador().propagaAoTime(Mensagens.VOU_BOTAR_BOLA_NO_CENTRO);
            getJogador().vaiParaOCentroComABola();
        }

    }

    private boolean pegouBola() {
        return message != null && (Mensagens.PEGUEI_BOLA.equals(message.getContent()) || Mensagens.TENHO_A_BOLA.equals(message.getContent()));
    }

    private boolean disse(String evento) {
        return message != null && evento.equals(message.getContent());
    }

    private boolean colidiuComBola() {
        if (mensagemVindaDaInterface != null) {
            boolean colidiu = Mensagens.Gui.COLIDIU_COM_BOLA.equals(mensagemVindaDaInterface);
            mensagemVindaDaInterface = null;
            return colidiu;
        }
        return false;
    }

    private boolean mensagemMesmoTime() {
        if (message != null) {
            String parametroTime = message.getUserDefinedParameter("time");
            if (parametroTime != null) {
                boolean estaNoMesmoTime = parametroTime.equals(getJogador().getTime());
                return mensagemOutroJogador() && estaNoMesmoTime;
            }
        }
        return false;
    }

    private boolean mensagemOutroTime() {
        return !mensagemMesmoTime();
    }

    private boolean mensagemMesmoJogador() {
        return message != null && getJogador().getLocalName().equals(message.getSender().getLocalName());
    }

    private boolean mensagemOutroJogador() {
        return !mensagemMesmoJogador();
    }

}
