package grafico;

import java.awt.Rectangle;
import java.awt.Shape;

public class GeometriaUtil {
	public static double getDirecaoPara(double d, double e, double f, double g) {
		float angulo = (float) Math.toDegrees(Math.atan2(g - e, f - d));
		if (angulo < 0)
			angulo += 360;
		return angulo;
	}
	
	public static Rectangle getSubArea(Shape retangulo, int divisorx, int divisory, int offsetx, int offsety){
		double w = retangulo.getBounds().getWidth() / divisorx;
		double h = retangulo.getBounds().getHeight() / divisory;
		double x = retangulo.getBounds().getX() + (w * offsetx);
		double y = retangulo.getBounds().getY() + (h * offsety);
		Rectangle subRetangulo = new Rectangle((int)x,(int)y,(int)w,(int)h);
		return subRetangulo;
	}
}
