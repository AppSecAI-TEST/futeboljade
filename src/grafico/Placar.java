package grafico;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Placar extends ObjetoJogo {
	
	public int golsEsquerda;
	public int golsDireita;
	public Placar(){
		
	}
	
	@Override
	void desenha() {
		Graphics2D g = getCampo().g;
		
		InfoAreasCampo areasCampo = getCampo().getInfoAreasCampo();
		Rectangle topo = areasCampo.getTopo();
		g.setColor(Color.BLACK);
		g.fill(topo);
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.ITALIC, 30));
		g.drawString(golsEsquerda+"", areasCampo.getXMeio()-30, 40);
		g.drawString(golsDireita+"", areasCampo.getXMeio()+30, 40);
		super.desenha();
	}

}
