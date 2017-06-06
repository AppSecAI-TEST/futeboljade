package jogo;

import java.util.Collection;
import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Time {
	@Getter
	private final String nome;

	@Getter
	private final Collection<Jogador> jogadores = new HashSet<>();

	public void addJogador(Jogador jogador) {
		jogadores.add(jogador);
	}
	
}
