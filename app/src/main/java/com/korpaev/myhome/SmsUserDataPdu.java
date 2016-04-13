package com.korpaev.myhome;

public class SmsUserDataPdu
{
    public SmsUserDataPdu()
    {

    }

    public String ConvertPduToGsm(byte[] arr)
    {
        //проверяем на отрицательное значение которое не знает андроид
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] < 0)
                arr[i] = (byte)(arr[i] + 256);
        }
        return HeadAndBodyGsmFromPdu(arr);
    }

    //Функция получает из последовательности байт - число
    public static int toInt32(byte[] bytes, int indx)
    {
        return ((bytes[indx++] & 0xFF) << 24) |
                ((bytes[indx++] & 0xFF) << 16) |
                ((bytes[indx++] & 0xFF) << 8) |
                (bytes[indx] & 0xFF);
    }

    int lenMsg = 0;
    int ind = 0;
    String str = "";
    //Возвращаем читаемый вид распарсенной пду
    public String HeadAndBodyGsmFromPdu(byte[] bytes)
    {
        //Пишем первые 2 байта SH
        str += String.valueOf((char)bytes[ind++]) + String.valueOf((char)bytes[ind++]);

        //Пишем следующие 4 байта - версия протокола но числом и как текст - было 0001 - стало 1
        str += toInt32(bytes, ind) + ";";
        ind += 4;

        //Переворачиваем следующие 4 байта в массиве - время - потому что в ардуино видимо неверно модуль присылает
        for (int i = 0; i < 2; i++)
        {
            byte temp = bytes[ind + i];
            bytes[ind + i] = bytes[ind + 3 - i];
            bytes[ind + 3 - i] = temp;
        }

        //Пишем перевернутые 4 байта как число в текущую строку
        str += toInt32(bytes, ind) + ";";
        ind += 4;

        //Получаем следующие 4 байты как число и пишем в строку - номер датчика
        str += toInt32(bytes, ind) + ";";
        ind += 4;

        //Получаем последний 15й байт и дописываем его в заголовок строки - размер остатка текущей смс - основная инфа по датчику
        int lenBody = bytes[ind++];
        str += lenBody + ";";

        lenMsg = ind + lenBody;
        //Пишем остаток в строку - инфа по датчику
        for (int i = ind; i < lenMsg; i++)
        {
            str += String.valueOf((char)bytes[i]);
        }
        ind = lenMsg;
        str += ";";
        if (lenMsg != bytes.length)
        {
            HeadAndBodyGsmFromPdu(bytes);
        }
        return str;
    }
}
