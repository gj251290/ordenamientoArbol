import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FrmOrdenamiento extends JFrame {

    private JButton btnOrdenarBurbuja;
    private JButton btnOrdenarRapido;
    private JButton btnOrdenarInsercion;
    private JToolBar tbOrdenamiento;
    @SuppressWarnings("rawtypes")
    private JComboBox cmbCriterio;
    private JTextField txtTiempo;
    private JButton btnBuscar;
    private JTextField txtBuscar;

    private JComboBox<String> cmbColumnaBusqueda;
    private JComboBox<String> cmbTipoBusqueda;
    private JButton btnLimpiar;

    private JTable tblDocumentos;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FrmOrdenamiento() {

        tbOrdenamiento = new JToolBar();
        btnOrdenarBurbuja = new JButton();
        btnOrdenarInsercion = new JButton();
        btnOrdenarRapido = new JButton();
        cmbCriterio = new JComboBox();
        txtTiempo = new JTextField();

        btnLimpiar = new JButton();
        btnBuscar = new JButton();
        txtBuscar = new JTextField();

        txtBuscar.setToolTipText("Texto a buscar");
        txtBuscar.setHorizontalAlignment(JTextField.CENTER);

        tblDocumentos = new JTable();

        setSize(800, 600);
        setTitle("Ordenamiento Documentos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        btnOrdenarBurbuja.setIcon(new ImageIcon(getClass().getResource("/iconos/burbuja.png")));
        btnOrdenarBurbuja.setToolTipText("Ordenar Burbuja");
        btnOrdenarBurbuja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarBurbujaClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarBurbuja);

        btnOrdenarRapido.setIcon(new ImageIcon(getClass().getResource("/iconos/rapido.png")));
        btnOrdenarRapido.setToolTipText("Ordenar Rapido");
        btnOrdenarRapido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarRapidoClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarRapido);

        btnOrdenarInsercion.setIcon(new ImageIcon(getClass().getResource("/iconos/insercion.png")));
        btnOrdenarInsercion.setToolTipText("Ordenar Inserción");
        btnOrdenarInsercion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarInsercionClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarInsercion);

        cmbCriterio.setModel(new DefaultComboBoxModel(
                new String[] { "Nombre Completo, Tipo de Documento", "Tipo de Documento, Nombre Completo" }));
        tbOrdenamiento.add(cmbCriterio);
        tbOrdenamiento.add(txtTiempo);

        cmbColumnaBusqueda = new JComboBox<>(new String[] { "Apellido", "Apellido 2", "Nombre", "Documento" });
        cmbColumnaBusqueda.setToolTipText("Filtrar por columna");
        tbOrdenamiento.add(cmbColumnaBusqueda);

        cmbTipoBusqueda = new JComboBox<>(new String[] { "Búsqueda Total", "Búsqueda Parcial" });
        cmbTipoBusqueda.setToolTipText("Seleccionar tipo de búsqueda");
        tbOrdenamiento.add(cmbTipoBusqueda);

        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/iconos/buscar.png")));
        btnBuscar.setToolTipText("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnBuscar(evt);
            }
        });

        btnLimpiar.setIcon(new ImageIcon(getClass().getResource("/iconos/limpiar.png")));
        btnLimpiar.setToolTipText("Limpiar Búsqueda");
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnLimpiarClick(evt);
            }
        });

        tbOrdenamiento.add(txtBuscar);
        tbOrdenamiento.add(btnBuscar);
        tbOrdenamiento.add(btnLimpiar);

        getContentPane().add(tbOrdenamiento, BorderLayout.NORTH);

        JScrollPane spDocumentos = new JScrollPane(tblDocumentos);
        getContentPane().add(spDocumentos, BorderLayout.CENTER);

        String nombreArchivo = System.getProperty("user.dir")
                + "/src/datos/Datos.csv";
        Documento.obtenerDatosDesdeArchivo(nombreArchivo);
        Documento.mostrarDatos(tblDocumentos);

    }

    private void btnOrdenarBurbujaClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            Util.iniciarCronometro();
            Documento.ordenarBurbuja(cmbCriterio.getSelectedIndex());
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            Documento.mostrarDatos(tblDocumentos);
        }
    }

    private void btnOrdenarRapidoClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            Util.iniciarCronometro();
            Documento.ordenarRapido(0, Documento.documentos.size() - 1, cmbCriterio.getSelectedIndex());
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            Documento.mostrarDatos(tblDocumentos);
        }
    }

    private void btnOrdenarInsercionClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            Util.iniciarCronometro();
            Documento.ordenarInsercion(cmbCriterio.getSelectedIndex());
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            Documento.mostrarDatos(tblDocumentos);
        }
    }

    private void btnBuscar(ActionEvent evt) {
        String valorBusqueda = txtBuscar.getText();
        int columnaBusqueda = cmbColumnaBusqueda.getSelectedIndex();
        boolean esBusquedaParcial = cmbTipoBusqueda.getSelectedItem().equals("Búsqueda Parcial");

        if (cmbCriterio.getSelectedIndex() >= 0 && !valorBusqueda.isEmpty()) {
            ArbolBinario arbolBinario = Documento.getArbolBinario(columnaBusqueda);
            List<Documento> resultados;

            if (esBusquedaParcial) {
                resultados = arbolBinario.buscarNodoParcial(valorBusqueda, columnaBusqueda);
            } else {
                Nodo resultado = arbolBinario.buscarNodoTotal(valorBusqueda, columnaBusqueda);
                resultados = (resultado != null) ? List.of(resultado.getDocumento()) : new ArrayList<>();
            }

            mostrarResultados(resultados);
        }
    }

    private void mostrarResultados(List<Documento> resultados) {
        if (!resultados.isEmpty()) {
            String[][] datos = new String[resultados.size()][4];
            for (int i = 0; i < resultados.size(); i++) {
                datos[i][0] = resultados.get(i).getApellido1();
                datos[i][1] = resultados.get(i).getApellido2();
                datos[i][2] = resultados.get(i).getNombre();
                datos[i][3] = resultados.get(i).getDocumento();
            }
            tblDocumentos.setModel(new DefaultTableModel(datos, Documento.encabezados));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún resultado para el texto ingresado.",
                    "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void btnLimpiarClick(ActionEvent evt) {
        txtBuscar.setText("");
        Documento.mostrarDatos(tblDocumentos);
    }

}