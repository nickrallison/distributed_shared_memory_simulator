public enum Verbosity {
    NONE,
    LOW,
    MEDIUM,
    HIGH;

    boolean isNone() {
        return this == NONE || this == LOW || this == MEDIUM || this == HIGH;
    }

    boolean isLow() {
        return this == LOW || this == MEDIUM || this == HIGH;
    }

    boolean isMedium() {
        return this == MEDIUM || this == HIGH;
    }

    boolean isHigh() {
        return this == HIGH;
    }
}




