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
		
		int y = 40;
		int x = areasCampo.getXMeio();
		
		g.setColor(Color.WHITE);
		g.fillRect(x-40, 7, 40, 40);
		g.fillRect(x+7, 7, 40, 40);
		
		g.setColor(Color.BLACK);
		g.drawString(golsEsquerda+"", x-30, y);
		g.drawString(golsDireita+"", x+17, y);
		
		super.desenha();
	}

}
