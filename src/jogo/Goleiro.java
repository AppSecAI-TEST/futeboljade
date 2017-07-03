package jogo;

import jogo.behaviour.GoleiroBehaviour;
import jogo.estilojogo.EstiloDeJogoFactory;

public class Goleiro extends Jogador {
	
	@Override
	protected void configuraComportamento() {
		addBehaviour(new GoleiroBehaviour(this));
		setEstiloDeJogo( EstiloDeJogoFactory.criaEstiloDeJogo());
	}

	@Override
	protected int getColisoesAtePegarBola() {
		return 1;
	}
}
