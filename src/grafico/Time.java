package grafico;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain=true)
public class Time {

	private String nome;
	private Set<Jogador> jogadores = new HashSet<>();
	private Color cor;
	private Gol golAlvo;
	private GrandeArea grandeAreaAlvo;
	private CampoAtaque campoAtaque;
	private TipoTime tipoTime;
	
	public Time(Color cor) {
		this.cor = cor;
	}
	public Time(String nome) {
		this.nome = nome;
	}
	public void addJogador(Jogador jogador) {
		getJogadores().add(jogador);
		jogador.setTime(this);
	}
	public List<Jogador> getJogadoresAFrente(Jogador jogador) {
		return tipoTime.getJogadoresAFrente(jogador, jogadores);
	}
	
}
