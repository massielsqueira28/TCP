/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author massiel
 */
import java.net.Socket;
import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import javax.sound.midi.Patch;
import javax.swing.JOptionPane;

public class Cliente_True {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        String IpServe;
        String NameFile;
        BufferedInputStream ReadFile;
        BufferedOutputStream WriteFile;
        String MensajeServe;
        String MensajeClient;

        IpServe = JOptionPane.showInputDialog("****Ingresa la ip del del server****");

        try {
            Socket Connect = new Socket(IpServe, 1099);
            DataInputStream InputCliente = new DataInputStream(Connect.getInputStream());
            DataOutputStream OupoutServe = new DataOutputStream(Connect.getOutputStream());

            while (true) {
                MensajeClient = InputCliente.readUTF();
                JOptionPane.showMessageDialog(null, "Servidor dice: " + MensajeClient);

                NameFile = JOptionPane.showInputDialog("Ingresa el nombre del archivo a descargar");
                MensajeServe = NameFile;
                OupoutServe.writeUTF(MensajeServe);

                MensajeClient = InputCliente.readUTF();

                //ReadFile = new BufferedInputStream(Connect.getInputStream());
                //WriteFile = new BufferedOutputStream(new FileOutputStream(NameFile));

                /*int inicioFuente = 0;
                int ByteWiteSZecuence;*/
                byte[] Bufer;

                if (MensajeClient.equals("El archivo no existe")) {
                    JOptionPane.showMessageDialog(null, "El archivo ya no existe en el server");
                } else {
                    JOptionPane.showMessageDialog(null, MensajeClient);
                    
                    int TamByte = InputCliente.readInt();
                    int CantByte = InputCliente.readInt();
                    JOptionPane.showMessageDialog(null,"Tamaño archivo: "+TamByte);
                    Bufer = new byte[TamByte];
                    
                    InputCliente.readFully(Bufer,0, Bufer.length);
                    
                    NameFile = InputCliente.readUTF();
                    /*File f = new File(NameFile);
                    
                    
                    if(f.exists())
                    {
                        String Hora = ZonedDateTime.now().toString();
                        NameFile = Hora+NameFile;
                    }*/
                    
                    FileOutputStream SaveFile = new FileOutputStream(NameFile);
                    
                    SaveFile.write(Bufer);
                    SaveFile.close();
                    
                    JOptionPane.showMessageDialog(null, "Archivo Recivido");
                    int a = 0;
                    try {
                         a = Integer.parseInt(JOptionPane.showInputDialog("Desea detener la connecxio?\n1.:Cerrar\nCualquier otro dseguir."));
                    } 
                    catch (Exception e) 
                    {
                        a = 0;
                    }
                    

                    if (a == 1) {
                        MensajeServe = "closeconect";
                        OupoutServe.writeUTF(MensajeServe);
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, InputCliente.readUTF());
                    }
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Clente: " + e.getMessage());
        }

    }

}
