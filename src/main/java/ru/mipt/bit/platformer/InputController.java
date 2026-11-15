package ru.mipt.bit.platformer;

import java.util.List;

/**
 * Выходной порт слоя приложения (application): источник команд на очередной такт.
 * Хотя данные поступают в игру, вызов инициирует {@link Game}: он опрашивает
 * реализацию этого интерфейса. В текущей модели опроса это вызываемый порт,
 * а не входной интерфейс запуска сценария.
 */
public interface InputController {

    List<Command> createCommands();
}
