package edu.uark.jfrorieemail.scurry;

/**
 * Created by grantjohns on 4/20/16.
 */
public class Message
{
    String message;
    int sendID, recieveID;

    public Message (String m, int s, int r)
    {
        message = m;
        sendID = s;
        recieveID = r;
    }

    String getMessage()
    {
        return  message;
    }
    void setmessage(String m)
    {
        message = m;
    }
    int getsendID()
    {
        return sendID;
    }
    void setsendID(int s)
    {
       sendID = s;
    }
    int getrecieveID()
    {
        return recieveID;
    }
    void setrecieveID(int r)
    {
        recieveID = r;
    }
}
