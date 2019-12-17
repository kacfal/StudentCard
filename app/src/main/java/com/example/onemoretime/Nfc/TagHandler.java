package com.example.onemoretime.Nfc;

import android.content.Context;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;

import com.example.onemoretime.R;

public class TagHandler {
    private Tag tag;
    private NfcA nfcA;
    private IsoDep iso;

    public TagHandler(Tag tag) {
        this.tag = tag;
        this.nfcA = NfcA.get(tag);
        this.iso = IsoDep.get(tag);
    }

    /**
     * Convert byte to hex string
     *
     * @return hex.
     */
    private static String byte2HexString(byte[] bytes) {
        StringBuilder ret = new StringBuilder();
        if (bytes != null) {
            for (Byte b : bytes) {
                ret.append(String.format("%02X", b.intValue() & 0xFF));
            }
        }
        return ret.toString();
    }

    /**
     * Get tag info
     *
     * @return The tag info.
     */
    public String getTagInfo() {
        return tag.toString();
    }

    /**
     * Get length tag ID
     *
     * @return Length ID.
     */
    public Integer getTagIDLength() {
        return tag.getId().length;
    }

    /**
     * Get tag ID
     *
     * @return ID.
     */
    public String getTagID() {
        byte[] tagId = tag.getId();
        String id = "";
        for (int i = 0; i < getTagIDLength(); i++) {
            id += Integer.toHexString(tagId[i] & 0xFF) + "";
        }
        return id;
    }

    /**
     * Get length tech list
     *
     * @return Length tech list.
     */
    public Integer getTechListLength() {
        return tag.getTechList().length;
    }

    /**
     * Get tech list
     *
     * @return Tech list.
     */
    public String getTechList() {
        String[] techList = tag.getTechList();
        String techs = "";
        for (int i = 0; i < getTechListLength(); i++) {
            techs += techList[i] + "\n ";
        }

        return techs;
    }

    /**
     * Get Describe Contents
     *
     * @return Describe Contents.
     */
    public int getDescribeContents() {
        return tag.describeContents();
    }

    /**
     * Get HashCode
     *
     * @return HashCode.
     */
    public int getHashCode() {
        return tag.hashCode();
    }

    /**
     * Get Sak
     *
     * @return Sak.
     */
    public int getSak() {
        return nfcA.getSak();
    }

    /**
     * Get ATQA
     *
     * @return ATQA.
     */
    public String getAtqa() {
        byte[] atqaBytes = NfcA.get(tag).getAtqa();
        atqaBytes = new byte[]{atqaBytes[1], atqaBytes[0]};
        return byte2HexString(atqaBytes);
    }

    /**
     * Get ATS
     * If iso is null return '-'
     *
     * @return ATS.
     */
    public String getAts() {
        String ats = "-";
        if (iso != null) {
            byte[] atsBytes = iso.getHistoricalBytes();
            if (atsBytes != null && atsBytes.length > 0) {
                ats = byte2HexString(atsBytes);
            }
        }
        return ats;
    }

    /**
     * Get (determine) the tag type resource ID from ATQA + SAK + ATS.
     * If no resource is found check for the tag type only on ATQA + SAK
     * (and then on ATQA only).
     *
     * @param atqa    The ATQA from the tag.
     * @param sak     The SAK from the tag.
     * @param ats     The ATS from the tag.
     * @param context The Context obj
     * @return The resource ID.
     */
    private int getTagIdentifier(String atqa, int sak, String ats, Context context) {
        String prefix = "tag_";
        ats = ats.replace("-", "");

        int ret = context.getResources().getIdentifier(
                prefix + atqa + sak + ats, "string", context.getPackageName());

        if (ret == 0) {
            ret = context.getResources().getIdentifier(
                    prefix + atqa + sak, "string", context.getPackageName());
        }

        if (ret == 0) {
            ret = context.getResources().getIdentifier(
                    prefix + atqa, "string", context.getPackageName());
        }

        if (ret == 0) {
            return R.string.tag_unknown;
        }
        return ret;
    }

    /**
     * Get Tag Type
     *
     * @param context The Context obj
     * @return Tag Type.
     */
    public String getTagType(Context context) {
        int tagTypeResourceID = getTagIdentifier(getAtqa(), getSak(), getAts(), context);
        String tagType;
        if (tagTypeResourceID == R.string.tag_unknown) {
            tagType = context.getString(R.string.tag_unknown_mf_classic);
        } else {
            tagType = context.getString(tagTypeResourceID);
        }
        return tagType;
    }
}
