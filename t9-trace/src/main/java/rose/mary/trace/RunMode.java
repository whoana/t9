package rose.mary.trace;

public enum RunMode {

    Recovery("recovery"), Server("server"), Distributor("distributor");

    private RunMode() {

    }

    String name;

    private RunMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
