package com.efuav.sdk.cloudapi.airsense;

/**
 * 空气传感器警告
 *
 * @author sean
 * @version 1.7
 * @date 2023/10/16
 */
public class AirsenseWarning {

    /**
     * ICAO civil aviation aircraft address
     */
    private String icao;

    /**
     * 危险程度越高，就越危险。
     * 对于大于或等于3的水平，建议飞机采取规避行动。
     */
    private WarningLevelEnum warningLevel;

    /**
     * 飞机位置的纬度是角度值。
     * 南纬为负值，北纬为正值。
     * 它精确到小数点后六位。
     */
    private Float latitude;

    /**
     * 飞机位置的经度是角度值。
     * 负值表示西经度，正值表示东经度。
     * 它精确到小数点后六位。
     */
    private Float longitude;

    /**
     * 绝对飞行高度。
     * 单位：米
     */
    private Integer altitude;

    /**
     * 绝对高度类型
     */
    private AltitudeTypeEnum altitudeType;

    /**
     * 航向的角度是角度值。
     * 0是北方。90在东边。
     * 它精确到小数点后一位。
     */
    private Float heading;

    /**
     * 飞行到飞机的相对高度。
     * 单位：米
     */
    private Integer relativeAltitude;

    /**
     * 相对高度变化趋势
     */
    private VertTrendEnum vertTrend;

    /**
     * 到飞机的水平距离。
     * 单位：米
     */
    private Integer distance;

    public AirsenseWarning() {
    }

    @Override
    public String toString() {
        return "AirsenseWarning{" +
                "icao='" + icao + '\'' +
                ", warningLevel=" + warningLevel +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", altitudeType=" + altitudeType +
                ", heading=" + heading +
                ", relativeAltitude=" + relativeAltitude +
                ", vertTrend=" + vertTrend +
                ", distance=" + distance +
                '}';
    }

    public String getIcao() {
        return icao;
    }

    public AirsenseWarning setIcao(String icao) {
        this.icao = icao;
        return this;
    }

    public WarningLevelEnum getWarningLevel() {
        return warningLevel;
    }

    public AirsenseWarning setWarningLevel(WarningLevelEnum warningLevel) {
        this.warningLevel = warningLevel;
        return this;
    }

    public Float getLatitude() {
        return latitude;
    }

    public AirsenseWarning setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public AirsenseWarning setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public AirsenseWarning setAltitude(Integer altitude) {
        this.altitude = altitude;
        return this;
    }

    public AltitudeTypeEnum getAltitudeType() {
        return altitudeType;
    }

    public AirsenseWarning setAltitudeType(AltitudeTypeEnum altitudeType) {
        this.altitudeType = altitudeType;
        return this;
    }

    public Float getHeading() {
        return heading;
    }

    public AirsenseWarning setHeading(Float heading) {
        this.heading = heading;
        return this;
    }

    public Integer getRelativeAltitude() {
        return relativeAltitude;
    }

    public AirsenseWarning setRelativeAltitude(Integer relativeAltitude) {
        this.relativeAltitude = relativeAltitude;
        return this;
    }

    public VertTrendEnum getVertTrend() {
        return vertTrend;
    }

    public AirsenseWarning setVertTrend(VertTrendEnum vertTrend) {
        this.vertTrend = vertTrend;
        return this;
    }

    public Integer getDistance() {
        return distance;
    }

    public AirsenseWarning setDistance(Integer distance) {
        this.distance = distance;
        return this;
    }
}
