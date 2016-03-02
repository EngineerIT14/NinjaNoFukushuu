package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
Desarrolladores: Irvin Emmanuel Trujillo Díaz, Javier García Roque y Luis Fernando
Descripción: Esta clase es la encargada de mostrar el juego y su comportamiento...
Profesor: Roberto Martinez Román. .
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

    //Scrolls/Pergamino
    private Array<ObjetosJuego> scroll;
    private Texture texturaScroll;

    //Vidas
    private Array<ObjetosJuego> vidas;
    private Texture texturaVidas;

    //Pociones
    private Array<ObjetosJuego> pociones;
    private Texture texturaPocion;

    private int Pergaminos;
    private int Vida;
    private Texto texto;


    /*//Estado para la suma del marcador
    private Estado estado;*/

    public PantallaJuego(Principal plataforma) {
        this.plataforma = plataforma;
    }

    @Override
    public void show() {
        // Crea la cámara/vista
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();
        vista = new StretchViewport(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO, camara);

        batch = new SpriteBatch();

        // Cámara para HUD
        camaraHUD = new OrthographicCamera(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        camaraHUD.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camaraHUD.update();

        cargarRecursos();
        crearObjetos();
        texto = new Texto();

        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    // Carga los recursos a través del administrador de assets
    private void cargarRecursos() {
        // Cargar las texturas/mapas
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager
        assetManager.load("MapaDeTierraV2.tmx", TiledMap.class);  // Cargar info del mapa
        assetManager.load("marioSprite.png", Texture.class);    // Cargar imagen
        // Texturas de los botones
        assetManager.load("derecha.png", Texture.class);
        assetManager.load("izquierda.png", Texture.class);

        // Se bloquea hasta que cargue todos los recursos
        assetManager.finishLoading();
        //Textura Objetos
        texturaScroll = new Texture(Gdx.files.internal("scroll.png"));
        texturaPocion = new Texture(Gdx.files.internal("pocion.png"));
    }

    private void crearObjetos() {
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager
        // Carga el mapa en memoria
        mapa = assetManager.get("MapaDeTierraV2.tmx");
        //mapa.getLayers().get(0).setVisible(false);
        // Crear el objeto que dibujará el mapa
        rendererMapa = new OrthogonalTiledMapRenderer(mapa,batch);
        rendererMapa.setView(camara);
        // Cargar frames
        texturaHataku = assetManager.get("marioSprite.png");
        // Crear el personaje
        hataku = new Personaje(texturaHataku);
        // Posición inicial del personaje
        hataku.getSprite().setPosition(Principal.ANCHO_MUNDO / 10, Principal.ALTO_MUNDO * 0.90f);

        // Crear los botones
        texturaBtnIzquierda = assetManager.get("izquierda.png");
        btnIzquierda = new Boton(texturaBtnIzquierda);
        btnIzquierda.setPosicion(TAM_CELDA, 5 * TAM_CELDA);
        btnIzquierda.setAlfa(0.7f); // Un poco de transparencia
        texturaBtnDerecha = assetManager.get("derecha.png");
        btnDerecha = new Boton(texturaBtnDerecha);
        btnDerecha.setPosicion(6 * TAM_CELDA, 5 * TAM_CELDA);
        btnDerecha.setAlfa(0.7f); // Un poco de transparencia
        //Lista scrolles
        scroll = new Array<ObjetosJuego>(3);
        for (int i = 0; i<3;i++) {
            ObjetosJuego nuevo = new ObjetosJuego(texturaScroll);
            nuevo.setTamanio(50,50);
            scroll.add(nuevo);
        }
        scroll.get(0).setPosicion(550,300);
        scroll.get(1).setPosicion(800, 50);
        scroll.get(2).setPosicion(230, 630);
        //Pociones
        pociones = new Array<ObjetosJuego>(2);
        for(int i =0; i< 2;i++) {
            ObjetosJuego nuevo = new ObjetosJuego(texturaPocion);
            nuevo.setTamanio(50,50);
            pociones.add(nuevo);
        }
        //Se colocan las pociones en el lugar correspondiente
        pociones.get(0).setPosicion(1000, Principal.ALTO_MUNDO / 2);
        pociones.get(1).setPosicion(300, 500);
    }

    /*
    Dibuja TODOS los elementos del juego en la pantalla.
    Este método se está ejecutando muchas veces por segundo.
     */
    @Override
    public void render(float delta) { // delta es el tiempo entre frames (Gdx.graphics.getDeltaTime())
        // Leer entrada

        // Actualizar objetos en la pantalla
        moverPersonaje();
        actualizarCamara(); // Mover la cámara para que siga al personaje

        // Dibujar
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);

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
        // Mostrar pergaminos
        texto.mostrarMensaje(batch, "Pergaminos: "+Pergaminos,
                0.8f *Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO*0.96f);

        // Mostrar vida
        texto.mostrarMensaje(batch, "Vida: " + Vida,
                0.1f * Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO * 0.96f);

        batch.end();
        //Dibuja el HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        batch.begin();
        btnIzquierda.render(batch);
        btnDerecha.render(batch);

        batch.end();

    }

    private void recogerObjeto() {
        //Recogerscrolls al tocarlos
        for (ObjetosJuego scrolls : scroll) {
            if(hataku.getSprite().getX()>= scrolls.getSprite().getX() && hataku.getSprite().getX()<= scrolls.getSprite().getX() + scrolls.getSprite().getWidth()
                    && hataku.getSprite().getY() >= scrolls.getSprite().getY() && hataku.getSprite().getY() <= scrolls.getSprite().getHeight() + scrolls.getSprite().getY()){
                if(scrolls.getEstado() != ObjetosJuego.Estado.DESAPARECIDO) {
                    Pergaminos++;
                    scrolls.quitarElemento();
                }
                break;
            }
        }
        //Recoger pociones al tocarlas
        for (ObjetosJuego pocion : pociones) {
            if(hataku.getSprite().getX()>= pocion.getSprite().getX() && hataku.getSprite().getX()<= pocion.getSprite().getX() + pocion.getSprite().getWidth()
                    && hataku.getSprite().getY() >= pocion.getSprite().getY() && hataku.getSprite().getY() <= pocion.getSprite().getHeight() + pocion.getSprite().getY()){
                if(pocion.getEstado() != ObjetosJuego.Estado.DESAPARECIDO){
                    Vida++;
                    pocion.quitarElemento();
                }
                break;
            }
        }
    }

    // Actualiza la posición de la cámara para que el personaje esté en el centro,
    // excepto cuando esta en la primera y última parte del mundo
    private void actualizarCamara() {
        float posX = hataku.getX();
        // Si está en la parte 'media'
        if (posX>=Principal.ANCHO_MUNDO/2 && posX<=ANCHO_MAPA-Principal.ANCHO_MUNDO/2) {
            // El personaje define el centro de la cámara
            camara.position.set((int)posX, camara.position.y, 0);
        } else if (posX>ANCHO_MAPA-Principal.ANCHO_MUNDO/2) {    // Si está en la última mitad
            // La cámara se queda media pantalla antes del fin del mundo  :)
            camara.position.set(ANCHO_MAPA-Principal.ANCHO_MUNDO/2, camara.position.y, 0);
        }
        camara.update();
    }

    /*
    Mueve el personaje en Y hasta que se encuentre sobre un bloque
     */
    private void moverPersonaje() {
        switch (hataku.getEstado()) {
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
                    hataku.actualizar();
                } else {
                    // Dejarlo sobre la celda que lo detiene
                    hataku.setPosicion(hataku.getX(), (celdaY+1)* TAM_CELDA);
                    hataku.setEstado(Personaje.Estado.QUIETO);
                }
                break;
            case MOV_DERECHA:       // Siempre se mueve
            case MOV_IZQUIERDA:
                hataku.actualizar();
                break;
        }

    }

    private void borrarPantalla() {
        //Gdx.gl.glClearColor(1, 1, 1, 1);    // Color de fondo
        Gdx.gl.glClearColor(107 / 255f, 140f / 255, 1, 1);
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

    @Override
    public void hide() {

    }

    // Libera los assets
    @Override
    public void dispose() {
        texturaHataku.dispose();
        mapa.dispose();
        texturaBtnDerecha.dispose();
        texturaBtnIzquierda.dispose();
        texturaPocion.dispose();
        texturaScroll.dispose();
    }

    /*
    Clase utilizada para manejar los eventos de touch en la pantalla
     */
    public class ProcesadorEntrada extends InputAdapter
    {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla virtual
        /*
        Se ejecuta cuando el usuario pone un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);
            // Preguntar si está dentro del botón derecho
            if (btnDerecha.contiene(x,y)) {
                // Tocó el botón derecha, hacer que el personaje se mueva a la derecha
                hataku.setEstado(Personaje.Estado.MOV_DERECHA);
            } else if (btnIzquierda.contiene(x,y)) {
                // Tocó el botón izquierda, hacer que el personaje se mueva a la izquierda
                hataku.setEstado(Personaje.Estado.MOV_IZQUIERDA);
            }
            return true;    // Indica que ya procesó el evento
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
}