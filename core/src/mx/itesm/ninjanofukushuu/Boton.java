package mx.itesm.ninjanofukushuu;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


/*
Desarrollador: Irvin Emmanuel Trujillo Díaz
Descripción: Esta clase representa a un objeto boton...
Profesor: Roberto Martinez Román.
*/



public class Boton {

    private Sprite sprite;              // Imagen
    //private Rectangle rectColision;     // Rectangulo para verificar colisiones o touch


    public Boton(Texture textura) {
        sprite = new Sprite(textura);
        // El rectángulo de colisión siempre está 'sobre' el sprite
        //rectColision = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void render(SpriteBatch batch){ //Se ejecuta automaticamente.. dibuja el boton..
        sprite.draw(batch);
    }

    public float getY() {
        return sprite.getY();
    }

    public float getX() {
        return sprite.getX();
    }


    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
        //rectColision.setPosition(x,y);
    }

    public void setTamanio(float ancho,float alto){
        sprite.setSize(ancho, alto);
    }

    public void setAlfa(float alfa) {
        sprite.setAlpha(alfa);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean contiene(float x, float y) { //Para verificar el touch
        return  this.getX() <= x && this.getX() + sprite.getWidth() >= x && this.getY() <= y && this.getY() + sprite.getHeight() >= y;
    }


    /*public Rectangle getRectColision() {
        return rectColision;
    }*/





}
