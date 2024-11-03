import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ArbolBinario {

    private Nodo raiz;
    private int criterio;

    public ArbolBinario(Nodo raiz) {
        this.raiz = raiz;
    }

    public int getCriterio() {
        return criterio;
    }

    public void setCriterio(int criterio) {
        this.criterio = criterio;
    }

    public ArbolBinario() {
    }

    public Nodo getRaiz() {
        return raiz;
    }

    public void agregarNodo(Nodo n) {
        if (raiz == null) {
            raiz = n;
            return;
        }

        Nodo actual = raiz;
        Nodo padre;
        while (true) {
            padre = actual;
            String valorActual = obtenerValorPorCriterio(actual.getDocumento(), criterio).toUpperCase();
            String valorNuevo = obtenerValorPorCriterio(n.getDocumento(), criterio).toUpperCase();

            if (valorNuevo.equals(valorActual)) {
                actual = actual.derecho;
                if (actual == null) {
                    padre.derecho = n;
                    return;
                }
            } else if (valorNuevo.compareTo(valorActual) > 0) {
                actual = actual.derecho;
                if (actual == null) {
                    padre.derecho = n;
                    return;
                }
            } else {
                actual = actual.izquierdo;
                if (actual == null) {
                    padre.izquierdo = n;
                    return;
                }
            }
        }
    }

    public void mostrar(JTable tbl) {
        String[][] datos = null;
        if (raiz != null) {
            datos = new String[Documento.documentos.size()][Documento.encabezados.length];

            Nodo n = raiz;
            Nodo padre;
            int fila = -1;
            while (n != null) {
                if (n.izquierdo == null) {
                    fila++;
                    datos[fila][0] = n.getDocumento().getApellido1();
                    datos[fila][1] = n.getDocumento().getApellido2();
                    datos[fila][2] = n.getDocumento().getNombre();
                    datos[fila][3] = n.getDocumento().getDocumento();
                    n = n.derecho;
                } else {
                    padre = n.izquierdo;
                    while (padre.derecho != null && padre.derecho != n) {
                        padre = padre.derecho;
                    }
                    if (padre.derecho == null) {
                        padre.derecho = n;
                        n = n.izquierdo;
                    } else {
                        padre.derecho = null;
                        fila++;
                        datos[fila][0] = n.getDocumento().getApellido1();
                        datos[fila][1] = n.getDocumento().getApellido2();
                        datos[fila][2] = n.getDocumento().getNombre();
                        datos[fila][3] = n.getDocumento().getDocumento();
                        n = n.derecho;
                    }
                }
            }
        }
        DefaultTableModel dtm = new DefaultTableModel(datos, Documento.encabezados);
        tbl.setModel(dtm);
    }

    public Nodo buscarNodoTotal(String valorBusqueda, int columnaBusqueda) {
        Nodo actual = raiz;
        String valorBusquedaUpper = valorBusqueda.toUpperCase();

        while (actual != null) {
            String valorActual = obtenerValorPorCriterio(actual.getDocumento(), columnaBusqueda).toUpperCase();

            if (valorActual.equals(valorBusquedaUpper)) {
                return actual;
            } else if (valorActual.compareTo(valorBusquedaUpper) > 0) {
                actual = actual.izquierdo;
            } else {
                actual = actual.derecho;
            }
        }
        return null;
    }

    public List<Documento> buscarNodoParcial(String valorBusqueda, int columnaBusqueda) {
        List<Documento> resultados = new ArrayList<>();
        String valorBusquedaUpper = valorBusqueda.toUpperCase();
        buscarParcialRecursivo(raiz, valorBusquedaUpper, columnaBusqueda, resultados);
        return resultados;
    }

    private void buscarParcialRecursivo(Nodo actual, String valorBusqueda, int columnaBusqueda,
            List<Documento> resultados) {
        if (actual == null) {
            return;
        }

        buscarParcialRecursivo(actual.izquierdo, valorBusqueda, columnaBusqueda, resultados);

        String valorActual = obtenerValorPorCriterio(actual.getDocumento(), columnaBusqueda).toUpperCase();
        if (valorActual.contains(valorBusqueda)) {
            resultados.add(actual.getDocumento());
        }

        buscarParcialRecursivo(actual.derecho, valorBusqueda, columnaBusqueda, resultados);
    }

    private String obtenerValorPorCriterio(Documento documento, int criterio) {
        return switch (criterio) {
            case 0 -> documento.getApellido1();
            case 1 -> documento.getApellido2();
            case 2 -> documento.getNombre();
            case 3 -> documento.getDocumento();
            default -> documento.getDocumento();
        };
    }
}
