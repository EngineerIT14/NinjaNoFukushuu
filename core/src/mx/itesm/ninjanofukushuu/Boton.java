package mx.itesm.ninjanofukushuu;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/*
Desarrollador: Irvin Emmanuel Trujillo Díaz
Descripción: Esta clase representa a un objeto boton...
Profesor: Roberto Martinez Román.
*/

//RegresandoElCambio adiosMundo

public class Boton {

    private Sprite sprite;

    public Boton(Texture textura){ //Constructor.
        sprite = new Sprite(textura);
        sprite.setAlpha(0.9f); //La transparencia del boton, con f le pones que sea un float.
    }

    public void render(SpriteBatch batch){ //Se ejecuta automaticamente..
        sprite.draw(batch);
    }

    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void setTamanio(float ancho,float alto){
        sprite.setSize(ancho,alto);
    }



    public Sprite getSprite() {
        return sprite;
    }

}
