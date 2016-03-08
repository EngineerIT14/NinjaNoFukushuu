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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
    // Botón saltar
    private Texture texturaSalto;
    private Boton btnSalto;

    //tamañoBotones
    private static int TAMANIO_BOTON = 69;


    //ITEMS

    //Scrolls/Pergamino
    private Array<ObjetosJuego> scroll;
    private Texture texturaScroll;

    //Enemigos
    private Array<ObjetosJuego> enemigoN1;
    private Texture texturaEN1;

    //Templo
    private Array<ObjetosJuego> templos; //son en total 3 templos
    private Texture texturaTemplo;

    //Pociones
    private Array<ObjetosJuego> pociones;
    private Texture texturaPocion;

    //HUD, MARCADORES DE VIDA Y PERGAMINOS..

    //VIDAS QUE SE MUESTRAN EN EL HUD.

    private Array<ObjetosJuego> vidas;
    private Texture texturaVidas;

    //Marcadores
    private int marcadorPergaminos;
    private int marcadorVidas;
    private Texto textoMarcadorPergaminos; //Texto para mostrar el marcador de vidas y marcador de pergaminos.
    private Texto textoMarcadorVidas;

    // Estados del juego
    private EstadosJuego estadoJuego;

    //Efectos del juego
    private Sound efectoSaltoHataku, efectoTomarVida, efectoTomarPergamino;




    //Variable para indicar si numero de nivel y hacer el cambio en el numero de vidas, ya que hataku tiene su armadura completa en el nivel 4 y las vidas deben aumentar a 5, esta variable es nuestra bandera...
    private boolean flag = false;
    private int numeroNivel = 1;




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



        this.cargarRecursos();
        this.crearObjetos();
        this.cargarAudio();


        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        estadoJuego = EstadosJuego.JUGANDO;
    }

    private void cargarAudio() {
        this.efectoSaltoHataku = Gdx.audio.newSound(Gdx.files.internal("efectoSaltoHataku.wav"));
        this.efectoSaltoHataku.setVolume(70,70);
        this.efectoTomarVida = Gdx.audio.newSound(Gdx.files.internal("efectoVida.wav"));
        this.efectoTomarVida.setVolume(70,70);
        this.efectoTomarPergamino = Gdx.audio.newSound(Gdx.files.internal("efectoPergamino.wav"));
        this.efectoTomarPergamino.setVolume(70,70);



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
        assetManager.load("salto.png", Texture.class);

        // Se bloquea hasta que cargue todos los recursos
        assetManager.finishLoading();
        //Textura Objetos que estan en la pantalla
        this.texturaScroll = new Texture(Gdx.files.internal("scroll.png"));
        this.texturaPocion = new Texture(Gdx.files.internal("pocion.png"));
        this.texturaEN1=new Texture(Gdx.files.internal("TierraE.png"));
        this.texturaTemplo = new Texture(Gdx.files.internal("TemploVerde.png"));

        //****************************************************************//
        //nota: se debe cosniderar que la imagen de vidas va cambiar cuando el ninja obtenga una parte de la armadura, recomiendo usar un switch y usar una bandera (boolean) cuando se pase el nivel y deppendiendo de la bandera cargar el archivo de imagenn correspondiente
        //Por ahora no lo implemento ya que estamos trabajando en el primer nivel.
        texturaVidas = new Texture(Gdx.files.internal("life1.png"));
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
        btnIzquierda.setPosicion(TAM_CELDA*2,TAM_CELDA/5);
        btnIzquierda.setAlfa(0.7f); // Un poco de transparencia
        btnIzquierda.setTamanio(PantallaJuego.TAMANIO_BOTON + 20, PantallaJuego.TAMANIO_BOTON);

        texturaBtnDerecha = assetManager.get("derecha.png");
        btnDerecha = new Boton(texturaBtnDerecha);
        btnDerecha.setPosicion(TAM_CELDA*8, TAM_CELDA/5);
        btnDerecha.setAlfa(0.7f); // Un poco de transparencia
        btnDerecha.setTamanio(PantallaJuego.TAMANIO_BOTON + 20, PantallaJuego.TAMANIO_BOTON);

        texturaSalto = assetManager.get("salto.png"); //boton para saltar... carga su imagen
        btnSalto = new Boton(texturaSalto);
        btnSalto.setPosicion(Principal.ANCHO_MUNDO-7 * TAM_CELDA, 100 + TAM_CELDA);
        btnSalto.setAlfa(0.7f);
        btnSalto.setTamanio(PantallaJuego.TAMANIO_BOTON,PantallaJuego.TAMANIO_BOTON);



        //Se crean objetos que son textos que se muestran en el HUD.
        this.textoMarcadorVidas = new Texto(0.1f * Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO * 0.96f);
        this.textoMarcadorPergaminos= new Texto(0.8f * Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO * 0.96f); //mandamos la posicion que queremos por default.


        //Lista scrolles: en todos los niveles solo hay 3 scroll
        this.scroll = new Array<ObjetosJuego>(3);
        for (int i = 0; i<3;i++) {
            ObjetosJuego nuevo = new ObjetosJuego(this.texturaScroll);
            //nuevo.setTamanio(20,50); //Irvin ya ajusto el tamaño en photoshop..
            this.scroll.add(nuevo);
        }

        //Posiciones pergamino nivel tierra
        this.scroll.get(0).setPosicion(50,310); //pergamino de en medio...
        this.scroll.get(1).setPosicion(750, 53); //pergamino de hasta abajo
        this.scroll.get(2).setPosicion(630, 85); //pergamino que está en precipicio

        //Pociones: En todos los niveles solo hay 2 pociones.
        this.pociones = new Array<ObjetosJuego>(2);
        for(int i =0; i< 2;i++) {
            ObjetosJuego nuevo = new ObjetosJuego(this.texturaPocion);
           // nuevo.setTamanio(50,50); //Irvin ya ajusto el tamaño de las imagenes en photoshop
            this.pociones.add(nuevo);
        }

        //Se colocan las pociones en el lugar correspondiente,
        this.pociones.get(0).setPosicion(950, Principal.ALTO_MUNDO / 2 -70);
        this.pociones.get(1).setPosicion(650, 230);

        //Enemigos: 5 enemigos en el primer nivel
        this.enemigoN1= new Array<ObjetosJuego>(5);
        for(int i =0; i< 5;i++) {
            ObjetosJuego nuevo = new ObjetosJuego(this.texturaEN1);
            nuevo.setTamanio(60,90);
            this.enemigoN1.add(nuevo);
        }

        //Se colocan los enemigos en su lugar correspondiente, en el nivel de TIERRA
        this.enemigoN1.get(0).setPosicion(900, 515); //samurai parte superior
        this.enemigoN1.get(1).setPosicion(430, 275);  //Samurai centro
        this.enemigoN1.get(2).setPosicion(790, 308); //Samurai Escalon
        this.enemigoN1.get(3).setPosicion(960, 135); //Samurai escalon
        this.enemigoN1.get(4).setPosicion(450, 515); //Samurai parte superio

        //Aqui se piensa poner un switch evaluando una variable de nivel,  de eso va dependar donde se va colocar el templo
        //templos, son 3.
        this.templos= new Array<ObjetosJuego>(3);
        for(int i =0; i< 3;i++) {
            ObjetosJuego nuevo = new ObjetosJuego(this.texturaTemplo);
            nuevo.setTamanio(60, 90);
            this.templos.add(nuevo);
        }

        this.templos.get(0).setPosicion(230, 540); //temploTierra


        //Objetos que representan las vidas, son las caras del ninja que estan en el HUD

        //Vidas: El numero de vidas cambia cuando se tiene la armadura completa, por lo que. al llegar all ultimo de nivel se debe de moficiar la variable globa de vidas, aumentando la vidas del persoonaje a 5.
        if(this.numeroNivel == 4){ //El nivel 4 es donde hataku enfrenta al jefe final, por lo que, sus vidas aumentan...
            this.flag = true;
        }
        else{
            this.flag = false; //no estoy en el nivel 4.
        }

        //Si es el nivel 4 se deben de poner 5 vidas
        if(this.flag) {
            this.vidas =  new Array<ObjetosJuego>(5);
            for(int i = 0; i<5; i++) {
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





        }
        else{ //entonces no estoy en el nivel 4, se deben de poner 3 vidas.
            this.vidas =  new Array<ObjetosJuego>(3);
            for(int i = 0; i<3; i++) {
                ObjetosJuego nuevo = new ObjetosJuego(this.texturaVidas);
                //nuevo.setTamanio(70,70); //Irvin ya ajusto el tamaño de las vidas en photoshop..
                this.vidas.add(nuevo);
            }

            this.vidas.get(0).setPosicion(this.textoMarcadorVidas.getX()+100,this.textoMarcadorVidas.getY()-50);
            this.vidas.get(1).setPosicion(this.textoMarcadorVidas.getX()+170,this.textoMarcadorVidas.getY()-50);
            this.vidas.get(2).setPosicion(this.textoMarcadorVidas.getX()+240,this.textoMarcadorVidas.getY()-50);


        }




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

        for (ObjetosJuego Enemigo : enemigoN1) {
            if (Enemigo.actualizar())
                Enemigo.render(batch);
        }

        //Dibujar iconos vidas
        for (ObjetosJuego vida : this.vidas) {
            if (vida.actualizar()) {
                vida.render(batch);
            }
        }

        this.templos.get(0).render(batch);//temploTierra


        // Mostrar pergaminos
        this.textoMarcadorPergaminos.mostrarMensaje(batch, "Pergaminos: " + this.marcadorPergaminos);

        // Mostrar vida
        this.textoMarcadorVidas.mostrarMensaje(batch, "Vida: " + this.marcadorVidas);

        batch.end();
        //Dibuja el HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        batch.begin();
        btnIzquierda.render(batch);
        btnDerecha.render(batch);
        btnSalto.render(batch);

        batch.end();

    }

    private void recogerObjeto() {
        //Recogerscrolls al tocarlos
        for (ObjetosJuego scrolls : scroll) {
            if(hataku.getSprite().getX()>= scrolls.getSprite().getX() && hataku.getSprite().getX()<= scrolls.getSprite().getX() + scrolls.getSprite().getWidth()
                    && hataku.getSprite().getY() >= scrolls.getSprite().getY() && hataku.getSprite().getY() <= scrolls.getSprite().getHeight() + scrolls.getSprite().getY()){
                if(scrolls.getEstado() != ObjetosJuego.Estado.DESAPARECIDO) {
                    this.marcadorPergaminos++;
                    this.efectoTomarPergamino.play(); //suena efecto
                    scrolls.quitarElemento();
                }
                break;
            }
        }
        //Recoger pociones al tocarlas
        for (ObjetosJuego pocion : pociones) {
            if(hataku.getSprite().getX()>= pocion.getSprite().getX() && hataku.getSprite().getX()<= pocion.getSprite().getX() + pocion.getSprite().getWidth()
                    && hataku.getSprite().getY() >= pocion.getSprite().getY() && hataku.getSprite().getY() <= pocion.getSprite().getHeight() + pocion.getSprite().getY()){
                if(vidas.size<3) {
                    if (pocion.getEstado() != ObjetosJuego.Estado.DESAPARECIDO) {
                        this.marcadorVidas++;
                        this.efectoTomarVida.play(); //suena efecto
                        ObjetosJuego nuevo = new ObjetosJuego(this.texturaVidas);
                        nuevo.setTamanio(80,80);
                        this.vidas.add(nuevo);
                        nuevo.setPosicion(vidas.get(vidas.size-2).getSprite().getX()+70,this.textoMarcadorVidas.getY()-50);
                        pocion.quitarElemento();
                    }
                    break;
                }
            }
        }

        //mata enemigos al toque
        for (ObjetosJuego Enemigo : enemigoN1) {
            if(hataku.getSprite().getX()>= Enemigo.getSprite().getX() && hataku.getSprite().getX()<= Enemigo.getSprite().getX() + Enemigo.getSprite().getWidth()
                    && hataku.getSprite().getY() >= Enemigo.getSprite().getY() && hataku.getSprite().getY() <= Enemigo.getSprite().getHeight() + Enemigo.getSprite().getY()){
                if (Enemigo.getEstado() != ObjetosJuego.Estado.DESAPARECIDO) {
                    this.marcadorVidas--;
                    Enemigo.quitarElemento();
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
                hataku.actualizar();
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
                px- Personaje.VELOCIDAD_Y;
        int celdaX = (int)(px/TAM_CELDA);   // Casilla del personaje en X
        if (hataku.getEstadoMovimiento()== Personaje.EstadoMovimiento.MOV_DERECHA) {
            celdaX++;   // Casilla del lado derecho
        }
        int celdaY = (int)(hataku.getY()/TAM_CELDA); // Casilla del personaje en Y
        TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(1);
        if ( capaPlataforma.getCell(celdaX,celdaY) != null ) {
            // Colisionará, dejamos de moverlo
            hataku.setEstado(Personaje.EstadoMovimiento.QUIETO);
        } else {
            hataku.actualizar();
        }
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
        this.plataforma.dispose();
        this.batch.dispose();
        this.mapa.dispose();

        this.rendererMapa.dispose();
        //texturas
        this.texturaHataku.dispose();
        this.texturaBtnDerecha.dispose();
        this.texturaBtnIzquierda.dispose();
        this.texturaSalto.dispose();
        this.texturaVidas.dispose();
        this.texturaPocion.dispose();
        this.texturaScroll.dispose();

        this.texturaEN1.dispose();
        this.texturaTemplo.dispose();

        //sonidos
        this.efectoSaltoHataku.dispose();
        this.efectoTomarVida.dispose();
        this.efectoTomarPergamino.dispose();


        // texturaVidas.dispose(); //EN DUDA SI SE DEBE DE ELIMIAR, YA QUE SI QUEREMOS QUE ,LAS VIDAS ACTUALES SIGAN EM EL SIGUIENTE NIVEL, LAS CARAS DEBEN DE SER LAS MISMAS A LAS VIDAS.. , si quisieramos que las vidas regresen a 3 al pasar el nivel, entonces si se deben de elimar la textura de vidas.
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
            if (estadoJuego==EstadosJuego.JUGANDO) {
                // Preguntar si las coordenadas están sobre el botón derecho
                if (btnDerecha.contiene(x, y) && hataku.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    // Tocó el botón derecha, hacer que el personaje se mueva a la derecha
                    hataku.setEstado(Personaje.EstadoMovimiento.MOV_DERECHA);
                } else if (btnIzquierda.contiene(x, y) && hataku.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    // Tocó el botón izquierda, hacer que el personaje se mueva a la izquierda
                    hataku.setEstado(Personaje.EstadoMovimiento.MOV_IZQUIERDA);
                } else if (btnSalto.contiene(x, y)) {
                    // Tocó el botón saltar
                    hataku.saltar();
                    efectoSaltoHataku.play();
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
            if ( hataku.getEstadoMovimiento()!= Personaje.EstadoMovimiento.INICIANDO && (btnDerecha.contiene(x, y) || btnIzquierda.contiene(x,y)) ) {
                // Tocó el botón derecha, hacer que el personaje se mueva a la derecha
                hataku.setEstado(Personaje.EstadoMovimiento.QUIETO);
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

    public enum EstadosJuego {
        GANO,
        JUGANDO,
        PAUSADO,
        PERDIO
    }
}
