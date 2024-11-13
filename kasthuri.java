package ChatApplication;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class kasthuri extends Frame implements Runnable, ActionListener {
    TextField textfield;
    TextArea textarea;
    Button btn;

    ServerSocket server;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    Thread chat;

    kasthuri() {
        textfield = new TextField(50); // Increased the width of the text field
        textarea = new TextArea();
        btn = new Button("Send");

        add(textfield);
        add(textarea);
        add(btn);

        setSize(500, 500);
        setTitle("kasthuri");
        setLayout(new FlowLayout());
        setVisible(true);

        btn.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        try {
            server = new ServerSocket(1200);
            socket = server.accept();
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }

        chat = new Thread(this);
        chat.setDaemon(true);
        chat.start();
    }

    public void actionPerformed(ActionEvent e) {
        String msg = textfield.getText();
        textarea.append("kasthuri: " + msg + "\n");
        textfield.setText(""); // Clear the text field instead of clearing the textarea

        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new kasthuri();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msg = in.readUTF();
                textarea.append("Friend: " + msg + "\n");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
