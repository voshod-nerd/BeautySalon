package com.voshodnerd.BeatySalon.utils;

public abstract class MessageConstant {
    public static final String NO_MASTER = "Не указан мастер";
    public static final String NO_USER = "Не указан пользователь";
    public static final String NO_SERVICE_LIST = "Отсуствуют список выбранных услуг";
    public static final String EARLIER_START_WORK_DAY = "Неверное время бронирования: раньше начала рабочего дня";
    public static final String LATE_FINISH_WORK_DAY = "Неверное время бронирования: позже окончания рабочего дня";
    public static final String BOOKING_TIME_IS_LONG = "Общее время работ превышает рабочее время мастера";
    public static final String BOOKING_TIME_INTERSECTS = "Время бронирование пересекается с другими записями мастера";
    public static final String BOOKING_IS_PERMITTED = "Бронирование разрешено";
    public static final String BOOKING_IS_SUCCESSFUL = "Бронирование успешно создано";
    public static final String BOOKING_IS_EXECUTED = "Заказ выполнен";
    public static final String BOOKING_NOT_FOUND = "Бронирование не найдено";
    public static final String MATERIAL_ALREADY_EXIST = "Материал с данных имененм уже сущесвует";
    public static final String MATERIAL_SUCCESSFUL_CREATED = "Материал успешно создан";
    public static final String MATERIAL_NOT_FOUND = "Материал не найдет";
    public static final String MATERIAL_LESS_ZERO = "Невозможно уменьшить количество менье 0";
    public static final String MATERIAL_COUNT_SUCCESSFUL_CHANGED = "Количество успешно изменено";
    public static final String CONSUME_MATERIAL_SUCCESSFUL_ADDED = "Потребляемый материл успешно добавлен";
    public static final String CONSUME_MATERIAL_ERROR = "Ошибка добавления потребляемого материала";
    public static final String CONSUME_MATERIAL_DELETED="Удален потребляемый материал";
    public static final String PROMOCODE_NOT_FOUND = "Промокод ошибочен.";
    public static final String INCOME_BY_CLOSE_BOOKING = "Оплата работы пастера по списку услуг";
    public static final String NOT_FOUND_CASHIER = "Нет доступной кассы";
    public static final String ADDED_NEW_MONEY = "Пополнение кассы";
    public static final String CASHIER_LESS_ZERO = "Нельзя выполнить операцию  снятия денег. Денег меньше 0";
    public static final String TRANSACTION_MONEY_PERFORMED = "Транзакция с кассой выполнена";
    public static final String MONEY_BALANCE = "Информация о балансе кассы";
    public static final String PICTURE_SAVE_SUCCESSFULLY = "Картина сохранена успешно";
    public static final String PICTURE_SAVE_ERROR = "Изображение не удалось сохранить";
}