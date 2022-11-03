import java.net.URISyntaxException;
import java.util.List;

import com.perforce.p4java.client.IClient;
import com.perforce.p4java.core.file.FileSpecBuilder;
import com.perforce.p4java.core.file.IFileSpec;
import com.perforce.p4java.exception.P4JavaException;
import com.perforce.p4java.option.client.SyncOptions;
import com.perforce.p4java.server.IOptionsServer;
import com.perforce.p4java.server.ServerFactory;

public class Main {
	private static final String P4_SERVER = "Specify P4 Server";
	private static final String P4_USERNAME = "Specify P4 Username";
	private static final String P4_PASSWORD = "Specify P4 Password";
	private static final String P4_CLIENT = "Specify P4 Client";
	
	
	public static void main(String[] args) {
		try {
			IOptionsServer server = getOptionsServer();
			if (server == null) {
				System.err.println("Couldn't connect to server");
				return;
			} else {
				System.out.println("Connected!");
			}
			
			// Log in
			server.setUserName(P4_USERNAME);
			server.login(P4_PASSWORD);
			
			// Set the client
			IClient client = server.getClient(P4_CLIENT);
			if (client == null) {
				System.err.println("Couldn't find client on server");
				return;
			}
			
			server.setCurrentClient(client);
			
			// Sync files onto client
			List<IFileSpec> syncList = client.sync(FileSpecBuilder.makeFileSpecList("//..."), new SyncOptions("-f"));
			if (syncList == null) {
				System.err.println("client sync returned a null");
				return;
			}
			
			System.out.println("Synced these files:");
			for (IFileSpec file : syncList) {
				System.out.println(file);
			}
			
		} catch (Exception ex) {
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}
	
	protected static IOptionsServer getOptionsServer() throws P4JavaException, URISyntaxException {
		IOptionsServer server = ServerFactory.getOptionsServer(P4_SERVER, null, null);
		if (server != null) {
			server.connect();
		}
		return server;
	}
}

