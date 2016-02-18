package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
Desarrollador: Luis Fernando
Descripcion: Clase con el objetivo de ser un Objeto para los elementos que aparecen y desaparecen en el juego (scrolles, vidas, pociones)
Profesor: Roberto Martinez Román.
*/
public class ObjetosJuego {
    private Sprite sprite;
    private Estado estado;

    public ObjetosJuego(Texture textura){
        sprite = new Sprite(textura);
        estado = Estado.ENMAPA; //Todos los elementos, van a estar en el mapa
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

    //Metodo que regresa TRUE para decir que el objeto esta en el mapa y FALSE para decir que desapareció, default es en el mapa
    public boolean actualizar() {
        switch(estado){
            case ENMAPA:
                return true;
            case DESAPARECIDO:
                return false;
            default:
                return true;
        }
    }

    //Metodo que se implementa en la pantalla juego para quitar un elemento de la pantalla
    public void quitarElemento() {
        if(estado != Estado.DESAPARECIDO){
            estado =Estado.DESAPARECIDO;
        }
    }

    //Get estado
    public Estado getEstado() {
        return estado;
    }

    //Estados
    public  enum  Estado{
        ENMAPA,
        DESAPARECIDO
    }
}
