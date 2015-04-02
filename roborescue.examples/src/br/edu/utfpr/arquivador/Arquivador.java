package br.edu.utfpr.arquivador;

import java.io.*;

/**
 * Código isnpirado em http://javapractices.com/topic/TopicAction.do?Id=57
 * @author Maycow Meira
 */
public class Arquivador {
    
   
    //create a Serializable List
//    List<String> quarks = Arrays.asList(
//      "up", "down", "strange", "charm", "top", "bottom"
//    );
    public static void serializar(String nomeArquivo, Serializable object){
        //serialize
        //note the use of abstract base class references

        try{
            //use buffering 
            OutputStream file = new FileOutputStream(nomeArquivo);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try{
                output.writeObject(object);
            }
            finally{
                output.close();
            }
        }  
        catch(IOException ex){
            System.out.println("Cannot perform output.");
        }
    }
    

    //deserialize the quarks.ser file
    //note the use of abstract base class references
    public static Serializable desserializar(String nomeArquivo){
        try{
            //use buffering
            InputStream file = new FileInputStream(nomeArquivo);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
            try{
              //deserialize the List
              Serializable object = (Serializable)input.readObject();
              //display its data
              return object;
            }
            finally{
              input.close();
            }
          }
          catch(ClassNotFoundException ex){
              System.out.println("Cannot perform input. Class not found.");
              
              return null;
          }
          catch(IOException ex){
              System.out.println("Cannot perform input.");
              
              return null;
          }
    }
}
