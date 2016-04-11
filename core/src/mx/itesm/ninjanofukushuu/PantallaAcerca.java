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
Desarrolladores = Luis Fernando e Irvin Emmanuel Trujillo Díaz
Descripción: Esta clase es la encargada de representar la pantalla de "About", donde se muestran elementos para conocer acerca de los desarrolladores
Profesor: Roberto Martinez Román.
 */
public class PantallaAcerca implements Screen {

    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;
    //Fondo
    private Fondo fondo;
    private Texture texturaFondo;

    //Botones
    private Boton btnMia,btnNuri,btnIrvin,btnJavier, btnFer, btnRegresar;
    private Texture texturaBtnMia,texturaBtnNuri, texturaBtnIrvin,texturaBtnJavier, texturaBtnFer, texturaRegresar;
    private static final int anchoBoton = 180, altoBoton = 200; //anchoBoton1 = 480 , altoBoton1 = 160;
    //Presentaciones
    private Presentacion presentacionIrvin, presentacionMia, presentacionJavier, presentacionNuri, presentacionFer;
    private  Texture texturaPresentacionIrvin, texturaPresentacionMia, texturaPresentacionJavier, texturaPresentacionNuri, texturaPresentacionFer;

    //Efectos y musica de fondo
    private Sound efectoClick;



    public PantallaAcerca(Principal principal) {
        this.principal = principal;
    }

    @Override
    public void show() {
        //Crear camara
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();
        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara);

        this.crearObjetos();
        //Batch
        batch = new SpriteBatch();

        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        //Crear fondo
        fondo = new Fondo(texturaFondo);
        //Crear botones
        btnMia = new Boton(texturaBtnMia);
        btnNuri = new  Boton(texturaBtnNuri);
        btnIrvin = new Boton(texturaBtnIrvin);
        btnJavier = new Boton(texturaBtnJavier);
        btnFer = new Boton(texturaBtnFer);
        btnRegresar = new Boton(texturaRegresar);

        //Crear presentaciones

        presentacionIrvin = new Presentacion(texturaPresentacionIrvin);
        presentacionMia = new Presentacion(texturaPresentacionMia);
        presentacionJavier = new Presentacion(texturaPresentacionJavier);
        presentacionNuri = new Presentacion(texturaPresentacionNuri);
        presentacionFer = new Presentacion(texturaPresentacionFer);

        //Poscicionar objetos
        // abanico.setPosicion(0 - 20, 0 - 35);
        btnIrvin.setPosicion(Principal.ANCHO_MUNDO / 4 - 100, Principal.ALTO_MUNDO * 2 / 3 - 100);
        btnMia.setPosicion(Principal.ANCHO_MUNDO / 2 - 100, Principal.ALTO_MUNDO * 2 / 3 - 100);
        btnJavier.setPosicion(Principal.ANCHO_MUNDO * 3 / 4 - 100, Principal.ALTO_MUNDO * 2 / 3 - 100);
        btnNuri.setPosicion(Principal.ANCHO_MUNDO * 3 / 8 - 100, Principal.ALTO_MUNDO / 3 - 100);
        btnFer.setPosicion(Principal.ANCHO_MUNDO * 5 / 8 - 100, Principal.ALTO_MUNDO / 3 - 100);
        btnRegresar.setPosicion(Principal.ANCHO_MUNDO * 7 / 8, Principal.ALTO_MUNDO * 1 / 5 -130);
        presentacionIrvin.setPosicion(0+50, 0 );
        presentacionMia.setPosicion(0+50, 0 );
        presentacionJavier.setPosicion(0+50, 0);
        presentacionNuri.setPosicion(0+50, 0 );
        presentacionFer.setPosicion(0+50, 0 );
        //Ajuste de tamaño
        //abanico.setTamanio(1280, 780);
        btnIrvin.setTamanio(anchoBoton, altoBoton);
        btnMia.setTamanio(anchoBoton, altoBoton);
        btnJavier.setTamanio(anchoBoton,altoBoton);
        btnNuri.setTamanio(anchoBoton, altoBoton);
        btnFer.setTamanio(anchoBoton, altoBoton);
        btnRegresar.setTamanio(anchoBoton-30 , altoBoton-30 );

        fondo.getSprite().setCenter(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2);
        fondo.getSprite().setOrigin(1500 / 2, 1500 / 2);
        presentacionIrvin.setTamanio(1200, 720);
        presentacionMia.setTamanio(1200,720);
        presentacionJavier.setTamanio(1200, 720);
        presentacionNuri.setTamanio(1200,720);
        presentacionFer.setTamanio(1200, 720);

    }

    //crea los objetos de textura y audio
    private void crearObjetos(){
        AssetManager assetManager = principal.getAssetManager();   // Referencia al assetManager
        //Fondo
        texturaFondo = assetManager.get("imagenesAcercaDe/Fondo.jpg");
        //Abanico
        //texturaAbanico = new Texture(Gdx.files.internal("Abanico.png"));
        //Botones
        texturaBtnMia = assetManager.get("imagenesAcercaDe/M.png");
        texturaBtnNuri = assetManager.get("imagenesAcercaDe/N.png");
        texturaBtnIrvin = assetManager.get("imagenesAcercaDe/I.png");
        texturaBtnJavier = assetManager.get("imagenesAcercaDe/J.png");
        texturaBtnFer = assetManager.get("imagenesAcercaDe/F.png");
        texturaRegresar = assetManager.get("return.png"); // esta en la raiz
        //Presentaciones
        texturaPresentacionIrvin = assetManager.get("imagenesAcercaDe/Irvi.png");
        texturaPresentacionMia = assetManager.get("imagenesAcercaDe/Mare.png");
        texturaPresentacionJavier = assetManager.get("imagenesAcercaDe/Javo.png");
        texturaPresentacionNuri = assetManager.get("imagenesAcercaDe/Nuria.png");
        texturaPresentacionFer = assetManager.get("imagenesAcercaDe/Fercho.png");

        efectoClick = assetManager.get("sonidoVentana.wav");


    }

    @Override
    public void render(float delta) {
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);

        fondo.getSprite().rotate(.1f);
        //DIBUJAR
        batch.begin();
        fondo.render(batch);
        //abanico.render(batch);
        btnIrvin.render(batch);
        btnMia.render(batch);
        btnJavier.render(batch);
        btnNuri.render(batch);
        btnFer.render(batch);
        btnRegresar.render(batch);
        //Se dibujaran las ´resentaciones si y solo si el estado es APERECIDO
        if(presentacionIrvin.actualizar()){
            presentacionIrvin.render(batch);
        }
        if(presentacionMia.actualizar()){
            presentacionMia.render(batch);
        }
        if(presentacionJavier.actualizar()){
            presentacionJavier.render(batch);
        }
        if(presentacionNuri.actualizar()){
            presentacionNuri.render(batch);
        }
        if(presentacionFer.actualizar()){
            presentacionFer.render(batch);
        }
        batch.end();
    }


     /*REVISION DE TOUCH*/
    //se elimino metodo leer entrada, ahora eso es de touchDown
    //Se eliminó el método de verificarBoton... ahora se usa touchUp, touchDown...

    //Clase utilizada para manejar los eventos de touch en la pantalla
    public class ProcesadorEntrada extends InputAdapter {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla virtual

        private boolean banderaBotonIrvin = false, banderaBotonMia = false, banderaBotonJavier = false, banderaBotonNuri = false, banderaBotonFer = false, banderaBotonRegresar = false;  //Banderas que nos sirven para saber si el boton está transparente y  pequeño o  no además que nos ayudan para la segunda condicion de touchUp.
        /*
        Se ejecuta cuando el usuario pone un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {

            //caso cuando no hay ninguna ventana emerge abierta...
            if(presentacionIrvin.getEstado() != Presentacion.Estado.APARECIDO && presentacionMia.getEstado() != Presentacion.Estado.APARECIDO && presentacionJavier.getEstado() != Presentacion.Estado.APARECIDO
                    && presentacionNuri.getEstado() != Presentacion.Estado.APARECIDO && presentacionFer.getEstado() !=  Presentacion.Estado.APARECIDO) {

                transformarCoordenadas(screenX, screenY);
                if (btnIrvin.contiene(x, y)) {
                    btnIrvin.setAlfa(.5f); //al presionarse se hace transparente
                    btnIrvin.setTamanio(anchoBoton, altoBoton - 2); //Lo hago más pequeño
                    this.banderaBotonIrvin = true; //el boton está transparente, entonces activo la bandera..
                }
                else if (btnMia.contiene(x, y)) {
                    btnMia.setAlfa(.5f);  //al presionarse se hace transparente
                    btnMia.setTamanio(anchoBoton, altoBoton - 2); //Lo hago más pequeño
                    this.banderaBotonMia = true;
                }
                else if (btnJavier.contiene(x, y)) {
                    btnJavier.setAlfa(.5f);
                    btnJavier.setTamanio(anchoBoton, altoBoton - 2); //Lo hago más pequeño
                    this.banderaBotonJavier = true;
                }
                else if (btnNuri.contiene(x, y)) {
                    btnNuri.setAlfa(.5f);
                    btnNuri.setTamanio(anchoBoton, altoBoton - 2); //Lo hago más pequeño
                    this.banderaBotonNuri = true;
                }
                else if (btnFer.contiene(x,y)){
                    btnFer.setAlfa(.5f);
                    btnFer.setTamanio(anchoBoton, altoBoton - 2); //Lo hago más pequeño
                    this.banderaBotonFer = true;
                }
                else if (btnRegresar.contiene(x,y)){
                    btnRegresar.setAlfa(.5f);
                    btnRegresar.setTamanio(anchoBoton-30, altoBoton - 32); //Lo hago más pequeño
                    this.banderaBotonRegresar = true;
                }
                return true;    // Indica que ya procesó el evento
            }

            //caso cuando esta una ventana emergente abierta..
            //Para desaparecer las presentaciones se puede seleccionar cualquiere espacio e la pantalla
            else
                if(presentacionIrvin.getEstado()== Presentacion.Estado.APARECIDO){
                    if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                        banderaBotonIrvin = false;
                        btnIrvin.setAlfa(1);
                        btnIrvin.setTamanio(anchoBoton, altoBoton); //tamaño original
                        presentacionIrvin.desaparecer();
                        efectoClick.play(PantallaMenu.volumen);
                    }
                }
                else if(presentacionMia.getEstado() == Presentacion.Estado.APARECIDO){
                    if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                        banderaBotonMia = false;
                        btnMia.setAlfa(1);
                        btnMia.setTamanio(anchoBoton, altoBoton); //tamaño original
                        presentacionMia.desaparecer();
                        efectoClick.play(PantallaMenu.volumen);
                    }
                }
                else if(presentacionJavier.getEstado() == Presentacion.Estado.APARECIDO){
                    if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                        banderaBotonJavier = false;
                        btnJavier.setAlfa(1);
                        btnJavier.setTamanio(anchoBoton,altoBoton); //tamaño orginal
                        presentacionJavier.desaparecer();
                        efectoClick.play(PantallaMenu.volumen);
                    }
                }
                else if(presentacionNuri.getEstado() == Presentacion.Estado.APARECIDO){
                    if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                        banderaBotonNuri = false;
                        btnNuri.setAlfa(1);
                        btnNuri.setTamanio(anchoBoton, altoBoton); //tamaño orginal
                        presentacionNuri.desaparecer();
                        efectoClick.play(PantallaMenu.volumen);
                    }
                }
                else if(presentacionFer.getEstado() == Presentacion.Estado.APARECIDO){
                    if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                        banderaBotonFer = false;
                        btnFer.setAlfa(1);
                        btnFer.setTamanio(anchoBoton,altoBoton); //tamaño orginal
                        presentacionFer.desaparecer();
                        efectoClick.play(PantallaMenu.volumen);
                    }
                }
                return true;    // Indica que ya procesó el evento
        }

        //Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            // Preguntar si las coordenadas son de cierto lugar de donde se quito el dedo
            if (btnIrvin.contiene(x, y) &&  this.banderaBotonIrvin) {
                presentacionIrvin.aparecer();
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido

            }
            else if (btnMia.contiene(x, y) && this.banderaBotonMia) {
                presentacionMia.aparecer();
                efectoClick.play(PantallaMenu.volumen);

            }
            else if (btnJavier.contiene(x, y) && this.banderaBotonJavier ) {
                presentacionJavier.aparecer();
                efectoClick.play(PantallaMenu.volumen);
            }
            else if (btnNuri.contiene(x, y) && this.banderaBotonNuri ) {
                presentacionNuri.aparecer();
                efectoClick.play(PantallaMenu.volumen);

            }
            else if (btnFer.contiene(x, y) && this.banderaBotonFer ) {
                presentacionFer.aparecer();
                efectoClick.play(PantallaMenu.volumen);

            }
            else if (btnRegresar.contiene(x, y) && this.banderaBotonRegresar ) {
                efectoClick.play(PantallaMenu.volumen);
                principal.setScreen(new PantallaMenu(principal,true)); //se manda true porque se esta escuchando la cancion, es decir, ya hay un objeto cancion reproduciendose..
            }
            else{ //entonces el usuario despego el dedo de la pantalla en otra parte que no sean los botones... entonces cancelo su selecciona
                // se le quita la transparencia y se regresa a su tamaño original de los botones

                banderaBotonIrvin = false;
                btnIrvin.setAlfa(1);
                btnIrvin.setTamanio(anchoBoton, altoBoton); //tamaño original
                banderaBotonMia = false;
                btnMia.setAlfa(1);
                btnMia.setTamanio(anchoBoton, altoBoton); //tamaño original
                banderaBotonJavier = false;
                btnJavier.setAlfa(1);
                btnJavier.setTamanio(anchoBoton,altoBoton); //tamaño orginal
                banderaBotonNuri = false;
                btnNuri.setAlfa(1);
                btnNuri.setTamanio(anchoBoton, altoBoton); //tamaño orginal
                banderaBotonFer = false;
                btnFer.setAlfa(1);
                btnFer.setTamanio(anchoBoton,altoBoton); //tamaño orginal
                banderaBotonRegresar = false;
                btnRegresar.setAlfa(1);
                btnRegresar.setTamanio(anchoBoton-30,altoBoton-30); //tamaño orginal

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

    @Override
    public void dispose() {
        //Eliminar basura
        principal.dispose();
        batch.dispose();

        //texturas
        texturaBtnIrvin.dispose();
        texturaBtnMia.dispose();
        texturaBtnJavier.dispose();
        texturaBtnNuri.dispose();
        texturaBtnFer.dispose();
        texturaPresentacionIrvin.dispose();
        texturaPresentacionMia.dispose();
        texturaPresentacionJavier.dispose();
        texturaPresentacionNuri.dispose();
        texturaPresentacionFer.dispose();
        texturaRegresar.dispose();
        texturaFondo.dispose();
        //texturaAbanico.dispose();
        efectoClick.dispose();

    }


}