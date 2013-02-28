package reasoning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

/**
 * Class for doing SELECT queries against a specified SESAME repository
 * 
 * @author CH
 * 
 */
public class SesameRepository {
	
	private Repository repository;

	/**
	 * Initializes the specified SESAME repository
	 * 
	 * @param sesameServer
	 *            The server URL
	 * @param repositoryId
	 *            The server ID
	 */
	public SesameRepository(String sesameServer, String repositoryId) {
		repository = new HTTPRepository(sesameServer, repositoryId);
		try {
			repository.initialize();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Execute a SELECT SPARQL query against the graph
	 * 
	 * @param queryString
	 *            SELECT SPARQL query
	 * @return list of solutions, each containing a HashMap<String, Value> of
	 *         bindings
	 */
    public ArrayList<HashMap<String, Value>> selectSPARQL(String queryString) {
        try{
            RepositoryConnection con = repository.getConnection();
            try {
                TupleQuery query = 
                    con.prepareTupleQuery(
                    org.openrdf.query.QueryLanguage.SPARQL, queryString);
                TupleQueryResult qres = query.evaluate();
                ArrayList<HashMap<String, Value>> reslist = new ArrayList<HashMap<String, Value>>();
                while (qres.hasNext()) {
                    BindingSet b = qres.next();
                    Set<String> names = b.getBindingNames();
                    HashMap<String, Value> hm = new HashMap<String, Value>();
                    for (Object n : names) {
                        hm.put((String) n, b.getValue((String) n));
                    }
                    reslist.add(hm);
                }
                return reslist;
            } finally {
                con.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
