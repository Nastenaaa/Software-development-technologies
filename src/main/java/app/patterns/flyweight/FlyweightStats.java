package app.patterns.flyweight;

public final class FlyweightStats {
    private FlyweightStats() {}
    public static String snapshot() {
        return "Flyweights -> accounts: " + AccountFlyweightFactory.size()
                + ", categories: " + CategoryFlyweightFactory.size();
    }
}

