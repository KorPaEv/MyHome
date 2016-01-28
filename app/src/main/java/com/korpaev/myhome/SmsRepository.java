package com.korpaev.myhome;

import java.util.List;

public interface SmsRepository
{
    void addSms(RawSms rawSms);
    void removeSms(RawSms rawSms);
    void updateSms(RawSms rawSms);

    List query(SmsSpecification specification);
}
