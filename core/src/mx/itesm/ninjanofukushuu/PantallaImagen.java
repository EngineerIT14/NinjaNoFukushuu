package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;


/**

 PantallaImagen
 Autor: Irvin Emmanuel Trujillo Díaz
 Descripcion: Pantalla que muestra imagenes, botones para mover las imagenes y un boton para salir...

 **/
public class PantallaImagen {

    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;

    //Imagenes que son para mostrar el arte
    private Fondo imagenDisenioMapa,imagenDisenioPersonajes,imagenDisenioEnemigos,imagenDisenioFondoJuego;
    private Texture texturaDisenioMapa, texturaDisenioPersonajes, texturaDisenioEnemigos, textiraDisenioFondoJuego;

    //Imagenes que son para mostrar la hisotira del juego, son 5...
    private Fondo imagenHistoria1, imagenHistoria2, imagenHistoria3, imagenHistoria4, imagenHistoria5;
    private Texture texturaHistoria1, texturaHistoria2, texturaHistoria3, texturaHistoria4, texturaHistoria5;


    //Botones para que el usuario pueda mover las imagenes...
    private Boton btnDerecha;
    private Texture texturaBtnDerecha;

    private Boton btnIzquierda;
    private Texture texturaBtnIzquierda;



    private Boton btnAccion;
    //en caso del que el parametro mandado sea uno especial, el btn accion tendra una textura y comportamiento determinado.
    private Texture texturaRegresar;

    private Texture texturaGaleriaAguaBloqueada;

    private static final int anchoBoton = 180, altoBoton = 200; //anchoBoton1 = 480 , altoBoton1 = 160;
    //Efectos
    private Sound efectoHoja;

    public PantallaImagen(Principal principal) {
        this.principal = principal;

    }





}
