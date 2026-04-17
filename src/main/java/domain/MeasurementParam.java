package domain;

public enum MeasurementParam {
    PH("pH", "Водородный показатель"),
    TEMPERATURE("TEMP", "Температура"),
    CONDUCTIVITY("COND", "Электропроводность"),
    TURBIDITY("TURB", "Мутность"),
    DISSOLVED_OXYGEN("DO", "Растворенный кислород"),
    CONCENTRATION("CONC", "Концентрация");

    private final String code;
    private final String description;

    MeasurementParam(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code;
    }
}