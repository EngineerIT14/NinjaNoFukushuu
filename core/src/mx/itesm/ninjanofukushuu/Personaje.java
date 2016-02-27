package mx.itesm.ninjanofukushuu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**

 Descripcion:  Representa el personaje en pantalla
 Profesor: Roberto Martinez Román.
 */
public class Personaje
{
    public static final float VELOCIDAD_Y = -2f;   // Velocidad de caída
    private static final float VELOCIDAD_X = 2;
    private Sprite sprite;  // Sprite cuando no se mueve

    // Animación
    private Animation animacion;    // Caminando
    private float timerAnimacion;

    // Estados del personaje
    private Estado estado;

    /*
    Constructor del personaje, recibe una imagen con varios frames, (ver imagen marioSprite.png)
     */
    public Personaje(Texture textura) {
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);
        // La divide en frames de 16x32 (ver marioSprite.png)
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(16,32);
        // Crea la animación con tiempo de 0.25 segundos entre frames.
        animacion = new Animation(0.25f,texturaPersonaje[0][3], texturaPersonaje[0][2], texturaPersonaje[0][1] );  //La matriz [0][3] continene un sprite de mario con el pie adelante y así.. va caminando
        // Animación infinita
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][0]);    // quieto sprite del mario quieto..
        estado = Estado.INICIANDO;
    }

    // Dibuja el personaje
    public void render(SpriteBatch batch) {

        // Dibuja el personaje dependiendo del estado
        switch (estado) {
            case INICIANDO:
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                // Incrementa el timer para calcular el frame que se dibuja
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Obtiene el frame que se debe mostrar (de acuerdo al timer)
                TextureRegion region = animacion.getKeyFrame(timerAnimacion);
                // Dibuja el frame en las coordenadas del sprite
                batch.draw(region, sprite.getX(), sprite.getY());
                break;
            case QUIETO:
                sprite.draw(batch); // Dibuja el sprite
                break;
        }

    }

    // Actualiza el sprite, de acuerdo al estado
    public void actualizar() {
        float nuevaX = sprite.getX();
        switch (estado) {
            case INICIANDO: // Caída inicial
                sprite.setY(sprite.getY() + VELOCIDAD_Y);
                break;
            case MOV_DERECHA:
                // Prueba que no salga del mundo
                nuevaX += VELOCIDAD_X;
                if (nuevaX<=PantallaJuego.ANCHO_MAPA-sprite.getWidth()) {
                    sprite.setX(nuevaX);
                }
                break;
            case MOV_IZQUIERDA:
                // Prueba que no salga del mundo
                nuevaX -= VELOCIDAD_X;
                if (nuevaX>=0) {
                    sprite.setX(nuevaX);
                }
                break;
        }
    }

    // Accesor de la variable sprite
    public Sprite getSprite() {
        return sprite;
    }

    // Accesores para la posición
    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public void setPosicion(float x, int y) {
        sprite.setPosition(x,y);
    }

    // Accesor del estado
    public Estado getEstado() {
        return estado;
    }

    // Modificador del estado
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public enum Estado {
        INICIANDO,
        QUIETO,
        MOV_IZQUIERDA,
        MOV_DERECHA,
        SALTANDO,
        CAYENDO
    }
}
