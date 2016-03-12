package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
Desarrolladores: Roberto Martinez Roman, Javier García Roque e Irvin Emmanuel Trujillo Díaz.
Descripción: Clase donde se ponen las especificaciones del texto
Profesor: Roberto Martinez Román.
*/

public class Texto
{
    private BitmapFont font;
    private float posicionX,posicionY;


    public Texto(float posicionx, float posiciony) {
        font = new BitmapFont(Gdx.files.internal("naftalene.fnt"));
        font.setColor(Color.CORAL);

        this.posicionX = posicionx;
        this.posicionY = posiciony;
    }

    public void mostrarMensaje(SpriteBatch batch, String mensaje) { //se recibe la posicion donde se desea mostrar el texto, el batch y el mensaje
        GlyphLayout glyp = new GlyphLayout();
        glyp.setText(font, mensaje);
        float anchoTexto = glyp.width;
        font.draw(batch,glyp,this.posicionX-anchoTexto/2,this.posicionY);
    }

    public void modificarCoordenadas(float x,float y){
        this.posicionX = x;
        this.posicionY = y;
    }

    public float getX(){
        return this.posicionX;
    }

    public float getY(){
        return this.posicionY;
    }
}
