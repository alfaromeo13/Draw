import java.io.*;
import com.rabbitmq.client.Channel;

public class Posiljaoc extends Thread {
	private Channel channel;
	String roomName;
	private Object s;

	public Posiljaoc(Object s, String roomName, Channel channel) {
		this.s = s;
		this.roomName = roomName;
		this.channel = channel;
	}

	public byte[] getBytes() throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(s);
		oos.flush();
		oos.reset();
		bytes = baos.toByteArray();
		oos.close();
		baos.close();
		return bytes;
	}

	@Override
	public void run() {
		try {
			byte[] byteMessage = getBytes();
			channel.basicPublish(roomName, "", null, byteMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}