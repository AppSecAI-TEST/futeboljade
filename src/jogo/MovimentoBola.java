package jogo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovimentoBola {

	private int erro, aceleracao, velocidade;

	public static MovimentoBola instance() {
		return new MovimentoBola((int) (Math.random() * 20 - 10), 4, 5);
	}

	public static MovimentoBola instance(int erro, int aceleracao, int velocidade) {
		return new MovimentoBola(erro, aceleracao, velocidade);
	}

}
