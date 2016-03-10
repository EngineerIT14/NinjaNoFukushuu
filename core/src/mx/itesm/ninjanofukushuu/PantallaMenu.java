package mx.itesm.ninjanofukushuu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.InputAdapter; //Este es para el touchUp y touchDown...


/*
Desarrolladores: Irvin Emmanuel Trujillo Díaz, Luis Fernando
Descripción: Esta clase es la encargada de mostrar el menu principal y sus determinados botones.
Profesor: Roberto Martinez Román.
*/

public class PantallaMenu implements Screen {

    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;
    //Fondo
    private Fondo fondo;
    private Texture texturaFondo;
    //Botones
    private OrthographicCamera camaraHUD;   // Cámara Botones
    private Boton btnPLay,btnInstructions,btnGallery,btnAbout;
    private Texture texturaBtnPlay,texturaBtnInstructions,texturaBtnGallery,texturaBtnAbout; //Textura, se administran los recursos...
    private static final int anchoBoton = 400 , altoBoton = 160;
    private static final float posicionYBotonJugarInstrucciones = Principal.ALTO_MUNDO/2-150, posicionYBotonGalleryAbout = Principal.ALTO_MUNDO/2 -310   ; //Posicion en y...

    //LOGO
    private Logotipo logo;
    private Texture texturaLogo;
    private static final int anchoLogo = 700 , altoLogo = 350;
    private static final int posicionCentradaXLogo = 330 , posicionCentradaYLogo = 380;

    //Efectos y musica de fondo
    private Sound efectoClick;
    private Music musicaFondo;
    private boolean banderaCancionJuego; //Esta bandera sirve para que se vuelva a crear el objeto de musicaFondo cuando regresas al menu principal y se interrumpa la que esta actualmente.

    //Dependiendo el caso,  sabemos que PANTALLA CARGAR, esto se hace en touchUp.
    // /*caso 1 = boton play, caso 2 = boton instrucciones, caso 3 = boton galería, caso 4 = boton acercaDe, caso0 = ningunBoton*/


    public PantallaMenu(Principal principal,boolean banderaCancionJuego) { //Constructor
        this.principal = principal;
        this.banderaCancionJuego = banderaCancionJuego;
    }

    //Metodo de la clase Screen, ya fue implementado
    @Override
    public void show() { //Se ejecuta cuando la pantalla se muestra en el juego
        // Crear la camara/vista
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();

        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara); //Se va ajustar la vista


        //CamaraHUD para botones
        camaraHUD = new OrthographicCamera(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        camaraHUD.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camaraHUD.update();

        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        this.cargarAudioJuego();
        this.crearObjetos();

        //Creando objetos...
        fondo = new Fondo(texturaFondo);
        btnPLay = new Boton(texturaBtnPlay);
        btnInstructions  = new Boton(texturaBtnInstructions);
        btnGallery = new Boton(texturaBtnGallery);
        btnAbout = new Boton(texturaBtnAbout);
        logo = new Logotipo(texturaLogo);

        //Colocando posición de botones.
        btnInstructions.setPosicion(295,PantallaMenu.posicionYBotonJugarInstrucciones);
        btnPLay.setPosicion(675, PantallaMenu.posicionYBotonJugarInstrucciones);
        btnGallery.setPosicion(15,PantallaMenu.posicionYBotonGalleryAbout);
        btnAbout.setPosicion(880,PantallaMenu.posicionYBotonGalleryAbout);
        logo.setPosicion(PantallaMenu.posicionCentradaXLogo, PantallaMenu.posicionCentradaYLogo);

        //ajustando el tamaño
        fondo.setTamanio(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        btnPLay.setTamanio(anchoBoton, altoBoton);
        btnInstructions.setTamanio(anchoBoton-10, altoBoton);
        btnGallery.setTamanio(anchoBoton+10, altoBoton); //Se le modifico el ancho a gallery para que se vea estético..
        btnAbout.setTamanio(anchoBoton+10, altoBoton);
        logo.setTamanio(anchoLogo-10, altoLogo);

        batch = new SpriteBatch();

    }

    //Obtener las texturas cargadas en la pantallaCargando.java
    private void crearObjetos(){
        AssetManager assetManager = principal.getAssetManager();   // Referencia al assetManager

        texturaFondo = assetManager.get("fondoMenu.png");
        texturaBtnPlay =  assetManager.get("botonPlay.png");
        texturaBtnInstructions =  assetManager.get("botonInstructions.png");
        texturaBtnGallery = assetManager.get("botonGallery.png");
        texturaBtnAbout =  assetManager.get("botonAbout.png");
        texturaLogo = assetManager.get("Title.png");
        //sonido
        this.efectoClick = assetManager.get("sonidoVentana.wav");
        this.efectoClick.setVolume(70,70);

    }

    //Metodo para cargar la musica del juego
    private void cargarAudioJuego() {
        //Musica de fondo
        if(!this.banderaCancionJuego) { //si es falsa, entonces no se esta escuchando nada....
            this.musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musicaJuegoPrincipal.wav"));
            this.musicaFondo.setLooping(true);
            this.musicaFondo.setVolume(70);
            this.musicaFondo.play();
        }
    }

    //Metodo de la clase Screen, ya fue implementado
    @Override//Recordar que render es automatico..
    public void render(float delta) { //ese delta es el tiempo, 60fps entonces el float seria 1/60
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0); //Con este color vas a borrar
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Instruccion para borrar

        batch.setProjectionMatrix(camara.combined); //Con este ajustas el batch...,en este caso, el boton, se ajusta.


        //fondo.getSprite().rotate(.1f);
        //DIBUJAR, primero las cosas que van atras....
        batch.begin(); //comienza a dibujar
        fondo.render(batch); //SE DIBUJAN LAS COSAS AQUI EN MEDIO...
        logo.render(batch);
        batch.end();

        //Dibuja el HUD, que es para los botones
        batch.setProjectionMatrix(camaraHUD.combined);
        batch.begin();
        btnPLay.render(batch);
        btnInstructions.render(batch);
        btnGallery.render(batch);
        btnAbout.render(batch);
        batch.end();

    }


    /*REVISION DE TOUCH*/
    //se elimino metodo leer entrada, ahora eso es de touchDown
    //Se eliminó el método de verificarBoton... ahora se usa touchUp, touchDown...

    //Métodos de la clase screen.
    @Override
    public void resize(int width, int height) {
        vista.update(width,height); //Actualizate!!!!
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }



    //Clase utilizada para manejar los eventos de touch en la pantalla
    public class ProcesadorEntrada extends InputAdapter {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla virtual
        private boolean banderaBotonPlay = false, banderaBotonInstructions = false, banderaBotonGallery = false, banderaBotonAbout = false; //Banderas que nos sirven para saber si el boton está transparente o no además que nos ayudan para la segunda condicion de touchUp.
        /*
        Se ejecuta cuando el usuario pone un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {

            transformarCoordenadas(screenX, screenY);

            if (btnPLay.contiene(x, y) ) {
                btnPLay.setAlfa(.5f);
                this.banderaBotonPlay = true; //el boton está transparente, entonces activo la bandera..
            }
            else if (btnInstructions.contiene(x, y)) {
                btnInstructions.setAlfa(.5f);  //al presionarse se hace transparente
                this.banderaBotonInstructions = true;
            }
            else if (btnAbout.contiene(x, y) ) {
                btnAbout.setAlfa(.5f);
                this.banderaBotonAbout = true;
            }
            else if (btnGallery.contiene(x, y) ) {
                btnGallery.setAlfa(.5f);
                this.banderaBotonGallery = true;
            }

            return true;    // Indica que ya procesó el evento
        }

        //Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            // Preguntar si las coordenadas son de cierto lugar de donde se quito el dedo
            //caso 1 = boton play, caso 2 = boton instrucciones, caso 3 = boton galería, caso 4 = boton acercaDe, caso0 = ningunBoton

            //en la pantalla cargando determinara que cargar... se manda el numero correspondiente para saber que se va cargar en esa clase..
            if (btnPLay.contiene(x, y) &&  this.banderaBotonPlay) {
                Gdx.app.log("leerEntrada", "HAY UN TAP EN PLAY!"); //cuando le apretan va decir esto..
                efectoClick.play(); //efecto de sonido
                principal.setScreen(new PantallaCargando(1,principal,true));  //se manda true porque ya esta la cancion reproduciendose

            }
            else if (btnInstructions.contiene(x, y) && this.banderaBotonInstructions) {
                Gdx.app.log("leerEntrada", "HAY UN TAP EN INSTRUCCIONES!"); //cuando le apretan va decir esto..
                efectoClick.play();
                principal.setScreen(new PantallaCargando(2,principal,true)); //se manda true porque ya esta la cancion reproduciendose
            }
            else if (btnAbout.contiene(x, y) && this.banderaBotonAbout ) {
                Gdx.app.log("leerEntrada", "HAY UN TAP! EN ABOUT"); //cuando le apretan va decir esto..
                efectoClick.play();
                principal.setScreen(new PantallaCargando(4,principal,true)); //se manda true porque ya esta la cancion reproduciendose
            }
            else if (btnGallery.contiene(x, y) && this.banderaBotonGallery ) {
                Gdx.app.log("leerEntrada", "HAY UN TAP EN GALLERIA!"); //cuando le apretan va decir esto..
                efectoClick.play();
                principal.setScreen(new PantallaCargando(3,principal,true));//se manda true porque ya esta la cancion reproduciendose
            }
            else{ //entonces el usuario despego el dedo de la pantalla en otra parte que no sean los botones...
                banderaBotonPlay = false;
                btnPLay.setAlfa(1);
                banderaBotonInstructions = false;
                btnInstructions.setAlfa(1);
                banderaBotonGallery = false;
                btnGallery.setAlfa(1);
                banderaBotonAbout = false;
                btnAbout.setAlfa(1);

            }
            return true;    // Indica que ya procesó el evento
        }

        private void transformarCoordenadas(int screenX, int screenY) {
            // Transformar las coordenadas de la pantalla física a la cámara
            coordenadas.set(screenX, screenY, 0);
            camaraHUD.unproject(coordenadas); //camaraHUD es para los botones
            // Obtiene las coordenadas relativas a la pantalla virtual
            x = coordenadas.x;
            y = coordenadas.y;
        }
    }



    //estos metodos sde ejecutan cuando se pasa a la otra pantalla


    @Override
    public void hide() {

    }

    @Override //Se ejecuta cuanto se pasa a otra pantalla, aqui se deben de liberar los recursos **
    public void dispose() {

        principal.dispose();
        batch.dispose();
        //Fondo
        texturaFondo.dispose();
        //Botones
        texturaBtnPlay.dispose();
        texturaBtnInstructions.dispose();
        texturaBtnGallery.dispose();
        texturaBtnAbout.dispose();
        //LOGO
        texturaLogo.dispose();
        //sonido
        this.efectoClick.dispose();

        //NOTA: LA MUSICA DE FONDO NO SE LIBER, YA QUE QUEREMOS QUE SE SIGA ESCUCHANDO..
    }
}
