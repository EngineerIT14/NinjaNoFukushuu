package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
Desarrollador: Irvin Emmanuel Trujillo Díaz
Descripción: Esta clase representa el objeto fondo..
Profesor: Roberto Martinez Román.
*/

public class Fondo {

    private Sprite sprite;

    public Fondo(Texture textura){

        sprite = new Sprite(textura);

    }

    public void render(SpriteBatch batch){ //Se ejecuta automaticamente..

        sprite.draw(batch);
    }

    public float getAncho(){
        return sprite.getWidth();
    }

    public float getAlto(){
        return sprite.getHeight();
    }

    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
        //rectColision.setPosition(x,y);
    }




    public void setTamanio(float ancho,float alto){

        sprite.setSize(ancho, alto);
    }

    public void cambiarFondo(Texture textura){
        sprite.setTexture(textura);
    }

    //GetSprite
    public Sprite getSprite() {
        return sprite;
    }



}
