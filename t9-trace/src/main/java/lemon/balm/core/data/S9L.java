package lemon.balm.core.data;

import java.io.Serializable;

import javax.crypto.SecretKey;

public class S9L implements Serializable{
    int d;
    byte[] i;
    SecretKey k;
    long s;
    byte[] u;

    public void setD(int d){ this.d = d;}
    public void setI(byte[] i){ this.i = i;}
    public void setK(SecretKey k){ this.k = k;}
    public void setS(long s){ this.s = s;}
    public void setU(byte[] u){ this.u = u;}

    public int getD(){return d;}
    public byte[] getI(){return i;}
    public SecretKey getK(){return k;}
    public long getS(){return s;}
    public byte[] getU(){return u;}

    public static S9L create(int d, long s, byte[] i , SecretKey k, byte[] u){
        S9L s9l = new S9L();
        s9l.setD(d);
        s9l.setS(s);
        s9l.setI(i);
        s9l.setK(k);
        s9l.setU(u);
        return s9l;
    }

    public final boolean validate() throws Exception{

        if(System.currentTimeMillis() - s <= d) {
            return true;
        }else{
            throw new Exception("Your license was expired now!");
        }

    }
    


}
