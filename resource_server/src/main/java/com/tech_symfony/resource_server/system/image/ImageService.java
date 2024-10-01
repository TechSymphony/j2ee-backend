package com.tech_symfony.resource_server.system.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public interface ImageService {
    public String sendImage(MultipartFile image) throws Exception;
    public File getImage(String image) throws Exception;
}

class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private TelegramBotsLongPollingApplication application;
    private TelegramClient client;

    private String botToken;
    private String chatId;

    public TelegramBot(String botToken, String chatId) throws Exception {
        if (botToken.equals("not_found")) {
            throw new Exception("TELEGRAM_TOKEN not found");
        }
        application = new TelegramBotsLongPollingApplication();
        application.registerBot(botToken, this);
        client = new OkHttpTelegramClient(botToken);

        this.botToken = botToken;
        this.chatId = chatId;
    }

    public File getImage(String image) {
        GetFile gF = new GetFile(image);
        File f;
        try {
            org.telegram.telegrambots.meta.api.objects.File receivedPhoto = client.execute(gF);
            URL url = new URL(receivedPhoto.getFileUrl(botToken));
            BufferedImage bI = ImageIO.read(url);
            f = File.createTempFile("image_", "png");
            ImageIO.write(bI, "png", f);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String sendImage(MultipartFile image) {
        try {
            File f = File.createTempFile("image_", "png");
            try (OutputStream os = new FileOutputStream(f)) {
                os.write(image.getBytes());
                // Sending image...
                SendPhoto sP = new SendPhoto(chatId, new InputFile(f));
                Message m = client.execute(sP);
                f.delete();
                System.out.println(m.getPhoto().get(m.getPhoto().size() - 1).getFileId());
                return m.getPhoto().get(m.getPhoto().size() - 1).getFileId();
            }
        } catch (Exception e) {
            // Error on writing
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println(update.getMessage().getText());
        }
    }
}

@Service
class TelegramService implements ImageService {
    private Environment env;

    @Value("${telegram.bot_token:not_found}")
    private String botToken;
    @Value("${telegram.group}")
    private String chatId;

    @Override
    public String sendImage(MultipartFile image) throws Exception {
        return new TelegramBot(botToken, chatId).sendImage(image);
    }

    @Override
    public File getImage(String image) throws Exception {
        return new TelegramBot(botToken, chatId).getImage(image);
    }
}