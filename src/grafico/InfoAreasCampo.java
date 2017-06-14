package grafico;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import lombok.Getter;
import lombok.Setter;
import view.Campo;

@Getter
@Setter
public class InfoAreasCampo {

	private Area campoNaoJogavel;
	private Area linhaDeFundo;
	private Rectangle limitesGolEsquerda;
	private Rectangle limitesGolDireita;
	private Campo campo;
	private Area campoJogavel;

	public void inicializa(Campo campo){
		this.campo = campo;
		iniciaLimitesGols();
		iniciaCamposJogaveisOuNao();
		iniciaLinhaDeFundo();
	}

	private void iniciaLinhaDeFundo() {
		this.linhaDeFundo = new Area();
		linhaDeFundo.add(new Area(
				new Rectangle2D.Double(
						getLimitesGolEsquerda().getX(),
						getYBordaCima(),
						getLimitesGolEsquerda().getWidth(),
						getLimitesTotais().getHeight())));
		
		linhaDeFundo.add(new Area(
				new Rectangle2D.Double(
						getLimitesGolDireita().getX(),
						getYBordaCima(),
						getLimitesGolDireita().getWidth(),
						getLimitesTotais().getBounds().getHeight())));
	}

	private void iniciaCamposJogaveisOuNao() {
		Area campoJogavel = new Area(getLimitesDentroQuatroLinhas());
		Area campoTotal = new Area(getLimitesTotais());
		
		campoJogavel.add(new Area(getLimitesGolDireita()));
		campoJogavel.add(new Area(getLimitesGolEsquerda()));
		
		this.campoJogavel = campoJogavel;
		campoTotal.subtract(campoJogavel);
		this.campoNaoJogavel = campoTotal;
	}

	private void iniciaLimitesGols() {
		setLimitesGolEsquerda(getCampo().getGolEsquerda().getLimites());
		getLimitesGolEsquerda().setLocation(
				getXBordaEsquerda()+1,
				(int) (getYMeio() - getLimitesGolEsquerda().getHeight()/2));
		
		setLimitesGolDireita(getCampo().getGolDireita().getLimites());
		getLimitesGolDireita().setLocation(
				(int) (getLimitesDentroQuatroLinhas().getMaxX()),
				(int) (getYMeio() - getLimitesGolEsquerda().getHeight()/2));
	}
	
	public Rectangle getLimitesDentroQuatroLinhas() {
		return new Rectangle(
				(int)getCampo().getGolEsquerda().getLimites().getMaxX(),
				(int)getYBordaCima()+10, 
				(int)(getCampo().getWidth() - ((getLimitesGolDireita().getWidth()*2)))-2,
				(int)getCampo().getHeight()-20);
	}
	
	public Rectangle getLimitesTotais() {
		return new Rectangle(
				getXBordaEsquerda(),
				getYBordaCima(),
				getCampo().getWidth(),
				getCampo().getHeight());
	}
	
	public Rectangle getLimitesCampoEsquerda(){
		return GeometriaUtil.getSubArea(getLimitesDentroQuatroLinhas(), 2, 1, 0, 0);	
	}
	
	public Rectangle getLimitesCampoDireita(){
		return GeometriaUtil.getSubArea(getLimitesDentroQuatroLinhas(), 2, 1, 1, 0);
	}
	
	public int getYBordaBaixo() { return getCampo().getHeight(); }
	public int getYBordaCima() { return 0; }
	public int getXBordaEsquerda() { return 0; }
	public int getXBordaDireita() {	return getCampo().getWidth(); }

	public int getXMeio() {	return getCampo().getWidth()/2; }
	public int getYMeio() {	return getCampo().getHeight()/2; }

	public Shape getLaterais() {
		Area laterais = new Area(getLimitesTotais());
		laterais.subtract(new Area(getLimitesDentroQuatroLinhas()));
		return laterais;
	}

	
}
