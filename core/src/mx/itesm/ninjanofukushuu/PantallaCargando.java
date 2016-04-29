package mx.itesm.ninjanofukushuu;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
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
 * Autores: Irvin Emmanuel Trujillo Díaz, Luis Fernando, Javier García
 */
public class PantallaCargando implements Screen
{
    private Principal plataforma;

    // La cámara y vista principal
    private OrthographicCamera camara;
    private Viewport vista;
    private static SpriteBatch batch;

    //static = 1 copia.
    // Imagen cargando
    private static Texture texturaCargando;
    private static Sprite spriteCargando;
    //Fondo de la textura  (la base blanca)
    private static Texture texturaFondoCargando;
    private static Sprite spriteFondoCargando;

    //Textura cargando...
    private static Texture texturaLetrasLoading;
    private static Sprite spriteLetrasLoading;

    //Banderas para determinar si los botones de galeriae estan desbloqueados, son publicos ya que se modifican en la pantalla juego..
    public static boolean banderaArteTierra = false, banderaArteAgua = false, banderaArteFuego = false; //Son public static para que sean manipuladas en otras clases..

    //Banderas para determinar si los niveles estan desbloqueados
    public static boolean banderaNivelAguaDesbloqueado = false, banderaNivelFuegoDesbloqueado = false; //Son public static para que sean manipuladas en otras clases..


    //Progreso de la partida
    public static Preferences partidaGuardada; //guardar el progreso del usuario..., es publica para que se pueda ir modificando en las otras clases...

    private AssetManager assetManager;  // Asset manager principal

    private int pantallaCargar; //aqui se va almacenar un numero que indicara que pantalla va cargar
    /*
                          * 1: elementos juego (seleccion de nivel..)
                          * 2: elementos instrucciones
                          * 3: elementos galeria de arte
                          * 4: elementos acerca de
                          * 5: recursos nivel tierra
                          * 6: recursos nivel agua
                          * 7: recursos nivel fuego
                          * */
    private boolean banderaMusicaFondo; //Para saber que en la clase pantallaMenu al cargarse, se debe de mostrar el logo del tec o no (al principio) tambien para evitar que se reproduzca nuevamente la canción..


    public PantallaCargando(Integer pantalla,Principal plataforma, boolean banderaMusicaFondo) {
        this.plataforma = plataforma;
        this.pantallaCargar = pantalla;
        this.banderaMusicaFondo = banderaMusicaFondo;
        this.assetManager = plataforma.getAssetManager();
        PantallaCargando.partidaGuardada =  Gdx.app.getPreferences("partidaGuardada"); //Para crear (en caso de que no este) o cargar la partida...
        //por default banderas estan en falso..                         //lave        boolean
        PantallaCargando.banderaArteTierra = partidaGuardada.getBoolean("arteTierra", false);
        PantallaCargando.banderaArteAgua = partidaGuardada.getBoolean("arteAgua", false);
        PantallaCargando.banderaArteFuego = partidaGuardada.getBoolean("arteFuego", false);
        PantallaCargando.banderaNivelAguaDesbloqueado = partidaGuardada.getBoolean("nivelAgua",false);
        PantallaCargando.banderaNivelFuegoDesbloqueado = partidaGuardada.getBoolean("nivelFuego",false);


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

        //DESACTIVANDO BOTONES PARA IMPEDIR QUE INTERRUMPA AL USUARIO EN EL JUEGO.
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);

        cargarImagenCargando();     // Cargar recursos para mostrar en la pantalla cargando...
        cargarRecursos(); //Cargando recursos de determinada pantalla


    }

    private void cargarImagenCargando() {
        if(!this.banderaMusicaFondo) { //si la banderaMusica es falsa, lo que nos quiere decir es que por primera vez se carga la pantalla, entonces se esta iniciando la APP, por lo que debe de mostrar el logo del tec.. recordar que apartir de la pantalllaMenu es donde se manda true  como argumentos..
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
            texturaFondoCargando = assetManager.get("fondoCargando.png");
            texturaLetrasLoading = assetManager.get("LOADING2.png");


            spriteFondoCargando = new Sprite(texturaFondoCargando);
            spriteFondoCargando.setPosition(Principal.ANCHO_MUNDO / 2 - spriteFondoCargando.getWidth() / 2,
                    Principal.ALTO_MUNDO / 2 - spriteFondoCargando.getHeight() / 2);

            spriteLetrasLoading = new Sprite(texturaLetrasLoading);
            spriteLetrasLoading.setPosition(Principal.ANCHO_MUNDO / 2 - spriteLetrasLoading.getWidth() / 2,
                    Principal.ALTO_MUNDO / 2 - spriteLetrasLoading.getHeight() / 2 -300);

        }
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(Principal.ANCHO_MUNDO / 2 - spriteCargando.getWidth() / 2,
                Principal.ALTO_MUNDO / 2 - spriteCargando.getHeight() / 2);

    }

    // Carga los recursos a través del administrador de assets (siguiente pantalla)
    private void cargarRecursos() {
        // Carga los recursos de la siguiente pantalla
        /*       * 0: Menu principal.
                     * 1: elementos juego (recursos)
                       * 2: elementos instrucciones
                            * 3: elementos galeria de arte
                                * 4: elementos acerca de
                                    *5: pantalla de seleccion de nivel..   */
        switch(pantallaCargar) {
            case 0: //Pantalla Menu
                    assetManager.load("NINJAH3.png", Texture.class);    // Cargar imagen
                    assetManager.load("fondoMenu.jpg", Texture.class);    // Cargar imagen
                    assetManager.load("botonPlay.png", Texture.class);    // Cargar imagen
                    assetManager.load("botonInstructions.png", Texture.class);    // Cargar imagen
                    assetManager.load("botonGallery.png", Texture.class);    // Cargar imagen
                    assetManager.load("botonAbout.png", Texture.class);    // Cargar imagen
                    assetManager.load("Title.png", Texture.class);    // Cargar imagen
                    assetManager.load("bocina.png",Texture.class);
                    assetManager.load("mute.png",Texture.class);
                    assetManager.load("sonidoVentana.wav", Sound.class);    // Cargar sonido
                    assetManager.load("seleccionNivel/sonidosGameplay/puertaTemplo.wav",Sound.class);
                    break;

            case 1: //Pantalla de seleccion de nivel...
                    assetManager.load("seleccionNivel/fondoSeleccionNivel.jpg",Texture.class);
                    assetManager.load("seleccionNivel/galeriaEarth.png",Texture.class);
                    assetManager.load("seleccionNivel/galeriaEarthLock.png",Texture.class);
                    assetManager.load("seleccionNivel/galeriaFire.png",Texture.class);
                    assetManager.load("seleccionNivel/galeriaFireLock.png",Texture.class);
                    assetManager.load("seleccionNivel/galeriaWater.png",Texture.class);
                    assetManager.load("seleccionNivel/galeriaWaterLock.png",Texture.class);

                    assetManager.load("seleccionNivel/galeriaWaterLock.png",Texture.class);

                    assetManager.load("Play.png",Texture.class);
                    assetManager.load("return.png",Texture.class);
                    assetManager.load("sonidoVentana.wav", Sound.class);
                    assetManager.load("bloqueado.wav",Sound.class);


                    break;
            case 2: // pantalla de instrucciones
                    assetManager.load("imagenesInstrucciones/fondoInstrucciones.jpg",Texture.class);
                    assetManager.load("return.png",Texture.class);
                    assetManager.load("sonidoVentana.wav", Sound.class);    // Cargar sonido
                    break;
            case 3: //pantalla de galeria de arte
                    assetManager.load("imagenesGaleriaArte/fondoGaleriaArte.jpg", Texture.class);
                    assetManager.load("imagenesGaleriaArte/galeriaEarth.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/galeriaEarthLock.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/galeriaFire.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/galeriaFireLock.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/galeriaWater.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/galeriaWaterLock.png",Texture.class);

                    //tambien se cargan las imagenes del arte de los niveles...
                    // Texturas de los botones  para mover las imagenes al ver la galeria de arte..
                    assetManager.load("seleccionNivel/botonesFlechas/derechaImagenes.png",Texture.class);
                    assetManager.load("seleccionNivel/botonesFlechas/izquierdaImagenes.png",Texture.class);

                //ArteNivelTierra
                    assetManager.load("imagenesGaleriaArte/arteTierra/arteMenu1.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteTierra/hatakuArte.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteTierra/mapaArte1.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteTierra/mapaArte2.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteTierra/samuraiArte.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteTierra/fondoArteTierra.png",Texture.class);

                    //ArteNivelAgua
                    assetManager.load("imagenesGaleriaArte/arteAgua/AguaE.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteAgua/arteFondo.jpg",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteAgua/arteMenu2.jpg",Texture.class);
                    assetManager.load("seleccionNivel/fondoSeleccionNivel.jpg",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteAgua/pocionArte.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteAgua/vidaArte2.png",Texture.class);



                    //ArteNivelFuego
                    assetManager.load("imagenesGaleriaArte/arteFuego/vidaArte3.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteFuego/disenioNivelFuego.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteFuego/Enemigo3.png",Texture.class);
                    assetManager.load("seleccionNivel/recursosNivelFuego/fondofuego.png",Texture.class);
                    assetManager.load("imagenesGaleriaArte/arteFuego/fondofuego2.jpg",Texture.class);
                    assetManager.load("imagenesAcercaDe/Fondo.jpg",Texture.class);


                    assetManager.load("return.png",Texture.class); //el return esta en la raiz...
                    //sonidos en raiz
                    assetManager.load("sonidoVentana.wav", Sound.class);    // Cargar sonido
                    assetManager.load("seleccionNivel/sonidosGameplay/efectoPergamino.wav",Sound.class); //para cuando cambias de hoja...
                    assetManager.load("bloqueado.wav",Sound.class);
                    break;

            case 4: //Pantalla acerca de.
                    assetManager.load("imagenesAcercaDe/Fondo.jpg",Texture.class);
                    assetManager.load("imagenesAcercaDe/M.png",Texture.class);
                    assetManager.load("imagenesAcercaDe/N.png", Texture.class);
                    assetManager.load("imagenesAcercaDe/I.png",Texture.class);
                    assetManager.load("imagenesAcercaDe/J.png", Texture.class);
                    assetManager.load("imagenesAcercaDe/F.png", Texture.class);
                    assetManager.load("return.png",Texture.class); // esta en la raiz..
                    assetManager.load("imagenesAcercaDe/Irvi.png",Texture.class);
                    assetManager.load("imagenesAcercaDe/Mare.png",Texture.class);
                    assetManager.load("imagenesAcercaDe/Javo.png",Texture.class);
                    assetManager.load("imagenesAcercaDe/Nuria.png",Texture.class);
                    assetManager.load("imagenesAcercaDe/Fercho.png",Texture.class);
                    assetManager.load("sonidoVentana.wav",Sound.class); // esta en la raiz...
                    break;
            case 5:
                    // se cargan recursos del juego..
                    //nivel tierra
                    assetManager.load("seleccionNivel/recursosNivelTierra/MapaDeTierraV2.tmx", TiledMap.class);  // Cargar info del mapa
                    //assetManager.load("seleccionNivel/recursosNivelTierra/marioSprite.png", Texture.class);    // Cargar imagen
                    assetManager.load("seleccionNivel/recursosNivelTierra/Hataku.png", Texture.class);    // Cargar imagen
                    assetManager.load("seleccionNivel/recursosNivelTierra/fondoTierra.jpg",Texture.class); //Fondo del mapa
                    // Texturas de los botones tanto para el gampeplay como para mover las imagenes al ver la historia..

                    assetManager.load("seleccionNivel/botonesFlechas/salto.png", Texture.class);

                    //botones para mover imagenes y personaje..
                    assetManager.load("seleccionNivel/botonesFlechas/derechaImagenes.png",Texture.class);
                    assetManager.load("seleccionNivel/botonesFlechas/izquierdaImagenes.png",Texture.class);

                    //Textura objetos
                    assetManager.load("seleccionNivel/items/scroll.png", Texture.class);
                    assetManager.load("seleccionNivel/items/pocion.png", Texture.class);
                    assetManager.load("seleccionNivel/items/llama1.png", Texture.class);

                    //Textura enemigo
                    assetManager.load("seleccionNivel/recursosNivelTierra/TierraE.png", Texture.class);
                    //Textura templo
                    assetManager.load("seleccionNivel/recursosNivelTierra/temploVerde.png", Texture.class);
                    //caritaVida
                    assetManager.load("seleccionNivel/recursosNivelTierra/life1.png", Texture.class);
                    //sonidos
                    assetManager.load("seleccionNivel/sonidosGameplay/efectoSaltoHataku.wav",Sound.class);
                    assetManager.load("seleccionNivel/sonidosGameplay/efectoVida.wav",Sound.class);
                    assetManager.load("seleccionNivel/sonidosGameplay/efectoPergamino.wav",Sound.class);
                    assetManager.load("seleccionNivel/sonidosGameplay/efectoDanio.wav",Sound.class);
                    assetManager.load("seleccionNivel/sonidosGameplay/puertaTemplo.wav",Sound.class);
                    assetManager.load("seleccionNivel/recursosPerdiste/muerteNinja.wav",Sound.class);
                    assetManager.load("seleccionNivel/recursosWinner/win.wav",Sound.class);


                //se cargan los recursos para mostrar la historia...
                    assetManager.load("botonPlay.png", Texture.class); // boton play...
                    //son 5 imagenes para explicar la historia...
                    assetManager.load("seleccionNivel/recursosNivelTierra/historiaTierra1.jpg",Texture.class);
                    assetManager.load("seleccionNivel/recursosNivelTierra/historiaTierra2.jpg",Texture.class);
                    assetManager.load("seleccionNivel/recursosNivelTierra/historiaTierra3.jpg",Texture.class);
                    assetManager.load("seleccionNivel/recursosNivelTierra/historiaTierra4.jpg",Texture.class);
                    assetManager.load("seleccionNivel/recursosNivelTierra/historiaTierra5.jpg",Texture.class);


                    //Para el gameOver
                    assetManager.load("seleccionNivel/recursosPerdiste/gameOver.png",Texture.class);
                    assetManager.load("seleccionNivel/recursosPerdiste/continue.png",Texture.class);

                    //Para el winner
                    assetManager.load("seleccionNivel/recursosWinner/winner.jpg", Texture.class);

                    //Para la pausa
                    assetManager.load("seleccionNivel/recursosPausa/menu.png",Texture.class);
                    assetManager.load("seleccionNivel/recursosPausa/Pausa.png",Texture.class);
                    assetManager.load("seleccionNivel/recursosPausa/fondoPausa.png",Texture.class);

                    break;
            case 6:
                // se cargan recursos del juego..
                //nivel Agua
                assetManager.load("seleccionNivel/recursosNivelAgua/MapaDeAgua2.tmx", TiledMap.class);  // Cargar info del mapa
                //assetManager.load("seleccionNivel/recursosNivelTierra/marioSprite.png", Texture.class);    // Cargar imagen
                assetManager.load("seleccionNivel/recursosNivelAgua/ninjita.png", Texture.class);    // Cargar imagen
                assetManager.load("seleccionNivel/recursosNivelAgua/fondoAgua.png",Texture.class); //Fondo del mapa
                // Texturas de los botones tanto para el gampeplay como para mover las imagenes al ver la historia..

                assetManager.load("seleccionNivel/botonesFlechas/salto.png", Texture.class);

                //botones para mover imagenes y personaje..
                assetManager.load("seleccionNivel/botonesFlechas/derechaImagenes.png",Texture.class);
                assetManager.load("seleccionNivel/botonesFlechas/izquierdaImagenes.png",Texture.class);

                //Textura objetos
                assetManager.load("seleccionNivel/items/scroll.png", Texture.class);
                assetManager.load("seleccionNivel/items/pocion.png", Texture.class);
                assetManager.load("seleccionNivel/items/ataque2.png", Texture.class);

                //Textura enemigo
                assetManager.load("seleccionNivel/recursosNivelAgua/aguaEnemigo.png", Texture.class);
                //Textura templo
                assetManager.load("seleccionNivel/recursosNivelTierra/temploVerde.png", Texture.class);
                //caritaVida
                assetManager.load("seleccionNivel/recursosNivelAgua/life2.png", Texture.class);
                //sonidos
                assetManager.load("seleccionNivel/sonidosGameplay/efectoSaltoHataku.wav",Sound.class);
                assetManager.load("seleccionNivel/sonidosGameplay/efectoVida.wav",Sound.class);
                assetManager.load("seleccionNivel/sonidosGameplay/efectoPergamino.wav",Sound.class);
                assetManager.load("seleccionNivel/sonidosGameplay/efectoDanio.wav",Sound.class);
                assetManager.load("seleccionNivel/sonidosGameplay/puertaTemplo.wav",Sound.class);
                assetManager.load("seleccionNivel/recursosPerdiste/muerteNinja.wav",Sound.class);
                assetManager.load("seleccionNivel/recursosWinner/win.wav",Sound.class);


                //se cargan los recursos para mostrar la historia...
                assetManager.load("botonPlay.png", Texture.class); // boton play...
                //son 5 imagenes para explicar la historia...
                assetManager.load("seleccionNivel/recursosNivelAgua/historiaAgua1.png",Texture.class);
                assetManager.load("seleccionNivel/recursosNivelAgua/historiaAgua2.jpg",Texture.class);
                assetManager.load("seleccionNivel/recursosNivelAgua/historiaAgua3.png",Texture.class);
                assetManager.load("seleccionNivel/recursosNivelAgua/historiaAgua4.jpg",Texture.class);
                assetManager.load("seleccionNivel/recursosNivelAgua/historiaAgua5.jpg",Texture.class);

                //Para el gameOver
                assetManager.load("seleccionNivel/recursosPerdiste/gameOver.png",Texture.class);
                assetManager.load("seleccionNivel/recursosPerdiste/continue.png",Texture.class);

                //Para el winner
                assetManager.load("seleccionNivel/recursosWinner/winner.jpg",Texture.class);

                //Para la pausa
                assetManager.load("seleccionNivel/recursosPausa/menu.png",Texture.class);
                assetManager.load("seleccionNivel/recursosPausa/Pausa.png",Texture.class);
                assetManager.load("seleccionNivel/recursosPausa/fondoPausa.png",Texture.class);


                break;
            case 7:
                // se cargan recursos del juego..
                //nivel Fuego
                assetManager.load("seleccionNivel/recursosNivelFuego/mapaDeFuego.tmx", TiledMap.class);  // Cargar info del mapa
                //assetManager.load("seleccionNivel/recursosNivelTierra/marioSprite.png", Texture.class);    // Cargar imagen
                assetManager.load("seleccionNivel/recursosNivelFuego/ninjaS.png", Texture.class);    // Cargar imagen
                assetManager.load("seleccionNivel/recursosNivelFuego/fondofuego.png",Texture.class); //Fondo del mapa
                // Texturas de los botones tanto para el gampeplay como para mover las imagenes al ver la historia..

                assetManager.load("seleccionNivel/botonesFlechas/salto.png", Texture.class);

                //botones para mover imagenes y personaje..
                assetManager.load("seleccionNivel/botonesFlechas/derechaImagenes.png",Texture.class);
                assetManager.load("seleccionNivel/botonesFlechas/izquierdaImagenes.png",Texture.class);

                //Textura objetos
                assetManager.load("seleccionNivel/items/scroll.png", Texture.class);
                assetManager.load("seleccionNivel/items/pocion.png", Texture.class);
                assetManager.load("seleccionNivel/items/ataque3.png", Texture.class);

                //Textura enemigo
                assetManager.load("seleccionNivel/recursosNivelFuego/Enemigo3.png", Texture.class);
                //Textura templo
                assetManager.load("seleccionNivel/recursosNivelTierra/temploVerde.png", Texture.class);
                //caritaVida
                assetManager.load("seleccionNivel/recursosNivelFuego/lifeFuego.png", Texture.class);
                //sonidos
                assetManager.load("seleccionNivel/sonidosGameplay/efectoSaltoHataku.wav",Sound.class);
                assetManager.load("seleccionNivel/sonidosGameplay/efectoVida.wav",Sound.class);
                assetManager.load("seleccionNivel/sonidosGameplay/efectoPergamino.wav",Sound.class);
                assetManager.load("seleccionNivel/sonidosGameplay/efectoDanio.wav",Sound.class);
                assetManager.load("seleccionNivel/sonidosGameplay/puertaTemplo.wav",Sound.class);
                assetManager.load("seleccionNivel/recursosPerdiste/muerteNinja.wav",Sound.class);
                assetManager.load("seleccionNivel/recursosWinner/win.wav",Sound.class);


                //se cargan los recursos para mostrar la historia...
                assetManager.load("botonPlay.png", Texture.class); // boton play...
                //son 5 imagenes para explicar la historia...
                assetManager.load("seleccionNivel/recursosNivelFuego/historiaFuego1.png",Texture.class);
                assetManager.load("seleccionNivel/recursosNivelFuego/historiaFuego2.jpg",Texture.class);
                assetManager.load("seleccionNivel/recursosNivelFuego/historiaFuego3.jpg",Texture.class);
                assetManager.load("seleccionNivel/recursosNivelFuego/historiaFuego4.png",Texture.class);
                assetManager.load("seleccionNivel/recursosNivelFuego/historiaFuego5.jpg",Texture.class);

                //Para el gameOver
                assetManager.load("seleccionNivel/recursosPerdiste/gameOver.png",Texture.class);
                assetManager.load("seleccionNivel/recursosPerdiste/continue.png",Texture.class);

                //Para el winner
                assetManager.load("seleccionNivel/recursosWinner/winner.jpg",Texture.class);

                //Para la pausa
                assetManager.load("seleccionNivel/recursosPausa/menu.png",Texture.class);
                assetManager.load("seleccionNivel/recursosPausa/Pausa.png",Texture.class);
                assetManager.load("seleccionNivel/recursosPausa/fondoPausa.png",Texture.class);

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
        if(banderaMusicaFondo) //Significa que esta cargando una pantalla, entonces la imagen es la que gira..
            spriteCargando.setRotation(spriteCargando.getRotation() -5);

        batch.setProjectionMatrix(camara.combined);

        // Entre begin-end dibujamos nuestros objetos en pantalla
        batch.begin();
        if(banderaMusicaFondo) { //al iniciar la APP, la bandera es falsa porque no se esta escuchando ninguna cancion, entonces es el logo del TEC, NO gira.
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
                    plataforma.setScreen(new SeleccionDeNivel(this.plataforma));

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
                    plataforma.setScreen(new PantallaImagen(this.plataforma,4)); //nivel de tierra... se  va a mostrar las imagenes de la historia..
                    break;
                case 6:
                   //LO QUE SE HA COMENTADO HA SIDO PORQUE AUN NO ESTAN LISTAS LAS IMAGENES DE HISTORIA AGUA, POR AHORA TE LLEVA A JUGAR..
                    plataforma.setScreen(new PantallaJuego(plataforma,2)); //nivel de agua..
                   // plataforma.setScreen(new PantallaImagen(this.plataforma,5)); //nivel de agua... se  va a mostrar las imagenes de la historia..
                    break;
                case 7:
                    //LO QUE SE HA COMENTADO HA SIDO PORQUE AUN NO ESTAN LISTAS LAS IMAGENES DE HISTORIA FUEGO, POR AHORA TE LLEVA A JUGAR..
                    plataforma.setScreen(new PantallaJuego(plataforma, 3)); //nivel de agua..
                    //plataforma.setScreen(new PantallaImagen(this.plataforma,6)); //nivel de fuego... se  va a mostrar las imagenes de la historia..
                    break;
                default:
                    plataforma.setScreen(new PantallaMenu(this.plataforma,this.banderaMusicaFondo));
                    break;
            }
        }
        /*else {
            // Aún no termina la carga de assets, leer el avance
            float avance = assetManager.getProgress()*100;
            Gdx.app.log("Cargando",avance+"%");
        }*/
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
        texturaLetrasLoading.dispose();
        texturaFondoCargando.dispose();
        texturaCargando.dispose();
        batch.dispose();
        // Los assets de PantallaJuego se liberan en el método dispose de PantallaJuego
    }
}
