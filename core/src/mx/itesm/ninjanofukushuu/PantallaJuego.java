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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import com.badlogic.gdx.Input.Keys;


/*
Desarrolladores: Irvin Emmanuel Trujillo Díaz, Javier García Roque y Luis Fernando
Descripción: Esta clase es la encargada de mostrar el juego y su comportamiento...
Profesor: Roberto Martinez Román.
*/
public class PantallaJuego implements Screen{

    public static final float ANCHO_MAPA = 1280;   // Como se creó en Tiled

    public static final int TAM_CELDA = 16;
    // Referencia al objeto de tipo Game (tiene setScreen para cambiar de pantalla).
    private Principal plataforma;
    //La camara y vista principal
    private OrthographicCamera camara;
    private Viewport vista;
    //Objeto para dibujar en la pantalla
    private SpriteBatch batch;
    //Mapa
    private TiledMap mapa; //Infomracion del mapa en memoria
    private OrthogonalTiledMapRenderer rendererMapa; //Objeto para dibujar el mapa
    private  Fondo fondo; //Imagen de fondo
    private Texture texturaFondo; //Textura fondo
    //Personaje Principal
    private Personaje hataku; //hataku en nuestro personaje principal
    private Texture texturaHataku;       // Aquí cargamos la imagen, en el caso de la clase, se cargo hatakuSprite.png con varios frames
    // HUD. Los componentes en la pantalla que no se mueven
    private OrthographicCamera camaraHUD;   // Cámara fija
    // Botones izquierda/derecha
    private Texture texturaBtnIzquierda;
    private Boton btnIzquierda;
    private Texture texturaBtnDerecha;
    private Boton btnDerecha;
    private boolean banderaBotonTouchApretado = false; //es para que se pueda probar con el teclado y los botones del juego al mismo tiempo..
    // Botón saltar
    private Texture texturaSalto;
    private Boton btnSalto;

    //tamañoBotones
    private static int TAMANIO_BOTON = 89;


    //ITEMS

    //Scrolls/Pergamino
    private ArrayList<ObjetosJuego> scroll;
    private Texture texturaScroll;

    //Enemigos
    private ArrayList<ObjetosJuego> enemigoN1;
    private Texture texturaEN1;
    private ArrayList<ObjetosJuego> enemigoN2;

    //Templo
    private ArrayList<ObjetosJuego> templos; //son en total 3 templos
    private Texture texturaTemplo;

    //Pociones
    private ArrayList<ObjetosJuego> pociones;
    private Texture texturaPocion;

    //HUD, MARCADORES DE VIDA Y PERGAMINOS..

    //VIDAS QUE SE MUESTRAN EN EL HUD.

    //Tambien nos va servir de marcador...
    private ArrayList<ObjetosJuego> vidas;
    private Texture texturaVidas;


    //Ataque
    private  ArrayList<ObjetosJuego> ataques;
    private Texture texturaAtaque;

    //Marcadores
    private int marcadorPergaminos;
    private Texto textoMarcadorPergaminos; //Texto para mostrar el marcador de vidas y marcador de pergaminos.
    private Texto textoMarcadorVidas;

    // Estados del juego
    private EstadosJuego estadoJuego;

    //Efectos del juego
    private Sound efectoSaltoHataku, efectoTomarVida, efectoTomarPergamino, efectoDanio, efectoPuertaTemplo;




    //Variable para indicar si numero de nivel y hacer el cambio en el numero de vidas, ya que hataku tiene su armadura completa en el nivel 4 y las vidas deben aumentar a 5, esta variable es nuestra bandera...
    private boolean flag = false;
    private int  numeroNivel; //es estatica ya que solo se quiere una copia de etsa variable y es modificada en seleccion de  nivel...
    private int ataqueFlag;




    /*//Estado para la suma del marcador
    private Estado estado;*/

    public PantallaJuego(Principal plataforma,int nivel) {
        this.plataforma = plataforma;
        numeroNivel=nivel;
    }

    @Override
    public void show() {
        // Crea la cámara/vista
        camara = new OrthographicCamera(Principal.ANCHO_CAMARA, Principal.ALTO_CAMARA);
        camara.position.set(Principal.ANCHO_CAMARA / 2, Principal.ALTO_CAMARA / 2, 0);
        camara.update();
        vista = new StretchViewport(Principal.ANCHO_CAMARA, Principal.ALTO_CAMARA, camara);

        batch = new SpriteBatch();

        // Cámara para HUD
        camaraHUD = new OrthographicCamera(Principal.ANCHO_CAMARA, Principal.ALTO_CAMARA);
        camaraHUD.position.set(Principal.ANCHO_CAMARA / 2, Principal.ALTO_CAMARA / 2, 0);
        camaraHUD.update();

        this.crearObjetos();

        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        estadoJuego = EstadosJuego.JUGANDO;
    }


    //los recursos se cargan en la pantallaCargando

    private void crearObjetos() {
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager
        if (this.numeroNivel == 1){ //en el nivel tierra{
            // Carga el mapa en memoria
            mapa = assetManager.get("seleccionNivel/recursosNivelTierra/MapaDeTierraV2.tmx");
        //mapa.getLayers().get(0).setVisible(false);
        // Crear el objeto que dibujará el mapa
        rendererMapa = new OrthogonalTiledMapRenderer(mapa, batch);
        rendererMapa.setView(camara);
        // Cargar frames
        //texturaHataku = assetManager.get("seleccionNivel/recursosNivelTierra/marioSprite.png");
        texturaHataku = assetManager.get("seleccionNivel/recursosNivelTierra/ninjita.png");
        // Crear el personaje
        hataku = new Personaje(texturaHataku);
        // Posición inicial del personaje
        if (this.numeroNivel == 1) //en el nivel tierra
            hataku.getSprite().setPosition(30, 100);

        //Textura fondo
        this.texturaFondo = assetManager.get("seleccionNivel/recursosNivelTierra/fondoTierra.jpg");
        fondo = new Fondo(texturaFondo);

        //Textura Objetos que estan en la pantalla
        this.texturaScroll = assetManager.get("seleccionNivel/items/scroll.png");
        this.texturaPocion = assetManager.get("seleccionNivel/items/pocion.png");
        this.texturaEN1 = assetManager.get("seleccionNivel/recursosNivelTierra/TierraE.png");
        this.texturaAtaque = assetManager.get("seleccionNivel/items/llama1.png");
        this.texturaTemplo = assetManager.get("seleccionNivel/recursosNivelTierra/temploVerde.png");

        //****************************************************************//
        //nota: se debe cosniderar que la imagen de vidas va cambiar cuando el ninja obtenga una parte de la armadura, recomiendo usar un switch y usar una bandera (boolean) cuando se pase el nivel y deppendiendo de la bandera cargar el archivo de imagenn correspondiente
        //Por ahora no lo implemento ya que estamos trabajando en el primer nivel.
        this.texturaVidas = assetManager.get("seleccionNivel/recursosNivelTierra/life1.png");


        //Musica y efectos se obtienen y se ajusta el volumen
        this.efectoSaltoHataku = assetManager.get("seleccionNivel/sonidosGameplay/efectoSaltoHataku.wav");
        this.efectoTomarVida = assetManager.get("seleccionNivel/sonidosGameplay/efectoVida.wav");
        this.efectoTomarPergamino = assetManager.get("seleccionNivel/sonidosGameplay/efectoPergamino.wav");
        this.efectoDanio = assetManager.get("seleccionNivel/sonidosGameplay/efectoDanio.wav");
        this.efectoPuertaTemplo = assetManager.get("seleccionNivel/sonidosGameplay/puertaTemplo.wav");


        // Crear los botones
        texturaBtnIzquierda = assetManager.get("seleccionNivel/botonesFlechas/izquierdaImagenes.png");
        btnIzquierda = new Boton(texturaBtnIzquierda);
        btnIzquierda.setPosicion(TAM_CELDA * 2, TAM_CELDA / 5);
        btnIzquierda.setAlfa(0.7f); // Un poco de transparencia
        btnIzquierda.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON);

        texturaBtnDerecha = assetManager.get("seleccionNivel/botonesFlechas/derechaImagenes.png");
        btnDerecha = new Boton(texturaBtnDerecha);
        btnDerecha.setPosicion(TAM_CELDA * 8, TAM_CELDA / 5);
        btnDerecha.setAlfa(0.7f); // Un poco de transparencia
        btnDerecha.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON);

        texturaSalto = assetManager.get("seleccionNivel/botonesFlechas/salto.png"); //boton para saltar... carga su imagen
        btnSalto = new Boton(texturaSalto);
        btnSalto.setPosicion(Principal.ANCHO_CAMARA - 6 * TAM_CELDA, 100 + TAM_CELDA);
        btnSalto.setAlfa(0.7f);
        btnSalto.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON);


        //Se crean objetos que son textos que se muestran en el HUD.
        this.textoMarcadorVidas = new Texto(0.1f * Principal.ANCHO_CAMARA + 30, Principal.ALTO_CAMARA * 0.96f);
        this.textoMarcadorPergaminos = new Texto(50 + 0.70f * Principal.ANCHO_CAMARA + 26, Principal.ALTO_CAMARA * 0.96f); //mandamos la posicion que queremos por default.


        //Lista scrolles: en todos los niveles solo hay 3 scroll
        this.scroll = new ArrayList<ObjetosJuego>(3);
        for (int i = 0; i < 3; i++) {
            ObjetosJuego nuevo = new ObjetosJuego(this.texturaScroll);
            nuevo.setTamanio(12, 35);
            this.scroll.add(nuevo);
        }

        //Posiciones pergamino nivel tierra
        this.scroll.get(0).setPosicion(50, 340); //pergamino de en medio...
        this.scroll.get(1).setPosicion(745, 32); //pergamino de hasta abajo
        this.scroll.get(2).setPosicion(627, 76); //pergamino que está en precipicio

        //Pociones: En todos los niveles solo hay 2 pociones.
        this.pociones = new ArrayList<ObjetosJuego>(2);
        for (int i = 0; i < 2; i++) {
            ObjetosJuego nuevo = new ObjetosJuego(this.texturaPocion);
            nuevo.setTamanio(30, 40);
            this.pociones.add(nuevo);
        }

        //Se colocan las pociones en el lugar correspondiente,
        this.pociones.get(0).setPosicion(940, Principal.ALTO_MUNDO / 2 - 40);
        this.pociones.get(1).setPosicion(255, 270);

        //Enemigos: 5 enemigos en el primer nivel
        this.enemigoN1 = new ArrayList<ObjetosJuego>(5);
        for (int i = 0; i < 5; i++) {
            ObjetosJuego nuevo = new ObjetosJuego(this.texturaEN1);
            nuevo.setTamanio(60, 90);
            this.enemigoN1.add(nuevo);
        }

        //Se colocan los enemigos en su lugar correspondiente, en el nivel de TIERRA
        this.enemigoN1.get(0).setPosicion(900, 519); //samurai parte superior
        this.enemigoN1.get(1).setPosicion(470, 280);  //Samurai centro
        this.enemigoN1.get(2).setPosicion(790, 295); //Samurai Escalon
        this.enemigoN1.get(3).setPosicion(960, 120); //Samurai escalon
        this.enemigoN1.get(4).setPosicion(570, 503); //Samurai parte superior

        //Colocar los ataque en su posicion
        this.ataques = new ArrayList<ObjetosJuego>(5);
        for (ObjetosJuego enemigo : enemigoN1) {
            ObjetosJuego nuevo = new ObjetosJuego(this.texturaAtaque);
            //nuevo.setTamanio(30, 30);
            this.ataques.add(nuevo);
            nuevo.setPosicion(enemigo.getSprite().getX() + 15, enemigo.getSprite().getY() + 25);
        }

        //Aqui se piensa poner un switch evaluando una variable de nivel,  de eso va dependar donde se va colocar el templo
        //templos, son 3.
        this.templos = new ArrayList<ObjetosJuego>(3);
        for (int i = 0; i < 3; i++) {
            ObjetosJuego nuevo = new ObjetosJuego(this.texturaTemplo);
            nuevo.setTamanio(60, 90);
            this.templos.add(nuevo);
        }

        this.templos.get(0).setPosicion(230, 510); //temploTierra


        //Objetos que representan las vidas, son las caras del ninja que estan en el HUD

        //Vidas: El numero de vidas cambia cuando se tiene la armadura completa, por lo que. al llegar all ultimo de nivel se debe de moficiar la variable globa de vidas, aumentando la vidas del persoonaje a 5.
        if (this.numeroNivel == 4) { //El nivel 4 es donde hataku enfrenta al jefe final, por lo que, sus vidas aumentan...
            this.flag = true;
        } else {
            this.flag = false; //no estoy en el nivel 4.
        }

        //Si es el nivel 4 se deben de poner 5 vidas
        if (this.flag) {
            this.vidas = new ArrayList<ObjetosJuego>(5);
            for (int i = 0; i < 5; i++) {
                ObjetosJuego nuevo = new ObjetosJuego(this.texturaVidas);
                //nuevo.setTamanio(80,80);
                this.vidas.add(nuevo);
            }

            //Se colocan las vidas en el lugar correspondiente
           /* this.vidas.get(0).setPosicion(1000, Principal.ALTO_MUNDO / 2);
            this.vidas.get(0).setPosicion(1000, Principal.ALTO_MUNDO / 2);
            this.vidas.get(0).setPosicion(1000, Principal.ALTO_MUNDO / 2);
            this.vidas.get(0).setPosicion(1000, Principal.ALTO_MUNDO / 2);
            this.vidas.get(0).setPosicion(1000, Principal.ALTO_MUNDO / 2);*/

        } else { //entonces no estoy en el nivel 4, se deben de poner 3 vidas.
            this.vidas = new ArrayList<ObjetosJuego>(3);
            for (int i = 0; i < 3; i++) {
                ObjetosJuego nuevo = new ObjetosJuego(this.texturaVidas);
                //nuevo.setTamanio(70,70); //Irvin ya ajusto el tamaño de las vidas en photoshop..
                this.vidas.add(nuevo);
            }
            this.vidas.get(0).setPosicion(this.textoMarcadorVidas.getX() + 95, this.textoMarcadorVidas.getY() - 45);
            this.vidas.get(1).setPosicion(this.textoMarcadorVidas.getX() + 165, this.textoMarcadorVidas.getY() - 45);
            this.vidas.get(2).setPosicion(this.textoMarcadorVidas.getX() + 235, this.textoMarcadorVidas.getY() - 45);
        }
        }
        else if (this.numeroNivel == 2){
            // Carga el mapa en memoria
            mapa = assetManager.get("seleccionNivel/recursosNivelAgua/MapaDeAgua.tmx");
            //mapa.getLayers().get(0).setVisible(false);
            // Crear el objeto que dibujará el mapa
            rendererMapa = new OrthogonalTiledMapRenderer(mapa, batch);
            rendererMapa.setView(camara);
            // Cargar frames
            texturaHataku = assetManager.get("seleccionNivel/recursosNivelAgua/ninjita.png");
            // Crear el personaje
            hataku = new Personaje(texturaHataku);
            // Posición inicial del personaje
            hataku.getSprite().setPosition(20, 20);

            //Textura fondo
            //this.texturaFondo = assetManager.get("seleccionNivel/recursosNivelTierra/fondoTierra.jpg");
            //fondo = new Fondo(texturaFondo);

            //Textura Objetos que estan en la pantalla
            this.texturaScroll = assetManager.get("seleccionNivel/items/scroll.png");
            this.texturaPocion = assetManager.get("seleccionNivel/items/pocion.png");
            this.texturaEN1 = assetManager.get("seleccionNivel/recursosNivelAgua/AguaE.png");
            this.texturaAtaque = assetManager.get("seleccionNivel/items/ataque2.png");
            this.texturaTemplo = assetManager.get("seleccionNivel/recursosNivelAgua/temploAzul.png");

            //****************************************************************//
            //nota: se debe cosniderar que la imagen de vidas va cambiar cuando el ninja obtenga una parte de la armadura, recomiendo usar un switch y usar una bandera (boolean) cuando se pase el nivel y deppendiendo de la bandera cargar el archivo de imagenn correspondiente
            //Por ahora no lo implemento ya que estamos trabajando en el primer nivel.
            this.texturaVidas = assetManager.get("seleccionNivel/recursosNivelAgua/life2.png");


            //Musica y efectos se obtienen y se ajusta el volumen
            this.efectoSaltoHataku = assetManager.get("seleccionNivel/sonidosGameplay/efectoSaltoHataku.wav");
            this.efectoTomarVida = assetManager.get("seleccionNivel/sonidosGameplay/efectoVida.wav");
            this.efectoTomarPergamino = assetManager.get("seleccionNivel/sonidosGameplay/efectoPergamino.wav");
            this.efectoDanio = assetManager.get("seleccionNivel/sonidosGameplay/efectoDanio.wav");
            this.efectoPuertaTemplo = assetManager.get("seleccionNivel/sonidosGameplay/puertaTemplo.wav");


            // Crear los botones
            texturaBtnIzquierda = assetManager.get("seleccionNivel/botonesFlechas/izquierdaImagenes.png");
            btnIzquierda = new Boton(texturaBtnIzquierda);
            btnIzquierda.setPosicion(TAM_CELDA * 2, TAM_CELDA / 5);
            btnIzquierda.setAlfa(0.7f); // Un poco de transparencia
            btnIzquierda.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON);

            texturaBtnDerecha = assetManager.get("seleccionNivel/botonesFlechas/derechaImagenes.png");
            btnDerecha = new Boton(texturaBtnDerecha);
            btnDerecha.setPosicion(TAM_CELDA * 8, TAM_CELDA / 5);
            btnDerecha.setAlfa(0.7f); // Un poco de transparencia
            btnDerecha.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON);

            texturaSalto = assetManager.get("seleccionNivel/botonesFlechas/salto.png"); //boton para saltar... carga su imagen
            btnSalto = new Boton(texturaSalto);
            btnSalto.setPosicion(Principal.ANCHO_CAMARA - 6 * TAM_CELDA, 100 + TAM_CELDA);
            btnSalto.setAlfa(0.7f);
            btnSalto.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON);


            //Se crean objetos que son textos que se muestran en el HUD.
            this.textoMarcadorVidas = new Texto(0.1f * Principal.ANCHO_CAMARA + 30, Principal.ALTO_CAMARA * 0.96f);
            this.textoMarcadorPergaminos = new Texto(50 + 0.70f * Principal.ANCHO_CAMARA + 26, Principal.ALTO_CAMARA * 0.96f); //mandamos la posicion que queremos por default.


            //Lista scrolles: en todos los niveles solo hay 3 scroll
            this.scroll = new ArrayList<ObjetosJuego>(3);
            for (int i = 0; i < 3; i++) {
                ObjetosJuego nuevo = new ObjetosJuego(this.texturaScroll);
                nuevo.setTamanio(12, 35);
                this.scroll.add(nuevo);
            }

            //Posiciones pergamino nivel agua
            this.scroll.get(0).setPosicion(20, 1040); //pergamino derecha arriba.
            this.scroll.get(1).setPosicion(680, 1230); //pergamino de hasta arriba izquierda
            this.scroll.get(2).setPosicion(680, 76); //pergamino abajo

            //Pociones: En todos los niveles solo hay 2 pociones.
            this.pociones = new ArrayList<ObjetosJuego>(2);
            for (int i = 0; i < 2; i++) {
                ObjetosJuego nuevo = new ObjetosJuego(this.texturaPocion);
                nuevo.setTamanio(30, 40);
                this.pociones.add(nuevo);
            }

            //Se colocan las pociones en el lugar correspondiente,
            this.pociones.get(0).setPosicion(400, 630);
            this.pociones.get(1).setPosicion(400, 990);

            //Enemigos: 4 enemigos en el segundo nivel
            this.enemigoN1 = new ArrayList<ObjetosJuego>(4);
            for (int i = 0; i < 4; i++) {
                ObjetosJuego nuevo = new ObjetosJuego(this.texturaEN1);
                nuevo.setTamanio(60, 90);
                this.enemigoN1.add(nuevo);
            }

            //Se colocan los enemigos en su lugar correspondiente, en el nivel de Agua
            this.enemigoN1.get(0).setPosicion(20, 565); //centro izquierda
            this.enemigoN1.get(1).setPosicion(560, 678);  //centro derecha
            this.enemigoN1.get(2).setPosicion(530, 805); //plataforma derecha
            this.enemigoN1.get(3).setPosicion(290, 805); //Plataforma Izquierda

            //Enemigos especiales
            this.enemigoN2 = new ArrayList<ObjetosJuego>(4);
            for (int i = 0; i < 4; i++) {
                ObjetosJuego nuevo = new ObjetosJuego(texturaEN1);
                nuevo.setTamanio(60,90);
                this.enemigoN2.add(nuevo);
            }

            this.enemigoN2.get(0).setPosicion(330,165);
            this.enemigoN2.get(1).setPosicion(590,965);
            this.enemigoN2.get(2).setPosicion(180,965);
            this.enemigoN2.get(3).setPosicion(380,1126);

            //Colocar los ataque en su posicion
            this.ataques = new ArrayList<ObjetosJuego>(5);
            for (ObjetosJuego enemigo : enemigoN1) {
                ObjetosJuego nuevo = new ObjetosJuego(this.texturaAtaque);
                nuevo.setTamanio(30, 30);
                this.ataques.add(nuevo);
                nuevo.setPosicion(enemigo.getSprite().getX() + 15, enemigo.getSprite().getY() + 25);
            }

            //Aqui se piensa poner un switch evaluando una variable de nivel,  de eso va dependar donde se va colocar el templo
            //templos, son 3.
            this.templos = new ArrayList<ObjetosJuego>(3);
            for (int i = 0; i < 3; i++) {
                ObjetosJuego nuevo = new ObjetosJuego(this.texturaTemplo);
                nuevo.setTamanio(60, 90);
                this.templos.add(nuevo);
            }

            this.templos.get(0).setPosicion(20, 1160); //temploAgua

            this.vidas = new ArrayList<ObjetosJuego>(3);
            for (int i = 0; i < 3; i++) {
                ObjetosJuego nuevo = new ObjetosJuego(this.texturaVidas);
                nuevo.setTamanio(70,70); //Irvin ya ajusto el tamaño de las vidas en photoshop..
                this.vidas.add(nuevo);
            }
            this.vidas.get(0).setPosicion(this.textoMarcadorVidas.getX() + 95, this.textoMarcadorVidas.getY() - 45);
            this.vidas.get(1).setPosicion(this.textoMarcadorVidas.getX() + 165, this.textoMarcadorVidas.getY() - 45);
            this.vidas.get(2).setPosicion(this.textoMarcadorVidas.getX() + 235, this.textoMarcadorVidas.getY() - 45);

            }
    }

    /*
    Dibuja TODOS los elementos del juego en la pantalla.
    Este método se está ejecutando muchas veces por segundo.
     */
    @Override
    public void render(float delta) { // delta es el tiempo entre frames (Gdx.graphics.getDeltaTime())
        // Leer entrada
        //Gdx.app.log("",hataku.getY()+"");
        // Actualizar objetos en la pantalla
        moverPersonaje();
        if(numeroNivel==2){
            actualizarCamaraAgua();
        }
        else{
            actualizarCamara(); // Mover la cámara para que siga al personaje
        }
        // Dibujar
        borrarPantalla();

        // Para verificar si el usuario ya tomo los 3 pergaminos y liberar el boton de galeria de arte...
        liberarArte();

        //Para verificar si el usuario ya perdio...
        perderJuego();

        //Para verificar si el usuario ya gano...
        ganarJuego();


        //MOVER PERSONAJES CON TECLADO (ESTO ES UTIL PARA LAS PRUEBAS,
        // SE PIENSA COMENTAR AL ENTREGAR EL PROYECTO)
        //
        controlarPersonajeConTeclado();





            //DIBUJAR OBJETOS COMPONENTES DEL JUEGO
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        //fondo.render(batch);
        batch.end();
        rendererMapa.setView(camara);
        rendererMapa.render();  // Dibuja el mapa
        recogerObjeto();
        // Entre begin-end dibujamos nuestros objetos en pantalla
        batch.begin();
        hataku.render(batch);    // Dibuja el personaje
        //Dibujar scrolls
        for (ObjetosJuego scrolls : scroll) {
            if (scrolls.actualizar()) {
                scrolls.render(batch);
            }
        }
        //Dibujar pociones
        for (ObjetosJuego pocion : pociones) {
            if (pocion.actualizar())
                pocion.render(batch);
        }

        for (ObjetosJuego Enemigo : enemigoN1) {
            if (Enemigo.actualizar())
                Enemigo.render(batch);
        }
        if(numeroNivel>1){
            for(ObjetosJuego enemigo:enemigoN2){
                if(enemigo.actualizar())
                    enemigo.render(batch);
            }
        }
        ataqueFlag=0;
        //Dibujar ataques
        for (int i=0;i<ataques.size();i++){
            ObjetosJuego ataque=ataques.get(i);
            ObjetosJuego enemigo=enemigoN1.get(i);
            ataque.render(batch);
            atacarEnemigo(ataque, enemigo);
            ataqueFlag++;
        }

        this.templos.get(0).render(batch);//temploTierra


        batch.end();
        //Dibuja el HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        batch.begin();
        // Mostrar pergaminos
        this.textoMarcadorPergaminos.mostrarMensaje(batch, "Scrolls: " + this.marcadorPergaminos);
        // Mostrar vida
        this.textoMarcadorVidas.mostrarMensaje(batch, "Health: ");
        //Dibujar iconos vidas
        for (ObjetosJuego vida : this.vidas) {
            if (vida.actualizar()) {
                vida.render(batch);
            }
        }
        btnIzquierda.render(batch);
        btnDerecha.render(batch);
        btnSalto.render(batch);

        batch.end();

    }

    private void actualizarCamaraAgua() {
        float posX = hataku.getX();
        float posY = hataku.getY();
        // Si está en la parte 'media'
        if (posX>=Principal.ANCHO_CAMARA/2 && posX<=Principal.ALTO_MUNDO-Principal.ANCHO_CAMARA/2) {
            // El personaje define el centro de la cámara
            camara.position.set((int)posX, camara.position.y, 0);
        } else if (posX>Principal.ALTO_MUNDO-Principal.ANCHO_CAMARA/2) {    // Si está en la última mitad
            // La cámara se queda media pantalla antes del fin del mundo  :)
            camara.position.set(Principal.ALTO_MUNDO-Principal.ANCHO_CAMARA/2, camara.position.y, 0);
        }
        if (posY>=Principal.ALTO_CAMARA/2 && posY<= ANCHO_MAPA-Principal.ALTO_CAMARA/2) {
            // El personaje define el centro de la cámara
            camara.position.set(camara.position.x, (int)posY, 0);
        } else if (posY>=ANCHO_MAPA-Principal.ALTO_CAMARA/2) {    // Si está en la última mitad
            // La cámara se queda media pantalla antes del fin del mundo  :)
            camara.position.set(camara.position.x, ANCHO_MAPA-Principal.ALTO_CAMARA/2, 0);
        }
        camara.update();
    }

    private void controlarPersonajeConTeclado() {

        if(Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) {
            hataku.setEstado(Personaje.EstadoMovimiento.MOV_IZQUIERDA);
            this.banderaBotonTouchApretado = false;
            if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                if (Personaje.EstadoSalto.EN_PISO == hataku.getEstadoSalto()) //Para que solamente suene una vez el sonido de salto
                    efectoSaltoHataku.play(PantallaMenu.volumen);
                hataku.saltar();
            }
        }

        else if(Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)){
            hataku.setEstado(Personaje.EstadoMovimiento.MOV_DERECHA);
            this.banderaBotonTouchApretado = false;
            if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                if (Personaje.EstadoSalto.EN_PISO == hataku.getEstadoSalto()) //Para que solamente suene una vez el sonido de salto
                    efectoSaltoHataku.play(PantallaMenu.volumen);
                hataku.saltar();
            }
        }

        else if (Gdx.input.isKeyJustPressed(Keys.SPACE) && Personaje.EstadoSalto.EN_PISO  == hataku.getEstadoSalto() ) {
                 //Para que solamente suene una vez el sonido de salto
                efectoSaltoHataku.play(PantallaMenu.volumen);
                hataku.saltar();
        }



        else if(!banderaBotonTouchApretado)//no se esta apretando nada..
            hataku.setEstado(Personaje.EstadoMovimiento.QUIETO);


    }
    //

    private void ganarJuego() {

        //PROBADORES PARA SABER EN QUE COORDENADAS VAS A GANAR..
        /*System.out.println("y = "+this.hataku.getY());

        System.out.println("X = "+this.hataku.getX());*/

        //temploTierra
        if(258 == this.hataku.getX() &&  512 <= this.hataku.getY() && this.numeroNivel == 1){ //258  y 512 es la posicion del templo, lo identifique con el system.out.println

            this.numeroNivel = 2;
            this.marcadorPergaminos = 0;
            this.efectoPuertaTemplo.play(PantallaMenu.volumen);
            PantallaCargando.partidaGuardada.putBoolean("nivelAgua", true); //se guarda el progreso y se desbloquea el nivel de agua...
            PantallaCargando.partidaGuardada.flush(); //se guardan los cambios

            //Se va regresar a seleccion de nivel
            plataforma.setScreen(new PantallaCargando(1, plataforma, true));

        }


        // temploAgua

        if( 44 == this.hataku.getX() && 1164  <= this.hataku.getY() && this.numeroNivel == 2){

            this.numeroNivel = 3;
            this.marcadorPergaminos = 0;
            this.efectoPuertaTemplo.play(PantallaMenu.volumen);
            PantallaCargando.partidaGuardada.putBoolean("nivelFuego", true); //se guarda el progreso y se desbloquea el nivel de agua...
            PantallaCargando.partidaGuardada.flush(); //se guardan los cambios

            //Se va regresar a seleccion de nivel
            plataforma.setScreen(new PantallaCargando(1, plataforma, true));

        }

/*
        //temploFuego

        if(this.templos.get(2).getSprite().getX() == this.hataku.getX() &&
                this.templos.get(2).getSprite().getY() == this.hataku.getY() && this.numeroNivel == 3){

            this.efectoPuertaTemplo.play(PantallaMenu.volumen);
            this.marcadorPergaminos = 0;

            //en este caso, como aun no hay otra pantalla, nos regresa al menu principal...
            plataforma.setScreen(new PantallaCargando(0, plataforma, true));

        }
        */

    }

    private void liberarArte() {
        if(this.marcadorPergaminos == 3 && this.numeroNivel == 1){
            //PantallaCargando.banderaArteTierra = true;
            PantallaCargando.partidaGuardada.putBoolean("arteTierra", true); //se guarda el progreso y se desbloquea la galeria de arte de tierra,,
            PantallaCargando.partidaGuardada.flush(); //se guardan los cambios

        }

        else if(this.marcadorPergaminos == 3 && this.numeroNivel == 2){
            //PantallaCargando.banderaArteAgua = true;
            PantallaCargando.partidaGuardada.putBoolean("arteAgua", true); //se guarda el progreso y se desbloquea la galeria de arte de agua..
            PantallaCargando.partidaGuardada.flush(); //se guardan los cambios


        }

        else if(this.marcadorPergaminos == 3 && this.numeroNivel == 3){
            //PantallaCargando.banderaArteFuego = true;
            PantallaCargando.partidaGuardada.getBoolean("arteFuego", true); // se guarda el progreso y se desbloquea la galeria de arte de fuego
            PantallaCargando.partidaGuardada.flush(); //se guardan los cambios
        }
    }

    private void atacarEnemigo(ObjetosJuego ataque, ObjetosJuego enemigo) {
        if(enemigo.getEstado()==ObjetosJuego.Estado.ENMAPA) {
            if (ataque.getEstadoAtaque() == ObjetosJuego.EstadoAtaque.DISPONIBLE) {
                if (hataku.getSprite().getX() < ataque.getSprite().getX()) {
                    ataque.mandarIzquierda();
                    if(enemigo.getSprite().isFlipX())
                        enemigo.getSprite().flip(true,false); //para que se voltee
                } else {
                    ataque.mandarDerecha();
                    if(!enemigo.getSprite().isFlipX())
                        enemigo.getSprite().flip(true,false); //para que se voltee
                }
            } else {
                ataque.actualizarAtaque(enemigoN1.get(ataqueFlag).getSprite().getX() + 15, enemigoN1.get(ataqueFlag).getSprite().getY() + 25);
            }
        }
        else {
            ataque.ocultar();
            ataque.actualizarAtaque(0, 0);
        }
    }

    private void perderJuego() {//Método para verificar si el usuario ya perdio

        //el usuario perdio sus vidas
        if (this.vidas.size() == 0){
            plataforma.setScreen(new PantallaCargando(0,plataforma,true)); //nos regresa a la pantalla principal.
        }

        //el usuario cayo en un precipcio
        if (this.hataku.getY()<-50){
            plataforma.setScreen(new PantallaCargando(0,plataforma,true)); //nos regresa a la pantalla principal.

        }
    }

    private void recogerObjeto() {
        //Recogerscrolls al tocarlos
        for (ObjetosJuego scrolls : scroll) {
            if(hataku.getSprite().getX()>= scrolls.getSprite().getX() && hataku.getSprite().getX()<= scrolls.getSprite().getX() + scrolls.getSprite().getWidth()
                    && hataku.getSprite().getY() >= scrolls.getSprite().getY() && hataku.getSprite().getY() <= scrolls.getSprite().getHeight() + scrolls.getSprite().getY() ||
                    hataku.getSprite().getX()+hataku.getSprite().getWidth()>= scrolls.getSprite().getX() && hataku.getSprite().getX()+hataku.getSprite().getWidth()<= scrolls.getSprite().getX() + scrolls.getSprite().getWidth()
                            && hataku.getSprite().getY()+hataku.getSprite().getHeight() >= scrolls.getSprite().getY() && hataku.getSprite().getY()+hataku.getSprite().getHeight() <= scrolls.getSprite().getHeight() + scrolls.getSprite().getY()){
                if(scrolls.getEstado() != ObjetosJuego.Estado.DESAPARECIDO) {
                    this.marcadorPergaminos++;
                    this.efectoTomarPergamino.play(PantallaMenu.volumen); //suena efecto
                    scrolls.quitarElemento();
                }
                break;
            }
        }
        //Recoger pociones al tocarlas
        for (ObjetosJuego pocion : pociones) {
            if(hataku.getSprite().getX()>= pocion.getSprite().getX() && hataku.getSprite().getX()<= pocion.getSprite().getX() + pocion.getSprite().getWidth()
                    && hataku.getSprite().getY() >= pocion.getSprite().getY() && hataku.getSprite().getY() <= pocion.getSprite().getHeight() + pocion.getSprite().getY() ||
                    hataku.getSprite().getX()+hataku.getSprite().getWidth()>= pocion.getSprite().getX() && hataku.getSprite().getX()+hataku.getSprite().getWidth()<= pocion.getSprite().getX() + pocion.getSprite().getWidth()
                            && hataku.getSprite().getY()+hataku.getSprite().getHeight() >= pocion.getSprite().getY() && hataku.getSprite().getY()+hataku.getSprite().getHeight() <= pocion.getSprite().getHeight() + pocion.getSprite().getY()){
                if(vidas.size()<3) {
                    if (pocion.getEstado() != ObjetosJuego.Estado.DESAPARECIDO) {

                        this.efectoTomarVida.play(PantallaMenu.volumen); //suena efecto
                        ObjetosJuego nuevo = new ObjetosJuego(this.texturaVidas);
                        this.vidas.add(nuevo);
                        nuevo.setPosicion(vidas.get(vidas.size()-2).getSprite().getX()+70,this.textoMarcadorVidas.getY()-45);
                        pocion.quitarElemento();
                    }
                    break;
                }
            }
        }

        //mata enemigos al toque
        for (ObjetosJuego Enemigo : enemigoN1) {
            if(hataku.getSprite().getX()>= Enemigo.getSprite().getX() && hataku.getSprite().getX()<= Enemigo.getSprite().getX() + Enemigo.getSprite().getWidth()-10
                    && hataku.getSprite().getY() >= Enemigo.getSprite().getY() && hataku.getSprite().getY() <= Enemigo.getSprite().getHeight()-5 + Enemigo.getSprite().getY()){
                if (Enemigo.getEstado() != ObjetosJuego.Estado.DESAPARECIDO) {

                    this.efectoDanio.play(PantallaMenu.volumen);
                    vidas.remove(vidas.size() - 1);
                    Enemigo.quitarElemento();
                }
                break;
            }
        }

        //tomar daño de ataque enemigos
        for (ObjetosJuego ataque: ataques){
            if(ataque.getSprite().getX()<= hataku.getSprite().getX()+hataku.getSprite().getWidth() && ataque.getSprite().getX()>= hataku.getSprite().getX()
                    && ataque.getSprite().getY()+ataque.getSprite().getHeight()<=hataku.getSprite().getY()+hataku.getSprite().getHeight() && ataque.getSprite().getY()+ataque.getSprite().getHeight()>=hataku.getSprite().getY() ||
                    ataque.getSprite().getX()<= hataku.getSprite().getX()+hataku.getSprite().getWidth() && ataque.getSprite().getX()>= hataku.getSprite().getX()
                            && ataque.getSprite().getY()<=hataku.getSprite().getY()+hataku.getSprite().getHeight() && ataque.getSprite().getY()>=hataku.getSprite().getY() ||
                    ataque.getSprite().getX()+ataque.getSprite().getWidth()<= hataku.getSprite().getX()+hataku.getSprite().getWidth() && ataque.getSprite().getX()+ataque.getSprite().getWidth()>= hataku.getSprite().getX()
                            && ataque.getSprite().getY()+ataque.getSprite().getHeight()<=hataku.getSprite().getY()+hataku.getSprite().getHeight() && ataque.getSprite().getY()+ataque.getSprite().getHeight()>=hataku.getSprite().getY() ||
                    ataque.getSprite().getX()+ataque.getSprite().getWidth()<= hataku.getSprite().getX()+hataku.getSprite().getWidth() && ataque.getSprite().getX()+ataque.getSprite().getWidth()>= hataku.getSprite().getX()
                            && ataque.getSprite().getY()<=hataku.getSprite().getY()+hataku.getSprite().getHeight() && ataque.getSprite().getY()>=hataku.getSprite().getY()){
                if (ataque.getEstadoAtaque() != ObjetosJuego.EstadoAtaque.OCULTO){
                    ataque.ocultar();
                    this.efectoDanio.play(PantallaMenu.volumen);
                    vidas.remove(vidas.size() - 1);
                }
            }
        }

    }

    // Actualiza la posición de la cámara para que el personaje esté en el centro,
    // excepto cuando esta en la primera y última parte del mundo
    private void actualizarCamara() {
        float posX = hataku.getX();
        float posY = hataku.getY();
        // Si está en la parte 'media'
        if (posX>=Principal.ANCHO_CAMARA/2 && posX<=ANCHO_MAPA-Principal.ANCHO_CAMARA/2) {
            // El personaje define el centro de la cámara
            camara.position.set((int)posX, camara.position.y, 0);
        } else if (posX>ANCHO_MAPA-Principal.ANCHO_CAMARA/2) {    // Si está en la última mitad
            // La cámara se queda media pantalla antes del fin del mundo  :)
            camara.position.set(ANCHO_MAPA-Principal.ANCHO_CAMARA/2, camara.position.y, 0);
        }
        if (posY>=Principal.ALTO_CAMARA/2 && posY<= Principal.ALTO_MUNDO-Principal.ALTO_CAMARA/2) {
            // El personaje define el centro de la cámara
            camara.position.set(camara.position.x, (int)posY, 0);
        } else if (posY>=Principal.ALTO_MUNDO-Principal.ALTO_CAMARA/2) {    // Si está en la última mitad
            // La cámara se queda media pantalla antes del fin del mundo  :)
            camara.position.set(camara.position.x, Principal.ALTO_MUNDO-Principal.ALTO_CAMARA/2, 0);
        }
        camara.update();
    }

    /*
    Mueve el personaje en Y hasta que se encuentre sobre un bloque
     */
    private void moverPersonaje() {
        switch (hataku.getEstadoMovimiento()) {
            case INICIANDO:
                // Los bloques en el mapa son de 16x16
                // Calcula la celda donde estaría después de moverlo
                int celdaX = (int)(hataku.getX()/ TAM_CELDA);
                int celdaY = (int)((hataku.getY()+hataku.VELOCIDAD_Y)/ TAM_CELDA);
                // Recuperamos la celda en esta posición
                // La capa 0 es el fondo
                TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(1);
                TiledMapTileLayer.Cell celda = capa.getCell(celdaX, celdaY);
                // probar si la celda está ocupada
                if (celda==null) {
                    // Celda vacía, entonces el personaje puede avazar
                    hataku.caer();
                } else {
                    // Dejarlo sobre la celda que lo detiene
                    hataku.setPosicion(hataku.getX(), (celdaY+1)* TAM_CELDA);
                    hataku.setEstado(Personaje.EstadoMovimiento.QUIETO);
                }
                break;
            case MOV_DERECHA:       // Siempre se mueve
            case MOV_IZQUIERDA:
                probarChoqueParedes();      // Prueba si debe moverse
                break;
        }
        // Prueba si debe caer por llegar a un espacio vacío
        if ( hataku.getEstadoMovimiento()!= Personaje.EstadoMovimiento.INICIANDO
                && (hataku.getEstadoSalto() != Personaje.EstadoSalto.SUBIENDO) ) {
            // Calcula la celda donde estaría después de moverlo
            int celdaX = (int) (hataku.getX() / TAM_CELDA);
            int celdaY = (int) ((hataku.getY() + hataku.VELOCIDAD_Y) / TAM_CELDA);
            // Recuperamos la celda en esta posición
            // La capa 0 es el fondo
            TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(1);
            TiledMapTileLayer.Cell celdaAbajo = capa.getCell(celdaX, celdaY);
            TiledMapTileLayer.Cell celdaDerecha = capa.getCell(celdaX+1, celdaY);
            // probar si la celda está ocupada
            if ( celdaAbajo==null && celdaDerecha==null ) {
                // Celda vacía, entonces el personaje puede avanzar
                hataku.caer();
                hataku.setEstadoSalto(Personaje.EstadoSalto.CAIDA_LIBRE);
            } else {
                // Dejarlo sobre la celda que lo detiene
                hataku.setPosicion(hataku.getX(), (celdaY + 1) * TAM_CELDA);
                hataku.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
            }
        }
        // Saltar
        switch (hataku.getEstadoSalto()) {
            case SUBIENDO:
            case BAJANDO:
                hataku.actualizarSalto();    // Actualizar posición en 'y'
                probarChoqueParedesSalto();
                break;
        }

    }

    private void probarChoqueParedesSalto() {
        Personaje.EstadoSalto estado = hataku.getEstadoSalto();
        // Quitar porque este método sólo se llama cuando se está moviendo
        if ( estado!= Personaje.EstadoSalto.SUBIENDO){
            return;
        }
        float px = hataku.getX();    // Posición actual
        // Posición después de actualizar
        px = hataku.getEstadoMovimiento()==Personaje.EstadoMovimiento.MOV_DERECHA? px+Personaje.VELOCIDAD_X: px- Personaje.VELOCIDAD_Y;
        int celdaX = (int)(px/TAM_CELDA);   // Casilla del personaje en X
        if (hataku.getEstadoMovimiento()== Personaje.EstadoMovimiento.MOV_DERECHA) {
            celdaX++;   // Casilla del lado derecho
        }
        int celdaY = (int)(hataku.getY()/TAM_CELDA); // Casilla del personaje en Y
        TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(1);
        if ( capaPlataforma.getCell(celdaX,celdaY+2) != null ) { //se le suma 2 para saber si va chocar desde antes..
            // Colisionará, dejamos de moverlo
            hataku.setEstadoSalto(Personaje.EstadoSalto.BAJANDO);
        } else {
            hataku.actualizar();
        }
    }


    private void borrarPantalla() {
        //Gdx.gl.glClearColor(1, 1, 1, 1);    // Color de fondo
        Gdx.gl.glClearColor(107 / 255f, 140f / 255, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }

    @Override
    public void pause() {

    }

    private void probarChoqueParedes() {
        Personaje.EstadoMovimiento estado = hataku.getEstadoMovimiento();
        // Quitar porque este método sólo se llama cuando se está moviendo
        if ( estado!= Personaje.EstadoMovimiento.MOV_DERECHA && estado!=Personaje.EstadoMovimiento.MOV_IZQUIERDA){
            return;
        }
        float px = hataku.getX();    // Posición actual
        // Posición después de actualizar
        px = hataku.getEstadoMovimiento()==Personaje.EstadoMovimiento.MOV_DERECHA? px+Personaje.VELOCIDAD_X:
                px- Personaje.VELOCIDAD_X;
        int celdaX = (int)(px/TAM_CELDA);   // Casilla del personaje en X
        if (hataku.getEstadoMovimiento()== Personaje.EstadoMovimiento.MOV_DERECHA) {
            celdaX++;   // Casilla del lado derecho
        }
        int celdaY = (int)(hataku.getY()/TAM_CELDA); // Casilla del personaje en Y
        TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(1);
        if ( capaPlataforma.getCell(celdaX,celdaY) != null ) {
            // Colisionará, dejamos de moverlo
            hataku.setEstado(Personaje.EstadoMovimiento.QUIETO);
        }
        else {
            hataku.actualizar(); //Hataku debe moverse,,
        }
    }

    /*
    Clase utilizada para manejar los eventos de touch en la pantalla
     */
    public class ProcesadorEntrada extends InputAdapter
    {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla virtual
        private boolean banderaBotonDerecha = false, banderaBotonIzquierda = false, banderaBotonSaltar = false; //Nos sirven para saber si lo debemos de regresar de tamaño y quitarle la trasnparencia cuando han sido presionados, esto se hace  en touchUp


        /*
        Se ejecuta cuando el usuario pone un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);
            if (estadoJuego==EstadosJuego.JUGANDO) {
                banderaBotonTouchApretado = true;
                // Preguntar si las coordenadas están sobre el botón derecho
                if (btnDerecha.contiene(x, y) && hataku.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    // Tocó el botón derecha, hacer que el personaje se mueva a la derecha
                    btnDerecha.setAlfa(.5f);
                    btnDerecha.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON - 2); //lo hago más pequeño
                    this.banderaBotonDerecha = true; //fue presionado le boton, indico aquí que fue preisonado.
                    hataku.setEstado(Personaje.EstadoMovimiento.MOV_DERECHA);

                } else if (btnIzquierda.contiene(x, y) && hataku.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    // Tocó el botón izquierda, hacer que el personaje se mueva a la izquierda
                    btnIzquierda.setAlfa(.5f);
                    btnIzquierda.setTamanio(PantallaJuego.TAMANIO_BOTON,PantallaJuego.TAMANIO_BOTON-1); //lo hago más pequeño
                    this.banderaBotonIzquierda = true;
                    hataku.setEstado(Personaje.EstadoMovimiento.MOV_IZQUIERDA);
                } else if (btnSalto.contiene(x, y) ) {
                    // Tocó el botón saltar
                    if(Personaje.EstadoSalto.EN_PISO == hataku.getEstadoSalto()) //Para que solamente suene una vez el sonido de salto
                        efectoSaltoHataku.play(PantallaMenu.volumen);

                    btnSalto.setAlfa(.5f);
                    btnSalto.setTamanio(PantallaJuego.TAMANIO_BOTON,PantallaJuego.TAMANIO_BOTON-2); //lo hago más pequeño
                    this.banderaBotonSaltar = true;
                    hataku.saltar();
                }
            }
            return true;    // Indica que ya procesó el evento
        }

        /*
       Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
        */
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);
            // Preguntar si las coordenadas son de algún botón para DETENER el movimiento
            if ( hataku.getEstadoMovimiento()!= Personaje.EstadoMovimiento.INICIANDO && (btnDerecha.contiene(x, y) || btnIzquierda.contiene(x,y))) {
                // Tocó el botón derecha, hacer que el personaje se mueva a la derecha
                hataku.setEstado(Personaje.EstadoMovimiento.QUIETO);

                //Ajusto tamaño y transprencia
                if(banderaBotonDerecha) {
                    btnDerecha.setAlfa(.7f);
                    btnDerecha.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON); //se rgresa a posicion original
                    banderaBotonDerecha = false;
                }
                else if(banderaBotonIzquierda) {
                    btnIzquierda.setAlfa(.7f);
                    btnIzquierda.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON); //se regresa a posicion original
                    banderaBotonIzquierda = false;
                }

                else if (banderaBotonSaltar) {
                    btnSalto.setAlfa(.7f);
                    btnSalto.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON); //se regresa a posicion orignal
                    banderaBotonSaltar = false;
                }

            }

            if( hataku.getEstadoMovimiento()!= Personaje.EstadoMovimiento.INICIANDO && btnSalto.contiene(x,y)){
                if (banderaBotonSaltar) {
                    btnSalto.setAlfa(.7f);
                    btnSalto.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON); //se regresa a posicion orignal
                    banderaBotonSaltar = false;
                }
            }
            return true;    // Indica que ya procesó el evento
        }

        // Se ejecuta cuando el usuario MUEVE el dedo sobre la pantalla
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            transformarCoordenadas(screenX, screenY);
            // Acaba de salir de las fechas (y no es el botón de salto)
            if (x<Principal.ANCHO_CAMARA/2 && hataku.getEstadoMovimiento()!= Personaje.EstadoMovimiento.QUIETO) {
                if (!btnIzquierda.contiene(x, y) && !btnDerecha.contiene(x, y) ) {
                    hataku.setEstado(Personaje.EstadoMovimiento.QUIETO);
                    //Ajusto tamaño y transprencia
                    if(banderaBotonDerecha) {
                        btnDerecha.setAlfa(.7f);
                        btnDerecha.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON); //se rgresa a posicion original
                        banderaBotonDerecha = false;
                    }
                    if(banderaBotonIzquierda) {
                        btnIzquierda.setAlfa(.7f);
                        btnIzquierda.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON); //se regresa a posicion original
                        banderaBotonIzquierda = false;
                    }
                }
            }
            else {
                if (!btnSalto.contiene(x,y)){
                    if (banderaBotonSaltar) {
                        btnSalto.setAlfa(.7f);
                        btnSalto.setTamanio(PantallaJuego.TAMANIO_BOTON, PantallaJuego.TAMANIO_BOTON); //se regresa a posicion orignal
                        banderaBotonSaltar = false;
                    }
                }
            }
            return true;
        }

        private void transformarCoordenadas(int screenX, int screenY) {
            // Transformar las coordenadas de la pantalla física a la cámara HUD
            coordenadas.set(screenX, screenY, 0);
            camaraHUD.unproject(coordenadas);
            // Obtiene las coordenadas relativas a la pantalla virtual
            x = coordenadas.x;
            y = coordenadas.y;
        }
    }

    //Se ejecutan de manera automatica cuando nos movemos de pantalla...
    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    // Libera los assets
    @Override
    public void dispose() {

        this.plataforma.dispose();
        this.batch.dispose();
        this.mapa.dispose();
        this.texturaFondo.dispose();
        this.rendererMapa.dispose();

        //texturas
        this.texturaHataku.dispose();
        this.texturaBtnDerecha.dispose();
        this.texturaBtnIzquierda.dispose();
        this.texturaSalto.dispose();
        this.texturaVidas.dispose();
        this.texturaPocion.dispose();
        this.texturaScroll.dispose();
        this.texturaVidas.dispose();
        this.texturaEN1.dispose();
        this.texturaTemplo.dispose();
        this.texturaAtaque.dispose();

        //sonidos (efectos)
        this.efectoSaltoHataku.dispose();
        this.efectoTomarVida.dispose();
        this.efectoTomarPergamino.dispose();
        this.efectoDanio.dispose();
        this.efectoPuertaTemplo.dispose();
    }


    public enum EstadosJuego {
        JUGANDO,
    }


}
