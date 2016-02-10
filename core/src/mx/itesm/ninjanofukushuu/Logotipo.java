package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
Desarrollador: Irvin Emmanuel Trujillo Díaz
Descripción: Esta clase representa el objeto logotipo, pueden ser el el título del juego (logo).
Profesor: Roberto Martinez Román.
*/
public class Logotipo {

        private Sprite sprite;

        public Logotipo(Texture textura){
            sprite = new Sprite(textura);
        }

        public void setPosicion(float x, float y) {
            sprite.setPosition(x, y);
        }

        public void setTamanio(float ancho,float alto){

            sprite.setSize(ancho,alto);
        }


    public void render(SpriteBatch batch){ //Se ejecuta automaticamente..
            sprite.draw(batch);
        }


}
