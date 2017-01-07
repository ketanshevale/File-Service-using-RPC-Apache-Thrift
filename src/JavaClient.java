import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.sql.Date;
import java.util.List;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;


import java.util.ArrayList;


import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
public class JavaClient {


	public static void main(String [] args) {

		   if ((args.length != 6) && (args.length != 8)) {
			      System.out.println("Please enter arguments, machine, port, operation, filename, user");
			      System.exit(0);
			    }

		
		TMemoryBuffer myBuffer = new TMemoryBuffer(7000);
		TJSONProtocol jProto = new TJSONProtocol(myBuffer);
  	    String myOperation = null, myFile="", myUser=null;
  	    
		int opBit=0, fileBit=0, userBit=0;
		for(String s : args){
			if(opBit==1){
				myOperation = s;
				opBit=0;
			}
			if(s.contains("--operation")){
				opBit =1;
			}
		}
		for(String s : args){
			if(userBit==1){
				myUser = s;
				userBit=0;
			}
			if(s.contains("--user")){
				userBit =1;
			}
		} 
		if(!(myOperation.equalsIgnoreCase("list"))){
		for(String s : args){
			if(fileBit==1){
				myFile = s;
				fileBit=0;
			}
			if(s.contains("--filename")){
				fileBit =1;
			}
		}
		}
	  

	   	  
    try {
      TTransport transport;
        transport = new TSocket(String.valueOf(args[0]), Integer.valueOf(args[1]));
        transport.open();
      TProtocol protocol = new  TBinaryProtocol(transport);
      FileStore.Client client = new FileStore.Client(protocol);     

      
		RFile r = new RFile();
		  RFileMetadata rm = new RFileMetadata();
		  java.util.Date date= new java.util.Date();
		  StatusReport report = new StatusReport();
		  if(myOperation.equalsIgnoreCase("write")){
		  Path path = Paths.get("./"+myFile);
		  byte[] bytesOfFile = Files.readAllBytes(path);
		  String strData = new String(bytesOfFile, "UTF-8");
		  byte[] bytesOfMessage = strData.getBytes("UTF-8");
		  MessageDigest md = MessageDigest.getInstance("MD5");
		  byte[] thedigest = md.digest(bytesOfMessage);
		  rm.filename = myFile; 
		  r.content= strData;
		  }
		  rm.owner	= String.valueOf(myUser);		  
		  r.meta=rm;
		  if(myOperation.equalsIgnoreCase("write")){
		  report= client.writeFile(r);
		  report.write(jProto);
		  myBuffer.close();
			byte[] readBytes = myBuffer.getArray();
			String output = new String(readBytes, 0, readBytes.length);
			System.out.println(output);
		  }
		  
		  RFile readObject=null;
		  rm.filename = myFile; 
		  if(myOperation.equalsIgnoreCase("read")){
		  readObject =client.readFile(rm.filename, rm.owner);		  		  
		  try{
		  if(readObject.meta != null){
			  readObject.write(jProto);
			  myBuffer.close();
				byte[] readBytes = myBuffer.getArray();
				String output = new String(readBytes, 0, readBytes.length);
				System.out.println(output);
		}else {
			  SystemException k = new SystemException();
			  k.message = "File not available";
			  k.write(jProto);
			  myBuffer.close();
				byte[] readBytes = myBuffer.getArray();
				String output = new String(readBytes, 0, readBytes.length);
				System.out.println(output);
		  }
		  }catch(TException e){
			  e.getMessage();
		  }
		  catch(Throwable e){
			  e.toString();
		  }
		}
		
		  List<RFileMetadata> userList= new ArrayList();
		  if(myOperation.equalsIgnoreCase("list")){		  
		  userList = client.listOwnedFiles(myUser);
		  if((userList != null) && (userList.size() == 0)){
			  SystemException k = new SystemException();
			  k.message = "User "+ myUser+" does not exists.";
			  k.write(jProto);
			  myBuffer.close();
				byte[] readBytes = myBuffer.getArray();
				String output = new String(readBytes, 0, readBytes.length);
				System.out.println(output);
		  }
		  else{
			  String output="";
			  for(RFileMetadata a: userList){
			  a.write(jProto);
			  myBuffer.close();
			  byte[] readBytes = myBuffer.getArray();
			  output = new String(readBytes, 0, readBytes.length);
			}
			  System.out.println(output);
		  }
		  }
	    	if(userList != null)
	    		userList.removeAll(userList);
	   
	      transport.close();
    } catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
	} catch (IOException e) {
		System.out.println("{\"1\":{\"str\":\"File does not exists. Please Enter valid file name.\"}}");
		//e.printStackTrace();
	} catch (TException x) {
      x.printStackTrace();
    }
    finally{
    }
  }
}