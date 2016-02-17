package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
Desarrollador: Luis Fernando
Descripcion: ....
Profesor: Roberto Martinez Román.
*/
public class Presentacion {

    private Sprite sprite;
    private Estado estado; //oculto, aparecido

    //Constructor
    public Presentacion(Texture textura){
        sprite = new Sprite(textura);
        estado = Estado.OCULTO;
    }
    //Render
    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
    //Posición
    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
    }
    //Tamaño
    public void setTamanio(float ancho,float alto){
        sprite.setSize(ancho, alto);
    }
    //GetSprite
    public Sprite getSprite() {
        return sprite;
    }

    //Metodo que regresa false para ocultar y true para aparecer, default es oculto
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

    //Get estado
    public Estado getEstado() {
        return estado;
    }

    //Metodo para aparecer presentaciones
    public void aparecer() {
        if(estado != Estado.APARECIDO){
            estado =Estado.APARECIDO;
        }
    }

    //Metodo para desaparecer presentaciones
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
