package main;
import java.util.List;
import java.util.Random;

public class Veznedar {
    private int tellerId;
    private String ad;
    private String soyad;
    private String vPassword;


    Veznedar(int tellerId,String ad,String soyad,String vPassword){
        this.tellerId=tellerId;
        this.ad=ad;
        this.soyad=soyad;
        this.vPassword=vPassword;
    }
    // GETTERS
    public int getTellerId() {
        return tellerId;
    }
    public String getAd() {
        return ad;
    }
    public String getSoyad() {
        return soyad;
    }
    public String getVPassword() {
        return vPassword;
    }

    // SETTERS
    public void setTellerId(int tellerId) {
        this.tellerId = tellerId;
    }
    public void setAd(String ad) {
        this.ad = ad;
    }
    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }
    public void setVPassword(String vPassword) {
        this.vPassword = vPassword;
    }

    Bank banka=new Bank(1,"İnüBank","Malatya");

    public void musteriEkle(Musteri m){
        List<Musteri> musteriler=banka.getMusteriler();
        musteriler.add(m);
        banka.setMusteriler(musteriler);
    }
    public void musteriSil(int musteriId){
        List<Musteri> musteriler=banka.getMusteriler();
        for(Musteri m:musteriler){
            if(m.getMusteriId()==musteriId){
                musteriler.remove(m);
                break;
            }
        }
        banka.setMusteriler(musteriler);
    }
    public void vHesapAc(int musteriId){
        List<Musteri> musteriler=banka.getMusteriler();
        for(Musteri m:musteriler){
            if(m.getMusteriId()==musteriId){
             m.mHesapAc(new Hesap(musteriId,1001));
            }
        }
    }
    public void vHesapKapat(int musteriId,int hesapId){
        List<Musteri> musteriler=banka.getMusteriler();
        for(Musteri m:musteriler){
            if(m.getMusteriId()==musteriId){
                m.mHesapKapat(hesapId);
            }
        }
    }
    public void vParaYatir(int musteriId, int hesapId,int yatırılacak){
        List<Musteri> musteriler=banka.getMusteriler();
        for(Musteri m:musteriler){
            if(m.getMusteriId()==musteriId){
                m.paraYatir(hesapId,yatırılacak);
            }
        }
    }
    public void vParaCek(int musteriId, int hesapId,int cekilecek){
        List<Musteri> musteriler=banka.getMusteriler();
        for(Musteri m:musteriler){
            if(m.getMusteriId()==musteriId){
                m.paraCek(hesapId,cekilecek);
            }
        }
    }
}
