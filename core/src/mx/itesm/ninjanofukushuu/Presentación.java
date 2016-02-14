package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Atem on 13/02/2016.
 */
public class Presentación {

    private Sprite sprite;
    private Estado estado; //oculto, aparecidp

    public Presentación(Texture textura){
        sprite = new Sprite(textura);
        estado = Estado.OCULTO;
    }
    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
    }
    public void setTamanio(float ancho,float alto){
        sprite.setSize(ancho, alto);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean actualizar() {
        switch(estado){
            case OCULTO:
                return false;
            case APARECIDO:
                return true;
            default:
                return false;
        }
    }

    public Estado getEstado() {
        return estado;
    }

    public void aparecer() {
        if(estado != Estado.APARECIDO){
            estado =Estado.APARECIDO;
        }
    }

    public void desaparecer() {
        if(estado != Estado.OCULTO){
            estado = Estado.OCULTO;
        }
    }

    //Estados
    public  enum  Estado{
        OCULTO,
        APARECIDO
    }
}
