package grafico;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum TipoTime implements VisorJogadores{
	
	CASA {

		@Override
		public List<Jogador> getJogadoresAFrente(Jogador jogador, Set<Jogador> jogadores) {
			return jogadores.stream().filter(j->j.getX() > jogador.getX()).collect(Collectors.toList());
		}
		
	}, VISITANTE{

		@Override
		public List<Jogador> getJogadoresAFrente(Jogador jogador, Set<Jogador> jogadores) {
			return jogadores.stream().filter(j->j.getX() < jogador.getX()).collect(Collectors.toList());
		}
	}

}

interface VisorJogadores{
	List<Jogador> getJogadoresAFrente(Jogador jogador, Set<Jogador> jogadores);
}
