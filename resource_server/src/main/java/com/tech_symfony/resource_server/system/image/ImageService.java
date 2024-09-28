package com.tech_symfony.resource_server.system.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
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
    public String sendImage(MultipartFile image);
    public File getImage(String image);
}

@Service
class TelegramService implements ImageService, LongPollingSingleThreadUpdateConsumer {

    @Value("${TELEGRAM_BOT_TOKEN:not_found}")
    String botToken;

    private TelegramBotsLongPollingApplication application;
    private TelegramClient client;

    @Value("${TELEGRAM_GROUP:not_found}")
    String chat_id;

    public TelegramService() throws Exception {
        if (botToken.equals("not_found")) {
            throw new Exception("TELEGRAM_TOKEN not found");
        }
        application = new TelegramBotsLongPollingApplication();
        application.registerBot(botToken, this);
        client = new OkHttpTelegramClient(botToken);
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
                SendPhoto sP = new SendPhoto(chat_id, new InputFile(f));
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

    public static void main(String[] args) {
        try {
            TelegramService s = new TelegramService();

//            s.sendImage("telegram", "https://i1-vnexpress.vnecdn.net/2024/09/28/Trump-9403-1727483522.png?w=1020&h=0&q=100&dpr=1&fit=crop&s=wu0EE_1OkSaeLPCNK73IxQ");

            System.out.println(s.getImage("AgACAgUAAxkDAAMEZveb6jr5iETdPGp4hMWWKhdLz4IAAnC_MRsj-rlXB5IjdmyTpowBAAMCAANzAAM2BA"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
			System.out.println(update.getMessage().getText());
		}
    }
}