package jogo;

import java.awt.Point;

import lombok.Getter;
import lombok.Setter;

public class Jogador {

	@Getter
	@Setter
	private Point posicao;
	@Getter
	@Setter
	private String nome;
	@Getter
	private Time time = new Time("Sem time");
	
	@Getter
	@Setter
	private Campo campo;
	
	public Jogador(){
		this("Sem nome", new Point(0,0));
	}
	
	public Jogador(String nome, Point posicao){
		this.nome = nome;
		this.posicao = posicao;
	}

	public void setTime(Time time){
		time.addJogador(this);
		this.time = time;
	}

	public void correAtrasDaBola() {
		Point bola = getCampo().getBola();
		getCampo().mostraJogadorCorrendoAtrasDaBolaIgualUmTanso(this, bola);
	}

}
