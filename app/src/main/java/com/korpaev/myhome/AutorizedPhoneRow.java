package com.korpaev.myhome;

public class AutorizedPhoneRow
{
    private String idDevice;
    private String phoneNumber; // разрешенный номер
    private boolean sendSmsRights; // права на смс уведомление для номера
    private boolean callRights; // права на обратный звонок для номера
    private boolean isAdmNumb; // является ли номер админским

    public String getIdDevice() { return idDevice; }
    public String getPhoneNumber() { return phoneNumber; }
    public boolean getSendSmsRights() { return sendSmsRights; }
    public boolean getCallRights() { return callRights; }
    public boolean getIsAdmNumb() { return isAdmNumb; }

    public void setIdDevice(String idDevice) { this.idDevice = idDevice; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setSendSmsRights(boolean sendSmsRights) { this.sendSmsRights = sendSmsRights; }
    public void setCallRights(boolean callRights) { this.callRights = callRights; }
    public void setIsAdmNumb(boolean isAdmNumb) { this.isAdmNumb = isAdmNumb; }

    //+79998887766;1;1;1
    public void ParseRow(String row)
    {
        String[] splitRows;
        splitRows = row.split(";");
        setPhoneNumber("+" + splitRows[0]);
        setSendSmsRights(splitRows[1].equals("1") ? true : false);
        setCallRights(splitRows[2].equals("1") ? true : false);
        setIsAdmNumb(splitRows[3].equals("1") ? true : false);
    }
}
