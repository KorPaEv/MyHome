package com.korpaev.myhome;

public class HistoryRow
{
    private String _idDevice;
    private String _dateTime;
    private String _smsBody;

    public HistoryRow()
    {

    }

    public String getId() { return _idDevice; }
    public String getDateTime() { return _dateTime; }
    public String getSmsBody() { return _smsBody; }

    public void setId(String idDev) { _idDevice = idDev; }
    public void setDateTime(String dateTime) { _dateTime = dateTime; }
    public void setSmsBody(String smsBody) { _smsBody = smsBody; }
}
