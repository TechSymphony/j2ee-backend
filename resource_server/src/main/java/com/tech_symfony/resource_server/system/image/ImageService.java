package com.tech_symfony.resource_server.system.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

interface ImageService {
    public boolean sendImage(String to, String image);
}

@Service
class TelegramService implements ImageService, LongPollingSingleThreadUpdateConsumer {

    @Value("${TELEGRAM_BOT_TOKEN:not_found}")
    String botToken;

    private TelegramBotsLongPollingApplication application;
    private TelegramClient client;
    String chat_id = "-4540576248";

    public TelegramService() throws Exception {
        if (botToken.equals("not_found")) {
            throw new Exception("TELEGRAM_TOKEN not found");
        }
        application = new TelegramBotsLongPollingApplication();
        application.registerBot(botToken, this);
        client = new OkHttpTelegramClient(botToken);
    }

    public boolean sendImage(MultipartFile image) {
        return false;
    }

    public boolean sendImage(String to, String image) {

        // byte[] imageBytes = DatatypeConverter.parseBase64Binary(image);

        try {
            // File f = File.createTempFile("image", "png");
            // FileOutputStream fos = new FileOutputStream(f);
            // fos.write(imageBytes);
            // fos.close();
            File f = File.createTempFile("tmp_", "png");
            URL url = new URL(image);
            BufferedImage bI = ImageIO.read(url);
            ImageIO.write(bI, "png", f);

            SendPhoto sP = new SendPhoto(chat_id, new InputFile(f));
            client.execute(sP);
            return true;
        } catch (Exception e) {
            // Error on writing
            e.printStackTrace();
        }

        // GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);

        // GetUpdatesResponse updatesResponse = tgBot.execute(getUpdates);
        return false;
    }

    public static void main(String[] args) {
        try {
            TelegramService s = new TelegramService();
            String b64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQBKci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tCa00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5XPp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ihMsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQPsrHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3ekokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDFNx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66PfyuRj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3kvgLI5AzFfo379UAAAAASUVORK5CYII=";
            
            s.sendImage("telegram", "https://i1-vnexpress.vnecdn.net/2024/09/28/Trump-9403-1727483522.png?w=1020&h=0&q=100&dpr=1&fit=crop&s=wu0EE_1OkSaeLPCNK73IxQ");
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