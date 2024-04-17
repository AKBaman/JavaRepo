class Message {
    private String content;
    private boolean empty = true;

    public synchronized String read() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted while waiting for message to be read.");
            }
        }
        empty = true;
        notifyAll();
        return content;
    }

    public synchronized void write(String content) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted while waiting for message to be written.");
            }
        }
        this.content = content;
        empty = false;
        notifyAll();
    }
}

class Sender implements Runnable {
    private Message message;

    public Sender(Message message) {
        this.message = message;
    }

    public void run() {
        String[] messages = {"Hello", "How are you?", "Bye"};
        for (String msg : messages) {
            message.write(msg);
            System.out.println("Sent: " + msg);
            try {
                Thread.sleep(1000); // Simulate some processing time
            } catch (InterruptedException e) {
                System.out.println("Interrupted while sending message.");
            }
        }
    }
}

class Receiver implements Runnable {
    private Message message;

    public Receiver(Message message) {
        this.message = message;
    }

    public void run() {
        for (int i = 0; i < 3; i++) {
            String receivedMessage = message.read();
            System.out.println("Received: " + receivedMessage);
            try {
                Thread.sleep(1000); // Simulate some processing time
            } catch (InterruptedException e) {
                System.out.println("Interrupted while receiving message.");
            }
        }
    }
}

public class InterProcessCommunication {
    public static void main(String[] args) {
        Message message = new Message();

        Thread senderThread = new Thread(new Sender(message));
        Thread receiverThread = new Thread(new Receiver(message));

        senderThread.start();
        receiverThread.start();
    }
}
