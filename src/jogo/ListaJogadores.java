package jogo;

import jade.util.leap.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ListaJogadores implements Serializable {
	@Getter
	@Setter
	private Set<jogo.Jogador> jogadores = new HashSet<>();

	public void addJogador(jogo.Jogador jogador) {
		jogadores.add(jogador);
	}

	public List<Jogador> getParceirosDe(Jogador jogador) {
		return jogadores.stream().filter(j -> !j.equals(jogador) && j.getTime().equals(jogador.getTime()))
				.collect(Collectors.toList());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListaJogadores other = (ListaJogadores) obj;
		if (jogadores == null) {
			if (other.jogadores != null)
				return false;
		} else if (!jogadores.equals(other.jogadores))
			return false;
		return true;
	}

}
