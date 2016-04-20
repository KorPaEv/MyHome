package com.korpaev.myhome;

public final class DeviceInfoRow
{
    private String idDevice;
    private String name;

    public DeviceInfoRow (String idDevice, String name)
    {
        this.idDevice = idDevice;
        this.name = name;
    }

    public String getId()
    {
        return idDevice;
    }

    public String getName()
    {
        return name;
    }
}
