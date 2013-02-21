package reasoning;

import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class Main {
	
	public static void main(String[] args) {
		String sesameServer = "http://asimov.ludat.lth.se";
		String repositoryID = "KnowRob";
		Repository myRepository = new HTTPRepository(sesameServer, repositoryID);
		try {
			myRepository.initialize();
			System.out.println("Done!");
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		//Documentation
		//http://www.openrdf.org/doc/sesame2/users/ch08.html#d0e810
	}

}
