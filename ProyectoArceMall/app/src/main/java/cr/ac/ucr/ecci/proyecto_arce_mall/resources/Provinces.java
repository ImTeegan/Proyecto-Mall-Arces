package cr.ac.ucr.ecci.proyecto_arce_mall.resources;

public enum Provinces {
    SANJOSE("San José", 9.93333, -84.08333),
    HEREDIA("Heredia", 10.00236, -84.11651),
    ALAJUELA("Alajuela", 10.01652, -84.21163),
    CARTAGO("Cartago", 9.86444, -83.91944),
    PUNTARENAS("Puntarenas", 9.97625, -84.83836),
    LIMON("Limon", 9.99074, -83.03596),
    GUANACASTE("Guanacaste", 10.630573, -85.439346);

    private String name;
    private double latitude;
    private double longitude;

    private Provinces(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName(){
        return name;
    }

    public Double getLatitude(){
        return latitude;
    }

    public Double getLongitude(){
        return longitude;
    }
}
