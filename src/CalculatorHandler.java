import org.apache.thrift.TException;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.transport.TIOStreamTransport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class CalculatorHandler implements FileStore.Iface{
	  private List<RFileMetadata> l;
	  public CalculatorHandler() {
		  l= new ArrayList<RFileMetadata>();
	  } 
	  
	  	StatusReport sr = new StatusReport();	  	
	  	RFileMetadata rMeta= new RFileMetadata();	
	  	RFile r = new RFile();
	  
	    
	    public StatusReport writeFile(RFile rFile) throws SystemException, org.apache.thrift.TException{
	    	
	    	sr.setStatus(Status.FAILED);
	    	//System.out.println("in write File");			 
	    	java.util.Date date= new java.util.Date();
	    	int flag=0;
	    	PrintWriter writer = null;
	    	try {
	    		if(l.size() > 0){
	    			//System.out.println("Size > 0");
			    for(RFileMetadata a : l){
			    	if((a.filename.equals(rFile.meta.filename) && (a.owner.equals(rFile.meta.owner)))){
			    	flag =1;
			    	a.updated = date.getTime();
	    			a.version += 1;
					writer = new PrintWriter(rFile.meta.filename, "UTF-8");
					writer.print("");
	    			writer.append(rFile.content);
	    			
	    			rFile.meta.contentLength=rFile.content.length();
	    			rFile.meta.created =  date.getTime();
	    			  byte[] bytesOfMessage = rFile.content.getBytes("UTF-8");
	    			  MessageDigest md = null;
					try {
						md = MessageDigest.getInstance("MD5");
					} catch (NoSuchAlgorithmException e) {e.printStackTrace();}
	    			  byte[] thedigest = md.digest(bytesOfMessage);
	    			  rFile.meta.contentHash = thedigest.toString();
	    			//System.out.println(a.version);
	    			//System.out.println(a.updated);
			    	}
			    }
			    	if(flag == 0){
			    		//System.out.println("size > 0 and first time");//First time
		    			//isFile = new File(rFile.meta.filename);
						writer = new PrintWriter(rFile.meta.filename, "UTF-8");
		    			writer.println(rFile.content);
		    			
		    			rFile.meta.contentLength=rFile.content.length();
		    			rFile.meta.created =  date.getTime();
		    			rFile.meta.updated =  date.getTime();
		    			rFile.meta.version = 0;
		    			  byte[] bytesOfMessage = rFile.content.getBytes("UTF-8");
		    			  MessageDigest md = null;
						try {
							md = MessageDigest.getInstance("MD5");
						} catch (NoSuchAlgorithmException e) {e.printStackTrace();}
		    			  byte[] thedigest = md.digest(bytesOfMessage);
		    			  
		    			  rFile.meta.contentHash = thedigest.toString();		    			  
		    	    	l.add(rFile.meta);
			    	}
	    	}else{//First time
	    		//System.out.println("size < 0 First in list");
    			//isFile = new File(rFile.meta.filename);
				writer = new PrintWriter(rFile.meta.filename, "UTF-8");
    			writer.println(rFile.content);
    			
    			rFile.meta.contentLength=rFile.content.length();
    			rFile.meta.created =  date.getTime();
    			rFile.meta.updated =  date.getTime();
    			rFile.meta.version = 0;
  			  byte[] bytesOfMessage = rFile.content.getBytes("UTF-8");
  			  MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {e.printStackTrace();}
  			  byte[] thedigest = md.digest(bytesOfMessage);
  			  rFile.meta.contentHash = thedigest.toString();
    	    	l.add(rFile.meta);
	    	}
	    
	    	    	writer.close();
	    	sr.setStatus(Status.SUCCESSFUL);
	    	} catch (IOException e) {
	    		sr.setStatus(Status.FAILED);
	    		e.printStackTrace();
	    	}
	    	return sr;
	    }
	    public RFile readFile(String filename, String owner) throws SystemException, org.apache.thrift.TException{
    		RFile readObject = new RFile();
	    	try {
//				System.out.println("in read file");
				int flag =0;
				for(RFileMetadata a: l){
					if(a.filename.equals(filename) && a.owner.equals(owner)){
					flag=1;
					readObject.setMeta(a);
					readObject.meta.setContentLength(a.contentLength);
					readObject.meta.setCreated(a.created);
					readObject.meta.setUpdated(a.updated);
					readObject.meta.setVersion(a.version);					
					byte[] encoded = Files.readAllBytes(Paths.get(filename));
					String content = new String(encoded, "UTF-8");
					readObject.content=content.toString();
					return readObject;
					}
				}
					if(flag == 1){
						//System.out.println("yes");
					}
					else{
						//System.out.println("no");
						readObject.meta=null;
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return (readObject);
	    }
	    public List<RFileMetadata> listOwnedFiles(String user) throws SystemException, org.apache.thrift.TException{
	//    	System.out.println("in list owened files");
	    	List<RFileMetadata> userList = new ArrayList();
	    	 	
			for(RFileMetadata a: l){
				if(a.owner.equals(user)){					
					a.setCreated(a.created);
					a.setUpdated(a.updated);
					a.setVersion(a.version);
					a.setContentLength(a.contentLength);
					userList.add(a);
				}
			}
	    	
	    	return userList;
	    }
	 
}
