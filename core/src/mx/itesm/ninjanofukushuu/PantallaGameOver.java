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
    private SpriteBatch batch;

    private int nivelDondePerdi; //Para saber en caso de que el usuairo quier volver a jugar, que nivel cargar...

    private Fondo fondo; /* Es la imagen de gameOver*/
    private Texture texturaFondo;

    //Botones para que el usuario pueda tener opciones
    private Boton btnContinue;
    private Texture texturaBtnContinue;

    private Boton btnMenu;
    private Texture texturaBtnMenu;


    //Efectos
    private Sound efectoClick;

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




        //Batch

        this.batch = new SpriteBatch();
    }

    //crea los objetos de textura y audio
    private void crearObjetos() {
        AssetManager assetManager = principal.getAssetManager();   // Referencia al assetManager
        //Textura de fondos y botones
        this.texturaFondo = assetManager.get("seleccionNivel/recursosPerdiste/gameOver.png");
        this.texturaBtnMenu = assetManager.get("seleccionNivel/recursosPausa/menu.png");
        this.texturaBtnContinue = assetManager.get("seleccionNivel/recursosPerdiste/continue.png");

        //Crear fondo
        this.fondo = new Fondo(texturaFondo);

        this.fondo.getSprite().setCenter(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2);
        this.fondo.getSprite().setOrigin(1500 / 2, 1500 / 2);

        //botones
        this.btnContinue = new Boton(this.texturaBtnContinue);
        this.btnContinue.setPosicion(Principal.ANCHO_MUNDO / 2-160,  Principal.ALTO_MUNDO / 2-200);

        this.btnMenu = new Boton(this.texturaBtnMenu);
        this.btnMenu.setPosicion(Principal.ANCHO_MUNDO / 2-160, Principal.ALTO_MUNDO / 2-350);

        this.efectoClick = assetManager.get("sonidoVentana.wav");


    }


    @Override
    public void render(float delta) {
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.batch.setProjectionMatrix(camara.combined);

        //DIBUJAR
        this.batch.begin();
        this.fondo.render(batch);
        this.btnMenu.render(batch);
        this.btnContinue.render(batch);
        this.batch.end();
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
                btnContinue.setTamanio(btnContinue.getAncho() , btnContinue.getAlto() - 2); //Lo hago más pequeño
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
                banderaBotonContinue = false;
                banderaBotonMenu = false;
                btnContinue.setAlfa(1);
                btnMenu.setAlfa(1);

                btnContinue.setTamanio(this.anchoBoton, this.altoBton); //tamaño orginal
                btnMenu.setTamanio(this.anchoBoton, this.altoBton);
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
        this.texturaBtnContinue.dispose();
        this.texturaBtnMenu.dispose();
        texturaFondo.dispose();
    }
}




