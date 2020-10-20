import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class EchoBot {

    public static void main(String[] args) {
        String token = args[0];

        DiscordApi api = new DiscordApiBuilder()
                .setToken(token)
                .login()
                .join();

        api.addMessageCreateListener(event -> {
            String message = event.getMessageContent();
            String author = event.getMessageAuthor().getDisplayName();
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

            if ((event.getMessageContent()
                    .startsWith("!echo")) && (event.getMessageAuthor().isRegularUser())) {

                // Split and pull out content
                String[] messageParts = message.split(" ", 2);
                String content = messageParts[1];

                // Send message to channel
                event.getChannel().sendMessage(content);

                // naive logging
                String timeMessage = String.format("%s INFO %s: %s\n", formatter.format(date), author, content);
                String logFile = CreateFile("echo", "log");
                writeToFile(logFile,timeMessage);
                System.out.println(timeMessage);
            }
        });
    }

    private static String CreateFile(String fileName, String extension) {
        String outFile = String.format("%s.%s", fileName, extension);
        new File(outFile);

        return outFile;
    }

    private static void writeToFile(String fileName, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
}

