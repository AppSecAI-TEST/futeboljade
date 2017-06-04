package grafico;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import lombok.Getter;
import lombok.Setter;
import view.Campo;

@Getter
@Setter
public class InfoAreasCampo {

	private Area golEsquerda;
	private Area golDireita;
	private Area campoNaoJogavel;
	private Area linhaDeFundo;
	private Rectangle limitesGolEsquerda;
	private Rectangle limitesGolDireita;
	private Campo campo;

	public void inicializa(Campo campo){
		this.campo = campo;
		
		this.limitesGolEsquerda = campo.getGolEsquerda().getLimites();
		limitesGolEsquerda.setLocation(
				getXBordaEsquerda(),
				(int) (getYMeio() - limitesGolEsquerda.getHeight()/2));
		
		this.limitesGolDireita = campo.getGolDireita().getLimites();
		limitesGolDireita.setLocation(
				(int) (getXBordaDireita()-limitesGolDireita.getWidth()),
				(int) (getYMeio() - limitesGolEsquerda.getHeight()/2));
		
		Area campoJogavel = new Area(getLimitesDentroQuatroLinhas());
		Area campoTotal = new Area(getLimites());
		this.golEsquerda = new Area(campo.getGolEsquerda().getLimites());
		this.golDireita = new Area(campo.getGolDireita().getLimites());
		
		campoJogavel.add(golEsquerda);
		campoJogavel.add(golDireita);
		campoTotal.subtract(campoJogavel);
		this.campoNaoJogavel = campoTotal;
		
		this.linhaDeFundo = new Area();
		linhaDeFundo.add(new Area(
				new Rectangle2D.Double(golEsquerda.getBounds2D().getX(), getYBordaCima(),
				golEsquerda.getBounds2D().getWidth(), campoTotal.getBounds2D().getHeight())));
		linhaDeFundo.add(new Area(
				new Rectangle2D.Double(golDireita.getBounds2D().getX(), getYBordaCima(),
				golDireita.getBounds2D().getWidth(), campoTotal.getBounds2D().getHeight())));
	}
	
	public Double getLimitesDentroQuatroLinhas() {
		return new Rectangle2D.Double(
				getCampo().getGolEsquerda().getLimites().getMaxX(),
				getYBordaCima()+5, 
				(getCampo().getWidth()-(getCampo().getGolDireita().getLimites().getWidth()*2)),
				getCampo().getHeight()-10);
	}
	
	public Rectangle2D getLimites() {
		return new Rectangle2D.Double(
				getXBordaEsquerda(),
				getYBordaCima(),
				getCampo().getWidth(),
				getCampo().getHeight());
	}
	
	public int getYBordaBaixo() { return getCampo().getHeight()/2; }
	public int getYBordaCima() { return -getCampo().getHeight()/2; }
	public int getXBordaEsquerda() { return -getCampo().getWidth()/2; }
	public int getXBordaDireita() {	return getCampo().getWidth()/2; }

	public int getXMeio() {	return 0; }
	public int getYMeio() {	return 0; }
	
}
