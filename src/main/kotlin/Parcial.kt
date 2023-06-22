//Punto 1

class Programa(
    val titulo: String,
    val conductoresPrincipales: List<ConductorPrincipal>,
    var presupuestoBase: Double,
    val sponsor: Sponsor,
    var dia: String,
    val duracion: Int,
    val ultimasEmisiones: MutableList<Int>
    ){
    val NUM_EMISIONES = 5
    var CANT_CONDUCTORES_MAX = 3
    val MIN_RATING = 5

    fun setCantidadConductoresMaximo(nuevoMax: Int){
        CANT_CONDUCTORES_MAX = nuevoMax
    }
    //Usé take para tomar las ultimas 5 emisiones, ya que no me gustaba no permitir agregar mas de 5 rating al programa.
    fun getPromedioUltimasEmisiones(): Double = this.ultimasEmisiones.take(n=NUM_EMISIONES).average()
    fun cantidadConductores(): Int = this.conductoresPrincipales.size
}

//Para este caso (Restricciones), prefiero usar strategy, ya que el programa puede tener varios
// tipos de restricciones y se pueden combinar las mismas.
interface IRestriccion{
    fun restringir(): Boolean
}

class Restriccion(val programa: Programa){
    var tipoRestriccion: IRestriccion = RestingirRating(programa)
    fun seRestringe() = tipoRestriccion.restringir()
}

class RestingirRating(val programa: Programa):IRestriccion{

    override fun restringir(): Boolean = programa.getPromedioUltimasEmisiones() <= programa.MIN_RATING
}

class RestringirCantidadConductores(val programa: Programa):IRestriccion{
    override fun restringir(): Boolean = programa.cantidadConductores() > programa.CANT_CONDUCTORES_MAX
}

class RestringirConductoresPuntuales(val programa: Programa):IRestriccion{
    val conductoresPuntuales: MutableSet<ConductorPrincipal> = mutableSetOf(juanCarlosPerezLoizeau, marioMonteverde)

    override fun restringir(): Boolean = !encontrarConductoresPuntuales()

    fun encontrarConductoresPuntuales(): Boolean =
        programa.conductoresPrincipales.any { conductoresPuntuales.contains(it) }

    fun agregarConductorPuntual(conductorPuntual : ConductorPrincipal){
        conductoresPuntuales.add(conductorPuntual)
    }
    fun eliminarConductorPuntual(conductorPuntual : ConductorPrincipal){
        conductoresPuntuales.remove(conductorPuntual)
    }
}

class RestringirPresupuesto(val programa: Programa):IRestriccion{
    val PRESUPUESTO_MAX = 100000
    override fun restringir(): Boolean = programa.presupuestoBase > PRESUPUESTO_MAX
}

//TODO: Ver este despues, me hace ruido (entendí la consigna???)
class RestringirCombinado(val programa: Programa):IRestriccion{
    val TOPE_RATING = 5
    val MAX_CONDUCTORES = 3
    val CONDUCTOR_ESTRELLA = "Pinky"
    val PRESUPUESTO_MAX = 100000

    override fun restringir(): Boolean = this.primeraCondicion() && segundaCondicion()

    fun primeraCondicion() = programa.getPromedioUltimasEmisiones() <= TOPE_RATING || programa.CANT_CONDUCTORES_MAX < programa.conductoresPrincipales.size
    fun segundaCondicion() = programa.conductoresPrincipales.any { it.nombre === CONDUCTOR_ESTRELLA } && programa.presupuestoBase < PRESUPUESTO_MAX
}

class Sponsor(){}
val herbalife = Sponsor()

class ConductorPrincipal(val nombre: String){}
val juanCarlosPerezLoizeau = ConductorPrincipal(nombre = "Juan Carlos Perez Loizeau")
val marioMonteverde = ConductorPrincipal(nombre = "Mario Monteverde")
val pinky = ConductorPrincipal(nombre = "Pinky")
val homero = ConductorPrincipal(nombre = "Homero")

//Punto 2
//Para esta parte me pareció mejor usar un observer
abstract class Accion(val programa: Programa) {
    abstract fun ejecutarAccion()
}

class DividirPrograma(programa: Programa): Accion(programa){
    val mitadConductores = programa.cantidadConductores()/2
    val primerosActores: List<ConductorPrincipal> = programa.conductoresPrincipales.take(n= mitadConductores)
    val ultimosActores: List<ConductorPrincipal> = programa.conductoresPrincipales.takeLast(n=mitadConductores)

    fun primerNombre(): String = "${programa.titulo.take(n = 1)} en el aire!"
    fun segundoNombre(): String = if(programa.titulo.contains(" ")) programa.titulo.takeLast(n = 1) else "Programa Sin Nombre"

    override fun ejecutarAccion() {
        Programa(primerNombre(), primerosActores, programa.presupuestoBase/2, herbalife, programa.dia, programa.duracion/2, mutableListOf(4,4,5,10,12))
        Programa(segundoNombre(), ultimosActores, programa.presupuestoBase/2, herbalife, programa.dia, programa.duracion/2, mutableListOf(5,5,5,10,5))
    }
}

class ProgamarLosSimpsons(programa: Programa): Accion(programa){
    override fun ejecutarAccion() {
        Programa("Los Simpsons", mutableListOf(homero), 20000.00, herbalife, programa.dia, programa.duracion, mutableListOf(6,5,7,10,4))
    }
}

class FusionarPrograma(programa: Programa): Accion(programa){
    override fun ejecutarAccion() {
        TODO("Not yet implemented")
    }
}

class MoverPrograma(programa: Programa): Accion(programa){
    override fun ejecutarAccion() {
        programa.dia = "MARTES"
    }

}

//Punto 3
object Encargado{
    val programas: MutableSet<Programa> = mutableSetOf()
    val restricciones: MutableSet<Restriccion> = mutableSetOf()
    val acciones: MutableSet<Accion> = mutableSetOf()

    fun restriccionesACumplir() = restricciones.none { it.seRestringe() }
}


