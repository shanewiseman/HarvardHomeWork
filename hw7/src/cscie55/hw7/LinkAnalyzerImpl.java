package cscie55.hw7;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LinkAnalyzerImpl implements LinkAnalyzer {
    
    static LinkAnalyzerImpl linkAnalyzer;
    
    public static void main(String[] args){
        initLinkAnalyzer();
        initRMI();
    }
    private static void initLinkAnalyzer(){
        linkAnalyzer = new LinkAnalyzerImpl();
    }
    private static void initRMI(){
        
        try {
            String name = "linkanalyzer";

            LinkAnalyzer stub =
                  (LinkAnalyzer) UnicastRemoteObject.exportObject(linkAnalyzer, 0);
            
            Registry registry = LocateRegistry.getRegistry();
           
            registry.rebind(name, stub);

        } catch (Exception e) {
            System.err.println("exception:");
            e.printStackTrace();
        }
    }
    
/*            LinkAnalyzerImpl Instance Code    */
    
    List<LinkAnalyzerNode> nodes = new ArrayList<LinkAnalyzerNode>();
    
    public LinkAnalyzerImpl(){
        super();
    }

    @Override
    public Set<Link> linksByTime(long startTime, long endTime)
            throws RemoteException {
        Set<Link> returnSet = new HashSet<Link>();
        
        for(LinkAnalyzerNode node : nodes){
            returnSet.addAll(node.linksByTime(startTime,endTime));
        }
        
        return returnSet;
    }

    @Override
    public Set<Link> linksByURL(String url) throws RemoteException {
        Set<Link> returnSet = new HashSet<Link>();
        
        for(LinkAnalyzerNode node : nodes){
            returnSet.addAll(node.linksByURL(url));
        }
        
        return returnSet;
    }

    @Override
    public Set<Link> linksByTag(String... tags) throws RemoteException {
        
        Set<Link> returnSet = new HashSet<Link>();
        
        for(LinkAnalyzerNode node : nodes){
            returnSet.addAll(node.linksByTag(tags));
        }
        
        return returnSet;
    }

    @Override
    public void registerNode(LinkAnalyzerNode node) throws RemoteException {
        nodes.add(node);
    }
}
