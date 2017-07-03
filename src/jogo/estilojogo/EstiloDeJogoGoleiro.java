package jogo.estilojogo;

import jogo.Jogador;

import java.util.stream.Stream;

public class EstiloDeJogoGoleiro implements EstiloDeJogo {
    @Override
    public boolean deveChutar() {
        return true;
    }

    @Override
    public int calculaErroDirecaoChute() {
        return 0;
    }

    @Override
    public int calculaAceleracaoChute() {
        return 10;
    }

    @Override
    public int calculaVelocidadeChute() {
        return 0;
    }

    @Override
    public boolean devePassar() {
        return false;
    }

    @Override
    public Jogador selecionaColegaPassarBola(Stream<Jogador> stream) {
        return Jogador.NULL;
    }

    @Override
    public void movimentaComBola(Jogador jogador) {
    }

    @Override
    public boolean deveCorrerAtrasDaBola(Jogador jogador) {
        return jogador.getDistanciaBola() < 20;
    }
}
