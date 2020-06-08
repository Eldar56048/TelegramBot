package controllers;

import domain.models.Order;
import domain.models.Orders;
import domain.models.Product;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import repositories.OrderRepository;
import repositories.PostgresRepository;
import repositories.interfaces.IDBRepository;
import services.ProductService;
import services.UserService;
import services.interfaces.IProductService;
import services.interfaces.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.Math.addExact;
import static java.lang.Math.toIntExact;


public class TelegramController extends TelegramLongPollingBot {
    private final IUserService userService = new UserService();
    ArrayList<Order> orders = new ArrayList<>();
    OrderController orderController = new OrderController();
    ArrayList<Product> products = new ArrayList<>();
    OrderRepository neworder = new OrderRepository();
    Orders mainOrder = new Orders();
    String adress;

    String phone;
    String oplata;
    String time;
    String getType;

    long chatid;
    private IDBRepository dbrepo = new PostgresRepository();
    private final String USERNAME = "EldarTalgatJavaBot";
    private final String TOKEN = "1111442450:AAFmWPbLl0dS-Z4SbhjorD5yh3H3o0Pav3c";

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();


        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    SendMsg(message, "Это команда старт!");
                    System.out.println(message.getText());
                    break;
                case "Сделать заказ":
                    try {
                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Оформить заказ":
                    if (!orders.isEmpty()) {
                        try {
                            execute(sendMessageIssue(update.getMessage().getChatId()));
                            if (update.hasMessage()) {
                                String adress = update.getMessage().getText();
                                System.out.println(adress);
                            }
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {
                        SendMsg(message, "Вы еще нечего не выбрали");
                    }
                    break;


                default:
                    if (update.getMessage().getText().contains("Адрес")) {
                        adress = update.getMessage().getText();
                        vseVerno(update.getMessage().getChatId(), adress);
                        chatid = update.getMessage().getChatId();
                        SendPhone(update.getMessage().getChatId());

                    } else if (update.getMessage().getText().contains("8") && update.getMessage().getText().length() == 11) {

                        phone = update.getMessage().getText();

                        vseVerno(update.getMessage().getChatId(), phone);

                        try {
                            execute(sendMessageMoney(chatid));


                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }


                    } else if (Pattern.matches("[a-zA-Z]+", update.getMessage().getText()) == false && update.getMessage().getText().length() < 6) {
                        time = update.getMessage().getText();
                        System.out.println(time);
                        vseVerno(update.getMessage().getChatId(), time);
                        try {
                            execute(new SendMessage().setText(
                                    "Ваш заказ принят ожидайте  Спасибо что ползуетесь нашим ботом")
                                    .setChatId(update.getMessage().getChatId()));


                            String allorder = orderController.seeKorzina(update.getCallbackQuery().getMessage().getChatId(), orders);
                            neworder.add(allorder, getType,  adress, phone,time,  oplata);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else
                        SendMsg(message, "Введите коректный формат");

            }

        } else if (update.getCallbackQuery().getData().equals("time")) {

            chooseTime(update.getCallbackQuery());

        } else if (update.getCallbackQuery().getData().equals("doner") || update.getCallbackQuery().getData().equals("drink") || update.getCallbackQuery().getData().equals("food")) {
            String productname = update.getCallbackQuery().getData();
            IProductService productService = new ProductService();
            ArrayList<Product> product = productService.getProductByName(productname);

            for (int i = 0; i < product.size(); i++) {
                String allInfo = product.get(i).getPhotoUrl() + "\n" + product.get(i).getName() + "\n" + product.get(i).getStructure() + "\n" +
                        product.get(i).getPrice();


                try {
                    SendMediaGroup sendPhotoRequest = new SendMediaGroup();
                    sendPhotoRequest.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    execute(new SendMessage().setText(
                            allInfo)
                            .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                    execute(sendInlineKeyBoardOrder(update.getCallbackQuery().getMessage().getChatId(),
                            product.get(i).getPrice(),
                            product.get(i).getId(),

                            orderController.countOrders(
                                    update.getCallbackQuery().getMessage().getChatId(), orders, product.get(i).getId())));

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }


        } else if (Pattern.matches("[a-zA-Z]+", update.getCallbackQuery().getData()) == false) {
            int id = Integer.parseInt(update.getCallbackQuery().getData());
            if (orderController.Filter(id, update.getCallbackQuery().getMessage().getChatId(), orders)) {
                orders = orderController.addCount(update.getCallbackQuery().getMessage().getChatId(), id, orders);
            } else {

                orders = orderController.NewOrder(update.getCallbackQuery().getMessage().getChatId(), id, 1, orders);

            }
            orderController.seeKorzina(update.getCallbackQuery().getMessage().getChatId(), orders);

            System.out.println(
                    id);
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            String answer = "Заказать за " + orderController.getPrice(id) + " тг " + orderController.countOrders(chat_id, orders, id) + "  штук";

            EditMessageReplyMarkup new_message = new EditMessageReplyMarkup()
                    .setChatId(chat_id)
                    .setMessageId(toIntExact(message_id))
                    .setReplyMarkup(sendInlineKeyBoardOrder2(chat_id,
                            orderController.getPrice(id),
                            id,
                            orderController.countOrders(chat_id, orders, id)));
            try {
                execute(new_message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        } else if (update.getCallbackQuery().getData().equals("seekorzina")) {
            OrderController order = new OrderController();
            try {
                String allorder = order.seeKorzina(update.getCallbackQuery().getMessage().getChatId(), orders);
                execute(new SendMessage().setText(
                        allorder)
                        .setChatId(update.getCallbackQuery().getMessage().getChatId()));

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        } else if (update.getCallbackQuery().getData().equals("clear")) {
            OrderController order = new OrderController();
            for (int i = 0; i < orders.size(); i++) {
                orders.get(i).setCount(0);
            }

            try {
               String  allorder = order.removeOrder(update.getCallbackQuery().getMessage().getChatId(), orders);
                execute(new SendMessage().setText(
                        allorder)
                        .setChatId(update.getCallbackQuery().getMessage().getChatId()));

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (update.getCallbackQuery().getData().equals("dostavka")){
        getType="Доставка";
            sendDostavka(update.getCallbackQuery());
        } else if (update.getCallbackQuery().getData().equals("sam")) {
            getType="Самовывоз";
            adress = "Самовывоз";
            System.out.println(adress);
            vseVerno(update.getCallbackQuery().getMessage().getChatId(), adress);
            System.out.println(adress);
            try {
                execute(sendMessageTime(update.getCallbackQuery().getMessage().getChatId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (update.getCallbackQuery().getData().equals("lave")) {
            oplata = "Наличными";
            vseVerno(chatid, oplata);

            try {
                execute(sendMessageTime(chatid));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.getCallbackQuery().getData().equals("card")) {
            oplata = "Картой";

            vseVerno(chatid, oplata);
            try {
                execute(sendMessageTime(chatid));
            } catch (TelegramApiException e) {


            }
        } else if (update.getCallbackQuery().getData().equals("now")) {
            time = "Сейчас";
            vseVerno(update.getCallbackQuery().getMessage().getChatId(), time);

            try {
                execute(new SendMessage().setText(
                        "Ваш заказ принят ожидайте  Спасибо что ползуетесь нашим ботом")
                        .setChatId(update.getCallbackQuery().getMessage().getChatId()));

                neworder.add(orderController.seeKorzina(update.getCallbackQuery().getMessage().getChatId(), orders), getType,  adress, phone,time,  oplata);


            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }


    }

    private void chooseTime(CallbackQuery callbackQuery) {
        try {
            execute(new SendMessage().setText(
                    "Введите удобное вам время в этом формате 12:00")
                    .setChatId(callbackQuery.getMessage().getChatId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendDostavka(CallbackQuery callbackQuery) {

        try {
            execute(new SendMessage().setText(
                    "Введите свой адреc \n" +
                            " (например: 'Адрес город,улица,номер дома'")
                    .setChatId(callbackQuery.getMessage().getChatId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void SendPhone(long chatid) {

        try {
            execute(new SendMessage().setText(
                    "Введите свой номер \n" +
                            " (например: '87777777070'")
                    .setChatId(chatid));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void vseVerno(long ChatID, String text) {
        try {
            execute(new SendMessage().setText(
                    "Вы ввели:   " + text)
                    .setChatId(ChatID));
        } catch (TelegramApiException e) {
            e.printStackTrace();

        }
    }

    public static SendMessage sendInlineKeyBoardOrder(long chatId, int price, long id, int count) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Корзина");
        inlineKeyboardButton1.setCallbackData("seekorzina");
        inlineKeyboardButton2.setText("Заказать за " + price + "тг   " + count + "  штук");
        inlineKeyboardButton2.setCallbackData("" + id + "");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Очистить корзину").setCallbackData("clear"));
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Выбирай что ты хочешь, количество и размер выберем позже")
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    public static SendMessage sendMessageIssue(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Доставка");
        inlineKeyboardButton1.setCallbackData("dostavka");
        inlineKeyboardButton2.setText("Самовывоз");
        inlineKeyboardButton2.setCallbackData("sam");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Отлично! Выбери способ получения товара")
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    public static SendMessage sendMessageMoney(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Наличные");
        inlineKeyboardButton1.setCallbackData("lave");
        inlineKeyboardButton2.setText("Карта");
        inlineKeyboardButton2.setCallbackData("card");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Отлично! Выбери способ оплаты товара")
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    public static SendMessage sendMessageTime(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Прямо сейчас");
        inlineKeyboardButton1.setCallbackData("now");
        inlineKeyboardButton2.setText("Ко времени");
        inlineKeyboardButton2.setCallbackData("time");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Отлично! Когда начинать готовить?")
                .setReplyMarkup(inlineKeyboardMarkup);
    }

    public static InlineKeyboardMarkup sendInlineKeyBoardOrder2(long chatId, int price, long id, int count) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Корзина");
        inlineKeyboardButton1.setCallbackData("seekorzina");
        inlineKeyboardButton2.setText("Заказать за " + price + "тг   " + count + "  штук");
        inlineKeyboardButton2.setCallbackData("" + id + "");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Очистить корзину").setCallbackData("clear"));
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    public static SendMessage sendInlineKeyBoardMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Донер");
        inlineKeyboardButton1.setCallbackData("doner");
        inlineKeyboardButton2.setText("Напитки");
        inlineKeyboardButton2.setCallbackData("drink");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Закуски").setCallbackData("Food"));
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Выбирай что ты хочешь, количество и размер выберем позже").setReplyMarkup(inlineKeyboardMarkup);
    }

    public void SetButtons(SendMessage sendMessage) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Сделать заказ"));
        keyboardSecondRow.add(new KeyboardButton("Оформить заказ"));
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public synchronized void answerCallbackQuery(String callbackId, String message) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        answer.setText(message);
        answer.setShowAlert(true);
        try {
            answerCallbackQuery(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void SendMsg(Message message, String text) {

        SendMessage sendMessage = new SendMessage();

        sendMessage.enableMarkdown(true);

        sendMessage.setChatId(message.getChatId().toString());

        sendMessage.setReplyToMessageId(message.getMessageId());

        sendMessage.setText(text);

        try {
            SetButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {

            e.printStackTrace();

        }

    }
}

