package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*

Desarrollador: Irvin Emmanuel Trujillo Díaz
Descripción: Pantalla que aparece cuando el usuario pierde en el videojuego, dandole las opciones para volver a jugar ese nivel desde el principio o regresar al menu principal.
Profesor: Roberto Martinez Román.

* */
public class PantallaGameOver implements Screen {

    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private static SpriteBatch batch;
    //static = 1 copia

    private int nivelDondePerdi; //Para saber en caso de que el usuairo quier volver a jugar, que nivel cargar...

    //static = 1 copia en memoria
    private static Fondo fondo; /* Es la imagen de gameOver*/
    private static Texture texturaFondo;

    //Botones para que el usuario pueda tener opciones
    private static Boton btnContinue;
    private static Texture texturaBtnContinue;

    private static Boton btnMenu;
    private static Texture texturaBtnMenu;


    //Efectos
    private static  Sound efectoClick, efectoMuerteNinja;

    //constructor
    public PantallaGameOver(Principal principal, int nivel) {
        this.principal = principal;
        this.nivelDondePerdi = nivel;


    }


    @Override
    public void show() {
        //Crear camara
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();

        vista = new StretchViewport(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO, camara);
        this.crearObjetos();
        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());
        efectoMuerteNinja.play(PantallaMenu.volumen); //Se rerpoduce sonido

        //DESACTIVANDO BOTONES PARA IMPEDIR QUE INTERRUMPA AL USUARIO EN EL JUEGO.
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);

    }

    //crea los objetos de textura y audio
    private void crearObjetos() {
        AssetManager assetManager = principal.getAssetManager();   // Referencia al assetManager
        //Textura de fondos y botones
        texturaFondo = assetManager.get("seleccionNivel/recursosPerdiste/gameOver.png");
        texturaBtnMenu = assetManager.get("seleccionNivel/recursosPausa/menu.png");
        texturaBtnContinue = assetManager.get("seleccionNivel/recursosPerdiste/continue.png");

        //Crear fondo
        fondo = new Fondo(texturaFondo);

        fondo.getSprite().setCenter(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2);
        fondo.getSprite().setOrigin(1500 / 2, 1500 / 2);

        //botones
        btnContinue = new Boton(texturaBtnContinue);
        btnContinue.setPosicion(Principal.ANCHO_MUNDO / 2-160,  Principal.ALTO_MUNDO / 2-200);

        btnMenu = new Boton(texturaBtnMenu);
        btnMenu.setPosicion(Principal.ANCHO_MUNDO / 2-160, Principal.ALTO_MUNDO / 2-350);

        efectoClick = assetManager.get("sonidoVentana.wav");
        efectoMuerteNinja =  assetManager.get("seleccionNivel/recursosPerdiste/muerteNinja.wav");
        //Batch
        batch = new SpriteBatch();
    }


    @Override
    public void render(float delta) {
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);

        //DIBUJAR
        batch.begin();
        fondo.render(batch);
        btnMenu.render(batch);
        btnContinue.render(batch);
        batch.end();
    }


    //Clase utilizada para manejar los eventos de touch en la pantalla
    public class ProcesadorEntrada extends InputAdapter {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla virtual
        private float anchoBoton = btnContinue.getAncho();
        private float altoBton = btnContinue.getAlto();

        private boolean banderaBotonContinue = false, banderaBotonMenu = false;
        /*
        Se ejecuta cuando el usuario pone un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse*/

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            if (btnContinue.contiene(x, y)) {
                btnContinue.setAlfa(.5f);
                btnContinue.setTamanio(btnContinue.getAncho(), btnContinue.getAlto() - 2); //Lo hago más pequeño
                this.banderaBotonContinue = true;

            }

            if (btnMenu.contiene(x, y)) {
                btnMenu.setAlfa(.5f);
                btnMenu.setTamanio(btnMenu.getAncho(), btnMenu.getAlto() - 2); //Lo hago más pequeño
                this.banderaBotonMenu = true;
            }


            return true;    // Indica que ya procesó el evento
        }

        //Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            // Preguntar si las coordenadas son de cierto lugar de donde se quito el dedo

            //en la pantalla cargando determinara que cargar... se manda el numero correspondiente para saber que se va cargar en esa clase..
            if (btnContinue.contiene(x, y) && this.banderaBotonContinue) {
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido
                principal.setScreen(new PantallaJuego(principal,nivelDondePerdi));   //Nos regresa al nivel donde se perdio---
            }
            if (btnMenu.contiene(x, y) && this.banderaBotonMenu) {
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido
                principal.setScreen(new PantallaMenu(principal, true));  //nos regresa al menu principal.
            }
            else { //entonces el usuario despego el dedo de la pantalla en otra parte que no sean los botones...
                // se le quita la transparencia y se regresa a su tamaño original
                if(banderaBotonContinue) {
                    banderaBotonContinue = false;
                    btnContinue.setAlfa(1);
                    btnContinue.setTamanio(this.anchoBoton, this.altoBton); //tamaño orginal
                }
                else if(banderaBotonMenu) {
                    banderaBotonMenu = false;
                    btnMenu.setAlfa(1);
                    btnMenu.setTamanio(this.anchoBoton, this.altoBton);
                }
            }
            return true;    // Indica que ya procesó el evento
        }

        private void transformarCoordenadas(int screenX, int screenY) {
            // Transformar las coordenadas de la pantalla física a la cámara
            coordenadas.set(screenX, screenY, 0);
            camara.unproject(coordenadas); //camaraHUD es para los botones
            // Obtiene las coordenadas relativas a la pantalla virtual
            x = coordenadas.x;
            y = coordenadas.y;
        }
    }


    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //Eliminar basura
        principal.dispose();
        batch.dispose();
        texturaBtnContinue.dispose();
        texturaBtnMenu.dispose();
        texturaFondo.dispose();
    }
}




