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
Desarrolladores: Irvin Emmanuel Trujillo Díaz, Luis Fernando, Javier García Roque
Descripción: Esta clase es la encargada de mostrar el menu principal y sus determinados botones.
Profesor: Roberto Martinez Román.
*/

public class PantallaMenu implements Screen {

    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private static SpriteBatch batch;

    //Se utiliza static para que sea solamente 1 copia en memoria.
    //Fondo
    private static Fondo fondo;
    private static Fondo fondo1; //este rota
    private static Texture texturaFondo;
    private static Texture textureFondo1;
    //Botones
    private OrthographicCamera camaraHUD;   // Cámara Botones
    private static Boton btnPLay,btnInstructions,btnGallery,btnAbout,btnSonido;
    private static Texture texturaBtnPlay,texturaBtnInstructions,texturaBtnGallery,texturaBtnAbout,texturaBtnSonido, texturaMute; //Textura, se administran los recursos...
    private static final int ANCHO_BOTON = 385 , ALTO_BOTON = 135 , ANCHO_BOTON_SONIDO = 150, ALTO_BOTON_SONIDO= 100;
    private static final float POSICION_Y_BOTON_JUGAR_INSTRUCCIONES = Principal.ALTO_MUNDO/2-150, POSICION_Y_BOTON_GALLERY_ABOUT = Principal.ALTO_MUNDO/2 -310   ; //Posicion en y...

    //LOGO
    private static Logotipo logo; //1 copia
    private static Texture texturaLogo; //1 copia
    private static final int ANCHO_LOGO = 700 , ALTO_LOGO = 350;
    private static final int POSICION_X_CENTRADA_LOGO = 330 , POSICION_Y_CENTRADA_LOGO = 380;

    //Efectos y musica de fondo
    private static Sound efectoClick, efectoTrucoActivado; //Para que existe solamente 1 copia...
    private static Music musicaFondo; //Para que exista solamente 1 copia...
    public static float volumen = .5f; //volumen para reproducir sonidos...
    private boolean banderaCancionJuego; //Esta bandera sirve para que se no se  vuelva a crear el objeto de musicaFondo cuando regresas al menu principal y se interrumpa la que esta actualmente.

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

        this.cargarAudioJuego();
        this.crearObjetos();
        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        //ACTIVANDO BOTONES DE DISPOSITIVO PARA QUE EL USUARIO LOS PUEDA USAR SI ASI LOS DESEA...
        Gdx.input.setCatchBackKey(false);
        Gdx.input.setCatchMenuKey(false);

    }

    //Obtener las texturas cargadas en la pantallaCargando.java
    private void crearObjetos(){
        AssetManager assetManager = principal.getAssetManager();   // Referencia al assetManager
        textureFondo1 = assetManager.get("fondoMenu.jpg");
        texturaFondo = assetManager.get("NINJAH3.png");
        texturaBtnPlay =  assetManager.get("botonPlay.png");
        texturaBtnInstructions =  assetManager.get("botonInstructions.png");
        texturaBtnGallery = assetManager.get("botonGallery.png");
        texturaBtnAbout =  assetManager.get("botonAbout.png");
        texturaLogo = assetManager.get("Title.png");
        texturaBtnSonido = assetManager.get("bocina.png");
        texturaMute = assetManager.get("mute.png");

        //sonido
        efectoClick = assetManager.get("sonidoVentana.wav");
        efectoTrucoActivado = assetManager.get("seleccionNivel/sonidosGameplay/puertaTemplo.wav");

        //Creando objetos...
        fondo1 = new Fondo(textureFondo1);
        fondo = new Fondo(texturaFondo);
        btnPLay = new Boton(texturaBtnPlay);
        btnInstructions  = new Boton(texturaBtnInstructions);
        btnGallery = new Boton(texturaBtnGallery);
        btnAbout = new Boton(texturaBtnAbout);

        //Validacion para saber si el juego esta con mute, si se encuentra silenciado, la textura de la bocina debe ser la silenciada.
        if(!Principal.sonidoConMute)
            btnSonido = new Boton(texturaBtnSonido);
        else
            btnSonido =  new Boton(texturaMute);
        logo = new Logotipo(texturaLogo);

        //Colocando posición de botones. Se requieren cambios en y (+22)
        btnInstructions.setPosicion(295,PantallaMenu.POSICION_Y_BOTON_JUGAR_INSTRUCCIONES);
        btnPLay.setPosicion(675, PantallaMenu.POSICION_Y_BOTON_JUGAR_INSTRUCCIONES);
        btnGallery.setPosicion(15,PantallaMenu.POSICION_Y_BOTON_GALLERY_ABOUT);
        btnAbout.setPosicion(880, PantallaMenu.POSICION_Y_BOTON_GALLERY_ABOUT);
        btnSonido.setPosicion(1110, Principal.ALTO_MUNDO - 120);
        logo.setPosicion(PantallaMenu.POSICION_X_CENTRADA_LOGO, PantallaMenu.POSICION_Y_CENTRADA_LOGO);

        fondo1.getSprite().setCenter(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2);
        fondo1.getSprite().setOrigin(1500 / 2, 1500 / 2);

        //Ajustando tamaño adecuado.
        fondo.setTamanio(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        btnPLay.setTamanio(PantallaMenu.ANCHO_BOTON - 10, PantallaMenu.ALTO_BOTON);
        btnInstructions.setTamanio(PantallaMenu.ANCHO_BOTON - 10, PantallaMenu.ALTO_BOTON);
        btnGallery.setTamanio(PantallaMenu.ANCHO_BOTON + 10, PantallaMenu.ALTO_BOTON); //Se le modifico el ancho a gallery para que se vea estético..
        btnAbout.setTamanio(PantallaMenu.ANCHO_BOTON + 10, PantallaMenu.ALTO_BOTON);
        btnSonido.setTamanio(PantallaMenu.ANCHO_BOTON_SONIDO,PantallaMenu.ALTO_BOTON_SONIDO);
        logo.setTamanio(ANCHO_LOGO-10, ALTO_LOGO);


        batch = new SpriteBatch();
    }

    //Metodo para cargar la musica del juego
    private void cargarAudioJuego() {
        //Musica de fondo
        if(!banderaCancionJuego) { //si es falsa, entonces no se esta escuchando nada....
            musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musicaJuegoPrincipal.mp3"));
            musicaFondo.setLooping(true);
            musicaFondo.play();
            musicaFondo.setVolume(PantallaMenu.volumen);
        }
    }

    //Metodo de la clase Screen, ya fue implementado
    @Override//Recordar que render es automatico..
    public void render(float delta) { //ese delta es el tiempo, 60fps entonces el float seria 1/60
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0); //Con este color vas a borrar
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Instruccion para borrar

        batch.setProjectionMatrix(camara.combined); //Con este ajustas el batch...,en este caso, el boton, se ajusta.

        fondo1.getSprite().rotate(.1f);
        //fondo.getSprite().rotate(.1f);
        //DIBUJAR, primero las cosas que van atras....
        batch.begin(); //comienza a dibujar
        fondo1.render(batch);
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
        btnSonido.render(batch);
        batch.end();

    }
    /*REVISION DE TOUCH*/

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
        private int contadorCabeza = 0; //Para aplicar el truco de liberacion de todo el contenido del juego

        private boolean banderaBotonPlay = false, banderaBotonInstructions = false,
                banderaBotonGallery = false, banderaBotonAbout = false, banderaBotonSonido = false; //Banderas que nos sirven para saber si el boton está transparente y  pequeño o  no además que nos ayudan para la segunda condicion de touchUp.
        /*
        Se ejecuta cuando el usuario pone un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {

            transformarCoordenadas(screenX, screenY);

            if (btnPLay.contiene(x, y)) {
                contadorCabeza = 0;
                btnPLay.setAlfa(.5f);
                btnPLay.setTamanio(PantallaMenu.ANCHO_BOTON - 10, PantallaMenu.ALTO_BOTON - 2); //Lo hago más pequeño
                banderaBotonPlay = true; //el boton está transparente, entonces activo la bandera..
            }
            //el -10, +10 +10 es porque así estan los botones originales con el tamaño correspondiente
            else if (btnInstructions.contiene(x, y)) {
                btnInstructions.setAlfa(.5f);  //al presionarse se hace transparente
                contadorCabeza = 0;
                btnInstructions.setTamanio(PantallaMenu.ANCHO_BOTON - 10, PantallaMenu.ALTO_BOTON - 2); //Lo hago más pequeño
                banderaBotonInstructions = true;
            }
            else if (btnAbout.contiene(x, y) ) {
                contadorCabeza = 0;
                btnAbout.setAlfa(.5f);
                btnAbout.setTamanio(PantallaMenu.ANCHO_BOTON + 10, PantallaMenu.ALTO_BOTON - 2); //Lo hago más pequeño
                banderaBotonAbout = true;
            }
            else if (btnGallery.contiene(x, y) ) {
                contadorCabeza = 0;
                btnGallery.setAlfa(.5f);
                btnGallery.setTamanio(PantallaMenu.ANCHO_BOTON + 10, PantallaMenu.ALTO_BOTON - 2); //Lo hago más pequeño
                banderaBotonGallery = true;
            }

            else if (btnGallery.contiene(x, y) ) {
                contadorCabeza = 0;
                btnGallery.setAlfa(.5f);
                btnGallery.setTamanio(PantallaMenu.ANCHO_BOTON+10,PantallaMenu.ALTO_BOTON-2); //Lo hago más pequeño
                banderaBotonGallery = true;
            }

            else if (btnSonido.contiene(x,y)){
                contadorCabeza = 0;
                btnSonido.setAlfa(.5f);
                btnSonido.setTamanio(PantallaMenu.ANCHO_BOTON_SONIDO,PantallaMenu.ALTO_BOTON_SONIDO-2); //Lo hago más pequeño
                banderaBotonSonido = true;

            }

            else if (250<=x && x<=390 && 382<=y && y<=516) { //posicion cabeza del ninja
                //System.out.println(x+" "+y);
                contadorCabeza+=1; //Para activar el truco
                if(contadorCabeza == 10)
                    desbloquearTodoElContenido();
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
            if (btnPLay.contiene(x, y) &&  banderaBotonPlay) {
                //Gdx.app.log("leerEntrada", "HAY UN TAP EN PLAY!"); //cuando le apretan va decir esto..
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido
                principal.setScreen(new PantallaCargando(1,principal,true));  //se manda true porque ya esta la cancion reproduciendose

            }
            else if (btnInstructions.contiene(x, y) && banderaBotonInstructions) {
                //Gdx.app.log("leerEntrada", "HAY UN TAP EN INSTRUCCIONES!"); //cuando le apretan va decir esto..
                efectoClick.play(PantallaMenu.volumen);
                principal.setScreen(new PantallaCargando(2,principal,true)); //se manda true porque ya esta la cancion reproduciendose
            }
            else if (btnAbout.contiene(x, y) && banderaBotonAbout ) {
                //Gdx.app.log("leerEntrada", "HAY UN TAP! EN ABOUT"); //cuando le apretan va decir esto..
                efectoClick.play(PantallaMenu.volumen);
                principal.setScreen(new PantallaCargando(4,principal,true)); //se manda true porque ya esta la cancion reproduciendose
            }
            else if (btnGallery.contiene(x, y) && banderaBotonGallery ) {
                //Gdx.app.log("leerEntrada", "HAY UN TAP EN GALLERIA!"); //cuando le apretan va decir esto..
                efectoClick.play(PantallaMenu.volumen);
                principal.setScreen(new PantallaCargando(3,principal,true));//se manda true porque ya esta la cancion reproduciendose
            }
            else if (btnSonido.contiene(x, y) && banderaBotonSonido && !Principal.sonidoConMute ) {
                //Gdx.app.log("leerEntrada", "HAY UN TAP EN SONIDO!"); //cuando le apretan va decir esto..
                silenciarJuego();
                banderaBotonSonido = false;
                btnSonido.setAlfa(1);
                btnSonido.setTamanio(PantallaMenu.ANCHO_BOTON_SONIDO, ALTO_BOTON_SONIDO);
                Principal.sonidoConMute = true;

            }

            else if (btnSonido.contiene(x, y) && banderaBotonSonido && Principal.sonidoConMute ) {
                //Gdx.app.log("leerEntrada", "HAY UN TAP EN SONIDO!"); //cuando le apretan va decir esto..
                activarMusicaJuego();
                banderaBotonSonido = false;
                btnSonido.setAlfa(1);
                btnSonido.setTamanio(PantallaMenu.ANCHO_BOTON_SONIDO, ALTO_BOTON_SONIDO);
                Principal.sonidoConMute = false;

            }

            else{ //entonces el usuario despego el dedo de la pantalla en otra parte que no sean los botones...
                // se le quita la transparencia y se regresa a su tamaño original
                //****NOTA: el -10, +10 +10 es porque así estan los botones originales con el tamaño correspondiente
                if(banderaBotonPlay) {
                    banderaBotonPlay = false;
                    btnPLay.setAlfa(1);
                    btnPLay.setTamanio(PantallaMenu.ANCHO_BOTON - 10, PantallaMenu.ALTO_BOTON); //tamaño original
                }
                else if(banderaBotonInstructions) {
                    banderaBotonInstructions = false;
                    btnInstructions.setAlfa(1);
                    btnInstructions.setTamanio(PantallaMenu.ANCHO_BOTON - 10, PantallaMenu.ALTO_BOTON); //tamaño original
                }
                else if(banderaBotonGallery) {
                    banderaBotonGallery = false;
                    btnGallery.setAlfa(1);
                    btnGallery.setTamanio(PantallaMenu.ANCHO_BOTON + 10, PantallaMenu.ALTO_BOTON); //tamaño orginal
                }
                else if(banderaBotonAbout) {
                    banderaBotonAbout = false;
                    btnAbout.setAlfa(1);
                    btnAbout.setTamanio(PantallaMenu.ANCHO_BOTON + 10, PantallaMenu.ALTO_BOTON); //tamaño orginal
                }
                else if(banderaBotonSonido) {
                    banderaBotonSonido = false;
                    btnSonido.setAlfa(1);
                    btnSonido.setTamanio(PantallaMenu.ANCHO_BOTON_SONIDO, ALTO_BOTON_SONIDO);
                }
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

    private void desbloquearTodoElContenido() { //Metodo que se activa cuando el usuario toca 10 veces la cabeza del ninja, es un glitch para liberar los debsloqueables..
        //Liberas niveles

        PantallaCargando.partidaGuardada.putBoolean("nivelAgua", true); //se guarda el progreso y se desbloquea el nivel de agua...
        PantallaCargando.partidaGuardada.flush(); //se guardan los cambios

        PantallaCargando.partidaGuardada.putBoolean("nivelFuego", true); //se guarda el progreso y se desbloquea el nivel de agua...
        PantallaCargando.partidaGuardada.flush(); //se guardan los cambios

        //Liberas galeria de arte
        PantallaCargando.partidaGuardada.putBoolean("arteTierra", true); //se guarda el progreso y se desbloquea la galeria de arte de tierra,,
        PantallaCargando.partidaGuardada.flush(); //se guardan los cambios

        PantallaCargando.partidaGuardada.putBoolean("arteAgua", true); //se guarda el progreso y se desbloquea la galeria de arte de agua// ,,
        PantallaCargando.partidaGuardada.flush(); //se guardan los cambios

        PantallaCargando.partidaGuardada.putBoolean("arteFuego", true); //se guarda el progreso y se desbloquea la galeria de arte de fuego// ,,
        PantallaCargando.partidaGuardada.flush(); //se guardan los cambios

        //El sonido suena, aun si el usuario quito el sonido
        efectoTrucoActivado.play();
    }

    private void silenciarJuego() {
        btnSonido = new Boton(texturaMute);
        btnSonido.setPosicion(1110, Principal.ALTO_MUNDO - 120);
        PantallaMenu.volumen = 0f;
        musicaFondo.setVolume(PantallaMenu.volumen);
    }

    private void activarMusicaJuego(){ //Para que se vuela a poner la musica del juego
        btnSonido = new Boton(texturaBtnSonido);
        btnSonido.setPosicion(1110, Principal.ALTO_MUNDO - 120);
        PantallaMenu.volumen = .5f;
        musicaFondo.setVolume(PantallaMenu.volumen);
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
        textureFondo1.dispose(); //Ninja...
        //Botones
        texturaBtnPlay.dispose();
        texturaBtnInstructions.dispose();
        texturaBtnGallery.dispose();
        texturaBtnAbout.dispose();
        //LOGO
        texturaLogo.dispose();
        //sonido
        efectoClick.dispose();
        this.efectoTrucoActivado.dispose();
        //texturas
        texturaBtnSonido.dispose();
        texturaMute.dispose();
        //NOTA: LA MUSICA DE FONDO NO SE LIBERA, YA QUE QUEREMOS QUE SE SIGA ESCUCHANDO..
    }
}
