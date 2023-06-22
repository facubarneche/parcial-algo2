//Punto 1

class Programa(
    val titulo: String,
    val conductoresPrincipales: MutableSet<ConductorPrincipal>,
    var presupuestoBase: Double,
    val sponsor: Sponsor,
    val dia: String,
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
    override fun restringir(): Boolean = programa.conductoresPrincipales.size > programa.CANT_CONDUCTORES_MAX
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
class ConductorPrincipal(val nombre: String){}
val juanCarlosPerezLoizeau = ConductorPrincipal(nombre = "Juan Carlos Perez Loizeau")
val marioMonteverde = ConductorPrincipal(nombre = "Mario Monteverde")
val pinky = ConductorPrincipal(nombre = "Pinky")

//Punto 2



//Punto 3
object Encargado{
    val programas: MutableSet<Programa> = mutableSetOf()
    val restricciones: MutableSet<Restriccion> = mutableSetOf()

    fun restriccionesACumplir() = restricciones.none { it.seRestringe() }
}


