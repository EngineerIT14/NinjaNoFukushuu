package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
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
    private EstadoAtaque estadoAtaque;
    private float ataquex;
    private float velocidad;
    private float tiempoOculto; //Lo que lleva oculto
    private float maxTiempoOculto; //MAX (aleatorio)
    private int distancia;
    private float alto,ancho;

    public ObjetosJuego(Texture textura){
        sprite = new Sprite(textura);
        estado = Estado.ENMAPA; //Todos los elementos, van a estar en el mapa
        velocidad = 1f;
        ataquex = 0;
        estadoAtaque =EstadoAtaque.DISPONIBLE;
        distancia = 150;
        alto=30;
        ancho=30;
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
    public void actualizarAtaque(float x,float y){
        switch (estadoAtaque){
            case DERECHA:
                ataquex += velocidad;
                if (estadoAtaque==estadoAtaque.DERECHA) {
                    if (!sprite.isFlipX()) { //se voltea el ataque..
                        sprite.flip(true, false);
                    }
                }


                if (ataquex>=distancia){
                    ataquex=0;
                    estadoAtaque=EstadoAtaque.OCULTO;
                    tiempoOculto=0;
                    maxTiempoOculto = tiempoAzar();
                }
                sprite.setPosition(sprite.getX()+velocidad,sprite.getY());
                sprite.setSize(ancho,alto);
                break;
            case IZQUIERDA:
                if (estadoAtaque==estadoAtaque.IZQUIERDA) {
                    if (sprite.isFlipX()) { // se voltea el ataqye
                        sprite.flip(true,false);
                    }
                }


                ataquex -= velocidad;
                if (ataquex<-distancia){
                    ataquex=0;
                    estadoAtaque=EstadoAtaque.OCULTO;
                    tiempoOculto=0;
                    maxTiempoOculto = tiempoAzar();
                }
                sprite.setPosition(sprite.getX()-velocidad,sprite.getY());
                sprite.setSize(ancho,alto);
                break;
            case OCULTO:
                sprite.setPosition(x,y);
                sprite.setSize(0,0);
                tiempoOculto += Gdx.graphics.getDeltaTime();
                if(tiempoOculto>=maxTiempoOculto){
                    estadoAtaque = EstadoAtaque.DISPONIBLE;
                }
                break;
            case DISPONIBLE:
                return;

        }
    }
//
    private float tiempoAzar() {
        return (float)Math.random()*2+0.3f;
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

    //Get estadoAtaque
    public EstadoAtaque getEstadoAtaque(){
        return estadoAtaque;
    }

    public void mandarDerecha() {
        estadoAtaque=EstadoAtaque.DERECHA;
    }

    public void mandarIzquierda() {
        estadoAtaque = EstadoAtaque.IZQUIERDA;
    }

    public void ocultar() {
        if (estadoAtaque !=EstadoAtaque.OCULTO){
            estadoAtaque =EstadoAtaque.OCULTO;
            maxTiempoOculto=tiempoAzar();
            tiempoOculto = 0;
            ataquex=0;
        }
    }

    //Estados
    public  enum  Estado{
        ENMAPA,
        DESAPARECIDO
    }
    public  enum  EstadoAtaque{
        DERECHA,
        IZQUIERDA,
        OCULTO,
        DISPONIBLE
    }
}
