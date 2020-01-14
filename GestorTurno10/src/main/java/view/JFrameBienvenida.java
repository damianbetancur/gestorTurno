package view;

import controller.LoginController;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase JFrameBienvenida
 *
 * @author Ariel
 */
public class JFrameBienvenida extends javax.swing.JFrame {

    private int auxiliar = 0;
    private boolean realizado = false;

    hilo ejecutar = new hilo();

    public JFrameBienvenida() {
        initComponents();
        JFrameBienvenida.this.getRootPane().setOpaque(false);
        JFrameBienvenida.this.getContentPane().setBackground(new Color(0, 0, 0, 0));
        JFrameBienvenida.this.setBackground(new Color(0, 0, 0, 0));
        this.setResizable(false);
        this.setLocationRelativeTo(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        text = new javax.swing.JLabel();
        barra = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        text.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        text.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(text, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 380, 30));
        getContentPane().add(barra, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 400, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/background.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        if (realizado == false) {
            realizado = true;
            barra.setMaximum(49);
            barra.setMinimum(0);
            barra.setStringPainted(true);
            ejecutar.start();
        }
    }//GEN-LAST:event_formWindowActivated

    /**
     * hijo de Ejecucion
     */
    private class hilo extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    auxiliar++;
                    barra.setValue(auxiliar);
                    repaint();

                    switch (auxiliar) {
                        case 3:
                            text.setText("Empleado Verificado"+LoginController.getInstanceUsuario().getNombre());

                            break;
                        case 20:
                            text.setText("Cargando programa...");

                            break;
                        case 50:
                            text.setText("Acceso Autorizado");
                            break;
                        case 60:
                            
                            JFramePrincipal sistema = new JFramePrincipal();                           
                            sistema.arranca();
                            JFrameBienvenida.this.dispose();
                            break;

                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(JFrameBienvenida.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barra;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel text;
    // End of variables declaration//GEN-END:variables
}