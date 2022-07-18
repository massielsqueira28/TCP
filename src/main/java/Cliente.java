/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author massiel
 */
import java.net.ServerSocket;
import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException{
        
        Socket Connect = null;
        String Ruta = "/home/Documentos/";
        
        String NameFileRecive;
        File Directory = new File(Ruta);
        ServerSocket Server = null;
        BufferedInputStream LecturaByteFile;
        BufferedOutputStream EnviarClienteFile;
        try
        {
            Server = new ServerSocket(1099);
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Error en el Server Socket: "+ e.getMessage());
        }
        if(!Directory.exists())
        {
            JOptionPane.showMessageDialog(null,"    Silo encunetra");
            
            try
            {
                Directory.mkdir();
            
            }catch(Exception e)
            {
                JOptionPane.showMessageDialog(null,"Error al crear directorioi "+ e.getMessage());
            }
        }
        
        try
        {
            while(true)
            {
                byte[]Bufer;

                Connect = Server.accept();
                JOptionPane.showMessageDialog(null,"****Se hizo una conexion de : "+ Connect.getInetAddress() + "****");
                
                
                EnviarClienteFile = new BufferedOutputStream(Connect.getOutputStream());
                
                DataOutputStream EscribirEnclientte = new DataOutputStream(Connect.getOutputStream());
                DataInputStream LeerCliente = new DataInputStream(Connect.getInputStream());
                
                EscribirEnclientte.writeUTF("Conexion Aceptada");
                
                NameFileRecive = LeerCliente.readUTF();
                
                Ruta = Ruta+"/"+NameFileRecive;
                File ArchivoEnviar = new File(Ruta);
                LecturaByteFile = new BufferedInputStream(new FileInputStream(ArchivoEnviar));
                
                int inicioFuente = 0;
                int ByteWiteSZecuence;
                Bufer = new byte[1024];
                
                if(!ArchivoEnviar.exists())
                {
                    EscribirEnclientte.writeUTF("El archivo no existe");
                }
                else
                {
                    EscribirEnclientte.writeUTF("Enviando archvo...");
                    while ((ByteWiteSZecuence = LecturaByteFile.read(Bufer)) != -1) 
                    {
                        EnviarClienteFile.write(Bufer, inicioFuente,ByteWiteSZecuence);
                        
                        
                    }
                    LecturaByteFile.close();
                    EnviarClienteFile.close();
                }
                
                //Se espera que el cliente envie una cadena diciendo s desea terminar la conexion
                String CadenaCerrar = LeerCliente.readUTF();
                
                if(CadenaCerrar.equals("closeconect"))
                {
                    EscribirEnclientte.writeUTF("Conexion cerrada");
                    
                    /* Cerrando la conexion */
                    break;
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"En conexcion");
                }
                
               
                
                
                
                
                

            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Mensaje de error: " +e.getMessage());
        }
        
        
    }
    
}
