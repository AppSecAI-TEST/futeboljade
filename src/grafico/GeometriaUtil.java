package grafico;

public class GeometriaUtil {
	public static float getDirecaoPara(float x1, float y1, float x2, float y2) {
		float angulo = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
		if (angulo < 0)
			angulo += 360;
		return angulo;
	}
}
