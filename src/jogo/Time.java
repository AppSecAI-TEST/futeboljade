package jogo;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import jade.util.leap.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Time implements Serializable {
	@Getter
	private final String nome;

	@Getter
	@Setter
	private Set<jogo.Jogador> jogadores = new HashSet<>();

	public void addJogador(jogo.Jogador jogador) {
		jogadores.add(jogador);
	}

	public Stream<Jogador> getParceirosDe(Jogador jogador) {
		return jogadores.stream().filter(j -> !j.equals(jogador) && j.getTime().equals(jogador.getTime()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jogadores == null) ? 0 : jogadores.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Time other = (Time) obj;
		if (jogadores == null) {
			if (other.jogadores != null)
				return false;
		} else if (!jogadores.equals(other.jogadores))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}
