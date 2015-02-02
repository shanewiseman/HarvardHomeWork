package cscie55.hw7;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface LinkAnalyzer extends Remote
{
    /**
     * Return the Links whose timestamp is between startTime and endTime.
     * @param startTime Minimum timestamp to be returned.
     * @param endTime Maximum timestamp to be returned.
     * @return Links whose timestamp is between startTime and endTime.
     * @throws RemoteException
     */
    Set<Link> linksByTime(long startTime, long endTime) throws RemoteException;

    /**
     * Return the Links with a given URL.
     * @param url URL to search for.
     * @return Links with the given URL.
     * @throws RemoteException
     */
    Set<Link> linksByURL(String url) throws RemoteException;

    /**
     * Return the Links containing all of the given tags.
     * @param tags Set of tags of interest.
     * @return Links containing all of the given tags.
     * @throws RemoteException
     */
    Set<Link> linksByTag(String ... tags) throws RemoteException;

    /**
     * Register the given LinkAnalyzerNode as a node for use by this LinkAnalyzer.
     * @param node A node to be used by this LinkAnalyzer.
     * @throws RemoteException
     */
    void registerNode(LinkAnalyzerNode node) throws RemoteException;

    /**
     * RMI name of the LinkAnalyzer.
     */
    static String URL = "//localhost/linkanalyzer";
}
