package edu.uark.jfrorieemail.scurry;

/**
 * Created by grantjohns
 */
public class Review
{
    String content;
    int sendID, recieveID;
    //I'm not sure how to sent the review score?
    //int score;

    public Review (String c, int s, int r)//, int sc
    {
        content = c;
        sendID = s;
        recieveID = r;
        //score = sc;
    }

    String getContent()
    {
        return  content;
    }
    void setContent(String m)
    {
        content = m;
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

   //int getScore()
    //{
      //  return score;
    //}
    //void setScore(int sc)
    //{
        //score = sc;
    //}
}