
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
Desarrolladores: Javier García Roque.
Descripción: Clase donde se ponen las especificaciones del texto
Profesor: Roberto Martinez Román.
*/

public class Texto
{
    private BitmapFont font;

    public Texto() {
        font = new BitmapFont();
        font.setColor(Color.YELLOW);
        font.getData().scale(1.5f);
    }

    public void mostrarMensaje(SpriteBatch batch, String mensaje, float x, float y) {
        GlyphLayout glyp = new GlyphLayout();
        glyp.setText(font, mensaje);
        float anchoTexto = glyp.width;
        font.draw(batch,glyp,x-anchoTexto/2,y);
    }
}
