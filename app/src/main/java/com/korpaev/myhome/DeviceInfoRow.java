package com.korpaev.myhome;

public class DeviceInfoRow
{
    private String idDevice;
    private String nameDevice;
    private int protovolVerDev;
    private String addressDevice;
    private String phoneArduino;

    public DeviceInfoRow()
    {

    }

    public DeviceInfoRow (String idDevice, String nameDevice)
    {
        setId(idDevice);
        setNameDevice(nameDevice);
    }

    public String getId() { return idDevice; }
    public String getNameDevice() { return nameDevice; }
    public String getAddressDevice() { return addressDevice; }
    public int getProtovolVerDev() { return protovolVerDev; }
    public String getPhoneArduino() { return phoneArduino; }

    public void setId(String idDev) { idDevice = idDev; }
    public void setNameDevice(String nameDev) { nameDevice = nameDev; }
    public void setAddressDevice(String addrDev) { addressDevice = addrDev; }
    public void setProtovolVerDev(int protovolV) { protovolVerDev = protovolV; }
    public void setPhoneArduino(String phoneArd) { phoneArduino = phoneArd; }
}
