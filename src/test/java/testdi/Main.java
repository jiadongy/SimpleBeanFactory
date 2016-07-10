package testdi;

/**
 * Created by jiadongy on 16/7/10.
 */
public class Main {

    int anInt;
    double aDouble;
    String string;
    byte aByte;
    short aShort;
    float aFloat;
    char aChar;
    long aLong;
    boolean aBoolean;

    Main1 main1;
    Main2 main2;
    Main3 main3;

    public Main(int anInt, double aDouble, String string, byte aByte, short aShort, Main1 main1) {
        this.anInt = anInt;
        this.aDouble = aDouble;
        this.string = string;
        this.aByte = aByte;
        this.aShort = aShort;
        this.main1 = main1;
    }

    public Main() {
    }

    public Main factoryMethod(Main3 main3) {
        Main main = new Main();
        main.main3 = main3;
        return main;
    }

    public void init() {
        this.string = "init done";
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public byte getaByte() {
        return aByte;
    }

    public void setaByte(byte aByte) {
        this.aByte = aByte;
    }

    public short getaShort() {
        return aShort;
    }

    public void setaShort(short aShort) {
        this.aShort = aShort;
    }

    public float getaFloat() {
        return aFloat;
    }

    public void setaFloat(float aFloat) {
        this.aFloat = aFloat;
    }

    public char getaChar() {
        return aChar;
    }

    public void setaChar(char aChar) {
        this.aChar = aChar;
    }

    public long getaLong() {
        return aLong;
    }

    public void setaLong(long aLong) {
        this.aLong = aLong;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Main1 getMain1() {
        return main1;
    }

    public void setMain1(Main1 main1) {
        this.main1 = main1;
    }

    public Main2 getMain2() {
        return main2;
    }

    public void setMain2(Main2 main2) {
        this.main2 = main2;
    }
}
