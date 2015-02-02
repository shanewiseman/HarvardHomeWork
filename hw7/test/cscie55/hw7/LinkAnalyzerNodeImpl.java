package cscie55.hw7;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;


public class LinkAnalyzerNodeImpl implements LinkAnalyzerNode,Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    List<Link> links = new ArrayList<Link>();
    static LinkAnalyzerNodeImpl node;
    static LinkAnalyzer master;
    
    public static void main(String[] args){
        initNode(args[0]);
        initRMI();

    }
    
    private static void initNode(String path){
        File rootdir = null;
        
        try {
            rootdir = new File(path);
        } catch (Exception e){
            System.err.format("IOException: %s%n", e);
            System.exit(1);
        }
        
        node = new LinkAnalyzerNodeImpl(rootdir);
    }
    private static void initRMI(){
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            
            // create the remote object and send it to the registry
            String name = "Node." + node.hashCode();

            LinkAnalyzerNode stub =
                  (LinkAnalyzerNode) UnicastRemoteObject.exportObject(node, 0);
            
            Registry registry = LocateRegistry.getRegistry();

            registry.rebind(name, stub);
            
        } catch (Exception e) {
            System.err.println("exception:");
            e.printStackTrace();
            System.exit(1);
        }

        
        // lets grab the master of whom we'll slave too
        try {
             master = (LinkAnalyzer) Naming.lookup
                    (LinkAnalyzer.URL);
             master.registerNode(node);
        } catch (Exception e){
            // if we cant slave we're useless
            System.exit(1);
        }
    }
    
    
    
    /*              INSTANCE METHODS AND FIELDS    */
    
    public LinkAnalyzerNodeImpl(File rootDir){
        super();
        
        if( ! rootDir.isDirectory()){
            throw new RuntimeException();
        }
        
        File[] files = rootDir.listFiles();
        
        for( int i = 0; i < files.length; i++){
            try (BufferedReader reader = Files.newBufferedReader( Paths.get(files[i].getAbsolutePath()) )) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    try{
                        links.add(Link.parse(line));
                    }catch (Exception e){
                        //IGNORING ANY PARSING PROBLEMS
                    }
                }
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            } 
        }
        
        
    }

    @Override
    public Set<Link> linksByTime(long startTime, long endTime)
            throws RemoteException {
        Set<Link> returnSet = new HashSet<Link>();
        
        for(Link link : links){
            
            if( startTime < link.timestamp() && link.timestamp() < endTime){
                returnSet.add(link);
            }
        }
        
        return returnSet;
    }

    @Override
    public Set<Link> linksByURL(String url) throws RemoteException {
        Set<Link> returnSet = new HashSet<Link>();
        
        for(Link link : links){
            if( link.url().equals(url) ){
                returnSet.add(link);
            }
        }
        
        return returnSet;
    }

    @Override
    public Set<Link> linksByTag(String... tags) throws RemoteException {
        
        Set<Link> returnSet = new HashSet<Link>();
        
        
        for(Link link : links){
            returnSet.add(link);
            for(String tag : tags ){
                if(! link.tags().contains(tag)){
                    
                    returnSet.remove(link);
                    break;
                    
                }
            }
        }
        
        return returnSet;
    }

}
