import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import java.util.HashMap;
public class JavaServer {
  
	  public static CalculatorHandler handler;
	  public static FileStore.Processor processor;
	  public static int port;
	  
	  public static void main(String [] args) {
    try { 
    	
    	   if (args.length != 1) {
    		      System.out.println("Please enter only port number");
    		      System.exit(0);
    		    }

    	
        handler = new CalculatorHandler();
        processor = new FileStore.Processor(handler);
//cmd
        port= Integer.valueOf(args[0]);
        
        Runnable simple = new Runnable() {
            public void run() {
              simple(processor);
            }
          };      
          new Thread(simple).start();
          
          
    } catch (Exception x) {
      x.printStackTrace();
    }
  }
	    public static void simple(FileStore.Processor processor) {
	        try {
	        	
	          TServerTransport serverTransport = new TServerSocket(25000);
	          TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
	          System.out.println("Starting the server...");
	         
	          server.serve();
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
	  
  }
