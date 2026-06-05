import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GestionObra {

    // ─────────────────────────────────────────────
    // PARÁMETROS ECONÓMICOS  IVA | Inflación | Utilidad
    // ─────────────────────────────────────────────
    static String[][] parametros = new String[1][3];

    // ─────────────────────────────────────────────
    // TABLAS DE CATÁLOGO  (ID | Descripción | Unidad | PrecioUnitario)
    // ─────────────────────────────────────────────
    static String[][] catalogoMateriales = {
        {"1","Cemento Tolteca Bulto 50kg",  "Bulto 50kg",   "185.00"},
        {"2","Varilla 3/8",                  "Tonelada",     "23500.00"},
        {"3","Arena de Río",                 "Metro Cúbico", "420.00"},
        {"4","Grava Triturada",              "Metro Cúbico", "480.00"},
        {"5","Ladrillo Rojo",                "Millar",       "3200.00"},
        {"6","Alambre Recocido",             "Kilogramo",    "45.00"},
        {"7","Clavos de 3 pulg",             "Kilogramo",    "38.00"},
        {"8","Yeso Constructor Bulto 40kg",  "Bulto 40kg",   "120.00"},
        {"9","Pintura Vinílica Cubeta 19L",  "Cubeta 19L",   "1450.00"},
        {"10","Impermeabilizante Cubeta 19L","Cubeta 19L",   "1850.00"}
    };

    static String[][] catalogoManoObra = {
        {"1","Oficial Albañil",       "Jornal","450.00"},
        {"2","Peón de Albañilería",   "Jornal","280.00"},
        {"3","Oficial Carpintero",    "Jornal","480.00"},
        {"4","Oficial Fierrero",      "Jornal","480.00"},
        {"5","Oficial Electricista",  "Jornal","520.00"},
        {"6","Oficial Plomero",       "Jornal","500.00"},
        {"7","Oficial Pintor",        "Jornal","420.00"},
        {"8","Ayudante General",      "Jornal","300.00"},
        {"9","Cabo de Oficios",       "Jornal","600.00"},
        {"10","Maestro de Obra",      "Jornal","800.00"}
    };

    static String[][] catalogoHerramienta = {
        {"1","Mezcladora Concreto",   "Día","350.00"},
        {"2","Andamio Tubular",       "Día","45.00"},
        {"3","Rotomartillo SDS",      "Día","180.00"},
        {"4","Vibrador de Concreto",  "Día","220.00"},
        {"5","Cortadora de Azulejo",  "Día","130.00"},
        {"6","Compactadora",          "Día","400.00"},
        {"7","Extensión Uso Rudo",    "Día","25.00"},
        {"8","Escalera Aluminio",     "Día","35.00"},
        {"9","Carretilla y Pala",     "Día","20.00"},
        {"10","Arnés de Seguridad",   "Día","15.00"}
    };

    static String[][] catalogoServicios = {
        {"1","Flete de Materiales",    "Viaje",    "1200.00"},
        {"2","Planos Arquitectón.",    "Proyecto", "8500.00"},
        {"3","Dictamen Estructural",   "Dictamen", "5000.00"},
        {"4","Retiro de Escombro",     "Viaje",    "950.00"},
        {"5","Proyecto Hidrosanit.",   "Proyecto", "4500.00"},
        {"6","Toma de Agua Temp.",     "Trámite",  "2100.00"},
        {"7","Licencia Construcción",  "Trámite",  "6800.00"},
        {"8","Mecánica de Suelos",     "Estudio",  "7500.00"},
        {"9","Supervisión Externa",    "Mes",      "12000.00"},
        {"10","Limpieza de Obra",      "Servicio", "3000.00"}
    };

    // ─────────────────────────────────────────────
    // PRESUPUESTO  (Descripción | Unidad | PrecioU | Cantidad | Importe)
    // Se reservan 10 filas por rubro
    // ─────────────────────────────────────────────
    static String[][] presMateriales   = new String[10][5];
    static String[][] presManoObra     = new String[10][5];
    static String[][] presHerramienta  = new String[10][5];
    static String[][] presServicios    = new String[10][5];

    // ═══════════════════════════════════════════
    //  FUNCIONES AUXILIARES DE ENTRADA, VALIDACIÓN Y FORMATEO
    // ═══════════════════════════════════════════

    public static String RellenarEspacios(String dato, int tamano) {
        return String.format("%1$-" + tamano + "s", dato);
    }

    public static String Dialogo(String texto) throws IOException {
        System.out.print(texto);
        BufferedReader lectura = new BufferedReader(new InputStreamReader(System.in));
        return lectura.readLine();
    }

    public static String Leer(String texto) throws IOException {
        String cadena = Dialogo(texto);
        if (cadena != null) {
            cadena = cadena.trim();
            if (cadena.isEmpty()) cadena = null;
        }
        return cadena;
    }

    public static boolean EsNumeroEntero(String dato) {
        if (dato == null || dato.isEmpty()) return false;
        for (char c : dato.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    public static boolean EsNumeroDouble(String dato) {
        if (dato == null || dato.isEmpty()) return false;
        boolean punto = false;
        for (char c : dato.toCharArray()) {
            if (!Character.isDigit(c)) {
                if (c == '.' && !punto) punto = true;
                else return false;
            }
        }
        return true;
    }

    public static int ObtenerUltimaPosicion(String[][] matriz) {
        int ultimaPosicion = -1;
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][0] != null && !matriz[i][0].isEmpty()) {
                ultimaPosicion = i;
            }
        }
        return ultimaPosicion;
    }

    //═══════════════════════════════════════════
    // OBTENER FECHA ACTUAL EN FORMATO dd/MM/yyyy
    //═══════════════════════════════════════════
    public static String ObtenerFechaActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(new Date());
    }

    // ═══════════════════════════════════════════
    //   CONFIGURACIÓN DE PARÁMETROS ECONÓMICOS
    // ═══════════════════════════════════════════

    public static void AgregarParametros() throws IOException {
        System.out.println("==================================================");
        System.out.println("   CONFIGURACIÓN DE PARÁMETROS ECONÓMICOS         ");
        System.out.println("==================================================");
        String iva, inflacion, utilidad;

        do {
            iva = Leer("Introduce porcentaje de IVA (Ej. 16): ");
        } while (!EsNumeroEntero(iva) && !EsNumeroDouble(iva));

        do {
            inflacion = Leer("Introduce el porcentaje de inflación (Ej. 5): ");
        } while (!EsNumeroEntero(inflacion) && !EsNumeroDouble(inflacion));

        do {
            utilidad = Leer("Introduce el margen de ganancia deseado (Ej. 20): ");
        } while (!EsNumeroEntero(utilidad) && !EsNumeroDouble(utilidad));

        parametros[0][0] = iva;
        parametros[0][1] = inflacion;
        parametros[0][2] = utilidad;
        System.out.println();
    }

    // ═══════════════════════════════════════════
    //   MOSTRAR CATÁLOGO INDEXADO
    // ═══════════════════════════════════════════

    public static void MostrarCatalogo(String titulo, String[][] catalogo) {
        System.out.println("--------------------------------------------------");
        System.out.println("CATÁLOGO DISPONIBLE: " + titulo);
        System.out.println("--------------------------------------------------");
        System.out.printf("%-5s %-30s %-18s %s%n", "ID", "Descripción", "Unidad", "Precio Unitario");
        for (String[] fila : catalogo) {
            System.out.printf("[%-2s] %-30s %-18s $%s%n",
                fila[0], fila[1], fila[2], fila[3]);
        }
        System.out.println("--------------------------------------------------");
    }

    // ═══════════════════════════════════════════
    //   BUSCAR EN CATÁLOGO POR ID
    // ═══════════════════════════════════════════

    public static int BuscarEnCatalogo(String id, String[][] catalogo) {
        for (int i = 0; i < catalogo.length; i++) {
            if (catalogo[i][0].equals(id.trim())) return i;
        }
        return -1;
    }

    // ═══════════════════════════════════════════
    //   CAPTURA GENÉRICA DE UN CONCEPTO AL PRESUPUESTO
    // ═══════════════════════════════════════════

    public static void CapturarConcepto(String titulo, String[][] catalogo,
                                         String[][] presupuesto) throws IOException {
        MostrarCatalogo(titulo, catalogo);

        String id;
        int pos = -1;
        do {
            id = Leer("Introduce el ID/Código de " + titulo + " a agregar [1..10]: ");
            if (id != null && EsNumeroEntero(id)) {
                pos = BuscarEnCatalogo(id, catalogo);
                if (pos == -1) System.out.println("  ID no existe, intenta de nuevo.");
            } else {
                System.out.println("  Dato inválido, intenta de nuevo.");
                pos = -1;
            }
        } while (pos == -1);

        String descripcion = catalogo[pos][1];
        String unidad      = catalogo[pos][2];
        String precioU     = catalogo[pos][3];

        String cantidad;
        do {
            cantidad = Leer("Introduce la cantidad requerida para " + descripcion + ": ");
            if (!EsNumeroEntero(cantidad) && !EsNumeroDouble(cantidad)) {
                System.out.println("  Cantidad inválida, intenta de nuevo.");
                cantidad = null;
            }
        } while (cantidad == null);

        double importe = Double.parseDouble(precioU) * Double.parseDouble(cantidad);
        String importeStr = String.format("%.2f", importe);

        // Insertar en la siguiente posición libre del presupuesto del rubro
        int siguiente = ObtenerUltimaPosicion(presupuesto) + 1;
        if (siguiente < presupuesto.length) {
            presupuesto[siguiente][0] = descripcion;
            presupuesto[siguiente][1] = unidad;
            presupuesto[siguiente][2] = precioU;
            presupuesto[siguiente][3] = cantidad;
            presupuesto[siguiente][4] = importeStr;
            System.out.println("¡" + titulo + " agregado exitosamente a la tabla de presupuestos!");
        } else {
            System.out.println("  El presupuesto de " + titulo + " está lleno (máx. 10 conceptos).");
        }
        System.out.println();
    }

    // ═══════════════════════════════════════════
    //   MOSTRAR DESGLOSE DE UN RUBRO
    // ═══════════════════════════════════════════

    public static double MostrarDesglose(String tituloRubro, String[][] presupuesto) {
        double totalRubro = 0;
        int ultimo = ObtenerUltimaPosicion(presupuesto);

        System.out.println("DESGLOSE DEL RUBRO: " + tituloRubro);
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("%-30s %-18s %-12s %-10s %s%n",
            "Descripción", "Unidad", "Precio U.", "Cantidad", "Importe");
        System.out.println("----------------------------------------------------------------------");

        if (ultimo >= 0) {
            for (int i = 0; i <= ultimo; i++) {
                double importe = Double.parseDouble(presupuesto[i][4]);
                totalRubro += importe;
                System.out.printf("%-30s %-18s $%-11s %-10s $%s%n",
                    presupuesto[i][0],
                    presupuesto[i][1],
                    presupuesto[i][2],
                    presupuesto[i][3],
                    presupuesto[i][4]);
            }
        } else {
            System.out.println("  (Sin conceptos capturados)");
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.printf("TOTAL %-35s ---------> $%.2f%n", tituloRubro + ":", totalRubro);
        System.out.println();
        return totalRubro;
    }

    // ═══════════════════════════════════════════
    //   TABLA DE PRESUPUESTO GLOBAL (Opción 5)
    // ═══════════════════════════════════════════

    public static void MostrarPresupuestoFinal() {
        System.out.println("=====================================================================");
        System.out.println("              TABLA DE PRESUPUESTO DE OBRA FINAL                     ");
        System.out.println("           Fecha de generación: " + ObtenerFechaActual() + "         ");
        System.out.println("=====================================================================");

        double tMat  = MostrarDesglose("MATERIALES",          presMateriales);
        double tMano = MostrarDesglose("MANO DE OBRA",        presManoObra);
        double tHerr = MostrarDesglose("HERRAMIENTA Y EQUIPO",presHerramienta);
        double tServ = MostrarDesglose("SERVICIOS",           presServicios);

        double subtotal  = tMat + tMano + tHerr + tServ;
        double porcIva   = Double.parseDouble(parametros[0][0]) / 100.0;
        double porcInfl  = Double.parseDouble(parametros[0][1]) / 100.0;
        double porcUtil  = Double.parseDouble(parametros[0][2]) / 100.0;

        double montoIva      = subtotal * porcIva;
        double subtotalIva   = subtotal + montoIva;
        double totalGlobal   = subtotalIva * (1 + porcInfl) * (1 + porcUtil);

        System.out.println("=====================================================================");
        System.out.println("                   CÓMPUTO GLOBAL DE COSTOS                          ");
        System.out.println("=====================================================================");
        System.out.printf("Subtotal de la Obra (Costo Directo):      --------> $%,.2f%n", subtotal);
        System.out.printf("Importe del IVA Aplicado (%.0f%%):           --------> $%,.2f%n",
            Double.parseDouble(parametros[0][0]), montoIva);
        System.out.printf("Subtotal Acumulado con IVA:                --------> $%,.2f%n", subtotalIva);
        System.out.printf("Total Global (Con Inflación y Utilidad):   --------> $%,.2f%n", totalGlobal);
        System.out.println("=====================================================================");
        System.out.println();
    }

    // ═══════════════════════════════════════════
    //   MENÚ PRINCIPAL
    // ═══════════════════════════════════════════

    public static void MenuPrincipal() throws IOException {
        String opcion;
        do {
            System.out.println("==================================================");
            System.out.println("|        SISTEMA GESTOR DE PRESUPUESTOS DE OBRA  |");
            System.out.println("==================================================");
            System.out.println(" 1.- Materiales");
            System.out.println(" 2.- Mano de Obra");
            System.out.println(" 3.- Herramienta y Equipo");
            System.out.println(" 4.- Servicios");
            System.out.println(" 5.- Ver Presupuesto Final");
            System.out.println(" 6.- Salir");
            System.out.println("==================================================");

            opcion = Leer("¿Qué opción deseas? ");

            if (opcion == null) {
                System.out.println("Opción incorrecta.");
                opcion = "0";
            } else {
                switch (opcion) {
                    case "1":
                        CapturarConcepto("MATERIALES", catalogoMateriales, presMateriales);
                        break;
                    case "2":
                        CapturarConcepto("MANO DE OBRA", catalogoManoObra, presManoObra);
                        break;
                    case "3":
                        CapturarConcepto("HERRAMIENTA Y EQUIPO", catalogoHerramienta, presHerramienta);
                        break;
                    case "4":
                        CapturarConcepto("SERVICIOS", catalogoServicios, presServicios);
                        break;
                    case "5":
                        MostrarPresupuestoFinal();
                        break;
                    case "6":
                        System.out.println("Salida del Sistema.");
                        break;
                    default:
                        System.out.println("No existe esa opción.");
                        break;
                }
            }
        } while (!opcion.equals("6"));
    }

    // ═══════════════════════════════════════════
    //  PUNTO DE ENTRADA AL PROGRAMA  
    // ═══════════════════════════════════════════

    public static void main(String[] args) throws IOException {
        AgregarParametros();
        MenuPrincipal();
    }
}
