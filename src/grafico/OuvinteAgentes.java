package grafico;

import jogo.CampoListener;
import jogo.Jogador;
import lombok.AllArgsConstructor;
import view.Campo;

@AllArgsConstructor
public class OuvinteAgentes implements CampoListener {
	
	private Campo campo;


	@Override
	public void jogadorIndoNaDirecaoDaBolaBemLoko(Jogador jogador) {}


	@Override
	public void jogadorAdicionado(Jogador jogador) {	
	}

}
