package mx.itesm.ninjanofukushuu;


import com.badlogic.gdx.Game;


/*
Desarrollador: Irvin Emmanuel Trujillo Díaz
Descripcion: clase representa un objeto juego.
Profesor: Roberto Martinez Román.
*/


public class Principal extends Game  {

    public static final float ANCHO_MUNDO = 1280;
    public static final float ALTO_MUNDO = 720;


    @Override
    public void create () {

        setScreen(new PantallaMenu(this)); //Se manda el objeto...
    }


}