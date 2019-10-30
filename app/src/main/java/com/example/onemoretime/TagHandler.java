package com.example.onemoretime;

import android.nfc.Tag;
import android.nfc.tech.NfcA;

public class TagHandler {
    private Tag tag;
    private NfcA nfcA;

    public TagHandler(Tag tag){
        this.tag = tag;
        this.nfcA = NfcA.get(tag);
    }

    public String getTagInfo(){
        return tag.toString();
    }

    public Integer getTagIDLength(){
        return tag.getId().length;
    }

    public String getTagID(){
        byte[] tagId = tag.getId();
        String id = "";
        for(int i=0; i<getTagIDLength(); i++){
            id += Integer.toHexString(tagId[i] & 0xFF) + " ";
        }
        return id;
    }

    public Integer getTechListLength(){
        return tag.getTechList().length;
    }

    public String getTechList(){
        String[] techList = tag.getTechList();
        String techs = "";
        for(int i=0; i<getTechListLength(); i++){
            techs += techList[i] + "\n ";
        }

        return techs;
    }

    public int getDescribeContents(){
        return tag.describeContents();
    }

    public int getHashCode(){
        return tag.hashCode();
    }

    public int getSak(){
        return nfcA.getSak();
    }

    private static String byte2HexString(byte[] bytes) {
        StringBuilder ret = new StringBuilder();
        if (bytes != null) {
            for (Byte b : bytes) {
                ret.append(String.format("%02X", b.intValue() & 0xFF));
            }
        }
        return ret.toString();
    }

    public String getAtqa(){
        byte[] atqaBytes = NfcA.get(tag).getAtqa();
        atqaBytes = new byte[] {atqaBytes[1], atqaBytes[0]};
        return byte2HexString(atqaBytes);
    }
}
