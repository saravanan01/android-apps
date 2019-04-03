package android.sa.com.memorableplaces;


public class Place {
    private String mAddress;
    private double mLatitude = 0.0;
    private double mLongitude = 0.0;


    public Place(String mAddress, double mLatitude, double mLongitude) {
        this.mAddress = mAddress;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }
    public Place(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    @Override
    public String toString() {
        return mAddress != null ? mAddress :  String.format("%f,%f",mLatitude,mLongitude);
    }

}
