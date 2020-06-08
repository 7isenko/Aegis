package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class DiscordControlMessageListener extends ListenerAdapter {
    private DiscordManager dm;
    private final Guild guild;
    private final TextChannel controlChannel;

    public DiscordControlMessageListener(DiscordManager dm) {
        super();
        this.dm = dm;
        this.guild = dm.getGuild();
        this.controlChannel = guild.getTextChannelById(Aegis.config.getString("control_channel_id"));
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String rawMessage = event.getMessage().getContentRaw();
        if (!event.getChannel().equals(controlChannel))
            return;

        if (rawMessage.startsWith("!")) {
            String[] fullCommand = rawMessage.split(" ");
            String command = fullCommand[0];
            switch (command) {
                case "!start":
                    dm.start();
                    logResult("Бот запущен");
                    break;
                case "!stop":
                    dm.stop();
                    logResult("Бот выключен, вайтлист очищен. Роли сейчас заберу");
                    break;
                case "!kick":
                    dm.kickWithoutRoles();
                    logResult("Массовый безрольный кик запущен");
                    break;
                case "!emote":
                case "!emoji":
                case "!react":
                case "!ultra":
                    dm.start();
                    dm.startEmoteMode();
                    break;
                case "!add":
                    if (dm.isEmoteMode()) {
                        try {
                            int num = Integer.parseInt(fullCommand[1]);
                            dm.setChosenRoles(num);
                            logResult("Запущен процесс добавления " + num + " игроков");
                        } catch (Exception e) {
                            logResult("Ошибочка вышла, !help");
                        }
                    } else logResult("Сначала напиши !emote и подожди");
                    break;
                case "!help":
                    logResult("!start - Запуск простого вайтлист-бота (включать).\n" +
                            "!emote, !emoji, !react, !ultra - Запуск крутого бота, работающего через реацию на сообщении.\n" +
                            "!add <число> - дает роль \"избранного\" <числу> людей, оставивших реакцию.\n" +
                            "!stop - оба бота стопаются.\n" +
                            "!kick - кикает из дискорда всех челиков без ролей.\n" +
                            "!help - вывести это сообщение");
                default:
                    logResult("Команда была введена неправильно, чек !help");
            }
        }
    }

    private void logResult(String answer) {
        controlChannel.sendMessage(answer).queue();
    }
}