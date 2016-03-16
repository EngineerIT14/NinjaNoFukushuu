package mx.itesm.ninjanofukushuu;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Descripción: Pantala intermedia entre el menú y el juego
 * Profesor: Roberto Martinez Román
 * Autores: Javier García e Irvin Emmanuel Trujillo Díaz
 */
public class PantallaCargando implements Screen
{
    private Principal plataforma;

    // La cámara y vista principal
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;

    // Imagen cargando
    private Texture texturaCargando;
    private Sprite spriteCargando;
    //Fondo de la textura  (la base blanca)
    private Texture texturaFondoCargando;
    private Sprite spriteFondoCargando;

    //Textura cargando...
    private  Texture texturaLetrasLoading;
    private Sprite spriteLetrasLoading;

    //Banderas para determinar si los botones de galeriae estan desbloqueados
    private boolean banderaArteTierra = true, banderaArteAgua = false, banderaArteFuego = false;





    private AssetManager assetManager;  // Asset manager principal

    private int pantallaCargar; //aqui se va almacenar un numero que indicara que pantalla va cargar
    private boolean banderaMusicaFondo; //Para saber que en la clase pantallaMenu al cargarse, que bandera se manda y no se reprodusca la cancion de fondo más de 1 vez si esta ya puesta..
                                /*
                                * 1: elementos juego
                                * 2: elementos instrucciones
                                * 3: elementos galeria de arte
                                * 4: elementos acerca de
                                * */

    public PantallaCargando(Integer pantalla,Principal plataforma, boolean banderaMusicaFondo) {
        this.plataforma = plataforma;
        this.pantallaCargar = pantalla;
        this.banderaMusicaFondo = banderaMusicaFondo;
        this.assetManager = plataforma.getAssetManager();
    }

    @Override
    public void show() {
        // Crea la cámara/vista
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();
        vista = new StretchViewport(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO, camara);
        batch = new SpriteBatch();

        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        // Clase utilizada para manejar los eventos de touch en la pantalla, la coloco porque se estaban dando problemas al aparecer la pantalla cargando, ya que se quedaban registrado los botones del menu principal y el usuario si presionaba algo en pantallla cargando crasheaba...
        //Se resetea con esta configuracion de procesadorEntradar();
        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        // Cargar recursos

        if(!this.banderaMusicaFondo) { //si la banderaMusica es falsa, lo que nos quiere decir es que por primera vez se carga la pantalla, entonces se esta iniciando la APP, por lo que debe de mostrar el logo del equipoRocket y del tec.. recordar que apartir de la pantalllaMenu es donde se manda true  como argumentos..
            assetManager.load("logoRocket.jpg", Texture.class);
            assetManager.finishLoading();
            texturaCargando = assetManager.get("logoRocket.jpg");
        }
        else{
            assetManager.load("fondoCargando.png", Texture.class);
            assetManager.load("cargando.png", Texture.class);
            assetManager.load("LOADING2.png",Texture.class);
            assetManager.finishLoading();
            texturaCargando = assetManager.get("cargando.png");
            this.texturaFondoCargando = assetManager.get("fondoCargando.png");
            this.texturaLetrasLoading = assetManager.get("LOADING2.png");


            spriteFondoCargando = new Sprite(this.texturaFondoCargando);
            spriteFondoCargando.setPosition(Principal.ANCHO_MUNDO / 2 - spriteFondoCargando.getWidth() / 2,
                    Principal.ALTO_MUNDO / 2 - spriteFondoCargando.getHeight() / 2);

            spriteLetrasLoading = new Sprite(this.texturaLetrasLoading);
            spriteLetrasLoading.setPosition(Principal.ANCHO_MUNDO / 2 - spriteLetrasLoading.getWidth() / 2,
                    Principal.ALTO_MUNDO / 2 - spriteLetrasLoading.getHeight() / 2 -300);

        }
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(Principal.ANCHO_MUNDO / 2 - spriteCargando.getWidth() / 2,
                                    Principal.ALTO_MUNDO / 2 - spriteCargando.getHeight() / 2);


        cargarRecursos();
    }

    // Carga los recursos a través del administrador de assets (siguiente pantalla)
    private void cargarRecursos() {
        // Carga los recursos de la siguiente pantalla
           /*       * 0: Menu principal.
                     * 1: elementos juego
                       * 2: elementos instrucciones
                            * 3: elementos galeria de arte
                                * 4: elementos acerca de
            */
        switch(pantallaCargar) {
            case 0: //Pantalla Menu
                    assetManager.load("NINJAH3.png", Texture.class);    // Cargar imagen
                    assetManager.load("FONDOMIL2.jpg", Texture.class);    // Cargar imagen
                    assetManager.load("botonPlay.png", Texture.class);    // Cargar imagen
                    assetManager.load("botonInstructions.png", Texture.class);    // Cargar imagen
                    assetManager.load("botonGallery.png", Texture.class);    // Cargar imagen
                    assetManager.load("botonAbout.png", Texture.class);    // Cargar imagen
                    assetManager.load("Title.png", Texture.class);    // Cargar imagen
                    assetManager.load("sonidoVentana.wav", Sound.class);    // Cargar sonido

                    break;

            case 1: //Pantalla Juego
                    assetManager.load("MapaDeTierraV2.tmx", TiledMap.class);  // Cargar info del mapa
                    assetManager.load("marioSprite.png", Texture.class);    // Cargar imagen
                    // Texturas de los botones
                    assetManager.load("derecha.png", Texture.class);
                    assetManager.load("izquierda.png", Texture.class);
                    assetManager.load("salto.png", Texture.class);

                    //Textura objetos
                    assetManager.load("scroll.png", Texture.class);
                    assetManager.load("pocion.png", Texture.class);
                    assetManager.load("llama1.png", Texture.class);

                    //Textura enemigo
                    assetManager.load("TierraE.png", Texture.class);
                    //Textura templo
                    assetManager.load("temploVerde.png", Texture.class);
                    //caritaVida
                    assetManager.load("life1.png", Texture.class);
                    //sonidos
                    assetManager.load("efectoSaltoHataku.wav",Sound.class);
                    assetManager.load("efectoVida.wav",Sound.class);
                    assetManager.load("efectoPergamino.wav",Sound.class);
                    assetManager.load("efectoDanio.wav",Sound.class);
                    break;
            case 2:
                    assetManager.load("N.jpg",Texture.class);
                    assetManager.load("return.png",Texture.class);
                    assetManager.load("sonidoVentana.wav", Sound.class);    // Cargar sonido
                    break;
            case 3:
                    assetManager.load("M.jpg", Texture.class);
                    assetManager.load("galeriaEarth.png",Texture.class);
                    assetManager.load("galeriaEarthLock.png",Texture.class);
                    assetManager.load("galeriaFire.png",Texture.class);
                    assetManager.load("galeriaFireLock.png",Texture.class);
                    assetManager.load("galeriaWater.png",Texture.class);
                    assetManager.load("galeriaWaterLock.png",Texture.class);

                    assetManager.load("return.png",Texture.class);
                    assetManager.load("sonidoVentana.wav", Sound.class);    // Cargar sonido
                    break;

            case 4: //Pantalla acerca de.
                    assetManager.load("Fondo.jpg",Texture.class);
                    assetManager.load("M.png",Texture.class);
                    assetManager.load("N.png", Texture.class);
                    assetManager.load("I.png",Texture.class);
                    assetManager.load("J.png", Texture.class);
                    assetManager.load("F.png", Texture.class);
                    assetManager.load("return.png",Texture.class);
                    assetManager.load("Irvi.png",Texture.class);
                    assetManager.load("Mare.png",Texture.class);
                    assetManager.load("Javo.png",Texture.class);
                    assetManager.load("Nuria.png",Texture.class);
                    assetManager.load("Fercho.png",Texture.class);
                    assetManager.load("sonidoVentana.wav",Sound.class);
                    break;
            case 5:
                    assetManager.load("SN.jpg",Texture.class);
                    assetManager.load("return.png",Texture.class);
                    assetManager.load("sonidoVentana.wav", Sound.class);
                    break;
            default: break;
        }
    }

    @Override
    public void render(float delta) {

        // Actualizar carga
        actualizar();

        // Dibujar
        borrarPantalla();

        //gira la imagen cargando cuando no sea la primer pantalla
        if(banderaMusicaFondo == true) //al iniciar la APP, se esta mandando como argumento falso ya que no se esta reproduciendo la musica antes.. por ende, cuando es true, estas cargando las otras pantallas, porm lo que debe de girar la imagen..
            spriteCargando.setRotation(spriteCargando.getRotation() -5);

        batch.setProjectionMatrix(camara.combined);

        // Entre begin-end dibujamos nuestros objetos en pantalla
        batch.begin();
        if(banderaMusicaFondo == true) { //al iniciar la APP, se esta mandando como argumento falso ya que no se esta reproduciendo la musica antes.. por ende, cuando es true, estas cargando las otras pantallas,
            spriteFondoCargando.draw(batch);
            spriteLetrasLoading.draw(batch);
        }
        spriteCargando.draw(batch);

        batch.end();
    }

    private void actualizar() {

        if (assetManager.update()) {// Terminó la carga, cambiar de pantalla
            switch(this.pantallaCargar){//dependiendo que se cargo, cambiamos la pantalla...
                case 0:
                    plataforma.setScreen(new PantallaMenu(this.plataforma,this.banderaMusicaFondo));
                    break;
                case 1:
                    plataforma.setScreen(new PantallaJuego(this.plataforma));
                    break;
                case 2:
                    plataforma.setScreen(new PantallaInstrucciones(this.plataforma));
                    break;
                case 3:
                    plataforma.setScreen(new PantallaGaleria(this.plataforma, banderaArteTierra,banderaArteAgua,banderaArteFuego));
                    break;
                case 4:
                    plataforma.setScreen(new PantallaAcerca(this.plataforma));
                    break;
                case 5:
                    plataforma.setScreen(new SeleccionDeNivel(this.plataforma));
                    break;
                default: break;
            }
        } else {
            // Aún no termina la carga de assets, leer el avance
            float avance = assetManager.getProgress()*100;
            Gdx.app.log("Cargando",avance+"%");
        }
    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0, 0, 0, 0);    // r, g, b, alpha  //color NEGRO
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }


    @Override
    public void resize(int width, int height) {
        vista.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    //Clase utilizada para manejar los eventos de touch en la pantalla, la coloco porque se estaban dando problemas al aparecer la pantalla cargando, ya que se quedaban registrado los botones del menu principal y el usuario si presionaba algo en pantallla cargando crasheaba...
    //Se resetea con esta configuracion el touch...
    public class ProcesadorEntrada extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return true;    // Indica que ya procesó el evento
        }
        //Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return true;    // Indica que ya procesó el evento
        }

    }



    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        plataforma.dispose();
        texturaCargando.dispose();
        batch.dispose();
        // Los assets de PantallaJuego se liberan en el método dispose de PantallaJuego
    }
}
