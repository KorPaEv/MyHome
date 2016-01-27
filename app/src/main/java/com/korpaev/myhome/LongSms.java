package com.korpaev.myhome;

public final class LongSms
{
    String number;
    String message;

    public LongSms (String number, String message)
    {
        this.number = number;
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public String getNumber()
    {
        return number;
    }
}
