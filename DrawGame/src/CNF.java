import com.rabbitmq.client.ConnectionFactory;

public class CNF {
	public static ConnectionFactory cf() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("admin");
		factory.setPassword("admin");
		factory.setVirtualHost("app");
		factory.setHost("2.tcp.eu.ngrok.io");// npr 8.tcp.ngrok.io dakle tu lijepimo loink iz ngroka
		factory.setPort(18621);// npr 17595
		return factory;
	}
}
