import java.util.*;

public class PGIRC {

    // =====================================================
    // 1. ÁRBOL BINARIO DE BÚSQUEDA
    // =====================================================

    static class Nodo {
        int valor;
        Nodo izquierdo;
        Nodo derecho;

        Nodo(int valor) {
            this.valor = valor;
        }
    }

    static class ArbolBinarioBusqueda {

        Nodo raiz;

        // Insertar un valor
        public void insertar(int valor) {
            raiz = insertarRecursivo(raiz, valor);
        }

        private Nodo insertarRecursivo(Nodo nodo, int valor) {

            if (nodo == null) {
                return new Nodo(valor);
            }

            if (valor < nodo.valor) {
                nodo.izquierdo = insertarRecursivo(nodo.izquierdo, valor);
            } else if (valor > nodo.valor) {
                nodo.derecho = insertarRecursivo(nodo.derecho, valor);
            }

            return nodo;
        }

        // Comprobar si existe un valor sin mostrar comparaciones
        public boolean existe(int valor) {

            Nodo actual = raiz;

            while (actual != null) {

                if (valor == actual.valor) {
                    return true;
                }

                if (valor < actual.valor) {
                    actual = actual.izquierdo;
                } else {
                    actual = actual.derecho;
                }
            }

            return false;
        }

        // Buscar mostrando las comparaciones realizadas
        public boolean buscar(int valor) {

            Nodo actual = raiz;

            while (actual != null) {

                System.out.println(
                        "Comparando " + valor + " con " + actual.valor
                );

                if (valor == actual.valor) {
                    return true;
                }

                if (valor < actual.valor) {
                    actual = actual.izquierdo;
                } else {
                    actual = actual.derecho;
                }
            }

            return false;
        }

        // Recorrido Inorden
        public void inorden(Nodo nodo) {

            if (nodo != null) {
                inorden(nodo.izquierdo);
                System.out.print(nodo.valor + " ");
                inorden(nodo.derecho);
            }
        }

        // Recorrido Preorden
        public void preorden(Nodo nodo) {

            if (nodo != null) {
                System.out.print(nodo.valor + " ");
                preorden(nodo.izquierdo);
                preorden(nodo.derecho);
            }
        }

        // Recorrido Postorden
        public void postorden(Nodo nodo) {

            if (nodo != null) {
                postorden(nodo.izquierdo);
                postorden(nodo.derecho);
                System.out.print(nodo.valor + " ");
            }
        }

        // Eliminar un nodo
        public void eliminar(int valor) {
            raiz = eliminarRecursivo(raiz, valor);
        }

        private Nodo eliminarRecursivo(Nodo nodo, int valor) {

            if (nodo == null) {
                return null;
            }

            if (valor < nodo.valor) {

                nodo.izquierdo =
                        eliminarRecursivo(nodo.izquierdo, valor);

            } else if (valor > nodo.valor) {

                nodo.derecho =
                        eliminarRecursivo(nodo.derecho, valor);

            } else {

                // Caso 1: nodo hoja
                if (nodo.izquierdo == null &&
                        nodo.derecho == null) {

                    return null;
                }

                // Caso 2: solo tiene hijo derecho
                if (nodo.izquierdo == null) {
                    return nodo.derecho;
                }

                // Caso 2: solo tiene hijo izquierdo
                if (nodo.derecho == null) {
                    return nodo.izquierdo;
                }

                // Caso 3: tiene dos hijos
                int sucesor =
                        encontrarMinimo(nodo.derecho);

                nodo.valor = sucesor;

                nodo.derecho =
                        eliminarRecursivo(
                                nodo.derecho,
                                sucesor
                        );
            }

            return nodo;
        }

        private int encontrarMinimo(Nodo nodo) {

            while (nodo.izquierdo != null) {
                nodo = nodo.izquierdo;
            }

            return nodo.valor;
        }
    }


    // =====================================================
    // 2. GRAFO
    // =====================================================

    static class Arista {

        String destino;
        int distancia;
        int tiempo;
        boolean bloqueada;

        Arista(
                String destino,
                int distancia,
                int tiempo
        ) {

            this.destino = destino;
            this.distancia = distancia;
            this.tiempo = tiempo;
            this.bloqueada = false;
        }
    }


    static class Estado {

        String ciudad;
        int costo;

        Estado(String ciudad, int costo) {
            this.ciudad = ciudad;
            this.costo = costo;
        }
    }


    static class Grafo {

        Map<String, List<Arista>> adyacencia =
                new LinkedHashMap<>();


        public void agregarCiudad(String ciudad) {

            adyacencia.putIfAbsent(
                    ciudad,
                    new ArrayList<>()
            );
        }


        public void agregarRuta(
                String origen,
                String destino,
                int distancia,
                int tiempo
        ) {

            agregarCiudad(origen);
            agregarCiudad(destino);

            adyacencia.get(origen)
                    .add(
                            new Arista(
                                    destino,
                                    distancia,
                                    tiempo
                            )
                    );
        }


        public void bloquearRuta(
                String origen,
                String destino
        ) {

            for (Arista arista :
                    adyacencia.getOrDefault(
                            origen,
                            new ArrayList<>()
                    )) {

                if (arista.destino.equals(destino)) {
                    arista.bloqueada = true;
                }
            }
        }


        // Mostrar lista de adyacencia
        public void mostrarListaAdyacencia() {

            System.out.println(
                    "\n===== LISTA DE ADYACENCIA ====="
            );

            for (String origen : adyacencia.keySet()) {

                System.out.print(origen + " -> ");

                List<Arista> rutas =
                        adyacencia.get(origen);

                for (int i = 0; i < rutas.size(); i++) {

                    Arista arista = rutas.get(i);

                    System.out.print(
                            arista.destino
                                    + " ("
                                    + arista.distancia
                                    + " km, "
                                    + arista.tiempo
                                    + " min"
                    );

                    if (arista.bloqueada) {
                        System.out.print(
                                ", BLOQUEADA"
                        );
                    }

                    System.out.print(")");

                    if (i < rutas.size() - 1) {
                        System.out.print(", ");
                    }
                }

                System.out.println();
            }
        }


        // Algoritmo de Dijkstra
        public void dijkstra(
                String origen,
                String destino,
                boolean usarTiempo
        ) {

            Map<String, Integer> costos =
                    new HashMap<>();

            Map<String, String> anterior =
                    new HashMap<>();

            for (String ciudad :
                    adyacencia.keySet()) {

                costos.put(
                        ciudad,
                        Integer.MAX_VALUE
                );
            }

            costos.put(origen, 0);

            PriorityQueue<Estado> cola =
                    new PriorityQueue<>(
                            Comparator.comparingInt(
                                    estado -> estado.costo
                            )
                    );

            cola.add(
                    new Estado(origen, 0)
            );

            while (!cola.isEmpty()) {

                Estado estadoActual =
                        cola.poll();

                String actual =
                        estadoActual.ciudad;

                // Ignorar estados antiguos de la cola
                if (estadoActual.costo >
                        costos.get(actual)) {

                    continue;
                }

                for (Arista arista :
                        adyacencia.getOrDefault(
                                actual,
                                new ArrayList<>()
                        )) {

                    // No utilizar rutas bloqueadas
                    if (arista.bloqueada) {
                        continue;
                    }

                    int peso;

                    if (usarTiempo) {
                        peso = arista.tiempo;
                    } else {
                        peso = arista.distancia;
                    }

                    int nuevoCosto =
                            estadoActual.costo
                                    + peso;

                    if (nuevoCosto <
                            costos.get(
                                    arista.destino
                            )) {

                        costos.put(
                                arista.destino,
                                nuevoCosto
                        );

                        anterior.put(
                                arista.destino,
                                actual
                        );

                        cola.add(
                                new Estado(
                                        arista.destino,
                                        nuevoCosto
                                )
                        );
                    }
                }
            }


            if (costos.get(destino) ==
                    Integer.MAX_VALUE) {

                System.out.println(
                        "No existe una ruta disponible."
                );

                return;
            }


            // Reconstruir camino
            List<String> ruta =
                    new ArrayList<>();

            String actual = destino;

            while (actual != null) {

                ruta.add(actual);

                if (actual.equals(origen)) {
                    break;
                }

                actual =
                        anterior.get(actual);
            }

            Collections.reverse(ruta);


            System.out.println(
                    "Ruta: "
                            + String.join(
                                    " -> ",
                                    ruta
                            )
            );

            if (usarTiempo) {

                System.out.println(
                        "Tiempo total: "
                                + costos.get(destino)
                                + " minutos"
                );

            } else {

                System.out.println(
                        "Distancia total: "
                                + costos.get(destino)
                                + " km"
                );
            }
        }
    }


    // =====================================================
    // 3. CREACIÓN DE DATOS INICIALES
    // =====================================================

    public static ArbolBinarioBusqueda crearArbolInicial() {

        ArbolBinarioBusqueda arbol =
                new ArbolBinarioBusqueda();

        int[] codigos = {
                50, 30, 70,
                20, 40, 60,
                80, 10, 25,
                35, 45, 65,
                90
        };

        for (int codigo : codigos) {
            arbol.insertar(codigo);
        }

        return arbol;
    }


    public static Grafo crearGrafoInicial() {

        Grafo grafo =
                new Grafo();

        grafo.agregarRuta(
                "A", "B", 5, 10
        );

        grafo.agregarRuta(
                "A", "C", 10, 15
        );

        grafo.agregarRuta(
                "B", "C", 3, 5
        );

        grafo.agregarRuta(
                "B", "D", 7, 12
        );

        grafo.agregarRuta(
                "C", "D", 1, 3
        );

        grafo.agregarRuta(
                "C", "E", 8, 10
        );

        grafo.agregarRuta(
                "D", "E", 2, 4
        );

        grafo.agregarRuta(
                "E", "B", 6, 8
        );

        // Restricción solicitada en la actividad
        grafo.bloquearRuta(
                "C",
                "E"
        );

        return grafo;
    }


    // =====================================================
    // 4. MENÚ DEL ABB
    // =====================================================

    public static void menuABB(
            Scanner scanner,
            ArbolBinarioBusqueda arbol
    ) {

        int opcion;

        do {

            System.out.println(
                    "\n================================"
            );

            System.out.println(
                    " ÁRBOL BINARIO DE BÚSQUEDA"
            );

            System.out.println(
                    "================================"
            );

            System.out.println(
                    "1. Recorrido Inorden"
            );

            System.out.println(
                    "2. Recorrido Preorden"
            );

            System.out.println(
                    "3. Recorrido Postorden"
            );

            System.out.println(
                    "4. Buscar código"
            );

            System.out.println(
                    "5. Insertar código"
            );

            System.out.println(
                    "6. Eliminar nodo"
            );

            System.out.println(
                    "0. Volver"
            );

            System.out.print(
                    "\nSeleccione una opción: "
            );

            opcion =
                    leerEntero(scanner);


            switch (opcion) {

                case 1:

                    System.out.print(
                            "\nInorden: "
                    );

                    arbol.inorden(
                            arbol.raiz
                    );

                    System.out.println();

                    break;


                case 2:

                    System.out.print(
                            "\nPreorden: "
                    );

                    arbol.preorden(
                            arbol.raiz
                    );

                    System.out.println();

                    break;


                case 3:

                    System.out.print(
                            "\nPostorden: "
                    );

                    arbol.postorden(
                            arbol.raiz
                    );

                    System.out.println();

                    break;


                case 4:

                    System.out.print(
                            "\nIngrese el código que desea buscar: "
                    );

                    int codigoBuscar =
                            leerEntero(scanner);

                    boolean encontrado =
                            arbol.buscar(
                                    codigoBuscar
                            );

                    if (encontrado) {

                        System.out.println(
                                "Código encontrado."
                        );

                    } else {

                        System.out.println(
                                "Código no encontrado."
                        );
                    }

                    break;


                case 5:

                    System.out.print(
                            "\nIngrese el código que desea insertar: "
                    );

                    int codigoInsertar =
                            leerEntero(scanner);

                    if (arbol.existe(
                            codigoInsertar)) {

                        System.out.println(
                                "El código ya existe."
                        );

                    } else {

                        arbol.insertar(
                                codigoInsertar
                        );

                        System.out.println(
                                "Código insertado correctamente."
                        );
                    }

                    break;


                case 6:

                    System.out.print(
                            "\nIngrese el nodo que desea eliminar: "
                    );

                    int nodoEliminar =
                            leerEntero(scanner);

                    if (arbol.existe(
                            nodoEliminar)) {

                        arbol.eliminar(
                                nodoEliminar
                        );

                        System.out.println(
                                "Nodo eliminado correctamente."
                        );

                        System.out.print(
                                "Árbol actual en Inorden: "
                        );

                        arbol.inorden(
                                arbol.raiz
                        );

                        System.out.println();

                    } else {

                        System.out.println(
                                "El nodo no existe."
                        );
                    }

                    break;


                case 0:
                    break;


                default:

                    System.out.println(
                            "Opción no válida."
                    );
            }

        } while (opcion != 0);
    }


    // =====================================================
    // 5. MENÚ DEL GRAFO
    // =====================================================

    public static void menuGrafo(
            Scanner scanner,
            Grafo grafo
    ) {

        int opcion;

        do {

            System.out.println(
                    "\n================================"
            );

            System.out.println(
                    "         GRAFO DE RUTAS"
            );

            System.out.println(
                    "================================"
            );

            System.out.println(
                    "1. Minimizar distancia"
            );

            System.out.println(
                    "2. Minimizar tiempo"
            );

            System.out.println(
                    "3. Mostrar lista de adyacencia"
            );

            System.out.println(
                    "0. Volver"
            );

            System.out.print(
                    "\nSeleccione una opción: "
            );

            opcion =
                    leerEntero(scanner);


            switch (opcion) {

                case 1:

                    System.out.println(
                            "\nRuta mínima por distancia:"
                    );

                    grafo.dijkstra(
                            "A",
                            "E",
                            false
                    );

                    break;


                case 2:

                    System.out.println(
                            "\nRuta mínima por tiempo:"
                    );

                    grafo.dijkstra(
                            "A",
                            "E",
                            true
                    );

                    break;


                case 3:

                    grafo.mostrarListaAdyacencia();

                    break;


                case 0:
                    break;


                default:

                    System.out.println(
                            "Opción no válida."
                    );
            }

        } while (opcion != 0);
    }


    // =====================================================
    // 6. CAMINO CRÍTICO
    // =====================================================

    public static void caminoCritico() {

        /*
         * A = 4
         * B depende de A = 3
         * C depende de A = 2
         * D depende de B = 5
         * E depende de C y D = 6
         */

        int A = 4;

        int B =
                A + 3;

        int C =
                A + 2;

        int D =
                B + 5;

        int inicioE =
                Math.max(
                        C,
                        D
                );

        int E =
                inicioE + 6;


        System.out.println(
                "\n===== CAMINO CRÍTICO ====="
        );

        System.out.println(
                "Camino crítico: "
                        + "A -> B -> D -> E"
        );

        System.out.println(
                "Duración total: "
                        + E
                        + " unidades de tiempo"
        );
    }


    // =====================================================
    // 7. VALIDACIÓN DE ENTRADA
    // =====================================================

    public static int leerEntero(
            Scanner scanner
    ) {

        while (!scanner.hasNextInt()) {

            System.out.println(
                    "Entrada no válida. "
                            + "Ingrese un número."
            );

            scanner.next();

            System.out.print(
                    "Seleccione una opción: "
            );
        }

        return scanner.nextInt();
    }


    // =====================================================
    // 8. PROGRAMA PRINCIPAL - MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        Scanner scanner =
                new Scanner(System.in);

        ArbolBinarioBusqueda arbol =
                crearArbolInicial();

        Grafo grafo =
                crearGrafoInicial();

        int opcionPrincipal;


        do {

            System.out.println(
                    "\n================================"
            );

            System.out.println(
                    "             PGIRC"
            );

            System.out.println(
                    "================================"
            );

            System.out.println(
                    "1. Árbol Binario de Búsqueda"
            );

            System.out.println(
                    "2. Grafo de rutas"
            );

            System.out.println(
                    "3. Camino crítico"
            );

            System.out.println(
                    "0. Salir"
            );

            System.out.print(
                    "\nSeleccione una opción: "
            );


            opcionPrincipal =
                    leerEntero(scanner);


            switch (opcionPrincipal) {

                case 1:

                    menuABB(
                            scanner,
                            arbol
                    );

                    break;


                case 2:

                    menuGrafo(
                            scanner,
                            grafo
                    );

                    break;


                case 3:

                    caminoCritico();

                    break;


                case 0:

                    System.out.println(
                            "\nPrograma finalizado."
                    );

                    break;


                default:

                    System.out.println(
                            "\nOpción no válida."
                    );
            }

        } while (
                opcionPrincipal != 0
        );


        scanner.close();
    }
}
