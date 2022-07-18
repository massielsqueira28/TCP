/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.ServerSocket;
import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

/**
 *
 * @author massiel
 */
public class server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Socket Connect = null;

        File[] Lista = null;

        String NameFileRecive;

        String User = System.getProperty("user.name");
        String Ruta = "/home/" + User + "/Documentos";

        File Directory = new File(Ruta);

        ServerSocket Server = null;
        BufferedInputStream LecturaByteFile;
        BufferedOutputStream EnviarClienteFile;
        try {
            Server = new ServerSocket(1099);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en el Server Socket: ");
        }
        if (!Directory.exists()) {
            JOptionPane.showMessageDialog(null, "Creando el directorio: " + Directory.exists());

            try {
                Directory.mkdir();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al crear directorio " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Servidor listo para recivir peticiones");

            /*Se sacan los ficheros del directorio*/
            try {
                Lista = Directory.listFiles();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error  en la lista" + e.getMessage());
            }

        }

        try {

            while (true) {
                byte[] Bufer;

                Connect = Server.accept();
                JOptionPane.showMessageDialog(null, "****Se hizo una conexion con : " + Connect.getInetAddress() + "****");

                EnviarClienteFile = new BufferedOutputStream(Connect.getOutputStream());

                DataOutputStream EscribirEnclientte = new DataOutputStream(Connect.getOutputStream());
                DataInputStream LeerCliente = new DataInputStream(Connect.getInputStream());

                EscribirEnclientte.writeUTF("Conexion Aceptada");

                NameFileRecive = LeerCliente.readUTF();
                File ArchivoEnviar = null;
                if (Lista != null) {
                    for (File file : Lista) {

                        if (NameFileRecive.equals(file.getName())) {
                            ArchivoEnviar = file;
                        }

                    }
                }

                if (!ArchivoEnviar.exists() || ArchivoEnviar == null) {
                    EscribirEnclientte.writeUTF("El archivo no existe");
                } else {

                    FileInputStream LeeFile = new FileInputStream(ArchivoEnviar);

                    int TamByte = (int) ArchivoEnviar.length();
                    Bufer = new byte[TamByte];

                    LeeFile.read(Bufer);

                    EscribirEnclientte.writeUTF("Enviando archvo...");

                    EscribirEnclientte.writeInt(TamByte);
                    EscribirEnclientte.writeInt(Bufer.length);
                    EscribirEnclientte.write(Bufer);
                    EscribirEnclientte.writeUTF(ArchivoEnviar.getName());

                }

                //Se espera que el cliente envie una cadena diciendo s desea terminar la conexion
                String CadenaCerrar = LeerCliente.readUTF();

                if (CadenaCerrar.equals("closeconect")) {
                    EscribirEnclientte.writeUTF("Conexion cerrada");

                    /* Cerrando la conexion */
                    Connect.close();
                    Server.close();
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "En conexion");
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Mensaje de error server : " + e.getMessage());
        }

    }

}
